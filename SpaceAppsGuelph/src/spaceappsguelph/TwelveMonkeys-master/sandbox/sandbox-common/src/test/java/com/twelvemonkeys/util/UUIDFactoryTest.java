package com.twelvemonkeys.util;

import org.junit.Ignore;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class UUIDFactoryTest {
    private static final String EXAMPLE_COM_UUID = "http://www.example.com/uuid/";

    // Nil UUID

    @Test
    public void testNilUUIDVariant() {
        assertEquals(0, UUIDFactory.NIL.variant());
    }
    @Test
    public void testNilUUIDVersion() {
        assertEquals(0, UUIDFactory.NIL.version());
    }

    @Test
    public void testNilUUIDFromStringRep() {
        assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), UUIDFactory.NIL);
    }

    @Test
    public void testNilUUIDFromLong() {
        assertEquals(new UUID(0l, 0l), UUIDFactory.NIL);
    }

    // Version 3 UUIDs (for comparison with v5)

    @Test
    public void testVersion3NameBasedMD5Variant() throws UnsupportedEncodingException {
        assertEquals(2, UUID.nameUUIDFromBytes(EXAMPLE_COM_UUID.getBytes("UTF-8")).variant());
    }
    @Test
    public void testVersion3NameBasedMD5Version() throws UnsupportedEncodingException {
        assertEquals(3, UUID.nameUUIDFromBytes(EXAMPLE_COM_UUID.getBytes("UTF-8")).version());
    }

    @Test
    public void testVersion3NameBasedMD5Equals() throws UnsupportedEncodingException {
        UUID a = UUID.nameUUIDFromBytes(EXAMPLE_COM_UUID.getBytes("UTF-8"));
        UUID b = UUID.nameUUIDFromBytes(EXAMPLE_COM_UUID.getBytes("UTF-8"));
        assertEquals(a, b);
    }

    @Test
    public void testVersion3NameBasedMD5NotEqualSHA1() throws UnsupportedEncodingException {
        UUID a = UUID.nameUUIDFromBytes(EXAMPLE_COM_UUID.getBytes("UTF-8"));
        UUID b = UUIDFactory.nameUUIDv5FromBytes(EXAMPLE_COM_UUID.getBytes("UTF-8"));
        assertFalse(a.equals(b));
    }

    @Test
    public void testVersion3NameBasedMD5FromStringRep() throws UnsupportedEncodingException {
        UUID a = UUID.nameUUIDFromBytes(EXAMPLE_COM_UUID.getBytes("UTF-8"));
        assertEquals(a, UUID.fromString(a.toString()));
    }

    // Version 5 UUIDs

    @Test
    public void testVersion5NameBasedSHA1Variant() throws UnsupportedEncodingException {
        UUID a = UUIDFactory.nameUUIDv5FromBytes(EXAMPLE_COM_UUID.getBytes("UTF-8"));
        assertEquals(2, a.variant());
    }
    @Test
    public void testVersion5NameBasedSHA1Version() throws UnsupportedEncodingException {
        UUID a = UUIDFactory.nameUUIDv5FromBytes(EXAMPLE_COM_UUID.getBytes("UTF-8"));
        assertEquals(5, a.version());
    }

    @Test
    public void testVersion5NameBasedSHA1Equals() throws UnsupportedEncodingException {
        UUID a = UUIDFactory.nameUUIDv5FromBytes(EXAMPLE_COM_UUID.getBytes("UTF-8"));
        UUID b = UUIDFactory.nameUUIDv5FromBytes(EXAMPLE_COM_UUID.getBytes("UTF-8"));
        assertEquals(a, b);
    }

    @Test
    public void testVersion5NameBasedSHA1Different() throws UnsupportedEncodingException {
        Random random = new Random();
        byte[] data = new byte[128];
        random.nextBytes(data);
        
        UUID a = UUIDFactory.nameUUIDv5FromBytes(data);

        // Swap a random byte with its "opposite"
        int i;
        while (data[i = random.nextInt(data.length)] == data[data.length - 1 -  i]) {}
        data[i] = data[data.length - 1 - i];

        UUID b = UUIDFactory.nameUUIDv5FromBytes(data);

        assertFalse(a.equals(b));
    }

    @Test
    public void testVersion5NameBasedSHA1NotEqualMD5() throws UnsupportedEncodingException {
        UUID a = UUIDFactory.nameUUIDv5FromBytes(EXAMPLE_COM_UUID.getBytes("UTF-8"));
        UUID b = UUID.nameUUIDFromBytes(EXAMPLE_COM_UUID.getBytes("UTF-8"));
        assertFalse(a.equals(b));
    }

    @Test
    public void testVersion5NameBasedSHA1FromStringRep() throws UnsupportedEncodingException {
        UUID a = UUIDFactory.nameUUIDv5FromBytes(EXAMPLE_COM_UUID.getBytes("UTF-8"));
        assertEquals(a, UUID.fromString(a.toString()));
    }

    // Version 1 UUIDs

    @Test
    public void testVersion1NodeBasedVariant() {
        assertEquals(2, UUIDFactory.timeNodeBasedUUID().variant());
    }

    @Test
    public void testVersion1NodeBasedVersion() {
        assertEquals(1, UUIDFactory.timeNodeBasedUUID().version());
    }

    @Test
    public void testVersion1NodeBasedFromStringRep() {
        UUID uuid = UUIDFactory.timeNodeBasedUUID();
        assertEquals(uuid, UUID.fromString(uuid.toString()));
    }

    @Test
    public void testVersion1NodeBasedMacAddress() {
        UUID uuid = UUIDFactory.timeNodeBasedUUID();
        assertEquals(UUIDFactory.MAC_ADDRESS_NODE, uuid.node());
        // TODO: Test that this is actually a Mac address from the local computer, or specified through system property?
    }

    @Test
    public void testVersion1NodeBasedClockSeq() {
        UUID uuid = UUIDFactory.timeNodeBasedUUID();
        assertEquals(UUIDFactory.Clock.getClockSequence(), uuid.clockSequence());

        // Test time fields (within reasonable limits +/- 100 ms or so?)
        assertEquals(UUIDFactory.Clock.currentTimeHundredNanos(), uuid.timestamp(), 1e6);
    }

    @Test
    public void testVersion1NodeBasedTimestamp() {
        UUID uuid = UUIDFactory.timeNodeBasedUUID();
        // Test time fields (within reasonable limits +/- 100 ms or so?)
        assertEquals(UUIDFactory.Clock.currentTimeHundredNanos(), uuid.timestamp(), 1e6);
    }

    @Test
    public void testVersion1NodeBasedUniMulticastBitUnset() {
        // Do it a couple of times, to avoid accidentally have correct bit
        for (int i = 0; i < 100; i++) {
            UUID uuid = UUIDFactory.timeNodeBasedUUID();
            assertEquals(0, (uuid.node() >> 40) & 1);
        }
    }

    @Test
    public void testVersion1NodeBasedUnique() {
        for (int i = 0; i < 100; i++) {
            UUID a = UUIDFactory.timeNodeBasedUUID();
            UUID b = UUIDFactory.timeNodeBasedUUID();
            assertFalse(a.equals(b));
        }
    }

    @Test
    public void testVersion1SecureRandomVariant() {
        assertEquals(2, UUIDFactory.timeRandomBasedUUID().variant());
    }

    @Test
    public void testVersion1SecureRandomVersion() {
        assertEquals(1, UUIDFactory.timeRandomBasedUUID().version());
    }

    @Test
    public void testVersion1SecureRandomFromStringRep() {
        UUID uuid = UUIDFactory.timeRandomBasedUUID();
        assertEquals(uuid, UUID.fromString(uuid.toString()));
    }

    @Test
    public void testVersion1SecureRandomNode() {
        UUID uuid = UUIDFactory.timeRandomBasedUUID();
        assertEquals(UUIDFactory.SECURE_RANDOM_NODE, uuid.node());
    }

    @Test
    public void testVersion1SecureRandomClockSeq() {
        UUID uuid = UUIDFactory.timeRandomBasedUUID();
        assertEquals(UUIDFactory.Clock.getClockSequence(), uuid.clockSequence());
    }

    @Test
    public void testVersion1SecureRandomTimestamp() {
        UUID uuid = UUIDFactory.timeRandomBasedUUID();

        // Test time fields (within reasonable limits +/- 100 ms or so?)
        assertEquals(UUIDFactory.Clock.currentTimeHundredNanos(), uuid.timestamp(), 1e6);
    }

    @Test
    public void testVersion1SecureRandomUniMulticastBit() {
        // Do it a couple of times, to avoid accidentally have correct bit
        for (int i = 0; i < 100; i++) {
            UUID uuid = UUIDFactory.timeRandomBasedUUID();
            assertEquals(1, (uuid.node() >> 40) & 1);
        }
    }

    @Test
    public void testVersion1SecureRandomUnique() {
        for (int i = 0; i < 100; i++) {
            UUID a = UUIDFactory.timeRandomBasedUUID();
            UUID b = UUIDFactory.timeRandomBasedUUID();
            assertFalse(a.equals(b));
        }
    }

    // Clock tests

    @Test(timeout = 10000l)
    public void testClock() throws InterruptedException {
        final long[] times = new long[100000];

        ExecutorService service = Executors.newFixedThreadPool(20);
        for (int i = 0; i < times.length; i++) {
            final int index = i;

            service.submit(new Runnable() {
                public void run() {
                    times[index] = UUIDFactory.Clock.currentTimeHundredNanos();
                }
            });
        }

        service.shutdown();
        assertTrue("Execution timed out", service.awaitTermination(10, TimeUnit.SECONDS));

        Arrays.sort(times); // This is what really takes time...

        for (int i = 0, timesLength = times.length; i < timesLength; i++) {
            if (i == 0) {
                continue;
            }

            assertFalse(String.format("times[%d] == times[%d]: 0x%016x", i - 1, i, times[i]), times[i - 1] == times[i]);
        }
    }

    @Test(timeout = 10000l)
    public void testClockSkew() throws InterruptedException {
        long clockSequence = UUIDFactory.Clock.getClockSequence();

        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100000; i++) {
            service.submit(new Runnable() {
                public void run() {
                    UUIDFactory.Clock.currentTimeHundredNanos();
                }
            });
        }

        service.shutdown();
        assertTrue("Execution timed out", service.awaitTermination(10, TimeUnit.SECONDS));

        assertEquals(clockSequence, UUIDFactory.Clock.getClockSequence(), 1); // Verify that clock skew doesn't happen "often"
    }

    // Tests for node address system property

    @Test
    public void testParseNodeAddressesSingle() {
        long[] nodes = UUIDFactory.parseMacAddressNodes("00:11:22:33:44:55");

        assertEquals(1, nodes.length);
        assertEquals(0x001122334455l, nodes[0]);
    }

    @Test
    public void testParseNodeAddressesSingleWhitespace() {
        long[] nodes = UUIDFactory.parseMacAddressNodes("  00:11:22:33:44:55\r\n");

        assertEquals(1, nodes.length);
        assertEquals(0x001122334455l, nodes[0]);
    }

    @Test
    public void testParseNodeAddressesMulti() {
        long[] nodes = UUIDFactory.parseMacAddressNodes("00:11:22:33:44:55, aa:bb:cc:dd:ee:ff, \n\t 0a-1b-2c-3d-4e-5f,");

        assertEquals(3, nodes.length);
        assertEquals(0x001122334455l, nodes[0]);
        assertEquals(0xaabbccddeeffl, nodes[1]);
        assertEquals(0x0a1b2c3d4e5fl, nodes[2]);
    }

    @Test(expected = NullPointerException.class)
    public void testParseNodeAddressesNull() {
        UUIDFactory.parseMacAddressNodes(null);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseNodeAddressesEmpty() {
        UUIDFactory.parseMacAddressNodes("");
    }

    @Test(expected = NumberFormatException.class)
    public void testParseNodeAddressesNonAddress() {
        UUIDFactory.parseMacAddressNodes("127.0.0.1");
    }

    @Test(expected = NumberFormatException.class)
    public void testParseNodeAddressesBadAddress() {
        UUIDFactory.parseMacAddressNodes("00a:11:22:33:44:55");
    }

    @Test(expected = NumberFormatException.class)
    public void testParseNodeAddressesBadAddress4() {
        UUIDFactory.parseMacAddressNodes("00:11:22:33:44:550");
    }

    @Test(expected = NumberFormatException.class)
    public void testParseNodeAddressesBadAddress2() {
        UUIDFactory.parseMacAddressNodes("0x:11:22:33:44:55");
    }

    @Test(expected = NumberFormatException.class)
    public void testParseNodeAddressesBadAddress3() {
        UUIDFactory.parseMacAddressNodes("00:11:22:33:44:55:99");
    }

    // Comparator test

    @Test
    public void testComparator() {
        UUID min = new UUID(0, 0);
        // Long.MAX_VALUE and MIN_VALUE are really adjacent values when comparing unsigned...
        UUID midLow = new UUID(Long.MAX_VALUE, Long.MAX_VALUE);
        UUID midHigh = new UUID(Long.MIN_VALUE, Long.MIN_VALUE);
        UUID max = new UUID(-1l, -1l);

        Comparator<UUID> comparator = UUIDFactory.comparator();

        assertEquals(0, comparator.compare(min, min));
        assertEquals(-1, comparator.compare(min, midLow));
        assertEquals(-1, comparator.compare(min, midHigh));
        assertEquals(-1, comparator.compare(min, max));

        assertEquals(1, comparator.compare(midLow, min));
        assertEquals(0, comparator.compare(midLow, midLow));
        assertEquals(-1, comparator.compare(midLow, midHigh));
        assertEquals(-1, comparator.compare(midLow, max));

        assertEquals(1, comparator.compare(midHigh, min));
        assertEquals(1, comparator.compare(midHigh, midLow));
        assertEquals(0, comparator.compare(midHigh, midHigh));
        assertEquals(-1, comparator.compare(midHigh, max));

        assertEquals(1, comparator.compare(max, min));
        assertEquals(1, comparator.compare(max, midLow));
        assertEquals(1, comparator.compare(max, midHigh));
        assertEquals(0, comparator.compare(max, max));
    }

    @Test
    public void testComparatorRandom() {
        final Comparator<UUID> comparator = UUIDFactory.comparator();

        for (int i = 0; i < 10000; i++) {
            UUID one = UUID.randomUUID();
            UUID two = UUID.randomUUID();

            if (one.getMostSignificantBits() < 0 && two.getMostSignificantBits() >= 0
                    || one.getMostSignificantBits() >= 0 && two.getMostSignificantBits() < 0
                    || one.getLeastSignificantBits() < 0 && two.getLeastSignificantBits() >= 0
                    || one.getLeastSignificantBits() >= 0 && two.getLeastSignificantBits() < 0) {
                // These will differ due to the differing signs
                assertEquals(-one.compareTo(two), comparator.compare(one, two));
            }
            else {
                assertEquals(one.compareTo(two), comparator.compare(one, two));
            }
        }
    }
    
    // Various testing

    @Ignore("Development testing only")
    @Test
    public void testOracleSYS_GUID() {
        // TODO: Consider including this as a "fromCompactString" or similar...
        String str = "AEB87F28E222D08AE043803BD559D08A";
        BigInteger bigInteger = new BigInteger(str, 16); // ALT: Create byte array of every 2 chars.
        long msb = bigInteger.shiftRight(64).longValue();
        long lsb = bigInteger.longValue();
        UUID uuid = new UUID(msb, lsb);
        System.err.println("uuid: " + uuid);
        System.err.println("uuid.variant(): " + uuid.variant());
        System.err.println("uuid.version(): " + uuid.version());
    }
}