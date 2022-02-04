package com.olivtopa.poseidon.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olivtopa.poseidon.domain.CurvePoint;
import com.olivtopa.poseidon.exceptions.DataNotFoundException;
import com.olivtopa.poseidon.repositories.CurvePointRepository;

@Service
public class CurveService {

	@Autowired
	private CurvePointRepository curvePointRepository;

	public Iterable<CurvePoint> getAllCurve() {
		return curvePointRepository.findAll();
	}
	
	public CurvePoint getCurveById(Integer id) {
		return curvePointRepository.findById(id).orElseThrow(()->new DataNotFoundException(String.format("CurvePoint %d not found",id)));
	}

	public void save(CurvePoint curvePoint) {
		curvePointRepository.save(curvePoint);
	}

	public void deleteBid(CurvePoint curvePoint) {
		curvePointRepository.delete(curvePoint);
	}

}
