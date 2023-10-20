package com.examples.empapp;

import com.examples.empapp.dao.EmployeeDao;
import com.examples.empapp.model.Employee;
import com.examples.empapp.service.EmployeeService;
import com.examples.empapp.service.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    EmployeeDao empDao;

//    @Mock
//    Employee employee;

    @InjectMocks
    EmployeeService empService = new EmployeeServiceImpl();

    @Test
    public void shouldCreateEmployee_whenPassingMandatoryDetails() {
//        when(empDao.create(new Employee())).thenReturn(true);

        // String name, int age, String designation, String department, String country
        Employee emp = new Employee("Sanjay", 30, "Lead", "IT", "India");

        when(empDao.create(emp)).thenReturn(true);

        boolean status = empService.create(emp);
        assertTrue(status);
    }
}
