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
@Table(name = "job_state_request")
public class JobStateRequest {

    @Id
    private String id;
    private String jobId;
    private long createdTime;

    protected JobStateRequest() {}

    public JobStateRequest(String jobId, long createdTime) {
        this.id = IDGenerator.genID();
        this.jobId = jobId;
        this.createdTime = createdTime;
    }

}
