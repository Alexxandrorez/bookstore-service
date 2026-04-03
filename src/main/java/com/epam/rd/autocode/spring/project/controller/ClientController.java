package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.service.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/all")
    public String listAllClients(Model model) {
        model.addAttribute("clients", clientService.getAllClients());
        return "admin/clients-list";
    }

    @GetMapping("/{email:.+}")
    public String clientProfile(@PathVariable String email, Model model) {
        model.addAttribute("client", clientService.getClientByEmail(email));
        return "admin/profile";
    }

    @PostMapping("/delete")
    public String deleteClient(@RequestParam String email) {
        clientService.deleteClientByEmail(email);
        return "redirect:/clients/all";
    }
}