package com.twelvemonkeys.util.convert;

import com.twelvemonkeys.lang.ObjectAbstractTestCase;
import com.twelvemonkeys.lang.Validate;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * PropertyConverterAbstractTestCase
 * <p/>
 *
 * @author <a href="mailto:harald.kuhr@gmail.com">Harald Kuhr</a>
 * @author last modified by $Author: haku $
 * @version $Id: //depot/branches/personal/haraldk/twelvemonkeys/release-2/twelvemonkeys-core/src/test/java/com/twelvemonkeys/util/convert/PropertyConverterAbstractTestCase.java#2 $
 */
public abstract class PropertyConverterAbstractTestCase extends ObjectAbstractTestCase {
    protected Object makeObject() {
        return makePropertyConverter();
    }

    protected abstract PropertyConverter makePropertyConverter();

    protected abstract Conversion[] getTestConversions();

    @Test
    public void testConvert() {
        PropertyConverter converter = makePropertyConverter();

        Conversion[] tests = getTestConversions();

        for (Conversion test : tests) {
            Object obj;
            try {
                obj = converter.toObject(test.original(), test.type(), test.format());

                assertEquals(String.format("'%s' converted to incorrect type", test.original()), test.type(), obj.getClass());
                if (test.type().isArray()) {
                    assertArrayEquals0(String.format("'%s' not converted", test.original()), test.value(), obj);
                }
                else {
                    assertEquals(String.format("'%s' not converted", test.original()), test.value(), obj);
                }

                String result = converter.toString(test.value(), test.format());

                assertEquals(String.format("'%s' does not match", test.converted()), test.converted(), result);

                obj = converter.toObject(result, test.type(), test.format());
                assertEquals(String.format("'%s' converted to incorrect type", test.original()), test.type(), obj.getClass());

                if (test.type().isArray()) {
                    assertArrayEquals0(String.format("'%s' did not survive round trip conversion", test.original()), test.value(), obj);
                }
                else {
                    assertEquals(String.format("'%s' did not survive round trip conversion", test.original()), test.value(), obj);
                }
            }
            catch (ConversionException e) {
                failBecause(String.format("Converting '%s' to %s failed", test.original(), test.type()), e);
            }
        }
    }

    private static void assertArrayEquals0(final String message, final Object left, final Object right) {
        Class<?> componentType = left.getClass().getComponentType();
        if (componentType.isPrimitive()) {
            if (int.class == componentType) {
                assertArrayEquals(message, (int[]) left, (int[]) right);
            }
            else if (short.class == componentType) {
                assertArrayEquals(message, (short[]) left, (short[]) right);
            }
            else if (long.class == componentType) {
                assertArrayEquals(message, (long[]) left, (long[]) right);
            }
            else if (float.class == componentType) {
                assertArrayEquals(message, (float[]) left, (float[]) right, 0f);
            }
            else if (double.class == componentType) {
                assertArrayEquals(message, (double[]) left, (double[]) right, 0d);
            }
            else if (boolean.class == componentType) {
                assertTrue(message, Arrays.equals((boolean[]) left, (boolean[]) right));
            }
            else if (byte.class == componentType) {
                assertArrayEquals(message, (byte[]) left, (byte[]) right);
            }
            else if (char.class == componentType) {
                assertArrayEquals(message, (char[]) left, (char[]) right);
            }
            else {
                fail(String.format("Unknown primitive type: %s", componentType));
            }
        }
        else {
            assertArrayEquals(message, (Object[]) left, (Object[]) right);
        }
    }

    private static void failBecause(String message, Throwable exception) {
        AssertionError error = new AssertionError(message);
        error.initCause(exception);
        throw error;
    }

    public static final class Conversion {
        private final String strVal;
        private final Object objVal;
        private final String format;
        private final String convertedStrVal;

        public Conversion(String pStrVal, Object pObjVal) {
            this(pStrVal, pObjVal, null);
        }

        public Conversion(String pStrVal, Object pObjVal, String pFormat) {
            this(pStrVal, pObjVal, pFormat, pStrVal);
        }

        public Conversion(String pStrVal, Object pObjVal, String pFormat, String pConvertedStrVal) {
            Validate.notNull(pStrVal, "strVal");
            Validate.notNull(pObjVal, "objVal");
            Validate.notNull(pConvertedStrVal, "convertedStrVal");

            strVal = pStrVal;
            objVal = pObjVal;
            format = pFormat;
            convertedStrVal = pConvertedStrVal;
        }

        public String original() {
            return strVal;
        }

        public Object value() {
            return objVal;
        }

        public Class type() {
            return objVal.getClass();
        }

        public String format() {
            return format;
        }

        public String converted() {
            return convertedStrVal;
        }
    }
}
