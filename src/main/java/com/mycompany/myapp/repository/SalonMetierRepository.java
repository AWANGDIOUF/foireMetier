package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SalonMetier;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SalonMetier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalonMetierRepository extends JpaRepository<SalonMetier, Long> {}
