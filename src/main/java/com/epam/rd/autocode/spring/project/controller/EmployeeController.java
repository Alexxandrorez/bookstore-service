package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.EmployeeDTO;
import com.epam.rd.autocode.spring.project.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/all")
    public String getAllEmployees(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "employees-list";
    }

    @GetMapping("/{email}")
    public String getEmployeeByEmail(@PathVariable String email, Model model) {
        model.addAttribute("employee", employeeService.getEmployeeByEmail(email));
        return "employee-profile";
    }

    @PostMapping("/add")
    public String addEmployee(EmployeeDTO employeeDTO) {
        employeeService.addEmployee(employeeDTO);
        return "redirect:/employees/all";
    }

    @PostMapping("/update")
    public String updateEmployee(@RequestParam String email, EmployeeDTO employeeDTO) {
        employeeService.updateEmployeeByEmail(email, employeeDTO);
        return "redirect:/employees/" + email;
    }

    @PostMapping("/delete")
    public String deleteEmployee(@RequestParam String email) {
        employeeService.deleteEmployeeByEmail(email);
        return "redirect:/employees/all";
    }
}