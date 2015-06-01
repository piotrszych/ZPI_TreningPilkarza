package zip.android.treningpilkarski.logika;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncrypter
{
    final protected static String s_salt = "randomsalt";
    final protected static char[] hexArray = "0123456789abcdef".toCharArray();

    public static String computeSHA256Hash(String password)
    {
        String result;

        MessageDigest mdSha256 = null;
        try
        {
            mdSha256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        try {
            if (mdSha256 != null) {
                password += s_salt;
                mdSha256.update(password.getBytes("ASCII"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] data = mdSha256 != null ? mdSha256.digest() : null;

        result = bytesToHex(data);

        return result;
    }

    public static String computeSHAHash(String password)
    {
        String result;

        MessageDigest mdSha1 = null;
        try
        {
            mdSha1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        try {
            if (mdSha1 != null) {
                mdSha1.update(password.getBytes("ASCII"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] data = mdSha1 != null ? mdSha1.digest() : null;

        result = bytesToHex(data);

        return result;
    }


    public static String computeMD5Hash(String password)
    {
        String result;
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuilder MD5Hash = new StringBuilder();
            for (byte aMessageDigest : messageDigest)
            {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                MD5Hash.append(h);
            }

            result = MD5Hash.toString();

            return result;

        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
