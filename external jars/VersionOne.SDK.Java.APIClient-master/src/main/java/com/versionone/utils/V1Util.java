package com.versionone.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.List;

/**
 * Set of units.
 */
public final class V1Util {

	private static String CR_XML = "&#10;";
	private static String CR_SYSTEM = "\r";
	
    private V1Util() {
    }

    /**
     * Verification of equivalence of the objects.
     *
     * @param o1 first object.
     * @param o2 second object.
     * @return true if reference of the objects is equals or internal form of
     *         first object equals internal form of second object, false otherwise.
     */
    public static boolean equals(Object o1, Object o2) {
        return (o1 == o2) || ((o1 != null) && o1.equals(o2));
    }

    /**
     * Coping data(character type) from {@code input} reader to {@code output} writer.
     *
     * @param input source of data.
     * @param output destination of data.
     * @throws IOException if any errors occur during copying process.
     */
    public static void copyStream(Reader input, Writer output)
            throws IOException {
        copyStream(input, output, 4096);
    }

    /**
     * Coping data(binary type) from {@code input} stream to {@code output} stream.
     *
     * @param input input source of data.
     * @param output destination of data.
     * @throws IOException if any errors occur during copying process.
     */
    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {
        copyStream(input, output, 4096);
    }

    /**
     * Coping data(character type) from {@code input} reader to {@code output} writer.
     *
     * @param input input source of data.
     * @param output destination of data.
     * @param buffersize size of buffer with is using for data copy.
     * @throws IOException if any errors occur during copying process.
     * @throws IllegalArgumentException if {@code buffersize} less then 1.
     */
    public static void copyStream(Reader input, Writer output, int buffersize)
            throws IOException, IllegalArgumentException {
        if (buffersize < 1) {
            throw new IllegalArgumentException(
                    "buffersize must be greater than 0");
        }
        char[] buffer = new char[buffersize];
        int n;
        while ((n = input.read(buffer)) >= 0) {
            output.write(buffer, 0, n);
        }
    }

    /**
     * Coping data(binary type) from {@code input} stream to {@code output} stream.
     *
     * @param input input source of data.
     * @param output destination of data.
     * @param buffersize size of buffer with is using for data copy.
     * @throws IOException if any errors occur during copying process.
     * @throws IllegalArgumentException if {@code buffersize} less then 1.
     */
    public static void copyStream(InputStream input, OutputStream output,
            int buffersize) throws IOException, IllegalArgumentException {
        if (buffersize < 1) {
            throw new IllegalArgumentException(
                    "buffersize must be greater than 0");
        }
        byte[] buffer = new byte[buffersize];
        int n;
        while ((n = input.read(buffer)) >= 0) {
            output.write(buffer, 0, n);
        }
    }

    /**
     * @param <T> type of result array.
     * @param oids source list of elements.
     * @param clazz source class of result array.
     * @return resulting array.
     */
    public static <T> T[] convertListToArray(List<?> oids, Class<T> clazz) {
        T[] resultArray = (T[]) Array.newInstance(clazz, oids.size());

        return oids.toArray(resultArray);
    }

    /**
     * Verify - is string empty or null?
     *
     * @param string string for verifying.
     * @return true if string is not empty in the other case return false.
     */
    public static boolean isNullOrEmpty(String string) {
        return (string == null) || (string.trim().length() == 0);
    }
    
    /**
     * Converts system specific end-of-line characters to xml specific end-of-line characters.
     * @param string string to process.
     * @return string with xml specific end-of-line characters.
     */
    public static String convertSystemCrToXmlCr(String string) {
    	return string.replace(CR_SYSTEM, CR_XML);
    }
    
    /**
     * Converts xml specific end-of-line characters to system specific end-of-line characters.
     * @param string string to process.
     * @return string with system specific end-of-line characters.
     */
    public static String convertXmlCrToSystemCr(String string) {
    	return string.replace(CR_XML, CR_SYSTEM);
    }
}
