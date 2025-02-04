package com.example.demo.Service.Impl;


import com.example.demo.Common.*;
import com.example.demo.Models.Account;
import com.example.demo.Models.Address;
import com.example.demo.Models.Chat;
import com.example.demo.Models.Customer;
import com.example.demo.Repository.AccountRepository;
import com.example.demo.Repository.AddressRepository;
import com.example.demo.Repository.CustomerRepository;
import com.example.demo.Service.AddressService;
import com.example.demo.DTO.AddressDTO;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService{

    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;
    private final CustomerRepository customerRepository;

    public AddressServiceImpl(AddressRepository addressRepository, ModelMapper modelMapper, CustomerRepository customerRepository) {
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
        this.customerRepository = customerRepository;
    }

    // Lấy tất cả địa chỉ của một khách hàng
    @Transactional
    @Override
    public List<AddressDTO> getAllAddress(UUID customerId) {
        List<Address> addresses = addressRepository.findByCustomerId(customerId);
        return addresses.stream()
            .map(address -> modelMapper.map(address, AddressDTO.class))
            .collect(Collectors.toList());
    }

    // Lấy địa chỉ theo ID
    @Transactional
    @Override
    public AddressDTO getAddressById(UUID id) {
        Address address = addressRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Address not found"));
        return modelMapper.map(address, AddressDTO.class);
    }
    
    // Tạo mới địa chỉ
    @Transactional
    @Override
    public void createAddress(AddressDTO addressDTO) {
        Address address = modelMapper.map(addressDTO,Address.class);
        Customer  customer = customerRepository.findById(addressDTO.getCustomerId())
                           .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        address.setCustomer(customer);
        customer.getAddressList().add(address);
        addressRepository.save(address);
    }
    
    // Cập nhật thông tin địa chỉ
    @Transactional
    @Override
    public void updateAddress(UUID idToUpdate, AddressDTO updatedAddress) {
        //kiem tra  customer
        Customer customer = customerRepository.findById(updatedAddress.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        //lay address
        Address addressToUpdate = addressRepository.findById(idToUpdate)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));
        //update address
        modelMapper.map(updatedAddress, addressToUpdate);
        addressToUpdate.setId(idToUpdate);

        addressRepository.save(addressToUpdate);
    }


    // Xóa địa chỉ
    @Transactional
    @Override
    public void deleteAddress(UUID id) {
        Address addressExisting = addressRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Address not found"));
        Customer customer = addressExisting.getCustomer();

        customer.getAddressList().remove(addressExisting);

        customerRepository.save(customer);

        addressRepository.deleteById(id);

    }
}

