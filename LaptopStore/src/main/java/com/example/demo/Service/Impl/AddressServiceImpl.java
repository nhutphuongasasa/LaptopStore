package com.example.demo.Service.Impl;


import com.example.demo.Models.Address;
import com.example.demo.Models.Customer;
import com.example.demo.Repository.AddressRepository;
import com.example.demo.Repository.CustomerRepository;
import com.example.demo.Service.AddressService;
import com.example.demo.DTO.AddressDTO;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public AddressDTO createAddress(AddressDTO addressDTO) {
        Customer  customer = customerRepository.findById(addressDTO.getCustomerId())
                           .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        Address address = Address.builder()
                .id(null)
                .customer(customer)
                .city(addressDTO.getCity())
                .district(addressDTO.getDistrict())
                .ward(addressDTO.getWard())
                .street(addressDTO.getStreet())
                .phone(addressDTO.getPhone())
                .build();

        customer.getAddressList().add(address);

        Address addressExisting = addressRepository.save(address);

        return convertToDTO(addressExisting);
    }
    
    // Cập nhật thông tin địa chỉ
    @Transactional
    @Override
    public AddressDTO updateAddress(UUID idToUpdate, AddressDTO updatedAddress) {
        Customer customer = customerRepository.findById(updatedAddress.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        Address addressToUpdate = addressRepository.findById(idToUpdate)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));

        modelMapper.map(updatedAddress, addressToUpdate);
        addressToUpdate.setId(idToUpdate);

        Address addressExisting = addressRepository.save(addressToUpdate);

        return convertToDTO(addressExisting);
    }


    // Xóa địa chỉ
    @Transactional
    @Override
    public void deleteAddress(UUID id) {
        Address addressExisting = addressRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Address not found"));
        Customer customerExisting = addressExisting.getCustomer();

        customerExisting.getAddressList().remove(addressExisting);

        addressRepository.deleteById(id);

    }

    private AddressDTO convertToDTO(Address address) {
        return AddressDTO.builder()
                .id(address.getId())
                .customerId(address.getCustomer().getId())
                .city(address.getCity())
                .district(address.getDistrict())
                .ward(address.getWard())
                .street(address.getStreet())
                .phone(address.getPhone())
                .build();
    }
}

