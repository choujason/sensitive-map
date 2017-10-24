package com.example.po_yeh_chou.textinput_firebase;

/**
 * Created by Po_Yeh_Chou on 24/10/2017.
 */

public class imageUploadInfo {

    public String imageName;

    public String imageURL;

    public imageUploadInfo() {

    }

    public imageUploadInfo(String name, String url) {

        this.imageName = name;
        this.imageURL= url;
    }

    public String getImageName() {
        return imageName;
    }

    public String getImageURL() {
        return imageURL;
    }


}
