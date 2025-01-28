package com.luv2code.jobportal.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "skills")
public class Skills {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "experience_level")
    private String experienceLevel;
    @Column(name = "name")
    private String name;
    @Column(name = "years_of_experience")
    private String yearsOfExperience;

    @ManyToOne
    @JoinColumn(name = "job_seeker_profile")
    private JobSeekerProfile jobSeekerProfile;

    public Skills() {

    }

    public Skills(String experienceLevel, String name,
                  String yearsOfExperience, JobSeekerProfile jobSeekerProfileId) {
        this.experienceLevel = experienceLevel;
        this.name = name;
        this.yearsOfExperience = yearsOfExperience;
        this.jobSeekerProfile = jobSeekerProfileId;
    }

    public int getId() {
        return id;
    }


    public String getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(String yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public JobSeekerProfile getJobSeekerProfile() {
        return jobSeekerProfile;
    }

    public void setJobSeekerProfile(JobSeekerProfile jobSeekerProfileId) {
        this.jobSeekerProfile = jobSeekerProfileId;
    }

    @Override
    public String toString() {
        return "Skills{" +
                "id=" + id +
                ", experienceLevel='" + experienceLevel + '\'' +
                ", name='" + name + '\'' +
                ", yearsOfExperience='" + yearsOfExperience + '\'' +
                ", jobSeekerProfileId=" + jobSeekerProfile +
                '}';
    }
}
