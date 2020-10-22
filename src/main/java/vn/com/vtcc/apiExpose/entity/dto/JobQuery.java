package vn.com.vtcc.apiExpose.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JobQuery {

    @JsonProperty("job_id")
    private String id;

    public JobQuery() {
    }

    public JobQuery(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
