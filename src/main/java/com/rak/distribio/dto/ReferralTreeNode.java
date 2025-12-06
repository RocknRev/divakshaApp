package com.rak.distribio.dto;

import java.util.List;

public class ReferralTreeNode {
    private Long userId;
    private String username;
    private boolean isActive;
    private int level;
    private List<ReferralTreeNode> children;
    
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public List<ReferralTreeNode> getChildren() {
        return children;
    }
    public void setChildren(List<ReferralTreeNode> children) {
        this.children = children;
    }

    // Constructors, Getters, Setters
    
}
