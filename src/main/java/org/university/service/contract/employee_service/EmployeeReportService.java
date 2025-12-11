package org.university.service.contract.employee_service;

import java.math.BigDecimal;

public interface EmployeeReportService {
    BigDecimal getAverageTransportRevenuePerEmployee(Long employeeId);
}
