/*
 * Copyright (c) 2013, Harald Kuhr
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

package com.twelvemonkeys.imageio.plugins.tiff;

import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * CCITTFaxDecoderStreamTest
 *
 * @author <a href="mailto:harald.kuhr@gmail.com">Harald Kuhr</a>
 * @author last modified by $Author: haraldk$
 * @version $Id: CCITTFaxDecoderStreamTest.java,v 1.0 09.03.13 14:44 haraldk
 *          Exp$
 */
public class CCITTFaxDecoderStreamTest {

    // group3_1d.tif: EOL|3W|1B|2W|EOL|3W|1B|2W|EOL|3W|1B|2W|EOL|2W|2B|2W|5*F
    static final byte[] DATA_G3_1D = { 0x00, 0x18, 0x4E, 0x00, 0x30, (byte) 0x9C, 0x00, 0x61, 0x38, 0x00, (byte) 0xBE,
            (byte) 0xE0 };

    // group3_1d_fill.tif
    static final byte[] DATA_G3_1D_FILL = { 0x00, 0x01, (byte) 0x84, (byte) 0xE0, 0x01, (byte) 0x84, (byte) 0xE0, 0x01,
            (byte) 0x84, (byte) 0xE0, 0x1, 0x7D, (byte) 0xC0 };

    // group3_2d.tif: EOL|k=1|3W|1B|2W|EOL|k=0|V|V|V|EOL|k=1|3W|1B|2W|EOL|k=0|V-1|V|V|6*F
    static final byte[] DATA_G3_2D = { 0x00, 0x1C, 0x27, 0x00, 0x17, 0x00, 0x1C, 0x27, 0x00, 0x12, (byte) 0xC0 };

    // group3_2d_fill.tif
    static final byte[] DATA_G3_2D_FILL = { 0x00, 0x01, (byte) 0xC2, 0x70, 0x01, 0x70, 0x01, (byte) 0xC2, 0x70, 0x01,
            0x2C };

    static final byte[] DATA_G3_2D_lsb2msb = { 0x00, 0x38, (byte) 0xE4, 0x00, (byte) 0xE8, 0x00, 0x38, (byte) 0xE4,
            0x00, 0x48, 0x03 };

    // group4.tif:
    // Line 1: V-3, V-2, V0
    // Line 2: V0 V0 V0
    // Line 3: V0 V0 V0
    // Line 4: V-1, V0, V0 EOL EOL
    static final byte[] DATA_G4 = { 0x04, 0x17, (byte) 0xF5, (byte) 0x80, 0x08, 0x00, (byte) 0x80 };

    // TODO: Better tests (full A4 width scan lines?)

    // From http://www.mikekohn.net/file_formats/tiff.php
    static final byte[] DATA_TYPE_2 = { (byte) 0x84, (byte) 0xe0, // 10000100
                                                                  // 11100000
            (byte) 0x84, (byte) 0xe0, // 10000100 11100000
            (byte) 0x84, (byte) 0xe0, // 10000100 11100000
            (byte) 0x7d, (byte) 0xc0, // 01111101 11000000
    };

    static final byte[] DATA_TYPE_3 = { 0x00, 0x01, (byte) 0xc2, 0x70, // 00000000
                                                                       // 00000001
                                                                       // 11000010
                                                                       // 01110000
            0x00, 0x01, 0x78, // 00000000 00000001 01111000
            0x00, 0x01, 0x78, // 00000000 00000001 01110000
            0x00, 0x01, 0x56, // 00000000 00000001 01010110
            // 0x01, // 00000001

    };

    // 001 00110101 10 000010 1 1 1 1 1 1 1 1 1 1 010 11 (000000 padding)
    static final byte[] DATA_TYPE_4 = { 0x26, // 001 00110
            (byte) 0xb0, // 101 10 000
            0x5f, // 010 1 1 1 1 1
            (byte) 0xfa, // 1 1 1 1 1 010
            (byte) 0xc0 // 11 (000000 padding)
    };

    // Image should be (6 x 4):
    // 1 1 1 0 1 1 x x
    // 1 1 1 0 1 1 x x
    // 1 1 1 0 1 1 x x
    // 1 1 0 0 1 1 x x
    final BufferedImage image = new BufferedImage(6, 4, BufferedImage.TYPE_BYTE_BINARY);;

    @Before
    public void init() {

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 6; x++) {
                image.setRGB(x, y, x != 3 ? 0xff000000 : 0xffffffff);
            }
        }

        image.setRGB(2, 3, 0xffffffff);
    }

    @Test
    public void testDecodeType2() throws IOException {
        InputStream stream = new CCITTFaxDecoderStream(new ByteArrayInputStream(DATA_TYPE_2), 6,
                TIFFBaseline.COMPRESSION_CCITT_MODIFIED_HUFFMAN_RLE, 1, 0L);

        byte[] imageData = ((DataBufferByte) image.getData().getDataBuffer()).getData();
        byte[] bytes = new byte[imageData.length];
        new DataInputStream(stream).readFully(bytes);
        assertArrayEquals(imageData, bytes);
    }

    @Test
    public void testDecodeType3_1D() throws IOException {
        InputStream stream = new CCITTFaxDecoderStream(new ByteArrayInputStream(DATA_G3_1D), 6,
                TIFFExtension.COMPRESSION_CCITT_T4, 1, 0L);

        byte[] imageData = ((DataBufferByte) image.getData().getDataBuffer()).getData();
        byte[] bytes = new byte[imageData.length];
        new DataInputStream(stream).readFully(bytes);
        assertArrayEquals(imageData, bytes);
    }

    @Test
    public void testDecodeType3_1D_FILL() throws IOException {
        InputStream stream = new CCITTFaxDecoderStream(new ByteArrayInputStream(DATA_G3_1D_FILL), 6,
                TIFFExtension.COMPRESSION_CCITT_T4, 1, TIFFExtension.GROUP3OPT_FILLBITS);

        byte[] imageData = ((DataBufferByte) image.getData().getDataBuffer()).getData();
        byte[] bytes = new byte[imageData.length];
        new DataInputStream(stream).readFully(bytes);
        assertArrayEquals(imageData, bytes);
    }

    @Test
    public void testDecodeType3_2D() throws IOException {
        InputStream stream = new CCITTFaxDecoderStream(new ByteArrayInputStream(DATA_G3_2D), 6,
                TIFFExtension.COMPRESSION_CCITT_T4, 1, TIFFExtension.GROUP3OPT_2DENCODING);

        byte[] imageData = ((DataBufferByte) image.getData().getDataBuffer()).getData();
        byte[] bytes = new byte[imageData.length];
        new DataInputStream(stream).readFully(bytes);
        assertArrayEquals(imageData, bytes);
    }

    @Test
    public void testDecodeType3_2D_FILL() throws IOException {
        InputStream stream = new CCITTFaxDecoderStream(new ByteArrayInputStream(DATA_G3_2D_FILL), 6,
                TIFFExtension.COMPRESSION_CCITT_T4, 1,
                TIFFExtension.GROUP3OPT_2DENCODING | TIFFExtension.GROUP3OPT_FILLBITS);

        byte[] imageData = ((DataBufferByte) image.getData().getDataBuffer()).getData();
        byte[] bytes = new byte[imageData.length];
        new DataInputStream(stream).readFully(bytes);
        assertArrayEquals(imageData, bytes);
    }

    @Test
    public void testDecodeType3_2D_REVERSED() throws IOException {
        InputStream stream = new CCITTFaxDecoderStream(new ByteArrayInputStream(DATA_G3_2D_lsb2msb), 6,
                TIFFExtension.COMPRESSION_CCITT_T4, 2, TIFFExtension.GROUP3OPT_2DENCODING);

        byte[] imageData = ((DataBufferByte) image.getData().getDataBuffer()).getData();
        byte[] bytes = new byte[imageData.length];
        new DataInputStream(stream).readFully(bytes);
        assertArrayEquals(imageData, bytes);
    }

    @Test
    public void testDecodeType4() throws IOException {
        InputStream stream = new CCITTFaxDecoderStream(new ByteArrayInputStream(DATA_G4), 6,
                TIFFExtension.COMPRESSION_CCITT_T6, 1, 0L);

        byte[] imageData = ((DataBufferByte) image.getData().getDataBuffer()).getData();
        byte[] bytes = new byte[imageData.length];
        new DataInputStream(stream).readFully(bytes);
        assertArrayEquals(imageData, bytes);
    }

    @Test
    public void testDecodeMissingRows() throws IOException {
        // See https://github.com/haraldk/TwelveMonkeys/pull/225 and https://github.com/haraldk/TwelveMonkeys/issues/232
        InputStream inputStream = getClass().getResourceAsStream("/tiff/ccitt_tolessrows.tif");

        // Skip until StripOffsets: 8
        for (int i = 0; i < 8; i++) {
            inputStream.read();
        }

        // Read until StripByteCounts: 7
        byte[] data = new byte[7];
        new DataInputStream(inputStream).readFully(data);

        InputStream stream = new CCITTFaxDecoderStream(new ByteArrayInputStream(data),
                6, TIFFExtension.COMPRESSION_CCITT_T6, 1, 0L);

        byte[] bytes = new byte[6]; // 6 x 6 pixel, 1 bpp => 6 bytes
        new DataInputStream(stream).readFully(bytes);

        // Pad image data with 0s
        byte[] imageData = Arrays.copyOf(((DataBufferByte) image.getData().getDataBuffer()).getData(), 6);
        assertArrayEquals(imageData, bytes);

        // Ideally, we should have no more data now, but the stream don't know that...
        // assertEquals("Should contain no more data", -1, stream.read());
    }

    @Test
    public void testMoreChangesThanColumns() throws IOException {
        // Produces an CCITT Stream with 9 changes on 8 columns.
        byte[] data = new byte[] {(byte) 0b10101010};
        ByteArrayOutputStream imageOutput = new ByteArrayOutputStream();
        OutputStream outputSteam = new CCITTFaxEncoderStream(imageOutput,
                8, 1, TIFFExtension.COMPRESSION_CCITT_T6, 1, 0L);
        outputSteam.write(data);
        outputSteam.close();

        byte[] encoded = imageOutput.toByteArray();
        InputStream inputStream = new CCITTFaxDecoderStream(new ByteArrayInputStream(encoded), 8,
                TIFFExtension.COMPRESSION_CCITT_T6, 1, 0L);
        byte decoded = (byte) inputStream.read();
        assertEquals(data[0], decoded);
    }

    @Test
    public void testMoreChangesThanColumnsFile() throws IOException {
        // See https://github.com/haraldk/TwelveMonkeys/issues/328
        // 26 changes on 24 columns: H0w1b, H1w1b, ..., H1w0b
        InputStream stream = getClass().getResourceAsStream("/tiff/ccitt-too-many-changes.tif");

        // Skip bytes before StripOffsets: 86
        for (int i = 0; i < 86; i++) {
            stream.read();
        }

        InputStream inputStream = new CCITTFaxDecoderStream(stream,
                24, TIFFExtension.COMPRESSION_CCITT_T6, 1, 0L);
        byte decoded = (byte) inputStream.read();
        assertEquals((byte) 0b10101010, decoded);
    }
}
