package com.anderson.users.auth.filters;

import static com.anderson.users.auth.TokenJWTConfig.SECRET;
import com.anderson.users.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

// spring handles everything from behind. When an POST req incomes to our API and the req is requiring "/login" path
// spring trigger this component and the corresponding method itself.
public class JWTAuthFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JWTAuthFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // we have to configure our auth filters based on this 3 initial methods

    // method to try an authentication action
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // define our data
        User user;
        String username;
        String password;

        try {
            // insert the incoming data into var user with ObjectMapper
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            username = user.getUsername();
            password = user.getPassword();

            /*
            logger.info("JWTAuthFilter attempting to authenticate user: " + username);
            logger.info("JWTAuthFilter attempting to authenticate password: " + password);
            */

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // pass the username and password to generate auth token
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

        // finally pass the token to finally get authenticated
        return authenticationManager.authenticate(authToken);
    }

    // if auth is success we trigger this method
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        // get data from the user authenticated

        // cast result from getPrincipal()
        String username = ((org.springframework.security.core.userdetails.User)authResult.getPrincipal()).toString();

        // set our secret
        String originalInput = SECRET + ":" + username;

        // encode to base64
        String token = Base64.getEncoder().encodeToString(originalInput.getBytes());

        // add the token to res headers to retrieve it later
        response.addHeader("Authorization", "Bearer " + token);

        Map<String, Object> body = new HashMap<>();

        // map the data to send to client in JSON format
        body.put("token", token);
        body.put("username", username);
        body.put("message", "Successfully authenticated user");

        // finally send it to client
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));

        // set status res
        response.setStatus(HttpServletResponse.SC_OK);

        // set content type
        response.setContentType("application/json");
    }

    // if auth is not success we trigger this method
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // map the data to sent in JSON type
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Error in authentication. Username or password is incorrect");
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
    }
}
