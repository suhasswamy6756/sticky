package com.suhas.sticky;

public class firebasemodel {

    private  String title;
    private  String content;
    private  String date;

    public firebasemodel(){

    }
    public firebasemodel(String title,String content,String _date_){
        this.content =content;
        this.title = title;
        this.date = _date_;


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

    public String getDate() {
        return date;
    }

    public void setDate(String _date_) {
        this.date = _date_;
    }
}
