package org.university.service.contract.employee_service;

import org.university.entity.Employee;

import java.util.List;

public interface EmployeeSortingService {
    List<Employee> sortEmployeesBySalaryAscending();
    List<Employee> sortEmployeesBySalaryDescending();
    List<Employee> sortEmployeesByQualificationAscending();
    List<Employee> sortEmployeesByQualificationDescending();
}
