package com.project.librefit.librefit2;

import java.io.Serializable;

public class account_info implements Serializable{
String name,email,image_url,UID;

    public account_info(String name, String email, String image_url,String UID) {
        this.name = name;
        this.email = email;
        this.image_url = image_url;
        this.UID=UID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public account_info() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
