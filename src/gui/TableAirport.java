package gui;


import java.awt.Color;

import logic.AirportSystem;
import models.Airport;


public class TableAirport extends Table {
	
    private static final String[] HEADERS = { "CODE", "NAME", "X", "Y", "CODE2" };

    

    public TableAirport(AirportSystem system) {
        super(system, "Airports", new Color(180, 200, 230), new Color(230, 240, 250));
    }

   @Override
    protected String[] getHeaders() {
        return HEADERS;
    }
    
   @Override
    protected void addDataRows() {
        for (Airport a : system.getAirports().values()) {
            rows.add(makeCell(a.getCode(), false));
            rows.add(makeCell(a.getName(), false));
            rows.add(makeCell(String.valueOf(a.getX()), false));
            rows.add(makeCell(String.valueOf(a.getY()), false));
            rows.add(makeCell(a.getCode2(), false));
        }
    }
    
   @Override
    protected int getRowCount() {
        return system.getAirports().size();
    }
    
   
}