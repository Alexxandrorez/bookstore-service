package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.dto.EmployeeDTO;
import com.epam.rd.autocode.spring.project.service.ClientService;
import com.epam.rd.autocode.spring.project.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final EmployeeService employeeService;
    private final ClientService clientService;

    public AdminController(EmployeeService employeeService, ClientService clientService) {
        this.employeeService = employeeService;
        this.clientService = clientService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("employeeCount", employeeService.getAllEmployees().size());
        model.addAttribute("clientCount", clientService.getAllClients().size());
        return "admin/dashboard";
    }

    @GetMapping("/clients/all")
    public String listAllClients(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 8;
        Page<ClientDTO> clientPage = clientService.getAllClients(page, pageSize);

        model.addAttribute("clients", clientPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", clientPage.getTotalPages());
        return "admin/clients-list";
    }

    @GetMapping("/employees")
    public String listEmployees(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 8;
        Page<EmployeeDTO> employeePage = employeeService.getAllEmployees(page, pageSize);

        model.addAttribute("employees", employeePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", employeePage.getTotalPages());
        return "admin/employees-list";
    }

    @GetMapping("/clients/{email:.+}")
    public String clientProfile(@PathVariable String email, Model model) {
        model.addAttribute("client", clientService.getClientByEmail(email));
        return "admin/profile";
    }

    @PostMapping("/clients/delete")
    public String deleteClient(@RequestParam String email) {
        clientService.deleteClientByEmail(email);
        return "redirect:/admin/clients/all";
    }

    @PostMapping("/employees/delete")
    public String deleteEmployee(@RequestParam String email) {
        employeeService.deleteEmployeeByEmail(email);
        return "redirect:/admin/employees";
    }

    @GetMapping("/employees/new")
    public String showAddEmployeeForm(Model model) {
        model.addAttribute("employee", new EmployeeDTO());
        return "admin/employee-form";
    }

    @PostMapping("/employees/add")
    public String addEmployee(@Valid @ModelAttribute("employee") EmployeeDTO employeeDTO,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/employee-form";
        }

        try {
            employeeService.addEmployee(employeeDTO);
            return "redirect:/admin/employees";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMessage", "Працівник з таким телефоном або Email вже існує!");
            return "admin/employee-form";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Сталася системна помилка: " + e.getMessage());
            return "admin/employee-form";
        }
    }
}