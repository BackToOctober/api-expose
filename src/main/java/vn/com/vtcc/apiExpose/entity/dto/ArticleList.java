package vn.com.vtcc.apiExpose.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ArticleList {

    @JsonProperty("list_article")
    private List<String> articleList;

    public ArticleList(List<String> articleList) {
        this.articleList = articleList;
    }

    public ArticleList() {
    }

    public List<String> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<String> articleList) {
        this.articleList = articleList;
    }
}
