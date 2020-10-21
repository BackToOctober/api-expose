package vn.com.vtcc.apiExpose.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class JobChecking {
    private String id;
    private List<String> files;

    @JsonProperty("job_state")
    private String jobState;

    public JobChecking() {
    }

    public JobChecking(String id, List<String> files, String jobState) {
        this.id = id;
        this.files = files;
        this.jobState = jobState;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public String getJobState() {
        return jobState;
    }

    public void setJobState(String jobState) {
        this.jobState = jobState;
    }
}
