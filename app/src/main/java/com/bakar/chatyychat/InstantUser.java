package com.bakar.chatyychat;

public class InstantUser {
    private String displayName;
    private String email;

    public InstantUser(String displayName, String email) {
        this.displayName = displayName;
        this.email = email;
    }

    public InstantUser() { }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }
}
