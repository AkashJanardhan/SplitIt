package com.example.splitit.model;

public class Group {
    private long id;
    private String name;

    public Group(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Group(String name) {
        this.name = name;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public void setId(long id) { this.id = id; }
}
