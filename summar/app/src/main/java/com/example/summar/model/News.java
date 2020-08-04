package com.example.summar.model;

import java.io.Serializable;

public class News implements Serializable {

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getTitle() {
        return title;
    }

    public String getArticle() {
        return article;
    }

    public String getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }

    public String getPic_url() {
        return pic_url;
    }

    public String getSummary() {
        return summary;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDate() {
        return date;
    }

    public String getClassification() {
        return classification;
    }

    private String _id;

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    private String article_id;
    private String title;
    private String article;
    private String classification; //classification
    private String date;
    private String time;
    private String url;
    private String pic_url;
    private String summary;
    private String keyword;
//===============上面是新的===============
    private String id;
    private String imageUrl;


    public News() {}
    public News(String pic_url, String title, String summary) {
        this.pic_url = pic_url;
        this.title = title;
        this.summary = summary;
    }
    public News(String title, String summary) {
        this.title = title;
        this.summary = summary;
    }
    public News(String id,String imageUrl,String title, String summary,String url,String date,String classification,String keyword) {
        this.id=id;
        this.imageUrl=imageUrl;
        this.title = title;
        this.summary = summary;
        this.url=url;
        this.date=date;
        this.classification=classification;
        this.keyword = keyword;
    }

    public News(String _id, String title, String article, String classification, String date, String time, String url, String pic_url, String summary, String keyword){
        this._id = _id;
        this.title = title;
        this.article=article;
        this.classification=classification;
        this.date=date;
        this.time=time;
        this.url=url;
        this.pic_url=pic_url;
        this.summary=summary;
        this.keyword=keyword;
    }

}
