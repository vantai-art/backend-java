package com.ngovantai.example901.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    public JwtAuthFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                // 🔹 Lấy username & role từ JWT
                String username = jwtUtils.getUsernameFromJwt(token);
                String role = jwtUtils.getRoleFromJwt(token);

                // 🔹 Danh sách quyền
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();

                // ✅ Đảm bảo role luôn có tiền tố ROLE_
                String prefixedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
                authorities.add(new SimpleGrantedAuthority(prefixedRole));

                // ✅ Thêm alias để tương thích: STAFF ↔ EMPLOYEE
                if (role.equalsIgnoreCase("STAFF")) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
                } else if (role.equalsIgnoreCase("EMPLOYEE")) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_STAFF"));
                }

                // ✅ Thiết lập người dùng vào SecurityContext
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,
                        null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);

                // 🔍 Ghi log kiểm tra
                System.out.println("✅ JWT Auth: user=" + username + ", authorities=" + authorities);

            } catch (Exception e) {
                System.out.println("❌ JWT validation failed: " + e.getMessage());
            }
        }

        // ✅ Tiếp tục chuỗi filter
        filterChain.doFilter(request, response);
    }
}
