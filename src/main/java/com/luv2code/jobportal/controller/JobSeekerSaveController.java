package com.luv2code.jobportal.controller;

import com.luv2code.jobportal.entity.JobPostActivity;
import com.luv2code.jobportal.entity.JobSeekerProfile;
import com.luv2code.jobportal.entity.JobSeekerSave;
import com.luv2code.jobportal.entity.Users;
import com.luv2code.jobportal.services.*;
import org.springframework.boot.Banner;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class JobSeekerSaveController {
    private final UsersService usersService;
    private final JobSeekerProfileService jobSeekerProfileService;
    private final JobPostActivityService jobPostActivityService;

    private final JobSeekerSaveService jobSeekerSaveService;

    public JobSeekerSaveController(UsersService usersService, JobSeekerProfileService jobSeekerProfileService, JobPostActivityService jobPostActivityService, JobSeekerSaveService jobSeekerSaveService) {
        this.usersService = usersService;
        this.jobSeekerProfileService = jobSeekerProfileService;
        this.jobPostActivityService = jobPostActivityService;
        this.jobSeekerSaveService = jobSeekerSaveService;
    }

//    This will handles event when user clicks the save the job button
    @PostMapping("job-details/save/{id}")
    public String save(@PathVariable("id")int id, JobSeekerSave jobSeekerSave){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
//            first find the job seeker profile and jobs for association
            String email=authentication.getName();
            Users users=usersService.getUserByEmail(email).orElseThrow(()->new UsernameNotFoundException("not found"));
            JobSeekerProfile jobSeekerProfile=jobSeekerProfileService.getOne(users.getUserId()).orElseThrow(()->new UsernameNotFoundException("not found"));
            JobPostActivity jobPostActivity=jobPostActivityService.getOne(id);
            if(jobSeekerProfile!=null && jobPostActivity!=null){
                jobSeekerSave=new JobSeekerSave();
                jobSeekerSave.setJob(jobPostActivity);
                jobSeekerSave.setUserId(jobSeekerProfile);

            }
            else{
                throw new RuntimeException("User not found");
            }
            jobSeekerSaveService.addNew(jobSeekerSave);
        }
        return "redirect:/dashboard/";

    }

//    This will handles when user wants to see their saved jobs

    @GetMapping("saved-jobs/")
    public String savedJobs(Model model){
//        first find the user profile so we can simply find the all saved jobs associated with the profile
        List<JobPostActivity>jobPost=new ArrayList<>();
        Object profile=usersService.getCurrentUserProfile();

            List<JobSeekerSave>jobSeekerSaveList=jobSeekerSaveService.getCandidatesJob((JobSeekerProfile) profile);
            for (JobSeekerSave saved: jobSeekerSaveList){
                jobPost.add(saved.getJob());
            }
            model.addAttribute("jobPost",jobPost);
            model.addAttribute("user",profile);

        return "saved-jobs";
    }
























}
