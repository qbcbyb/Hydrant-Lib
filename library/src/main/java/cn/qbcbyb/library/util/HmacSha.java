package cn.qbcbyb.library.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacSha {

    private static final String UTF8 = "UTF-8";
    private static final String EMPTY_STRING = "";
    private static final String CARRIAGE_RETURN = "\r\n";
    private static final String HMAC_SHA1 = "HmacSHA1";

    public static String getSignature(String baseString, String appSecret) throws Exception {
        return doSign(baseString, appSecret);
    }

    private static String doSign(String toSign, String keyString) throws Exception {
        SecretKeySpec key = new SecretKeySpec((keyString).getBytes(UTF8), HMAC_SHA1);
        Mac mac = Mac.getInstance(HMAC_SHA1);
        mac.init(key);
        byte[] bytes = mac.doFinal(toSign.getBytes(UTF8));
        return new String(Base64.encode(bytes)).replace(CARRIAGE_RETURN, EMPTY_STRING);
    }
}
