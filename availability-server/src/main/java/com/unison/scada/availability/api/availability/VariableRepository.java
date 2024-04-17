package com.unison.scada.availability.api.availability;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VariableRepository extends JpaRepository<Variable, String> {
}
