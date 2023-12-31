package com.examples.java.empapp.service;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.examples.java.empapp.model.Employee;

public class EmployeeService {

	Map<Integer, Employee> employees = new HashMap<>();
	
	//Map<String, Integer> impExpCount = new HashMap<>();
	
	Integer impExpCount = 0;
	
	private synchronized void incrementCount() {
			impExpCount++;
	}
	
	public Integer getImportExportCount() {
		 return impExpCount;
	}
	
	Comparator<Employee> EMPLOYEE_NAME_ASC_SORT = new Comparator<Employee>() {
		@Override
		public int compare(Employee o1, Employee o2) {
			return o1.getName().compareTo(o2.getName());

		}
	};

	public EmployeeService() {
		// Initializing with employee data
		employees.put(1, new Employee(1, "Anil", 45, "Delivery Manager", "Operations", "India"));
		employees.put(2, new Employee(2, "Swapnil", 35, "Quality Analyst", "Quality", "India"));
		employees.put(3, new Employee(3, "Georgil", 42, "Manager", "Operations", "USA"));
		employees.put(4, new Employee(4, "Sunil", 26, "Associate", "Operations", "India"));
		employees.put(5, new Employee(5, "Saril", 30, "Lead Associate", "Operations", "UK"));
		employees.put(6, new Employee(6, "Vinil", 36, "Manager", "Admin", "Australia"));
	}

	public boolean create(Employee employee) {
		employee.setEmpId(employees.size() + 1);
		return employees.put(employee.getEmpId(), employee) != null ? true : false;
	}

	public Employee get(int id) {
		return employees.get(id);
	}

	public List<Employee> getAll() {
		ArrayList<Employee> empList = new ArrayList<Employee>(employees.values());
//		Collections.sort(empList, EMPLOYEE_NAME_ASC_SORT);
		return empList;

	}

	public boolean update(Employee employee) {
		return employees.put(employee.getEmpId(), employee) != null ? true : false;
	}

	public boolean delete(int id) {
		return employees.remove(id) != null ? true : false;
	}
	
	public boolean deleteAll() {
		employees = new HashMap<>();
		return true;
	}
	
	public int getCount() {
		return employees.entrySet().size();
	}

	public boolean validate(Employee emp, String msg, Predicate<Employee> condition,
			Function<String, Boolean> operation) {
		if (!condition.test(emp)) {
			return operation.apply(msg);
		}
		return true;
	}

	// Get Employee count greater than given age
	public long getEmployeeCountAgeGreaterThan(Predicate<Employee> condition) {
		long count = employees.values().stream().filter(condition).count();

		return count;
	}

	// Get list of Employee IDs whose age is greater than given age
	public List<Integer> getEmployeeIdsAgeGreaterThan(int age) {
		List<Integer> empIds = employees.values().stream().filter(emp -> emp.getAge() > age).map(emp -> emp.getEmpId())
				.collect(Collectors.toList());
		return empIds;
	}

	// Get Department wise Employee count
	public Map<String, Long> getEmployeeCountByDepartment() {

		return employees.values()
				.stream()
				.collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()));
		// Key - Department name
		// Value - Count
	}

	// Get Department wise Employee count ordered by Department name
	public Map<String, Long> getEmployeeCountByDepartmentOdered() {
		return employees.values()
				.stream()
				.sorted(Comparator.comparing(Employee::getDepartment))
				.collect(Collectors.groupingBy(Employee::getDepartment, LinkedHashMap::new, Collectors.counting()));
	}

	// Get Department wise average Employee age ordered by Department name
	public Map<String, Double> getAvgEmployeeAgeByDept() {
		return employees.values()
				.stream()
				.sorted(Comparator.comparing(Employee::getDepartment))
				.collect(Collectors.groupingBy(Employee::getDepartment, TreeMap::new, Collectors.averagingInt(Employee::getAge)));
	}

	// Get Departments have more than given Employee count
	public List<String> getDepartmentsHaveEmployeesMoreThan(int criteria) {
		// List<String> deptList = new ArrayList<>();

		return employees.values()
				.stream()
				.sorted(Comparator.comparing(Employee::getDepartment))
				.collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()))
				// .forEach((k,v) -> {if(v > criteria) {deptList.add(k);}});
				// return deptList;

				.entrySet().stream() // Creating one more stream to filter the output
				.filter(entry -> entry.getValue() > criteria).map(Map.Entry::getKey).collect(Collectors.toList());
	}

	// Get Employee names starting with given string
	public List<String> getEmployeeNamesStartsWith(String prefix) {
		return employees.values()
				.stream()
				.filter(emp -> emp.getName().startsWith(prefix))
				.map(emp -> emp.getName())
				.collect(Collectors.toList());
	}

	public synchronized void bulkImport() {
		int counter = 0;
		try (Scanner in = new Scanner(new FileReader("./input/employee-input1.txt"))) {
			while (in.hasNextLine()) {
				String emp = in.nextLine();
				Employee employee = new Employee();
				StringTokenizer tokenizer = new StringTokenizer(emp, ",");

				// Emp ID
//				employee.setEmpId(Integer.parseInt(tokenizer.nextToken()));
				// Name
				employee.setName(tokenizer.nextToken());
				// Age
				employee.setAge(Integer.parseInt(tokenizer.nextToken()));
				// Designation
				employee.setDesignation(tokenizer.nextToken());
				// Department
				employee.setDepartment(tokenizer.nextToken());
				// Country
				employee.setCountry(tokenizer.nextToken());

//				employees.put(employee.getEmpId(), employee);
				create(employee);
				incrementCount();
				counter++;
			}
			System.out.format("%d Employees are imported successfully.\n", counter);
		} catch (IOException e) {
			System.out.println("Error occured while importing employee data. " + e.getMessage());
		}
	}

	public synchronized void bulkExport() {
		try (FileWriter out = new FileWriter("./output/employee-output.txt")) {
			employees
					.values().stream().map(emp -> emp.getEmpId() + "," + emp.getName() + "," + emp.getAge() + ","
							+ emp.getDesignation() + "," + emp.getDepartment() + "," + emp.getCountry() + "\n")
					.forEach(rec -> {
						try {
							out.write(rec);
							incrementCount();
						} catch (IOException e) {
							System.out
									.println("Error occured while writing employee data into file. " + e.getMessage());
							e.printStackTrace();
						}
					});
			System.out.format("%d Employees are exported successfully.\n", employees.values().size());
			
		} catch (IOException e) {
			System.out.println("Error occured while exporting employee data. " + e.getMessage());
		}
	}
}
