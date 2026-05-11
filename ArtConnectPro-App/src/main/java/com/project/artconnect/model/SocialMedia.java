package com.project.artconnect.model;

public class SocialMedia {
    private String platform;
    private String accountHandle;

    public SocialMedia() {
    }

    public SocialMedia(String platform, String accountHandle) {
        this.platform = platform;
        this.accountHandle = accountHandle;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getAccountHandle() {
        return accountHandle;
    }

    public void setAccountHandle(String accountHandle) {
        this.accountHandle = accountHandle;
    }

    @Override
    public String toString() {
        return platform + " : " + accountHandle;
    }

}
