package com.examples.empapp;

import com.examples.empapp.model.Employee;
import com.examples.empapp.service.EmployeeService;
import com.examples.empapp.service.EmployeeServiceImpl;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class EmployeeManagementApp {

	private static Scanner in;
	private static EmployeeService empService;

	// Creates Thread Pool
	private static ExecutorService executor = Executors.newFixedThreadPool(5);

	public static void main(String[] args) {

		in = new Scanner(System.in);
		empService = new EmployeeServiceImpl();

		System.out.print("Welcome to Employee Management App!");

		while (true) {

			System.out.println("\n");
			System.out.println("1. Add Employee");
			System.out.println("2. View Employee");
			System.out.println("3. Update Employee");
			System.out.println("4. Delete Employee");
			System.out.println("5. View All Employees");
			System.out.println("6. Delete All Employees");
			System.out.println("7. Get Employees Count");
			System.out.println("8. Print Statistics");
			System.out.println("9. Import");
			System.out.println("10. Export");
			System.out.println("11. Load Test");
			System.out.println("12. Exit");

			System.out.print("Enter the option: ");
			int option = 0;
			// Get option from user
			try {
				option = Integer.parseInt(in.next());
			} catch (NumberFormatException e) {
				System.out.println("Invalid option. Please enter valid option.");
				continue;
			}
			int empId;
			try {
				switch (option) {
					case 1:
						addEmployee();
						System.out.println("Employee has been added successfully!");
						break;
					case 2:
						System.out.print("\nPlease enter employee id: ");
						empId = in.nextInt();
						Employee emp = empService.get(empId);
						printHeader();
						printDetail(emp);
						break;
					case 3:
						System.out.print("\nPlease enter employee id: ");
						empId = in.nextInt();
						Employee empForUpdate = empService.get(empId);
						captureEmpDetail(empForUpdate);
						empService.update(empForUpdate);
						System.out.println("Employee has been updated successfully!");
						break;
					case 4:
						System.out.print("\nPlease enter employee id: ");
						empId = in.nextInt();
						empService.delete(empId);
						System.out.println("Employee has been deleted successfully!");
						break;
					case 5:
						List<Employee> employees = empService.getAll();
						printHeader();
						for (Employee employee : employees) {
							printDetail(employee);
						}
						break;

					case 6:
						empService.deleteAll();
						System.out.println("\nAll Emloyees Deleted");
						break;

					case 7:
						System.out.println("\nEmployees Count: " + empService.getCount());
						break;

					case 8:
						printStatistics();
						break;

					case 9:
						bulkImport();
						break;

					case 10:
						bulkExport();
						break;

					case 11:
						loadTest();
						break;

					case 12:
						System.out.println("Thank you!!!");
						in.close();
						System.exit(0);
						break;

					default:
						break;
				}
			} catch (NumberFormatException e) {
				System.out.println("\nInvalid input. Please enter valid input.");
			}
		}

	}

	private static void printStatistics() {

		System.out.println("\nNo of employees older than thirty years: "
				+ empService.getEmployeeCountAgeGreaterThan(e -> e.getAge() > 30));
		System.out.println("List employee IDs older than thirty years: " + empService.getEmployeeIdsAgeGreaterThan(30));
		System.out.println("Employee count by Department: " + empService.getEmployeeCountByDepartment());
		System.out.println("Employee count by Department ordered: " + empService.getEmployeeCountByDepartmentOdered());
		System.out.println("Average Employee Age by Department: " + empService.getAvgEmployeeAgeByDept());
		System.out.println("List Departments have more than 3 employees: " + empService.getDepartmentsHaveEmployeesMoreThan(3));
		System.out.println("List Employees starts with 'S':" + empService.getEmployeeNamesStartsWith("S"));

	}

	private static void printHeader() {
		System.out.format("\n%5s %15s %5s %15s %15s %15s", "EmpID", "Name", "Age", "Designation", "Department",
				"Country");
	}

	private static void printDetail(Employee emp) {
		if (emp == null) {
			return;
		}

		System.out.format("\n%5d %15s %5d %15s %15s %15s", emp.getEmpId(), emp.getName(), emp.getAge(),
				emp.getDesignation(), emp.getDepartment(), emp.getCountry());
	}

	private static void addEmployee() throws NumberFormatException {
		Employee employee = new Employee();

		captureEmpDetail(employee);

		empService.create(employee);
	}

	private static void captureEmpDetail(Employee employee) throws NumberFormatException {
		System.out.print("\nEnter employee Name: ");
		employee.setName(in.next());

		try {
			boolean val = true;
			do {
				System.out.print("Enter employee Age: ");
				String errorMsg = "Invalid Age. Age should be between 18 to 60.";
				employee.setAge(Integer.parseInt(in.next()));
				val = empService.validate(employee, errorMsg, e -> e.getAge() >= 18 && e.getAge() <= 60, m -> {
					System.out.println(m);
					return false;
				});
			} while (!val);
		} catch (NumberFormatException e) {
			throw e;
		}

		System.out.print("Enter employee Designation: ");
		employee.setDesignation(in.next());

		System.out.print("Enter employee Department: ");
		employee.setDepartment(in.next());

		System.out.print("Enter employee Country: ");
		employee.setCountry(in.next());
	}

	private static void bulkImport() {
		Callable<Boolean> importThread = new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
//				System.out.println(Thread.currentThread() + " Waiting for 5s to start import process.");
				Thread.sleep(2000);
				empService.bulkImport();
				return true;
			}
		};

		Future<Boolean> importFuture = executor.submit(importThread);
		System.out.println(Thread.currentThread().getName() + " Import process triggered");
	}

	private static void bulkExport() {
		Callable<Boolean> exportThread = new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
//				System.out.println(Thread.currentThread() + " Waiting for 5s to start export process.");
				Thread.sleep(2000);
				empService.bulkExport();
				return true;
			}
		};

		Future<Boolean> exportFuture = executor.submit(exportThread);
		System.out.println(Thread.currentThread().getName() + " Export process triggered");
	}

	// Generates load to the application
	private static void loadTest() {
		System.out.print("\nEnter no of employees to create: ");
		int empCount = in.nextInt();
		System.out.print("Enter no of bulk imports/exports : ");
		int impExpCount = in.nextInt();

		System.out.println("Load testing started...");

		createTestEmployees(empCount);

		for(int i = 0; i < impExpCount; i++)
		{
			bulkImport();
			bulkExport();
		}

		System.out.println("Load testing completed...");
	}

	// Creates test employees
	private static void createTestEmployees(int empCount) {
		Employee emp;
		for(int i=0; i < empCount; i++) {
			emp = new Employee();
			emp.setName("test" + i);
			emp.setAge(20 + i % 60);
			emp.setDepartment("dept" + i);
			emp.setDesignation("desig" + i);
			emp.setCountry("country" + i);

			empService.create(emp);
		}
	}

}
