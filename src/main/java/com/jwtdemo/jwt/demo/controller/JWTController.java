package com.jwtdemo.jwt.demo.controller;

import com.jwtdemo.jwt.demo.model.JwtRequest;
import com.jwtdemo.jwt.demo.model.JwtResponse;
import com.jwtdemo.jwt.demo.service.UserService;
import com.jwtdemo.jwt.demo.utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JWTController {

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String Home(){
        return "hello";
    }

    @PostMapping("/authenticate")
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception {

       try{
           authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                   jwtRequest.getUsername(),
                   jwtRequest.getPassword()
           ));

       }catch (BadCredentialsException e){

          throw new Exception("BAD CREDENTIALS DETAILS",e);

       }

       final UserDetails userDetails= userService.loadUserByUsername(jwtRequest.getUsername());

       final  String token= jwtUtility.generateToken(userDetails);

       return  new JwtResponse(token);

    }
}
