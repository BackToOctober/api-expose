package vn.com.vtcc.apiExpose.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.vtcc.apiExpose.entity.JobStateRequest;

@Repository
public interface JobStateRequestRepository extends CrudRepository<JobStateRequest, String> {
}
