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

public class JSON implements FileHandler {
	private static final int AIRPORT_COLUMNS_MIN = 4;
	private static final int AIRPORT_COLUMNS_MAX = 5;
	private static final int FLIGHT_FIELDS = 4;

	protected int shift;
	
	public JSON(int s) {
		this.shift = s;
	}

	@Override
	public void save(AirportSystem system, String path) throws Exception {
		
		try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(path))) {
			bw.write("{");
			bw.newLine();
			bw.write("\"airports\":[");
			bw.newLine();
			int i = 0;
			int br = system.getAirports().size();
			for(Airport a : system.getAirports().values()) {
				if(a.getCode2() == null || a.getCode2().isEmpty()) {
					bw.write("{\"code\":\"" + a.getCode() + "\",\"name\":\"" + a.getName()
					+ "\",\"x\":" + a.getX() + ",\"y\":" + a.getY() + "}");
				}
				else {
					bw.write("{\"code\":\"" + a.getCode() + "\",\"name\":\"" + a.getName()
					+ "\",\"x\":" + a.getX() + ",\"y\":" + a.getY() + ",\"code2\":\""+ a.getCode2() + "\"" +   "}");
				}
				
				if(i<br-1) bw.write(",");
				bw.newLine();
				i++;
			}
			bw.write("],");
			bw.newLine();
			bw.write("\"flights\":[");
			bw.newLine();
			int j = 0;
			int br2 = system.getFlights().size();
			for(Flight f : system.getFlights()) {
				bw.write("{\"from\":\"" + f.getSource().getCode() + "\",\"to\":\"" + f.getDestination().getCode()
	                    + "\",\"departure\":\"" + f.getTimeString()
	                    + "\",\"duration\":" + f.getDurationMin() + "}");
				if(j<br2-1) bw.write(",");
				bw.newLine();
				j++;
			}
			bw.write("]");
			bw.newLine();
			bw.write("}");
			
		}

	}

	
	@Override
	public void load(AirportSystem system, String path) throws Exception {
		
		AirportSystem temp = new AirportSystem();

		if(!Files.exists(Paths.get(path))) {
			throw new FileNotFoundException("Fajl ne postoji na putanji: " + path);
		}
		
		if (!path.toLowerCase().endsWith(".json")) {          
		    throw new FileFormatException("Ocekivan je .json fajl.");
		}

		boolean foundAirports = false;

		try (BufferedReader br = Files.newBufferedReader(Paths.get(path))){
			String line;
			int counter = 0;
			while((line = br.readLine()) != null) {
				counter++;
				line = line.trim();  //skidamo razmake sa krajeva

				if(line.contains("\"airports\"")) foundAirports = true;

				if(line.contains("\"code\"")) {
					line = line.replace("{", "").replace("\"", "").replace("},", "").replace("}", "").trim();
					String[] words = line.split(",");
					if(words.length < AIRPORT_COLUMNS_MIN || words.length > AIRPORT_COLUMNS_MAX) {
						throw new FileFormatException("Aerodrom mora imati polja code, name, x, y, linija " + counter);
					}
					String code = words[0].split(":")[1].trim();
					String name = words[1].split(":")[1].trim();
					String name2 = encrypt(name, shift);
					int x = parseNumber(words[2].split(":")[1], counter, "X koordinata");
					int y = parseNumber(words[3].split(":")[1], counter, "Y koordinata");
					if(words.length == 5) {
						String code2 = words[4].split(":")[1].trim();
						temp.addAirport(new Airport(name2, code, x, y, code2));
					}
					else {
						temp.addAirport(new Airport(name2, code, x, y, ""));
					}
				}
				else if(line.contains("\"from\"")) {
					line = line.replace("{", "").replace("\"", "").replace("},", "").replace("}", "").trim();
					String[] words = line.split(",");
					if(words.length != FLIGHT_FIELDS) {
						throw new FileFormatException("Let mora imati polja from, to, departure, duration, linija " + counter);
					}
					String from = words[0].split(":")[1].trim();
					String to = words[1].split(":")[1].trim();
					String[] time = words[2].split(":");
					if(time.length != 3) {
						throw new FileFormatException("Vreme poletanja nije u dobrom formatu, linija " + counter);
					}
					int hour = parseNumber(time[1], counter, "sat poletanja");
					int min = parseNumber(time[2], counter, "minut poletanja");
					int duration = parseNumber(words[3].split(":")[1], counter, "trajanje leta");
					Airport source = temp.findAirport(from);
					Airport destination = temp.findAirport(to);
					if (source == null) {
						throw new FileFormatException("Red " + counter + ": polazni aerodrom '"
								+ from + "' nije definisan u sekciji airports.");
					}
					if (destination == null) {
						throw new FileFormatException("Red " + counter + ": dolazni aerodrom '"
								+ to + "' nije definisan u sekciji airports.");
					}
					temp.addFlight(new Flight(source, destination, duration, hour, min));
				}

			}
		}catch (IOException e) {
	        throw new FileFormatException("Fajl se ne moze procitati: " + path);
	    }

		if(!foundAirports) {
			throw new FileFormatException("JSON fajl nema sekciju \"airports\".");
		}

		system.replaceWith(temp);

	}
	

}
