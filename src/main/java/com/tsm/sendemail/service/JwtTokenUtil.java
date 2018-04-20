package com.tsm.sendemail.service;

import static com.tsm.sendemail.security.SecurityConstants.ACCESS_TOKEN_VALIDITY_SECONDS;
import static com.tsm.sendemail.security.SecurityConstants.SIGNING_KEY;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.function.Function;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5117178732468774972L;

	public String getUserEmailFromToken(final String token) {
		Assert.hasText(token, "The token must not be empty!");
		return (String) getClaimFromToken(token, Claims::getSubject);
	}

	private <T> Object getClaimFromToken(final String token, Function<Claims, ? extends Object> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(final String token) {
		return Jwts.parser().setSigningKey(SIGNING_KEY).parseClaimsJws(token).getBody();
	}

	public String doGenerateToken(final String subject) {
		Assert.hasText(subject, "The subject must not be empty!");
		Claims claims = Jwts.claims().setSubject(subject);
		claims.put("scopes", Collections.emptyList());

		return Jwts.builder().setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
				.signWith(SignatureAlgorithm.HS512, SIGNING_KEY).compact();
	}

	public Date getExpirationDateFromToken(final String token) {
		Assert.hasText(token, "The token must not be empty!");
		return (Date) getClaimFromToken(token, Claims::getExpiration);
	}

	public Boolean validateToken(final String token, final String subject) {
		Assert.hasText(token, "The token must not be empty!");
		Assert.hasText(subject, "The subject must not be empty!");
		final String username = getUserEmailFromToken(token);
		return (username.equals(subject) && !isTokenExpired(token));
	}

	private Boolean isTokenExpired(final String token) {
		Assert.hasText(token, "The token must not be empty!");
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

}
