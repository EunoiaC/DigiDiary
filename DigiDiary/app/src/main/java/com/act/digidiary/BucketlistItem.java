package com.act.digidiary;

public class BucketlistItem{

    String title;
    Boolean isCompleted;
    int id;

    public BucketlistItem( String title, Boolean isCompleted, int id){
        this.title = title;
        this.isCompleted= isCompleted;
        this.id = id;
    }

    public void isCompleted(Boolean b){
        isCompleted = b;
    }
}
