package com.example.demo.Service;

import java.util.List;
import java.util.UUID;

import com.example.demo.DTO.AddressDTO;

public interface AddressService {
    public List<AddressDTO> getAllAddress(UUID customerId);
    public AddressDTO getAddressById(UUID id);
    public AddressDTO createAddress(AddressDTO addressDTO);
    public AddressDTO updateAddress(UUID idToUpdate, AddressDTO updatedAddressDTO);
    public void deleteAddress(UUID id);
}
