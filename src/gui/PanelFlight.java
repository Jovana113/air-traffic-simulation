package gui;


import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;

import exceptions.ProjectException;
import logic.AirportSystem;
import models.Airport;
import models.Flight;

//forma za unos letova
public class PanelFlight extends Panel {

	private final AirportSystem system;
	private final Message display;
	private final Runnable onFlightAdded;

	private TextField fromField, toField, hourField, minuteField, durationField;

	public PanelFlight(AirportSystem system, Message display, Runnable onFlightAdded) {
		this.system = system;
		this.display = display;
		this.onFlightAdded = onFlightAdded;
		buildUI();
	}

	private void buildUI() {
		setLayout(new FlowLayout(FlowLayout.CENTER, 8, 8));
		setBackground(new Color(245, 245, 245));

		Label title = new Label("New flight:");
		title.setFont(new Font("SansSerif", Font.BOLD, 15));
		add(title);

		fromField = new TextField(4);
		toField = new TextField(4);
		hourField = new TextField(3);
		minuteField = new TextField(3);
		durationField = new TextField(5);

		add(makeLabel("From:"));
		add(fromField);
		add(makeLabel("To:"));
		add(toField);
		add(makeLabel("Hour:"));
		add(hourField);
		add(makeLabel("Min:"));
		add(minuteField);
		add(makeLabel("Duration:"));
		add(durationField);

		Button addButton = new Button("Add flight");
		addButton.setFont(new Font("SansSerif", Font.BOLD, 13));
		addButton.addActionListener(e -> addFlight());
		add(addButton);
	}

	private Label makeLabel(String text) {
		Label l = new Label(text, Label.RIGHT);
		l.setFont(new Font("SansSerif", Font.PLAIN, 14));
		return l;
	}

	private void addFlight() {
		try {
			String fromCode = fromField.getText().trim();
			String toCode = toField.getText().trim();

			if (fromCode.isEmpty() || toCode.isEmpty()
					|| hourField.getText().trim().isEmpty()
					|| minuteField.getText().trim().isEmpty()
					|| durationField.getText().trim().isEmpty()) {
				display.showMessage("Greska: sva polja moraju biti popunjena.");
				return;
			}

			int hour = Integer.parseInt(hourField.getText().trim());
			int minute = Integer.parseInt(minuteField.getText().trim());
			int duration = Integer.parseInt(durationField.getText().trim());

			Airport source = system.findAirport(fromCode);
			Airport destination = system.findAirport(toCode);

			if (source == null || destination == null) {
				display.showMessage("Greska: aerodrom ne postoji.");
				return;
			}

			Flight flight = new Flight(source, destination, duration, hour, minute);
			system.addFlight(flight);

			clearFields();
			display.showMessage("Dodat let: " + flight);
			onFlightAdded.run();

		} catch (NumberFormatException e) {
			display.showMessage("Greska: sati, minuti i trajanje moraju biti celi brojevi.");
		} catch (ProjectException e) {
			display.showMessage("Greska: " + e.getMessage());
		}
	}

	private void clearFields() {
		fromField.setText("");
		toField.setText("");
		hourField.setText("");
		minuteField.setText("");
		durationField.setText("");
	}
}