package com.example.splitit.model;

public class Member {
    private long id;
    private String name;
    private long groupId;
    private String imageUri;

    public Member(long id, String name, long groupId, String imageUri) {
        this.id = id;
        this.name = name;
        this.groupId = groupId;
        this.imageUri = imageUri;
    }

    public Member(String name, long groupId, String imageUri) {
        this.name = name;
        this.groupId = groupId;
        this.imageUri = imageUri;
    }

    public Member(String name, long groupId) {
        this.name = name;
        this.groupId = groupId;
    }

    public String getImageUri() {
        return imageUri;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public long getGroupId() { return groupId; }
    public void setId(long id) { this.id = id; }
}

