package org.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "departments")
public class Departments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private int department_id;

    @Column(name = "department_name", nullable = false)
    private String departmentName;

    @Column(name = "manager_id")
    private Integer managerId;

    public int getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }
}
