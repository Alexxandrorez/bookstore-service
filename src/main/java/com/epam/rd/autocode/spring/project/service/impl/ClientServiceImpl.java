package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.exception.AlreadyExistException;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.service.ClientService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;

    public ClientServiceImpl(ClientRepository clientRepository, ModelMapper modelMapper) {
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll().stream()
                .map(client -> modelMapper.map(client,ClientDTO.class))
                .toList();
    }

    @Override
    public ClientDTO getClientByEmail(String email) {

        Client clientEnt= clientRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("Not found"));
        return modelMapper.map(clientEnt, ClientDTO.class);
    }

    @Override
    public ClientDTO updateClientByEmail(String email, ClientDTO client) {
        Client clientEnt= clientRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("Not found"));
        modelMapper.map(client,clientEnt);

        Client clientSaved= clientRepository.save(clientEnt);
        return modelMapper.map(clientSaved,ClientDTO.class);
    }

    @Override
    public void deleteClientByEmail(String email) {
        Client clientEnt= clientRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("Not found"));

        clientRepository.delete(clientEnt);
    }

    @Override
    public ClientDTO addClient(ClientDTO client) {
        if (clientRepository.findByEmail(client.getEmail()).isPresent()) {
            throw new AlreadyExistException("Client with this email already exists");
        }
        Client clientEnt= modelMapper.map(client, Client.class);
        clientRepository.save(clientEnt);
        return modelMapper.map(clientEnt, ClientDTO.class);
    }
}
