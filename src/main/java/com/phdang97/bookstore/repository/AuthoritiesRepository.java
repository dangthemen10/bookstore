package com.phdang97.bookstore.repository;

import com.phdang97.bookstore.entity.Authorities;
import com.phdang97.bookstore.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthoritiesRepository extends JpaRepository<Authorities, Integer> {

  Authorities findAuthoritiesByRole(Role role);
}
