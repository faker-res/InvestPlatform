package la.niub.abcapi.invest.platform.component.util;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.ParseException;
import java.io.UnsupportedEncodingException;

/**
 * @author jrxia
 * 2/1/18
 */
public class MailUtil {
    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    /**
     * copy from MimeUtility
     * @param word
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String decodeInnerWords(String word) throws UnsupportedEncodingException {
        int start = 0, i;
        StringBuffer buf = new StringBuffer();
        while ((i = word.indexOf("=?", start)) >= 0) {
            buf.append(word.substring(start, i));
            // find first '?' after opening '=?' - end of charset
            int end = word.indexOf('?', i + 2);
            if (end < 0) {
                break;
            }
            // find next '?' after that - end of encoding
            end = word.indexOf('?', end + 1);
            if (end < 0) {
                break;
            }
            // find terminating '?='
            end = word.indexOf("?=", end + 1);
            if (end < 0) {
                break;
            }
            String s = word.substring(i, end + 2);
            try {
                s = MimeUtility.decodeWord(s);
            } catch (ParseException pex) {
                // ignore it, just use the original string
            }
            buf.append(s);
            start = end + 2;
        }
        if (start == 0) {
            return word;
        }
        if (start < word.length()) {
            buf.append(word.substring(start));
        }
        return buf.toString();
    }
}
