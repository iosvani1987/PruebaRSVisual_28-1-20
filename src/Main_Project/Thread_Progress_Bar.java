/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main_Project;

import static Main_Project.Menu.jTextArea_Salida;
import simulating_analing_paralell.Algorithm_Simulate_Analing;

/**
 *
 * @author CIO
 */
public class Thread_Progress_Bar extends Thread {

    public void run() {
//        Progress_Bar bar = new Progress_Bar();
        long TInicio, TFin, tiempo; //Variables para determinar el tiempo de ejecución
        TInicio = System.currentTimeMillis();
         // Espero por todos los hilos
            for (Algorithm_Simulate_Analing h : Menu.hilos) {
                while (h.isAlive()) {
                    }
            }
            
            TFin = System.currentTimeMillis(); //Tomamos la hora en que finalizó el algoritmo y la almacenamos en la variable T
            tiempo = TFin - TInicio;
            jTextArea_Salida.append("TIEMPO: " + tiempo/1000 +" segundos");
    }

}
