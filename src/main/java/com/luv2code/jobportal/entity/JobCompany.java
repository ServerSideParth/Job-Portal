package com.luv2code.jobportal.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "job_company")
public class JobCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "logo")
    private String logo;

    @Column(name = "name")
    private String name;

    public JobCompany(){


    }

    public JobCompany(int id, String logo, String name) {
        this.id = id;
        this.logo = logo;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "JobCompany{" +
                "id=" + id +
                ", logo='" + logo + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
