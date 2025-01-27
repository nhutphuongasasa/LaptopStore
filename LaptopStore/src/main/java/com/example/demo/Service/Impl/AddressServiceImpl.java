package com.example.demo.Service.Impl;


import com.example.demo.Models.Account;
import com.example.demo.Models.Address;
import com.example.demo.Models.Chat;
import com.example.demo.Models.Customer;
import com.example.demo.Repository.AccountRepository;
import com.example.demo.Repository.AddressRepository;
import com.example.demo.Repository.CustomerRepository;
import com.example.demo.Service.AddressService;
import com.example.demo.Common.Enums;
import com.example.demo.DTO.AddressDTO;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
@Transactional
@Service
public class AddressServiceImpl implements AddressService{

    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    public AddressServiceImpl(AddressRepository addressRepository, ModelMapper modelMapper, CustomerRepository customerRepository,AccountRepository accountRepository) {
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }

    // Lấy tất cả địa chỉ của một khách hàng
    @Override
    public List<AddressDTO> getAllAddress(UUID customerId) {
        List<Address> addresses = addressRepository.findByCustomerId(customerId);
        return addresses.stream()
            .map(address -> modelMapper.map(address, AddressDTO.class))
            .collect(Collectors.toList());
    }

    // Lấy địa chỉ theo ID
    @Override
    public AddressDTO getAddressById(UUID id) {
        Address address = addressRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Address not found"));
        return modelMapper.map(address, AddressDTO.class);
    }
    
    // Tạo mới địa chỉ
    @Override
    public void createAddress(AddressDTO addressDTO) {
        //tim kiem account theo id 
        Account account = accountRepository.findById(addressDTO.getCustomer().getCustomerId())
            .orElseThrow(() -> new RuntimeException("Account not found"));
        //admin khong them dia chi
        if(account.getRole().equals(Enums.role.ADMIN)){
            throw new RuntimeException("This account is not Admin");
        }
        //lay customer voi khoa chinh la account
        Optional<Customer>  customer = customerRepository.findById(account);
        Address address = modelMapper.map(addressDTO, Address.class);
        
        Customer customerTemp = customer.get();
        //lay danh sach address
        List<Address> list = customerTemp.getAddressList();
        list.add(address);
        //them address vao list cua customer
        customerTemp.setAddressList(list);

        addressRepository.save(address);
    }
    
    // Cập nhật thông tin địa chỉ
    @Override
    public void updateAddress(UUID idToUpdate, AddressDTO updatedAddress) {

        // Tìm kiếm account theo id
        Account account = accountRepository.findById(updatedAddress.getCustomer().getCustomerId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Lấy customer với khóa chính là account
        Optional<Customer> customer = customerRepository.findById(account);
        Customer customerTemp = customer.orElseThrow(() -> new RuntimeException("Customer not found"));

        // Tìm địa chỉ cần update trong danh sách customer
        Address addressToUpdate = customerTemp.getAddressList().stream()
                .filter(address -> address.getId().equals(idToUpdate))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Address not found"));

        // Sử dụng builder để tạo một Address mới dựa trên updatedAddress
        Address updatedEntity = Address.builder()
                .id(addressToUpdate.getId())  // Giữ nguyên ID
                .customer(addressToUpdate.getCustomer()) // Giữ nguyên Customer
                .city(updatedAddress.getCity())
                .district(updatedAddress.getDistrict())
                .ward(updatedAddress.getWard())
                .street(updatedAddress.getStreet())
                .phone(updatedAddress.getPhone())
                .build();

        // Thay thế Address cũ bằng Address đã được cập nhật trong danh sách
        int indexOfOldAddress = customerTemp.getAddressList().indexOf(addressToUpdate);
        customerTemp.getAddressList().set(indexOfOldAddress, updatedEntity);

        // Lưu các thay đổi vào cơ sở dữ liệu
        customerRepository.save(customerTemp);
    }


    // Xóa địa chỉ
    @Override
    public void deleteAddress(UUID id) {
        Address addressExisting = addressRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Address not found"));

        addressExisting.setCustomer(null);
        addressRepository.save(addressExisting);
        addressRepository.deleteById(id);
    }
}

