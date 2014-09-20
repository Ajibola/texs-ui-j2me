package net.texsoftware.lib;

/*
 * Util.java
 *
 * Created on November 20, 2007, 2:31 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.HttpsConnection;
import javax.microedition.io.SocketConnection;
import net.texsoftware.logger.Log;

/**
 *
 * @author Administrator
 */
public final class Util {

    public static void errexit(String s) {
        throw new RuntimeException("Error during encoding: " + s);
    }

    public static int roundUp(int a, int b) {
        a += b - 1;
        return a - (a % b);
    }

    public static int convertHexToInteger(String s) {
        int base = 10;
        if (s.toLowerCase().startsWith("0x")) {
            base = 16;
            s = s.substring(2);
        }
        return Integer.parseInt(s, base);
    }

    public static String normalizeUrl(String url) {
        String result = "";

        result = url.replace('.', '_');
        result = url.replace(' ', '_');
        result = url.replace('/', '_');
        result = result.replace(':', '_');
        result = Util.base64Encode(result.getBytes());
        return result;
    }

    // if the same key occurs in h1 and h2, use the value from h1
    public static final Hashtable hashtableMerge(Hashtable h1, Hashtable h2) {
        //System.out.println("in hastableMerge");
        Hashtable h = new Hashtable();
        Enumeration keys = h1.keys();
        while (keys.hasMoreElements()) {
            Object k = keys.nextElement();
            if (h1.get(k) != null) {
                h.put(k, h1.get(k));
            }
        }
        keys = h2.keys();
        while (keys.hasMoreElements()) {
            Object k = keys.nextElement();
            if (!h.containsKey(k) && h2.get(k) != null) {
                h.put(k, h2.get(k));
            }
        }
        return h;
    }

    // sorts String values
    public static final Enumeration sort(Enumeration e) {
        Vector v = new Vector();
        while (e.hasMoreElements()) {
            String s = (String) e.nextElement();
            int i = 0;
            for (i = 0; i < v.size(); i++) {
                int c = s.compareTo((String) v.elementAt(i));
                if (c < 0) { // s should go before i
                    v.insertElementAt(s, i);
                    break;
                } else if (c == 0) { // s already there
                    break;
                }
            }
            if (i >= v.size()) { // add s at end
                v.addElement(s);
            }
        }
        return v.elements();
    }

    // this is an OAuth-friendly url encode -- should work fine for ordniary encoding also
    public static final String urlEncode(String s) {
        if (s == null) {
            return s;
        }
        StringBuffer sb = new StringBuffer(s.length() * 3);
        try {
            char c;
            for (int i = 0; i < s.length(); i++) {
                c = s.charAt(i);
                if (c == '&') {
                    //sb.append("&amp;"); // don't do this
                    sb.append("%26");
                    //} else if (c == ' ') {
                    //    sb.append('+'); // maybe don't do this either
                } else if ( // safe characters
                        c == '-'
                        || c == '_'
                        || c == '.'
                        || c == '!'
                        || c == '~'
                        || c == '*'
                        || c == '\''
                        || c == '('
                        || c == ')'
                        || (c >= 'A' && c <= 'Z')
                        || (c >= 'a' && c <= 'z')) {
                    sb.append(c);
                } else {
                    sb.append('%');
                    if (c > 15) { // is it a non-control char, ie. >x0F so 2 chars
                        sb.append(Integer.toHexString((int) c).toUpperCase()); // just add % and the string
                    } else {
                        sb.append("0" + Integer.toHexString((int) c).toUpperCase());
                        // otherwise need to add a leading 0
                    }
                }
            }

        } catch (Exception ex) {
            return (null);
        }
        return (sb.toString());
    }

    public static String encode(String s, String enc)
            throws UnsupportedEncodingException {

        boolean needToChange = false;
        boolean wroteUnencodedChar = false;
        int maxBytesPerChar = 10; // rather arbitrary limit, but safe for now
        StringBuffer out = new StringBuffer(s.length());
        ByteArrayOutputStream buf = new ByteArrayOutputStream(maxBytesPerChar);

        OutputStreamWriter writer = new OutputStreamWriter(buf, enc);

        for (int i = 0; i < s.length(); i++) {
            int c = (int) s.charAt(i);
            //System.out.println("Examining character: " + c);
            if (dontNeedEncoding(c)) {
                if (c == ' ') {
                    c = '+';
                    needToChange = true;
                }
                //System.out.println("Storing: " + c);
                out.append((char) c);
                wroteUnencodedChar = true;
            } else {
                // convert to external encoding before hex conversion
                try {
                    if (wroteUnencodedChar) { // Fix for 4407610
                        writer = new OutputStreamWriter(buf, enc);
                        wroteUnencodedChar = false;
                    }
                    writer.write(c);
                    /*
                     * If this character represents the start of a Unicode
                     * surrogate pair, then pass in two characters. It's not
                     * clear what should be done if a bytes reserved in the
                     * surrogate pairs range occurs outside of a legal
                     * surrogate pair. For now, just treat it as if it were
                     * any other character.
                     */
                    if (c >= 0xD800 && c <= 0xDBFF) {
                        /*
                         System.out.println(Integer.toHexString(c)
                         + " is high surrogate");
                         */
                        if ((i + 1) < s.length()) {
                            int d = (int) s.charAt(i + 1);
                            /*
                             System.out.println("\tExamining "
                             + Integer.toHexString(d));
                             */
                            if (d >= 0xDC00 && d <= 0xDFFF) {
                                /*
                                 System.out.println("\t"
                                 + Integer.toHexString(d)
                                 + " is low surrogate");
                                 */
                                writer.write(d);
                                i++;
                            }
                        }
                    }
                    writer.flush();
                } catch (Exception e) {
                    buf.reset();
                    continue;
                }
                byte[] ba = buf.toByteArray();
                for (int j = 0; j < ba.length; j++) {
                    out.append('%');
                    char ch = CCharacter.forDigit((ba[j] >> 4) & 0xF, 16);
                    // converting to use uppercase letter as part of
                    // the hex value if ch is a letter.
                    //            if (Character.isLetter(ch)) {
                    //            ch -= caseDiff;
                    //            }
                    out.append(ch);
                    ch = CCharacter.forDigit(ba[j] & 0xF, 16);
                    //            if (Character.isLetter(ch)) {
                    //            ch -= caseDiff;
                    //            }
                    out.append(ch);
                }
                buf.reset();
                needToChange = true;
            }
        }

        return (needToChange ? out.toString() : s);
    }

    static class CCharacter {

        public static char forDigit(int digit, int radix) {
            if ((digit >= radix) || (digit < 0)) {
                return '\0';
            }
            if ((radix < Character.MIN_RADIX) || (radix > Character.MAX_RADIX)) {
                return '\0';
            }
            if (digit < 10) {
                return (char) ('0' + digit);
            }
            return (char) ('a' - 10 + digit);
        }
    }

    public static boolean dontNeedEncoding(int ch) {
        int len = _dontNeedEncoding.length();
        boolean en = false;
        for (int i = 0; i < len; i++) {
            if (_dontNeedEncoding.charAt(i) == ch) {
                en = true;
                break;
            }
        }

        return en;
    }
    //private static final int caseDiff = ('a' - 'A');
    private static String _dontNeedEncoding = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ -_.*";

    /**
     * Translates String from x-www-form-urlEncoded format into text.
     *
     * @param s String to be translated
     * @return the translated String.
     */
    public static String decode(String s) {

        ByteArrayOutputStream out = new ByteArrayOutputStream(s.length());

        for (int i = 0; i < s.length(); i++) {
            int c = (int) s.charAt(i);
            if (c == '+') {
                out.write(' ');
            } else if (c == '%') {
                int c1 = Character.digit(s.charAt(++i), 16);
                int c2 = Character.digit(s.charAt(++i), 16);
                out.write((char) (c1 * 16 + c2));
            } else {
                out.write(c);
            }
        } // end for

        return out.toString();

    }

    public static final byte[] getUrlBytes(String url) {
        byte[] byteOut = null;
        HttpConnection connection = null;
        InputStream inputstream = null;

        try {
            connection = (HttpConnection) Connector.open(url, Connector.READ_WRITE, true);
            //HTTP Request

            connection.setRequestMethod(HttpConnection.GET);
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.01; Windows NT 5.0)");

            if (connection.getResponseCode() == HttpConnection.HTTP_OK) {
                inputstream = connection.openInputStream();
                int length = (int) connection.getLength();

                if (length != -1) {
                    byte incomingData[] = new byte[length];
                    inputstream.read(incomingData);
                    byteOut = incomingData;
                } else {
                    ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
                    int ch;
                    while ((ch = inputstream.read()) != -1) {
                        bytestream.write(ch);
                    }
                    byteOut = bytestream.toByteArray();
                    bytestream.close();
                }
            }
        } catch (IOException error) {
            error.printStackTrace();
        } finally {
            if (inputstream != null) {
                try {
                    inputstream.close();
                } catch (Exception error) {
                    /*log error*/
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception error) {
                    /*log error*/
                }
            }
        }
        return byteOut;
    }

    public static final String getUrlResponse(String url) {
        return getUrlResponse(url, false);
    }

    public static final String getUrlResponse(String url, boolean gzip) {
        StringBuffer stringBuff = null;
        HttpConnection connection = null;
        InputStream inputstream = null;

        long t0, t1;                  // time stamps
        t0 = System.currentTimeMillis();

        try {
            connection = (HttpConnection) Connector.open(url, Connector.READ_WRITE, true);
            //HTTP Request

            connection.setRequestMethod(HttpConnection.GET);
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.01; Windows NT 5.0)");
            if (gzip) {
                connection.setRequestProperty("Accept-Encoding",
                        "gzip, deflate");
            }

            if (connection.getResponseCode() == HttpConnection.HTTP_OK) {
                inputstream = connection.openInputStream();
                int length = (int) connection.getLength();

                if (gzip) {
                    inputstream = new GZIPInputStream(inputstream);
                }

                Log.out("content length = " + length);

                byte[] buf = new byte[512];
                int len;

                stringBuff = new StringBuffer();

                while ((len = inputstream.read(buf)) >= 0) {
                    // Print the content if you want
                    String bufStr = new String(buf);
                    stringBuff.append(bufStr);
                    bufStr = null;
                }
            }
        } catch (Exception error) {
            Log.err(error);
        } finally {
            if (inputstream != null) {
                try {
                    inputstream.close();
                    inputstream = null;
                } catch (Exception error) {
                    /*log error*/
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                    connection = null;
                } catch (Exception error) {
                    /*log error*/
                }
            }
        }

        t1 = System.currentTimeMillis();
        Log.out(" elapsed time (secs): " + (double) ((t1 - t0) / 1000));
        return stringBuff.toString();
    }

    public static final String getHttpsUrlResponse(String url, boolean gzip) {
        HttpsConnection connection = null;
        InputStream inputstream = null;
        String dataRead = "";

        long t0, t1;                  // time stamps
        t0 = System.currentTimeMillis();

        try {
            connection = (HttpsConnection) Connector.open(url, Connector.READ_WRITE, true);
            //HTTP Request

            connection.setRequestMethod(HttpsConnection.GET);
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.01; Windows NT 5.0)");
            if (gzip) {
                connection.setRequestProperty("Accept-Encoding",
                        "gzip, deflate");
            }

            if (connection.getResponseCode() == HttpsConnection.HTTP_OK) {
                int length = (int) connection.getLength();

                if (gzip) {
                    inputstream = new GZIPInputStream(inputstream);
                } else {
                    inputstream = connection.openInputStream();
                }

                Log.out("content length = " + length);

                if (length == -1) {/*unknown length returned by server.
                     It is more efficient to read the data in chunks, so we
                     will be reading in chunk of 1500 = Maximum MTU possible*/

                    int chunkSize = 1500;
                    byte[] data = new byte[chunkSize];
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int dataSizeRead = 0;//size of data read from input stream.
                    while ((dataSizeRead = inputstream.read(data)) != -1) {
                        /*it is not recommended to write to string in the
                         loop as it causes heap defragmentation and it is
                         inefficient, therefore we use the
                         ByteArrayOutputStream.*/
                        baos.write(data, 0, dataSizeRead);
                        System.out.println("Data Size Read = " + dataSizeRead);
                    }
                    dataRead = new String(baos.toByteArray());
                    baos.close();
                } else {
                    //known length
                    DataInputStream dis = new DataInputStream(inputstream);
                    byte[] data = new byte[length];
                    //try to read all the bytes returned from the server.
                    dis.readFully(data);
                    dataRead = new String(data);
                }

            } else {
                Log.out("\nServer returned unhandled "
                        + "response code. " + connection.getResponseCode());
            }
        } catch (Exception error) {
            Log.err(error);
        } finally {
            if (inputstream != null) {
                try {
                    inputstream.close();
                    inputstream = null;
                } catch (Exception error) {
                    /*log error*/
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                    connection = null;
                } catch (Exception error) {
                    /*log error*/
                }
            }
        }

        t1 = System.currentTimeMillis();
        Log.out(" elapsed time (secs): " + (double) ((t1 - t0) / 1000));
        return dataRead;
    }

    public static String postUrlResponse(String url, String params) throws IOException {

        HttpConnection c = null;
        InputStream is = null;
        OutputStream os = null;
        StringBuffer b = new StringBuffer();
        String response = "";

        c = (HttpConnection) Connector.open(url, Connector.READ_WRITE, true);
        //HTTP Request
        c.setRequestMethod(HttpConnection.POST);
        c.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        c.setRequestProperty("User-Agent", "Profile/MIDP-2.0 Configuration/CLDC-1.0");
        c.setRequestProperty("Connection", "keep-alive");
        c.setRequestProperty("Content-Length", String.valueOf(params.length() + url.length()));
        os = c.openOutputStream();

        //start
        os.write(params.getBytes());
        os.flush();
        //end

        is = c.openDataInputStream();
        int length = (int) c.getLength();

        int ch;
        while ((ch = is.read()) != -1) {
            b.append((char) ch);
        }
        response = b.toString();

        if (is != null) {
            is.close();
        }
        if (os != null) {
            os.close();
        }
        if (c != null) {
            c.close();
        }

        c = null;
        is = null;
        os = null;
        b = null;

        return response;
    }
    private static char[] map1 = new char[64];

    static {
        int i = 0;
        for (char c = 'A'; c <= 'Z'; c++) {
            map1[i++] = c;
        }
        for (char c = 'a'; c <= 'z'; c++) {
            map1[i++] = c;
        }
        for (char c = '0'; c <= '9'; c++) {
            map1[i++] = c;
        }
        map1[i++] = '+';
        map1[i++] = '/';
    }

    public static final String base64Encode(byte[] in) {
        int iLen = in.length;
        int oDataLen = (iLen * 4 + 2) / 3;// output length without padding
        int oLen = ((iLen + 2) / 3) * 4;// output length including padding
        char[] out = new char[oLen];
        int ip = 0;
        int op = 0;
        int i0;
        int i1;
        int i2;
        int o0;
        int o1;
        int o2;
        int o3;
        while (ip < iLen) {
            i0 = in[ip++] & 0xff;
            i1 = ip < iLen ? in[ip++] & 0xff : 0;
            i2 = ip < iLen ? in[ip++] & 0xff : 0;
            o0 = i0 >>> 2;
            o1 = ((i0 & 3) << 4) | (i1 >>> 4);
            o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
            o3 = i2 & 0x3F;
            out[op++] = map1[o0];
            out[op++] = map1[o1];
            out[op] = op < oDataLen ? map1[o2] : '=';
            op++;
            out[op] = op < oDataLen ? map1[o3] : '=';
            op++;
        }
        return new String(out);
    }

    public static void sendEmail(final String from, final String to, final String subject, final String message) {

        new Thread(new Runnable() {
            public void run() {

                SocketConnection sc = null;
                InputStream is = null;
                OutputStream os = null;

                try {
                    String smtpServerAddress = "smtp.mail.yahoo.com";
                    sc = (SocketConnection) Connector.open("socket://" + smtpServerAddress + ":25");
                    is = sc.openInputStream();
                    os = sc.openOutputStream();

                    os.write(("HELO there" + "\r\n").getBytes());
                    os.write(("MAIL FROM: " + from + "\r\n").getBytes());
                    os.write(("RCPT TO: " + to + "\r\n").getBytes());
                    os.write("DATA\r\n".getBytes());
                    // stamp the msg with date
                    os.write(("Date: " + new Date() + "\r\n").getBytes());
                    os.write(("From: " + from + "\r\n").getBytes());
                    os.write(("To: " + to + "\r\n").getBytes());
                    os.write(("Subject: " + subject + "\r\n").getBytes());
                    os.write((message + "\r\n").getBytes()); // message body
                    os.write(".\r\n".getBytes());
                    os.write("QUIT\r\n".getBytes());

                    // debug
                    StringBuffer sb = new StringBuffer();
                    int c = 0;
                    while (((c = is.read()) != -1)) {
                        sb.append((char) c);
                    }
                    Log.out("SMTP server response - " + sb.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (os != null) {
                            os.close();
                        }
                        if (sc != null) {
                            sc.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static Object findString(Vector tempVector, String searchString) {
        Object result = null;

        for (int i = 0; i < tempVector.size(); i++) {
            Object tempObject = tempVector.elementAt(i);

            if (tempObject.toString().equalsIgnoreCase(searchString)) {
                result = tempObject;
                return result;
            }
        }

        return result;
    }

    public static Vector reverseVector(Vector tempVector) {
        Vector result = new Vector();

        for (int i = tempVector.size() - 1; i >= 0; i--) {
            result.addElement(tempVector.elementAt(i));
        }

        return result;
    }
}
