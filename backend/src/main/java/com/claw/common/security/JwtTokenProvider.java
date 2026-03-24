package com.claw.common.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    
    @Value("${app.jwt.secret}")
    private String jwtSecret;
    
    @Value("${app.jwt.expiration}")
    private long jwtExpiration;
    
    @Value("${app.jwt.refreshExpiration}")
    private long refreshExpiration;
    
    private SecretKey secretKey;
    
    @PostConstruct
    public void init() {
        // 生成安全的密钥
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret));
    }
    
    /**
     * 生成访问令牌
     */
    public String generateAccessToken(Long userId, String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);
        
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("type", "access")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * 生成访问令牌（简化版，需要用户名）
     */
    public String generateAccessToken(Long userId) {
        // 需要先获取用户名，这里暂时返回null，实际使用中需要传入用户名
        throw new UnsupportedOperationException("需要用户名参数，请使用generateAccessToken(Long userId, String username)");
    }
    
    /**
     * 生成刷新令牌
     */
    public String generateRefreshToken(Long userId, String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshExpiration);
        
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("type", "refresh")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * 生成刷新令牌（简化版，需要用户名）
     */
    public String generateRefreshToken(Long userId) {
        // 需要先获取用户名，这里暂时返回null，实际使用中需要传入用户名
        throw new UnsupportedOperationException("需要用户名参数，请使用generateRefreshToken(Long userId, String username)");
    }
    
    /**
     * 从令牌中获取用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        
        return claims.get("userId", Long.class);
    }
    
    /**
     * 从令牌中获取用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        
        return claims.getSubject();
    }
    
    /**
     * 验证令牌
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("无效的JWT令牌格式: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT令牌已过期: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("不支持的JWT令牌: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT令牌参数为空: {}", e.getMessage());
        } catch (JwtException e) {
            logger.error("JWT令牌验证失败: {}", e.getMessage());
        }
        return false;
    }
    
    /**
     * 获取认证信息
     */
    public Authentication getAuthentication(String token, UserDetails userDetails) {
        UserDetails user = userDetails;
        return new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );
    }
    
    /**
     * 获取访问令牌过期时间（秒）
     */
    public long getAccessTokenExpirationInSeconds() {
        return jwtExpiration / 1000;
    }
    
    /**
     * 获取刷新令牌过期时间（秒）
     */
    public long getRefreshTokenExpirationInSeconds() {
        return refreshExpiration / 1000;
    }
}