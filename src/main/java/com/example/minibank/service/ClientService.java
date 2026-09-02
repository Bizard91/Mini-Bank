package com.example.minibank.service;

import com.example.minibank.dto.CreateClientRequest;
import com.example.minibank.entity.Client;
import com.example.minibank.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.minibank.exception.ClientAlreadyExistsException;
import com.example.minibank.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public Client createClient(CreateClientRequest request) {
        if (clientRepository.existsByEmail(request.getEmail())) {
            throw new ClientAlreadyExistsException("Client with this email already exists");
        }

        Client client = Client.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .build();

        return clientRepository.save(client);
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() ->  new ResourceNotFoundException("Client not found"));
    }
}