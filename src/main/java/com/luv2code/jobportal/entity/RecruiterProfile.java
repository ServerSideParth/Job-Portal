package com.luv2code.jobportal.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "recruiter_profile")
public class RecruiterProfile {
    @Id
    @Column(name = "user_account_id`")
    private int userAccountId;

    @OneToOne
    @JoinColumn(name = "user_account_id")
    @MapsId
    private Users userId;
    @Column(name = "city")
    private String city;
    @Column(name = "company")
    private String company;
    @Column(name = "country")
    private String country;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "profile_photo",nullable = true,length = 64)
    private String profilePhoto;
    @Column(name = "state")
    private String state;

    public RecruiterProfile() {

    }
    public RecruiterProfile(Users users) {
      this.userId=users;
    }


    public RecruiterProfile(int userAccountId, Users userId,
                            String city, String company,
                            String country, String firstName,
                            String lastName, String profilePhoto,
                            String state) {
        this.userAccountId = userAccountId;
        this.userId = userId;
        this.city = city;
        this.company = company;
        this.country = country;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePhoto = profilePhoto;
        this.state = state;
    }

    public int getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(int userAccountId) {
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    @Transient
    public String getPhotosImagePath(){
        if(profilePhoto==null){
            return null;
        }
        return "/photos/recruiter/"+userAccountId+"/"+profilePhoto;
    }

    @Override
    public String toString() {
        return "RecruiterProfile{" +
                "userAccountId=" + userAccountId +
                ", userId=" + userId +
                ", city='" + city + '\'' +
                ", company='" + company + '\'' +
                ", country='" + country + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
