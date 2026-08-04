package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.awt.ScrollPane;

import logic.AirportSystem;


//osnova za tabele, podklase odredjuju samo kolone i redove
public abstract class Table extends Panel {
	
	protected static final int ROW_HEIGHT = 26;
	protected final AirportSystem system;
    protected final Panel rows;
    protected final ScrollPane scroll;
    private final Color headerColor;
    private final Color cellColor;

	
	protected Table(AirportSystem system, String title, Color headerColor, Color cellColor) {
		this.system = system;
        this.headerColor = headerColor;
        this.cellColor = cellColor;

        setLayout(new BorderLayout());

        Label heading = new Label(title, Label.CENTER);
        heading.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(heading, BorderLayout.NORTH);

        rows = new Panel();
        //linije resetke
        rows.setLayout(new GridLayout(0, getHeaders().length, 1, 1));
        rows.setBackground(Color.GRAY);

        scroll = new ScrollPane();
        scroll.add(rows);
        add(scroll, BorderLayout.CENTER);

        refresh();
	}
	
	 public final void refresh() {
	        rows.removeAll();

	        for (String header : getHeaders()) {
	            rows.add(makeCell(header, true));
	        }
	        addDataRows();

	        int totalRows = getRowCount() + 1;   
	        Dimension size = new Dimension(scroll.getWidth(), totalRows * ROW_HEIGHT);
	        rows.setSize(size);
	        rows.setPreferredSize(size);

	        scroll.validate();
	        scroll.doLayout();
	    }
	 
	 
	 protected abstract String[] getHeaders();
	 protected abstract void addDataRows();
	 protected abstract int getRowCount();

	 protected Label makeCell(String text, boolean isHeader) {
	        Label l = new Label("   " + text);
	        if (isHeader) {
	            l.setFont(new Font("SansSerif", Font.BOLD, 12));
	            l.setBackground(headerColor);
	        } else {
	            l.setFont(new Font("SansSerif", Font.PLAIN, 12));
	            l.setBackground(cellColor);
	        }
	        return l;
	    }

	

}
