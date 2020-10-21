package vn.com.vtcc.apiExpose.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.vtcc.apiExpose.entity.model.JobRequest;

@Repository
public interface JobRequestRepository extends CrudRepository<JobRequest, String> {
}
