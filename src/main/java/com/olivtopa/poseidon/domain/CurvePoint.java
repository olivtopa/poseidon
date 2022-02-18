package com.olivtopa.poseidon.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
@Table(name = "curvepoint")
public class CurvePoint {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Digits(integer = 10, fraction = 2)
	@Positive(message = "this value must not be negative")
	@NotNull(message = "this must be numeric and not null")
	private Integer curveId;
	private LocalDateTime asOfDate;
	@Digits(integer = 10, fraction = 2)
	@Positive(message = "this value must not be negative")
	@NotNull(message = "this must be numeric and not null")
	private Double term;
	@Digits(integer = 10, fraction = 2)
	@Positive(message = "this value must not be negative")
	@NotNull(message = "this must be numeric and not null")
	private Double value;
	private LocalDateTime creationDate;

	public CurvePoint() {
		super();
	}

	public CurvePoint(Integer curveId, Double term, Double value) {
		super();
		this.curveId = curveId;
		this.term = term;
		this.value = value;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCurveId() {
		return curveId;
	}

	public void setCurveId(Integer curveId) {
		this.curveId = curveId;
	}

	public LocalDateTime getAsOfDate() {
		return asOfDate;
	}

	public void setAsOfDate(LocalDateTime asOfDate) {
		this.asOfDate = asOfDate;
	}

	public Double getTerm() {
		return term;
	}

	public void setTerm(Double term) {
		this.term = term;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public String toString() {
		return "CurvePoint [id=" + id + ", curveId=" + curveId + ", asOfDate=" + asOfDate + ", term=" + term
				+ ", value=" + value + ", creationDate=" + creationDate + "]";
	}

}
