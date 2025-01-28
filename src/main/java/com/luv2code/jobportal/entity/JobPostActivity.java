package com.luv2code.jobportal.entity;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "job_post_activity")
public class JobPostActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_post_id")
    private int jobPostId;

    @Column(name = "description_of_job")
    @Length(max = 10000)
    private String descriptionOfJob;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "job_type")
    private String jobType;

    @Column(name = "posted_date")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date postedDate;

    @Column(name = "remote")
    private String remote;

    @Column(name = "salary")
    private String salary;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_company_id")
    private JobCompany jobCompanyId;

    @Transient
    private Boolean isActive;

    @Transient
    private Boolean isSaved;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_location_id")
    private JobLocation jobLocationId;

    @ManyToOne
    @JoinColumn(name = "posted_by_id")
    private Users postedById;

    public JobPostActivity(){

    }

    public JobPostActivity(int jobPostId, String descriptionOfJob,
                           String jobTitle, String jobType,
                           Date postedDate, String remote,
                           String salary, JobCompany jobCompanyId,
                           JobLocation jobLocationId, Users postedById,
                           boolean isActive,Boolean isSaved) {
        this.jobPostId = jobPostId;
        this.descriptionOfJob = descriptionOfJob;
        this.jobTitle = jobTitle;
        this.jobType = jobType;
        this.postedDate = postedDate;
        this.remote = remote;
        this.salary = salary;
        this.jobCompanyId = jobCompanyId;
        this.jobLocationId = jobLocationId;
        this.postedById = postedById;
        this.isActive=isActive;
        this.isSaved=isSaved;
    }

    public int getJobPostId() {
        return jobPostId;
    }

    public void setJobPostId(int jobPostId) {
        this.jobPostId = jobPostId;
    }

    public String getDescriptionOfJob() {
        return descriptionOfJob;
    }

    public void setDescriptionOfJob(String descriptionOfJob) {
        this.descriptionOfJob = descriptionOfJob;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public String getRemote() {
        return remote;
    }

    public void setRemote(String remote) {
        this.remote = remote;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public JobCompany getJobCompanyId() {
        return jobCompanyId;
    }

    public void setJobCompanyId(JobCompany jobCompanyId) {
        this.jobCompanyId = jobCompanyId;
    }

    public JobLocation getJobLocationId() {
        return jobLocationId;
    }

    public void setJobLocationId(JobLocation jobLocationId) {
        this.jobLocationId = jobLocationId;
    }

    public Users getPostedById() {
        return postedById;
    }

    public void setPostedById(Users postedById) {
        this.postedById = postedById;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Boolean getIsSaved() {
        return isSaved;
    }

    public void setIsSaved(Boolean saved) {
        isSaved = saved;
    }

    @Override
    public String toString() {
        return "JobPostActivity{" +
                "jobPostId=" + jobPostId +
                ", descriptionOfJob='" + descriptionOfJob + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", jobType='" + jobType + '\'' +
                ", postedDate=" + postedDate +
                ", remote='" + remote + '\'' +
                ", salary='" + salary + '\'' +
                ", jobCompanyId=" + jobCompanyId +
                ", isActive=" + isActive +
                ", isSaved=" + isSaved +
                ", jobLocationId=" + jobLocationId +
                ", postedById=" + postedById +
                '}';
    }
}






















