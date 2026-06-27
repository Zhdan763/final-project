package dev.sorokin.eventmanager.security.jwt;

import dev.sorokin.eventmanager.location.LocationController;
import dev.sorokin.eventmanager.user.User;
import dev.sorokin.eventmanager.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;


@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenManager jwtTokenManager;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);

    public JwtTokenFilter(
            JwtTokenManager jwtTokenManager,
            @Lazy UserService userService) {
        this.jwtTokenManager = jwtTokenManager;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal
            (HttpServletRequest request,
             HttpServletResponse response,
             FilterChain filterChain
            ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        var jwtToken = authHeader.substring(7);
        String loginFromToken;
        try {
            loginFromToken = jwtTokenManager.getLoginFromToken(jwtToken);
        } catch (Exception e) {
            logger.error("Error while getting login from token", e);
            filterChain.doFilter(request, response);
            return;
        }

        User user = userService.findByLogin(loginFromToken);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user,
                null,
                List.of(new SimpleGrantedAuthority(user.role().toString()))
        );

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
