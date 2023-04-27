package com.nis.repository;

import com.nis.entity.Center;
import com.nis.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessingCenterRepository extends JpaRepository<Country,Long> {

}
