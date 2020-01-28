 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulating_analing_paralell;

import Main_Project.Menu;
import static Main_Project.Menu.jTextArea_Salida;
import java.awt.Dimension;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import javafx.scene.control.ProgressBar;
import javax.swing.SwingWorker;
import org.math.array.DoubleArray;
import org.math.plot.Plot3DPanel;

/**
 *
 * @author Iosvani
 */
public class Solution_Node implements Comparable<Solution_Node> {

    private double[] coef; // Matriz de coeficientes que aproxima el polinomio de la Fase
    private double fitness; // Valor de la Funcion Objetivo
    private double[][] phase;
    private int ID;
    private double Fitness_Similitud;
    private double Fitness_suavidad;

    public Solution_Node(double[] coef, double fitness, double[][] phase, int ID) {
        this.coef = coef;
        this.fitness = fitness;
        this.phase = phase;
        this.ID = ID;
    }

    public Solution_Node(int poly_orden, double[] delta, double[][] Imagen, double suavidad) {
//        this.Image = Imagen;
//        this.suavidad = suavidad;
        int num_coef = (poly_orden + 1) * (poly_orden + 2) / 2;
        this.coef = new double[num_coef];
        Random_Node(num_coef, delta);
        this.fitness = Funcion_Objetivo(Imagen, suavidad);

    }

    public Solution_Node(double[] coef_array, int poly_orden, double[] delta, double[][] Imagen, double suavidad) {

        int num_coef = (poly_orden + 1) * (poly_orden + 2) / 2;
        this.coef = new double[num_coef];
//        Random_Node(num_coef, delta);
        for (int i = 0; i < coef_array.length; i++) {
            this.coef[i] = coef_array[i];
        }
        this.fitness = Funcion_Objetivo(Imagen, suavidad);

    }

    public Solution_Node(double[] coef, double[][] Imagen, double suavidad) {
//        this.Image = Imagen;
        this.coef = coef;
        this.fitness = Funcion_Objetivo(Imagen, suavidad);
    }

    public Solution_Node(int ID, double[][] Imagen) {
//        this.Image = Imagen;
        this.ID = ID;
        this.phase = Imagen;
        this.coef = null;
    }

    private void Random_Node(int num_coef, double[] delta) {

        for (int i = 0; i < num_coef; i++) {
//            this.coef[i] = 0;
            this.coef[i] = Auxiliar.randomValue_Generate(-delta[i], delta[i]);
        }
    }

    /**
     * IMPLEMENTA EL METODO COMARETO DE COMPARABLE
     *
     */
    public int compareTo(Solution_Node nodo) {
        if (this.ID < nodo.getID()) {
            return -1;
        }
        if (this.ID == nodo.getID()) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * ** CREA UN NODO VECINO A PARTIR DE UNA FUNCION DE VECINDAD ***
     */
    public Solution_Node Neiborght(int idx, double[] delta_coef, double factor, double[][] Imagen, double suavidad) {
        double[] temp = this.getCoef().clone();
        double value = this.getCoef()[idx];
//        double factor = 0.1;

//        temp[idx] = value + Auxiliar.randomValue_Generate(-factor * delta_coef[idx], factor * delta_coef[idx]);
        temp[idx] = value + Auxiliar.randomValue_Generate(-1, 1) * 2 * delta_coef[idx];

        if (temp[idx] > delta_coef[idx] || temp[idx] < -delta_coef[idx]) {
            temp[idx] = Auxiliar.randomValue_Generate(-delta_coef[idx], delta_coef[idx]);
        }
        Solution_Node result = new Solution_Node(temp, Imagen, suavidad);

        return result;
    }

    /**
     * ** CREA UN NODO VECINO A PARTIR DE UNA FUNCION DE VECINDAD ***
     */
    public Solution_Node Neiborght_Gredy(double[] delta_coef, double[][] Imagen, double suavidad) {
        double[] temp = this.getCoef().clone();
        double alpha = Auxiliar.randomValue_Generate(-1, 1);
        temp[0] = +alpha * 2 * delta_coef[0];
        if (temp[0] > delta_coef[0] || temp[0] < -delta_coef[0]) {
            temp[0] = Auxiliar.randomValue_Generate(-delta_coef[0], delta_coef[0]);
        }
        Solution_Node node = new Solution_Node(temp, Imagen, suavidad);
        for (int i = 1; i < temp.length; i++) {
            temp = this.getCoef().clone();
            temp[i] = +alpha * 2 * delta_coef[i];
            Solution_Node node_aux = new Solution_Node(temp, Imagen, suavidad);
            if (node_aux.getFitness() < node.getFitness()) {
                node = node_aux;
            }
        }
        return node;
    }

    /**
     * ** CREA UN NODO VECINO A PARTIR DE UNA FUNCION DE VECINDAD ***
     */
    public Solution_Node Neiborght_Gredy_FactorV_1D(double[] delta_coef, double factor, double[][] Imagen, double suavidad, int k_vecinos) {
        double[] temp = this.getCoef().clone();
        if (k_vecinos >= delta_coef.length) {
            temp[0] = +Auxiliar.randomValue_Generate(-factor * delta_coef[0], factor * delta_coef[0]);
            if (temp[0] > delta_coef[0] || temp[0] < -delta_coef[0]) {
                temp[0] = Auxiliar.randomValue_Generate(-delta_coef[0], delta_coef[0]);
            }
            Solution_Node node = new Solution_Node(temp, Imagen, suavidad);
            for (int i = 1; i < temp.length; i++) {
                temp = this.getCoef().clone();
                temp[i] = +Auxiliar.randomValue_Generate(-factor * delta_coef[i], factor * delta_coef[i]);
                Solution_Node node_aux = new Solution_Node(temp, Imagen, suavidad);
                if (node_aux.getFitness() < node.getFitness()) {
                    node = node_aux;
                }
            }
            if (k_vecinos > delta_coef.length) {
                for (int i = 0; i < k_vecinos - delta_coef.length; i++) {
                    int idx1 = Auxiliar.randomInt(delta_coef.length-1);
                    temp[idx1] = +Auxiliar.randomValue_Generate(-factor * delta_coef[idx1], factor * delta_coef[idx1]);
                    if (temp[idx1] > delta_coef[idx1] || temp[idx1] < -delta_coef[idx1]) {
                        temp[idx1] = Auxiliar.randomValue_Generate(-delta_coef[idx1], delta_coef[idx1]);
                    }
                    Solution_Node node_aux = new Solution_Node(temp, Imagen, suavidad);
                    if (node_aux.getFitness() < node.getFitness()) {
                        node = node_aux;
                    }
                }
            }
            return node;
        } else {
            int idx1 = Auxiliar.randomInt(delta_coef.length-1);
            temp[idx1] = +Auxiliar.randomValue_Generate(-factor * delta_coef[idx1], factor * delta_coef[idx1]);
            if (temp[idx1] > delta_coef[idx1] || temp[idx1] < -delta_coef[idx1]) {
                temp[idx1] = Auxiliar.randomValue_Generate(-delta_coef[idx1], delta_coef[idx1]);
            }
            Solution_Node node = new Solution_Node(temp, Imagen, suavidad);

            for (int i = 1; i < k_vecinos; i++) {
                idx1 = Auxiliar.randomInt(delta_coef.length - 1);
                temp[idx1] = +Auxiliar.randomValue_Generate(-factor * delta_coef[idx1], factor * delta_coef[idx1]);
                if (temp[idx1] > delta_coef[idx1] || temp[idx1] < -delta_coef[idx1]) {
                    temp[idx1] = Auxiliar.randomValue_Generate(-delta_coef[idx1], delta_coef[idx1]);
                }
                Solution_Node node_aux = new Solution_Node(temp, Imagen, suavidad);
                if (node_aux.getFitness() < node.getFitness()) {
                    node = node_aux;
                }
            }
            return node;
        }

    }

    /**
     * ** CREA UN NODO VECINO A PARTIR DE UNA FUNCION DE VECINDAD ***
     */
    public Solution_Node Neiborght_Gredy_FactorV_2D(double[] delta_coef, double factor, double[][] Imagen, double suavidad, int k_vecinos) {
        double[] temp = this.getCoef().clone();
        if (k_vecinos >= delta_coef.length) {
            temp[0] = +Auxiliar.randomValue_Generate(-factor * delta_coef[0], factor * delta_coef[0]);
            if (temp[0] > delta_coef[0] || temp[0] < -delta_coef[0]) {
                temp[0] = Auxiliar.randomValue_Generate(-delta_coef[0], delta_coef[0]);
            }
            int idx1 = Auxiliar.randomInt(delta_coef.length-1);
            temp[idx1] = +Auxiliar.randomValue_Generate(-factor * delta_coef[idx1], factor * delta_coef[idx1]);
            if (temp[idx1] > delta_coef[idx1] || temp[idx1] < -delta_coef[idx1]) {
                temp[idx1] = Auxiliar.randomValue_Generate(-delta_coef[idx1], delta_coef[idx1]);
            }
            Solution_Node node = new Solution_Node(temp, Imagen, suavidad);

            for (int i = 1; i < temp.length; i++) {
                temp = this.getCoef().clone();
                temp[i] = +Auxiliar.randomValue_Generate(-factor * delta_coef[i], factor * delta_coef[i]);
                idx1 = Auxiliar.randomInt(delta_coef.length-1);
                temp[idx1] = +Auxiliar.randomValue_Generate(-factor * delta_coef[idx1], factor * delta_coef[idx1]);
                if (temp[idx1] > delta_coef[idx1] || temp[idx1] < -delta_coef[idx1]) {
                    temp[idx1] = Auxiliar.randomValue_Generate(-delta_coef[idx1], delta_coef[idx1]);
                }
                Solution_Node node_aux = new Solution_Node(temp, Imagen, suavidad);
                if (node_aux.getFitness() < node.getFitness()) {
                    node = node_aux;
                }
            }
            if (k_vecinos > delta_coef.length) {
                for (int i = 0; i < k_vecinos - delta_coef.length; i++) {
                    idx1 = Auxiliar.randomInt(delta_coef.length-1);
                    int idx2 = Auxiliar.randomInt(delta_coef.length-1);

                    temp[idx1] = +Auxiliar.randomValue_Generate(-factor * delta_coef[idx1], factor * delta_coef[idx1]);
                    if (temp[idx1] > delta_coef[idx1] || temp[idx1] < -delta_coef[idx1]) {
                        temp[idx1] = Auxiliar.randomValue_Generate(-delta_coef[idx1], delta_coef[idx1]);
                    }
                    temp[idx2] = +Auxiliar.randomValue_Generate(-factor * delta_coef[idx2], factor * delta_coef[idx2]);
                    if (temp[idx2] > delta_coef[idx2] || temp[idx2] < -delta_coef[idx2]) {
                        temp[idx2] = Auxiliar.randomValue_Generate(-delta_coef[idx2], delta_coef[idx2]);
                    }

                    Solution_Node node_aux = new Solution_Node(temp, Imagen, suavidad);
                    if (node_aux.getFitness() < node.getFitness()) {
                        node = node_aux;
                    }
                }
            }
            return node;
        } else {
            int idx1 = Auxiliar.randomInt(delta_coef.length-1);
            int idx2 = Auxiliar.randomInt(delta_coef.length-1);

            temp[idx1] = +Auxiliar.randomValue_Generate(-factor * delta_coef[idx1], factor * delta_coef[idx1]);
            if (temp[idx1] > delta_coef[idx1] || temp[idx1] < -delta_coef[idx1]) {
                temp[idx1] = Auxiliar.randomValue_Generate(-delta_coef[idx1], delta_coef[idx1]);
            }
            temp[idx2] = +Auxiliar.randomValue_Generate(-factor * delta_coef[idx2], factor * delta_coef[idx2]);
            if (temp[idx2] > delta_coef[idx2] || temp[idx2] < -delta_coef[idx2]) {
                temp[idx2] = Auxiliar.randomValue_Generate(-delta_coef[idx2], delta_coef[idx2]);
            }
            Solution_Node node = new Solution_Node(temp, Imagen, suavidad);

            for (int i = 1; i < k_vecinos; i++) {
                idx1 = Auxiliar.randomInt(delta_coef.length-1);
                idx2 = Auxiliar.randomInt(delta_coef.length-1);

                temp = this.getCoef().clone();
                temp[idx1] = +Auxiliar.randomValue_Generate(-factor * delta_coef[idx1], factor * delta_coef[idx1]);
                temp[idx2] = +Auxiliar.randomValue_Generate(-factor * delta_coef[idx2], factor * delta_coef[idx2]);

                Solution_Node node_aux = new Solution_Node(temp, Imagen, suavidad);
                if (node_aux.getFitness() < node.getFitness()) {
                    node = node_aux;
                }
            }
            return node;
        }

//        double[] temp = this.getCoef().clone();
//        int idx1 = Auxiliar.randomInt(delta_coef.length);
//        int idx2 = Auxiliar.randomInt(delta_coef.length);
//
//        temp[idx1] = +Auxiliar.randomValue_Generate(-factor * delta_coef[idx1], factor * delta_coef[idx1]);
//        if (temp[idx1] > delta_coef[idx1] || temp[idx1] < -delta_coef[idx1]) {
//            temp[idx1] = Auxiliar.randomValue_Generate(-delta_coef[idx1], delta_coef[idx1]);
//        }
//        temp[idx2] = +Auxiliar.randomValue_Generate(-factor * delta_coef[idx2], factor * delta_coef[idx2]);
//        if (temp[idx2] > delta_coef[idx2] || temp[idx2] < -delta_coef[idx2]) {
//            temp[idx2] = Auxiliar.randomValue_Generate(-delta_coef[idx2], delta_coef[idx2]);
//        }
//        Solution_Node node = new Solution_Node(temp, Imagen, suavidad);
//
//        for (int i = 1; i < temp.length; i++) {
//            idx1 = Auxiliar.randomInt(delta_coef.length);
//            idx2 = Auxiliar.randomInt(delta_coef.length);
//
//            temp = this.getCoef().clone();
//            temp[idx1] = +Auxiliar.randomValue_Generate(-factor * delta_coef[idx1], factor * delta_coef[idx1]);
//            temp[idx2] = +Auxiliar.randomValue_Generate(-factor * delta_coef[idx2], factor * delta_coef[idx2]);
//
//            Solution_Node node_aux = new Solution_Node(temp, Imagen, suavidad);
//            if (node_aux.getFitness() < node.getFitness()) {
//                node = node_aux;
//            }
//        }
//        return node;
    }

    /**
     * * Calcula el valor de la Funcion Objetivo de un nod ***
     */
    private double Funcion_Objetivo(double[][] Imagen, double suavidad) {
        double fitness = 0;
        this.Fitness_Similitud = 0;
        this.Fitness_suavidad = 0;
        double Interferogram;
        int rows = Imagen.length;
        int cols = Imagen[0].length;
        setPhase(new double[rows][cols]);

        double x, y;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
//                x = Xmin + Dx * (j);
//                y = Ymin + Dy * (i);
                x = j;
                y = i;
//                System.out.println(i + "--" + j);
                phase[i][j] = Phase_Funtion(this.getCoef(), x, y);
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Interferogram = 127 - 128 * Math.cos(getPhase()[i][j]);
//                fitness += Math.pow(Interferogram - Imagen[i][j], 2);
                if (i == 0 || j == 0) {
                    this.Fitness_Similitud += Math.pow(Interferogram - Imagen[i][j], 2);
                } else {
                    this.Fitness_Similitud += Math.pow(Interferogram - Imagen[i][j], 2);
                    this.Fitness_suavidad += suavidad * Math.pow(getPhase()[i][j] - getPhase()[i - 1][j - 1], 2) + suavidad * Math.pow(getPhase()[i - 1][j] - getPhase()[i][j - 1], 2);
                }
            }
        }

        fitness = this.Fitness_Similitud + this.Fitness_suavidad;
        this.Fitness_Similitud /= (rows * cols);
        this.Fitness_suavidad /= (rows * cols);
        return fitness / (rows * cols);
    }

    /**
     * METODO QUE CALCULA LE VALOR DE LA FASE A PARTIR DEL VECTOR DE
     * COEFICIENTES
     */
    public double Phase_Funtion(double[] coef, double x, double y) {
        double phase = coef[0];
        int k, exp, c = 0;
        int grado = (int) (-3 + Math.sqrt(9 + 8 * (coef.length - 1))) / 2;
        for (int i = 1; i <= grado; i++) {
            k = 0;
            for (int j = i + c; j <= 2 * i + c; j++) {
                exp = (i + k + 2) % (i + 1);
                phase += coef[j] * Math.pow(x, exp) * Math.pow(y, i - exp);
                k++;
            }
            c += i;
        }
        return phase;
    }

    /**
     * METODO QUE CALCULA LA DERIVADA A PARTIR DE LOS COEFICIENTES DEL VECTOR DE
     * COEFICIENTES
     */
    public double Derivate_Ai(double[] coef, double x, double y, int idx) {
        double phase = 1;
        int k, exp, c = 0;
        int grado = (int) (-3 + Math.sqrt(9 + 8 * (coef.length - 1))) / 2;
        if (idx == 0) {
            return phase;
        }
        for (int i = 1; i <= grado; i++) {
            k = 0;
            for (int j = i + c; j <= 2 * i + c; j++) {
                exp = (i + k + 2) % (i + 1);
                if (idx == j) {
                    return Math.pow(x, exp) * Math.pow(y, i - exp);
                }
//                phase += coef[j] * Math.pow(x, exp) * Math.pow(y, i - exp);
                k++;
            }
            c += i;
        }
        return phase;
    }

    /**
     * METODO QUE CALCULA EL VECTOR GRADIENETE DE LA FUNCION OBJETIVO
     *
     */
    public double[] Gradiente(double[][] Imagen, double Suavidad) {
        double[] gradiente = new double[coef.length];
        int rows = Imagen.length;
        int cols = Imagen[0].length;
        double x, y;

        for (int idx = 0; idx < gradiente.length; idx++) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    x = j;
                    y = i;
                    gradiente[idx] += -254 * Math.sin(this.phase[i][j]) * Derivate_Ai(coef, x, y, idx) + 2 * Suavidad * ((Derivate_Ai(coef, x, y, idx) - Derivate_Ai(coef, x + 1, y + 1, idx)) + (Derivate_Ai(coef, x + 1, y, idx) - Derivate_Ai(coef, x, y + 1, idx)));
                }
            }
        }

        return gradiente;
    }

    /**
     * @return the coef
     */
    public double[] getCoef() {
        return coef;
    }

    /**
     * @return the fitness
     */
    public double getFitness() {
        return fitness;
    }

    /**
     * @param fitness the fitness to set
     */
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public synchronized void PrintFile(String name, int rows, int cols, String file_name) {
        String temp = "";
        for (int i = 0; i < this.coef.length; i++) {
            temp = temp + this.coef[i] + ", ";
        }
        temp = name + "\n" + temp + "\n" + "Fitness: " + this.fitness + "\n" + rows + " " + cols + "\n";

        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter(file_name, true);
            pw = new PrintWriter(fichero);
            pw.println(temp);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Nuevamente aprovechamos el finally para 
                // asegurarnos que se cierra el fichero.
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }

    /**
     *
     * @param N numero de elementos
     * @return
     */
    public double[] Increment(int N, double begin, double end) {
        double[] result = new double[N];
        double paso = (end - begin) / (double) N;
        result[0] = begin;
        for (int i = 1; i < N; i++) {
            result[i] = result[i - 1] + paso;
        }
        return result;
    }

    public synchronized void PrintPront() {
//        Progress_Bar.value++;
        String temp = "";
        for (int i = 0; i < getCoef().length; i++) {
            temp = temp + getCoef()[i] + ", ";
        }
        temp = "Windows: "+ID + "\n" + temp + "\n" + "Fitness: " + getFitness() + "  Similitud: " + this.Fitness_Similitud/(phase.length*phase[0].length) + " Suavidad: " + this.Fitness_suavidad + "\n" + phase.length + " " + phase[0].length + "\n \n";
        Menu.jTextArea_Salida.append(temp);
//        worker.execute();
//        Menu.jTextArea_Salida.append(temp);
//        double[] x = DoubleArray.increment(0.0, 1.0, (double) phase[0].length - 1);
//        double[] y = DoubleArray.increment(0.0, 1.0, (double) phase.length);
////        double[]x = Increment(150, 0, (double) phase[0].length - 1);
////        double[]y = Increment(140, 0, (double) phase.length - 1);
//        Plot3DPanel grafica3D = new Plot3DPanel("SOUTH");
//        grafica3D.addGridPlot("Fase", x, y, phase);
////        Menu.jInternalFrame_fase.setPreferredSize(new Dimension(203, 190));
//        Menu.jInternalFrame_fase.setContentPane(grafica3D);
    }

    public synchronized void PrintFile(String name, int rows, int cols, String file_name, String str_Fitness) {
        String temp = "";
        for (int i = 0; i < this.coef.length; i++) {
            temp = temp + this.coef[i] + ", ";
        }
        temp = name + "\n" + temp + "\n" + "Fitness: " + this.fitness + "\n" + rows + " " + cols + "\n" + str_Fitness + "\n";

        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter(file_name, true);
            pw = new PrintWriter(fichero);
            pw.println(temp);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Nuevamente aprovechamos el finally para 
                // asegurarnos que se cierra el fichero.
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }

    public synchronized void PrintFile_Fitness(String name, ArrayList<Solution_Node> better) {
//        System.out.println(name +"  "+ better.size());
        String temp = "";
        for (Solution_Node nodo : better) {
            temp = temp + nodo.getFitness() + ", ";
        }

        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            File dir = new File("Fitness");
            dir.mkdir();
            fichero = new FileWriter("Fitness\\" + name + ".txt", true);
            pw = new PrintWriter(fichero);
            pw.println(temp);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Nuevamente aprovechamos el finally para 
                // asegurarnos que se cierra el fichero.
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }

    public synchronized String toString(String name) {
        String temp = "";
        for (int i = 0; i < this.coef.length; i++) {
            temp = temp + this.coef[i] + ", ";
        }
        temp = name + "\n" + temp + "\n" + "Fitness: " + this.fitness + "\n";
        return temp;
    }

    /**
     * @return the phase
     */
    public double[][] getPhase() {
        return phase;
    }

    /**
     * @return the ID
     */
    public int getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * @param phase the phase to set
     */
    public void setPhase(double[][] phase) {
        this.phase = phase;
    }

}
