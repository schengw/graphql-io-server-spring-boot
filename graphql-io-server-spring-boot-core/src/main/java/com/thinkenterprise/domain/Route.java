package com.thinkenterprise.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Route  {

	@Id
	@GeneratedValue
	private Long id;
	private String flightNumber;
	private String departure;
	private String destination;
	private Boolean disabled;

	
	public Route() {
		super();
	}

	public Route(String flightNumber) {
		super();
		this.flightNumber=flightNumber;
	}
		
	public Route(Long id, String flightNumber, String departure, String destination, Boolean disabled) {
		super();
		this.id=id;
		this.flightNumber=flightNumber;
		this.destination=destination;
		this.departure=departure;
		this.disabled=disabled;
	}

	public void setId(Long id) {
		this.id=id;
	}

	public Long getId() {
		return this.id;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled=disabled;
	}

	public Boolean getDisabled() {
		return this.disabled;
	}

	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String number) {
		this.flightNumber = number;
	}	
}
