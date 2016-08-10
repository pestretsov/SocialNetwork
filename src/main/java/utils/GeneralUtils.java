package utils;

/**
 * Created by artemypestretsov on 7/21/16.
 */
public class GeneralUtils {
    public static String bytesToHexString(byte[] bytes) {
        final StringBuilder result = new StringBuilder();
        for (byte b: bytes) {
            String hexVal = Integer.toHexString(0xFF & b);
            if (hexVal.length() == 1)
                result.append("0");
            result.append(hexVal);
        }

        return result.toString();
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
