package org.example.entity;

import java.io.Serializable;
import java.util.Objects;

public class EmployeeProyectsId implements Serializable {
    private int employee;
    private int project;

    public EmployeeProyectsId() {}

    public int getEmployee() {
        return employee;
    }

    public void setEmployee(int employee) {
        this.employee = employee;
    }

    public int getProject() {
        return project;
    }

    public void setProject(int project) {
        this.project = project;
    }

    @Override
    public int hashCode() {
        return Objects.hash(employee, project);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EmployeeProyectsId that = (EmployeeProyectsId) obj;
        return employee == that.employee && project == that.project;
    }
}