# Complete Workflow:
## 1.User Login Request:
A user submits their login credentials via loginUser().
loginUser() creates a UsernamePasswordAuthenticationToken and passes it to the AuthenticationManager.

```bash
 @PostMapping("/login")
 public ResponseEntity<JwtTokenResponse> loginUser(@RequestBody LoginDto loginDto)
 {
        // 1.To Authenticate user by comparing provided login credentials (email and plaintext password) with the details stored in the database.
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword()));
        if(authentication.isAuthenticated()) // if authenticated user
        {
            // 2.setting the authenticated user in the SecurityContextHolder, To specify who's the current user is.
            SecurityContextHolder.getContext().setAuthentication(authentication);
           // 3.generate JWT token
            String token = jwtUtils.generateToken(authentication);
            return new ResponseEntity<>(new JwtTokenResponse(token) ,HttpStatus.OK); // returns JwtTokenResponse with token, status
        }
        return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
}
```

## 2.AuthenticationManager:
The AuthenticationManager validates the credentials. If valid, it returns an Authentication object containing the user’s information.

```bash
public Authentication authenticate(Authentication authentication) throws AuthenticationException
{
    // Logic to validate user credentials against database and create an Authentication object
    // Return Authentication object if credentials are valid
}
```

## 3.JWT Token Generation:
If authentication is successful, JwtUtils.generateToken() creates a JWT token based on the Authentication object.
The token contains the username, roles, and an expiration date.

```bash
public String generateToken(Authentication authentication) {
    return Jwts.builder()
               .setSubject(authentication.getName())
               .setIssuedAt(new Date(System.currentTimeMillis()))
               .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day expiration
               .signWith(getSigninKey())
               .compact();
}

private SecretKey getSigninKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
}
// Extract
 private Claims extractAllClaims(String token)
    {
        return Jwts.parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimResolver)
    {
        Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }


    public String extractUsername(String token)
    {
        return extractClaim(token,Claims::getSubject);
    }

    private Date extractExpiration(String token)
    {
        return extractClaim(token,Claims::getExpiration);
    }
// Token Expired
    private boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }

 // Validate Token
    public boolean isValidToken(String token, UserDetails userDetails)
    {
      String username = extractUsername(token);
      return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
(total 8 methods )
```

## 4.JWT Authentication Filter (JwtAuthFilter):
The JwtAuthFilter intercepts incoming requests and extracts the JWT from the Authorization header.
It validates the token using JwtUtils.isValidToken(). If valid, it sets up the user’s authentication in the SecurityContextHolder.
This ensures that the authenticated user’s details are accessible for any further request processing.

```bash

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
```

## 5.Response:
If authentication is successful, the loginUser() method returns a ResponseEntity with a JwtTokenResponse containing the JWT token.
If the token is invalid or authentication fails, the request is denied or returns an unauthorized status.

```bash
return new ResponseEntity<>(new JwtTokenResponse(token) ,HttpStatus.OK); // returns JwtTokenResponse with token, status
```
