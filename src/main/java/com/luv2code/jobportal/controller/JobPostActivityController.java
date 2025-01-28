package com.luv2code.jobportal.controller;

import com.luv2code.jobportal.entity.*;
import com.luv2code.jobportal.services.JobPostActivityService;
import com.luv2code.jobportal.services.JobSeekerApplyService;
import com.luv2code.jobportal.services.JobSeekerSaveService;
import com.luv2code.jobportal.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
public class JobPostActivityController {

    private final UsersService usersService;
    private final JobPostActivityService jobPostActivityService;

    private final JobSeekerApplyService jobSeekerApplyService;

    private final JobSeekerSaveService jobSeekerSaveService;

    @Autowired
    public JobPostActivityController(UsersService usersService, JobPostActivityService jobPostActivityService, JobSeekerApplyService jobSeekerApplyService, JobSeekerSaveService jobSeekerSaveService) {
        this.usersService = usersService;
        this.jobPostActivityService = jobPostActivityService;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.jobSeekerSaveService = jobSeekerSaveService;
    }

    @GetMapping("/dashboard/")
    public String searchJobs(Model model,
                             @RequestParam(value = "job",required = false)String job,
                             @RequestParam(value = "location",required = false)String location,
                             @RequestParam(value = "partTime",required = false)String partTime,
                             @RequestParam(value = "fullTime",required = false)String fullTime,
                             @RequestParam(value = "freelance",required = false)String freelance,
                             @RequestParam(value = "remoteOnly",required = false)String remoteOnly,
                             @RequestParam(value = "officeOnly",required = false)String officeOnly,
                             @RequestParam(value = "partialRemote",required = false)String partialRemote,
                             @RequestParam(value = "today",required = false)boolean today,
                             @RequestParam(value = "days7",required = false)boolean days7,
                             @RequestParam(value = "days30",required = false)boolean days30




                             ) {
        model.addAttribute("partTime", Objects.equals(partTime,"Part-time"));
        model.addAttribute("fullTime", Objects.equals(fullTime,"Full-time"));
        model.addAttribute("freelance", Objects.equals(freelance,"Freelance"));

        model.addAttribute("remoteOnly", Objects.equals(remoteOnly,"Remote-Only"));
        model.addAttribute("officeOnly", Objects.equals(officeOnly,"Office-Only"));
        model.addAttribute("partialRemote", Objects.equals(partialRemote,"Partial-Remote"));

        model.addAttribute("today", today);
        model.addAttribute("days7", days7);
        model.addAttribute("days30", days30);

        model.addAttribute("job",job);
        model.addAttribute("location",location);

        LocalDate searchDate=null;
        List<JobPostActivity> jobPost=null;
        boolean dateSearchFlag=true;

        boolean remote=true;
        boolean type=true;

        if(days30){
            searchDate=LocalDate.now().minusDays(30);
        } else if (days7) {
            searchDate=LocalDate.now().minusDays(7);
        }
        else if(today){
            searchDate=LocalDate.now();
        }
        else {
            dateSearchFlag=false;
        }
         if(partTime==null && fullTime==null && freelance==null){
             partTime="Part-time";
             fullTime="Full-time";
             freelance="Freelance";
             type=false;
         }
         if(officeOnly==null && remoteOnly==null && partialRemote==null){
             officeOnly="Office-Only";
             remoteOnly="Remote-Only";
             partialRemote="Partial-Remote";
             remote=false;

         }
//
         if(!dateSearchFlag && !remote && !type && !StringUtils.hasText(job) &&
                 !StringUtils.hasText(location)){
             
//             if non of the flag is selected then get all jobs
             jobPost=jobPostActivityService.getAll();
         }
         else{


//             if any of the flags is selected then filters the jobs basis on the flags
             jobPost=jobPostActivityService.search(job,location,Arrays.asList(remoteOnly,officeOnly,partialRemote),
                     Arrays.asList(partTime,fullTime,freelance)
                     ,searchDate);
         }

        Object currentUserProfile = usersService.getCurrentUserProfile();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            model.addAttribute("username", currentUsername);
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
                List<RecruiterJobsDto> recruiterJobs = jobPostActivityService.getRecruiterJobs(((RecruiterProfile) currentUserProfile).getUserAccountId());
                model.addAttribute("jobPost", recruiterJobs);
            }
            else{
//                It gives the all applied jobs associated with a provided profile
                List<JobSeekerApply> jobSeekerApplyList=jobSeekerApplyService.getCandidatesJobs(
                        (JobSeekerProfile)currentUserProfile
                );
//                It gives the job seeker saves based on a provided profile
               List<JobSeekerSave>jobSeekerSaveList=jobSeekerSaveService.getCandidatesJob(
                       (JobSeekerProfile) currentUserProfile
               );
/*               check the job
               if it is applied by an any user mark the job status to be active
               if it is saved by any user mark the job status to be saved
 */
                boolean exist;
                boolean saved;
//                it will take a jobs that we filter then check it is applied or an saved if it is mark it status
                for(JobPostActivity jobActivity:jobPost){
                    exist=false;
                    saved=false;
                    for(JobSeekerApply jobApply:jobSeekerApplyList){
                        if(Objects.equals(jobActivity.getJobPostId(),jobApply.getJob().getJobPostId())){
                            jobActivity.setIsActive(true);
                            exist=true;
                            break;
                        }
                    }
                    for(JobSeekerSave jobSave:jobSeekerSaveList){
                        if(Objects.equals(jobActivity.getJobPostId(),jobSave.getJob().getJobPostId())){
                            jobActivity.setIsSaved(true);
                            saved=true;
                            break;
                        }
                    }
                    if(!exist){
                        jobActivity.setIsActive(false);
                    }
                    if(!saved){
                        jobActivity.setIsSaved(false);
                    }
                }
//                Here filterd jobs add to the model
                model.addAttribute("jobPost",jobPost);
            }
        }

        model.addAttribute("user", currentUserProfile);

        return "dashboard";
    }

    @GetMapping("global-search/")
    public String globalSearch(Model model,
                               @RequestParam(value = "job",required = false)String job,
                               @RequestParam(value = "location",required = false)String location,
                               @RequestParam(value = "partTime",required = false)String partTime,
                               @RequestParam(value = "fullTime",required = false)String fullTime,
                               @RequestParam(value = "freelance",required = false)String freelance,
                               @RequestParam(value = "remoteOnly",required = false)String remoteOnly,
                               @RequestParam(value = "officeOnly",required = false)String officeOnly,
                               @RequestParam(value = "partialRemote",required = false)String partialRemote,
                               @RequestParam(value = "today",required = false)boolean today,
                               @RequestParam(value = "days7",required = false)boolean days7,
                               @RequestParam(value = "days30",required = false)boolean days30){

        model.addAttribute("partTime", Objects.equals(partTime,"Part-time"));
        model.addAttribute("fullTime", Objects.equals(fullTime,"Full-time"));
        model.addAttribute("freelance", Objects.equals(freelance,"Freelance"));

        model.addAttribute("remoteOnly", Objects.equals(remoteOnly,"Remote-Only"));
        model.addAttribute("officeOnly", Objects.equals(officeOnly,"Office-Only"));
        model.addAttribute("partialRemote", Objects.equals(partialRemote,"Partial-Remote"));

        model.addAttribute("today", today);
        model.addAttribute("days7", days7);
        model.addAttribute("days30", days30);

        model.addAttribute("job",job);
        model.addAttribute("location",location);

        LocalDate searchDate=null;
        List<JobPostActivity> jobPost=null;
        boolean dateSearchFlag=true;

        boolean remote=true;
        boolean type=true;

        if(days30){
            searchDate=LocalDate.now().minusDays(30);
        } else if (days7) {
            searchDate=LocalDate.now().minusDays(7);
        }
        else if(today){
            searchDate=LocalDate.now();
        }
        else {
            dateSearchFlag=false;
        }
        if(partTime==null && fullTime==null && freelance==null){
            partTime="Part-time";
            fullTime="Full-time";
            freelance="Freelance";
            type=false;
        }
        if(officeOnly==null && remoteOnly==null && partialRemote==null){
            officeOnly="Office-Only";
            remoteOnly="Remote-Only";
            partialRemote="Partial-Remote";
            remote=false;

        }
//
        if(!dateSearchFlag && !remote && !type && !StringUtils.hasText(job) &&
                !StringUtils.hasText(location)){

//             if non of the flag is selected then get all jobs
            jobPost=jobPostActivityService.getAll();
        }
        else{


//             if any of the flags is selected then filters the jobs basis on the flags
            jobPost=jobPostActivityService.search(job,location,Arrays.asList(remoteOnly,officeOnly,partialRemote),
                    Arrays.asList(partTime,fullTime,freelance)
                    ,searchDate);
        }

          model.addAttribute("jobPost",jobPost);
        return "global-search";
    }

//    This is for showing the add job display
    @GetMapping("/dashboard/add")
    public String addJobs(Model model) {
        model.addAttribute("jobPostActivity", new JobPostActivity());
        model.addAttribute("user", usersService.getCurrentUserProfile());
        return "add-jobs";
    }
// This controller is for submission o
    @PostMapping("/dashboard/addNew")
    public String addNew(JobPostActivity jobPostActivity, Model model) {

        Users user = usersService.getCurrentUser();
        if (user != null) {
            jobPostActivity.setPostedById(user);
        }
        jobPostActivity.setPostedDate(new Date());
        model.addAttribute("jobPostActivity", jobPostActivity);
        JobPostActivity saved = jobPostActivityService.addNew(jobPostActivity);
        return "redirect:/dashboard/";
    }
// This will handle the edit operation of jobs
    @PostMapping("dashboard/edit/{id}")
    public String editJob(@PathVariable("id") int id, Model model) {

        JobPostActivity jobPostActivity = jobPostActivityService.getOne(id);
        model.addAttribute("jobPostActivity", jobPostActivity);
        model.addAttribute("user", usersService.getCurrentUserProfile());
        return "add-jobs";
    }
}








