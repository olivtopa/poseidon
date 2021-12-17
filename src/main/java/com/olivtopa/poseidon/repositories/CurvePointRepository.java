package com.olivtopa.poseidon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.olivtopa.poseidon.domain.CurvePoint;

public interface CurvePointRepository extends JpaRepository<CurvePoint, Integer> {

}
