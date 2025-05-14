package com.istanbul.eminonurehber.Repository;

import com.istanbul.eminonurehber.Entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByCategoryName(String categoryName);
    Optional<Company> findByEmail(String email);

    List<Company> findTop5ByOrderByClickCountDesc();
}
