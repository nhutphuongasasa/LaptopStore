package com.example.demo.Service;

import java.util.List;
import java.util.UUID;

import com.example.demo.DTO.AddressDTO;

public interface AddressService {
    public List<AddressDTO> getAllAddress(UUID customerId);
    public AddressDTO getAddressById(UUID id);
    public void createAddress(AddressDTO addressDTO);
    public void updateAddress(UUID idToUpdate, AddressDTO updatedAddressDTO);
    public void deleteAddress(UUID id);
}
