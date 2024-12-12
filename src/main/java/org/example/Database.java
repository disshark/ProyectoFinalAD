package org.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class Database {
    private static final String url = "jdbc:sqlite:company_database.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Conexión exitosa a la base de datos SQLite.");
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos SQLite.");
            e.printStackTrace();
        }
        return conn;
    }

    public static void createTables() {
        String createCustomersTable =
                "CREATE TABLE IF NOT EXISTS customers (\n" +
                        "    customer_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "    customer_name TEXT NOT NULL,\n" +
                        "    contact_email TEXT,\n" +
                        "    contact_phone TEXT\n" +
                        ");";

        String createDepartmentsTable =
                "CREATE TABLE IF NOT EXISTS departments (\n" +
                        "    department_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "    department_name TEXT NOT NULL,\n" +
                        "    manager_id INTEGER,\n" +
                        "    FOREIGN KEY(manager_id) REFERENCES employees_realistic(employee_id)\n" +
                        ");";

        String createEmployeeProjectsTable =
                "CREATE TABLE IF NOT EXISTS employee_projects (\n" +
                        "    employee_id INTEGER,\n" +
                        "    project_id INTEGER,\n" +
                        "    hours_worked REAL NOT NULL,\n" +
                        "    PRIMARY KEY (employee_id, project_id),\n" +
                        "    FOREIGN KEY(employee_id) REFERENCES employees_realistic(employee_id),\n" +
                        "    FOREIGN KEY(project_id) REFERENCES projects(project_id)\n" +
                        ");";

        String createEmployeesTable =
                "CREATE TABLE IF NOT EXISTS employees_realistic (\n" +
                        "    employee_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "    first_name TEXT NOT NULL,\n" +
                        "    last_name TEXT NOT NULL,\n" +
                        "    department_id INTEGER,\n" +
                        "    hire_date TEXT NOT NULL,\n" +
                        "    salary REAL NOT NULL,\n" +
                        "    position TEXT NOT NULL,\n" +
                        "    FOREIGN KEY(department_id) REFERENCES departments(department_id)\n" +
                        ");";

        String createOrderItemsTable =
                "CREATE TABLE IF NOT EXISTS order_items (\n" +
                        "    order_item_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "    order_id INTEGER NOT NULL,\n" +
                        "    product_name TEXT NOT NULL,\n" +
                        "    quantity INTEGER NOT NULL,\n" +
                        "    price REAL NOT NULL,\n" +
                        "    FOREIGN KEY(order_id) REFERENCES orders(order_id)\n" +
                        ");";

        String createOrdersTable =
                "CREATE TABLE IF NOT EXISTS orders (\n" +
                        "    order_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "    customer_id INTEGER NOT NULL,\n" +
                        "    order_date TEXT NOT NULL,\n" +
                        "    amount REAL NOT NULL,\n" +
                        "    FOREIGN KEY(customer_id) REFERENCES customers(customer_id)\n" +
                        ");";

        String createProjectsTable =
                "CREATE TABLE IF NOT EXISTS projects (\n" +
                        "    project_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "    project_name TEXT NOT NULL,\n" +
                        "    department_id INTEGER,\n" +
                        "    budget REAL NOT NULL,\n" +
                        "    start_date TEXT NOT NULL,\n" +
                        "    end_date TEXT,\n" +
                        "    FOREIGN KEY(department_id) REFERENCES departments(department_id)\n" +
                        ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute(createCustomersTable);
            stmt.execute(createDepartmentsTable);
            stmt.execute(createEmployeesTable);
            stmt.execute(createEmployeeProjectsTable);
            stmt.execute(createOrderItemsTable);
            stmt.execute(createOrdersTable);
            stmt.execute(createProjectsTable);

            System.out.println("Tablas creadas exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al crear las tablas.");
            e.printStackTrace();
        }
    }

    public static void importCustomers(String csvFilePath) {
        String query = "INSERT INTO customers (customer_name, contact_email, contact_phone) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             BufferedReader br = new BufferedReader(new FileReader(csvFilePath));
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Saltar encabezado
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                pstmt.setString(1, data[0]);
                pstmt.setString(2, data[1]);
                pstmt.setString(3, data[2]);
                pstmt.executeUpdate();
            }
            System.out.println("Clientes importados correctamente.");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void importDepartments(String csvFilePath) {
        String query = "INSERT INTO departments (department_id, department_name, manager_id) VALUES (?, ?, ?)";

        try (Connection conn = connect();
             BufferedReader br = new BufferedReader(new FileReader(csvFilePath));
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Saltar encabezado
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                try {
                    pstmt.setInt(1, Integer.parseInt(data[0].trim()));
                    pstmt.setString(2, data[1].trim());
                    pstmt.setInt(3, Integer.parseInt(data[2].trim()));

                    pstmt.executeUpdate();
                } catch (NumberFormatException e) {
                    System.err.println("Error en los datos del CSV: " + line);
                    e.printStackTrace();
                }
            }
            System.out.println("Departamentos importados correctamente.");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void importEmployees(String csvFilePath) {
        String query = "INSERT INTO employees_realistic (employee_id, first_name, last_name, department_id, hire_date, salary, position) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             BufferedReader br = new BufferedReader(new FileReader(csvFilePath));
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Saltar la primera línea (encabezado)
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                try {
                    pstmt.setInt(1, Integer.parseInt(data[0].trim()));
                    pstmt.setString(2, data[1].trim());
                    pstmt.setString(3, data[2].trim());
                    pstmt.setInt(4, Integer.parseInt(data[3].trim()));
                    pstmt.setString(5, data[4].trim());
                    pstmt.setDouble(6, Double.parseDouble(data[5].trim()));
                    pstmt.setString(7, data[6].trim());

                    pstmt.executeUpdate();
                } catch (NumberFormatException e) {
                    System.err.println("Error al procesar la fila: " + line);
                    e.printStackTrace();
                }
            }

            System.out.println("Empleados importados correctamente.");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void importEmployeeProjects(String csvFilePath) {
        String query = "INSERT INTO employee_projects (employee_id, project_id, hours_worked) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             BufferedReader br = new BufferedReader(new FileReader(csvFilePath));
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Saltar encabezado
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                pstmt.setInt(1, Integer.parseInt(data[0]));
                pstmt.setInt(2, Integer.parseInt(data[1]));
                pstmt.setDouble(3, Double.parseDouble(data[2]));
                pstmt.executeUpdate();
            }
            System.out.println("Proyectos de empleados importados correctamente.");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void importOrders(String csvFilePath) {
        String query = "INSERT INTO orders (order_id, customer_id, order_date, amount) VALUES (?, ?, ?, ?)";

        try (Connection conn = connect();
             BufferedReader br = new BufferedReader(new FileReader(csvFilePath));
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Saltar el encabezado
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                try {
                    pstmt.setInt(1, Integer.parseInt(data[0].trim()));
                    pstmt.setInt(2, Integer.parseInt(data[1].trim()));
                    pstmt.setString(3, data[2].trim());
                    pstmt.setDouble(4, Double.parseDouble(data[3].trim()));

                    pstmt.executeUpdate();
                } catch (NumberFormatException e) {
                    System.err.println("Error en la fila: " + line);
                    e.printStackTrace();
                }
            }

            System.out.println("Órdenes importadas correctamente.");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }


    public static void importOrderItems(String csvFilePath) {
        String query = "INSERT INTO order_items (order_item_id, order_id, product_name, quantity, price) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             BufferedReader br = new BufferedReader(new FileReader(csvFilePath));
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Saltar la primera línea (encabezado)
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                try {
                    pstmt.setInt(1, Integer.parseInt(data[0].trim()));
                    pstmt.setInt(2, Integer.parseInt(data[1].trim()));
                    pstmt.setString(3, data[2].trim());
                    pstmt.setInt(4, Integer.parseInt(data[3].trim()));
                    pstmt.setDouble(5, Double.parseDouble(data[4].trim()));

                    pstmt.executeUpdate();
                } catch (NumberFormatException e) {
                    System.err.println("Error en la fila: " + line);
                    e.printStackTrace();
                }
            }

            System.out.println("Artículos de órdenes importados correctamente.");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void importProjects(String csvFilePath) {
        String query = "INSERT INTO projects (project_id, project_name, department_id, budget, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             BufferedReader br = new BufferedReader(new FileReader(csvFilePath));
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Saltar el encabezado
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                try {
                    pstmt.setInt(1, Integer.parseInt(data[0].trim()));
                    pstmt.setString(2, data[1].trim());
                    pstmt.setInt(3, Integer.parseInt(data[2].trim()));
                    pstmt.setDouble(4, Double.parseDouble(data[3].trim()));
                    pstmt.setString(5, data[4].trim());
                    pstmt.setString(6, data[5].trim());

                    pstmt.executeUpdate();
                } catch (NumberFormatException e) {
                    System.err.println("Error en la fila: " + line);
                    e.printStackTrace();
                }
            }

            System.out.println("Proyectos importados correctamente.");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

}
