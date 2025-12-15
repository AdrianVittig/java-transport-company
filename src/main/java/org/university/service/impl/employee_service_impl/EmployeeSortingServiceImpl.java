package org.university.service.impl.employee_service_impl;

import org.university.dao.EmployeeDao;
import org.university.entity.Employee;
import org.university.service.contract.employee_service.EmployeeSortingService;

import java.util.List;

public class EmployeeSortingServiceImpl implements EmployeeSortingService {
    private final EmployeeDao employeeDao;

    public EmployeeSortingServiceImpl(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    private int getQualificationIndex(Employee employee) {
        if(employee.getDriverQualifications() == null ||
        employee.getDriverQualifications().isEmpty()) {
            return -1;
        }

        return employee.getDriverQualifications()
                .stream()
                .mapToInt(Enum::ordinal)
                .max()
                .orElse(-1);
    }

    @Override
    public List<Employee> sortEmployeesBySalaryAscending(boolean isAscending) {
        return employeeDao.sortEmployeesBySalary(isAscending);
    }

    @Override
    public List<Employee> sortEmployeesByQualificationAscending(boolean isAscending) {
        return employeeDao.sortEmployeesByQualification(isAscending);
    }

}
