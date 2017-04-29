package com.twelvemonkeys.imageio.plugins.bmp;

import com.twelvemonkeys.imageio.util.ImageReaderAbstractTest;
import org.junit.Ignore;
import org.junit.Test;

import javax.imageio.ImageReadParam;
import javax.imageio.spi.ImageReaderSpi;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * CURImageReaderTest
 *
 * @author <a href="mailto:harald.kuhr@gmail.com">Harald Kuhr</a>
 * @author last modified by $Author: haraldk$
 * @version $Id: CURImageReaderTest.java,v 1.0 Apr 1, 2008 10:39:17 PM haraldk Exp$
 */
public class CURImageReaderTest extends ImageReaderAbstractTest<CURImageReader> {
    protected List<TestData> getTestData() {
        return Arrays.asList(
                new TestData(getClassLoaderResource("/cur/hand.cur"), new Dimension(32, 32)),
                new TestData(getClassLoaderResource("/cur/zoom.cur"), new Dimension(32, 32))
        );
    }

    protected ImageReaderSpi createProvider() {
        return new CURImageReaderSpi();
    }

    @Override
    protected CURImageReader createReader() {
        return new CURImageReader();
    }

    protected Class<CURImageReader> getReaderClass() {
        return CURImageReader.class;
    }

    protected List<String> getFormatNames() {
        return Collections.singletonList("cur");
    }

    protected List<String> getSuffixes() {
        return Collections.singletonList("cur");
    }

    protected List<String> getMIMETypes() {
        return Arrays.asList("image/vnd.microsoft.cursor", "image/cursor", "image/x-cursor");
    }

    private void assertHotSpot(final TestData pTestData, final ImageReadParam pParam, final Point pExpected) throws IOException {
        CURImageReader reader = createReader();
        reader.setInput(pTestData.getInputStream());

        BufferedImage image = reader.read(0, pParam);

        // We can only be sure the hotspot is defined, if no param, but if defined, it must be correct
        Object hotspot = image.getProperty("cursor_hotspot");
        if (hotspot != Image.UndefinedProperty || pParam == null) {

            // Typically never happens, because of weirdness with UndefinedProperty
            assertNotNull("Hotspot for cursor not present", hotspot);

            // Image weirdness
            assertTrue("Hotspot for cursor undefined (java.awt.Image.UndefinedProperty)", Image.UndefinedProperty != hotspot);

            assertTrue(String.format("Hotspot not a java.awt.Point: %s", hotspot.getClass()), hotspot instanceof Point);
            assertEquals(pExpected, hotspot);
        }

        assertNotNull("Hotspot for cursor not present", reader.getHotSpot(0));
        assertEquals(pExpected, reader.getHotSpot(0));
    }

    @Test
    public void testHandHotspot() throws IOException {
        assertHotSpot(getTestData().get(0), null, new Point(15, 15));
    }

    @Test
    public void testZoomHotspot() throws IOException {
        assertHotSpot(getTestData().get(1), null, new Point(13, 11));
    }

    @Test
    public void testHandHotspotWithParam() throws IOException {
        ImageReadParam param = new ImageReadParam();
        assertHotSpot(getTestData().get(0), param, new Point(15, 15));
    }

    @Test
    public void testHandHotspotExplicitDestination() throws IOException {
        CURImageReader reader = createReader();
        reader.setInput(getTestData().get(0).getInputStream());
        BufferedImage image = reader.read(0);

        // Create dest image with same data, except properties...
        BufferedImage dest = new BufferedImage(
                image.getColorModel(), image.getRaster(), image.getColorModel().isAlphaPremultiplied(), null
        );
        ImageReadParam param = new ImageReadParam();
        param.setDestination(dest);

        assertHotSpot(getTestData().get(0), param, new Point(15, 15));
    }

    // TODO: Test cursor is transparent

    @Test
    @Ignore("Known issue")
    @Override
    public void testNotBadCaching() throws IOException {
        super.testNotBadCaching();
    }

    @Test
    @Ignore("Known issue: Subsampled reading currently not supported")
    @Override
    public void testReadWithSubsampleParamPixels() throws IOException {
        super.testReadWithSubsampleParamPixels();
    }
}