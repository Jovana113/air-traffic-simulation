package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import exceptions.FileFormatException;
import exceptions.FileNotFoundException;
import logic.AirportSystem;
import models.Airport;
import models.Flight;

public class CSV implements FileHandler {
	
	private static final int AIRPORT_COLUMNS_MIN = 4;
	private static final int AIRPORT_COLUMNS_MAX = 5;

    private static final int FLIGHT_COLUMNS = 4;
    
    protected int shift;

	public CSV(int s) {
		this.shift = s;
	}

	@Override
	public void save(AirportSystem system, String path) throws Exception {
		try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(path))) {
			bw.write("# AIRPORTS");
			bw.newLine();
			bw.write("CODE,NAME,X,Y,CODE2");
			bw.newLine();
			for(Airport a : system.getAirports().values()) {
				if(a.getCode2() == null || a.getCode2().isEmpty()) {
					bw.write(a.getCode() + "," + a.getName() + "," + a.getX() + "," + a.getY());
				}
				else {
					bw.write(a.getCode() + "," + a.getName() + "," + a.getX() + "," + a.getY() + "," + a.getCode2());
				}
				bw.newLine();
			}
			
			bw.write("# FLIGHTS");
			bw.newLine();
			bw.write("FROM,TO,DEPARTURE,DURATION");
			bw.newLine();
			for(Flight f :system.getFlights()) {
				bw.write(f.getSource().getCode() + "," + f.getDestination().getCode() + "," + f.getTimeString() + "," + f.getDurationMin());
				bw.newLine();
			}
			
		}

	}

	@Override
	public void load(AirportSystem system, String path) throws Exception {
		AirportSystem temp = new AirportSystem();
		
		if(!Files.exists(Paths.get(path))) {
			throw new FileNotFoundException("Fajl ne postoji na putanji: " + path);
		}
		
		if (!path.toLowerCase().endsWith(".csv")) {
		    throw new FileFormatException("Ocekivan je .csv fajl.");
		}
		
		boolean foundAirports = false;
		
		try (BufferedReader br = Files.newBufferedReader(Paths.get(path))){
			String line;
			String section = "";   //airports or flights
			int counter = 0;
			while((line = br.readLine()) != null) {
				counter++;
				if (line.startsWith("#")) {
					section = line;
					if (line.contains("AIRPORTS")) foundAirports = true;
					continue;
				}
				if(line.startsWith("CODE") || line.startsWith("FROM")) {
					continue;
				}
				if(line.trim().isEmpty()) continue;
				
				String[] words = line.split(",");
				
				if(section.contains("AIRPORTS")) {
					if(words.length < AIRPORT_COLUMNS_MIN || words.length > AIRPORT_COLUMNS_MAX) {
						throw new FileFormatException("Fajl ne sadrzi ocekivane kolone, linija " + counter);
					}
					String code = words[0].trim();
					String name = words[1].trim();
					String name2 = encrypt(name, shift);
					int x = parseNumber(words[2], counter, "X koordinata");
					int y = parseNumber(words[3], counter, "Y koordinata");
					if(words.length == 5) {
						String code2 = words[4].trim();
						temp.addAirport(new Airport(name2, code, x, y, code2));
					}else {
						temp.addAirport(new Airport(name2, code, x, y, ""));
					}
					
				}
				else if(section.contains("FLIGHTS")) {
					if(words.length < FLIGHT_COLUMNS) {
						throw new FileFormatException("Fajl ne sadrzi ocekivane kolone, linija " + counter);
					}
					String from = words[0].trim();
					String to = words[1].trim();
					String[] time = words[2].split(":");
					if(time.length != 2) {
						throw new FileFormatException("Vreme poletanja nije u dobrom formatu, linija " + counter);
					}
					int hour = parseNumber(time[0], counter, "sat poletanja");
                    int min = parseNumber(time[1], counter, "minut poletanja");
                    int duration = parseNumber(words[3], counter, "trajanje leta");
					Airport source = temp.findAirport(from); 
					Airport destination = temp.findAirport(to);
					if (source == null) {
                        throw new FileFormatException("Red " + counter + ": polazni aerodrom '"
                                + from + "' nije definisan u sekciji AIRPORTS.");
                    }
                    if (destination == null) {
                        throw new FileFormatException("Red " + counter + ": dolazni aerodrom '"
                                + to + "' nije definisan u sekciji AIRPORTS.");
                    }
					temp.addFlight(new Flight(source, destination, duration, hour, min));
				}
				else {
				    throw new FileFormatException("Red " + counter
				        + ": podatak van sekcije. Fajl mora poceti sa '# AIRPORTS'.");
				}
			}
		}catch (IOException e) {
	        throw new FileFormatException("Fajl se ne moze procitati: " + path);
	    }
		if (!foundAirports) {
		    throw new FileFormatException("Fajl nema sekciju '# AIRPORTS'.");
		}
		system.replaceWith(temp);
		
	
	}

}
