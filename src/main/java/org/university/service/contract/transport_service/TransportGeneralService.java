package org.university.service.contract.transport_service;

import org.university.dto.TransportDto;

import java.math.BigDecimal;
import java.util.Set;

public interface TransportGeneralService extends TransportCRUDSystemService, TransportPricingSystemService, TransportFilterService, TransportPaymentSystemService,
TransportSortingService, TransportReportService{
}
