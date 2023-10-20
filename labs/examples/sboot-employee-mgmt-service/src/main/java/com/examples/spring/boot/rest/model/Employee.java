package com.examples.spring.boot.rest.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class Employee {

	private Integer id;
	@NotBlank(message="Employee Name cannot be empty.")
	@NotEmpty(message="Employee Name cannot be empty.")
	private String name;
	@Min(value = 20)
	@Max(value = 60)
	private Integer age = null;
	private String gender;
	private boolean contractor;
//	private List<String> skills;	
	private String designation;
	private String department;
	private String address;
	private String country;	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public boolean isContractor() {
		return contractor;
	}

	public void setContractor(boolean contractor) {
		this.contractor = contractor;
	}
	
//	public List<String> getSkills() {
//		return skills;
//	}
//
//	public void setSkills(List<String> skills) {
//		this.skills = skills;
//	}	

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
