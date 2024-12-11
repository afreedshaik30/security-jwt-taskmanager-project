package com.sb.main.controllers;

import com.sb.main.DTO.JwtTokenResponse;
import com.sb.main.DTO.LoginDto;
import com.sb.main.DTO.UserDto;
import com.sb.main.security.JwtUtils;
import com.sb.main.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
public class AuthController
{
    private UserService userService;
    private AuthenticationManager authManager;
    private JwtUtils jwtUtils;

    public AuthController(UserService userService,AuthenticationManager authManager, JwtUtils jwtUtils)
    {
        this.userService = userService;
        this.authManager = authManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto)
    {
        UserDto createUser = userService.createUser(userDto);
        return new ResponseEntity<>(createUser, HttpStatus.CREATED);
    }

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
}
