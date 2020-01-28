/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulating_analing_paralell;

import Main_Project.Menu;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Iosvani
 */
public class Algorithm_Simulate_Analing extends Thread {

    /**
     * PARAMETROS DEL METODO RECOCIDO SIMULADO *
     */
    private double[] Temperatura;   // Arreglo de la temperatura definida
    private int N;                  // Numero de  iteraciones
    private double[] delta;         // Arreglo con  las variaciones de cada coeficiente
    private int ITemp;              // Iteraciones por cada nivel de Temperatura
    private double factor_suavidad;
    private int vecindad_method;
    private int run_2_method;
    private int k_vecinos;

    private double factor_vecindad_fijo;
    private double factor_vecindad_final;
    private double factor_vecindad_inicial;
    private double factor_vecindad;

    /**
     * Parametros para el Nodo Solucion *
     */
    private int poly_orden;         // Orden del polinomio
    Solution_Node best;

    /**
     * * VARIABLES DE LA IMAGEN**
     */
    private double[][] Imagen;  // Matriz con los valores de la imagen
    private Random rand;        // Generador Aleatorio
    private int rows;
    private int cols;

    private double Error;
    private int num_franjas;
    private double factor_delta;

    public Algorithm_Simulate_Analing(double[][] Img, int poly_orden, double[] Temperatura, int N, int ITemp, int metodo_vecindad, int k_vecino, double factor_vecindad_fijo, double factor_vecindad_final, double factor_vecindad_inicial, double factor_suavidad, double error, double factor_delta, int run_2_method ) {
        /**
         * * INICIALIZACION DE LOS PARAMETROS DEL ALGORITMO ***
         */
        this.Imagen = Img;
        this.poly_orden = poly_orden;
        this.Temperatura = Temperatura;
        this.N = N;
        this.ITemp = ITemp;
        this.vecindad_method = metodo_vecindad;
        this.k_vecinos = k_vecino;
        this.run_2_method = run_2_method;

        this.factor_vecindad_fijo = factor_vecindad_fijo;
        this.factor_vecindad_final = factor_vecindad_final;
        this.factor_vecindad_inicial = factor_vecindad_inicial;
        this.factor_suavidad = factor_suavidad;

        this.Error = error;
        this.factor_delta = factor_delta;

        // INICIALIZA LA SEMILLA PARA LOS VALORES ALEATORIOS
        rand = new Random(System.currentTimeMillis());

        // CALCULAR EL NUMERO DE FRANJAS  DE LA MATRIZ
        int[][] temp_matrix = BW(Imagen);
        int h = Franges_Hor(temp_matrix);
        int v = Franges_Ver(temp_matrix);
        int d = Franges_Diagonal(temp_matrix);
        int d1 = Franges_Diagonal_Inversa(temp_matrix);
//        int num_franjas;
        if (h > v && h > d) {
            num_franjas = h;
        } else {
            if (v >= h && v > d) {
                num_franjas = v;
            } else {
                num_franjas = d;
            }
        }
        if (num_franjas < d1) {
            num_franjas = d1;
        }

        // CALCULA LOS INTERVALOS DE LOS COEFIICENTES
        this.rows = this.Imagen.length;
        this.cols = this.Imagen[0].length;
        this.delta = Delta(num_franjas, poly_orden, cols, rows);
        for (int i = 0; i < this.delta.length; i++) {
            this.delta[i] = factor_delta * this.delta[i];
        }

    }

    public void run() {

        Solution_Node s_j, s_i;
        this.best = new Solution_Node(this.poly_orden, this.delta, Imagen, factor_suavidad);
        int index_rep = 0;
        ArrayList<Solution_Node> better = new ArrayList<>();

        while (index_rep < 2) {
            double dE, prob;
            int v, best_idx = 0, kk = 0;

            if (index_rep < 1) {
                s_i = this.best;
            } else {
                switch (run_2_method) {
                    case 0:
                        s_i = this.best;
                        break;
                    case 1:
                        // Aumentar el grado del polinomio
                        this.delta = Delta(num_franjas, poly_orden + 1, cols, rows);
                        for (int i = 0; i < this.delta.length; i++) {
                            this.delta[i] = factor_delta * this.delta[i];
                        }
                        s_i = new Solution_Node(this.best.getCoef(), poly_orden + 1, delta, Imagen, this.factor_suavidad);
                        break;
                    case 2:
                        // Aumentar el delta en 2
                        for (int i = 0; i < this.delta.length; i++) {
                            this.delta[i] = 2*factor_delta * this.delta[i];
                        }
                        s_i = new Solution_Node( poly_orden , delta, Imagen, this.factor_suavidad);
                        break;
                        default:
                            s_i = this.best;
                            break;

                }

            }

            for (int i = 0; i < this.N; i++) {
                better.add(this.best);

                for (int j = 0; j < this.ITemp; j++) {
                    double m, n, factor_lineal;
                    switch (vecindad_method) {
                        case 0:
                            m = (this.factor_vecindad_final - this.factor_vecindad_inicial) / (this.N - 1);
                            n = this.factor_vecindad_inicial;
                            factor_lineal = m * i + n;
                            // Busqueda goloza (Vecinos Modificados a partir de un coeficientes) Lineal
                            s_j = s_i.Neiborght_Gredy_FactorV_1D(delta, factor_lineal, Imagen, this.factor_suavidad, this.k_vecinos);
                            break;

                        case 1:
                            m = (this.factor_vecindad_final - this.factor_vecindad_inicial) / (this.N - 1);
                            n = this.factor_vecindad_inicial;
                            factor_lineal = m * i + n;
                            // Busqueda goloza (Vecinos Modificados a partir de un coeficientes) Lineal
                            s_j = s_i.Neiborght_Gredy_FactorV_2D(delta, factor_lineal, Imagen, this.factor_suavidad, this.k_vecinos);
                            break;

                        case 2:
                            // Busqueda goloza (Vecinos Modificados a partir de un coeficientes) Boltzman
                            s_j = s_i.Neiborght_Gredy_FactorV_1D(delta, this.Temperatura[i] / this.Temperatura[0], Imagen, this.factor_suavidad, this.k_vecinos);
                            break;

                        case 3:
                            // Busqueda goloza (Vecinos Modificados a partir de 2 coeficientes) Boltzman
                            s_j = s_i.Neiborght_Gredy_FactorV_2D(delta, this.Temperatura[i] / this.Temperatura[0], Imagen, this.factor_suavidad, this.k_vecinos);
                            break;

                        case 4:
                            // Factor de vecindad Fijo
                            v = rand.nextInt(s_i.getCoef().length); // Genero un nuemero aleatorio para modificar un coeficiente
                            s_j = s_i.Neiborght(v, delta, this.factor_vecindad, Imagen, factor_suavidad);     // Genero un nuevo Nodo Variando un coeficiente en un intervalo
                            break;
                        default:
                            // Busqueda Goloza sin factor
                            s_j = s_i.Neiborght_Gredy(delta, Imagen, this.factor_suavidad);

                    }

                    dE = s_j.getFitness() - s_i.getFitness();           // Calculo la diferencia entre los fitness de cada nodo

                    if (dE < 0) {
                        s_i = s_j;
                        if (s_i.getFitness() < best.getFitness()) {
                            best = s_i;
                            best_idx = i;
                        }
//                    System.out.println("1* "+kk++);
                    } else {
                        prob = Math.exp(-dE / this.Temperatura[i]);
                        if (Math.random() < prob) {
                            s_i = s_j;
                        }
                    }
                }

            }
            if (this.best.getFitness() < this.Error) {
                break;
            }
            index_rep++;
//            this.poly_orden++;
//            this.delta = Delta(num_franjas, poly_orden, cols, rows);
//            best = new Solution_Node(best.getCoef(), poly_orden, delta, Imagen, factor_suavidad);
        }
////        System.out.println(best.getFitness());
////        // DESCENSO DE GRADIENTE
////        double alphaG = 0.00000005;
////        Solution_Node Current = best;
////        for (int i = 0; i < 1000; i++) {
////            double[] coef = new double[Current.getCoef().length];
////            double[] grad = Current.Gradiente(Imagen, factor_suavidad);
////            for (int j = 0; j < coef.length; j++) {
////                coef[j] = Current.getCoef()[j] - alphaG * grad[j];
////            }
////            Solution_Node tmp = new Solution_Node(coef, Imagen, factor_suavidad);
////            if (tmp.getFitness() < best.getFitness()) {
////                best = tmp;
////            }
////            Current = tmp;
////            System.out.println(Current.getFitness());
////        }
        // END
        best.setID(Integer.parseInt(this.getName()));
//        best.PrintPront(this.getName(), rows, cols);
        best.PrintFile_Fitness(this.getName(), better);
        synchronized (Menu.nodos) {
            Menu.nodos.add(best);
        }

//        best.PrintFile(this.getName(), this.rows, this.cols, this.file);
//        System.out.println(best_idx);
//        return best;
    }

    /**
     * METODO QUE CALCULA LE VALOR DE LA FASE A PARTIR DEL VECTOR DE
     * COEFICIENTES
     */
    private double[] Delta(int franjas, int orden_polinomio, double xmax, double ymax) {
        int num_Coef = (orden_polinomio + 1) * (orden_polinomio + 2) / 2;
        double[] delta = new double[num_Coef];
        int k, exp, c = 0;
        delta[0] = 2 * Math.PI * franjas;

        int grado = (int) (-3 + Math.sqrt(9 + 8 * (num_Coef - 1))) / 2;
        for (int i = 1; i <= grado; i++) {
            k = 0;
            for (int j = i + c; j <= 2 * i + c; j++) {
                exp = (i + k + 2) % (i + 1);
                delta[j] = 2 * Math.PI * franjas / (Math.pow(xmax, exp) * Math.pow(ymax, i - exp));
                k++;
            }
            c += i;
        }
        return delta;
    }

    /**
     * * METODOS QUE CALCULA LAS FRANJAS DE LOS INTERFEROGRAMAS ***
     */
    public synchronized int Franges_Hor(int[][] img) {
        int max = 0;
        for (int i = 0; i < img.length; i++) {
            double current = img[i][0];
            int contador = 0;
            if (current == 1) {
                contador = 2;
            }
            for (int j = 1; j < img[0].length; j++) {
                if (current != img[i][j]) {
                    contador++;
                    current = img[i][j];
                }
            }
            if (max < contador / 2) {
                max = contador / 2;
            }
        }
        return max;
    }

    public synchronized int Franges_Ver(int[][] img) {
        int max = 0;
        for (int i = 0; i < img[0].length; i++) {
            double current = img[0][i];
            int contador = 0;
            if (current == 1) {
                contador = 2;
            }
            for (int j = 1; j < img.length; j++) {
                if (current != img[j][i]) {
                    contador++;
                    current = img[j][i];
                }
            }
            if (max < contador / 2) {
                max = contador / 2;
            }
        }
        return max;
    }

    public synchronized int Franges_Diagonal(int[][] img) {
        int longitud;
        if (img.length < img[0].length) {
            longitud = img.length;
        } else {
            longitud = img[0].length;
        }
        double current = img[0][0];
        int contador = 0;
        if (current == 1) {
            contador = 2;
        }

        for (int i = 1; i < longitud; i++) {
            if (current != img[i][i]) {
                contador++;
                current = img[i][i];
            }
        }
        return contador / 2;
    }

    public synchronized int Franges_Diagonal_Inversa(int[][] img) {
        int longitud;
        if (img.length < img[0].length) {
            longitud = img.length;
        } else {
            longitud = img[0].length;
        }
        double current = img[0][0];
        int contador = 0;
        if (current == 1) {
            contador = 2;
        }

        for (int i = longitud - 1; i >= 0; i--) {
            if (current != img[i][i]) {
                contador++;
                current = img[i][i];
            }
        }
        return contador / 2;
    }

    /**
     * *END FRANJAS***
     */
    public int[][] BW(double[][] img) {
        int row = img.length;
        int col = img[0].length;
        int[][] bw = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (img[i][j] >= 128) {
                    bw[i][j] = 1;
                } else {
                    bw[i][j] = 0;
                }
            }
        }

        return bw;
    }
}
