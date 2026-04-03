package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.exception.InsufficientFundsException;
import com.epam.rd.autocode.spring.project.model.UserPrincipal;
import com.epam.rd.autocode.spring.project.service.OrderService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final MessageSource messageSource;

    public OrderController(OrderService orderService, MessageSource messageSource) {
        this.orderService = orderService;
        this.messageSource = messageSource;
    }

    @GetMapping("/client")
    public String getOrdersByClient(Principal principal, Model model) {
        UserPrincipal userPrincipal = (UserPrincipal) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        String email = userPrincipal.getEmail();
        model.addAttribute("orders", orderService.getOrdersByClient(email));
        model.addAttribute("userEmail", email);
        return "client-orders";
    }

    @GetMapping("/employee")
    public String getOrdersByEmployee(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        UserPrincipal userPrincipal = (UserPrincipal) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        String email = userPrincipal.getEmail();
        model.addAttribute("orders", orderService.getOrdersByEmployee(email));
        model.addAttribute("employeeEmail", email);
        return "employee-orders";
    }

    @PostMapping("/add")
    public String addOrder(@RequestParam String bookName,
                           Principal principal,
                           RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }

        try {
            UserPrincipal userPrincipal = (UserPrincipal) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
            String email = userPrincipal.getEmail();

            orderService.addOrder(email, bookName);
            redirectAttributes.addFlashAttribute("message",
                    messageSource.getMessage("order.success", new Object[]{bookName}, LocaleContextHolder.getLocale()));
            return "redirect:/books/all";
        } catch (InsufficientFundsException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/books/details/" + bookName;
        }
    }
}