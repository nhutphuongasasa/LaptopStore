package com.example.demo.Repository;

import com.example.demo.Models.Account;
import com.example.demo.Models.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Account> {
    
}
