package it.polito.tdp.extflightdelays.model;

import java.util.Objects;

public class Rotta {

	private Airport origin;
	private Airport destination;
	private Integer n;
	
	public Rotta(Airport origin, Airport destination, Integer n) {
		this.origin = origin;
		this.destination = destination;
		this.n = n;
	}

	public Airport getOrigin() {
		return origin;
	}

	public void setOrigin(Airport origin) {
		this.origin = origin;
	}

	public Airport getDestination() {
		return destination;
	}

	public void setDestination(Airport destination) {
		this.destination = destination;
	}

	public Integer getN() {
		return n;
	}

	public void setN(Integer n) {
		this.n = n;
	}

	@Override
	public int hashCode() {
		return Objects.hash(destination, n, origin);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rotta other = (Rotta) obj;
		return Objects.equals(destination, other.destination) && Objects.equals(n, other.n)
				&& Objects.equals(origin, other.origin);
	}
	
	
	
	
}
