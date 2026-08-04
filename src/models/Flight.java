package models;


import exceptions.InvalidAirportException;
import exceptions.InvalidTimeException;

public class Flight {
	private Airport source, destination;
	private int durationMin, hour, minutes;
	
	
	public Flight(Airport source, Airport destination, int durationMin, int hour, int minutes) throws InvalidTimeException, InvalidAirportException {
		checkHour(hour);
		checkMin(minutes);
		checkDuration(durationMin);
		checkAirports(source, destination);
		this.source = source;
		this.destination = destination;
		this.durationMin = durationMin;
		this.hour = hour;
		this.minutes = minutes;
	}
	
	private void checkDuration(int durationMin) throws InvalidTimeException {
		if(durationMin <= 0) {
			throw new InvalidTimeException("trajanje leta ne moze biti negativno");
		}
		
	}

	private void checkAirports(Airport source, Airport destination) throws InvalidAirportException {
		if(source == null || destination == null) {
			throw new InvalidAirportException("mora postojati i polazni i dolazni aerodrom");
		}
		if(source.equals(destination)) {
			throw new InvalidAirportException("ne mogu polazni i dolazni aerodrom biti isti");
		}
	}

	private void checkMin(int minutes) throws InvalidTimeException {
		if(minutes < 0 || minutes > 59) {
			throw new InvalidTimeException("minuti nisu u dobrom formatu");
		}
		
	}

	private void checkHour(int hour) throws InvalidTimeException {
		if(hour < 0 || hour > 23) {
			throw new InvalidTimeException("sati nisu u dobrom formatu");
		}
		
	}

	public Airport getSource() {
		return source;
	}
	public Airport getDestination() {
		return destination;
	}
	public int getDurationMin() {
		return durationMin;
	}
	public int getHour() {
		return hour;
	}
	public int getMinutes() {
		return minutes;
	}
	
	public String getTimeString() {
        return String.format("%02d:%02d", hour, minutes);
    }

	@Override
	public String toString() {
		return   source.getCode() + ", " + destination.getCode() + ", " + getTimeString() + ", " + durationMin;
	}


	
}
