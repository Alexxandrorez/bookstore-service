package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/client")
    public String getOrdersByClient(@RequestParam String email, Model model) {
        model.addAttribute("orders", orderService.getOrdersByClient(email));
        model.addAttribute("userEmail", email);
        return "client-orders";
    }

    @GetMapping("/employee")
    public String getOrdersByEmployee(@RequestParam String email, Model model) {
        model.addAttribute("orders", orderService.getOrdersByEmployee(email));
        model.addAttribute("employeeEmail", email);
        return "employee-orders";
    }

    @PostMapping("/add")
    public String addOrder(@ModelAttribute OrderDTO orderDTO) {
        orderService.addOrder(orderDTO);
        return "redirect:/orders/client?email=" + orderDTO.getClientEmail();
    }
}