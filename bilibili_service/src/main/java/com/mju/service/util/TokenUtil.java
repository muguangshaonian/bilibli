package com.mju.service.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.mju.domain.exception.ConditionException;

import java.util.Calendar;
import java.util.Date;

public class TokenUtil {
    private static final String ISSUER = "签发者";

    public static String generateToken(Long userId) throws Exception{
        Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND,30);//过期时间
        return JWT.create().withExpiresAt(calendar.getTime())
                .withIssuer(ISSUER)
                .withKeyId(String.valueOf(userId))
                .sign(algorithm);
    }

    public static Long verifyToken(String token){
        try {
            Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            String keyId = decodedJWT.getKeyId();
            return Long.valueOf(keyId);
        } catch (TokenExpiredException e) {
            throw new ConditionException("555","token已过期 ！");
        }catch (Exception e){
            throw new ConditionException("非法用户token !");
        }
    }
}
