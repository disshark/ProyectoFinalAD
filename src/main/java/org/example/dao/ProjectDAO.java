package org.example.dao;

import org.example.dto.ProjectCostDTO;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import java.util.List;

public class ProjectDAO {

    public List<ProjectCostDTO> getProjectCosts() {
        String sql = """
                SELECT 
                    p.project_id AS projectId,
                    sc.total_salary_cost AS totalSalaryCost,
                    p.budget AS budget,
                    (sc.total_salary_cost / p.budget) AS budgetFraction
                FROM projects p
                INNER JOIN (
                    SELECT
                        ep.project_id,
                        SUM((e.salary / 1900) * ep.hours_worked) AS total_salary_cost
                    FROM employee_projects ep
                    JOIN employees_realistic e ON ep.employee_id = e.employee_id
                    GROUP BY ep.project_id
                ) AS sc ON p.project_id = sc.project_id
                """;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            NativeQuery<Object[]> query = session.createNativeQuery(sql);
            query.addScalar("projectId", Integer.class);
            query.addScalar("totalSalaryCost", Double.class);
            query.addScalar("budget", Double.class);
            query.addScalar("budgetFraction", Double.class);

            // Mapear los resultados al DTO
            return query.list().stream()
                    .map(row -> new ProjectCostDTO(
                            (Integer) row[0],
                            (Double) row[1],
                            (Double) row[2],
                            (Double) row[3]
                    ))
                    .toList();
        }
    }
}