package com.luv2code.jobportal.repository;

import com.luv2code.jobportal.entity.JobPostActivity;
import com.luv2code.jobportal.entity.JobSeekerApply;
import com.luv2code.jobportal.entity.JobSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobSeekerApplyRepository extends JpaRepository<JobSeekerApply,Integer> {

//    This will give me the total jobs applied by a job seeker with its id
    List<JobSeekerApply> findByUserId(JobSeekerProfile userId);

//   This will give the no of seeker applied for a particular job
    List<JobSeekerApply> findByJob(JobPostActivity job);

}
