/*
 * Copyright (c) 2012, Harald Kuhr
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name "TwelveMonkeys" nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.twelvemonkeys.imageio.plugins.jpeg;

import com.twelvemonkeys.imageio.metadata.CompoundDirectory;
import com.twelvemonkeys.imageio.metadata.jpeg.JPEG;
import com.twelvemonkeys.imageio.metadata.jpeg.JPEGSegment;
import com.twelvemonkeys.imageio.metadata.jpeg.JPEGSegmentUtil;
import com.twelvemonkeys.imageio.metadata.tiff.TIFFReader;
import org.junit.Test;
import org.mockito.InOrder;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * EXIFThumbnailReaderTest
 *
 * @author <a href="mailto:harald.kuhr@gmail.com">Harald Kuhr</a>
 * @author last modified by $Author: haraldk$
 * @version $Id: EXIFThumbnailReaderTest.java,v 1.0 04.05.12 15:55 haraldk Exp$
 */
public class EXIFThumbnailReaderTest extends AbstractThumbnailReaderTest {

    @Override
    protected EXIFThumbnailReader createReader(final ThumbnailReadProgressListener progressListener, final int imageIndex, final int thumbnailIndex, final ImageInputStream stream) throws IOException {
        List<JPEGSegment> segments = JPEGSegmentUtil.readSegments(stream, JPEG.APP1, "Exif");
        stream.close();

        assertNotNull(segments);
        assertFalse(segments.isEmpty());

        TIFFReader reader = new TIFFReader();
        InputStream data = segments.get(0).data();
        if (data.read() < 0) {
            throw new AssertionError("EOF!");
        }

        ImageInputStream exifStream = ImageIO.createImageInputStream(data);
        CompoundDirectory ifds = (CompoundDirectory) reader.read(exifStream);

        assertEquals(2, ifds.directoryCount());

        return new EXIFThumbnailReader(progressListener, ImageIO.getImageReadersByFormatName("JPEG").next(), imageIndex, thumbnailIndex, ifds.getDirectory(1), exifStream);
    }

    @Test
    public void testReadJPEG() throws IOException {
        ThumbnailReader reader = createReader(mock(ThumbnailReadProgressListener.class), 0, 0, createStream("/jpeg/cmyk-sample-multiple-chunk-icc.jpg"));

        assertEquals(114, reader.getWidth());
        assertEquals(160, reader.getHeight());

        BufferedImage thumbnail = reader.read();
        assertNotNull(thumbnail);
        assertEquals(114, thumbnail.getWidth());
        assertEquals(160, thumbnail.getHeight());
    }

    @Test
    public void testReadRaw() throws IOException {
        ThumbnailReader reader = createReader(mock(ThumbnailReadProgressListener.class), 0, 0, createStream("/jpeg/exif-rgb-thumbnail-sony-d700.jpg"));

        assertEquals(80, reader.getWidth());
        assertEquals(60, reader.getHeight());

        BufferedImage thumbnail = reader.read();
        assertNotNull(thumbnail);
        assertEquals(80, thumbnail.getWidth());
        assertEquals(60, thumbnail.getHeight());
    }

    @Test
    public void testProgressListenerJPEG() throws IOException {
        ThumbnailReadProgressListener listener = mock(ThumbnailReadProgressListener.class);

        createReader(listener, 42, 43, createStream("/jpeg/cmyk-sample-multiple-chunk-icc.jpg")).read();

        InOrder order = inOrder(listener);
        order.verify(listener).processThumbnailStarted(42, 43);
        order.verify(listener, atLeastOnce()).processThumbnailProgress(100f);
        order.verify(listener).processThumbnailComplete();
    }

    @Test
    public void testProgressListenerRaw() throws IOException {
        ThumbnailReadProgressListener listener = mock(ThumbnailReadProgressListener.class);

        createReader(listener, 0, 99, createStream("/jpeg/exif-rgb-thumbnail-sony-d700.jpg")).read();

        InOrder order = inOrder(listener);
        order.verify(listener).processThumbnailStarted(0, 99);
        order.verify(listener, atLeastOnce()).processThumbnailProgress(100f);
        order.verify(listener).processThumbnailComplete();
    }
}
