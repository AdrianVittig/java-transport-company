package org.university.service.contract.transport_service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public interface TransportReportService {
    int getTransportsCount();
    BigDecimal getTotalTransportRevenue();
    Map<Long, Integer> getTransportsCountByDriver();
    BigDecimal getCompanyRevenueForAPeriod(Long companyId,
                                           LocalDate startDate,
                                           LocalDate endDate);
    Map<Long, BigDecimal> getDriverRevenue();
}
