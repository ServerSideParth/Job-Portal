package com.luv2code.jobportal.controller;

import com.luv2code.jobportal.entity.JobSeekerProfile;
import com.luv2code.jobportal.entity.Skills;
import com.luv2code.jobportal.entity.Users;
import com.luv2code.jobportal.repository.UsersRepository;
import com.luv2code.jobportal.services.JobSeekerProfileService;
import com.luv2code.jobportal.util.FileUploadUtil;
import com.luv2code.jobportal.util.fileDownloadUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/job-seeker-profile")
public class JobSeekerProfileController {

    private JobSeekerProfileService jobSeekerProfileService;

    private UsersRepository usersRepository;

    @Autowired
    public JobSeekerProfileController(JobSeekerProfileService jobSeekerProfileService, UsersRepository usersRepository) {
        this.jobSeekerProfileService = jobSeekerProfileService;
        this.usersRepository = usersRepository;
    }

    @GetMapping("/")
    public String jobSeekerProfile(Model model) {
        JobSeekerProfile jobSeekerProfile = new JobSeekerProfile();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Skills> skills = new ArrayList<>();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Users user = usersRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found."));
            Optional<JobSeekerProfile> seekerProfile = jobSeekerProfileService.getOne(user.getUserId());
            if (seekerProfile.isPresent()) {
                jobSeekerProfile = seekerProfile.get();
                if (jobSeekerProfile.getSkills().isEmpty()) {
                    skills.add(new Skills());
                    jobSeekerProfile.setSkills(skills);
                }
            }

            model.addAttribute("skills", skills);
            model.addAttribute("profile", jobSeekerProfile);
        }

        return "job-seeker-profile";
    }

    @PostMapping("/addNew")
    public String addNew(JobSeekerProfile jobSeekerProfile,
                         @RequestParam("image") MultipartFile image,
                         @RequestParam("pdf") MultipartFile pdf,
                         Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Users user = usersRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found."));
            jobSeekerProfile.setUserId(user);
            jobSeekerProfile.setUserAccountId(user.getUserId());
        }

        List<Skills> skillsList = new ArrayList<>();
        model.addAttribute("profile", jobSeekerProfile);
        model.addAttribute("skills", skillsList);

        for (Skills skills : jobSeekerProfile.getSkills()) {

            skills.setJobSeekerProfile(jobSeekerProfile);
        }

        String imageName = "";
        String resumeName = "";

        if (!Objects.equals(image.getOriginalFilename(), "")) {
            imageName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
            jobSeekerProfile.setProfilePhoto(imageName);
        }

        if (!Objects.equals(pdf.getOriginalFilename(), "")) {
            resumeName = StringUtils.cleanPath(Objects.requireNonNull(pdf.getOriginalFilename()));
            jobSeekerProfile.setResume(resumeName);
        }

        JobSeekerProfile seekerProfile = jobSeekerProfileService.addNew(jobSeekerProfile);

        try {
            String uploadDir = "photos/candidate/" + seekerProfile.getUserAccountId();
            if (!Objects.equals(image.getOriginalFilename(), "")) {
                FileUploadUtil.saveFile(uploadDir, imageName, image);
            }
            if (!Objects.equals(pdf.getOriginalFilename(), "")) {
                FileUploadUtil.saveFile(uploadDir, resumeName, pdf);
            }
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return "redirect:/dashboard/";
    }

    @GetMapping("/{id}")
    public String candidateProfile(@PathVariable("id")int id,Model model){
       Optional<JobSeekerProfile>seekerProfile= jobSeekerProfileService.getOne(id);
       model.addAttribute("profile",seekerProfile.get());
       return "job-seeker-profile";
    }

    @GetMapping("/downloadResume")
    public ResponseEntity<?>downloadResume(@RequestParam("fileName") String fileName,
                                           @RequestParam(value = "userID")String userId){
        fileDownloadUtil fileDownloadUtil=new fileDownloadUtil();
        Resource resource=null;

        try{
            resource= fileDownloadUtil.getFileResource("photos/candidate/"+userId,fileName);
        }catch (IOException io){
            return ResponseEntity.badRequest().build();
        }
        if(resource==null){
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }
        String contentType="application/octet-stream";
        String headerValue="attachment; filename=\""+resource.getFilename()+"\"";
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,headerValue)
                .body(resource);
    }
}




/*


MIME Type: MIME (Multipurpose Internet Mail Extensions) types are used to specify the nature and format of a
document, file, or assortment of bytes.

application/octet-stream: This MIME type is a general binary data type. It is used to indicate that the
content should be treated as arbitrary binary data.

Purpose: This MIME type is typically used when the exact content type is unknown or if the content is a
generic binary file. It instructs the browser or client that the content is not specifically
formatted for display, and the browser should offer to download the file instead.





Purpose: The Content-Disposition header is used in HTTP responses to indicate if the content should
be displayed inline in the browser or treated as an attachment to be downloaded and saved locally.

Attachment: The attachment keyword specifies that the content should be downloaded and saved as a file.

Filename: The filename="..." part specifies the suggested name for the file to be saved.





 */

























