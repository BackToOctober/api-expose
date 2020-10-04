package vn.com.vtcc.apiExpose.entity;

import vn.com.vtcc.apiExpose.utils.IDGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "job_request")
public class JobRequest {

    @Id
    private String id;

    @Column(columnDefinition = "longtext")
    private String query;
    private String jobState;
    private int retry;
    private long createdTime;
    private long updatedTime;
    @Column(columnDefinition = "longtext")
    private String files;

    public JobRequest() {}

    public JobRequest(String query, String jobState, long updatedTime, long createdTime) {
        this(IDGenerator.genID(), query, jobState, 0, updatedTime, createdTime, null);
    }

    public JobRequest(String id, String query, String jobState, long updatedTime, long createdTime) {
        this(id, query, jobState, 0, updatedTime, createdTime, null);
    }

    public JobRequest(String id, String query, String jobState, int retry, long updatedTime, long createdTime) {
        this(id, query, jobState, retry, updatedTime, createdTime, null);
    }

    public JobRequest(String id, String query, String jobState, int retry, long updatedTime, long createdTime, String files) {
        this.id = id;
        this.query = query;
        this.jobState = jobState;
        this.retry = retry;
        this.updatedTime = updatedTime;
        this.createdTime = createdTime;
        this.files = files;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getJobState() {
        return jobState;
    }

    public void setJobState(String jobState) {
        this.jobState = jobState;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
    }
}
