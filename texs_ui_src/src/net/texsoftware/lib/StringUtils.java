package net.texsoftware.lib;

import java.util.Vector;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import javax.microedition.lcdui.Font;

/**
 * Utility methods for String handling
 *
 * @author ssaa
 */
public class StringUtils {

    private final static String ELIPSIS = "...";

    /**
     * Truncates the string to fit the maxWidth. If truncated, an elipsis "..."
     * is displayed to indicate this.
     *
     * @param str
     * @param font
     * @param maxWidth
     * @return String - truncated string with ellipsis added to end of the
     * string
     */
    public static String truncate(final String str, final Font font,
            final int maxWidth) {
        if (font.stringWidth(str) <= maxWidth) {
            return str;
        }

        StringBuffer truncated = new StringBuffer(str);
        while (font.stringWidth(truncated.toString()) > maxWidth) {
            truncated.deleteCharAt(truncated.length() - 1);
        }

        int length = truncated.length();
        int space = truncated.toString().lastIndexOf(' ');

        if (space > 0) {
            truncated.delete(space, length);
        } else {
            truncated.delete(length - ELIPSIS.length(), truncated.length());
        }

        truncated.append(ELIPSIS);
        return truncated.toString();
    }

    public static String truncateWord(String text) {
        int spaceIndex = text.lastIndexOf(' ');
        int dotIndex = text.lastIndexOf('.');

        if (dotIndex > spaceIndex) {
            return text.substring(dotIndex);
        } else {
            return text.substring(spaceIndex);
        }
    }

    /**
     * Split a string in to several lines of text which will display within a
     * maximum width.
     *
     * @param str
     * @param font
     * @param maxWidth
     * @return
     */
    public static Vector splitToLines(final String str, final Font font, final int maxWidth) {
        final Vector lines = new Vector();

        if (font.stringWidth(str) <= maxWidth) {
            lines.addElement(str);
            return lines;
        }

        final StringBuffer currentLine = new StringBuffer();
        String word = null;
        int currentIndex = 0;
        int wordBoundaryIndex = str.indexOf(' ', currentIndex);
        if (wordBoundaryIndex == -1) {
            for (int i = 0; i < str.length(); i++) {
                if (font.stringWidth(str.substring(0, i)) > maxWidth) {
                    wordBoundaryIndex = i;
                    break;
                }
            }
        }

        while (currentIndex != -1 && currentIndex < str.length()) {

            word = str.substring(currentIndex, wordBoundaryIndex + 1);

            if (currentIndex == 0) {
                currentLine.append(word);
            } else {
                if (font.stringWidth((currentLine.toString() + " " + word)) < maxWidth) {
                    currentLine.append(" " + word);
                } else {
                    lines.addElement(currentLine.toString());
                    currentLine.setLength(0);
                    currentLine.append(word);
                }
            }

            currentIndex = wordBoundaryIndex + 1;
            wordBoundaryIndex = str.indexOf(' ', currentIndex);
            if (wordBoundaryIndex == -1) {
                wordBoundaryIndex = str.length() - 1;
                for (int i = currentIndex; i < str.length(); i++) {
                    String tempSubStr = str.substring(currentIndex, i);
                    if (font.stringWidth(tempSubStr) > maxWidth) {
                        wordBoundaryIndex = i;
                        break;
                    }
                    tempSubStr = null;
                }
            }

            word = null;
        }
        lines.addElement(currentLine.toString());

        return lines;
    }

    public static Vector appendSplitToLines(final Vector lines, final String str, final Font font, final int maxWidth) {

        if (font.stringWidth(str) <= maxWidth) {
            lines.addElement(str);
            return lines;
        }

        final StringBuffer currentLine = new StringBuffer();
        String word = null;
        int currentIndex = 0;
        int wordBoundaryIndex = str.indexOf(' ', currentIndex);
        if (wordBoundaryIndex == -1) {
            for (int i = 0; i < str.length(); i++) {
                if (font.stringWidth(str.substring(0, i)) > maxWidth) {
                    wordBoundaryIndex = i;
                    break;
                }
            }
        }

        while (currentIndex != -1 && currentIndex < str.length()) {

            word = str.substring(currentIndex, wordBoundaryIndex + 1);

            if (currentIndex == 0) {
                currentLine.append(word);
            } else {
                if (font.stringWidth((currentLine.toString() + " " + word)) < maxWidth) {
                    currentLine.append(" " + word);
                } else {
                    lines.addElement(currentLine.toString());
                    currentLine.setLength(0);
                    currentLine.append(word);
                }
            }

            currentIndex = wordBoundaryIndex + 1;
            wordBoundaryIndex = str.indexOf(' ', currentIndex);
            if (wordBoundaryIndex == -1) {
                wordBoundaryIndex = str.length() - 1;
                for (int i = currentIndex; i < str.length(); i++) {
                    String tempSubStr = str.substring(currentIndex, i);
                    if (font.stringWidth(tempSubStr) > maxWidth) {
                        wordBoundaryIndex = i;
                        break;
                    }
                    tempSubStr = null;
                }
            }

            word = null;
        }
        lines.addElement(currentLine.toString());

        return lines;
    }

    /**
     * Split string into multiple strings
     *
     * @param original Original string
     * @param separator Separator string in original string
     * @return Splitted string array
     */
    // TODO -- add in a max split param
    public static final String[] split(String original, String separator) {
        Vector nodes = new Vector();

        // Parse nodes into vector
        int index = original.indexOf(separator);
        while (index >= 0) {
            nodes.addElement(original.substring(0, index));
            original = original.substring(index + separator.length());
            index = original.indexOf(separator);
        }
        // Get the last node
        nodes.addElement(original);

        // Create splitted string array
        String[] result = new String[nodes.size()];
        if (nodes.size() > 0) {
            for (int loop = 0; loop < nodes.size(); loop++) {
                result[loop] = (String) nodes.elementAt(loop);
            }
        }
        return result;
    }

    public static String replaceAll(String text, String searchString, String replacementString) {
        StringBuffer sBuffer = new StringBuffer();
        int pos = 0;
        while ((pos = text.indexOf(searchString)) != -1) {
            sBuffer.append(text.substring(0, pos) + replacementString);
            text = text.substring(pos + searchString.length());
        }
        sBuffer.append(text);
        return sBuffer.toString();
    }

    /**
     * Returns a new string with all the whitespace removed
     *
     * @param s the source string
     * @return the string without whitespace or null
     */
    public static String removeWhiteSpace(String s) {
        String retn = null;

        if (s != null) {
            int len = s.length();
            StringBuffer sbuf = new StringBuffer(len);

            for (int i = 0; i < len; i++) {
                char c = s.charAt(i);

                if (!(Character.toLowerCase(c) == ' ')) {
                    sbuf.append(c);
                }
            }
            retn = sbuf.toString();
        }
        return retn;
    }

    /**
     * Method removes HTML tags from given string.
     *
     * @param text Input parameter containing HTML tags (eg. <b>cat</b>)
     * @return String without HTML tags (eg. cat)
     */
    public static String removeHtml(String text) {
        try {
            int idx = text.indexOf("<");
            if (idx == -1) {
                text = decodeEntities(text);
                return text;
            }

            String plainText = "";
            String htmlText = text;
            int htmlStartIndex = htmlText.indexOf("<", 0);
            if (htmlStartIndex == -1) {
                return text;
            }
            htmlText = StringUtils.replace(htmlText, "</p>", "\r\n");
            htmlText = StringUtils.replace(htmlText, "<br/>", "\r\n");
            htmlText = StringUtils.replace(htmlText, "<br>", "\r\n");
            while (htmlStartIndex >= 0) {
                plainText += htmlText.substring(0, htmlStartIndex);
                int htmlEndIndex = htmlText.indexOf(">", htmlStartIndex);
                htmlText = htmlText.substring(htmlEndIndex + 1);
                htmlStartIndex = htmlText.indexOf("<", 0);
            }
            plainText = plainText.trim();
            plainText = decodeEntities(plainText);
            return plainText;
        } catch (Exception e) {
            System.err.println("Error while removing HTML: " + e.toString());
            return text;
        }
    }

    public static String decodeEntities(String html) {
        String result = StringUtils.replace(html, "&lt;", "<");
        result = StringUtils.replace(result, "&gt;", ">");
        result = StringUtils.replace(result, "&nbsp;", " ");
        result = StringUtils.replace(result, "&amp;", "&");
        result = StringUtils.replace(result, "&auml;", "ä");
        result = StringUtils.replace(result, "&ouml;", "ö");
        result = StringUtils.replace(result, "&quot;", "'");
        result = StringUtils.replace(result, "&lquot;", "'");
        result = StringUtils.replace(result, "&rquot;", "'");
        result = StringUtils.replace(result, "&#xd;", "\r");
        return result;
    }

    /* Replace all instances of a String in a String.
     *   @param  s  String to alter.
     *   @param  f  String to look for.
     *   @param  r  String to replace it with, or null to just remove it.
     */
    public static String replace(String s, String f, String r) {
        if (s == null) {
            return s;
        }
        if (f == null) {
            return s;
        }
        if (r == null) {
            r = "";
        }
        int index01 = s.indexOf(f);
        while (index01 != -1) {
            s = s.substring(0, index01) + r + s.substring(index01 + f.length());
            index01 += r.length();
            index01 = s.indexOf(f, index01);
        }
        return s;
    }

    public static String cleanEncodedString(String str) {
        String resultStr = str;
        String encoding = "UTF-8";

        InputStream in = new ByteArrayInputStream(str.getBytes());
        InputStreamReader isr;
        try {
            isr = new InputStreamReader(in, encoding);

            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            int result = isr.read();
            while (result != -1) {
                byte b = (byte) result;
                buf.write(b);
                result = isr.read();
            }
            resultStr = buf.toString();

            return resultStr;
        } catch (Exception uee) {
            uee.printStackTrace();
        }
        return resultStr;
    }

    public Vector sort(String[] e) {
        Vector v = new Vector();
        for (int count = 0; count < e.length; count++) {
            String s = e[count];
            int i = 0;
            for (i = 0; i < v.size(); i++) {
                int c = s.compareTo((String) v.elementAt(i));
                if (c < 0) {
                    v.insertElementAt(s, i);
                    break;
                } else if (c == 0) {
                    break;
                }
            }
            if (i >= v.size()) {
                v.addElement(s);
            }
        }
        return v;
    }

    public static void bubbleSort(Object[] p_array) throws Exception {
        boolean anyCellSorted;
        int length = p_array.length;
        Object tmp;
        for (int i = length; --i >= 0;) {
            anyCellSorted = false;
            for (int j = 0; j < i; j++) {
                if (p_array[j].toString().compareTo(p_array[j + 1].toString()) > 0) {
                    tmp = p_array[j];
                    p_array[j] = p_array[j + 1];
                    p_array[j + 1] = tmp;
                    anyCellSorted = true;
                }
            }
            if (anyCellSorted == false) {
                return;
            }
        }
    }

    public static boolean validateEmailID(String email) {

        if (email == null || email.length() == 0 || email.indexOf("@") == -1 || email.indexOf(" ") != -1) {
            return false;
        }
        int emailLenght = email.length();
        int atPosition = email.indexOf("@");

        String beforeAt = email.substring(0, atPosition);
        String afterAt = email.substring(atPosition + 1, emailLenght);

        if (beforeAt.length() == 0 || afterAt.length() == 0) {
            return false;
        }
        if (email.charAt(atPosition - 1) == '.') {
            return false;
        }
        if (email.charAt(atPosition + 1) == '.') {
            return false;
        }
        if (afterAt.indexOf(".") == -1) {
            return false;
        }
        char dotCh = 0;
        for (int i = 0; i < afterAt.length(); i++) {
            char ch = afterAt.charAt(i);
            if ((ch == 0x2e) && (ch == dotCh)) {
                return false;
            }
            dotCh = ch;
        }
        if (afterAt.indexOf("@") != -1) {
            return false;
        }
        int ind = 0;
        do {
            int newInd = afterAt.indexOf(".", ind + 1);

            if (newInd == ind || newInd == -1) {
                String prefix = afterAt.substring(ind + 1);
                if (prefix.length() > 1 && prefix.length() < 6) {
                    break;
                } else {
                    return false;
                }
            } else {
                ind = newInd;
            }
        } while (true);
        dotCh = 0;
        for (int i = 0; i < beforeAt.length(); i++) {
            char ch = beforeAt.charAt(i);
            if (!((ch >= 0x30 && ch <= 0x39) || (ch >= 0x41 && ch <= 0x5a) || (ch >= 0x61 && ch <= 0x7a)
                    || (ch == 0x2e) || (ch == 0x2d) || (ch == 0x5f))) {
                return false;
            }
            if ((ch == 0x2e) && (ch == dotCh)) {
                return false;
            }
            dotCh = ch;
        }
        return true;
    }

    public static final int getMonthNumber(String month) {
        if (month.equalsIgnoreCase("Jan")) {
            return 0;
        }
        if (month.equalsIgnoreCase("Feb")) {
            return 1;
        }
        if (month.equalsIgnoreCase("Mar")) {
            return 2;
        }
        if (month.equalsIgnoreCase("Apr")) {
            return 3;
        }
        if (month.equalsIgnoreCase("May")) {
            return 4;
        }
        if (month.equalsIgnoreCase("Jun")) {
            return 5;
        }
        if (month.equalsIgnoreCase("Jul")) {
            return 6;
        }
        if (month.equalsIgnoreCase("Aug")) {
            return 7;
        }
        if (month.equalsIgnoreCase("Sep")) {
            return 8;
        }
        if (month.equalsIgnoreCase("Oct")) {
            return 9;
        }
        if (month.equalsIgnoreCase("Nov")) {
            return 10;
        }
        if (month.equalsIgnoreCase("Dec")) {
            return 11;
        } else {
            return 0;
        }
    }

    public static final String getMonthName(int month) {
        switch (month) {
            case 0:
                return "Jan";
            case 1:
                return "Feb";
            case 2:
                return "Mar";
            case 3:
                return "Apr";
            case 4:
                return "May";
            case 5:
                return "Jun";
            case 6:
                return "Jul";
            case 7:
                return "Aug";
            case 8:
                return "Sep";
            case 9:
                return "Oct";
            case 10:
                return "Nov";
            case 11:
                return "Dec";
            default:
                return "";
        }
    }

    public static String getAmount(String amount) {
        String result = "";

        amount = amount.trim();

        if (amount.equalsIgnoreCase("null")) {
            amount = "0";
        }

        int strLength = amount.length();

        if (strLength > 3) {
            for (int i = strLength; i >= 0; i = i - 3) {
                int beginIndex = i - 3;
                int endIndex = i;

                if (beginIndex > 0) {
                    result = "," + amount.substring(beginIndex, endIndex)
                            + result;
                } else {
                    result = amount.substring(0, endIndex) + result;
                }
            }
        } else {
            result = amount;
        }

        if (amount.equalsIgnoreCase("0")) {
            result = "Not Stated";
        } else {
            result = "N " + result;
        }

        return result;
    }

    public static String getTimeString(long timestamp) {
        String result = "";

        long diff = (System.currentTimeMillis() - timestamp) / (1000);
        //diff = diff - (60 * 60); // normalize for Nigerian timezone
        // Log.out("Cal is : " + Calendar.getInstance().getTime().getTime());
        // Log.out("timestamp is : " + timestamp);
        // Log.out("Diff is : " + diff);

        if (diff < 0) {
            diff = diff * -1;
        }

        if ((diff) < 60) {

            if (diff > 1) {
                result = diff + " secs ago";
            } else {
                result = diff + " sec ago";
            }
        } else if ((diff / 60) < 60) {
            diff = diff / 60;
            if (diff > 1) {
                result = diff + " mins ago";
            } else {
                result = diff + " min ago";
            }
        } else if ((diff / (60 * 60)) < 24) {
            diff = diff / (60 * 60);
            if (diff > 1) {
                result = diff + " hrs ago";
            } else {
                result = diff + " hr ago";
            }
        } else if ((diff / (60 * 60 * 24)) < 365) {
            diff = diff / (60 * 60 * 24);
            if (diff > 1) {
                result = diff + " days ago";
            } else {
                result = diff + " day ago";
            }
        } else if ((diff / (60 * 60 * 24 * 365)) < 10) {
            diff = diff / (60 * 60 * 24);
            if (diff > 1) {
                result = diff + " years ago";
            } else {
                result = diff + " year ago";
            }
        } else {
            Date postDate = new Date(timestamp);
            Calendar postCal = Calendar.getInstance();
            postCal.setTime(postDate);

            result = getMonthName(postCal.get(Calendar.MONTH)) + " "
                    + postCal.get(Calendar.DAY_OF_MONTH);
        }

        // Logger.out("result is : " + result);

        return result;
    }
}
