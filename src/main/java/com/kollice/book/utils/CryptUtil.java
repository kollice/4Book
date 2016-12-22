package com.kollice.book.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by 00259 on 2016/12/22.
 */
public class CryptUtil {
    private static transient Logger log = LoggerFactory.getLogger(CryptUtil.class);
    private static final String ALGORITHM_MD5 = "MD5";
    private static final String ALGORITHM_DES = "DES";
    private static final String CHARSET_UTF8 = "UTF-8";
    private static final String DEFAULT_DES_KEY = CryptUtil.class.getName();

    public static String encrypt(String... datas) {
        StringBuilder sb = new StringBuilder();
        for (String data : datas) {
            if (data != null) {
                sb.append(data);
            }
        }
        byte[] bytes = encryptMD5(sb.toString());
        return byte2hex(bytes).toLowerCase();
    }

    public static String encode(String value) {
        String result = null;
        if (StringUtils.isNotEmpty(value)) {
            try {
                result = URLEncoder.encode(value, CHARSET_UTF8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public static String decode(String value) {
        String result = null;
        if (StringUtils.isNotEmpty(value)) {
            try {
                result = URLDecoder.decode(value, CHARSET_UTF8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public static String encodeMap(Map<String, Object> paraMap) {
        String param = null;
        if (paraMap != null) {
            StringBuilder sb = new StringBuilder();
            Map<String, Object> sortedParams = new TreeMap<String, Object>(paraMap);
            Set<Map.Entry<String, Object>> paramSet = sortedParams.entrySet();
            boolean hasParam = false;
            for (Map.Entry<String, Object> ent : paramSet) {
                if (ent.getValue() != null && StringUtils.isNotEmpty(ent.getValue().toString())) {
                    if (hasParam) {
                        sb.append("&");
                    } else {
                        hasParam = true;
                    }
                    sb.append(ent.getKey()).append("=").append(encodeBase64(ent.getValue().toString()));
                }
            }
            param = encode(encodeBase64(sb.toString()));
        }
        return param;
    }

    public static Map<String, String> decodeMap(String param) throws Exception {
        Map<String, String> result = new HashMap();
        if (StringUtils.isNotEmpty(param)) {
            String originParams = decodeBase64(decode(param));
            String[] pairs = originParams.split("&");
            if (pairs != null && pairs.length > 0) {
                for (String pair : pairs) {
                    String[] parampair = pair.split("=", 2);
                    if (parampair != null && parampair.length == 2) {
                        result.put(parampair[0], decodeBase64(parampair[1]));
                    }
                }
            }
        }
        return result;
    }

    public static String encodeBase64(String str) {
        String result = null;
        try {
            byte[] b = str.getBytes(CHARSET_UTF8);
            result = Base64.encodeBase64URLSafeString(b);
        } catch (UnsupportedEncodingException e) {}
        return result;
    }

    public static String decodeBase64(String str) {
        String result = null;
        try {
            result = new String(Base64.decodeBase64(str), CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {}
        return result;
    }

    private static byte[] encryptMD5(String data) {
        byte[] bytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM_MD5);
            bytes = md.digest(data.getBytes(CHARSET_UTF8));
        } catch (Exception e) {
            log.error("ERROR in encryptMD5:[" + data + "]", e);
        }
        return bytes;
    }

    public static String encryptDes(String data) {
        return encryptDes(DEFAULT_DES_KEY, data);
    }

    public static String encryptDes(String key, String data) {
        String result = "";
        try {
            Cipher cipher = getDesCipher(key, Cipher.ENCRYPT_MODE);
            result = byte2hex(cipher.doFinal(data.getBytes()));
        } catch (Exception e) {
            log.error("ERROR in encryptDes:[" + data + "]", e);
        }
        return result;
    }

    public static String decryptDes(String data) {
        return decryptDes(DEFAULT_DES_KEY, data);
    }

    public static String decryptDes(String key, String data) {
        String result = "";
        try {
            Cipher cipher = getDesCipher(key, Cipher.DECRYPT_MODE);
            result = new String(cipher.doFinal(hex2byte(data.getBytes())));
        } catch (Exception e) {
            log.error("ERROR in decryptDes:[" + data + "]", e);
        }
        return result;
    }

    private static Cipher getDesCipher(String key, int mode) throws Exception {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(key.getBytes());
        KeyGenerator _generator = KeyGenerator.getInstance(ALGORITHM_DES);
        _generator.init(secureRandom);
        Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
        cipher.init(mode, _generator.generateKey());
        return cipher;
    }

    public static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }

    public static byte[] hex2byte(byte[] b) {
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }
}
