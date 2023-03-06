package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Objectif;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Objectif entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ObjectifRepository extends JpaRepository<Objectif, Long> {}
