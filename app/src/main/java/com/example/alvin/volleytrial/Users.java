package com.example.alvin.volleytrial;

import com.google.gson.annotations.SerializedName;

public class Users {
    private int Id;
    private String Name;
    private String Email;
    private String Photo;

//    @SerializedName("id") private int Id;
//    @SerializedName("name") private String Name;
//    @SerializedName("email") private String Email;


    public Users(int id, String name, String email, String photo) {
        Id = id;
        Name = name;
        Email = email;
        Photo = photo;
    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getPhoto() {
        return Photo;
    }

}

