package org.university;

import org.university.configuration.SessionFactoryUtil;
import org.university.dao.*;
import org.university.dto.*;
import org.university.entity.Transport;
import org.university.exception.DAOException;
import org.university.service.contract.company_service.*;
import org.university.service.contract.customer_service.*;
import org.university.service.contract.driving_license_service.DrivingLicenseService;
import org.university.service.contract.employee_service.*;
import org.university.service.contract.id_card_service.IdCardService;
import org.university.service.contract.person_service.PersonService;
import org.university.service.contract.transport_service.*;
import org.university.service.contract.vehicle_service.*;
import org.university.service.impl.company_service_impl.*;
import org.university.service.impl.customer_service_impl.*;
import org.university.service.impl.driving_license_service_impl.DrivingLicenseServiceImpl;
import org.university.service.impl.employee_service_impl.*;
import org.university.service.impl.id_card_service_impl.IdCardServiceImpl;
import org.university.service.impl.person_service_impl.PersonServiceImpl;
import org.university.service.impl.transport_service_impl.*;
import org.university.service.impl.vehicle_service_impl.*;
import org.university.util.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Runner {
    private Runner() {}

    public static void runApplication() {
        CompanyDao companyDao = new CompanyDao();
        EmployeeDao employeeDao = new EmployeeDao();
        VehicleDao vehicleDao = new VehicleDao();
        TransportDao transportDao = new TransportDao();
        CustomerDao customerDao = new CustomerDao();
        PersonDao personDao = new PersonDao();
        IdentificationCardDao identificationCardDao = new IdentificationCardDao();
        DrivingLicenseDao drivingLicenseDao = new DrivingLicenseDao();

        CompanyCRUDService companyCrud = new CompanyCRUDServiceImpl(companyDao, employeeDao, vehicleDao, transportDao);
        CompanyFilterService companyFilter = new CompanyFilterServiceImpl(companyDao);
        CompanySortingService companySorting = new CompanySortingServiceImpl(companyDao);
        CompanyReportService companyReport = new CompanyReportServiceImpl(companyDao, transportDao);
        CompanyGeneralService companyGeneral = new CompanyGeneralServiceImpl(companyCrud, companyFilter, companySorting, companyReport);

        CustomerCRUDService customerCrud = new CustomerCRUDServiceImpl(customerDao, transportDao);
        CustomerReportService customerReport = new CustomerReportServiceImpl(customerDao, transportDao);
        CustomerGeneralService customerGeneral = new CustomerGeneralServiceImpl(customerCrud, customerReport);

        VehicleCRUDService vehicleCrud = new VehicleCRUDServiceImpl(vehicleDao, companyDao, employeeDao, transportDao);
        VehicleReportService vehicleReport = new VehicleReportServiceImpl(vehicleDao, transportDao);
        VehicleGeneralService vehicleGeneral = new VehicleGeneralServiceImpl(vehicleCrud, vehicleReport);

        TransportCRUDSystemService transportCrud = new TransportCRUDSystemServiceImpl(transportDao, vehicleDao, companyDao, employeeDao, customerDao);
        TransportPricingSystemService transportPricing = new TransportPricingSystemServiceImpl();
        TransportPaymentSystemService transportPayment = new TransportPaymentSystemServiceImpl(transportDao, customerDao);
        TransportFilterService transportFilter = new TransportFilterServiceImpl(transportDao);
        TransportSortingService transportSorting = new TransportSortingServiceImpl(transportDao);
        TransportReportService transportReport = new TransportReportServiceImpl(transportDao, companyDao);
        TransportGeneralService transportGeneral = new TransportGeneralServiceImpl(
                transportCrud, transportPricing, transportPayment, transportFilter, transportSorting, transportReport
        );
        TransportFileService transportFileService = new TransportFileServiceImpl(transportGeneral);

        EmployeeCRUDService employeeCrud = new EmployeeCRUDServiceImpl(employeeDao, drivingLicenseDao, vehicleDao, transportDao, companyDao);
        EmployeeFilterService employeeFilter = new EmployeeFilterServiceImpl(employeeDao);
        EmployeeSortingService employeeSorting = new EmployeeSortingServiceImpl(employeeDao);
        EmployeeReportService employeeReport = new EmployeeReportServiceImpl(transportDao, employeeDao);
        EmployeeGeneralService employeeGeneral = new EmployeeGeneralServiceImpl(employeeCrud, employeeFilter, employeeSorting, employeeReport);

        PersonService personService = new PersonServiceImpl(personDao, identificationCardDao);
        IdCardService idCardService = new IdCardServiceImpl(identificationCardDao, personDao);
        DrivingLicenseService drivingLicenseService = new DrivingLicenseServiceImpl(drivingLicenseDao, employeeDao);

        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                printMainMenu();
                String choice = readLine(sc, "Choice: ").trim();

                try {
                    switch (choice) {
                        case "1" -> companyMenu(sc, companyGeneral);
                        case "2" -> customerMenu(sc, customerGeneral);
                        case "3" -> employeeMenu(sc, employeeGeneral);
                        case "4" -> vehicleMenu(sc, vehicleGeneral);
                        case "5" -> transportMenu(sc, transportGeneral, transportCrud, transportPricing, transportFileService);
                        case "6" -> personMenu(sc, personService);
                        case "7" -> idCardMenu(sc, idCardService);
                        case "8" -> drivingLicenseMenu(sc, drivingLicenseService);
                        case "0" -> { return; }
                        default -> System.out.println("Invalid choice.");
                    }
                } catch (DAOException e) {
                    System.out.println("Error: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Unexpected error: " + e.getMessage());
                }
            }
        } finally {
            SessionFactoryUtil.getSessionFactory().close();
        }
    }

    private static void printMainMenu() {
        System.out.println("\n===== MAIN MENU =====");
        System.out.println("1) Companies");
        System.out.println("2) Customers");
        System.out.println("3) Employees");
        System.out.println("4) Vehicles");
        System.out.println("5) Transports");
        System.out.println("6) Persons");
        System.out.println("7) Identification cards");
        System.out.println("8) Driving licenses");
        System.out.println("0) Exit");
    }

    private static void companyMenu(Scanner sc, CompanyGeneralService company) {
        while (true) {
            System.out.println("\n--- COMPANY MENU ---");
            System.out.println("1) Create company");
            System.out.println("2) Get company by id");
            System.out.println("3) List all companies");
            System.out.println("4) Update company");
            System.out.println("5) Delete company");
            System.out.println("6) Add employee to company");
            System.out.println("7) Remove employee from company");
            System.out.println("8) Get all employee ids for company");
            System.out.println("9) Add vehicle to company");
            System.out.println("10) Remove vehicle from company");
            System.out.println("11) Get all vehicle ids for company");
            System.out.println("12) Get all transport ids for company");
            System.out.println("13) Filter companies with revenue over threshold");
            System.out.println("14) Filter companies where name includes");
            System.out.println("15) Sort companies by name ASC");
            System.out.println("16) Sort companies by revenue ASC");
            System.out.println("17) Report: company total revenue");
            System.out.println("18) Report: company transports count");
            System.out.println("19) Report: company average transport revenue");
            System.out.println("0) Back");

            String ch = readLine(sc, "Choice: ").trim();

            switch (ch) {
                case "1" -> {
                    CompanyDto dto = new CompanyDto();
                    dto.setName(readRequiredLine(sc, "Name: "));
                    dto.setRevenue(readBigDecimal(sc, "Revenue: "));
                    CompanyDto created = company.createCompany(dto);
                    System.out.println("Created company id=" + created.getId());
                }
                case "2" -> {
                    Long id = readLong(sc, "Company id: ");
                    CompanyDto dto = company.getCompanyById(id);
                    System.out.println("Company: id=" + dto.getId() + ", name=" + dto.getName() + ", revenue=" + dto.getRevenue());
                }
                case "3" -> company.getAllCompanies().forEach(c ->
                        System.out.println("id=" + c.getId() + ", name=" + c.getName() + ", revenue=" + c.getRevenue())
                );
                case "4" -> {
                    Long id = readLong(sc, "Company id: ");
                    CompanyDto dto = new CompanyDto();
                    dto.setName(readRequiredLine(sc, "New name: "));
                    dto.setRevenue(readBigDecimal(sc, "New revenue: "));
                    CompanyDto updated = company.updateCompany(id, dto);
                    System.out.println("Updated company id=" + updated.getId());
                }
                case "5" -> {
                    Long id = readLong(sc, "Company id: ");
                    company.deleteCompany(id);
                    System.out.println("Deleted company id=" + id);
                }
                case "6" -> {
                    Long employeeId = readLong(sc, "Employee id: ");
                    Long companyId = readLong(sc, "Company id: ");
                    company.addEmployeeToCompany(employeeId, companyId);
                    System.out.println("Employee added to company.");
                }
                case "7" -> {
                    Long employeeId = readLong(sc, "Employee id: ");
                    Long companyId = readLong(sc, "Company id: ");
                    company.removeEmployeeFromCompany(employeeId, companyId);
                    System.out.println("Employee removed from company.");
                }
                case "8" -> {
                    Long companyId = readLong(sc, "Company id: ");
                    System.out.println("Employee ids: " + company.getAllEmployeeIdsForCompany(companyId));
                }
                case "9" -> {
                    Long companyId = readLong(sc, "Company id: ");
                    Long vehicleId = readLong(sc, "Vehicle id: ");
                    company.addVehicleToCompany(companyId, vehicleId);
                    System.out.println("Vehicle added to company.");
                }
                case "10" -> {
                    Long companyId = readLong(sc, "Company id: ");
                    Long vehicleId = readLong(sc, "Vehicle id: ");
                    company.removeVehicleFromCompany(companyId, vehicleId);
                    System.out.println("Vehicle removed from company.");
                }
                case "11" -> {
                    Long companyId = readLong(sc, "Company id: ");
                    System.out.println("Vehicle ids: " + company.getAllVehicleIdsForCompany(companyId));
                }
                case "12" -> {
                    Long companyId = readLong(sc, "Company id: ");
                    System.out.println("Transport ids: " + company.getAllTransportIdsForCompany(companyId));
                }
                case "13" -> {
                    BigDecimal threshold = readBigDecimal(sc, "Revenue threshold: ");
                    company.filterCompaniesWithRevenueOver(threshold).forEach(c ->
                            System.out.println("id=" + c.getId() + ", name=" + c.getName() + ", revenue=" + c.getRevenue())
                    );
                }
                case "14" -> {
                    String match = readRequiredLine(sc, "Name contains: ");
                    company.filterCompaniesWhichNameIncludes(match).forEach(c ->
                            System.out.println("id=" + c.getId() + ", name=" + c.getName())
                    );
                }
                case "15" -> company.sortCompaniesByNameAscending(true).forEach(c ->
                        System.out.println("id=" + c.getId() + ", name=" + c.getName())
                );
                case "16" -> company.sortCompaniesByRevenueAscending(true).forEach(c ->
                        System.out.println("id=" + c.getId() + ", revenue=" + c.getRevenue())
                );
                case "17" -> {
                    Long companyId = readLong(sc, "Company id: ");
                    System.out.println("Total revenue: " + company.getCompanyTotalRevenue(companyId));
                }
                case "18" -> {
                    Long companyId = readLong(sc, "Company id: ");
                    System.out.println("Transports count: " + company.getCompanyTransportsCount(companyId));
                }
                case "19" -> {
                    Long companyId = readLong(sc, "Company id: ");
                    System.out.println("Average transport revenue: " + company.getCompanyAverageTransportRevenue(companyId));
                }
                case "0" -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void customerMenu(Scanner sc, CustomerGeneralService customer) {
        while (true) {
            System.out.println("\n--- CUSTOMER MENU ---");
            System.out.println("1) Create customer");
            System.out.println("2) Get customer by id");
            System.out.println("3) List all customers");
            System.out.println("4) Update customer");
            System.out.println("5) Delete customer");
            System.out.println("6) Add transport to customer");
            System.out.println("7) Remove transport from customer");
            System.out.println("8) Get all transport ids for customer");
            System.out.println("9) Report: total spent");
            System.out.println("10) Report: transports count");
            System.out.println("11) Report: average spending per transport");
            System.out.println("0) Back");

            String ch = readLine(sc, "Choice: ").trim();

            switch (ch) {
                case "1" -> {
                    CustomerDto dto = new CustomerDto();
                    dto.setFirstName(readRequiredLine(sc, "First name: "));
                    dto.setLastName(readRequiredLine(sc, "Last name: "));
                    dto.setBirthDate(readDate(sc, "Birth date (YYYY-MM-DD): "));
                    dto.setBudget(readBigDecimal(sc, "Budget: "));
                    CustomerDto created = customer.createCustomer(dto);
                    System.out.println("Created customer id=" + created.getId());
                }
                case "2" -> {
                    Long id = readLong(sc, "Customer id: ");
                    CustomerDto dto = customer.getCustomerById(id);
                    System.out.println("Customer: id=" + dto.getId() + ", name=" + dto.getFirstName() + " " + dto.getLastName() + ", budget=" + dto.getBudget());
                }
                case "3" -> customer.getAllCustomers().forEach(c ->
                        System.out.println("id=" + c.getId() + ", name=" + c.getFirstName() + " " + c.getLastName() + ", budget=" + c.getBudget())
                );
                case "4" -> {
                    Long id = readLong(sc, "Customer id: ");
                    CustomerDto dto = new CustomerDto();
                    dto.setFirstName(readRequiredLine(sc, "New first name: "));
                    dto.setLastName(readRequiredLine(sc, "New last name: "));
                    dto.setBirthDate(readDate(sc, "New birth date (YYYY-MM-DD): "));
                    dto.setBudget(readBigDecimal(sc, "New budget: "));
                    CustomerDto updated = customer.updateCustomer(id, dto);
                    System.out.println("Updated customer id=" + updated.getId());
                }
                case "5" -> {
                    Long id = readLong(sc, "Customer id: ");
                    customer.deleteCustomer(id);
                    System.out.println("Deleted customer id=" + id);
                }
                case "6" -> {
                    Long customerId = readLong(sc, "Customer id: ");
                    Long transportId = readLong(sc, "Transport id: ");
                    customer.addTransportToCustomer(customerId, transportId);
                    System.out.println("Transport added to customer.");
                }
                case "7" -> {
                    Long customerId = readLong(sc, "Customer id: ");
                    Long transportId = readLong(sc, "Transport id: ");
                    customer.removeTransportFromCustomer(customerId, transportId);
                    System.out.println("Transport removed from customer.");
                }
                case "8" -> {
                    Long customerId = readLong(sc, "Customer id: ");
                    System.out.println("Transport ids: " + customer.getAllTransportIdsForCustomer(customerId));
                }
                case "9" -> {
                    Long customerId = readLong(sc, "Customer id: ");
                    System.out.println("Total spent: " + customer.getCustomerTotalSpent(customerId));
                }
                case "10" -> {
                    Long customerId = readLong(sc, "Customer id: ");
                    System.out.println("Transports count: " + customer.getCustomerTransportsCount(customerId));
                }
                case "11" -> {
                    Long customerId = readLong(sc, "Customer id: ");
                    System.out.println("Average spending per transport: " + customer.getAverageSpendingPerTransport(customerId));
                }
                case "0" -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void employeeMenu(Scanner sc, EmployeeGeneralService employee) {
        while (true) {
            System.out.println("\n--- EMPLOYEE MENU ---");
            System.out.println("1) Create employee");
            System.out.println("2) Get employee by id");
            System.out.println("3) List all employees");
            System.out.println("4) Update employee");
            System.out.println("5) Delete employee");
            System.out.println("6) Get company id for employee");
            System.out.println("7) Get driving license id for employee");
            System.out.println("8) Get all vehicle ids for employee");
            System.out.println("9) Get all transport ids for employee");
            System.out.println("10) Filter employees by qualification");
            System.out.println("11) Filter employees by salary >= value");
            System.out.println("12) Sort employees by salary ASC");
            System.out.println("13) Sort employees by qualification ASC");
            System.out.println("14) Report: average transport revenue per employee");
            System.out.println("0) Back");

            String ch = readLine(sc, "Choice: ").trim();

            switch (ch) {
                case "1" -> {
                    EmployeeDto dto = new EmployeeDto();
                    dto.setFirstName(readRequiredLine(sc, "First name: "));
                    dto.setLastName(readRequiredLine(sc, "Last name: "));
                    dto.setBirthDate(readDate(sc, "Birth date (YYYY-MM-DD): "));
                    dto.setSalary(readBigDecimal(sc, "Salary: "));
                    dto.setDriverQualifications(readQualifications(sc, "Qualifications (comma-separated): "));
                    EmployeeDto created = employee.createEmployee(dto);
                    System.out.println("Created employee id=" + created.getId());
                }
                case "2" -> {
                    Long id = readLong(sc, "Employee id: ");
                    EmployeeDto dto = employee.getEmployeeById(id);
                    System.out.println("Employee: id=" + dto.getId()
                            + ", name=" + dto.getFirstName() + " " + dto.getLastName()
                            + ", salary=" + dto.getSalary()
                            + ", qualifications=" + dto.getDriverQualifications()
                            + ", companyId=" + dto.getCompanyId()
                            + ", drivingLicenseId=" + dto.getDrivingLicenseId());
                }
                case "3" -> employee.getAllEmployees().forEach(e ->
                        System.out.println("id=" + e.getId()
                                + ", name=" + e.getFirstName() + " " + e.getLastName()
                                + ", salary=" + e.getSalary()
                                + ", qualifications=" + e.getDriverQualifications()
                                + ", companyId=" + e.getCompanyId()
                                + ", drivingLicenseId=" + e.getDrivingLicenseId())
                );
                case "4" -> {
                    Long id = readLong(sc, "Employee id: ");
                    EmployeeDto dto = new EmployeeDto();
                    dto.setFirstName(readRequiredLine(sc, "New first name: "));
                    dto.setLastName(readRequiredLine(sc, "New last name: "));
                    dto.setBirthDate(readDate(sc, "New birth date (YYYY-MM-DD): "));
                    dto.setSalary(readBigDecimal(sc, "New salary: "));
                    dto.setDriverQualifications(readQualifications(sc, "New qualifications (comma-separated): "));
                    EmployeeDto updated = employee.updateEmployee(id, dto);
                    System.out.println("Updated employee id=" + updated.getId());
                }
                case "5" -> {
                    Long id = readLong(sc, "Employee id: ");
                    employee.deleteEmployee(id);
                    System.out.println("Deleted employee id=" + id);
                }
                case "6" -> {
                    Long employeeId = readLong(sc, "Employee id: ");
                    System.out.println("Company id: " + employee.getCompanyIdForEmployee(employeeId));
                }
                case "7" -> {
                    Long employeeId = readLong(sc, "Employee id: ");
                    System.out.println("Driving license id: " + employee.getDrivingLicenseIdForEmployee(employeeId));
                }
                case "8" -> {
                    Long employeeId = readLong(sc, "Employee id: ");
                    System.out.println("Vehicle ids: " + employee.getAllVehicleIdsForEmployee(employeeId));
                }
                case "9" -> {
                    Long employeeId = readLong(sc, "Employee id: ");
                    System.out.println("Transport ids: " + employee.getAllTransportIdsForEmployee(employeeId));
                }
                case "10" -> {
                    DriverQualifications q = readDriverQualification(sc, "Qualification: ");
                    employee.filterEmployeeByQualification(q).forEach(e ->
                            System.out.println("id=" + e.getId() + ", name=" + e.getFirstName() + " " + e.getLastName() + ", qualifications=" + e.getDriverQualifications())
                    );
                }
                case "11" -> {
                    BigDecimal salary = readBigDecimal(sc, "Salary threshold: ");
                    employee.filterEmployeesBySalary(salary).forEach(e ->
                            System.out.println("id=" + e.getId() + ", name=" + e.getFirstName() + " " + e.getLastName() + ", salary=" + e.getSalary())
                    );
                }
                case "12" -> employee.sortEmployeesBySalaryAscending(true).forEach(e ->
                        System.out.println("id=" + e.getId() + ", salary=" + e.getSalary())
                );
                case "13" -> employee.sortEmployeesByQualificationAscending(true).forEach(e ->
                        System.out.println("id=" + e.getId() + ", qualifications=" + e.getDriverQualifications())
                );
                case "14" -> {
                    Long employeeId = readLong(sc, "Employee id: ");
                    System.out.println("Average transport revenue: " + employee.getAverageTransportRevenuePerEmployee(employeeId));
                }
                case "0" -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void vehicleMenu(Scanner sc, VehicleGeneralService vehicle) {
        while (true) {
            System.out.println("\n--- VEHICLE MENU ---");
            System.out.println("1) Create vehicle");
            System.out.println("2) Get vehicle by id");
            System.out.println("3) List all vehicles");
            System.out.println("4) Update vehicle");
            System.out.println("5) Delete vehicle");
            System.out.println("6) Get company id for vehicle");
            System.out.println("7) Get employee id for vehicle");
            System.out.println("8) Get all transport ids for vehicle");
            System.out.println("9) Report: revenue by vehicle type");
            System.out.println("10) Report: average revenue per transport for vehicle");
            System.out.println("0) Back");

            String ch = readLine(sc, "Choice: ").trim();

            switch (ch) {
                case "1" -> {
                    VehicleDto dto = new VehicleDto();
                    dto.setVehicleType(readVehicleType(sc, "Vehicle type: "));
                    dto.setDistanceTraveled(readBigDecimal(sc, "Distance traveled: "));
                    dto.setCompanyId(readOptionalLongZeroMeansNull(sc, "Company id (0 = none): "));
                    dto.setEmployeeId(readOptionalLongZeroMeansNull(sc, "Employee id (0 = none): "));
                    VehicleDto created = vehicle.createVehicle(dto);
                    System.out.println("Created vehicle id=" + created.getId());
                }
                case "2" -> {
                    Long id = readLong(sc, "Vehicle id: ");
                    VehicleDto dto = vehicle.getVehicleById(id);
                    System.out.println("Vehicle: id=" + dto.getId()
                            + ", type=" + dto.getVehicleType()
                            + ", km=" + dto.getDistanceTraveled()
                            + ", companyId=" + dto.getCompanyId()
                            + ", employeeId=" + dto.getEmployeeId());
                }
                case "3" -> vehicle.getAllVehicles().forEach(v ->
                        System.out.println("id=" + v.getId()
                                + ", type=" + v.getVehicleType()
                                + ", km=" + v.getDistanceTraveled()
                                + ", companyId=" + v.getCompanyId()
                                + ", employeeId=" + v.getEmployeeId())
                );
                case "4" -> {
                    Long id = readLong(sc, "Vehicle id: ");
                    VehicleDto dto = new VehicleDto();
                    dto.setVehicleType(readVehicleType(sc, "New vehicle type: "));
                    dto.setDistanceTraveled(readBigDecimal(sc, "New distance traveled: "));
                    dto.setCompanyId(readOptionalLongZeroMeansNull(sc, "New company id (0 = none): "));
                    dto.setEmployeeId(readOptionalLongZeroMeansNull(sc, "New employee id (0 = none): "));
                    VehicleDto updated = vehicle.updateVehicle(id, dto);
                    System.out.println("Updated vehicle id=" + updated.getId());
                }
                case "5" -> {
                    Long id = readLong(sc, "Vehicle id: ");
                    vehicle.deleteVehicle(id);
                    System.out.println("Deleted vehicle id=" + id);
                }
                case "6" -> {
                    Long vehicleId = readLong(sc, "Vehicle id: ");
                    System.out.println("Company id: " + vehicle.getCompanyIdForVehicle(vehicleId));
                }
                case "7" -> {
                    Long vehicleId = readLong(sc, "Vehicle id: ");
                    System.out.println("Employee id: " + vehicle.getEmployeeIdForVehicle(vehicleId));
                }
                case "8" -> {
                    Long vehicleId = readLong(sc, "Vehicle id: ");
                    System.out.println("Transport ids: " + vehicle.getAllTransportIdsForVehicle(vehicleId));
                }
                case "9" -> {
                    VehicleType type = readVehicleType(sc, "Vehicle type: ");
                    System.out.println("Revenue for type " + type + ": " + vehicle.getRevenueByVehicleType(type));
                }
                case "10" -> {
                    Long vehicleId = readLong(sc, "Vehicle id: ");
                    System.out.println("Average revenue per transport: " + vehicle.getAverageRevenueForAVehiclePerTransport(vehicleId));
                }
                case "0" -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void transportMenu(
            Scanner sc,
            TransportGeneralService transport,
            TransportCRUDSystemService transportCrud,
            TransportPricingSystemService pricing,
            TransportFileService fileService
    ) {
        while (true) {
            System.out.println("\n--- TRANSPORT MENU ---");
            System.out.println("1) Create transport (no blanks)");
            System.out.println("2) Get transport by id");
            System.out.println("3) List all transports");
            System.out.println("4) Update transport (no blanks)");
            System.out.println("5) Delete transport");
            System.out.println("6) Get company id for transport");
            System.out.println("7) Get employee id for transport");
            System.out.println("8) Get customer id for transport");
            System.out.println("9) Get vehicle id for transport");
            System.out.println("10) Filter transports by destination");
            System.out.println("11) Mark transport as PAID");
            System.out.println("12) Pay single transport");
            System.out.println("13) Calculate customer debt");
            System.out.println("14) Get unpaid transport ids for customer");
            System.out.println("15) Calculate and persist total price for transport");
            System.out.println("16) Sort transports by destination ASC");
            System.out.println("17) Report: transports count");
            System.out.println("18) Report: total transport revenue");
            System.out.println("19) Report: transports count by driver");
            System.out.println("20) Report: company revenue for period");
            System.out.println("21) Report: driver revenue");
            System.out.println("22) Save transport to file");
            System.out.println("23) Load transport from file");
            System.out.println("0) Back");

            String ch = readLine(sc, "Choice: ").trim();

            switch (ch) {
                case "1" -> {
                    TransportDto dto = new TransportDto();
                    dto.setStartPoint(readRequiredLine(sc, "Start point: "));
                    dto.setEndPoint(readRequiredLine(sc, "End point: "));
                    dto.setDepartureDate(readDate(sc, "Departure date (YYYY-MM-DD): "));
                    dto.setArrivalDate(readDate(sc, "Arrival date (YYYY-MM-DD): "));
                    dto.setCargoType(readCargoType(sc, "Cargo type (GOODS/PASSENGERS/ADR): "));
                    dto.setInitPrice(readBigDecimal(sc, "Init price: "));
                    dto.setQuantity(readBigDecimal(sc, "Quantity: "));
                    dto.setCompanyId(readLong(sc, "Company id: "));
                    dto.setEmployeeId(readLong(sc, "Employee id: "));
                    dto.setVehicleId(readLong(sc, "Vehicle id: "));
                    dto.setCustomerId(readLong(sc, "Customer id: "));

                    Transport entityForPricing = buildTransportEntityForPricing(dto);
                    BigDecimal total = pricing.calculateTotalPrice(entityForPricing);
                    dto.setTotalPrice(total);
                    dto.setPaymentStatus(entityForPricing.getPaymentStatus());

                    TransportDto created = transportCrud.createTransport(dto);
                    System.out.println("Created transport id=" + created.getId());
                }
                case "2" -> {
                    Long id = readLong(sc, "Transport id: ");
                    TransportDto dto = transport.getTransportById(id);
                    System.out.println("Transport: id=" + dto.getId()
                            + ", route=" + dto.getStartPoint() + " -> " + dto.getEndPoint()
                            + ", departure=" + dto.getDepartureDate()
                            + ", arrival=" + dto.getArrivalDate()
                            + ", cargo=" + dto.getCargoType()
                            + ", init=" + dto.getInitPrice()
                            + ", qty=" + dto.getQuantity()
                            + ", total=" + dto.getTotalPrice()
                            + ", status=" + dto.getPaymentStatus()
                            + ", companyId=" + dto.getCompanyId()
                            + ", employeeId=" + dto.getEmployeeId()
                            + ", vehicleId=" + dto.getVehicleId()
                            + ", customerId=" + dto.getCustomerId());
                }
                case "3" -> transport.getAllTransports().forEach(t ->
                        System.out.println("id=" + t.getId()
                                + ", " + t.getStartPoint() + " -> " + t.getEndPoint()
                                + ", total=" + t.getTotalPrice()
                                + ", status=" + t.getPaymentStatus()
                                + ", companyId=" + t.getCompanyId()
                                + ", employeeId=" + t.getEmployeeId()
                                + ", vehicleId=" + t.getVehicleId()
                                + ", customerId=" + t.getCustomerId())
                );
                case "4" -> {
                    Long id = readLong(sc, "Transport id: ");
                    TransportDto dto = new TransportDto();
                    dto.setStartPoint(readRequiredLine(sc, "New start point: "));
                    dto.setEndPoint(readRequiredLine(sc, "New end point: "));
                    dto.setDepartureDate(readDate(sc, "New departure date (YYYY-MM-DD): "));
                    dto.setArrivalDate(readDate(sc, "New arrival date (YYYY-MM-DD): "));
                    dto.setCargoType(readCargoType(sc, "New cargo type (GOODS/PASSENGERS/ADR): "));
                    dto.setInitPrice(readBigDecimal(sc, "New init price: "));
                    dto.setQuantity(readBigDecimal(sc, "New quantity: "));
                    dto.setCompanyId(readLong(sc, "New company id: "));
                    dto.setEmployeeId(readLong(sc, "New employee id: "));
                    dto.setVehicleId(readLong(sc, "New vehicle id: "));
                    dto.setCustomerId(readLong(sc, "New customer id: "));

                    Transport entityForPricing = buildTransportEntityForPricing(dto);
                    BigDecimal total = pricing.calculateTotalPrice(entityForPricing);
                    dto.setTotalPrice(total);
                    dto.setPaymentStatus(entityForPricing.getPaymentStatus());

                    TransportDto updated = transport.updateTransport(id, dto);
                    System.out.println("Updated transport id=" + updated.getId());
                }
                case "5" -> {
                    Long id = readLong(sc, "Transport id: ");
                    transport.deleteTransport(id);
                    System.out.println("Deleted transport id=" + id);
                }
                case "6" -> {
                    Long id = readLong(sc, "Transport id: ");
                    System.out.println("Company id: " + transport.getCompanyIdForTransport(id));
                }
                case "7" -> {
                    Long id = readLong(sc, "Transport id: ");
                    System.out.println("Employee id: " + transport.getEmployeeIdForTransport(id));
                }
                case "8" -> {
                    Long id = readLong(sc, "Transport id: ");
                    System.out.println("Customer id: " + transport.getCustomerIdForTransport(id));
                }
                case "9" -> {
                    Long id = readLong(sc, "Transport id: ");
                    System.out.println("Vehicle id: " + transport.getVehicleIdForTransport(id));
                }
                case "10" -> {
                    String dest = readRequiredLine(sc, "Destination contains: ");
                    transport.filterTransportsByDestination(dest).forEach(t ->
                            System.out.println("id=" + t.getId() + ", end=" + t.getEndPoint())
                    );
                }
                case "11" -> {
                    Long id = readLong(sc, "Transport id: ");
                    transport.markTransportAsPaid(id);
                    System.out.println("Transport marked as PAID.");
                }
                case "12" -> {
                    Long id = readLong(sc, "Transport id: ");
                    transport.paySingleTransport(id);
                    System.out.println("Payment completed for transport.");
                }
                case "13" -> {
                    Long customerId = readLong(sc, "Customer id: ");
                    System.out.println("Customer debt: " + transport.calculateCustomerDebt(customerId));
                }
                case "14" -> {
                    Long customerId = readLong(sc, "Customer id: ");
                    System.out.println("Unpaid transport ids: " + transport.getUnpaidTransportIdsForCustomer(customerId));
                }
                case "15" -> {
                    Long id = readLong(sc, "Transport id: ");
                    TransportDto dto = transport.getTransportById(id);

                    Transport entity = new Transport();
                    entity.setCargoType(dto.getCargoType());
                    entity.setInitPrice(dto.getInitPrice());
                    entity.setQuantity(dto.getQuantity());

                    BigDecimal total = transport.calculateTotalPrice(entity);
                    dto.setTotalPrice(total);
                    dto.setPaymentStatus(entity.getPaymentStatus());

                    transport.updateTransport(id, dto);
                    System.out.println("Total price calculated and persisted. Total=" + total);
                }
                case "16" -> transport.sortTransportsByDestinationAscending(true).forEach(t ->
                        System.out.println("id=" + t.getId() + ", end=" + t.getEndPoint())
                );
                case "17" -> System.out.println("Transports count: " + transport.getTransportsCount());
                case "18" -> System.out.println("Total transport revenue: " + transport.getTotalTransportRevenue());
                case "19" -> {
                    Map<Long, Integer> map = transport.getTransportsCountByDriver();
                    map.forEach((k, v) -> System.out.println("DriverId=" + k + " -> transports=" + v));
                }
                case "20" -> {
                    Long companyId = readLong(sc, "Company id: ");
                    LocalDate start = readDate(sc, "Start date (YYYY-MM-DD): ");
                    LocalDate end = readDate(sc, "End date (YYYY-MM-DD): ");
                    System.out.println("Company revenue for period: " + transport.getCompanyRevenueForAPeriod(companyId, start, end));
                }
                case "21" -> {
                    Map<Long, BigDecimal> map = transport.getDriverRevenue();
                    map.forEach((k, v) -> System.out.println("DriverId=" + k + " -> revenue=" + v));
                }
                case "22" -> {
                    Long id = readLong(sc, "Transport id: ");
                    fileService.saveTransport(id);
                    System.out.println("Transport saved to file.");
                }
                case "23" -> {
                    Long id = readLong(sc, "Transport id: ");
                    TransportDto dto = fileService.loadTransportsFromFile(id);
                    System.out.println("Loaded from file: id=" + dto.getId()
                            + ", route=" + dto.getStartPoint() + " -> " + dto.getEndPoint()
                            + ", total=" + dto.getTotalPrice()
                            + ", status=" + dto.getPaymentStatus());
                }
                case "0" -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void personMenu(Scanner sc, PersonService person) {
        while (true) {
            System.out.println("\n--- PERSON MENU ---");
            System.out.println("1) Create person");
            System.out.println("2) Get person by id");
            System.out.println("3) List all people");
            System.out.println("4) Update person");
            System.out.println("5) Delete person");
            System.out.println("6) Get identification card id for person");
            System.out.println("0) Back");

            String ch = readLine(sc, "Choice: ").trim();

            switch (ch) {
                case "1" -> {
                    PersonDto dto = new PersonDto();
                    dto.setFirstName(readRequiredLine(sc, "First name: "));
                    dto.setLastName(readRequiredLine(sc, "Last name: "));
                    dto.setBirthDate(readDate(sc, "Birth date (YYYY-MM-DD): "));
                    PersonDto created = person.createPerson(dto);
                    System.out.println("Created person id=" + created.getId());
                }
                case "2" -> {
                    Long id = readLong(sc, "Person id: ");
                    PersonDto dto = person.getPersonById(id);
                    System.out.println("Person: id=" + dto.getId()
                            + ", name=" + dto.getFirstName() + " " + dto.getLastName()
                            + ", birthDate=" + dto.getBirthDate()
                            + ", idCardId=" + dto.getIdentificationCardId());
                }
                case "3" -> person.getAllPeople().forEach(p ->
                        System.out.println("id=" + p.getId()
                                + ", name=" + p.getFirstName() + " " + p.getLastName()
                                + ", birthDate=" + p.getBirthDate()
                                + ", idCardId=" + p.getIdentificationCardId())
                );
                case "4" -> {
                    Long id = readLong(sc, "Person id: ");
                    PersonDto dto = new PersonDto();
                    dto.setFirstName(readRequiredLine(sc, "New first name: "));
                    dto.setLastName(readRequiredLine(sc, "New last name: "));
                    dto.setBirthDate(readDate(sc, "New birth date (YYYY-MM-DD): "));
                    dto.setIdentificationCardId(readOptionalLongZeroMeansNull(sc, "Identification card id (0 = none): "));
                    PersonDto updated = person.updatePerson(id, dto);
                    System.out.println("Updated person id=" + updated.getId());
                }
                case "5" -> {
                    Long id = readLong(sc, "Person id: ");
                    person.deletePerson(id);
                    System.out.println("Deleted person id=" + id);
                }
                case "6" -> {
                    Long id = readLong(sc, "Person id: ");
                    System.out.println("Identification card id: " + person.getIdentificationCardIdForPerson(id));
                }
                case "0" -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void idCardMenu(Scanner sc, IdCardService card) {
        while (true) {
            System.out.println("\n--- IDENTIFICATION CARD MENU ---");
            System.out.println("1) Create identification card");
            System.out.println("2) Get identification card by id");
            System.out.println("3) List all identification cards");
            System.out.println("4) Update identification card");
            System.out.println("5) Delete identification card");
            System.out.println("6) Assign id card to person");
            System.out.println("7) Remove id card from person");
            System.out.println("8) Get person id for id card");
            System.out.println("0) Back");

            String ch = readLine(sc, "Choice: ").trim();

            switch (ch) {
                case "1" -> {
                    IdentificationCardDto dto = new IdentificationCardDto();
                    dto.setCardNumber(readRequiredLine(sc, "Card number: "));
                    dto.setIssueDate(readDate(sc, "Issue date (YYYY-MM-DD): "));
                    dto.setExpiryDate(readDate(sc, "Expiry date (YYYY-MM-DD): "));
                    IdentificationCardDto created = card.createIdentificationCard(dto);
                    System.out.println("Created id card id=" + created.getId());
                }
                case "2" -> {
                    Long id = readLong(sc, "Id card id: ");
                    IdentificationCardDto dto = card.getIdentificationCardById(id);
                    System.out.println("Id card: id=" + dto.getId()
                            + ", number=" + dto.getCardNumber()
                            + ", issue=" + dto.getIssueDate()
                            + ", expiry=" + dto.getExpiryDate()
                            + ", personId=" + dto.getPersonId());
                }
                case "3" -> card.getAllIdentificationCards().forEach(c ->
                        System.out.println("id=" + c.getId()
                                + ", number=" + c.getCardNumber()
                                + ", issue=" + c.getIssueDate()
                                + ", expiry=" + c.getExpiryDate()
                                + ", personId=" + c.getPersonId())
                );
                case "4" -> {
                    Long id = readLong(sc, "Id card id: ");
                    IdentificationCardDto dto = new IdentificationCardDto();
                    dto.setCardNumber(readRequiredLine(sc, "New card number: "));
                    dto.setIssueDate(readDate(sc, "New issue date (YYYY-MM-DD): "));
                    dto.setExpiryDate(readDate(sc, "New expiry date (YYYY-MM-DD): "));
                    IdentificationCardDto updated = card.updateIdentificationCard(id, dto);
                    System.out.println("Updated id card id=" + updated.getId());
                }
                case "5" -> {
                    Long id = readLong(sc, "Id card id: ");
                    card.deleteIdentificationCard(id);
                    System.out.println("Deleted id card id=" + id);
                }
                case "6" -> {
                    Long cardId = readLong(sc, "Id card id: ");
                    Long personId = readLong(sc, "Person id: ");
                    card.assignIdCardToPerson(cardId, personId);
                    System.out.println("Id card assigned to person.");
                }
                case "7" -> {
                    Long cardId = readLong(sc, "Id card id: ");
                    Long personId = readLong(sc, "Person id: ");
                    card.removeIdCardFromPerson(cardId, personId);
                    System.out.println("Id card removed from person.");
                }
                case "8" -> {
                    Long cardId = readLong(sc, "Id card id: ");
                    System.out.println("Person id: " + card.getPersonIdForIdCard(cardId));
                }
                case "0" -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void drivingLicenseMenu(Scanner sc, DrivingLicenseService license) {
        while (true) {
            System.out.println("\n--- DRIVING LICENSE MENU ---");
            System.out.println("1) Create driving license");
            System.out.println("2) Get driving license by id");
            System.out.println("3) List all driving licenses");
            System.out.println("4) Update driving license");
            System.out.println("5) Delete driving license");
            System.out.println("6) Assign driving license to employee");
            System.out.println("7) Remove driving license from employee");
            System.out.println("8) Get employee id for driving license");
            System.out.println("0) Back");

            String ch = readLine(sc, "Choice: ").trim();

            switch (ch) {
                case "1" -> {
                    DrivingLicenseDto dto = new DrivingLicenseDto();
                    dto.setDrivingLicenseNumber(readRequiredLine(sc, "License number: "));
                    dto.setIssueDate(readDate(sc, "Issue date (YYYY-MM-DD): "));
                    dto.setExpiryDate(readDate(sc, "Expiry date (YYYY-MM-DD): "));
                    dto.setDrivingLicenseCategories(readDrivingLicenseCategories(sc, "Categories (comma-separated): "));
                    DrivingLicenseDto created = license.createDrivingLicense(dto);
                    System.out.println("Created driving license id=" + created.getId());
                }
                case "2" -> {
                    Long id = readLong(sc, "Driving license id: ");
                    DrivingLicenseDto dto = license.getDrivingLicenseById(id);
                    System.out.println("Driving license: id=" + dto.getId()
                            + ", number=" + dto.getDrivingLicenseNumber()
                            + ", issue=" + dto.getIssueDate()
                            + ", expiry=" + dto.getExpiryDate()
                            + ", categories=" + dto.getDrivingLicenseCategories()
                            + ", employeeId=" + dto.getEmployeeId());
                }
                case "3" -> license.getAllDrivingLicenses().forEach(d ->
                        System.out.println("id=" + d.getId()
                                + ", number=" + d.getDrivingLicenseNumber()
                                + ", issue=" + d.getIssueDate()
                                + ", expiry=" + d.getExpiryDate()
                                + ", categories=" + d.getDrivingLicenseCategories()
                                + ", employeeId=" + d.getEmployeeId())
                );
                case "4" -> {
                    Long id = readLong(sc, "Driving license id: ");
                    DrivingLicenseDto dto = new DrivingLicenseDto();
                    dto.setDrivingLicenseNumber(readRequiredLine(sc, "New license number: "));
                    dto.setIssueDate(readDate(sc, "New issue date (YYYY-MM-DD): "));
                    dto.setExpiryDate(readDate(sc, "New expiry date (YYYY-MM-DD): "));
                    dto.setDrivingLicenseCategories(readDrivingLicenseCategories(sc, "New categories (comma-separated): "));
                    DrivingLicenseDto updated = license.updateDrivingLicense(id, dto);
                    System.out.println("Updated driving license id=" + updated.getId());
                }
                case "5" -> {
                    Long id = readLong(sc, "Driving license id: ");
                    license.deleteDrivingLicense(id);
                    System.out.println("Deleted driving license id=" + id);
                }
                case "6" -> {
                    Long licenseId = readLong(sc, "Driving license id: ");
                    Long employeeId = readLong(sc, "Employee id: ");
                    license.assignDrivingLicenseToEmployee(licenseId, employeeId);
                    System.out.println("Driving license assigned to employee.");
                }
                case "7" -> {
                    Long licenseId = readLong(sc, "Driving license id: ");
                    Long employeeId = readLong(sc, "Employee id: ");
                    license.removeDrivingLicenseFromEmployee(licenseId, employeeId);
                    System.out.println("Driving license removed from employee.");
                }
                case "8" -> {
                    Long licenseId = readLong(sc, "Driving license id: ");
                    System.out.println("Employee id: " + license.getEmployeeIdForDrivingLicense(licenseId));
                }
                case "0" -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static Transport buildTransportEntityForPricing(TransportDto dto) {
        Transport t = new Transport();
        t.setCargoType(dto.getCargoType());
        t.setInitPrice(dto.getInitPrice());
        t.setQuantity(dto.getQuantity());
        return t;
    }

    private static String readLine(Scanner sc, String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }

    private static String readRequiredLine(Scanner sc, String prompt) {
        while (true) {
            String s = readLine(sc, prompt).trim();
            if (!s.isEmpty()) return s;
            System.out.println("Input cannot be blank.");
        }
    }

    private static Long readLong(Scanner sc, String prompt) {
        while (true) {
            String s = readLine(sc, prompt).trim();
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number (Long).");
            }
        }
    }

    private static Long readOptionalLongZeroMeansNull(Scanner sc, String prompt) {
        while (true) {
            String s = readLine(sc, prompt).trim();
            try {
                long v = Long.parseLong(s);
                return v == 0L ? null : v;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number (Long).");
            }
        }
    }

    private static BigDecimal readBigDecimal(Scanner sc, String prompt) {
        while (true) {
            String s = readLine(sc, prompt).trim().replace(",", ".");
            try {
                return new BigDecimal(s);
            } catch (Exception e) {
                System.out.println("Please enter a valid number (BigDecimal).");
            }
        }
    }

    private static LocalDate readDate(Scanner sc, String prompt) {
        while (true) {
            String s = readLine(sc, prompt).trim();
            try {
                return LocalDate.parse(s);
            } catch (Exception e) {
                System.out.println("Date format must be YYYY-MM-DD.");
            }
        }
    }

    private static CargoType readCargoType(Scanner sc, String prompt) {
        while (true) {
            String s = readLine(sc, prompt).trim();
            if (s.isEmpty()) {
                System.out.println("Cargo type cannot be blank.");
                continue;
            }
            try {
                return CargoType.valueOf(s.toUpperCase());
            } catch (Exception e) {
                System.out.println("Invalid cargo type. Use GOODS, PASSENGERS, or ADR.");
            }
        }
    }

    private static VehicleType readVehicleType(Scanner sc, String prompt) {
        while (true) {
            String s = readLine(sc, prompt).trim();
            if (s.isEmpty()) {
                System.out.println("Vehicle type cannot be blank.");
                continue;
            }
            try {
                return VehicleType.valueOf(s.toUpperCase());
            } catch (Exception e) {
                System.out.println("Invalid vehicle type. Use values from VehicleType enum.");
            }
        }
    }

    private static DriverQualifications readDriverQualification(Scanner sc, String prompt) {
        while (true) {
            String s = readLine(sc, prompt).trim();
            if (s.isEmpty()) {
                System.out.println("Qualification cannot be blank.");
                continue;
            }
            try {
                return DriverQualifications.valueOf(s.toUpperCase());
            } catch (Exception e) {
                System.out.println("Invalid qualification. Use values from DriverQualifications enum.");
            }
        }
    }

    private static Set<DriverQualifications> readQualifications(Scanner sc, String prompt) {
        while (true) {
            String s = readLine(sc, prompt).trim();
            if (s.isEmpty()) {
                System.out.println("Please enter at least one qualification.");
                continue;
            }
            try {
                Set<DriverQualifications> set = Arrays.stream(s.split(","))
                        .map(String::trim)
                        .filter(x -> !x.isEmpty())
                        .map(x -> DriverQualifications.valueOf(x.toUpperCase()))
                        .collect(Collectors.toCollection(HashSet::new));
                if (set.isEmpty()) {
                    System.out.println("Please enter at least one qualification.");
                    continue;
                }
                return set;
            } catch (Exception e) {
                System.out.println("Invalid qualification. Use values from DriverQualifications enum.");
            }
        }
    }

    private static Set<DrivingLicenseCategories> readDrivingLicenseCategories(Scanner sc, String prompt) {
        while (true) {
            String s = readLine(sc, prompt).trim();
            if (s.isEmpty()) {
                System.out.println("Please enter at least one category.");
                continue;
            }
            try {
                Set<DrivingLicenseCategories> set = Arrays.stream(s.split(","))
                        .map(String::trim)
                        .filter(x -> !x.isEmpty())
                        .map(x -> DrivingLicenseCategories.valueOf(x.toUpperCase()))
                        .collect(Collectors.toCollection(HashSet::new));
                if (set.isEmpty()) {
                    System.out.println("Please enter at least one category.");
                    continue;
                }
                return set;
            } catch (Exception e) {
                System.out.println("Invalid category. Use values from DrivingLicenseCategory enum.");
            }
        }
    }
}
