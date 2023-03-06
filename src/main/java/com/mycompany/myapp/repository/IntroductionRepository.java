package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Introduction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Introduction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IntroductionRepository extends JpaRepository<Introduction, Long> {}
