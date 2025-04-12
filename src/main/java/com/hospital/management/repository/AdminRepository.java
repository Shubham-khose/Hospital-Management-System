package com.hospital.management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospital.management.entities.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer>{
	Optional<Admin> findByEmailIdAndPassword(String email,String password);
}
