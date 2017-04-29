package com.twelvemonkeys.imageio.path;

import com.twelvemonkeys.imageio.stream.ByteArrayImageInputStream;
import com.twelvemonkeys.imageio.stream.SubImageInputStream;
import com.twelvemonkeys.imageio.stream.URLImageInputStreamSpi;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;

import static org.junit.Assert.*;

/**
 * PathsTest.
 *
 * @author <a href="mailto:harald.kuhr@gmail.com">Harald Kuhr</a>
 * @author last modified by $Author: harald.kuhr$
 * @version $Id: PathsTest.java,v 1.0 12/12/14 harald.kuhr Exp$
 */
public class PathsTest {
    static {
        IIORegistry.getDefaultInstance().registerServiceProvider(new URLImageInputStreamSpi());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadPathNull() throws IOException {
        Paths.readPath(null);
    }

    @Test
    public void testReadPathUnknown() throws IOException {
        assertNull(Paths.readPath(new ByteArrayImageInputStream(new byte[42])));
    }

    @Test
    public void testGrapeJPEG() throws IOException {
        ImageInputStream stream = resourceAsIIOStream("/jpeg/grape_with_path.jpg");

        Path2D path = Paths.readPath(stream);

        assertNotNull(path);
        assertPathEquals(readExpectedPath("/ser/grape-path.ser"), path);
    }

    @Test
    public void testGrapePSD() throws IOException {
        ImageInputStream stream = resourceAsIIOStream("/psd/grape_with_path.psd");

        Path2D path = Paths.readPath(stream);

        assertNotNull(path);
        assertPathEquals(readExpectedPath("/ser/grape-path.ser"), path);

    }

    @Test
    public void testGrapeTIFF() throws IOException {
        ImageInputStream stream = resourceAsIIOStream("/tiff/little-endian-grape_with_path.tif");

        Path2D path = Paths.readPath(stream);

        assertNotNull(path);
        assertPathEquals(readExpectedPath("/ser/grape-path.ser"), path);
    }

    @Test
    public void testMultipleTIFF() throws IOException {
        ImageInputStream stream = resourceAsIIOStream("/tiff/big-endian-multiple-clips.tif");

        Shape path = Paths.readPath(stream);

        assertNotNull(path);
    }

    @Test
    public void testGrape8BIM() throws IOException {
        ImageInputStream stream = resourceAsIIOStream("/psd/grape_with_path.psd");

        // PSD image resources from position 34, length 32598
        stream.seek(34);
        stream = new SubImageInputStream(stream, 32598);

        Path2D path = Paths.readPath(stream);

        assertNotNull(path);
        assertPathEquals(readExpectedPath("/ser/grape-path.ser"), path);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyClippingPathNullPath() throws IOException {
        Paths.applyClippingPath(null, new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_GRAY));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyClippingPathNullSource() throws IOException {
        Paths.applyClippingPath(new GeneralPath(), null);
    }

    @Test
    public void testApplyClippingPath() throws IOException {
        BufferedImage source = new BufferedImage(20, 20, BufferedImage.TYPE_3BYTE_BGR);

        Path2D path = readExpectedPath("/ser/grape-path.ser");

        BufferedImage image = Paths.applyClippingPath(path, source);

        assertNotNull(image);
        // Same dimensions as original
        assertEquals(source.getWidth(), image.getWidth());
        assertEquals(source.getHeight(), image.getHeight());
        // Transparent
        assertTrue(image.getColorModel().getTransparency() == Transparency.TRANSLUCENT);

        // Corners (at least) should be transparent
        assertEquals(0, image.getRGB(0, 0));
        assertEquals(0, image.getRGB(source.getWidth() - 1, 0));
        assertEquals(0, image.getRGB(0, source.getHeight() - 1));
        assertEquals(0, image.getRGB(source.getWidth() - 1, source.getHeight() - 1));

        // Center opaque
        assertEquals(0xff, image.getRGB(source.getWidth() / 2, source.getHeight() / 2) >>> 24);

        // TODO: Mor sophisticated test that tests all pixels outside path...
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyClippingPathNullDestination() throws IOException {
        Paths.applyClippingPath(new GeneralPath(), new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_GRAY), null);
    }

    @Test
    public void testApplyClippingPathCustomDestination() throws IOException {
        BufferedImage source = new BufferedImage(20, 20, BufferedImage.TYPE_3BYTE_BGR);

        Path2D path = readExpectedPath("/ser/grape-path.ser");

        // Destination is intentionally larger than source
        BufferedImage destination = new BufferedImage(30, 30, BufferedImage.TYPE_4BYTE_ABGR);
        BufferedImage image = Paths.applyClippingPath(path, source, destination);

        assertSame(destination, image);

        // Corners (at least) should be transparent
        assertEquals(0, image.getRGB(0, 0));
        assertEquals(0, image.getRGB(image.getWidth() - 1, 0));
        assertEquals(0, image.getRGB(0, image.getHeight() - 1));
        assertEquals(0, image.getRGB(image.getWidth() - 1, image.getHeight() - 1));

        // "inner" corners
        assertEquals(0, image.getRGB(source.getWidth() - 1, 0));
        assertEquals(0, image.getRGB(0, source.getHeight() - 1));
        assertEquals(0, image.getRGB(source.getWidth() - 1, source.getHeight() - 1));

        // Center opaque
        assertEquals(0xff, image.getRGB(source.getWidth() / 2, source.getHeight() / 2) >>> 24);

        // TODO: Mor sophisticated test that tests all pixels outside path...
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadClippedNull() throws IOException {
        Paths.readClipped(null);
    }

    @Test
    public void testReadClipped() throws IOException {
        BufferedImage image = Paths.readClipped(resourceAsIIOStream("/jpeg/grape_with_path.jpg"));

        assertNotNull(image);
        // Same dimensions as original
        assertEquals(857, image.getWidth());
        assertEquals(1800, image.getHeight());
        // Transparent
        assertTrue(image.getColorModel().getTransparency() == Transparency.TRANSLUCENT);

        // Corners (at least) should be transparent
        assertEquals(0, image.getRGB(0, 0));
        assertEquals(0, image.getRGB(image.getWidth() - 1, 0));
        assertEquals(0, image.getRGB(0, image.getHeight() - 1));
        assertEquals(0, image.getRGB(image.getWidth() - 1, image.getHeight() - 1));

        // Center opaque
        assertEquals(0xff, image.getRGB(image.getWidth() / 2, image.getHeight() / 2) >>> 24);

        // TODO: Mor sophisticated test that tests all pixels outside path...
    }

    // TODO: Test read image without path, as no-op

    static ImageInputStream resourceAsIIOStream(String name) throws IOException {
        return ImageIO.createImageInputStream(PathsTest.class.getResource(name));
    }

    static Path2D readExpectedPath(final String resource) throws IOException {
        ObjectInputStream ois = new ObjectInputStream(PathsTest.class.getResourceAsStream(resource));

        try {
            return (Path2D) ois.readObject();
        }
        catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
        finally {
            ois.close();
        }
    }

    static void assertPathEquals(final Path2D expectedPath, final Path2D actualPath) {
        PathIterator expectedIterator = expectedPath.getPathIterator(null);
        PathIterator actualIterator = actualPath.getPathIterator(null);
        float[] expectedCoords = new float[6];
        float[] actualCoords = new float[6];

        while(!actualIterator.isDone()) {
            int expectedType = expectedIterator.currentSegment(expectedCoords);
            int actualType = actualIterator.currentSegment(actualCoords);

            assertEquals(expectedType, actualType);
            assertArrayEquals(expectedCoords, actualCoords, 0);

            actualIterator.next();
            expectedIterator.next();
        }
    }
}
