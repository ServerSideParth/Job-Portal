package com.luv2code.jobportal.controller;

import com.luv2code.jobportal.entity.*;
import com.luv2code.jobportal.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.List;
import java.util.Optional;
// For jobseeker
@Controller
public class JobSeekerApplyController {

    private final JobPostActivityService jobPostActivityService;
    private final UsersService usersService;
    private final JobSeekerApplyService jobSeekerApplyService;
    private final JobSeekerSaveService jobSeekerSaveService;
    private final RecruiterProfileService recruiterProfileService;
    private final JobSeekerProfileService jobSeekerProfileService;

    @Autowired
    public JobSeekerApplyController(JobPostActivityService jobPostActivityService, UsersService usersService, JobSeekerApplyService jobSeekerApplyService, JobSeekerSaveService jobSeekerSaveService, RecruiterProfileService recruiterProfileService, JobSeekerProfileService jobSeekerProfileService) {
        this.jobPostActivityService = jobPostActivityService;
        this.usersService = usersService;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.jobSeekerSaveService = jobSeekerSaveService;
        this.recruiterProfileService = recruiterProfileService;
        this.jobSeekerProfileService = jobSeekerProfileService;
    }

    @GetMapping("job-details-apply/{id}")
    public String display(@PathVariable("id") int id, Model model) {
        JobPostActivity jobDetails = jobPostActivityService.getOne(id);

        List<JobSeekerSave>jobSeekerSaveList=jobSeekerSaveService.getJobCandidates(jobDetails);
        List<JobSeekerApply>jobSeekerApplyList=jobSeekerApplyService.getJobCandidates(jobDetails);

        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))){
                RecruiterProfile user=recruiterProfileService.getCurrentRecruiterProfile();
                if(user!=null){
                    model.addAttribute("applyList",jobSeekerApplyList);
                }

            }
            else {
                JobSeekerProfile user=jobSeekerProfileService.getCurrentSeekerProfile();
                if(user!=null){
                    boolean exist=false;
                    boolean saved=false;
                    for(JobSeekerApply jobSeekerApply:jobSeekerApplyList){
                        if(jobSeekerApply.getUserId().getUserAccountId()== user.getUserAccountId()){
                            exist=true;
                            break;
                        }
                    }
                    for(JobSeekerSave jobSeekerSave:jobSeekerSaveList){
                        if(jobSeekerSave.getUserId().getUserAccountId()==user.getUserAccountId()){
                            saved=true;
                            break;
                        }
                    }
                    model.addAttribute("alreadyApplied",exist);
                    model.addAttribute("alreadySaved",saved);
                }
            }
        }
        model.addAttribute("jobDetails", jobDetails);
        model.addAttribute("user", usersService.getCurrentUserProfile());
        return "job-details";
    }
    @PostMapping("job-details/apply/{id}")
    public String apply(@PathVariable("id")int id,JobSeekerApply jobSeekerApply){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            Users users=usersService.findByEmail(authentication.getName());
            Optional<JobSeekerProfile> jobSeekerProfile=jobSeekerProfileService.getOne(users.getUserId());
            JobPostActivity jobPostActivity=jobPostActivityService.getOne(id);
            if(jobSeekerProfile.isPresent() && jobPostActivity!=null){
                jobSeekerApply=new JobSeekerApply();
                    jobSeekerApply.setUserId(jobSeekerProfile.get());
                    jobSeekerApply.setJob(jobPostActivity);
                    jobSeekerApply.setApplyDate(new Date());
            }
            else {
                throw new RuntimeException("User not found");
            }
            jobSeekerApplyService.addNew(jobSeekerApply);
        }
        return "redirect:/dashboard/";

    }




}












