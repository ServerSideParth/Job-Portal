package com.luv2code.jobportal.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "job_seeker_apply")
public class JobSeekerApply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "apply_date")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date applyDate;

    @Column(name = "cover_letter")
    private String coverLetter;

    @ManyToOne
    @JoinColumn(name = "job")
    private JobPostActivity job;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private JobSeekerProfile userId;

    public JobSeekerApply() {
    }

    public JobSeekerApply(Integer id, Date applyDate,
                          String coverLetter, JobPostActivity job,
                          JobSeekerProfile userId) {
        this.id = id;
        this.applyDate = applyDate;
        this.coverLetter = coverLetter;
        this.job = job;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public JobPostActivity getJob() {
        return job;
    }

    public void setJob(JobPostActivity job) {
        this.job = job;
    }

    public JobSeekerProfile getUserId() {
        return userId;
    }

    public void setUserId(JobSeekerProfile userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "JobSeekerApply{" +
                "id=" + id +
                ", applyDate=" + applyDate +
                ", coverLetter='" + coverLetter + '\'' +
                ", job=" + job +
                ", userId=" + userId +
                '}';
    }
}
