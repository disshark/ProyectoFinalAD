package org.example.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "projects")
public class Projects {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private int projectId;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private int department;

    @Column(name = "budget", nullable = false)
    private double budget;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getDepartment() {
        return department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
