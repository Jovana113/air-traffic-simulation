package logic;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import exceptions.InvalidAirportException;
import models.Airport;
import models.Flight;


//cuva sve aerodrome i letove, linkedhashmap pamti redosled unosa
public class AirportSystem {
	private ArrayList<Flight> flights = new ArrayList<Flight>();
	private LinkedHashMap<String, Airport> airports = new LinkedHashMap<String, Airport>();

	public AirportSystem() {}
	
	public ArrayList<Flight> getFlights() {
		return flights;
	}

	public LinkedHashMap<String, Airport> getAirports() {
		return airports;
	}
	
	public void addAirport(Airport a) throws InvalidAirportException {
		if(airports.containsKey(a.getCode())) {
			throw new InvalidAirportException("ovaj aerodrom vec postoji");
		}
		airports.put(a.getCode(), a);

	}
	
	public void addFlight(Flight f) {
		flights.add(f);
	}

	public Airport findAirport(String code) {
		if(code.length() == 3) {
			return airports.get(code);
		}
		else {
			for(Airport a :  airports.values()) {
				if(code.equals(a.getCode2())) {
					return a;
				}
			}
		}
		return null;
	}
	
	public void replaceWith(AirportSystem drugi) {
		this.airports.clear();
	    this.flights.clear();
	    this.airports.putAll(drugi.airports);
	    this.flights.addAll(drugi.flights);
	}
	

}
