package vn.com.vtcc.apiExpose.entity;

import lombok.Getter;
import lombok.Setter;
import vn.com.vtcc.apiExpose.utils.IDGenerator;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "job_request")
public class JobRequest {

    @Id
    private String id;
    private String query;
    private String jobState;
    private long createdTime;
    private long updatedTime;

    protected JobRequest() {}

    public JobRequest(String query, String jobState, long updatedTime, long createdTime) {
        this.id = IDGenerator.genID();
        this.query = query;
        this.jobState = jobState;
        this.updatedTime = updatedTime;
        this.createdTime = createdTime;
    }

    public JobRequest(String id, String query, String jobState, long updatedTime, long createdTime) {
        this.id = id;
        this.query = query;
        this.jobState = jobState;
        this.updatedTime = updatedTime;
        this.createdTime = createdTime;
    }
}
