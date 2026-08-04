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



//forma za unos aerodroma
public class PanelAirport extends Panel {

	private final AirportSystem system;
	private final Message display;
	private final Runnable onAirportAdded;

	private TextField nameField, codeField, xField, yField, code2Field;

	public PanelAirport(AirportSystem system, Message display, Runnable onAirportAdded) {
		this.system = system;
		this.display = display;
		this.onAirportAdded = onAirportAdded;
		buildUI();
	}
	
	

	private void buildUI() {
		setLayout(new FlowLayout(FlowLayout.CENTER, 8, 8));   
		setBackground(new Color(245, 245, 245));

		Label title = new Label("New  airport:");
		title.setFont(new Font("SansSerif", Font.BOLD, 15));
		add(title);

		
		nameField = new TextField(16);
		codeField = new TextField(4);
		code2Field = new TextField(4);
		xField = new TextField(5);
		yField = new TextField(5);

		add(makeLabel("Name:"));
		add(nameField);
		add(makeLabel("Code:"));
		add(codeField);
		add(makeLabel("Code2:"));
		add(code2Field);
		add(makeLabel("X:"));
		add(xField);
		add(makeLabel("Y:"));
		add(yField);

		Button addButton = new Button("Add airport");
		addButton.setFont(new Font("SansSerif", Font.BOLD, 13));
		addButton.addActionListener(e -> addAirport());
		add(addButton);
	}


	private Label makeLabel(String text) {
		Label l = new Label(text, Label.RIGHT);
		l.setFont(new Font("SansSerif", Font.PLAIN, 14));
		return l;
	}

	private void addAirport() {
		try {
			String name = nameField.getText().trim();
			String code = codeField.getText().trim();
			String code2 = code2Field.getText().trim();

			if (name.isEmpty() || code.isEmpty()
					|| xField.getText().trim().isEmpty()
					|| yField.getText().trim().isEmpty()) {
				display.showMessage("Greska: sva polja moraju biti popunjena.");
				return;
			}

			int x = Integer.parseInt(xField.getText().trim());
			int y = Integer.parseInt(yField.getText().trim());

			Airport a = new Airport(name, code, x, y, code2);
			system.addAirport(a);

			clearFields();
			display.showMessage("Dodat aerodrom: " + a);
			onAirportAdded.run();

		} catch (NumberFormatException e) {
			display.showMessage("Greska: koordinate moraju biti celi brojevi.");
		} catch (ProjectException e) {
			display.showMessage("Greska: " + e.getMessage());
		}
	}

	private void clearFields() {
		nameField.setText("");
		codeField.setText("");
		xField.setText("");
		yField.setText("");
		code2Field.setText("");
	}
}