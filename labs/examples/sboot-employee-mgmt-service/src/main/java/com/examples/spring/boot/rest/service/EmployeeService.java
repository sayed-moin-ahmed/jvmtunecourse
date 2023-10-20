package com.examples.spring.boot.rest.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.examples.spring.boot.rest.model.Employee;

@Service
public class EmployeeService {

	private static Map<Integer, Employee> employees = new LinkedHashMap<Integer, Employee>();

	public Integer add(Employee employee) {

		employee.setId(employees.size() + 1);

		employees.put(employee.getId(), employee);

		return employee.getId();
	}

	public void update(Employee employee) {
		employees.put(employee.getId(), employee);
	}

	public Employee get(Integer empId) {

		return employees.get(empId);
	}

	public void delete(Integer empId) {

		employees.remove(empId);
	}

	public List<Employee> list() {

		return new ArrayList<Employee>(employees.values());
	}

	public void clear() {
		employees.clear();
	}

}
