package gui;

import java.awt.FileDialog;
import java.awt.CheckboxMenuItem;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;

import exceptions.ProjectException;
import io.CSV;
import io.FileHandler;
import io.JSON;
import logic.AirportSystem;


//meni i rad sa fajlovima
public class MenuController {
	private final Frame parent;
    private final AirportSystem system;
    private final Message display;
    private final Runnable onDataLoaded;
    private CheckboxMenuItem sifrovanje;
	
	
	public MenuController(Frame parent, AirportSystem system, Message display, Runnable onDataLoaded) {
		this.parent = parent;
		this.system = system;
		this.display = display;
		this.onDataLoaded = onDataLoaded;
		
	}
	
	public void addMenu() {
		MenuBar bar = new MenuBar();
		Menu file = new Menu("Menu");
		file.setFont(new Font("SansSerif", Font.PLAIN, 19));
		file.add("Save CSV");
		file.add("Load CSV");
		file.add("Save JSON");
		file.add("Load JSON");
		file.addSeparator();
		sifrovanje = new CheckboxMenuItem("Sifruj podatke");
		file.add(sifrovanje);
		
		
		file.addActionListener((ae) -> {
			handleCommand(ae.getActionCommand());
		});
		
		bar.setFont(new Font("SansSerif", Font.PLAIN, 19));
		bar.add(file);
		parent.setMenuBar(bar);
	}

	private void handleCommand(String command) {
		try {
			boolean isCsv = command.endsWith("CSV");
	        boolean isSave = command.startsWith("Save");
	        int shift = sifrovanje.getState() ? 3 : 0;

	        FileHandler handler = isCsv ? new CSV(shift) : new JSON(shift);
	        String path = chooseFile(isSave ? FileDialog.SAVE : FileDialog.LOAD);
	        if (path == null) return;

	        if (isSave) {
	            handler.save(system, path);
	            display.showMessage("Sacuvano u: " + path);
	        } else {
	            handler.load(system, path);
	            onDataLoaded.run();
	            display.showMessage("Ucitano iz: " + path);
	        }
	    }  catch (ProjectException e) {
	        display.showMessage("Greska: " + e.getMessage());
	    } catch (Exception e) {
	        display.showMessage("Neocekivana greska pri radu sa fajlom: " + e.getMessage());
	    }
		
	}

	private String chooseFile(int mode) {
		String title = (mode == FileDialog.LOAD) ? "Ucitaj fajl" : "Sacuvaj fajl";
		FileDialog fd = new FileDialog(parent, title, mode);
		fd.setVisible(true);
		String name = fd.getFile();
		if(name == null) return null;
		return fd.getDirectory() + name;
		
	}

}
