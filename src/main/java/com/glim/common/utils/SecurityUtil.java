package com.glim.common.utils;

import com.glim.common.security.dto.SecurityUserDto;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class SecurityUtil {

    public static String getUsername(){
        return ((SecurityUserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }
    public static String getUserNickname(){
        return ((SecurityUserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getNickname();
    }
    public static SecurityUserDto getUser(){
        return (SecurityUserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
