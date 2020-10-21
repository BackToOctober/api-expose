package vn.com.vtcc.apiExpose.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ArticleMetadata {

    private String article;

    @JsonProperty("metadata")
    private List<StructType> structTypes;

    public ArticleMetadata() {
    }

    public ArticleMetadata(String article, List<StructType> structTypes) {
        this.article = article;
        this.structTypes = structTypes;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public List<StructType> getStructTypes() {
        return structTypes;
    }

    public void setStructTypes(List<StructType> structTypes) {
        this.structTypes = structTypes;
    }
}
