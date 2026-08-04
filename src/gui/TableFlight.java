package gui;


import java.awt.Color;

import logic.AirportSystem;
import models.Flight;


public class TableFlight extends Table {


    private static final String[] HEADERS = { "FROM", "TO", "DEPARTURE", "DURATION" };

    public TableFlight(AirportSystem system) {
        super(system, "Flights",new Color(230, 190, 200), new Color(250, 235, 240));
    }

    @Override
    protected String[] getHeaders() {
        return HEADERS;
    }

    @Override
    protected void addDataRows() {
        for (Flight f : system.getFlights()) {
            rows.add(makeCell(f.getSource().getCode(), false));
            rows.add(makeCell(f.getDestination().getCode(), false));
            rows.add(makeCell(f.getTimeString(), false));
            rows.add(makeCell(String.valueOf(f.getDurationMin()), false));
        }
    }

    @Override
    protected int getRowCount() {
        return system.getFlights().size();
    }
    
}