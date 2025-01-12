package org.example.entity;


import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;


public enum Role {
   USER(Set.of(Permission.UNSECURE)),
   ADMIN(Set.of(Permission.UNSECURE, Permission.SECURE));


   private final Set<Permission> permissions;


   Role(Set<Permission> permissions) {
       this.permissions = permissions;
   }

   public Set<Permission> getPermissions() {
       return permissions;
   }

   public Set<SimpleGrantedAuthority> getAuthorities() {
       return getPermissions().stream()
               .map(permission -> new SimpleGrantedAuthority(permission.getPermisson()))
               .collect(Collectors.toSet());

   }
}
