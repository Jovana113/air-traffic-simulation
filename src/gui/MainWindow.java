package gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import logic.AirportSystem;
import util.InactivityTimer;


//glavni prozor, sklapa panele, nema svoju logiku
public class MainWindow extends Frame implements Message {

	private final AirportSystem system = new AirportSystem();

	private Label statusLabel;
	private TableAirport airportTable;
	private TableFlight flightTable;
	private InactivityTimer timer;

	public MainWindow() {
		super("Airports and flights");
		setSize(1200, 800);
		setBackground(new Color(245, 245, 245));
		setLocationRelativeTo(null);

		timer = new InactivityTimer(this);
		addComponents();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
		
		
		//svaki dogadjaj misa ili tastature racuna se kao akcija korisnika
		Toolkit.getDefaultToolkit().addAWTEventListener(
			e -> timer.reset(),
			AWTEvent.MOUSE_EVENT_MASK
				| AWTEvent.MOUSE_MOTION_EVENT_MASK
				| AWTEvent.KEY_EVENT_MASK
		);

		timer.start();
		setVisible(true);
	}

	private void addComponents() {
		statusLabel = new Label(" ");
		statusLabel.setFont(new Font("SansSerif", Font.BOLD, 17));
		
		Panel statusPanel = new Panel();
		statusPanel.setLayout(new BorderLayout());
		statusPanel.setBackground(new Color(220, 220, 220));
		statusPanel.add(statusLabel, BorderLayout.CENTER);
		statusPanel.setPreferredSize(new Dimension(0, 55));
		add(statusPanel, BorderLayout.SOUTH);

		airportTable = new TableAirport(system);
		flightTable = new TableFlight(system);

		
		//panel dobija akciju, ne referencu na tabelu
		Runnable afterAirportAdded = () -> airportTable.refresh();
		Runnable afterFlightAdded  = () -> flightTable.refresh();
		Runnable afterDataLoaded   = () -> refreshTables();


		Panel forms = new Panel();
		forms.setLayout(new GridLayout(2, 1));
		forms.add(new PanelAirport(system, this, afterAirportAdded));
		forms.add(new PanelFlight(system, this, afterFlightAdded));
		add(forms, BorderLayout.NORTH);
		
		Panel tables = new Panel();
		tables.setLayout(new GridLayout(1, 2, 20, 0));
		tables.add(airportTable);
		tables.add(flightTable);
		add(tables, BorderLayout.CENTER);

		MenuController menu = new MenuController(this, system, this, afterDataLoaded);
		menu.addMenu();
	}

	
	private void refreshTables() {
		airportTable.refresh();
		flightTable.refresh();
	}

	@Override
	public void showMessage(String text) {
		statusLabel.setText(" " + text);
	}

}