/*
 * Auxiliar.java
 *
 * Created on 15 de marzo de 2008, 11:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package simulating_analing_paralell;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Amilkar Yudier Puris Caceres
 */
public class Auxiliar {

    /**
     * Calcula un numero aleatorio entre "min" (incluido) y "max" (incluido).
     */
    public static double randomValue_Generate(double minValue, double maxValue) {
        return minValue + randomValue_Generate(maxValue - minValue);
    }

    /**
     * Calcula un numero aleatorio entre 0 (incluido) y el "max" (incluido).
     */
    public static double randomValue_Generate(double maxValue) {
//        Random rand = new Random(System.currentTimeMillis());
        return Math.random() * maxValue;
//        return rand.nextDouble() * maxValue;
    }

    /**
     * Calcula un numero aleatorio entre 0 (incluido) y el "max" (incluido).
     */
    public static int randomInt(int maxValue) {
//        Random rand = new Random(System.currentTimeMillis());
        return (int) (Math.random() * (maxValue + 1));
//        return rand.nextInt(maxValue + 1);
    }

    /**
     * Calcula el numero de franjas de una ventana
     */
    public static int Calcular_Franjas(double[][] image) {
        int[][] temp_matrix = BW(image);
        int h = Franges_Hor(temp_matrix);
        int v = Franges_Ver(temp_matrix);
        int d = Franges_Diagonal(temp_matrix);
        int d1 = Franges_Diagonal_Inversa(temp_matrix);
        int num_franjas;
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
        return num_franjas;
    }

    /**
     * * METODOS QUE CALCULA LAS FRANJAS DE LOS INTERFEROGRAMAS ***
     */
    public static int Franges_Hor(int[][] img) {
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

    public static double Min(double[][] matrix) {
        double min = Double.MAX_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (min > matrix[i][j]) {
                    min = matrix[i][j];
                }
            }
        }
        return min;
    }
    public static double Max(double[][] matrix) {
        double max = Double.MIN_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (max < matrix[i][j]) {
                    max = matrix[i][j];
                }
            }
        }
        return max;
    }

    public static int Franges_Ver(int[][] img) {
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

    public static int Franges_Diagonal(int[][] img) {
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

    public static int Franges_Diagonal_Inversa(int[][] img) {
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
    public static int[][] BW(double[][] img) {
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
