package vn.com.vtcc.apiExpose.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HtmlData {

    private String url;
    private String html;
    private String content;

    @JsonProperty("published_time")
    private String publishedTime;

    @JsonProperty("created_time")
    private String createdTime;

    public HtmlData() {
    }

    public HtmlData(String url, String html, String content, String publishedTime, String createdTime) {
        this.url = url;
        this.html = html;
        this.content = content;
        this.publishedTime = publishedTime;
        this.createdTime = createdTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublishedTime() {
        return publishedTime;
    }

    public void setPublishedTime(String publishedTime) {
        this.publishedTime = publishedTime;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
