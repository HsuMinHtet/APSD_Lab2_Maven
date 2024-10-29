package edu.miu.cse;

import edu.miu.cse.model.Employee;
import edu.miu.cse.model.PensionPlan;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args){

        PensionPlan pension1 = new PensionPlan("EX1089", LocalDate.parse("2023-01-17"), 100.00);
        PensionPlan pension2 = new PensionPlan("SM2307", LocalDate.parse("2019-11-04"), 1555.50);

        List<Employee> employeeList= new ArrayList<>();
        // Create and add employee objects with the given data
        employeeList.add(new Employee(
                1L,  "Daniel", "Agar",
                LocalDate.parse("2018-01-17"), 105945.50,pension1));

        employeeList.add(new Employee(
                2L,  "Benard", "Shaw",
                LocalDate.parse("2019-04-03"), 197750.00,null));

        employeeList.add(new Employee(
                3L,  "Carly", "Agar",
                LocalDate.parse("2014-05-16"), 842000.75,pension2));
        employeeList.add(new Employee(
                4L,  "Wesley", "Schneider",
                LocalDate.parse("2019-10-02"), 74500.00,null));
        printJSON(employeeList);
        //printUpcomingEnrolleesReport(employeeList);

    }

    private static void printJSON(List<Employee> employees) {
        // Sort by last name in ascending order, then by yearly salary in descending order
        List<Employee> sortedEmployees = employees.stream()
                .sorted(Comparator.comparing(Employee::getLastName)
                        .thenComparing(Comparator.comparing(Employee::getYearlySalary).reversed()))
                .collect(Collectors.toList());

        System.out.println("JSON-formatted list of all Employees:");

        // Format the sorted employees in JSON format
        String jsonOutput = sortedEmployees.stream()
                .map(employee -> String.format("  {\n    \"planReferenceNumber\": \"%s\",\n    \"employeeId\": %d,\n    \"firstName\": \"%s\",\n    \"lastName\": \"%s\",\n    \"employmentDate\": \"%s\",,\n    \"enrollmentDate\": \"%s\",\n    \"yearlySalary\": %.2f,\n    \"monthlyContribution\": %.2f\n  }",
                        (employee.getPensionPlan()!=null)?employee.getPensionPlan().getPlanReferenceNumber():"\"\"",
                        employee.getEmployeeId(),
                        employee.getFirstName(),
                        employee.getLastName(),
                        employee.getEmploymentDate(),
                        (employee.getPensionPlan()!=null)?employee.getPensionPlan().getEnrollmentDate():"\"\"",
                        employee.getYearlySalary(),
                        (employee.getPensionPlan()!=null)?employee.getPensionPlan().getMonthlyContribution():0))
                .collect(Collectors.joining(",\n", "[\n", "\n]"));

        System.out.println(jsonOutput);
    }

    private static void printUpcomingEnrolleesReport(List<Employee> employees){
        LocalDate firstDayOfNextMonth = YearMonth.now().plusMonths(1).atDay(1);
        LocalDate lastDayOfNextMonth = YearMonth.now().plusMonths(1).atEndOfMonth();

        List<Employee> upcomingEnrolles = employees.stream()
                .filter(employee -> employee.getPensionPlan()==null)
                .filter(employee -> employee.getEmploymentDate().isBefore(LocalDate.now().minusYears(5).minusMonths(1))).collect(Collectors.toList());
                        //.forEach(System.out::println);
        printJSON(upcomingEnrolles);
    }
}
