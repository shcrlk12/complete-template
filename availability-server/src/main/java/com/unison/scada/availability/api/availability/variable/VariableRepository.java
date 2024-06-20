package com.unison.scada.availability.api.availability.variable;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VariableRepository extends JpaRepository<Variable, UUID> {

    List<Variable> findByIsActiveTrue();
}
