package com.banmanwa.web.auth.dto;

public class ProfileTokenDto {

    private String id;
    private String nickname;
    private String profileImage;
    private String token;

    public ProfileTokenDto(ProfileDto profile, String token) {
        this.id = profile.getId();
        this.nickname = profile.getNickname();
        this.profileImage = profile.getProfileImage();
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
