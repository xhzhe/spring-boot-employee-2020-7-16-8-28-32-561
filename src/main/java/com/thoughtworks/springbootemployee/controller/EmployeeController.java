package com.thoughtworks.springbootemployee.controller;

import com.thoughtworks.springbootemployee.entity.ResultBean;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import com.thoughtworks.springbootemployee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * @author XUAL7
 */
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private static final String ID_COULD_NOT_BE_SET = "ID could not be set";
    private static final String SUCCESS = "success";
    public static final String EMPLOYEE_NOT_FOUND = "employee not found";
    public static final String CREATION_FAILED = "Creation failed";
    public final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResultBean<List<Employee>> getEmployees(@PathParam("page") Integer page, @PathParam("pageSize") Integer pageSize, @PathParam("gender") String gender) {
        List<Employee> result = gender == null ? null : employeeService.getEmployees(gender);
        return ResultBean.success((page == null || pageSize == null) ? result : employeeService.getEmployees(page, pageSize, result));
    }

    @GetMapping("/{employeeID}")
    @ResponseStatus(HttpStatus.OK)
    public ResultBean<Employee> getEmployee(@PathVariable Integer employeeID) {
        return ResultBean.success(findEmployee(employeeID));
    }

    public Employee findEmployee(Integer ID) {
        return EmployeeRepository.employees.stream()
                .filter(employee -> employee.getId().equals(ID))
                .findFirst()
                .orElse(EmployeeRepository.emptyEmployee);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResultBean<Employee> addEmployee(@RequestBody Employee employee) {
        if (employee.getId() != null) {
            return ResultBean.error(ResultBean.ERROR_CODE, ID_COULD_NOT_BE_SET);
        }
        boolean success = EmployeeRepository.addEmployee(employee);
        return success ? ResultBean.success(employee) : ResultBean.error(0, CREATION_FAILED);
    }

    @PutMapping("/{employeeID}")
    @ResponseStatus(HttpStatus.OK)
    public ResultBean<Employee> updateEmployee(@PathVariable Integer employeeID, @RequestBody Employee employee) {
        Employee employeeInDatabase = findEmployee(employeeID);
        if (employeeInDatabase == EmployeeRepository.emptyEmployee) {
            return ResultBean.error(ResultBean.ERROR_CODE, EMPLOYEE_NOT_FOUND);
        }
        Integer id = employeeInDatabase.getId();
        employeeInDatabase = employee;
        employeeInDatabase.setId(id);
        return ResultBean.success(employeeInDatabase);
    }

    @DeleteMapping("/{employeeID}")
    @ResponseStatus(HttpStatus.OK)
    public ResultBean<Boolean> deleteEmployee(@PathVariable Integer employeeID) {
        Employee employee = findEmployee(employeeID);
        if (employee == EmployeeRepository.emptyEmployee) {
            return ResultBean.error(ResultBean.ERROR_CODE, EMPLOYEE_NOT_FOUND);
        }
        EmployeeRepository.employees.remove(employee);
        return ResultBean.success();
    }

}
