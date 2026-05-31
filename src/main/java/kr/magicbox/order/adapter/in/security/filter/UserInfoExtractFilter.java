package kr.magicbox.order.adapter.in.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.magicbox.order.domain.vo.UserId;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class UserInfoExtractFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String userIdHeader = request.getHeader("X-User-Id");

        if (!isValidUserId(userIdHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        UserId userId = UserId.of(Long.valueOf(userIdHeader));
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userId, null);
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    private boolean isValidUserId(String userIdHeader) {
        try {
            return Long.parseLong(userIdHeader) > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
