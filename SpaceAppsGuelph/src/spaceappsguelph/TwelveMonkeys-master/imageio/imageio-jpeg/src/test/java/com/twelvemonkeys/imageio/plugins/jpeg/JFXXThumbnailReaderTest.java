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

import com.twelvemonkeys.imageio.metadata.jpeg.JPEG;
import com.twelvemonkeys.imageio.metadata.jpeg.JPEGSegment;
import com.twelvemonkeys.imageio.metadata.jpeg.JPEGSegmentUtil;
import org.junit.Test;
import org.mockito.InOrder;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

/**
 * JFXXThumbnailReaderTest
 *
 * @author <a href="mailto:harald.kuhr@gmail.com">Harald Kuhr</a>
 * @author last modified by $Author: haraldk$
 * @version $Id: JFXXThumbnailReaderTest.java,v 1.0 04.05.12 15:56 haraldk Exp$
 */
public class JFXXThumbnailReaderTest extends AbstractThumbnailReaderTest {
    @Override
    protected JFXXThumbnailReader createReader(ThumbnailReadProgressListener progressListener, int imageIndex, int thumbnailIndex, ImageInputStream stream) throws IOException {
        List<JPEGSegment> segments = JPEGSegmentUtil.readSegments(stream, JPEG.APP0, "JFXX");
        stream.close();

        assertNotNull(segments);
        assertFalse(segments.isEmpty());

        JPEGSegment jfxx = segments.get(0);
        return new JFXXThumbnailReader(progressListener, ImageIO.getImageReadersByFormatName("jpeg").next(), imageIndex, thumbnailIndex, JFXX.read(new DataInputStream(jfxx.segmentData()), jfxx.length()));
    }

    @Test
    public void testReadJPEG() throws IOException {
        ThumbnailReader reader = createReader(mock(ThumbnailReadProgressListener.class), 0, 0, createStream("/jpeg/jfif-jfxx-thumbnail-olympus-d320l.jpg"));

        assertEquals(80, reader.getWidth());
        assertEquals(60, reader.getHeight());

        BufferedImage thumbnail = reader.read();
        assertNotNull(thumbnail);
        assertEquals(80, thumbnail.getWidth());
        assertEquals(60, thumbnail.getHeight());
    }

    // TODO: Test JFXX indexed thumbnail
    // TODO: Test JFXX RGB thumbnail

    @Test
    public void testProgressListenerRaw() throws IOException {
        ThumbnailReadProgressListener listener = mock(ThumbnailReadProgressListener.class);

        createReader(listener, 0, 99, createStream("/jpeg/jfif-jfxx-thumbnail-olympus-d320l.jpg")).read();

        InOrder order = inOrder(listener);
        order.verify(listener).processThumbnailStarted(0, 99);
        order.verify(listener, atLeastOnce()).processThumbnailProgress(100f);
        order.verify(listener).processThumbnailComplete();
    }
}
