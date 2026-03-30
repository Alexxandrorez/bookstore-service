package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.BookItemDTO;
import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.model.*;
import com.epam.rd.autocode.spring.project.repo.BookRepository;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.repo.EmployeeRepository;
import com.epam.rd.autocode.spring.project.repo.OrderRepository;
import com.epam.rd.autocode.spring.project.service.OrderService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepository orderRepository, BookRepository bookRepository,
                            ClientRepository clientRepository, EmployeeRepository employeeRepository,
                            ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<OrderDTO> getOrdersByClient(String clientEmail) {
        if(clientRepository.findByEmail(clientEmail).isEmpty()){
            throw new NotFoundException("Client not found");
        }
        return orderRepository.findAllByClientEmail(clientEmail).stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .toList();
    }

    @Override
    public List<OrderDTO> getOrdersByEmployee(String employeeEmail) {
        if(employeeRepository.findByEmail(employeeEmail).isEmpty()){
            throw new NotFoundException("Employee not found");
        }
        return orderRepository.findAllByEmployeeEmail(employeeEmail).stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public OrderDTO addOrder(OrderDTO orderDTO) {
        Client client = clientRepository.findByEmail(orderDTO.getClientEmail())
                .orElseThrow(() -> new NotFoundException("Client not found"));
        Employee employee = employeeRepository.findByEmail(orderDTO.getEmployeeEmail())
                .orElseThrow(() -> new NotFoundException("Employee not found"));

        Order orderEntity = new Order();
        orderEntity.setClient(client);
        orderEntity.setEmployee(employee);
        orderEntity.setOrderDate(LocalDateTime.now());

        BigDecimal totalPrice = BigDecimal.ZERO;
        List<BookItem> items = new ArrayList<>();

        for (BookItemDTO itemDTO : orderDTO.getBookItems()) {
            Book book = bookRepository.findByName(itemDTO.getBookName())
                    .orElseThrow(() -> new NotFoundException("Book not found: " + itemDTO.getBookName()));

            BookItem bookItem = new BookItem();
            bookItem.setBook(book);
            bookItem.setQuantity(itemDTO.getQuantity());
            bookItem.setOrder(orderEntity);
            items.add(bookItem);

            BigDecimal itemSum = book.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
            totalPrice = totalPrice.add(itemSum);
        }

        orderEntity.setBookItems(items);
        orderEntity.setPrice(totalPrice);

        Order savedOrder = orderRepository.save(orderEntity);

        return modelMapper.map(savedOrder, OrderDTO.class);
    }
}

