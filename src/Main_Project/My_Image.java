package Main_Project;

import com.fathzer.soft.javaluator.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Iosvani
 */
public class My_Image implements Cloneable {

    private BufferedImage image;
    private int[][] matrix;
    private double[][] phase;

    // Datos para ZOOM funcion simulada
    private String function;
    private double Xmin;
    private double Xmax;
    private double Ymin;
    private double Ymax;

    public My_Image(BufferedImage img, int[][] matriz, double[][] phase) {
        this.image = img;
        this.matrix = matriz;
        this.phase = phase;
        Update();
    }

    public My_Image(BufferedImage img, int[][] matriz) {
        this.image = img;
        this.matrix = matriz;
    }

    public My_Image(double[][] matriz, int RGB) {
        this.image = new BufferedImage(matriz[0].length, matriz.length, BufferedImage.TYPE_INT_RGB);
//        this.matrix = (int[][])matriz;
        UpdateRGB(matriz);
    }

    public My_Image(double[][] matriz) {
        this.image = new BufferedImage(matriz[0].length, matriz.length, BufferedImage.TYPE_BYTE_GRAY);
        Update(matriz);
    }

    public My_Image(double[][] matriz, double[][] phase) {
        this.image = new BufferedImage(matriz[0].length, matriz.length, BufferedImage.TYPE_BYTE_GRAY);
        this.phase = phase;
        Update(matriz);
    }

    public My_Image(BufferedImage img) {
        this.image = img;

        matrix = new int[image.getHeight()][image.getWidth()];
        Color colorAux;
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                //Almacenamos el color del píxel
                colorAux = new Color(image.getRGB(j, i));
                //Calculamos la media de los tres canales (rojo, verde, azul)
                matrix[i][j] = (int) ((colorAux.getRed() + colorAux.getGreen() + colorAux.getBlue()) / 3);
            }
        }
    }

    public My_Image(String function, double Xmin, double Xmax, double Ymin, double Ymax, int raw, int col) {
        this.function = function;
        this.Xmin = Xmin;
        this.Xmax = Xmax;
        this.Ymin = Ymin;
        this.Ymax = Ymax;

        double Dx = (Xmax - Xmin) / col;
        double Dy = (Ymax - Ymin) / raw;

        matrix = new int[raw][col];
        phase = new double[raw][col];
        double[][] matemp = new double[raw][col];

        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double x, y;

        final DoubleEvaluator eval = new DoubleEvaluator();
        final StaticVariableSet<Double> variables = new StaticVariableSet<>();
        function = function.replaceAll("X", "x");
        function = function.replaceAll("Y", "y");
        String functionTemp = null;

        for (int i = 0; i < raw; i++) {
            y = Ymin + Dy * (i);
            functionTemp = function.replaceAll("y", "(" + String.valueOf(y) + ")");
            for (int j = 0; j < col; j++) {
                x = Xmin + Dx * (j);
                variables.set("x", x);
//                double result = eval.evaluate(functionTemp, variables);
                phase[i][j] = eval.evaluate(functionTemp, variables);
                matemp[i][j] = 128 - 127 * Math.cos(phase[i][j]);
//                matrix[i][j] = Math.round((float)(128 - 127 * Math.cos(result))); 
                if (matemp[i][j] < min) { // buscar el minimo
                    min = matemp[i][j];
                }
                if (matemp[i][j] > max) { // buscar el minimo
                    max = matemp[i][j];
                }
            }
        }
        this.image = new BufferedImage(col, raw, BufferedImage.TYPE_BYTE_GRAY);

        Update(Normalizar_Matrix(min, max, matemp));
    }

    public My_Image(File dir) {
        try {
            image = ImageIO.read(dir);
            matrix = new int[image.getHeight()][image.getWidth()];

            Color colorAux;
            for (int i = 0; i < image.getHeight(); i++) {
                for (int j = 0; j < image.getWidth(); j++) {
                    //Almacenamos el color del píxel
                    colorAux = new Color(image.getRGB(j, i));
                    //Calculamos la media de los tres canales (rojo, verde, azul)
                    matrix[i][j] = (int) ((colorAux.getRed() + colorAux.getGreen() + colorAux.getBlue()) / 3);
                }
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
//            Logger.getLogger(My_Image.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    
    public Object clone() {
        My_Image obj = null;
        try {
            obj = (My_Image) super.clone();

            if (function != null) {
                double[][] phaseClone = new double[phase.length][phase[0].length];
                for (int i = 0; i < phase.length; i++) {
                    for (int j = 0; j < phase[0].length; j++) {
                        phaseClone[i][j] = phase[i][j];
                    }
                }
                obj.setPhase(phaseClone);
            }

        } catch (CloneNotSupportedException ex) {
            System.out.println(" no se puede duplicar");
        }
//        obj.phase=(double[][])obj.phase.clone();

        return obj;
    }

    public double[][] Normalizar_Matrix(double min, double max, double[][] matrixTemp) {
        for (int i = 0; i < matrixTemp.length; i++) {
            for (int j = 0; j < matrixTemp[0].length; j++) {
                matrixTemp[i][j] = 255 * (matrixTemp[i][j] - min) / (max - min);
            }
        }
        return matrixTemp;
    }

    public void UpdateRGB(double[][] maTemp) {
        int colorSRGB;
        this.matrix = new int[maTemp.length][maTemp[0].length];
        //Recorremos la imagen píxel a píxel
        for (int i = 0; i < this.image.getHeight(); i++) {
            for (int j = 0; j < this.image.getWidth(); j++) {
                this.matrix[i][j] = (int) maTemp[i][j];
                if (matrix[i][j] < 0) {
                    this.image.setRGB(j, i, new Color(255, 0, 0).getRGB());
                } else {
                    colorSRGB = (this.matrix[i][j] << 16) | (this.matrix[i][j] << 8) | this.matrix[i][j];
                    //Asignamos el nuevo valor al BufferedImage
                    this.image.setRGB(j, i, colorSRGB);
                }
            }
        }
    }

    public BufferedImage Get_Image_Phase() {
        BufferedImage img_phase = new BufferedImage(phase[0].length, phase.length, BufferedImage.TYPE_BYTE_GRAY);
        int colorSRGB;
        for (int i = 0; i < img_phase.getHeight(); i++) {
            for (int j = 0; j < img_phase.getWidth(); j++) {
                colorSRGB = ((int) this.phase[i][j] << 16) | ((int) this.phase[i][j] << 8) | (int) this.phase[i][j];
                //Asignamos el nuevo valor al BufferedImage
                img_phase.setRGB(j, i, colorSRGB);
            }
        }
        return img_phase;
    }

    public void Update(double[][] maTemp) {
        int colorSRGB;
        this.matrix = new int[maTemp.length][maTemp[0].length];
        //Recorremos la imagen píxel a píxel
        for (int i = 0; i < this.image.getHeight(); i++) {
            for (int j = 0; j < this.image.getWidth(); j++) {
                this.matrix[i][j] = (int) maTemp[i][j];
                colorSRGB = (this.matrix[i][j] << 16) | (this.matrix[i][j] << 8) | this.matrix[i][j];
                //Asignamos el nuevo valor al BufferedImage
                this.image.setRGB(j, i, colorSRGB);
            }
        }
    }

    public void Update() {
        int colorSRGB;

        //Recorremos la imagen píxel a píxel
        for (int i = 0; i < this.image.getHeight(); i++) {
            for (int j = 0; j < this.image.getWidth(); j++) {
                colorSRGB = (this.matrix[i][j] << 16) | (this.matrix[i][j] << 8) | this.matrix[i][j];
                //Asignamos el nuevo valor al BufferedImage
                this.image.setRGB(j, i, colorSRGB);
            }
        }
    }

    // Metodo que devuelve una imagen con una nueva resolucion.
    public My_Image Escalar(int raw, int col) {

        double Dx = (Xmax - Xmin) / col;
        double Dy = (Ymax - Ymin) / raw;

        double[][] phaseZoom = new double[raw][col];
        double[][] matemp = new double[raw][col];

        double x, y;

        final DoubleEvaluator eval = new DoubleEvaluator();
        final StaticVariableSet<Double> variables = new StaticVariableSet<>();
        function = function.replaceAll("X", "x");
        function = function.replaceAll("Y", "y");
        String functionTemp = null;

        for (int i = 0; i < raw; i++) {
            y = Ymin + Dy * (i);
            functionTemp = function.replaceAll("y", "(" + String.valueOf(y) + ")");
            for (int j = 0; j < col; j++) {
                x = Xmin + Dx * (j);
                variables.set("x", x);
//                double result = eval.evaluate(functionTemp, variables);
                phaseZoom[i][j] = eval.evaluate(functionTemp, variables);
                matemp[i][j] = 128 - 127 * Math.cos(phaseZoom[i][j]);
//                matrix[i][j] = Math.round((float)(128 - 127 * Math.cos(result))); 
//                if (matemp[i][j] < min) { // buscar el minimo
//                    min = matemp[i][j];
//                }
//                if (matemp[i][j] > max) { // buscar el minimo
//                    max = matemp[i][j];
//                }
            }
        }
//        this.image = new BufferedImage(col, raw, BufferedImage.TYPE_BYTE_GRAY);
//
//        Update(Normalizar_Matrix(min, max, matemp));
        return new My_Image(matemp, phaseZoom);
    }

    /**
     * @return the image
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * @return the matrix
     */
    public int[][] getMatrix() {
        return matrix;
    }

    public double[][] getMatrixDouble() {
        double[][] result = new double[matrix.length][matrix[0].length];

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                result[i][j] = (double) matrix[i][j];
            }
        }
        return result;
    }

    /**
     * @return the phase
     */
    public double[][] getPhase() {
        return phase;
    }

    /**
     * @param phase the phase to set
     */
    public void setPhase(double[][] phase) {
        this.phase = phase;
    }

}
