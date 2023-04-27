package com.nis.repository;

import com.nis.entity.UserDetails;
import com.nis.model.Role;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDetails,Long> {

    Boolean existsByEmail(String email);
    Optional<UserDetails> findByEmail(String email);

    List<UserDetails> findAll(Specification<UserDetails> userAppointmentSpecification);

}
