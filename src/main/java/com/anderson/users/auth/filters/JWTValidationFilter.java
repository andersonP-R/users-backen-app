package com.anderson.users.auth.filters;

import com.anderson.users.auth.SimpleGrantedAuthorityJSONCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.*;

import static com.anderson.users.auth.TokenJWTConfig.SECRET;

public class JWTValidationFilter extends BasicAuthenticationFilter {

    public JWTValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    // method to check the generated token
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // retrieve headers
        String header = request.getHeader("Authorization");

        // if not contains our token
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // discard Bearer world to get token text only
        String token = header.replace("Bearer ", "");

        // check the authenticity of token sign
        try {

            // get the payload add to it
            Claims claims = Jwts.parser().verifyWith(SECRET).build().parseSignedClaims(token).getPayload();

            // get roles
            Object authoritiesClaims = claims.get("authorities");

            // get the username encrypted
            String username = claims.getSubject();

            // check roles - read json and get bytes
            Collection<? extends GrantedAuthority> authorities = Arrays.asList(
                    new ObjectMapper()
                    .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJSONCreator.class) // create an abstract class to modify the SimpleGrantedAuth to pass our incoming roles and mapped or construct it into authorities var
                    .readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class));

            // authenticating user. We don't use the password (null param) to validate the token. We use it just for
            // generate the token.
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities); // also pass auth list

            // set the auth token to finally authenticate us
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            // keep the req flow
            chain.doFilter(request, response);
        } catch (JwtException e) {

            // if sign is not correct
            Map<String, String> body = new HashMap<>();
            body.put("error", e.getMessage());
            body.put("message", "Invalid JWT");

            // send response to client
            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
        }

    }
}
