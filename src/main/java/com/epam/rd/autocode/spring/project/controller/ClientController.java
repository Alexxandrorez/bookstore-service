package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.service.ClientService;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/clients")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/all")
    public String getAllClients(Model model){
        model.addAttribute("clients",clientService.getAllClients());
        return "clients-list";
    }

    @GetMapping("/{email}")
    public String getClientByEmail(@PathVariable String email, Model model){
        model.addAttribute("client",clientService.getClientByEmail(email));
        return "profile";
    }

    @PostMapping("/update")
    public String updateClientByEmail(@RequestParam String email, ClientDTO client){
        clientService.updateClientByEmail(email,client);
        return "redirect:/clients/"+email;
    }

    @PostMapping("/delete")
    public String deleteClientByEmail(@RequestParam String email){
        clientService.deleteClientByEmail(email);
        return "redirect:/clients/all";
    }

    @PostMapping("/add")
    public String addClient(ClientDTO client){
        clientService.addClient(client);
        return "redirect:/clients/all";
    }
}
