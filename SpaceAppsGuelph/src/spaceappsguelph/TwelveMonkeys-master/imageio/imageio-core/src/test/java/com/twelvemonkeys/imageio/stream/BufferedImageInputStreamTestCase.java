package com.twelvemonkeys.imageio.stream;

import com.twelvemonkeys.io.ole2.CompoundDocument;
import com.twelvemonkeys.io.ole2.Entry;
import junit.framework.TestCase;

import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Random;

/**
 * BufferedImageInputStreamTestCase
 *
 * @author <a href="mailto:harald.kuhr@gmail.com">Harald Kuhr</a>
 * @author last modified by $Author: haraldk$
 * @version $Id: BufferedImageInputStreamTestCase.java,v 1.0 Jun 30, 2008 3:07:42 PM haraldk Exp$
 */
public class BufferedImageInputStreamTestCase extends TestCase {
    protected final Random random = new Random();

    public void testCreate() throws IOException {
        new BufferedImageInputStream(new ByteArrayImageInputStream(new byte[0]));
    }

    public void testCreateNull() throws IOException {
        try {
            new BufferedImageInputStream(null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException expected) {
            assertNotNull("Null exception message", expected.getMessage());
            String message = expected.getMessage().toLowerCase();
            assertTrue("Exception message does not contain parameter name", message.contains("stream"));
            assertTrue("Exception message does not contain null", message.contains("null"));
        }
    }

    // TODO: Write other tests

    // TODO: Create test that exposes read += -1 (eof) bug

    public void testArrayIndexOutOfBoundsBufferedReadBug() throws IOException {
        // TODO: Create a more straight forward way to prove correctness, for now this is good enough to avoid regression
        ImageInputStream input = new BufferedImageInputStream(new MemoryCacheImageInputStream(getClass().getResourceAsStream("/Thumbs-camera.db")));
        input.setByteOrder(ByteOrder.LITTLE_ENDIAN);
        Entry root = new CompoundDocument(input).getRootEntry();

        Entry child = root.getChildEntry("Catalog");

        assertNotNull("Input stream can never be null", child.getInputStream());
    }

    public void testReadResetReadDirectBufferBug() throws IOException {
        // Make sure we use the exact size of the buffer
        final int size = BufferedImageInputStream.DEFAULT_BUFFER_SIZE;

        // Fill bytes
        byte[] bytes = new byte[size * 2];
        random.nextBytes(bytes);

        // Create wrapper stream
        BufferedImageInputStream stream = new BufferedImageInputStream(new ByteArrayImageInputStream(bytes));

        // Read to fill the buffer, then reset
        stream.readLong();
        stream.seek(0);

        // Read fully and compare
        byte[] result = new byte[size];
        stream.readFully(result);
        assertTrue(rangeEquals(bytes, 0, result, 0, size));

        stream.readFully(result);
        assertTrue(rangeEquals(bytes, size, result, 0, size));
    }

    public void testBufferPositionCorrect() throws IOException {
        // Fill bytes
        byte[] bytes = new byte[1024];
        random.nextBytes(bytes);

        ByteArrayImageInputStream input = new ByteArrayImageInputStream(bytes);

        input.readByte();
        input.readByte();
        input.skipBytes(124);
        input.readByte();
        input.readByte();

        // Sanity check
        assertEquals(128, input.getStreamPosition());

        BufferedImageInputStream stream = new BufferedImageInputStream(input);

        assertEquals(input.getStreamPosition(), stream.getStreamPosition());

        stream.skipBytes(128);

        //assertTrue(256 <= input.getStreamPosition());
        assertEquals(256, stream.getStreamPosition());
        
        stream.seek(1020);
        assertEquals(1020, stream.getStreamPosition());
    }

    /**
     * Test two arrays for range equality. That is, they contain the same elements for some specified range.
     *
     * @param pFirst one array to test for equality
     * @param pFirstOffset the offset into the first array to start testing for equality
     * @param pSecond the other array to test for equality
     * @param pSecondOffset the offset into the second array to start testing for equality
     * @param pLength the length of the range to check for equality
     *
     * @return {@code true} if both arrays are non-{@code null}
     * and have at least {@code offset + pLength} elements
     * and all elements in the range from the first array is equal to the elements from the second array,
     * or if {@code pFirst == pSecond} (including both arrays being {@code null})
     * and {@code pFirstOffset == pSecondOffset}.
     * Otherwise {@code false}.
     */
    static boolean rangeEquals(byte[] pFirst, int pFirstOffset, byte[] pSecond, int pSecondOffset, int pLength) {
        if (pFirst == pSecond && pFirstOffset == pSecondOffset) {
            return true;
        }

        if (pFirst == null || pSecond == null) {
            return false;
        }

        if (pFirst.length < pFirstOffset + pLength || pSecond.length < pSecondOffset + pLength) {
            return false;
        }

        for (int i = 0; i < pLength; i++) {
            if (pFirst[pFirstOffset + i] != pSecond[pSecondOffset + i]) {
                return false;
            }
        }

        return true;
    }
}
