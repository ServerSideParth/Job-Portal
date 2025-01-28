package com.luv2code.jobportal.repository;

import com.luv2code.jobportal.entity.JobPostActivity;
import com.luv2code.jobportal.entity.JobSeekerProfile;
import com.luv2code.jobportal.entity.JobSeekerSave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobSeekerSaveRepository  extends JpaRepository<JobSeekerSave,Integer> {

//    This will give  me the no of saved jobs associated with the profile
    List<JobSeekerSave> findByUserId(JobSeekerProfile jobSeekerProfile);

//    This will give me the list of job seeker saves the provided job
    List<JobSeekerSave> findByJob(JobPostActivity job);
}
