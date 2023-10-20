package com.examples.empapp.dao;

import com.examples.empapp.model.Employee;
//import com.mysql.cj.jdbc.MysqlDataSource;
//import org.postgresql.ds.PGSimpleDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class EmployeeDaoJdbcImpl implements EmployeeDao {

	Connection conn = null;
	Statement stmt = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	// Eager Initialization
	public EmployeeDaoJdbcImpl() {

	}

	// Lazy Initialization
	public Connection getConnection() {
		try {
			if(conn == null) {
				// Option #1: Connecting to MySQL database
//				MysqlDataSource datasource = new MysqlDataSource();
//				datasource.setServerName("localhost");
//				datasource.setDatabaseName("training");
//				datasource.setUser("training");
//				datasource.setPassword("training");
//				conn = datasource.getConnection();
//				System.out.println("Connected to mysql server.");

				// Option #2: Connecting to Postgres database
//				PGSimpleDataSource datasource = new PGSimpleDataSource();
//				datasource.setServerNames(new String[]{"localhost"});
//				datasource.setDatabaseName("training");
//				datasource.setUser("postgres");
//				datasource.setPassword("postgres");
//				conn = datasource.getConnection();
//				System.out.println("Connected to postgres DB server.");

				// Option #3: Connecting to H2 DB
				// Option #3a: In Memory H2 DB
				String jdbcURL = "jdbc:h2:mem:empapp";
				conn = DriverManager.getConnection(jdbcURL);
				System.out.println("Connected to H2 in-memory database.");

				// Option #3b: Embedded H2 DB - Persists the data to disk (location: ~/h2/empapp)
//				String jdbcURL = "jdbc:h2:~/h2/empapp";
//				String username = "sa";
//				String password = "";
//				conn = DriverManager.getConnection(jdbcURL, username, password);
//				System.out.println("Connected to H2 embedded database.");

				// Option #3c: Server H2 DB - Persists the data to disk (location: ~/h2/empapp)
//				String jdbcURL = "jdbc:h2:tcp://localhost/~/h2/empapp";
//				String username = "sa";
//				String password = "";
//				conn = DriverManager.getConnection(jdbcURL, username, password);
//				System.out.println("Connected to H2 in server mode.");

				System.out.println("Connection created successfully. " + conn);
			}
		} catch (SQLException e) {
			System.out.println("ERROR: " + e.getMessage());
			e.printStackTrace();
		}
		return conn;
	}

	public boolean create(Employee employee) {
		// INSERT employee data
		boolean status = false;
		try {
//			stmt = conn.createStatement();
//			String query = "INSERT INTO employee(name, age, designation, department, country) values(\""
//					+ employee.getName() + "\"," + employee.getAge() + ",\"" + employee.getDesignation() + "\",\""
//					+ employee.getDepartment() + "\",\"" + employee.getCountry() + "\")";

			String query = "INSERT INTO employee(name, age, designation, department, country) values(?,?,?,?,?)";
			pstmt = getConnection().prepareStatement(query);
			pstmt.setString(1, employee.getName());
			pstmt.setInt(2, employee.getAge());
			pstmt.setString(3, employee.getDesignation());
			pstmt.setString(4, employee.getDepartment());
			pstmt.setString(5, employee.getCountry());

//			status = pstmt.execute(query);
//			pstmt.execute();
			status = pstmt.executeUpdate() > 0 ? true : false;

		} catch (SQLException e) {
			if(e.getMessage().contains("Table") &&
					(e.getMessage().contains("not found") || e.getMessage().contains("doesn't exist"))) {
				System.out.println("EMPLOYEE table not found");
				this.createTable();
			} else {
				e.printStackTrace();
			}
		}
		return status;
	}

	public boolean update(Employee employee) {
		// UPDATE employee data
		boolean status = false;
		try {
//			stmt = conn.createStatement();

//			String query = "UPDATE employee SET name = \"" + employee.getName() + "\", age = " + employee.getAge()
//					+ ",designation = \"" + employee.getDesignation() + "\",department = \"" + employee.getDepartment()
//					+ "\", country = \"" + employee.getCountry() + "\" WHERE id = " + employee.getEmpId();

			String query = "UPDATE employee SET name=?, age=?, designation=?, department=?, country=? WHERE id = ?";
			pstmt = getConnection().prepareStatement(query);
			pstmt.setString(1, employee.getName());
			pstmt.setInt(2, employee.getAge());
			pstmt.setString(3, employee.getDesignation());
			pstmt.setString(4, employee.getDepartment());
			pstmt.setString(5, employee.getCountry());
			pstmt.setInt(6, employee.getEmpId());

			System.out.println(query);
//			status = stmt.executeUpdate(query) > 0 ? true : false;
			status = pstmt.executeUpdate() > 0 ? true : false;

		} catch (SQLException e) {
			if(e.getMessage().contains("Table") &&
					(e.getMessage().contains("not found") || e.getMessage().contains("doesn't exist"))) {				System.out.println("EMPLOYEE table not found");
				this.createTable();
			} else {
				e.printStackTrace();
			}
		}
		return status;
	}

	public boolean delete(int id) {
		// DELETE employee data
		boolean status = false;
		try {
			stmt = getConnection().createStatement();

			String query = "DELETE FROM employee WHERE id = " + id;

			status = stmt.execute(query);
		} catch (SQLException e) {
			if(e.getMessage().contains("Table") &&
					(e.getMessage().contains("not found") || e.getMessage().contains("doesn't exist"))) {				System.out.println("EMPLOYEE table not found");
				this.createTable();
			} else {
				e.printStackTrace();
			}
		}
		return status;
	}

	public boolean deleteAll() {
		// DELETE all employees data
		boolean status = false;
		try {
			stmt = getConnection().createStatement();

			String query = "DELETE FROM employee";

			status = stmt.execute(query);
		} catch (SQLException e) {
			if(e.getMessage().contains("Table") &&
					(e.getMessage().contains("not found") || e.getMessage().contains("doesn't exist"))) {
				System.out.println("EMPLOYEE table not found");
				this.createTable();
			} else {
				e.printStackTrace();
			}		}
		return status;
	}

	public Employee get(int empId) {
		// SELECT employee data
		Employee emp = null;
		String query = "SELECT * FROM employee WHERE id = " + empId;
		try {
			stmt = getConnection().createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String designation = rs.getString("designation");
				String department = rs.getString("department");
				String country = rs.getString("country");
				emp = new Employee(id, name, age, designation, department, country);
			}
		} catch (SQLException e) {
			if(e.getMessage().contains("Table") &&
					(e.getMessage().contains("not found") || e.getMessage().contains("doesn't exist"))) {				System.out.println("EMPLOYEE table not found");
				this.createTable();
			} else {
				e.printStackTrace();
			}
		}
		return emp;
	}

	public List<Employee> getAll() {
		// SELECT All employees
		List<Employee> employees = new ArrayList<>();
		try {
			stmt = getConnection().createStatement();
			rs = stmt.executeQuery("SELECT * FROM employee");

			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String designation = rs.getString("designation");				
				String department = rs.getString("department");
				String country = rs.getString("country");
				employees.add(new Employee(id, name, age, designation, department, country));
			}
		} catch (SQLException e) {
			if(e.getMessage().contains("Table") &&
					(e.getMessage().contains("not found") || e.getMessage().contains("doesn't exist"))) {
				System.out.println("EMPLOYEE table not found");
				this.createTable();
			} else {
				e.printStackTrace();
			}
		}

		return employees;
	}

	public int getCount() {
		int count = 0;
		// Get employees count
		String query = "SELECT COUNT(*) FROM employee";
		try {
			stmt = getConnection().createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			if(e.getMessage().contains("Table") &&
					(e.getMessage().contains("not found") || e.getMessage().contains("doesn't exist"))) {
				System.out.println("EMPLOYEE table not found");
				this.createTable();
			} else {
				e.printStackTrace();
			}		}
		return count;
	}

	public boolean createTable() {
		// CREATE employee table
		boolean status = false;
		try {
			stmt = getConnection().createStatement();

			// H2 & MySQL database
			String query = "CREATE TABLE IF NOT EXISTS employee (id int auto_increment , name text, age int, designation text, department text, country text, PRIMARY KEY(id))";

			// Postgres database
//			String query = "CREATE TABLE IF NOT EXISTS employee (id serial , name text, age int, designation text, department text, country text, PRIMARY KEY(id))";

			System.out.println(query);
			status = stmt.execute(query);

			System.out.println("Table created successfully");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return status;
	}
}