package com.unison.scada.availability.global.config.security;

public enum UserRole {
    ROLE_USER,
    ROLE_MANAGER,
    ROLE_ADMIN;

    public static UserRole getUserRole(String roleName) {  // Default 생성자는 private 으로 설정되어 있음.
        for(UserRole userRole : UserRole.values())
        {
            if(userRole.name().equals(roleName))
                return userRole;
        }
        return null;
    }
}
