package com.springbootangular.controller;

import com.springbootangular.config.JwtUtil;
import com.springbootangular.entities.JwtRequest;
import com.springbootangular.entities.JwtResponse;
import com.springbootangular.service.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthenticController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;

    @PostMapping(value = "/generate-token")
    public ResponseEntity<?> generateToken( @RequestBody JwtRequest jwtRequest ) throws Exception {

            try {
                authenticate(jwtRequest.getUsername (), jwtRequest.getPassword ());

            } catch (UsernameNotFoundException e) {
                e.printStackTrace ( );
                throw new Exception("User not found");
            }
        UserDetails userDetails = this.userDetailsService.loadUserByUsername ( jwtRequest.getUsername () );
            String token = this.jwtUtil.generateToken ( userDetails );
            return ResponseEntity.ok ( new JwtResponse ( token ) );
    }

    private void authenticate(String username, String password) throws Exception {
        try{
            authenticationManager.authenticate ( new UsernamePasswordAuthenticationToken ( username, password ) );
        }catch (DisabledException e){
            throw new Exception ("User Disabled " + e.getMessage ());

        }catch(BadCredentialsException e){
            throw new Exception ( "Invalid Credentials " + e.getMessage () );
        }
    }
}
