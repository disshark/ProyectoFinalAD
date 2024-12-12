package org.example;

import org.example.dao.ProjectDAO;
import org.example.dto.ProjectCostDTO;

import java.io.File;
import java.sql.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        File file = new File("company_database.db");
        if (!file.exists()) {
            Database.connect();
            Database.createTables();
            Database.importCustomers("src/main/resources/customers.csv");
            Database.importDepartments("src/main/resources/departments.csv");
            Database.importEmployeeProjects("src/main/resources/employee_projects.csv");
            Database.importEmployees("src/main/resources/employees_realistic.csv");
            Database.importOrderItems("src/main/resources/order_items.csv");
            Database.importOrders("src/main/resources/orders.csv");
            Database.importProjects("src/main/resources/projects.csv");
        }

        consultaHibernate();
    }

    public static void consultaNormal() {
        String url = "jdbc:sqlite:company_database.db";
        String query = """
            SELECT 
                p.project_id AS projectId,
                sc.total_salary_cost AS totalSalaryCost,
                p.project_id AS projectId1,
                p.budget AS budget,
                (sc.total_salary_cost / p.budget) AS budgetFraction
            FROM projects p
            INNER JOIN (
                SELECT
                    ep.project_id,
                    SUM((e.salary / 1900) * ep.hours_worked) AS total_salary_cost
                FROM employee_projects ep
                INNER JOIN employees_realistic e ON ep.employee_id = e.employee_id
                GROUP BY ep.project_id
            ) AS sc ON p.project_id = sc.project_id;
        """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.printf("%-10s | %-20s | %-15s | %-15s | %-15s%n", "proyect_id", "proyect_salary_costs", "proyect_id:1", "budget", "cost_fraction");
            System.out.println("--------------------------------------------------------------------------------------");

            // Procesar los resultados
            while (rs.next()) {
                int projectId = rs.getInt("projectId");
                double totalSalaryCost = Math.round(rs.getDouble("totalSalaryCost") * 10.0) / 10.0;
                int projectId1 = rs.getInt("projectId1");
                double budget = rs.getDouble("budget");
                double budgetFraction = rs.getDouble("budgetFraction");

                System.out.printf("%-10d | %-20.1f | %-15d | %-15.2f | %-15.1f%n", projectId, totalSalaryCost, projectId1, budget, (budgetFraction * 100));
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void consultaHibernate() {
        ProjectDAO projectDAO = new ProjectDAO();

        // Obtener los costos de los proyectos
        List<ProjectCostDTO> projectCosts = projectDAO.getProjectCosts();

        System.out.printf("%-10s | %-20s | %-15s | %-15s | %-15s%n", "proyect_id", "proyect_salary_costs", "proyect_id:1", "budget", "cost_fraction");
        System.out.println("---------------------------------------------------------------------------------------");

        // Procesar y mostrar los resultados de la consulta
        for (ProjectCostDTO dto : projectCosts) {
            System.out.printf("%-10d | %-20.1f | %-15d | %-15.2f | %-15.1f%n",
                    dto.getProjectId(),
                    dto.getTotalSalaryCost(),
                    dto.getProjectId(),
                    dto.getBudget(),
                    dto.getBudgetFraction() * 100);
        }
    }
}