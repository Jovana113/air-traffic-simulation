package models;

import java.util.Objects;

import exceptions.InvalidCodeException;
import exceptions.InvalidCoordinatesException;

public class Airport {
	private String name, code, code2;
	private int x, y;

	public Airport(String name, String code, int x, int y, String code2) throws InvalidCoordinatesException, InvalidCodeException {
		checkCode(code);
		checkCoordinates(x, y);
		checkCode2(code2);
		this.name = name;
		this.code = code;
		this.x = x;
		this.y = y;
		this.code2 = code2;
	}

	private void checkCoordinates(int x, int y) throws InvalidCoordinatesException {
		if(x < -180 || x > 180) {
			throw new InvalidCoordinatesException("x kordinata mora biti izmedju -180 i 180");
		}
		if( y < -90 || y > 90) {
			throw new InvalidCoordinatesException("y kordinata mora biti izmedju -90 i 90");
		}
		
	}

	private void checkCode(String code) throws InvalidCodeException {
		if (code == null || code.trim().isEmpty()) {
	        throw new InvalidCodeException("Kod aerodroma ne sme biti prazan.");
	    }
	    if (!code.matches("[A-Z]{3}")) {
	        throw new InvalidCodeException(
	            "Kod mora imati tacno tri velika slova (A-Z), a unet je '" + code + "'.");
	    }
	}
	
	private void checkCode2(String code2) throws InvalidCodeException{
		if(code2 != null && !code2.isEmpty()) {
			if(!code2.matches("[A-Z]{4}")) {
				throw new InvalidCodeException("Kod mora imati tacno cetiri velika slova (A-Z), a unet je '" + code2 + "'.");
			}
		}
	}
	

	public String getName() {
		return name;
	}
	public String getCode() {
		return code;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public String getCode2() {
		return code2;
	}

	
	
	@Override
	public String toString() {
		return  code + ", " + name + ", " + x + ", " + y;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(code);
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null) return false;
		if(!(o instanceof Airport)) return false;
		Airport temp = (Airport) o;
		return this.code.equals(temp.getCode());

	}
	
}
