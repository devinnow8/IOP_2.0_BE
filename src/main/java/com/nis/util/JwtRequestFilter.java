package com.nis.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nis.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ObjectMapper mapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        try {

            String username = null;
            String jwtToken = null;
            // JWT Token is in the form "Bearer token". Remove Bearer word and get
            // only the Token
            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                jwtToken = requestTokenHeader.substring(7);
                try {
                    username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                } catch (IllegalArgumentException e) {
//                    throw new Exception("Unable to get JWT Token");
                    throw new Exception("Your session has been expired.");
                } catch (ExpiredJwtException e) {
//                    throw new Exception("JWT Token has expired");
                    throw new Exception("Your session has been expired.");
                }
            } else {
//                throw new Exception("JWT Token does not begin with Bearer String");
                throw new Exception("Your session has been expired.");
            }

            // Once we get the token validate it.
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = this.userService.loadUserByUsername(username);

                // if token is valid configure Spring Security to manually set
                // authentication
                if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

                    UsernamePasswordAuthenticationToken authentication =
                            jwtTokenUtil.getAuthenticationToken(jwtToken,
                                    SecurityContextHolder.getContext().getAuthentication(), userDetails);
                    authentication
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    System.out.println(authentication.getAuthorities());
                    logger.info("authenticated user "+username+" ,setting security context");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            chain.doFilter(request, response);
        }catch (Exception ex){
            logger.warn(ex.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);

//            ErrorResponse errorResponse= new ErrorResponse();
//            errorResponse.setMessage(ex.getMessage());
//            errorResponse.setTimestamp(LocalDate.now());
            response.getWriter().write(ex.getMessage());
//            mapper.writeValue(response.getWriter(),errorResponse);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        System.out.println(path);
        return path.contains("/nis/user/sign-in")
                || path.contains("/nis/user/sign-up")
                || path.contains("/swagger-ui")
                || path.contains("/v3/api-docs");
    }

}
