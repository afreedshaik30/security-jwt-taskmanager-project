package com.sb.main.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
   The JwtAuthFilter is a custom implementation of OncePerRequestFilter in Spring Security.
   It intercepts each incoming HTTP request,
   extracts and validates the JWT (JSON Web Token) from the request headers, and sets up the authentication context for authorized users.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter
{
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        // 1. get Token from Header (Extracts the JWT from the Authorization header of incoming requests.)
         String authHeader = request.getHeader("Authorization");
         String token = null;
         String username = null;

         if (authHeader != null && authHeader.startsWith("Bearer "))
         {
           token = authHeader.substring(7); // Extracts the actual JWT from index 7 (By excluding "Bearer_")
           username = jwtUtils.extractUsername(token); // To identify which user the token is associated with.
         }

        // 2. Validate the token and set authentication if valid user.
        // If valid, sets up the UsernamePasswordAuthenticationToken in the security context with the authenticated user details.

         if (username != null && SecurityContextHolder.getContext().getAuthentication() == null)
               //Checks if a valid token was found and if the security context does not already have an authenticated user.
         {
             UserDetails userDetails = userDetailsService.loadUserByUsername(username);
               // Loads the user’s details from the database using the UserDetailsService implementation.

             if(jwtUtils.isValidToken(token, userDetails)) //Validates the token using the JwtUtils
             {
                 UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
                    // authToken with the authenticated user’s details includes the principles,credentials,user’s authorities (roles) which are necessary for authorization checks.

                 authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // Sets additional authentication details specific to the current request, such as the IP address and session ID.

                 SecurityContextHolder.getContext().setAuthentication(authToken);
                   // authentication token is then stored in the SecurityContextHolder, making the authenticated user’s details available to any subsequent filters or services that require user information.
             }
         }
         filterChain.doFilter(request,response);
          //To allows the request to continue through the remaining parts filter chain.
    }
}
