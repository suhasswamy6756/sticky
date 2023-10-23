package com.suhas.sticky;

import java.util.Date;

public class firebasemodel {

    private  String title;
    private  String content;

    public firebasemodel(){

    }
    public firebasemodel(String title,String content,Date _date_){
        this.content =content;
        this.title = title;

    }


    public  String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;

    }

    public void setContent(String content) {
        this.content = content;

    }



}
