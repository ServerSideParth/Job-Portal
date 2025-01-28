package com.luv2code.jobportal.services;

import com.luv2code.jobportal.entity.JobSeekerProfile;
import com.luv2code.jobportal.entity.Users;
import com.luv2code.jobportal.repository.JobSeekerProfileRepository;
import com.luv2code.jobportal.repository.UsersRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobSeekerProfileService {

    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final UsersRepository usersService;

    public JobSeekerProfileService(JobSeekerProfileRepository jobSeekerProfileRepository, UsersRepository usersService) {
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.usersService = usersService;
    }

    public Optional<JobSeekerProfile> getOne(Integer id) {
        return jobSeekerProfileRepository.findById(id);
    }

    public JobSeekerProfile addNew(JobSeekerProfile jobSeekerProfile) {
        return jobSeekerProfileRepository.save(jobSeekerProfile);
    }

    public JobSeekerProfile getCurrentSeekerProfile() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
           Users users= usersService.findByEmail(authentication.getName()).orElseThrow(
                   ()->new UsernameNotFoundException("not found")
           );
           return jobSeekerProfileRepository.findById(users.getUserId()).orElseThrow(
                   ()->new UsernameNotFoundException("not found")
           );
        }
        return null;
    }
}
