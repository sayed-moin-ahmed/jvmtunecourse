package com.examples.spring.boot.rest.controller;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.examples.spring.boot.rest.model.Employee;
import com.examples.spring.boot.rest.model.ResponseMessage;
import com.examples.spring.boot.rest.service.EmployeeService;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
	
	Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	
	@Autowired
	EmployeeService empService;
	
//	Create Employee 	POST	/employees
//	Get All Employees	GET		/employees
//	Update Employee		PUT		/employees/{id}
//	Delete Employee		DELETE	/employees/{id}
//	Get Employee		GET		/employees/{id}
	
	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},  produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<ResponseMessage> createEmployee(@RequestBody @Valid Employee employee, BindingResult error) throws Exception
	{
			// Uncomment to test Generic Exception Handling behaviour
//			if(employee.getName().contains("test"))
//			{
//				throw new Exception("Invalid Name");
//			}
		
			logger.info("Have Errors? {}", error.hasErrors());
			
			// MethodArgumentNotValidException will be thrown if any validation error automatically
			
//			if(error.hasErrors()) {
//				throw new MethodArgumentNotValidException(null,error);
//			}
//			
			empService.add(employee);
			
			// Getting current resource path
	        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
	            "/{id}").buildAndExpand(employee.getId()).toUri();
	        
			return ResponseEntity.created(location).body(this.getResponse(employee.getId(), "Employee Created"));
	}
	
	@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public List<Employee> getAll()
	{
		return empService.list();
	}	

	@PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},  produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<ResponseMessage> updateEmployee(@RequestBody @Valid Employee employee, @PathVariable Integer id)
	{
		employee.setId(id);
		empService.update(employee);

		return ResponseEntity.ok().body(this.getResponse(employee.getId(), "Employee Updated"));
	}
	
	@DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<ResponseMessage> deleteEmployee(@PathVariable Integer id)
	{
		empService.delete(id);
		
		ResponseMessage response = getResponse(id, "Employee Deleted");
		
		return ResponseEntity.accepted().body(response);
	}
	
	@GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public Employee getEmployee(@PathVariable Integer id)
	{
		return empService.get(id);
	}
	
	@PostMapping("/loadTest")
	public String loadTest(@RequestParam int empCount) {
		Employee emp;
		for(int i=0; i < empCount; i++) {
			emp = new Employee();
			emp.setName("test" + i);
			emp.setAge(20 + i % 60);
			emp.setDepartment("dept" + i);
			emp.setDesignation("desig" + i);
			emp.setCountry("country" + i);
			
			empService.add(emp);				
		}			
		return "Created " + empCount + " Employees";
	}

	private ResponseMessage getResponse(Integer id, String message) {
		ResponseMessage response = new ResponseMessage();
		response.setId(id);
		response.setStatus(HttpStatus.OK.name());
		response.setStatusCode(HttpStatus.OK.value());
		response.setMessage(message);
		return response;
	}
	
	private ResponseMessage getErrorResponse(Integer id, String message) {
		ResponseMessage response = new ResponseMessage();
		response.setId(id);
		response.setStatus(HttpStatus.BAD_REQUEST.name());
		response.setStatusCode(HttpStatus.BAD_REQUEST.value());
		response.setMessage(message);
		return response;
	}	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	private ResponseEntity<ResponseMessage> handleValidationException(MethodArgumentNotValidException ex)
	{
		FieldError error = ex.getBindingResult().getFieldError("name");
		System.out.println("Error Message: " + error.getCode() + " - " + error.getDefaultMessage());
		return ResponseEntity.badRequest().body(this.getErrorResponse(-1, error.getDefaultMessage()));
		
	}
}
