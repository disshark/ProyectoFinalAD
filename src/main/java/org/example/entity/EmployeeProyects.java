package org.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "employee_projects")
@IdClass(EmployeeProyectsId.class)
public class EmployeeProyects {
    @Id
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeesRealistic employeeId;

    @Id
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Projects projectId;

    @Column(name = "hours_worked", nullable = false)
    private double hoursWorked;

    public EmployeesRealistic getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(EmployeesRealistic employeeId) {
        this.employeeId = employeeId;
    }

    public Projects getProjectId() {
        return projectId;
    }

    public void setProjectId(Projects projectId) {
        this.projectId = projectId;
    }

    public double getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }
}
