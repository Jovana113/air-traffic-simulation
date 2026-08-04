package util;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Timer;


//gasi program posle 60sek neaktivnosti
public class InactivityTimer {

    
    private static final int TIMEOUT = 60;
    private static final int WARNING = 55;
    private static final int TICK = 1000;

    private final Frame parent;
    private final Timer timer;

    private int seconds = 0;
    private boolean paused = false;

    private Dialog dialog;
    private Label countdownLabel;

    public InactivityTimer(Frame parent) {
        this.parent = parent;
        this.timer = new Timer(TICK, e -> tick());
    }

  
    public void start() {
        seconds = 0;
        timer.start();
    }

    //ignorise se dok je dijalog otvoren
    public void reset() {
    	if (dialog != null) return;
        seconds = 0;
    }
    
    private void confirmContinue() {
        seconds = 0;
        closeDialog();
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }
    
    private void tick() {
        if (paused) return;

        seconds++;

        if (seconds >= TIMEOUT) {
            System.exit(0);
        } else if (seconds == WARNING) {
            showDialog();
        } else if (seconds > WARNING) {
            updateCountdown();
        }
    }

    private void showDialog() {
        dialog = new Dialog(parent, "Upozorenje", false);   
        dialog.setLayout(new FlowLayout());

        countdownLabel = new Label(message(TIMEOUT - seconds));
        dialog.add(countdownLabel);

        Button continueButton = new Button("Nastavi");
        continueButton.addActionListener(e -> confirmContinue());
        dialog.add(continueButton);

        
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmContinue();
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

    private void updateCountdown() {
        if (countdownLabel != null) {
            countdownLabel.setText(message(TIMEOUT - seconds));
        }
    }

    private void closeDialog() {
        if (dialog != null) {
            dialog.dispose();
            dialog = null;
            countdownLabel = null;
        }
    }

    private String message(int n) {
        return "Program se zatvara za " + n + " s. Zelite li da nastavite?";
    }
}