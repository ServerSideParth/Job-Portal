package com.luv2code.jobportal.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "job_seeker_profile")
public class JobSeekerProfile {
    @Id
    @Column(name = "user_account_id")
    private Integer userAccountId;

    @OneToOne
    @JoinColumn(name = "user_account_id")
    @MapsId
    private Users userId;
    @Column(name = "city")
    private String city;
    @Column(name = "state")
    private String state;
    @Column(name = "country")
    private String country;
    @Column(name = "employment_type")
    private String employmentType;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "resume")
    private String resume;

    @Column(name = "work_authorization")
    private String workAuthorization;

    @Column(name = "profile_photo",nullable = true,length = 64)
    private String profilePhoto;

    @OneToMany(mappedBy = "jobSeekerProfile",cascade = CascadeType.ALL)
    private List<Skills> skills;
    public JobSeekerProfile() {
    }
    public JobSeekerProfile(Users users){
        this.userId=users;
    }

    public JobSeekerProfile(Integer userAccountId, Users userId, String city,
                            String state, String country,
                            String employmentType, String firstName,
                            String lastName, String resume,
                            String workAuthorization, String profilePhoto,
                            List<Skills> skills) {
        this.userAccountId = userAccountId;
        this.userId = userId;
        this.city = city;
        this.state = state;
        this.country = country;
        this.employmentType = employmentType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.resume = resume;
        this.workAuthorization = workAuthorization;
        this.profilePhoto = profilePhoto;
        this.skills = skills;
    }

    public Integer getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(Integer userAccountId) {
        this.userAccountId = userAccountId;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getWorkAuthorization() {
        return workAuthorization;
    }

    public void setWorkAuthorization(String workAuthorization) {
        this.workAuthorization = workAuthorization;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public List<Skills> getSkills() {
        return skills;
    }

    public void setSkills(List<Skills> skills) {
        this.skills = skills;
    }

    @Transient
    public String getPhotosImagePath(){
        if(profilePhoto==null || userAccountId==null){
            return null;
        }
        return "/photos/candidate/"+userAccountId+"/"+profilePhoto;
    }

    @Override
    public String toString() {
        return "jobSeekerProfile{" +
                "userAccountId=" + userAccountId +
                ", userId=" + userId +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", employmentType='" + employmentType + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", resume='" + resume + '\'' +
                ", workAuthorization='" + workAuthorization + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                '}';
    }
}
