package com.aspiderngi.prepaid.api.web.security;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.aspiderngi.common.service.entity.AgentDetails;
import com.aspiderngi.common.service.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.lang.Collections;

@Component
public final class TokenHandler {

	@Value("#{'${token.secret:9SyECk96oDsTmXfogIieDI0cD/8FpnojlYSUJT5U9I/FGVmBz5oskmjOR8cbXTvoPjX+Pq/T/b1PqpHX0lYm0oCBjXWICA==}'}")
	private String secretKey = "";

	public UserDetails parseUserFromToken(String token) {
		Jws<Claims> jwsClaims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
		Claims claims = jwsClaims.getBody();

		String userName = claims.getSubject();
		if(jwsClaims.getHeader().containsKey("cid")){
			Long cid = Long.parseLong(jwsClaims.getHeader().get("cid").toString());
			return new User(cid, userName, "");
		}
		if(jwsClaims.getHeader().containsKey("aid")){
			Long aid = Long.parseLong(jwsClaims.getHeader().get("aid").toString());
			ArrayList<SimpleGrantedAuthority> rolesArrayList = new ArrayList<SimpleGrantedAuthority>();
			String roleString = (String)jwsClaims.getBody().get("roles");
			String fName = (String)jwsClaims.getBody().get("firstName");
			String sName = (String)jwsClaims.getBody().get("secondName");
			rolesArrayList.add(new SimpleGrantedAuthority(roleString));
			return new AgentDetails(aid,userName,rolesArrayList);
		}
		return null;
	}

	public String createTokenForUser(UserDetails userDetails) {
		if(userDetails instanceof User) {
			User user = (User)userDetails; 
			return Jwts.builder()
					.setSubject(user.getUsername())
					.setHeaderParam("cid", user.getCID())
					.claim("roles", "")
					.setIssuedAt(new Date())
					.setExpiration(new Date(user.getExpires()))
					.signWith(SignatureAlgorithm.HS512, secretKey)
					.compact();
		}
		else if(userDetails instanceof AgentDetails) {
			AgentDetails agent = (AgentDetails)userDetails;
			return Jwts.builder()
					.setSubject(agent.getLogin())
					.setHeaderParam("aid", agent.getAID())
					.claim("roles", "ROLE_ADMIN")
					.claim("firstName", agent.getFirstName())
					.claim("secondName", agent.getSecondName())
					.setIssuedAt(new Date())
					.setExpiration(new Date(agent.getExpires()))
					.signWith(SignatureAlgorithm.HS512, secretKey)
					.compact();
		}
		else
			throw new IllegalStateException("Unable to cast entity");
	}
}