package org.example.dto;

public class ProjectCostDTO {
    private Integer projectId;
    private Double totalSalaryCost;
    private Double budget;
    private Double budgetFraction;

    public ProjectCostDTO(Integer projectId, Double totalSalaryCost, Double budget, Double budgetFraction) {
        this.projectId = projectId;
        this.totalSalaryCost = totalSalaryCost;
        this.budget = budget;
        this.budgetFraction = budgetFraction;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Double getTotalSalaryCost() {
        return totalSalaryCost;
    }

    public void setTotalSalaryCost(Double totalSalaryCost) {
        this.totalSalaryCost = totalSalaryCost;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Double getBudgetFraction() {
        return budgetFraction;
    }

    public void setBudgetFraction(Double budgetFraction) {
        this.budgetFraction = budgetFraction;
    }
}
