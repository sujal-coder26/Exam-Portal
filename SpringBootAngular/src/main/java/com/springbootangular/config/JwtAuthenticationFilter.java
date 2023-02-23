package com.springbootangular.config;

import com.springbootangular.service.impl.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal ( HttpServletRequest request , HttpServletResponse response , FilterChain filterChain ) throws ServletException, IOException {
       final String requestTokenHeader =  request.getHeader ( "Authorization" );
        System.out.println (requestTokenHeader );
        String username = null;
        String jwtToken = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith ( "Bearer " )){

                jwtToken = requestTokenHeader.substring (7);
            try{
             username= this.jwtUtil.extractUsername ( jwtToken );
            }catch(ExpiredJwtException e){
                System.out.println ("Error" );
            }
        }
        else{
            System.out.println ("Invalid Token! " );
        }

        if(username!= null && SecurityContextHolder.getContext ().getAuthentication () == null) {
            final UserDetails userDetails = this.userDetailsService.loadUserByUsername ( username );
            if (this.jwtUtil.validateToken ( jwtToken , userDetails )) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken ( userDetails , null , userDetails.getAuthorities ( ) );
                usernamePasswordAuthenticationToken.setDetails ( new WebAuthenticationDetailsSource ( ).buildDetails ( request ) );

                SecurityContextHolder.getContext ( ).setAuthentication ( usernamePasswordAuthenticationToken );
            }
        }
            else{
                System.out.println ("Token is not Valid! " );
            }
        filterChain.doFilter ( request, response );
    }
}
