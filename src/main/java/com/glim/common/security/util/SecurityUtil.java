package com.glim.common.security.util;

import com.glim.common.security.dto.SecurityUserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof SecurityUserDto userDetails) {
            return userDetails.getId(); // ✅ 여기서 유저 ID 꺼내는 거
        }

        return null;
    }
}
