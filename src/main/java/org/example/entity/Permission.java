package org.example.entity;

public enum Permission {
    SECURE("secure"),
    UNSECURE("unsecure");

    private final String permisson;

    Permission(String permisson){
        this.permisson = permisson;
    }

    public String getPermisson() {
        return permisson;
    }
}
