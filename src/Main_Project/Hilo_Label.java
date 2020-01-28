/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main_Project;

import java.util.List;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

/**
 *
 * @author CIO
 */
public class Hilo_Label extends SwingWorker<Double, Integer>{
    // Esta JProgressBar la recibiremos en el constructor o en
    // un parametro setProgreso()
    private JLabel progreso;
    String text;

    public Hilo_Label(JLabel progreso, String text) {
        this.progreso = progreso;
        this.text = text;
    }

    @Override
    protected void process(List<Integer> chunks) {
        System.out.println("process() esta en el hilo "
                + Thread.currentThread().getName());
        progreso.setText(text);
    }

    @Override
    protected Double doInBackground() throws Exception {
       progreso.setText(text); 
       return 100.0;
    }
}
    

