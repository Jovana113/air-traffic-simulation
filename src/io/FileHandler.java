package io;

import exceptions.FileFormatException;
import logic.AirportSystem;

public interface FileHandler {
	void save(AirportSystem system, String path) throws Exception;
	void load(AirportSystem system, String path)throws Exception;
	
	default int parseNumber(String text, int lineNumber, String fieldName)
            throws FileFormatException {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            throw new FileFormatException("Red " + lineNumber + ": " + fieldName
                    + " mora biti ceo broj, a pronadjeno je '" + text.trim() + "'.");
        }
    }
	
	default String encrypt(String s, int shift) {
		if(s == null || s.isEmpty()) return s;
		int n = s.length();
		shift = shift % n;
		int cut = n - shift;
		String prvi = "";
		String drugi = "";
		for(int i = 0; i < cut; i++) {
			prvi += s.charAt(i);
		}
		for(int i = cut; i < n; i++) {
			drugi += s.charAt(i);
		}
		return drugi+prvi;
	
	}
}
