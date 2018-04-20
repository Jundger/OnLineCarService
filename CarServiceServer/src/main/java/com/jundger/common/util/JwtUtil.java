package com.jundger.common.util;

import com.alibaba.fastjson.JSONObject;
import com.jundger.work.constant.JwtConfig;
import com.jundger.work.pojo.Customer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

/**
 * Created by Jundger on 2017/12/27.
 */
public class JwtUtil {

    /**
     * 由字符串生成加密key
     *
     * @return
     */
    public static SecretKey generalKey() {
        String stringKey = JwtConfig.JWT_SECRET;
        byte[] encodedKey = Base64.decodeBase64(stringKey);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 创建jwt
     *
     * @param id
     * @param subject
     * @param ttlMillis
     * @return
     * @throws Exception
     */
    public static String createJWT(String id, String subject, long ttlMillis) throws Exception {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        SecretKey key = generalKey();
        String issuer = JwtConfig.JWT_ISSUER;

        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setSubject(subject)
                .signWith(signatureAlgorithm, key);

        // 设置到期时间
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    /**
     * 解密jwt
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey key = generalKey();
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwt).getBody();
        return claims;
    }

    /**
     * 生成subject信息
     * @param customer
     * @return
     */
    public static String generalSubject(Customer customer) {
        JSONObject jo = new JSONObject();
        jo.put("phoneNumber", customer.getCustPhone());
        jo.put("nickname", customer.getCustName());
        return jo.toJSONString();
    }
}
