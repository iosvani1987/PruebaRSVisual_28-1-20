/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main_Project;

import Exceptions.ArbolVacioException;
import Exceptions.NodoNoEncontrado;
import arbolg.Arbolphsh;

import arbolg.Nodo;
import arbolg.WindowsFringes;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import simulating_analing_paralell.Algorithm_Simulate_Analing;
import simulating_analing_paralell.Auxiliar;
import simulating_analing_paralell.Solution_Node;
import slider.RangeSlider;
import javax.swing.*;
import Spline.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.FileReader;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.analysis.interpolation.AkimaSplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.TricubicInterpolator;
import org.apache.commons.math3.analysis.interpolation.TricubicSplineInterpolatingFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.jfree.chart.ChartPanel;
import org.math.array.DoubleArray;
import org.math.plot.Plot3DPanel;

/**
 *
 * @author CIO
 */
public class Menu extends javax.swing.JFrame {

    private RangeSlider rangeSliderX = new RangeSlider();
    private RangeSlider rangeSliderY = new RangeSlider();
    private My_Image Image_original;
    private My_Image Original_Simulada_Zoom;
    private My_Image Image_partitioned;
    private My_Image Image_recuperada;
    private My_Image Image_modificada;
    private boolean flag_Simulacion;
    private boolean flag_zoom = false;
    private Arbolphsh<WindowsFringes> tree;
    public static int Num_hojas;
    private ArrayList<Integer> name_nodos = new ArrayList<Integer>();
    private boolean image_flag_particionada;
    private boolean Select_Simulation_openFile;
    public static ArrayList<Solution_Node> nodos;
    public static ArrayList<Algorithm_Simulate_Analing> hilos;
    private double[] Temperature;

    DialogModal dlg;

    /**
     * Creates new form Menu
     */
    public Menu() {
        initComponents();
        this.setLocationRelativeTo(null);

        // Desabilitar los paneles adicionales
        Main_TPanel.setEnabledAt(1, false);
        Main_TPanel.setEnabledAt(2, false);
        Main_TPanel.setEnabledAt(3, false);

//        simulation_panel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
//        simulation_panel.setLayout(new GridBagLayout());
//        rangeSliderValue1.setHorizontalAlignment(JLabel.LEFT);
//        rangeSliderValue2.setHorizontalAlignment(JLabel.LEFT);
        // RangerSlider
//        RangeSlider rangeSlider = new RangeSlider();
        this.setResizable(false);
        jLabel_x_min.setText("X min: -2");
        jLabel_x_max.setText("X max: 2");
        rangeSliderX.setPreferredSize(new Dimension(240, rangeSliderX.getPreferredSize().height));
        rangeSliderX.setMinimum(-15);
        rangeSliderX.setMaximum(15);
        // Initialize values.
        rangeSliderX.setValue(-2);
        rangeSliderX.setUpperValue(2);
        rangeSliderX.setBounds(15, 100, 150, 50);
        rangeSliderX.setMajorTickSpacing(10);
        rangeSliderX.setMinorTickSpacing(1);
        rangeSliderX.setPaintTicks(true);
        rangeSliderX.setPaintLabels(true);
        // RangerSlider Y
        jLabel_y_min.setText("X min: -2");
        jLabel_y_max.setText("X max: 2");
        rangeSliderY.setPreferredSize(new Dimension(240, rangeSliderX.getPreferredSize().height));
        rangeSliderY.setMinimum(-15);
        rangeSliderY.setMaximum(15);
        // Initialize values.
        rangeSliderY.setValue(-2);
        rangeSliderY.setUpperValue(2);
        rangeSliderY.setBounds(200, 100, 150, 50);
        rangeSliderY.setMajorTickSpacing(10);
        rangeSliderY.setMinorTickSpacing(1);
        rangeSliderY.setPaintTicks(true);
        rangeSliderY.setPaintLabels(true);

        // Colocar el boton de Simular
        btn_simular.setBounds(250, 20, 100, 30);
        jPanel_btn_simular.add(btn_simular);

        // Initialize value display.
//        rangeSliderValue1.setText(String.valueOf(rangeSlider.getValue()));
//        rangeSliderValue2.setText(String.valueOf(rangeSlider.getUpperValue()));
        // Add listener to update display X Slider
        rangeSliderX.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                RangeSlider slider = (RangeSlider) e.getSource();
                jLabel_x_min.setText("X min: " + String.valueOf(slider.getValue()));
                jLabel_x_max.setText("X max: " + String.valueOf(slider.getUpperValue()));
            }
        });

        // Add listener to update display Y Slider
        rangeSliderY.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                RangeSlider slider = (RangeSlider) e.getSource();
                jLabel_y_min.setText("Y min: " + String.valueOf(slider.getValue()));
                jLabel_y_max.setText("Y max: " + String.valueOf(slider.getUpperValue()));
            }
        });
        jPanel_Simulacion.add(rangeSliderX);
        jPanel_Simulacion.add(rangeSliderY);
// Visibilidad del TextField de Vecindad.
//        text_factorV.setEnabled(false);

//        simulation_panel.add(rangeSliderLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
//            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 3, 3), 0, 0));
//        simulation_panel.add(rangeSliderValue1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
//            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 3, 0), 0, 0));
//        simulation_panel.add(rangeSliderLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
//            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 3, 3), 0, 0));
//        simulation_panel.add(rangeSliderValue2, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
//            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 6, 0), 0, 0));
//        simulation_panel.add(rangeSlider      , new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0,
//            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        vecindad_group = new javax.swing.ButtonGroup();
        Main_TPanel = new javax.swing.JTabbedPane();
        inicio_panel = new javax.swing.JPanel();
        jPanel_Inicio_Sup = new javax.swing.JPanel();
        cio_logo = new javax.swing.JLabel();
        jPanel_Centro = new javax.swing.JPanel();
        jPanel_Centro_left = new javax.swing.JPanel();
        jPanel_Simulacion = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField_funcion = new javax.swing.JTextField();
        jLabel_x_min = new javax.swing.JLabel();
        jLabel_x_max = new javax.swing.JLabel();
        jLabel_y_min = new javax.swing.JLabel();
        jLabel_y_max = new javax.swing.JLabel();
        jPanel_Imagen = new javax.swing.JPanel();
        jPanel_btn_simular = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTextField_Fila = new javax.swing.JTextField();
        jTextField_Columna = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        btn_simular = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jButton_open_file = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        text_Franjas = new javax.swing.JTextField();
        btn_aplicar_windows_fringes = new javax.swing.JButton();
        btn_aceptar_windows_fringes = new javax.swing.JButton();
        jPanel_Centro_right = new javax.swing.JPanel();
        jLabel_main_name = new javax.swing.JLabel();
        jLabel_Image = new javax.swing.JLabel();
        jPanel_inicio_Inferior = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        rs_panel = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jComboBox_Vecindad = new javax.swing.JComboBox();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        text_factorV_fijo = new javax.swing.JTextField();
        text_factorV_Inicial = new javax.swing.JTextField();
        text_factorV_Final = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jSpinner_Kvecinos = new javax.swing.JSpinner();
        jComboBox_2_corrida = new javax.swing.JComboBox();
        jLabel25 = new javax.swing.JLabel();
        label_rs_imagen_original = new javax.swing.JLabel();
        rs_ejecutar = new javax.swing.JButton();
        jPanel_Parametros = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        text_iteraciones = new javax.swing.JTextField();
        text_generaciones = new javax.swing.JTextField();
        jLabel_Generaciones = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        text_poly_orden = new javax.swing.JTextField();
        text_suavidad = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        text_factor_delta = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        text_Tolerancia = new javax.swing.JTextField();
        jPanel_Temperatura = new javax.swing.JPanel();
        jComboBox_Temperature = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jLabel_Tf = new javax.swing.JLabel();
        jLabel_Tf1 = new javax.swing.JLabel();
        jLabel_Tf2 = new javax.swing.JLabel();
        text_temperatura = new javax.swing.JTextField();
        text_temperatura_final = new javax.swing.JTextField();
        text_K = new javax.swing.JTextField();
        text_alpha = new javax.swing.JTextField();
        btn_simular_temperatura = new javax.swing.JButton();
        jPanel_Temperature = new javax.swing.JPanel();
        jLabel_grafica_temperature1 = new javax.swing.JLabel();
        jPanel_resultados = new javax.swing.JPanel();
        jLabel_image_recuperada = new javax.swing.JLabel();
        jInternalFrame_fase = new javax.swing.JInternalFrame();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea_Salida = new javax.swing.JTextArea();
        jLabel_salida = new javax.swing.JLabel();
        jPanel_PDI = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        text_resolution_scsale = new javax.swing.JTextField();
        jComboBox_Filtros = new javax.swing.JComboBox();
        rs_filtros1 = new javax.swing.JButton();
        rs_Restaurar = new javax.swing.JButton();
        rs_filtros3 = new javax.swing.JButton();
        jComboBox_Fase1 = new javax.swing.JComboBox();
        jPanel_estadisticas = new javax.swing.JPanel();
        jComboBox_Windows = new javax.swing.JComboBox();
        jPanel_grafica_fitness = new javax.swing.JPanel();
        jLabel_grafica_fitness = new javax.swing.JLabel();
        btn_graficar_fitness = new javax.swing.JButton();
        jSpinner_max = new javax.swing.JSpinner();
        jSpinner_Min = new javax.swing.JSpinner();
        jLabel18 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        btn_anadir = new javax.swing.JButton();
        btn_run_indep = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        vmo_panel = new javax.swing.JPanel();
        ag_panel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocation(new java.awt.Point(300, 50));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Main_TPanel.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N

        inicio_panel.setFont(new java.awt.Font("Times New Roman", 2, 11)); // NOI18N
        inicio_panel.setLayout(new javax.swing.BoxLayout(inicio_panel, javax.swing.BoxLayout.Y_AXIS));

        jPanel_Inicio_Sup.setBackground(new java.awt.Color(255, 204, 204));
        jPanel_Inicio_Sup.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 4));

        cio_logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/logo_CIO1.png"))); // NOI18N
        jPanel_Inicio_Sup.add(cio_logo);

        inicio_panel.add(jPanel_Inicio_Sup);

        jPanel_Centro.setBackground(new java.awt.Color(0, 102, 102));
        jPanel_Centro.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 10));

        jPanel_Centro_left.setLayout(new javax.swing.BoxLayout(jPanel_Centro_left, javax.swing.BoxLayout.Y_AXIS));

        jPanel_Simulacion.setBackground(new java.awt.Color(204, 204, 255));
        jPanel_Simulacion.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Simulacion", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Times New Roman", 3, 12))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel2.setText("Funcion");

        jTextField_funcion.setFont(new java.awt.Font("Times New Roman", 2, 10)); // NOI18N
        jTextField_funcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_funcionActionPerformed(evt);
            }
        });

        jLabel_x_min.setFont(new java.awt.Font("Times New Roman", 3, 12)); // NOI18N
        jLabel_x_min.setText("X min:");

        jLabel_x_max.setFont(new java.awt.Font("Times New Roman", 3, 12)); // NOI18N
        jLabel_x_max.setText("X max:");

        jLabel_y_min.setFont(new java.awt.Font("Times New Roman", 3, 12)); // NOI18N
        jLabel_y_min.setText("Y min:");

        jLabel_y_max.setFont(new java.awt.Font("Times New Roman", 3, 12)); // NOI18N
        jLabel_y_max.setText("Y max:");

        javax.swing.GroupLayout jPanel_SimulacionLayout = new javax.swing.GroupLayout(jPanel_Simulacion);
        jPanel_Simulacion.setLayout(jPanel_SimulacionLayout);
        jPanel_SimulacionLayout.setHorizontalGroup(
            jPanel_SimulacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_SimulacionLayout.createSequentialGroup()
                .addGap(143, 143, 143)
                .addComponent(jLabel2)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel_SimulacionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_SimulacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_SimulacionLayout.createSequentialGroup()
                        .addComponent(jLabel_x_min)
                        .addGap(37, 37, 37)
                        .addComponent(jLabel_x_max)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel_y_min)
                        .addGap(39, 39, 39)
                        .addComponent(jLabel_y_max))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_SimulacionLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jTextField_funcion, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel_SimulacionLayout.setVerticalGroup(
            jPanel_SimulacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_SimulacionLayout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(4, 4, 4)
                .addComponent(jTextField_funcion, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel_SimulacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_SimulacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel_y_min)
                        .addComponent(jLabel_y_max))
                    .addGroup(jPanel_SimulacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel_x_min)
                        .addComponent(jLabel_x_max)))
                .addGap(0, 54, Short.MAX_VALUE))
        );

        jPanel_Centro_left.add(jPanel_Simulacion);

        jPanel_btn_simular.setBackground(new java.awt.Color(204, 204, 255));
        jPanel_btn_simular.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 12))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Times New Roman", 3, 12)); // NOI18N
        jLabel3.setText("Fila");

        jTextField_Fila.setFont(new java.awt.Font("Times New Roman", 3, 12)); // NOI18N
        jTextField_Fila.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_FilaActionPerformed(evt);
            }
        });

        jTextField_Columna.setFont(new java.awt.Font("Times New Roman", 3, 12)); // NOI18N
        jTextField_Columna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_ColumnaActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Times New Roman", 3, 12)); // NOI18N
        jLabel4.setText("Columna");

        btn_simular.setBackground(new java.awt.Color(204, 204, 255));
        btn_simular.setFont(new java.awt.Font("Times New Roman", 3, 12)); // NOI18N
        btn_simular.setText("Simular");
        btn_simular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_simularActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_btn_simularLayout = new javax.swing.GroupLayout(jPanel_btn_simular);
        jPanel_btn_simular.setLayout(jPanel_btn_simularLayout);
        jPanel_btn_simularLayout.setHorizontalGroup(
            jPanel_btn_simularLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_btn_simularLayout.createSequentialGroup()
                .addGroup(jPanel_btn_simularLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_btn_simularLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jTextField_Fila, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel_btn_simularLayout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jLabel3)))
                .addGap(42, 42, 42)
                .addGroup(jPanel_btn_simularLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField_Columna, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_btn_simularLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(8, 8, 8)))
                .addGap(39, 39, 39)
                .addComponent(btn_simular, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel_btn_simularLayout.setVerticalGroup(
            jPanel_btn_simularLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_btn_simularLayout.createSequentialGroup()
                .addGroup(jPanel_btn_simularLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel_btn_simularLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField_Fila, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_Columna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_simular, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 7, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Abrir Fichero", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 12))); // NOI18N

        jButton_open_file.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Open-file-icon.png"))); // NOI18N
        jButton_open_file.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_open_fileActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dividir en Ventanas", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 12))); // NOI18N

        jLabel5.setFont(new java.awt.Font("Times New Roman", 3, 12)); // NOI18N
        jLabel5.setText("Franjas");

        text_Franjas.setFont(new java.awt.Font("Times New Roman", 3, 12)); // NOI18N
        text_Franjas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                text_FranjasKeyTyped(evt);
            }
        });

        btn_aplicar_windows_fringes.setBackground(new java.awt.Color(204, 204, 255));
        btn_aplicar_windows_fringes.setFont(new java.awt.Font("Times New Roman", 3, 12)); // NOI18N
        btn_aplicar_windows_fringes.setText("Aplicar");
        btn_aplicar_windows_fringes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_aplicar_windows_fringesActionPerformed(evt);
            }
        });

        btn_aceptar_windows_fringes.setBackground(new java.awt.Color(204, 204, 255));
        btn_aceptar_windows_fringes.setFont(new java.awt.Font("Times New Roman", 3, 12)); // NOI18N
        btn_aceptar_windows_fringes.setText("Aceptar");
        btn_aceptar_windows_fringes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_aceptar_windows_fringesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(text_Franjas, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 41, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_aplicar_windows_fringes, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_aceptar_windows_fringes, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(text_Franjas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(btn_aplicar_windows_fringes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_aceptar_windows_fringes))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jButton_open_file, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton_open_file)
                        .addContainerGap(26, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel_ImagenLayout = new javax.swing.GroupLayout(jPanel_Imagen);
        jPanel_Imagen.setLayout(jPanel_ImagenLayout);
        jPanel_ImagenLayout.setHorizontalGroup(
            jPanel_ImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_btn_simular, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel_ImagenLayout.setVerticalGroup(
            jPanel_ImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_ImagenLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jPanel_btn_simular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel_Centro_left.add(jPanel_Imagen);

        jPanel_Centro.add(jPanel_Centro_left);

        jLabel_main_name.setFont(new java.awt.Font("Times New Roman", 3, 12)); // NOI18N
        jLabel_main_name.setText("Imagen");

        jLabel_Image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Coma.png"))); // NOI18N

        javax.swing.GroupLayout jPanel_Centro_rightLayout = new javax.swing.GroupLayout(jPanel_Centro_right);
        jPanel_Centro_right.setLayout(jPanel_Centro_rightLayout);
        jPanel_Centro_rightLayout.setHorizontalGroup(
            jPanel_Centro_rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_Centro_rightLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel_Image, javax.swing.GroupLayout.PREFERRED_SIZE, 320, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_Centro_rightLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel_main_name, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(143, 143, 143))
        );
        jPanel_Centro_rightLayout.setVerticalGroup(
            jPanel_Centro_rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_Centro_rightLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel_main_name, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel_Image, javax.swing.GroupLayout.PREFERRED_SIZE, 352, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel_Centro.add(jPanel_Centro_right);

        inicio_panel.add(jPanel_Centro);

        jPanel_inicio_Inferior.setBackground(new java.awt.Color(255, 204, 204));
        jPanel_inicio_Inferior.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 7, 7));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        jLabel1.setText("Derchos reservados imore@cio.mx 21-6-19");
        jPanel_inicio_Inferior.add(jLabel1);

        inicio_panel.add(jPanel_inicio_Inferior);

        Main_TPanel.addTab("Inicio", inicio_panel);

        rs_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel17.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel17.setText("Imagen Original");
        rs_panel.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 0, 100, -1));

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "Búsqueda Golosa", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 14))); // NOI18N

        jLabel11.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel11.setText("Método");

        jComboBox_Vecindad.setFont(new java.awt.Font("Times New Roman", 2, 12)); // NOI18N
        jComboBox_Vecindad.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Lineal 1", "Lineal 2", "Boltzman 1", "Boltzman 2", "Fijo" }));
        jComboBox_Vecindad.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_VecindadItemStateChanged(evt);
            }
        });
        jComboBox_Vecindad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_VecindadActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel19.setText("Fv Fijo:");

        jLabel20.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel20.setText("Fv Inicial:");

        jLabel21.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel21.setText("Fv Final:");

        text_factorV_fijo.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        text_factorV_fijo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        text_factorV_fijo.setText("0.2");
        text_factorV_fijo.setEnabled(false);
        text_factorV_fijo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_factorV_fijoActionPerformed(evt);
            }
        });

        text_factorV_Inicial.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        text_factorV_Inicial.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        text_factorV_Inicial.setText("0.5");
        text_factorV_Inicial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_factorV_InicialActionPerformed(evt);
            }
        });

        text_factorV_Final.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        text_factorV_Final.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        text_factorV_Final.setText("0.1");
        text_factorV_Final.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_factorV_FinalActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel24.setText("K-vecinos");

        jSpinner_Kvecinos.setModel(new javax.swing.SpinnerNumberModel(6, 1, 200, 1));

        jComboBox_2_corrida.setFont(new java.awt.Font("Times New Roman", 2, 12)); // NOI18N
        jComboBox_2_corrida.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Default", "Polinomio", "Delta", " " }));
        jComboBox_2_corrida.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_2_corridaItemStateChanged(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel25.setText("2 corrida");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(text_factorV_Final, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 47, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(text_factorV_Inicial, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                            .addComponent(text_factorV_fijo)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jComboBox_Vecindad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jSpinner_Kvecinos, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jComboBox_2_corrida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinner_Kvecinos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox_Vecindad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(text_factorV_fijo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(text_factorV_Inicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                    .addComponent(text_factorV_Final, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox_2_corrida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18))
        );

        rs_panel.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 0, 190, 210));

        label_rs_imagen_original.setText(".");
        label_rs_imagen_original.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        rs_panel.add(label_rs_imagen_original, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 20, 210, 190));

        rs_ejecutar.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        rs_ejecutar.setText("Ejecutar");
        rs_ejecutar.setEnabled(false);
        rs_ejecutar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rs_ejecutarActionPerformed(evt);
            }
        });
        rs_panel.add(rs_ejecutar, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 220, -1, -1));

        jPanel_Parametros.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "Parámetros", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 14))); // NOI18N
        jPanel_Parametros.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel7.setText("Iteraciones:");
        jPanel_Parametros.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, 20));

        text_iteraciones.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        text_iteraciones.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        text_iteraciones.setText("200");
        text_iteraciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_iteracionesActionPerformed(evt);
            }
        });
        jPanel_Parametros.add(text_iteraciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 40, -1));

        text_generaciones.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        text_generaciones.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        text_generaciones.setText("50");
        text_generaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_generacionesActionPerformed(evt);
            }
        });
        jPanel_Parametros.add(text_generaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, 40, -1));

        jLabel_Generaciones.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel_Generaciones.setText("Vecinos:");
        jPanel_Parametros.add(jLabel_Generaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 60, 20));

        jLabel12.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel12.setText("Polinomio:");
        jPanel_Parametros.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, 20));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel10.setText("Suavidad:");
        jPanel_Parametros.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 63, 20));

        text_poly_orden.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        text_poly_orden.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        text_poly_orden.setText("2");
        text_poly_orden.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                text_poly_ordenFocusLost(evt);
            }
        });
        text_poly_orden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_poly_ordenActionPerformed(evt);
            }
        });
        jPanel_Parametros.add(text_poly_orden, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, 40, -1));

        text_suavidad.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        text_suavidad.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        text_suavidad.setText("5");
        text_suavidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_suavidadActionPerformed(evt);
            }
        });
        jPanel_Parametros.add(text_suavidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 110, 40, -1));

        jLabel14.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel14.setText("Delta:");
        jPanel_Parametros.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 40, 20));

        text_factor_delta.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        text_factor_delta.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        text_factor_delta.setText("2");
        text_factor_delta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_factor_deltaActionPerformed(evt);
            }
        });
        jPanel_Parametros.add(text_factor_delta, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, 40, -1));

        jLabel13.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel13.setText("Error:");
        jPanel_Parametros.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 40, 20));

        text_Tolerancia.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        text_Tolerancia.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        text_Tolerancia.setText("300");
        text_Tolerancia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_ToleranciaActionPerformed(evt);
            }
        });
        jPanel_Parametros.add(text_Tolerancia, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 170, 40, -1));

        rs_panel.add(jPanel_Parametros, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 140, 210));

        jPanel_Temperatura.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "Temperatura", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 14))); // NOI18N
        jPanel_Temperatura.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jComboBox_Temperature.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jComboBox_Temperature.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Exponencial", "Geométrica", "Toledo&Cuevas", "Lundy&Mees" }));
        jComboBox_Temperature.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_TemperatureItemStateChanged(evt);
            }
        });
        jComboBox_Temperature.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_TemperatureActionPerformed(evt);
            }
        });
        jPanel_Temperatura.add(jComboBox_Temperature, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 100, -1));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel9.setText("To:");
        jPanel_Temperatura.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 30, 20));

        jLabel_Tf.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel_Tf.setText("Tf:");
        jPanel_Temperatura.add(jLabel_Tf, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, 20));

        jLabel_Tf1.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel_Tf1.setText("K");
        jPanel_Temperatura.add(jLabel_Tf1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 20, 20));

        jLabel_Tf2.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel_Tf2.setText("α");
        jPanel_Temperatura.add(jLabel_Tf2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 20, 20));

        text_temperatura.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        text_temperatura.setText("9000");
        text_temperatura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_temperaturaActionPerformed(evt);
            }
        });
        jPanel_Temperatura.add(text_temperatura, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 40, -1));

        text_temperatura_final.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        text_temperatura_final.setText("2");
        text_temperatura_final.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_temperatura_finalActionPerformed(evt);
            }
        });
        jPanel_Temperatura.add(text_temperatura_final, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 80, 40, -1));

        text_K.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        text_K.setText("150");
        text_K.setEnabled(false);
        text_K.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_KActionPerformed(evt);
            }
        });
        jPanel_Temperatura.add(text_K, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, 40, -1));

        text_alpha.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        text_alpha.setText("0.9");
        text_alpha.setEnabled(false);
        text_alpha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_alphaActionPerformed(evt);
            }
        });
        jPanel_Temperatura.add(text_alpha, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 140, 40, -1));

        btn_simular_temperatura.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        btn_simular_temperatura.setText("Simular");
        btn_simular_temperatura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_simular_temperaturaActionPerformed(evt);
            }
        });
        jPanel_Temperatura.add(btn_simular_temperatura, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 171, 80, 20));

        jPanel_Temperature.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel_Temperature.setPreferredSize(new java.awt.Dimension(229, 196));
        jPanel_Temperature.setLayout(new java.awt.BorderLayout());

        jLabel_grafica_temperature1.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        jLabel_grafica_temperature1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_grafica_temperature1.setText("Gráfica");
        jLabel_grafica_temperature1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel_Temperature.add(jLabel_grafica_temperature1, java.awt.BorderLayout.CENTER);

        jPanel_Temperatura.add(jPanel_Temperature, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 30, 180, 160));

        rs_panel.add(jPanel_Temperatura, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 340, 210));

        jPanel_resultados.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "Resultados", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 18))); // NOI18N
        jPanel_resultados.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel_image_recuperada.setText(".");
        jLabel_image_recuperada.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel_resultados.add(jLabel_image_recuperada, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 250, 230));

        jInternalFrame_fase.setMinimumSize(new java.awt.Dimension(33, 33));
        jInternalFrame_fase.setPreferredSize(new java.awt.Dimension(287, 309));
        jInternalFrame_fase.setVisible(true);

        javax.swing.GroupLayout jInternalFrame_faseLayout = new javax.swing.GroupLayout(jInternalFrame_fase.getContentPane());
        jInternalFrame_fase.getContentPane().setLayout(jInternalFrame_faseLayout);
        jInternalFrame_faseLayout.setHorizontalGroup(
            jInternalFrame_faseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 254, Short.MAX_VALUE)
        );
        jInternalFrame_faseLayout.setVerticalGroup(
            jInternalFrame_faseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );

        jPanel_resultados.add(jInternalFrame_fase, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 60, 270, 230));

        jTextArea_Salida.setEditable(false);
        jTextArea_Salida.setColumns(20);
        jTextArea_Salida.setRows(5);
        jScrollPane1.setViewportView(jTextArea_Salida);

        jPanel_resultados.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, 940, 90));

        jLabel_salida.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        jLabel_salida.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_salida.setText("Salida");
        jPanel_resultados.add(jLabel_salida, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 310, 130, 20));

        jPanel_PDI.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "PDI", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 14))); // NOI18N
        jPanel_PDI.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel15.setText("Escala");
        jPanel_PDI.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 50, 20));

        jLabel16.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel16.setText("Filtro Pasabajas");
        jPanel_PDI.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 100, 20));

        text_resolution_scsale.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        text_resolution_scsale.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        text_resolution_scsale.setText("1");
        text_resolution_scsale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_resolution_scsaleActionPerformed(evt);
            }
        });
        jPanel_PDI.add(text_resolution_scsale, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 140, 30, -1));

        jComboBox_Filtros.setFont(new java.awt.Font("Times New Roman", 2, 14)); // NOI18N
        jComboBox_Filtros.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Media" }));
        jComboBox_Filtros.setEnabled(false);
        jComboBox_Filtros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_FiltrosActionPerformed(evt);
            }
        });
        jPanel_PDI.add(jComboBox_Filtros, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 70, -1));

        rs_filtros1.setFont(new java.awt.Font("Times New Roman", 3, 12)); // NOI18N
        rs_filtros1.setText("Aplicar");
        rs_filtros1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rs_filtros1ActionPerformed(evt);
            }
        });
        jPanel_PDI.add(rs_filtros1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 70, -1));

        rs_Restaurar.setFont(new java.awt.Font("Times New Roman", 3, 12)); // NOI18N
        rs_Restaurar.setText("Original");
        rs_Restaurar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rs_RestaurarActionPerformed(evt);
            }
        });
        jPanel_PDI.add(rs_Restaurar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 90, -1));

        rs_filtros3.setFont(new java.awt.Font("Times New Roman", 3, 12)); // NOI18N
        rs_filtros3.setText("Aplicar");
        rs_filtros3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rs_filtros3ActionPerformed(evt);
            }
        });
        jPanel_PDI.add(rs_filtros3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 70, -1));

        jPanel_resultados.add(jPanel_PDI, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 30, 110, 260));

        jComboBox_Fase1.setFont(new java.awt.Font("Times New Roman", 2, 14)); // NOI18N
        jComboBox_Fase1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Fase Recuperada", "Diferencia de Fase", "Fase Original" }));
        jComboBox_Fase1.setEnabled(false);
        jComboBox_Fase1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_Fase1ActionPerformed(evt);
            }
        });
        jPanel_resultados.add(jComboBox_Fase1, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 30, -1, 20));

        jPanel_estadisticas.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "Estadisticas", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 14))); // NOI18N
        jPanel_estadisticas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jComboBox_Windows.setFont(new java.awt.Font("Times New Roman", 2, 14)); // NOI18N
        jComboBox_Windows.setEnabled(false);
        jComboBox_Windows.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_WindowsItemStateChanged(evt);
            }
        });
        jComboBox_Windows.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_WindowsActionPerformed(evt);
            }
        });
        jPanel_estadisticas.add(jComboBox_Windows, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 80, -1));

        jPanel_grafica_fitness.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel_grafica_fitness.setLayout(new java.awt.BorderLayout());

        jLabel_grafica_fitness.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        jLabel_grafica_fitness.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_grafica_fitness.setText("Gráfica");
        jLabel_grafica_fitness.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel_grafica_fitness.add(jLabel_grafica_fitness, java.awt.BorderLayout.CENTER);

        jPanel_estadisticas.add(jPanel_grafica_fitness, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 230, 140));

        btn_graficar_fitness.setFont(new java.awt.Font("Times New Roman", 3, 12)); // NOI18N
        btn_graficar_fitness.setText("Graficar");
        btn_graficar_fitness.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_graficar_fitnessActionPerformed(evt);
            }
        });
        jPanel_estadisticas.add(btn_graficar_fitness, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 50, 80, -1));

        jSpinner_max.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(50)));
        jPanel_estadisticas.add(jSpinner_max, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, 60, -1));

        jSpinner_Min.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(50)));
        jPanel_estadisticas.add(jSpinner_Min, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 60, -1));

        jLabel18.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel18.setText("Max");
        jPanel_estadisticas.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 30, 20));

        jLabel23.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel23.setText("Min");
        jPanel_estadisticas.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 30, 20));

        btn_anadir.setFont(new java.awt.Font("Times New Roman", 3, 12)); // NOI18N
        btn_anadir.setText("Adicionar");
        btn_anadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_anadirActionPerformed(evt);
            }
        });
        jPanel_estadisticas.add(btn_anadir, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 90, 20));

        btn_run_indep.setFont(new java.awt.Font("Times New Roman", 3, 12)); // NOI18N
        btn_run_indep.setText("Ejecutar");
        btn_run_indep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_run_indepActionPerformed(evt);
            }
        });
        jPanel_estadisticas.add(btn_run_indep, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 230, 90, 20));

        jPanel_resultados.add(jPanel_estadisticas, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 30, 270, 260));

        jLabel22.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel22.setText("Imagen Recuperada");
        jPanel_resultados.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 130, 20));

        rs_panel.add(jPanel_resultados, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 240, 980, 430));

        Main_TPanel.addTab("Recosido Simulado", rs_panel);

        javax.swing.GroupLayout vmo_panelLayout = new javax.swing.GroupLayout(vmo_panel);
        vmo_panel.setLayout(vmo_panelLayout);
        vmo_panelLayout.setHorizontalGroup(
            vmo_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 995, Short.MAX_VALUE)
        );
        vmo_panelLayout.setVerticalGroup(
            vmo_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 679, Short.MAX_VALUE)
        );

        Main_TPanel.addTab("tab3", vmo_panel);

        javax.swing.GroupLayout ag_panelLayout = new javax.swing.GroupLayout(ag_panel);
        ag_panel.setLayout(ag_panelLayout);
        ag_panelLayout.setHorizontalGroup(
            ag_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 995, Short.MAX_VALUE)
        );
        ag_panelLayout.setVerticalGroup(
            ag_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 679, Short.MAX_VALUE)
        );

        Main_TPanel.addTab("tab4", ag_panel);

        getContentPane().add(Main_TPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(-5, 0, 1000, 710));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField_funcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_funcionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_funcionActionPerformed

    private void jTextField_FilaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_FilaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_FilaActionPerformed

    private void jTextField_ColumnaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_ColumnaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_ColumnaActionPerformed

    private void btn_simularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simularActionPerformed
        // TODO add your handling code here:
        image_flag_particionada = true;
        Select_Simulation_openFile = true;
        String function = jTextField_funcion.getText();
        boolean flag = true;
        // FUNCTION TO CHECK THE STRING ON MATH FUNCTION COmprueba que no exista 2 x o y consecutivas
        try {
            char temp = function.charAt(0);
            for (int i = 1; i < function.length(); i++) {
                if (temp == 'x' || temp == 'X' || temp == 'y' || temp == 'Y') {
                    if (function.charAt(i) == 'x' || function.charAt(i) == 'X' || function.charAt(i) == 'y' || function.charAt(i) == 'Y') {
                        flag = false;
                        //custom title, error icon
                        JOptionPane.showMessageDialog(Main_TPanel, "Función Incorrecta", "Inane error", JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                    temp = function.charAt(i);
                }
            }

            // IF EXPRESSION IS CORRECT
            if (flag) {
//            try {
                double xmin = (double) rangeSliderX.getValue();
                double xmax = (double) rangeSliderX.getUpperValue();
                double ymax = (double) rangeSliderY.getUpperValue();
                double ymin = (double) rangeSliderY.getValue();

                int row = Integer.parseInt(jTextField_Fila.getText());
                int col = Integer.parseInt(jTextField_Columna.getText());
//                try {
                this.Image_original = new My_Image(function, xmin, xmax, ymin, ymax, row, col);
//                } catch (Exception ex) {
//                    JOptionPane.showMessageDialog(Main_TPanel, "Función Incorrecta", "Inane error", JOptionPane.ERROR_MESSAGE);
//                }

                jLabel_Image.setIcon(new ImageIcon(Image_original.getImage().getScaledInstance(jLabel_Image.getWidth(), jLabel_Image.getHeight(), Image.SCALE_SMOOTH)));
                label_rs_imagen_original.setIcon(new ImageIcon(Image_original.getImage().getScaledInstance(label_rs_imagen_original.getWidth(), label_rs_imagen_original.getHeight(), Image.SCALE_SMOOTH)));
                Main_TPanel.setEnabledAt(1, true);
                Main_TPanel.setEnabledAt(2, true);
                Main_TPanel.setEnabledAt(3, true);
//            } catch (Exception e) {
//                //custom title, error icon
//                JOptionPane.showMessageDialog(inicio_panel, "Campo Incompleto", "Inane error", JOptionPane.ERROR_MESSAGE);
//            }
            }
        } catch (StringIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(inicio_panel, "Campo de la función vacio", "Inane error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(inicio_panel, "Campo Vacio", "Inane error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(inicio_panel, "Funcion Incorrecta", "Inane error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_simularActionPerformed

    private void jButton_open_fileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_open_fileActionPerformed
        // TODO add your handling code here:
//        Node_Position("344", 28, 28);

        image_flag_particionada = true;
        Select_Simulation_openFile = false;
        File file = Read_Files();
        if (file != null) {
            if (getFileExtension(file).equals("txt")) {
                int row = 0;
                int col = 0;
                Charset charset = Charset.forName("US-ASCII");
                try (BufferedReader reader = Files.newBufferedReader(file.toPath(), charset)) {
                    String line = null;
                    ArrayList<String> numbers = new ArrayList<>();
                    Pattern p = Pattern.compile("\\d+");

                    while ((line = reader.readLine()) != null) {
                        row++;
                        Matcher m = p.matcher(line);
                        while (m.find()) {
                            numbers.add(m.group());
                        }
//                System.out.println(line);
                    }
                    col = numbers.size() / row;
                    double[][] matrix = Matrix_Image(numbers, row, col);
                    this.Image_original = new My_Image(matrix);
                    jLabel_Image.setIcon(new ImageIcon(Image_original.getImage().getScaledInstance(jLabel_Image.getWidth(), jLabel_Image.getHeight(), Image.SCALE_SMOOTH)));
                    label_rs_imagen_original.setIcon(new ImageIcon(Image_original.getImage().getScaledInstance(label_rs_imagen_original.getWidth(), label_rs_imagen_original.getHeight(), Image.SCALE_SMOOTH)));
                } catch (IOException ex) {
                    System.err.format("IOException %s%n", ex);
                }
            } else {
                Image_original = new My_Image(file);
                jLabel_Image.setIcon(new ImageIcon(Image_original.getImage().getScaledInstance(jLabel_Image.getWidth(), jLabel_Image.getHeight(), Image.SCALE_SMOOTH)));
                label_rs_imagen_original.setIcon(new ImageIcon(Image_original.getImage().getScaledInstance(label_rs_imagen_original.getWidth(), label_rs_imagen_original.getHeight(), Image.SCALE_SMOOTH)));
            }
            Main_TPanel.setEnabledAt(1, true);
            Main_TPanel.setEnabledAt(2, true);
            Main_TPanel.setEnabledAt(3, true);
        }

    }//GEN-LAST:event_jButton_open_fileActionPerformed

    private void btn_aplicar_windows_fringesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_aplicar_windows_fringesActionPerformed
        // TODO add your handling code here:
        try {
            image_flag_particionada = false;
            int num_franjas_max = Integer.parseInt(text_Franjas.getText());
            tree = new Arbolphsh<WindowsFringes>(new WindowsFringes("0", Image_original.getMatrixDouble()));
            Num_hojas = 0;
            isBalance(getTree(), num_franjas_max);
            double[][] Matrix_tmp = Image_original.getMatrixDouble().clone();
            ShowPartitionR(tree.getRaiz(), tree, Matrix_tmp);
            Image_partitioned = new My_Image(Matrix_tmp, 1);
            jLabel_Image.setIcon(new ImageIcon(Image_partitioned.getImage().getScaledInstance(jLabel_Image.getWidth(), jLabel_Image.getHeight(), Image.SCALE_SMOOTH)));
            label_rs_imagen_original.setIcon(new ImageIcon(Image_partitioned.getImage().getScaledInstance(label_rs_imagen_original.getWidth(), label_rs_imagen_original.getHeight(), Image.SCALE_SMOOTH)));

//            for (int i = 0; i < Matrix_tmp.length; i++) {
//                for (int j = 0; j < Matrix_tmp[0].length; j++) {
//                    System.out.print(Matrix_tmp[i][j] + ", ");
//                }
//                System.out.println("");
//            }
        } catch (Exception e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(Main_TPanel, "Por favor Carge un Interferograma", "Inane error", JOptionPane.ERROR_MESSAGE);
        }

//        postOrden_Name(tree);
//        Collections.sort(name_nodos);
//        for (Integer name_nodo : name_nodos) {
//            System.out.print(name_nodo.intValue() + " ,");
//        }
    }//GEN-LAST:event_btn_aplicar_windows_fringesActionPerformed

    private void text_FranjasKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_text_FranjasKeyTyped
        // TODO add your handling code here:
        int k = (int) evt.getKeyChar();
        if ((k > 57 || k < 48) && (k != 8 && k != 127)) {
            evt.setKeyChar((char) KeyEvent.VK_CLEAR);
            JOptionPane.showMessageDialog(null, "No puede ingresar letras!!!", "Ventana Error Datos", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_text_FranjasKeyTyped

    private void text_iteracionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_iteracionesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_iteracionesActionPerformed

    private void text_generacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_generacionesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_generacionesActionPerformed

    private void text_temperaturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_temperaturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_temperaturaActionPerformed

    private void text_suavidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_suavidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_suavidadActionPerformed

    private void rs_ejecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rs_ejecutarActionPerformed
        // TODO add your handling code here:

        final SwingWorker worker = new SwingWorker() {

            @Override
            protected Object doInBackground() throws Exception {
                jTextArea_Salida.setText("");
                jLabel_salida.setText("Procesando ...");
                return null;
            }

            protected void done() { // metodo se ejecuta despues de actualizar el swing

                // Borrar los Item de JcomboBox de  WInods
                jComboBox_Windows.removeAllItems();
                jComboBox_Windows.addItem("");

                Original_Simulada_Zoom = (My_Image) Image_original.clone();

//                if (Image_original.getPhase() != null) {
//                    Original_Simulada_Zoom.setPhase(Image_original.getPhase().clone());
//                }
                // Leer La Temperatura
                Generar_Temperatura();
                // FIN de TEmperatura

                // Borrar el directorio donde pones los fitness de cada Ventana
                File Dir = new File("Fitness");
                borrarDirectorio(Dir);

                hilos = new ArrayList<Algorithm_Simulate_Analing>();
                int N = Integer.parseInt(text_iteraciones.getText());
                int ITemp = Integer.parseInt(text_generaciones.getText());
                double suavidad = Double.parseDouble(text_suavidad.getText());
                int poly_orden = Integer.parseInt(text_poly_orden.getText());
                double tolerancia = Double.parseDouble(text_Tolerancia.getText());
                double factor_delta = Double.parseDouble(text_factor_delta.getText());

                int metodo_vecindad = jComboBox_Vecindad.getSelectedIndex();
                int k_vecinos = Integer.parseInt(jSpinner_Kvecinos.getValue().toString());

                int second_run = jComboBox_2_corrida.getSelectedIndex();

                // Spiner ponerle rango de Busqueda
                jSpinner_max.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(2 * N), Integer.valueOf(0), Integer.valueOf(2 * N), Integer.valueOf(50)));
                jSpinner_Min.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(2 * N), Integer.valueOf(50)));
                // SELECCION DEL METODOD DE VECINDAD
                double vecindad_0 = 0;
                double vecindad_f = 0;
                double vecindad_fijo = 0;
                if (metodo_vecindad == 0 || metodo_vecindad == 1) {
                    vecindad_0 = Double.parseDouble(text_factorV_Inicial.getText());
                    vecindad_f = Double.parseDouble(text_factorV_Final.getText());
                } else { // Fijo
                    vecindad_fijo = Double.parseDouble(text_factorV_fijo.getText());
                }

                // INICIALIZO EL ARRAYlIST DONDE SE GUARDARA LOS NODOS DE CADA VENTANA
                nodos = new ArrayList<Solution_Node>();

                long TInicio, TFin, tiempo; //Variables para determinar el tiempo de ejecución

                // Verifico Si se dividio en ventana el interferograma
                if (image_flag_particionada) { // Si es verdadero no se dividio EL INTERFEROGRAMA
                    TInicio = System.currentTimeMillis();
                    Algorithm_Simulate_Analing SA = new Algorithm_Simulate_Analing(Image_original.getMatrixDouble(), poly_orden, Temperature, N, ITemp, metodo_vecindad, k_vecinos, vecindad_fijo, vecindad_f, vecindad_0, suavidad, tolerancia, factor_delta, second_run);
                    SA.setName("0");
                    jComboBox_Windows.addItem(SA.getName());
                    SA.start();
                    while (SA.isAlive()) {
                    }
                    for (Solution_Node nodo : nodos) {
                        nodo.PrintPront();
                    }
                    TFin = System.currentTimeMillis(); //Tomamos la hora en que finalizó el algoritmo y la almacenamos en la variable T
                    tiempo = TFin - TInicio;
                    jTextArea_Salida.append("TIEMPO: " + tiempo / 1000 + " segundos");
                    jLabel_salida.setText("Terminado");
                    // GRAFICAR
                    double[] x = DoubleArray.increment(0.0, 1.0, (double) nodos.get(0).getPhase()[0].length - 1);
                    double[] y = DoubleArray.increment(0.0, 1.0, (double) nodos.get(0).getPhase().length);
                    Plot3DPanel grafica3D = new Plot3DPanel("SOUTH");
                    grafica3D.addGridPlot("Fase", x, y, nodos.get(0).getPhase());
                    Menu.jInternalFrame_fase.setContentPane(grafica3D);

                    // Construyo el interferograma recuperado
                    double[][] Interferogram = new double[nodos.get(0).getPhase().length][nodos.get(0).getPhase()[0].length];
                    for (int i = 0; i < nodos.get(0).getPhase().length; i++) {
                        for (int j = 0; j < nodos.get(0).getPhase()[0].length; j++) {
                            Interferogram[i][j] = 127 - 128 * Math.cos(nodos.get(0).getPhase()[i][j]);
                        }
                    }

                    Image_recuperada = new My_Image(Interferogram, nodos.get(0).getPhase());
//                    Image_recuperada.setPhase(nodos.get(0).getPhase());
                    jLabel_image_recuperada.setIcon(new ImageIcon(Image_recuperada.getImage().getScaledInstance(jLabel_image_recuperada.getWidth(), jLabel_image_recuperada.getHeight(), Image.SCALE_SMOOTH)));
                    Image_modificada = (My_Image) Image_recuperada.clone();
//                    Image_modificada.setPhase(Image_recuperada.getPhase().clone());

                    // Actualizar El JcomboBox de estadisticas
                } else {
                    TInicio = System.currentTimeMillis();

                    postOrden_Ejecutar_Hojas(getTree(), poly_orden, Temperature, N, ITemp, metodo_vecindad, k_vecinos, vecindad_fijo, vecindad_f, vecindad_0, suavidad, tolerancia, factor_delta, jComboBox_Windows, second_run);

//            // Espero por todos los hilos
                    for (Algorithm_Simulate_Analing h : hilos) {
                        while (h.isAlive()) {
                        }
                    }
////            progress_bar.stop();
                    TFin = System.currentTimeMillis(); //Tomamos la hora en que finalizó el algoritmo y la almacenamos en la variable T
                    tiempo = TFin - TInicio;
                    for (Solution_Node nodo : nodos) {
                        nodo.PrintPront();
                    }
                    postOrden_Fill_WindowsJcomobox(tree, jComboBox_Windows);
                    jTextArea_Salida.append("TIEMPO: " + tiempo / 1000 + " segundos");
                    jLabel_salida.setText("Terminado");

                    // UNIR LA SOLUCIÓN DE LOS NODOS
                    ArrayList<Solution_Node> copy_nodos = new ArrayList<>(nodos);
//                    copy_nodos.addAll(nodos);
                    Solution_Node solution_join = Join_All_nodes(copy_nodos);
                    double[] x = DoubleArray.increment(0.0, 1.0, (double) solution_join.getPhase()[0].length - 1);
                    double[] y = DoubleArray.increment(0.0, 1.0, (double) solution_join.getPhase().length);
                    Plot3DPanel grafica3D = new Plot3DPanel("SOUTH");
                    grafica3D.addGridPlot("Fase", x, y, solution_join.getPhase());
                    Menu.jInternalFrame_fase.setContentPane(grafica3D);

                    // Construyo el interferograma recuperado
                    double[][] Interferogram = new double[solution_join.getPhase().length][solution_join.getPhase()[0].length];
                    for (int i = 0; i < solution_join.getPhase().length; i++) {
                        for (int j = 0; j < solution_join.getPhase()[0].length; j++) {
                            Interferogram[i][j] = 127 - 128 * Math.cos(solution_join.getPhase()[i][j]);
                        }
                    }
                    Image_recuperada = new My_Image(Interferogram, solution_join.getPhase().clone());
//                    Image_recuperada.setPhase(solution_join.getPhase());
                    jLabel_image_recuperada.setIcon(new ImageIcon(Image_recuperada.getImage().getScaledInstance(jLabel_image_recuperada.getWidth(), jLabel_image_recuperada.getHeight(), Image.SCALE_SMOOTH)));
                    Image_modificada = new My_Image(Interferogram, solution_join.getPhase().clone());
//                    Image_modificada = (My_Image)Image_recuperada.clone();
//                    Image_modificada.setPhase(solution_join.getPhase().clone());

                }
                if (Select_Simulation_openFile) { // Imagen Simulada
                    jComboBox_Fase1.setEnabled(true);
                } else { // Imagen de un fichero
                    jComboBox_Fase1.setEnabled(false);
                }

                // Habilitar Estadisticas
                jComboBox_Windows.setEnabled(true);
                jComboBox_Filtros.setEnabled(true);

            }
        };
        worker.execute();
    }//GEN-LAST:event_rs_ejecutarActionPerformed

    private void text_poly_ordenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_poly_ordenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_poly_ordenActionPerformed

    private void text_ToleranciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_ToleranciaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_ToleranciaActionPerformed

    private void jComboBox_FiltrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_FiltrosActionPerformed

    }//GEN-LAST:event_jComboBox_FiltrosActionPerformed

    private void btn_aceptar_windows_fringesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_aceptar_windows_fringesActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(this, "División Completa");
        Main_TPanel.setSelectedIndex(1);
    }//GEN-LAST:event_btn_aceptar_windows_fringesActionPerformed

    private void text_factor_deltaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_factor_deltaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_factor_deltaActionPerformed

    private void text_resolution_scsaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_resolution_scsaleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_resolution_scsaleActionPerformed

    private void rs_RestaurarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rs_RestaurarActionPerformed
        flag_zoom = false;
        jLabel_image_recuperada.setIcon(new ImageIcon(Image_recuperada.getImage().getScaledInstance(jLabel_image_recuperada.getWidth(), jLabel_image_recuperada.getHeight(), Image.SCALE_SMOOTH)));
        double[] x = DoubleArray.increment(0.0, 1.0, (double) Image_recuperada.getPhase()[0].length);
        double[] y = DoubleArray.increment(0.0, 1.0, (double) Image_recuperada.getPhase().length);
        Plot3DPanel grafica3D = new Plot3DPanel("SOUTH");
        grafica3D.addGridPlot("Fase", x, y, Image_recuperada.getPhase());
        Menu.jInternalFrame_fase.setContentPane(grafica3D);

        Image_modificada = (My_Image) Image_recuperada.clone();
//        Image_modificada.setPhase(Image_recuperada.getPhase().clone());

        Original_Simulada_Zoom = (My_Image) Image_original.clone();
        if (Image_original.getPhase() != null) {
            Original_Simulada_Zoom.setPhase(Image_original.getPhase().clone());
        }

        label_rs_imagen_original.setIcon(new ImageIcon(Image_partitioned.getImage().getScaledInstance(label_rs_imagen_original.getWidth(), label_rs_imagen_original.getHeight(), Image.SCALE_SMOOTH)));
    }//GEN-LAST:event_rs_RestaurarActionPerformed

    private void jComboBox_TemperatureItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_TemperatureItemStateChanged
        // TODO add your handling code here:
        int method_selected = jComboBox_Temperature.getSelectedIndex();

        // Exponencial
        if (method_selected == 0) {
            text_temperatura_final.setEnabled(true);

            text_alpha.setEnabled(false);

            text_K.setEnabled(false);
            if (!text_iteraciones.getText().isEmpty() && !text_temperatura.getText().isEmpty() && !text_temperatura_final.getText().isEmpty()) {
                int N = Integer.parseInt(text_iteraciones.getText());
                double T0 = Double.parseDouble(text_temperatura.getText());
                double Tf = Double.parseDouble(text_temperatura_final.getText());

                Temperature = new double[N];
                double[] x = rango(0, N - 1, 1);
                double alpha = 0.000001;

                // TEmepartura Function
                for (int i = 0; i < N; i++) {
                    double argumento = 100 * Tf / T0;
                    double beta = Math.log(argumento) / N + N * alpha;
                    double citma = Math.log(T0);
                    Temperature[i] = Math.exp(-alpha * i * i + beta * i + citma);
                }
                Graficas migrafica = new Graficas("Temperature");
                migrafica.agregarGrafica("Iterations", x, Temperature);
                ChartPanel cp = migrafica.obtienePanel();
                jPanel_Temperature.removeAll();
                jPanel_Temperature.add(cp, BorderLayout.CENTER);
                jPanel_Temperature.validate();
            }
        }
        // Geometrica
        if (method_selected == 1) {
            text_alpha.setEnabled(true);
            text_temperatura_final.setEnabled(false);
            text_K.setEnabled(false);

            if (!text_iteraciones.getText().isEmpty() && !text_temperatura.getText().isEmpty() && !text_alpha.getText().isEmpty()) {
                int N = Integer.parseInt(text_iteraciones.getText());
                double T0 = Double.parseDouble(text_temperatura.getText());
                double alpha = Double.parseDouble(text_alpha.getText());

                Temperature = new double[N];
                double[] x = rango(0, N - 1, 1);
                Temperature[0] = T0;

                // TEmepartura Function
                for (int i = 1; i < N; i++) {
                    Temperature[i] = alpha * Temperature[i - 1];
                }
                Graficas migrafica = new Graficas("Temperature");
                migrafica.agregarGrafica("Iterations", x, Temperature);
                ChartPanel cp = migrafica.obtienePanel();
                jPanel_Temperature.removeAll();
                jPanel_Temperature.add(cp, BorderLayout.CENTER);
                jPanel_Temperature.validate();
            }
        }
        // Toledo&Cuevas
        if (method_selected == 2) {
            text_K.setEnabled(true);
            text_temperatura_final.setEnabled(false);
            text_alpha.setEnabled(false);

            if (!text_iteraciones.getText().isEmpty() && !text_temperatura.getText().isEmpty() && !text_K.getText().isEmpty()) {
                int N = Integer.parseInt(text_iteraciones.getText());
                double T0 = Double.parseDouble(text_temperatura.getText());
                double K = Double.parseDouble(text_K.getText());

                Temperature = new double[N];
                double[] x = rango(0, N - 1, 1);

                // TEmepartura Function
                for (int i = 0; i < N; i++) {
                    Temperature[i] = T0 * Math.exp(-i / K);
                }
                Graficas migrafica = new Graficas("Temperature");
                migrafica.agregarGrafica("Iterations", x, Temperature);
                ChartPanel cp = migrafica.obtienePanel();
                jPanel_Temperature.removeAll();
                jPanel_Temperature.add(cp, BorderLayout.CENTER);
                jPanel_Temperature.validate();
            }

        }
        // Lundy & Mees
        if (method_selected == 3) {
            text_temperatura_final.setEnabled(true);

            text_alpha.setEnabled(false);
            text_K.setEnabled(false);

            if (!text_iteraciones.getText().isEmpty() && !text_temperatura.getText().isEmpty() && !text_temperatura_final.getText().isEmpty()) {
                int N = Integer.parseInt(text_iteraciones.getText());
                double T0 = Double.parseDouble(text_temperatura.getText());
                double Tf = Double.parseDouble(text_temperatura_final.getText());

                Temperature = new double[N];
                double[] x = rango(0, N - 1, 1);
                Temperature[0] = T0;
                double beta = (T0 - Tf) / (N * T0 * Tf);

                // TEmepartura Function
                for (int i = 1; i < N; i++) {
                    Temperature[i] = Temperature[i - 1] / (1 + beta * Temperature[i - 1]);
                }
                Graficas migrafica = new Graficas("Temperature");
                migrafica.agregarGrafica("Iterations", x, Temperature);
                ChartPanel cp = migrafica.obtienePanel();
                jPanel_Temperature.removeAll();
                jPanel_Temperature.add(cp, BorderLayout.CENTER);
                jPanel_Temperature.validate();
            }

        }
    }//GEN-LAST:event_jComboBox_TemperatureItemStateChanged

    private void jComboBox_TemperatureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_TemperatureActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox_TemperatureActionPerformed

    private void btn_simular_temperaturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simular_temperaturaActionPerformed
        // Codigo para simular la función de Temperatura
        rs_ejecutar.setEnabled(true);
        Generar_Temperatura();
    }//GEN-LAST:event_btn_simular_temperaturaActionPerformed

    private void text_alphaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_alphaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_alphaActionPerformed

    private void text_KActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_KActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_KActionPerformed

    private void text_temperatura_finalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_temperatura_finalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_temperatura_finalActionPerformed

    private void text_factorV_fijoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_factorV_fijoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_factorV_fijoActionPerformed

    private void text_factorV_InicialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_factorV_InicialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_factorV_InicialActionPerformed

    private void text_factorV_FinalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_factorV_FinalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_factorV_FinalActionPerformed

    private void jComboBox_Fase1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_Fase1ActionPerformed
        // TODO add your handling code here:
        int value = jComboBox_Fase1.getSelectedIndex();
        double[] x = DoubleArray.increment(0.0, 1.0, (double) Image_modificada.getPhase()[0].length - 1);
        double[] y = DoubleArray.increment(0.0, 1.0, (double) Image_modificada.getPhase().length);
        Plot3DPanel grafica3D = new Plot3DPanel("SOUTH");
        grafica3D.removeAllPlots();

        switch (value) {
            case 0: // Fase Recuperada
                grafica3D.addGridPlot("", x, y, Image_recuperada.getPhase());
                Menu.jInternalFrame_fase.setContentPane(grafica3D);
                break;
            case 1: // Diferencia de Fase
                int raw = Image_modificada.getPhase().length;
                int col = Image_modificada.getPhase()[0].length;
                double[][] Dif_fase_normal = new double[raw][col];
                double[][] Dif_fase_invertida = new double[raw][col];
                double sum_normal = 0;
                double sum_invertida = 0;

                double min_fase_recuperada = Auxiliar.Min(Image_recuperada.getPhase());
                double max_fase_recuperada = Auxiliar.Max(Image_recuperada.getPhase());

                // DC para la fase sin modificaion
                double Dc_normal;
//                if (min_fase_original > min_fase_recuperada) {
//                    Dc_normal = Math.abs(min_fase_original - min_fase_recuperada);
//                } else {
//                    Dc_normal = -Math.abs(min_fase_original - min_fase_recuperada);
//                }
                // DC para la fase invertida
                double Dc_invertida;
//                if (min_fase_original > -max_fase_recuperada) {
//                    Dc_invertida = Math.abs(min_fase_original + max_fase_recuperada);
//                } else {
//                    Dc_invertida = -Math.abs(min_fase_original + max_fase_recuperada);
//                }

                for (int i = 0; i < raw; i++) {
                    for (int j = 0; j < col; j++) {
                        sum_normal += Original_Simulada_Zoom.getPhase()[i][j] - Image_modificada.getPhase()[i][j];
                        sum_invertida += Original_Simulada_Zoom.getPhase()[i][j] + Image_modificada.getPhase()[i][j];
                    }
                }
                Dc_normal = sum_normal / (raw * col);
                Dc_invertida = sum_invertida / (raw * col);
                sum_normal = 0;
                sum_invertida = 0;
                for (int i = 0; i < raw; i++) {
                    for (int j = 0; j < col; j++) {
                        Dif_fase_normal[i][j] = Math.abs(Original_Simulada_Zoom.getPhase()[i][j] - (Image_modificada.getPhase()[i][j] + Dc_normal));
                        sum_normal += Math.pow(Dif_fase_normal[i][j], 2);
                        Dif_fase_invertida[i][j] = Math.abs(Original_Simulada_Zoom.getPhase()[i][j] - (-Image_modificada.getPhase()[i][j] + Dc_invertida));
                        sum_invertida += Math.pow(Dif_fase_invertida[i][j], 2);
                    }
                }

                double RMS1 = Math.sqrt(sum_normal) / (raw * col);
                double RMS2 = Math.sqrt(sum_invertida) / (raw * col);

                double errorRelativo_normal = 100 * (sum_normal / (raw * col * (max_fase_recuperada - min_fase_recuperada)));
                double errorRelativo_invertido = 100 * (sum_invertida / (raw * col * (max_fase_recuperada - min_fase_recuperada)));
                if (RMS1 < RMS2) {
                    grafica3D.addGridPlot("Error: " + errorRelativo_normal, x, y, Dif_fase_normal);
                    Menu.jInternalFrame_fase.setContentPane(grafica3D);
                } else {
                    grafica3D.addGridPlot("Error: " + errorRelativo_invertido, x, y, Dif_fase_invertida);
                    Menu.jInternalFrame_fase.setContentPane(grafica3D);
                }
                break;
            case 2: // Fase Original
//                x = DoubleArray.increment(0.0, 1.0, (double) Image_original.getPhase()[0].length - 1);
//                y = DoubleArray.increment(0.0, 1.0, (double) Image_original.getPhase().length);
                if (flag_zoom) {
                    grafica3D.addGridPlot("", x, y, Original_Simulada_Zoom.getPhase());
                    Menu.jInternalFrame_fase.setContentPane(grafica3D);
                } else {
                    grafica3D.addGridPlot("", x, y, Image_original.getPhase());
                    Menu.jInternalFrame_fase.setContentPane(grafica3D);
                }

                break;
            default:
                break;
        }
    }//GEN-LAST:event_jComboBox_Fase1ActionPerformed

    private void rs_filtros1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rs_filtros1ActionPerformed
        // TODO add your handling code here:
        int method_selected = jComboBox_Filtros.getSelectedIndex();
        switch (method_selected) {
            case 0:
                String test = JOptionPane.showInputDialog("Entre la Dimension ");
                if (test != null) {
                    try {
                        int N = Integer.parseInt(test);
                        double[][] kernel = new double[N][N];
                        for (int i = 0; i < N; i++) {
                            for (int j = 0; j < N; j++) {
                                kernel[i][j] = ((double) 1) / (N * N);
                            }
                        }
                        My_Image temp = Convolucion(Image_modificada, kernel);

                        jLabel_image_recuperada.setIcon(new ImageIcon(temp.getImage().getScaledInstance(jLabel_image_recuperada.getWidth(), jLabel_image_recuperada.getHeight(), Image.SCALE_SMOOTH)));
                        Image_modificada = (My_Image) temp.clone();
//                        Image_modificada.setPhase(temp.getPhase().clone());

                        double[] x = DoubleArray.increment(0.0, 1.0, (double) Image_modificada.getPhase()[0].length - 1);
                        double[] y = DoubleArray.increment(0.0, 1.0, (double) Image_modificada.getPhase().length);
                        Plot3DPanel grafica3D = new Plot3DPanel("SOUTH");
                        grafica3D.addGridPlot("Fase", x, y, Image_modificada.getPhase());
                        Menu.jInternalFrame_fase.setContentPane(grafica3D);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(Main_TPanel, "Entre un valor valido", "Inane error", JOptionPane.ERROR_MESSAGE);
                    }

                }

                break;

        }
    }//GEN-LAST:event_rs_filtros1ActionPerformed

    private void jComboBox_WindowsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_WindowsActionPerformed
        // TODO add your handling code here:
////        Charset charset = Charset.forName("US-ASCII");
////        File archivo = null;
////        FileReader fr = null;
////        BufferedReader reader = null;
////        if (!jComboBox_Windows.getSelectedItem().equals("")) {
////            try {
////                archivo = new File("Fitness\\" + jComboBox_Windows.getSelectedItem() + ".txt");
////                fr = new FileReader(archivo);
////                reader = new BufferedReader(fr);
////                String line = null;
////                ArrayList<String> numbers = new ArrayList<>();
////                Pattern p = Pattern.compile("\\d+.\\d+");
////
////                while ((line = reader.readLine()) != null) {
////                    Matcher m = p.matcher(line);
////                    while (m.find()) {
////                        numbers.add(m.group());
////                    }
//////                System.out.println(line);
////                }
////
////                int min = Integer.parseInt(jSpinner_Min.getValue().toString());
////                int max = Integer.parseInt(jSpinner_max.getValue().toString());
////                int number_element = max - min;
////
////                if (number_element > 0) {
//////                    double[] fitness_value = new double[numbers.size()];
//////                double[] x = new double[numbers.size()];
////                    double[] fitness_value = new double[number_element];
////                    double[] x = new double[number_element];
////
////                    for (int i = 0; i < number_element; i++) {
////                        fitness_value[i] = Double.parseDouble(numbers.get(min + i));
////                        x[i] = min + i;
////                    }
////
////                    Graficas migrafica = new Graficas("Fitness");
////                    migrafica.agregarGrafica("Iterations", x, fitness_value);
////                    ChartPanel cp = migrafica.obtienePanel();
////                    jPanel_grafica_fitness.removeAll();
////                    jPanel_grafica_fitness.add(cp, BorderLayout.CENTER);
////                    jPanel_grafica_fitness.validate();
////                    reader.close();
////                } else {
////                    // JoptionPanel
////                    JOptionPane.showMessageDialog(Main_TPanel, "Rango Mal definido", "Inane error", JOptionPane.ERROR_MESSAGE);
////                }
//////               
////            } catch (IOException ex) {
////                System.err.format("IOException %s%n", ex);
////            }
////        }
////////        Charset charset = Charset.forName("US-ASCII");
////////        File archivo = null;
////////        FileReader fr = null;
////////        BufferedReader reader = null;
////////        if (!jComboBox_Windows.getSelectedItem().equals("")) {
////////            try {
////////                archivo = new File("Fitness\\" + jComboBox_Windows.getSelectedItem() + ".txt");
////////                fr = new FileReader(archivo);
////////                reader = new BufferedReader(fr);
////////                String line = null;
////////                ArrayList<String> numbers = new ArrayList<>();
////////                Pattern p = Pattern.compile("\\d+.\\d+");
////////
////////                while ((line = reader.readLine()) != null) {
////////                    Matcher m = p.matcher(line);
////////                    while (m.find()) {
////////                        numbers.add(m.group());
////////                    }
//////////                System.out.println(line);
////////                }
////////                double[] fitness_value = new double[numbers.size()];
////////                double[] x = new double[numbers.size()];
////////                for (int i = 0; i < numbers.size(); i++) {
////////                    fitness_value[i] = Double.parseDouble(numbers.get(i));
////////                    x[i] = i;
////////                }
////////
////////                Graficas migrafica = new Graficas("Fitness");
////////                migrafica.agregarGrafica("Iterations", x, fitness_value);
////////                ChartPanel cp = migrafica.obtienePanel();
////////                jPanel_grafica_fitness.removeAll();
////////                jPanel_grafica_fitness.add(cp, BorderLayout.CENTER);
////////                jPanel_grafica_fitness.validate();
////////                reader.close();
////////            } catch (IOException ex) {
////////                System.err.format("IOException %s%n", ex);
////////            }
////////        }
    }//GEN-LAST:event_jComboBox_WindowsActionPerformed

    private void jComboBox_VecindadItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_VecindadItemStateChanged
        // TODO add your handling code here:
        int method_selected = jComboBox_Temperature.getSelectedIndex();
        // Lineal o Boltzman
        if (method_selected == 0 || method_selected == 1 || method_selected == 2 || method_selected == 3) {
            text_factorV_Inicial.setEnabled(true);
            text_factorV_Final.setEnabled(true);
            text_factorV_fijo.setEnabled(false);
        } else { // Fijo
            text_factorV_Inicial.setEnabled(false);
            text_factorV_Final.setEnabled(false);
            text_factorV_fijo.setEnabled(true);
        }


    }//GEN-LAST:event_jComboBox_VecindadItemStateChanged

    private void btn_graficar_fitnessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_graficar_fitnessActionPerformed
        // TODO add your handling code here:
        Charset charset = Charset.forName("US-ASCII");
        File archivo = null;
        FileReader fr = null;
        BufferedReader reader = null;
        if (!jComboBox_Windows.getSelectedItem().equals("")) {
            try {
                archivo = new File("Fitness\\" + jComboBox_Windows.getSelectedItem() + ".txt");
                fr = new FileReader(archivo);
                reader = new BufferedReader(fr);
                String line = null;
                ArrayList<String> numbers = new ArrayList<>();
                Pattern p = Pattern.compile("\\d+.\\d+");

                while ((line = reader.readLine()) != null) {
                    Matcher m = p.matcher(line);
                    while (m.find()) {
                        numbers.add(m.group());
                    }
//                System.out.println(line);
                }

                int min = Integer.parseInt(jSpinner_Min.getValue().toString());
                int max = Integer.parseInt(jSpinner_max.getValue().toString());
                int number_element;
                if (max > numbers.size()) {
                    number_element = numbers.size() - min;
                } else {
                    number_element = max - min;
                }

                if (number_element > 0) {
//                    double[] fitness_value = new double[numbers.size()];
//                double[] x = new double[numbers.size()];
                    double[] fitness_value = new double[number_element];
                    double[] x = new double[number_element];

                    for (int i = 0; i < number_element; i++) {
                        fitness_value[i] = Double.parseDouble(numbers.get(min + i));
                        x[i] = min + i;
                    }

                    Graficas migrafica = new Graficas("Fitness");
                    migrafica.agregarGrafica("Iterations", x, fitness_value);
                    ChartPanel cp = migrafica.obtienePanel();
                    jPanel_grafica_fitness.removeAll();
                    jPanel_grafica_fitness.add(cp, BorderLayout.CENTER);
                    jPanel_grafica_fitness.validate();
                    reader.close();
                } else {
                    // JoptionPanel
                    JOptionPane.showMessageDialog(Main_TPanel, "Rango Mal definido", "Inane error", JOptionPane.ERROR_MESSAGE);
                }
//               
            } catch (IOException ex) {
                System.err.format("IOException %s%n", ex);
            }
        }
    }//GEN-LAST:event_btn_graficar_fitnessActionPerformed

    private void jComboBox_WindowsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_WindowsItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox_WindowsItemStateChanged

    private void rs_filtros3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rs_filtros3ActionPerformed
        // TODO add your handling code here:

        double scale = Double.parseDouble(text_resolution_scsale.getText());
        int cols = (int)(Image_recuperada.getPhase()[0].length*scale);
        int rows = (int)(Image_recuperada.getPhase().length*scale);
        
        
        BufferedImage scaledImage = new BufferedImage(cols, rows, BufferedImage.TYPE_BYTE_GRAY);
        final AffineTransform at = AffineTransform.getScaleInstance(scale, scale);
        final AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        scaledImage = ato.filter(this.Image_recuperada.Get_Image_Phase(), scaledImage);
        My_Image temp = new My_Image(scaledImage);
        temp.Update();
        
        /* UNIR LA FASE Y GRAFICARLA*/
        
        double[] x = DoubleArray.increment(0.0, 1.0, (double) temp.getMatrix()[0].length - 1);
        double[] y = DoubleArray.increment(0.0, 1.0, (double) temp.getMatrix().length - 1);
        Plot3DPanel grafica3D = new Plot3DPanel("SOUTH");
        grafica3D.addGridPlot("Fase", x, y, temp.getMatrixDouble());
        Menu.jInternalFrame_fase.setContentPane(grafica3D);    
        
                // Construyo el interferograma recuperado
        double[][] Interferogram = new double[temp.getMatrix().length][temp.getMatrix()[0].length];
        for (int i = 0; i < temp.getMatrix().length; i++) {
            for (int j = 0; j < temp.getMatrix()[0].length; j++) {
                Interferogram[i][j] = 127 - 128 * Math.cos(temp.getMatrix()[i][j]);
            }
        }
        Image_modificada = new My_Image(Interferogram);
        Image_modificada.setPhase(temp.getMatrixDouble());
        jLabel_image_recuperada.setIcon(new ImageIcon(Image_modificada.getImage().getScaledInstance(jLabel_image_recuperada.getWidth(), jLabel_image_recuperada.getHeight(), Image.SCALE_SMOOTH)));
        if (Select_Simulation_openFile) {
            flag_zoom = true;
            Original_Simulada_Zoom = Image_original.Escalar(temp.getMatrix().length, temp.getMatrix()[0].length);
            label_rs_imagen_original.setIcon(new ImageIcon(Original_Simulada_Zoom.getImage().getScaledInstance(label_rs_imagen_original.getWidth(), label_rs_imagen_original.getHeight(), Image.SCALE_SMOOTH)));
        }
        

        
////////            double Ymax = (double) elemt.getPhase().length;

////////        ArrayList<Solution_Node> copy_nodos = new ArrayList<>(nodos);
////////        ArrayList<Solution_Node> zoom = new ArrayList<Solution_Node>();
////////
////////        for (Solution_Node elemt : nodos) {
////////            double Xmax = (double) elemt.getPhase()[0].length;
////////            double Ymax = (double) elemt.getPhase().length;
////////
////////            int row_new = (int) (scale * elemt.getPhase().length);
////////            int col_new = (int) (scale * elemt.getPhase()[0].length);
////////            double[][] phase_zoom = new double[row_new][col_new];
////////
////////            double Dx = (Xmax - 1) / col_new;
////////            double Dy = (Ymax - 1) / row_new;
////////
////////            double x, y;
////////            for (int i = 0; i < row_new; i++) {
////////                y = Dy * (i);
////////                for (int j = 0; j < col_new; j++) {
////////                    x = Dx * (j);
////////                    phase_zoom[i][j] = elemt.Phase_Funtion(elemt.getCoef(), x, y);
////////                }
////////            }
////////            zoom.add(new Solution_Node(elemt.getCoef(), elemt.getFitness(), phase_zoom, elemt.getID()));
////////        }
////////
////////        // Unir la fase ZOOm
////////        Solution_Node solution_join = Join_All_nodes_Zoom(copy_nodos, zoom);
////////        double[] x = DoubleArray.increment(0.0, 1.0, (double) solution_join.getPhase()[0].length - 1);
////////        double[] y = DoubleArray.increment(0.0, 1.0, (double) solution_join.getPhase().length);
////////        Plot3DPanel grafica3D = new Plot3DPanel("SOUTH");
////////        grafica3D.addGridPlot("Fase", x, y, solution_join.getPhase());
////////        Menu.jInternalFrame_fase.setContentPane(grafica3D);
////////
////////        // Construyo el interferograma recuperado
////////        double[][] Interferogram = new double[solution_join.getPhase().length][solution_join.getPhase()[0].length];
////////        for (int i = 0; i < solution_join.getPhase().length; i++) {
////////            for (int j = 0; j < solution_join.getPhase()[0].length; j++) {
////////                Interferogram[i][j] = 127 - 128 * Math.cos(solution_join.getPhase()[i][j]);
////////            }
////////        }
////////        Image_modificada = new My_Image(Interferogram);
////////        Image_modificada.setPhase(solution_join.getPhase());
////////        jLabel_image_recuperada.setIcon(new ImageIcon(Image_modificada.getImage().getScaledInstance(jLabel_image_recuperada.getWidth(), jLabel_image_recuperada.getHeight(), Image.SCALE_SMOOTH)));
////////        if (Select_Simulation_openFile) {
////////            flag_zoom = true;
////////            Original_Simulada_Zoom = Image_original.Escalar(solution_join.getPhase().length, solution_join.getPhase()[0].length);
////////            label_rs_imagen_original.setIcon(new ImageIcon(Original_Simulada_Zoom.getImage().getScaledInstance(label_rs_imagen_original.getWidth(), label_rs_imagen_original.getHeight(), Image.SCALE_SMOOTH)));
////////        }

    }//GEN-LAST:event_rs_filtros3ActionPerformed

    private void text_poly_ordenFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_text_poly_ordenFocusLost
        // TODO add your handling code here:
        int temp_poly_orden = Integer.parseInt(text_poly_orden.getText());
        jSpinner_Kvecinos.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf((temp_poly_orden + 1) * (temp_poly_orden + 2) / 2), Integer.valueOf(1), Integer.valueOf(200), Integer.valueOf(1)));
    }//GEN-LAST:event_text_poly_ordenFocusLost

    private void jComboBox_2_corridaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_2_corridaItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox_2_corridaItemStateChanged

    private void jComboBox_VecindadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_VecindadActionPerformed
        // TODO add your handling code here:
        int select_vecinity_method = jComboBox_Vecindad.getSelectedIndex();
        switch (select_vecinity_method) {
            case 0:
                text_factorV_Inicial.setEnabled(true);
                text_factorV_Final.setEnabled(true);
                text_factorV_fijo.setEnabled(false);
                break;
            case 1:
                text_factorV_Inicial.setEnabled(true);
                text_factorV_Final.setEnabled(true);
                text_factorV_fijo.setEnabled(false);
                break;
            case 2:
                text_factorV_Inicial.setEnabled(false);
                text_factorV_Final.setEnabled(false);
                text_factorV_fijo.setEnabled(false);
                break;
            case 3:
                text_factorV_Inicial.setEnabled(false);
                text_factorV_Final.setEnabled(false);
                text_factorV_fijo.setEnabled(false);
                break;
            case 4:
                text_factorV_Inicial.setEnabled(false);
                text_factorV_Final.setEnabled(false);
                text_factorV_fijo.setEnabled(true);
                break;
        }
    }//GEN-LAST:event_jComboBox_VecindadActionPerformed

    private void btn_anadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_anadirActionPerformed
        // TODO add your handling code here:
        dlg = new DialogModal(this, true);
        dlg.postOrden_Fill_List(tree);
        dlg.setVisible(true);
    }//GEN-LAST:event_btn_anadirActionPerformed

    private void btn_run_indepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_run_indepActionPerformed
        // TODO add your handling code here:
        final SwingWorker worker = new SwingWorker() {

            @Override
            protected Object doInBackground() throws Exception {
                jTextArea_Salida.setText("");
                jLabel_salida.setText("Procesando ...");
                return null;
            }

            protected void done() { // metodo se ejecuta despues de actualizar el swing

                ArrayList<String> selected_wind = dlg.getOut();
                // Borrar los Item de JcomboBox de  WInods
                jComboBox_Windows.removeAllItems();
                jComboBox_Windows.addItem("");

                Original_Simulada_Zoom = (My_Image) Image_original.clone();

                // Leer La Temperatura
                Generar_Temperatura();
                // FIN de TEmperatura

                // Borrar el directorio donde pones los fitness de cada Ventana
                File Dir = new File("Fitness");
                borrarDirectorio_Seleted(Dir, selected_wind);

                hilos = new ArrayList<Algorithm_Simulate_Analing>();
                int N = Integer.parseInt(text_iteraciones.getText());
                int ITemp = Integer.parseInt(text_generaciones.getText());
                double suavidad = Double.parseDouble(text_suavidad.getText());
                int poly_orden = Integer.parseInt(text_poly_orden.getText());
                double tolerancia = Double.parseDouble(text_Tolerancia.getText());
                double factor_delta = Double.parseDouble(text_factor_delta.getText());

                int metodo_vecindad = jComboBox_Vecindad.getSelectedIndex();
                int k_vecinos = Integer.parseInt(jSpinner_Kvecinos.getValue().toString());

                int second_run = jComboBox_2_corrida.getSelectedIndex();

                // Spiner ponerle rango de Busqueda
                jSpinner_max.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(2 * N), Integer.valueOf(0), Integer.valueOf(2 * N), Integer.valueOf(50)));
                jSpinner_Min.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(2 * N), Integer.valueOf(50)));
                // SELECCION DEL METODOD DE VECINDAD
                double vecindad_0 = 0;
                double vecindad_f = 0;
                double vecindad_fijo = 0;
                if (metodo_vecindad == 0 || metodo_vecindad == 1) {
                    vecindad_0 = Double.parseDouble(text_factorV_Inicial.getText());
                    vecindad_f = Double.parseDouble(text_factorV_Final.getText());
                } else { // Fijo
                    vecindad_fijo = Double.parseDouble(text_factorV_fijo.getText());
                }

                // INICIALIZO EL ARRAYlIST DONDE SE GUARDARA LOS NODOS DE CADA VENTANA
//        nodos = new ArrayList<Solution_Node>();
                long TInicio, TFin, tiempo; //Variables para determinar el tiempo de ejecución                
                TInicio = System.currentTimeMillis();

                // EJECUTAR EL ALGORITMO
//        postOrden_Ejecutar_Hojas(getTree(), poly_orden, Temperature, N, ITemp, metodo_vecindad, k_vecinos, vecindad_fijo, vecindad_f, vecindad_0, suavidad, tolerancia, factor_delta, jComboBox_Windows, second_run);
                postOrden_Ejecutar_Hojas_Ind(getTree(), poly_orden, Temperature, N, ITemp, metodo_vecindad, k_vecinos, vecindad_fijo, vecindad_f, vecindad_0, suavidad, tolerancia, factor_delta, jComboBox_Windows, second_run, selected_wind);

//            // Espero por todos los hilos
                for (Algorithm_Simulate_Analing h : hilos) {
                    while (h.isAlive()) {
                    }
                }
                for (Solution_Node nodo : nodos) {
                    nodo.PrintPront();
                }
////            progress_bar.stop();
                TFin = System.currentTimeMillis(); //Tomamos la hora en que finalizó el algoritmo y la almacenamos en la variable T
                tiempo = TFin - TInicio;
                postOrden_Fill_WindowsJcomobox(tree, jComboBox_Windows);
                jTextArea_Salida.append("TIEMPO: " + tiempo / 1000 + " segundos");
                jLabel_salida.setText("Terminado");

                // UNIR LA SOLUCIÓN DE LOS NODOS
                ArrayList<Solution_Node> copy_nodos = new ArrayList<>(nodos);
//                    copy_nodos.addAll(nodos);
                Solution_Node solution_join = Join_All_nodes(copy_nodos);
                double[] x = DoubleArray.increment(0.0, 1.0, (double) solution_join.getPhase()[0].length - 1);
                double[] y = DoubleArray.increment(0.0, 1.0, (double) solution_join.getPhase().length);
                Plot3DPanel grafica3D = new Plot3DPanel("SOUTH");
                grafica3D.addGridPlot("Fase", x, y, solution_join.getPhase());
                Menu.jInternalFrame_fase.setContentPane(grafica3D);

                // Construyo el interferograma recuperado
                double[][] Interferogram = new double[solution_join.getPhase().length][solution_join.getPhase()[0].length];
                for (int i = 0; i < solution_join.getPhase().length; i++) {
                    for (int j = 0; j < solution_join.getPhase()[0].length; j++) {
                        Interferogram[i][j] = 127 - 128 * Math.cos(solution_join.getPhase()[i][j]);
                    }
                }
                Image_recuperada = new My_Image(Interferogram, solution_join.getPhase().clone());
//                    Image_recuperada.setPhase(solution_join.getPhase());
                jLabel_image_recuperada.setIcon(new ImageIcon(Image_recuperada.getImage().getScaledInstance(jLabel_image_recuperada.getWidth(), jLabel_image_recuperada.getHeight(), Image.SCALE_SMOOTH)));
                Image_modificada = new My_Image(Interferogram, solution_join.getPhase().clone());
//                    Image_modificada = (My_Image)Image_recuperada.clone();
//                    Image_modificada.setPhase(solution_join.getPhase().clone());
            }
        };
        worker.execute();

    }//GEN-LAST:event_btn_run_indepActionPerformed

    public My_Image Amplificar(My_Image Img, double orden) {

        int rows = (int) (Img.getImage().getHeight() * orden);
        int cols = (int) (Img.getImage().getWidth() * orden);

        int matrix[][] = new int[rows][cols];

        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                matrix[i][j] = Img.getMatrix()[(int) (i / orden)][(int) (j / orden)];
            }
        }

        BufferedImage img_tmp = new BufferedImage(cols, rows, Img.getImage().getType());
        My_Image temp = new My_Image(img_tmp, matrix);
        temp.Update();
        return temp;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Menu().setVisible(true);
            }
        });
    }

    /**
     * FUNCION PARA LEER LOS FICHEROS
     */
    private File Read_Files() {
        JFileChooser selector = new JFileChooser("C:\\Users\\CIO\\Documents\\IOSVANI\\PROGRAMACION 2018\\IMAGENES");
//        selector.setMultiSelectionEnabled(true);
        selector.setDialogTitle("Seleccion de Ficheros Imagen");
        //Filtramos los tipos de archivos
        FileNameExtensionFilter filtroImagen = new FileNameExtensionFilter("JPG & GIF & BMP & TIF & TXT", "jpg", "gif", "bmp", "tif", "txt");
        selector.setFileFilter(filtroImagen);
        //Abrimos el cuadro de diálog
        int flag = selector.showOpenDialog(null);

        if (flag == JFileChooser.APPROVE_OPTION) {
            try {
//                File[] files = selector.getSelectedFiles();
                //Devuelve el fichero seleccionado
                File file = selector.getSelectedFile();
//                System.out.println(getFileExtension(file)); 
//                if (getFileExtension(file).equals("txt")) {
//                    
//                } else {
//                }
                //Asignamos a la variable bmp la imagen leida
//                bmp = ImageIO.read(imagenSeleccionada);
//                if ((resultado = folderFile.exists())) {
//                    File[] files = folderFile.listFiles();
//                    for (File file : files) {
//                        boolean isFolder = file.isDirectory();
////                        System.out.println((isFolder ? "FOLDER: " : "  FILE: ") + file.getName());
//                    }
                return file;
//                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e + "" + "\nNo se ha encontrado el archivo", "ADVERTENCIA!!!", JOptionPane.WARNING_MESSAGE);
            }
        }
        return null;
    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    public double[][] Matrix_Image(ArrayList<String> numbers, int row, int col) {
        double[][] matrix = new double[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                matrix[i][j] = Double.parseDouble(numbers.get(i * col + j));
            }
        }
        return matrix;
    }

    public void isBalance(Arbolphsh<WindowsFringes> tree, int nun_franjas) {
        do {
            flag_Simulacion = true;
            isBalanceR(tree.getRaiz(), nun_franjas, tree);
        } while (!flag_Simulacion);
    }

    private void isBalanceR(Nodo<WindowsFringes> nodo, int nun_franjas, Arbolphsh<WindowsFringes> tree) {
        if (nodo != null) {
            Nodo<WindowsFringes> ph = nodo.getPH();
            isBalanceR(ph, nun_franjas, tree);
            while (ph != null) {
                ph = ph.getSH();
                isBalanceR(ph, nun_franjas, tree);
            }
            // Pregunto si el nodo es una hoja
            if (nodo.getPH() == null) {
                if (Calcular_Franjas(nodo.getInfo().getWindows()) > nun_franjas) {
                    flag_Simulacion = false;
                    // Construyo los nuevos hijos
                    int row = nodo.getInfo().getWindows().length;
                    int col = nodo.getInfo().getWindows()[0].length;
                    int row_med = row / 2;
                    int col_med = col / 2;
                    String name = nodo.getInfo().getName();
                    WindowsFringes w1 = new WindowsFringes(name.concat("1"), Sub_Matrix(nodo.getInfo().getWindows(), 0, row_med, 0, col_med));

                    WindowsFringes w2 = new WindowsFringes(name.concat("2"), Sub_Matrix(nodo.getInfo().getWindows(), 0, row_med, col_med, col));

                    WindowsFringes w3 = new WindowsFringes(name.concat("3"), Sub_Matrix(nodo.getInfo().getWindows(), row_med, row, 0, col_med));

                    WindowsFringes w4 = new WindowsFringes(name.concat("4"), Sub_Matrix(nodo.getInfo().getWindows(), row_med, row, col_med, col));
                    try {
                        tree.insertaHijo(nodo.getInfo(), w1);
                        tree.insertaHijo(nodo.getInfo(), w2);
                        tree.insertaHijo(nodo.getInfo(), w3);
                        tree.insertaHijo(nodo.getInfo(), w4);
                    } catch (ArbolVacioException ex) {
                        Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NodoNoEncontrado ex) {
                        Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                    }

//                } else {
////                    System.out.println(nodo.getInfo().getName());
////                    Si es una hoja que cumple con el numero de franjas
//                    Num_hojas++;
                }
            }
        }
    }

    /**
     * FUNCION QUE LE PASO EL NUMERO DE LA CADENA Y ME DEVUELVE LA POSICION EN
     * LA MATRIZ DEL NODO
     */
    private int[] Node_Position(String name, int row, int col) {
        int[] result = new int[2];
        for (int i = 0; i < name.length(); i++) {
            switch (name.charAt(i)) {
                case '1':
                    break;
                case '2':
                    result[1] += col / Math.pow(2, i + 1);
                    break;
                case '3':
                    result[0] += row / Math.pow(2, i + 1);
                    break;
                case '4':
                    result[0] += row / Math.pow(2, i + 1);
                    result[1] += col / Math.pow(2, i + 1);
                    break;
            }
        }

        return result;
    }

    private void ShowPartitionR(Nodo<WindowsFringes> nodo, Arbolphsh<WindowsFringes> tree, double[][] Matrix) {
        if (nodo != null) {
            Nodo<WindowsFringes> ph = nodo.getPH();
            ShowPartitionR(ph, tree, Matrix);
            while (ph != null) {
                ph = ph.getSH();
                ShowPartitionR(ph, tree, Matrix);
            }
            // Pregunto si el nodo tiene hijos
            if (nodo.getPH() != null) {
                // Construyo los nuevos hijos
                int row = tree.getRaiz().getInfo().getWindows().length;
                int col = tree.getRaiz().getInfo().getWindows()[0].length;
                String name = nodo.getInfo().getName();
                int[] position = Node_Position(name.substring(1), row, col);
                int row_nodo = nodo.getInfo().getWindows().length;
                int col_nodo = nodo.getInfo().getWindows()[0].length;
                int row_tmp = position[0] + row_nodo / 2;
                int col_tmp = position[1] + col_nodo / 2;

                // lleno la fila
                for (int i = position[1]; i < position[1] + col_nodo; i++) {
                    Matrix[row_tmp][i] = -1;
                }
                // lleno la columna
                for (int i = position[0]; i < position[0] + row_nodo; i++) {
                    Matrix[i][col_tmp] = -1;
                }

            }
        }
    }

    /**
     * Calcula el numero de franjas de una ventana
     */
    public int Calcular_Franjas(double[][] image) {
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
    public int Franges_Hor(int[][] img) {
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

    public int Franges_Ver(int[][] img) {
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

    public int Franges_Diagonal(int[][] img) {
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

    public int Franges_Diagonal_Inversa(int[][] img) {
        int longitud;
        if (img.length < img[0].length) {
            longitud = img.length;
        } else {
            longitud = img[0].length;
        }
        double current = img[longitud - 1][longitud - 1];
        int contador = 0;
        if (current == 1) {
            contador = 2;
        }

        for (int i = longitud - 2; i >= 0; i--) {
            if (current != img[i][i]) {
                contador++;
                current = img[i][i];
            }
        }
        return contador / 2;
    }

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

    /**
     * *END FRANJAS***
     */
// Metodo que divide una submatrix
    public double[][] Sub_Matrix(double[][] matrix, int row1, int row2, int col1, int col2) {
        double[][] result = new double[row2 - row1][col2 - col1];
        int col, row = row1;
        for (int i = 0; i < result.length; i++) {
            col = col1;
            for (int j = 0; j < result[0].length; j++) {
                result[i][j] = matrix[row][col];
                col++;
            }
            row++;
        }
        return result;
    }

    // Post Orden Name Hoja
    public void postOrden_Name(Arbolphsh<WindowsFringes> tree) {
        postOrdenR_Name(tree.getRaiz());
    }

    private void postOrdenR_Name(Nodo<WindowsFringes> nodo) {
        if (nodo != null) {
            Nodo<WindowsFringes> ph = nodo.getPH();
            postOrdenR_Name(ph);
            while (ph != null) {
                ph = ph.getSH();
                postOrdenR_Name(ph);
            }
            name_nodos.add(new Integer(nodo.getInfo().getName()));
//            System.out.println(nodo.getInfo().getName());
//            System.out.print(',');
        }
    }

    /**
     * RECORRER EL ARBOL EN POSTORDEN*
     */
    public static void postOrden_Ejecutar_Hojas(Arbolphsh<WindowsFringes> tree, int poly_orden, double[] Temperatura, int N, int ITemp, int metodo_vecindad, int k_vecinos, double factor_vecindad_fijo, double factor_vecindad_final, double factor_vecindad_inicial, double factor_suavidad, double error, double factor_delta, JComboBox wind, int second_run) {
        postOrdenR_Ejecutar_Hojas(tree.getRaiz(), poly_orden, Temperatura, N, ITemp, metodo_vecindad, k_vecinos, factor_vecindad_fijo, factor_vecindad_final, factor_vecindad_inicial, factor_suavidad, error, factor_delta, wind, second_run);
    }

    private static void postOrdenR_Ejecutar_Hojas(Nodo<WindowsFringes> nodo, int poly_orden, double[] Temperatura, int N, int ITemp, int metodo_vecindad, int k_vecinos, double factor_vecindad_fijo, double factor_vecindad_final, double factor_vecindad_inicial, double factor_suavidad, double error, double factor_delta, JComboBox wind, int second_run) {
        if (nodo != null) {
            Nodo<WindowsFringes> ph = nodo.getPH();
            postOrdenR_Ejecutar_Hojas(ph, poly_orden, Temperatura, N, ITemp, metodo_vecindad, k_vecinos, factor_vecindad_fijo, factor_vecindad_final, factor_vecindad_inicial, factor_suavidad, error, factor_delta, wind, second_run);
            while (ph != null) {
                ph = ph.getSH();
                postOrdenR_Ejecutar_Hojas(ph, poly_orden, Temperatura, N, ITemp, metodo_vecindad, k_vecinos, factor_vecindad_fijo, factor_vecindad_final, factor_vecindad_inicial, factor_suavidad, error, factor_delta, wind, second_run);
            }
            // PREGUNTO SI EL NODO ES HOJA Y SI SI LLAMO AL ALGORITMO DE RS
            if (nodo.getPH() == null) {
//                System.out.println(nodo.getInfo().getName());
//                System.out.println(nodo.getInfo().getName());
//                System.out.println(Auxiliar.Calcular_Franjas(nodo.getInfo().getWindows()));
//                System.out.println("");

                Algorithm_Simulate_Analing SD = new Algorithm_Simulate_Analing(nodo.getInfo().getWindows(), poly_orden, Temperatura, N, ITemp, metodo_vecindad, k_vecinos, factor_vecindad_fijo, factor_vecindad_final, factor_vecindad_inicial, factor_suavidad, error, factor_delta, second_run);
                SD.setName(nodo.getInfo().getName());
                hilos.add(SD);
//                wind.addItem(SD.getName());
                SD.start();
            }

        }
    }

    /**
     * RECORRER EL ARBOL EN POSTORDEN*
     */
    public static void postOrden_Ejecutar_Hojas_Ind(Arbolphsh<WindowsFringes> tree, int poly_orden, double[] Temperatura, int N, int ITemp, int metodo_vecindad, int k_vecinos, double factor_vecindad_fijo, double factor_vecindad_final, double factor_vecindad_inicial, double factor_suavidad, double error, double factor_delta, JComboBox wind, int second_run, ArrayList<String> select) {
        postOrdenR_Ejecutar_Hojas_Ind(tree.getRaiz(), poly_orden, Temperatura, N, ITemp, metodo_vecindad, k_vecinos, factor_vecindad_fijo, factor_vecindad_final, factor_vecindad_inicial, factor_suavidad, error, factor_delta, wind, second_run, select);
    }

    private static void postOrdenR_Ejecutar_Hojas_Ind(Nodo<WindowsFringes> nodo, int poly_orden, double[] Temperatura, int N, int ITemp, int metodo_vecindad, int k_vecinos, double factor_vecindad_fijo, double factor_vecindad_final, double factor_vecindad_inicial, double factor_suavidad, double error, double factor_delta, JComboBox wind, int second_run, ArrayList<String> select) {
        if (nodo != null) {
            Nodo<WindowsFringes> ph = nodo.getPH();
            postOrdenR_Ejecutar_Hojas_Ind(ph, poly_orden, Temperatura, N, ITemp, metodo_vecindad, k_vecinos, factor_vecindad_fijo, factor_vecindad_final, factor_vecindad_inicial, factor_suavidad, error, factor_delta, wind, second_run, select);
            while (ph != null) {
                ph = ph.getSH();
                postOrdenR_Ejecutar_Hojas_Ind(ph, poly_orden, Temperatura, N, ITemp, metodo_vecindad, k_vecinos, factor_vecindad_fijo, factor_vecindad_final, factor_vecindad_inicial, factor_suavidad, error, factor_delta, wind, second_run, select);
            }
            // PREGUNTO SI EL NODO ES HOJA Y SI SI LLAMO AL ALGORITMO DE RS
            if (nodo.getPH() == null && select.contains(nodo.getInfo().getName())) {
                for (Solution_Node nodo1 : nodos) {
                    if (nodo1.getID() == Integer.parseInt(nodo.getInfo().getName())) {
                        nodos.remove(nodo1);
                        break;
                    }
                }

                Algorithm_Simulate_Analing SD = new Algorithm_Simulate_Analing(nodo.getInfo().getWindows(), poly_orden, Temperatura, N, ITemp, metodo_vecindad, k_vecinos, factor_vecindad_fijo, factor_vecindad_final, factor_vecindad_inicial, factor_suavidad, error, factor_delta, second_run);
                SD.setName(nodo.getInfo().getName());
                hilos.add(SD);
//                wind.addItem(SD.getName());
                SD.start();
            }

        }
    }

    /**
     * RECORRER EL ARBOL EN POSTORDEN*
     */
    public static void postOrden_Fill_WindowsJcomobox(Arbolphsh<WindowsFringes> tree, JComboBox wind) {
        postOrdenR_Fill_WindowsJcomobox(tree.getRaiz(), wind);
    }

    private static void postOrdenR_Fill_WindowsJcomobox(Nodo<WindowsFringes> nodo, JComboBox wind) {
        if (nodo != null) {
            Nodo<WindowsFringes> ph = nodo.getPH();
            postOrdenR_Fill_WindowsJcomobox(ph, wind);
            while (ph != null) {
                ph = ph.getSH();
                postOrdenR_Fill_WindowsJcomobox(ph, wind);
            }
            // PREGUNTO SI EL NODO ES HOJA Y SI SI LLAMO AL ALGORITMO DE RS
            if (nodo.getPH() == null) {
                wind.addItem(nodo.getInfo().getName());
            }

        }
    }

    /**
     * UNE LA FASE DE TODOS LOS NODOS
     */
    public static Solution_Node Join_All_nodes(ArrayList<Solution_Node> array_nodo) {
        Collections.sort(array_nodo);
        int idx;
        while (array_nodo.size() > 1) {
            idx = array_nodo.size();
            Solution_Node C1 = array_nodo.get(idx - 4);
            Solution_Node C2 = array_nodo.get(idx - 3);
            Solution_Node C3 = array_nodo.get(idx - 2);
            Solution_Node C4 = array_nodo.get(idx - 1);
            Solution_Node temp = Join_phase(C1, C2, C3, C4);
            array_nodo.remove(C1);
            array_nodo.remove(C2);
            array_nodo.remove(C3);
            array_nodo.remove(C4);
            array_nodo.add(temp);
            Collections.sort(array_nodo);
        }
        return array_nodo.get(0);

    }

    private static Solution_Node Join_phase(Solution_Node M1, Solution_Node M2, Solution_Node M3, Solution_Node M4) {
        // ID para la proxima hoja
        String Id_str = String.valueOf(M1.getID());
        int ID;
        if (Id_str.length() > 1) {
            ID = Integer.parseInt(Id_str.substring(0, Id_str.length() - 1));
        } else {
            ID = 0;
        }

        // Spline Cubic
        SplineInterpolator interp = new SplineInterpolator();

        Solution_Node result = null;
        int row = M1.getPhase().length + M3.getPhase().length;
        int col = M1.getPhase()[0].length + M2.getPhase()[0].length;
        double[][] matrix = new double[row][col];
        // Agrego la primera matriz ke se toma como referencia
        for (int i = 0; i < M1.getPhase().length; i++) {
            for (int j = 0; j < M1.getPhase()[0].length; j++) {
                matrix[i][j] = M1.getPhase()[i][j];
            }
        }

        // Emparejar por columna M1 M2
        double[] M2_col = new double[M2.getPhase().length];
        double[] M2_col_inv = new double[M2.getPhase().length];
        double[] M3_row = new double[M3.getPhase()[0].length];
        double[] M3_row_inv = new double[M3.getPhase()[0].length];
        double[] M4_row = new double[M4.getPhase()[0].length];
        double[] M4_row_inv = new double[M4.getPhase()[0].length];
        double[] M4_col = new double[M4.getPhase().length];
        double[] M4_col_inv = new double[M4.getPhase().length];

        // Buscar matrices inversa M2
        for (int i = 0; i < M2.getPhase().length; i++) {
            M2_col[i] = M2.getPhase()[i][0];
            M2_col_inv[i] = -M2.getPhase()[i][0];
        }

        // Buscar matrices inversa M3
        for (int i = 0; i < M3.getPhase()[0].length; i++) {
            M3_row[i] = M3.getPhase()[0][i];
            M3_row_inv[i] = -M3.getPhase()[0][i];
        }

        // Buscar matrices inversa M4 COL
        for (int i = 0; i < M4.getPhase().length; i++) {
            M4_col[i] = M4.getPhase()[i][0];
            M4_col_inv[i] = -M4.getPhase()[i][0];
        }

        // Buscar matrices inversa M4 ROW
        for (int i = 0; i < M4.getPhase()[0].length; i++) {
            M4_row[i] = M4.getPhase()[0][i];
            M4_row_inv[i] = -M4.getPhase()[0][i];
        }

        // Join M1 and M2
        double[] current_col = new double[M1.getPhase().length];
        // Build current col by cubic spline
        double[] x = new double[M1.getPhase()[0].length];
        for (int i = 0; i < x.length; i++) {
            x[i] = i;
        }
        double x_val = (x.length);
        for (int i = 0; i < M1.getPhase().length; i++) {
            if (M1.getCoef() == null) {
                double[] y = M1.getPhase()[i];
                PolynomialSplineFunction psf = interp.interpolate(x, y);
                PolynomialFunction[] splines = psf.getPolynomials();
                PolynomialFunction last = splines[splines.length - 1];
                current_col[i] = last.getCoefficients()[3] * Math.pow(x_val - x[x.length - 2], 3) + last.getCoefficients()[2] * Math.pow(x_val - x[x.length - 2], 2) + last.getCoefficients()[1] * (x_val - x[x.length - 2]) + last.getCoefficients()[0];
            } else {
                current_col[i] = M1.Phase_Funtion(M1.getCoef(), x_val, i);
            }

        }
        double area = current_col.length;
        double DC1 = 0;
        double DC2 = 0;
        for (int i = 0; i < current_col.length; i++) {
            DC1 += (current_col[i] - M2_col[i]);
            DC2 += (current_col[i] - M2_col_inv[i]);
        }
        DC1 = DC1 / area;
        DC2 = DC2 / area;
        double RMS1 = 0;
        double RMS2 = 0;
        for (int i = 0; i < current_col.length; i++) {
            RMS1 += Math.pow((current_col[i] - M2_col[i]) - DC1, 2);
            RMS2 += Math.pow((current_col[i] - M2_col_inv[i]) - DC2, 2);
        }
        RMS1 = RMS1 / area;
        RMS2 = RMS2 / area;
        if (RMS1 < RMS2) {
            for (int i = 0; i < M1.getPhase().length; i++) {
                for (int j = M1.getPhase()[0].length; j < matrix[0].length; j++) {
                    matrix[i][j] = M2.getPhase()[i][j - M1.getPhase()[0].length] + DC1;
                }
            }

        } else {
            for (int i = 0; i < M1.getPhase().length; i++) {
                for (int j = M1.getPhase()[0].length; j < matrix[0].length; j++) {
                    matrix[i][j] = -M2.getPhase()[i][j - M1.getPhase()[0].length] + DC2;
                }
            }
        }
        /**
         * ***************** END *****************
         */

        // Join M1 and M3
        double[] current_row = new double[M1.getPhase()[0].length];
        // Build current col by cubic spline
        x = new double[M1.getPhase().length];
        for (int i = 0; i < x.length; i++) {
            x[i] = i;
        }
        x_val = (x.length);
        for (int i = 0; i < M1.getPhase()[0].length; i++) {
            // Crear y
            double[] y = new double[M1.getPhase().length];

            if (M1.getCoef() == null) {
                for (int j = 0; j < M1.getPhase().length; j++) {
                    y[j] = M1.getPhase()[j][i];
                }
                PolynomialSplineFunction psf = interp.interpolate(x, y);
                PolynomialFunction[] splines = psf.getPolynomials();
                PolynomialFunction last = splines[splines.length - 1];
                current_row[i] = last.getCoefficients()[3] * Math.pow(x_val - x[x.length - 2], 3) + last.getCoefficients()[2] * Math.pow(x_val - x[x.length - 2], 2) + last.getCoefficients()[1] * (x_val - x[x.length - 2]) + last.getCoefficients()[0];
            } else {
                current_row[i] = M1.Phase_Funtion(M1.getCoef(), i, x_val);
            }

        }
        area = current_row.length;
        DC1 = 0;
        DC2 = 0;
        for (int i = 0; i < current_row.length; i++) {
            DC1 += (current_row[i] - M3_row[i]);
            DC2 += (current_row[i] - M3_row_inv[i]);
        }
        DC1 = DC1 / area;
        DC2 = DC2 / area;
        RMS1 = 0;
        RMS2 = 0;
        for (int i = 0; i < current_row.length; i++) {
            RMS1 += Math.pow((current_row[i] - M3_row[i]) - DC1, 2);
            RMS2 += Math.pow((current_row[i] - M3_row_inv[i]) - DC2, 2);
        }
        RMS1 = RMS1 / area;
        RMS2 = RMS2 / area;
        if (RMS1 < RMS2) {
            for (int i = M1.getPhase().length; i < matrix.length; i++) {
                for (int j = 0; j < M1.getPhase()[0].length; j++) {
                    matrix[i][j] = M3.getPhase()[i - M1.getPhase().length][j] + DC1;
                }
            }

        } else {
            for (int i = M1.getPhase().length; i < matrix.length; i++) {
                for (int j = 0; j < M1.getPhase()[0].length; j++) {
                    matrix[i][j] = -M3.getPhase()[i - M1.getPhase().length][j] + DC2;
                }
            }
        }
        /**
         * ***************** END *****************
         */

        // Join matrix and M4
        current_row = new double[M2.getPhase()[0].length];
        current_col = new double[M3.getPhase().length];
        // Build current col by cubic spline
        double[] x1 = new double[M2.getPhase().length];
        double[] x2 = new double[M3.getPhase()[0].length];
        for (int i = 0; i < x1.length; i++) {
            x1[i] = i;
        }
        for (int i = 0; i < x2.length; i++) {
            x2[i] = i;
        }

        double x_val1 = x1.length;
        double x_val2 = x2.length;

        // Calcular los valores del spline para cada fila y columna
        for (int i = M1.getPhase()[0].length; i < matrix[0].length; i++) {
//            if (M2.getCoef() == null) {
            // Crear y
            double[] y = new double[M2.getPhase().length];
            for (int j = 0; j < M2.getPhase().length; j++) {
                y[j] = matrix[j][i];
            }
            PolynomialSplineFunction psf = interp.interpolate(x1, y);
            PolynomialFunction[] splines = psf.getPolynomials();
            PolynomialFunction last = splines[splines.length - 1];
            current_row[i - M1.getPhase()[0].length] = last.getCoefficients()[3] * Math.pow(x_val1 - x[x.length - 2], 3) + last.getCoefficients()[2] * Math.pow(x_val1 - x[x.length - 2], 2) + last.getCoefficients()[1] * (x_val1 - x[x.length - 2]) + last.getCoefficients()[0];
//            } else {
//                current_row[i - M1.getMatrix()[0].length] = Phase_Funtion(M2.getCoef(), i - M1.getMatrix()[0].length, x_val1);
//            }

        }

        // built column Spline Cubic
        for (int i = M1.getPhase().length; i < matrix.length; i++) {
//            if (M3.getCoef() == null) {
            double[] y = new double[M3.getPhase()[0].length];
            for (int j = 0; j < M3.getPhase()[0].length; j++) {
                y[j] = matrix[i][j];
            }

            PolynomialSplineFunction psf = interp.interpolate(x2, y);
            PolynomialFunction[] splines = psf.getPolynomials();
            PolynomialFunction last = splines[splines.length - 1];
            current_col[i - M1.getPhase().length] = last.getCoefficients()[3] * Math.pow(x_val - x[x.length - 2], 3) + last.getCoefficients()[2] * Math.pow(x_val - x[x.length - 2], 2) + last.getCoefficients()[1] * (x_val - x[x.length - 2]) + last.getCoefficients()[0];
//            } else {
//                current_col[i - M1.getMatrix().length] = Phase_Funtion(M3.getCoef(), x_val2, i - M1.getMatrix().length);
//            }

        }
//////////        // CALCULO DEL RMS SEPARADO EL KE MENOR TENGA ESE ES EL IDEAL (ENTRE CADA FILA Y CADA COLUM Y SUS INVERSAS)
//////////        double area_row = current_row.length;
//////////        double area_col = current_col.length;
//////////        double DC1_row = 0;
//////////        double DC2_row = 0;
//////////        for (int i = 0; i < current_row.length; i++) {
//////////            DC1_row += (current_row[i] - M4_row[i]);
//////////            DC2_row += (current_row[i] - M4_row_inv[i]);
//////////        }
//////////        double DC1_col = 0;
//////////        double DC2_col = 0;
//////////        for (int i = 0; i < current_col.length; i++) {
//////////            DC1_col += (current_col[i] - M4_col[i]);
//////////            DC2_col += (current_col[i] - M4_col_inv[i]);
//////////        }
//////////        DC1_row = DC1_row / area_row;
//////////        DC2_row = DC2_row / area_row;
//////////        DC1_col = DC1_col / area_col;
//////////        DC2_col = DC2_col / area_col;
//////////
//////////        double RMS1_row = 0;
//////////        double RMS2_row = 0;
//////////        double RMS1_col = 0;
//////////        double RMS2_col = 0;
//////////        for (int i = 0; i < current_row.length; i++) {
//////////            RMS1_row += Math.pow((current_row[i] - M4_row[i]) - DC1_row, 2);
//////////            RMS2_row += Math.pow((current_row[i] - M4_row_inv[i]) - DC2_row, 2);
//////////        }
//////////        for (int i = 0; i < current_col.length; i++) {
//////////            RMS1_col += Math.pow((current_col[i] - M4_col[i]) - DC1_col, 2);
//////////            RMS2_col += Math.pow((current_col[i] - M4_col_inv[i]) - DC2_col, 2);
//////////        }
//////////        RMS1_row = RMS1_row / area_row;
//////////        RMS2_row = RMS2_row / area_row;
//////////        RMS1_col = RMS1_col / area_col;
//////////        RMS2_col = RMS2_col / area_col;
//////////        if (RMS1_row < RMS2_row && RMS1_row < RMS1_col && RMS1_row < RMS2_col) {
//////////            for (int i = M1.getPhase().length; i < matrix.length; i++) {
//////////                for (int j = M1.getPhase()[0].length; j < matrix[0].length; j++) {
//////////                    matrix[i][j] = M4.getPhase()[i - M1.getPhase().length][j - M1.getPhase()[0].length] + DC1_row;
//////////                }
//////////            }
//////////        }
//////////        if (RMS1_col < RMS2_col && RMS1_col < RMS1_row && RMS1_col < RMS2_row) {
//////////            for (int i = M1.getPhase().length; i < matrix.length; i++) {
//////////                for (int j = M1.getPhase()[0].length; j < matrix[0].length; j++) {
//////////                    matrix[i][j] = M4.getPhase()[i - M1.getPhase().length][j - M1.getPhase()[0].length] + DC1_col;
//////////                }
//////////            }
//////////        }
//////////        if (RMS2_row < RMS1_row && RMS2_row < RMS1_col && RMS2_row < RMS2_col) {
//////////            for (int i = M1.getPhase().length; i < matrix.length; i++) {
//////////                for (int j = M1.getPhase()[0].length; j < matrix[0].length; j++) {
//////////                    matrix[i][j] = -M4.getPhase()[i - M1.getPhase().length][j - M1.getPhase()[0].length] + DC2_row;
//////////                }
//////////            }
//////////        }
//////////        if (RMS2_col < RMS1_col && RMS2_col < RMS1_row && RMS2_col < RMS2_row) {
//////////            for (int i = M1.getPhase().length; i < matrix.length; i++) {
//////////                for (int j = M1.getPhase()[0].length; j < matrix[0].length; j++) {
//////////                    matrix[i][j] = -M4.getPhase()[i - M1.getPhase().length][j - M1.getPhase()[0].length] + DC2_col;
//////////                }
//////////            }
//////////        }

        // Join M4
        area = current_row.length + current_col.length;
        DC1 = 0;
        DC2 = 0;
        for (int i = 0; i < current_row.length; i++) {
            DC1 += (current_row[i] - M4_row[i]);
            DC2 += (current_row[i] - M4_row_inv[i]);
        }
        for (int i = 0; i < current_col.length; i++) {
            DC1 += (current_col[i] - M4_col[i]);
            DC2 += (current_col[i] - M4_col_inv[i]);
        }
        DC1 = DC1 / area;
        DC2 = DC2 / area;
        RMS1 = 0;
        RMS2 = 0;
        for (int i = 0; i < current_row.length; i++) {
            RMS1 += Math.pow((current_row[i] - M4_row[i]) - DC1, 2);
            RMS2 += Math.pow((current_row[i] - M4_row_inv[i]) - DC2, 2);
        }
        for (int i = 0; i < current_col.length; i++) {
            RMS1 += Math.pow((current_col[i] - M4_col[i]) - DC1, 2);
            RMS2 += Math.pow((current_col[i] - M4_col_inv[i]) - DC2, 2);
        }
        RMS1 = RMS1 / area;
        RMS2 = RMS2 / area;
        if (RMS1 < RMS2) {
            for (int i = M1.getPhase().length; i < matrix.length; i++) {
                for (int j = M1.getPhase()[0].length; j < matrix[0].length; j++) {
                    matrix[i][j] = M4.getPhase()[i - M1.getPhase().length][j - M1.getPhase()[0].length] + DC1;
                }
            }

        } else {
            for (int i = M1.getPhase().length; i < matrix.length; i++) {
                for (int j = M1.getPhase()[0].length; j < matrix[0].length; j++) {
                    matrix[i][j] = -M4.getPhase()[i - M1.getPhase().length][j - M1.getPhase()[0].length] + DC2;
                }
            }
        }
        /**
         * ***************** END *****************
         */
        result = new Solution_Node(ID, matrix);
        return result;
    }

    /*
     * JOIN FASE ZOOM
     */
    public static Solution_Node Join_All_nodes_Zoom(ArrayList<Solution_Node> array_nodo, ArrayList<Solution_Node> zoom) {
        Collections.sort(array_nodo);
        Collections.sort(zoom);
        int idx;
        while (array_nodo.size() > 1) {
            idx = array_nodo.size();
            Solution_Node C1 = array_nodo.get(idx - 4);
            Solution_Node C2 = array_nodo.get(idx - 3);
            Solution_Node C3 = array_nodo.get(idx - 2);
            Solution_Node C4 = array_nodo.get(idx - 1);

            Solution_Node C1_zoom = zoom.get(idx - 4);
            Solution_Node C2_zoom = zoom.get(idx - 3);
            Solution_Node C3_zoom = zoom.get(idx - 2);
            Solution_Node C4_zoom = zoom.get(idx - 1);

            Solution_Node temp_zoom = Join_phase_Zoom(C1, C2, C3, C4, C1_zoom, C2_zoom, C3_zoom, C4_zoom);
            Solution_Node temp = Join_phase(C1, C2, C3, C4);

            array_nodo.remove(C1);
            array_nodo.remove(C2);
            array_nodo.remove(C3);
            array_nodo.remove(C4);
            array_nodo.add(temp);

            zoom.remove(C1_zoom);
            zoom.remove(C2_zoom);
            zoom.remove(C3_zoom);
            zoom.remove(C4_zoom);
            zoom.add(temp_zoom);

            Collections.sort(array_nodo);
            Collections.sort(zoom);
        }
        return zoom.get(0);

    }

    /**
     * Unir las fases con el mismo DC que la original
     */
    private static Solution_Node Join_phase_Zoom(Solution_Node M1, Solution_Node M2, Solution_Node M3, Solution_Node M4, Solution_Node M1_zoom, Solution_Node M2_zoom, Solution_Node M3_zoom, Solution_Node M4_zoom) {
        // ID para la proxima hoja
        String Id_str = String.valueOf(M1.getID());
        int ID;
        if (Id_str.length() > 1) {
            ID = Integer.parseInt(Id_str.substring(0, Id_str.length() - 1));
        } else {
            ID = 0;
        }

        // Spline Cubic
        SplineInterpolator interp = new SplineInterpolator();

        Solution_Node result = null;
        int row = M1_zoom.getPhase().length + M3_zoom.getPhase().length;
        int col = M1_zoom.getPhase()[0].length + M2_zoom.getPhase()[0].length;

        double[][] matrix_zoom = new double[row][col];
        double[][] matrix = new double[M1.getPhase().length + M3.getPhase().length][M1.getPhase()[0].length + M2.getPhase()[0].length];
        // Agrego la primera matriz ke se toma como referencia
        for (int i = 0; i < M1_zoom.getPhase().length; i++) {
            for (int j = 0; j < M1_zoom.getPhase()[0].length; j++) {
                matrix_zoom[i][j] = M1_zoom.getPhase()[i][j];
            }
        }
        for (int i = 0; i < M1.getPhase().length; i++) {
            for (int j = 0; j < M1.getPhase()[0].length; j++) {
                matrix[i][j] = M1.getPhase()[i][j];
            }
        }

        // Emparejar por columna M1 M2
        double[] M2_col = new double[M2.getPhase().length];
        double[] M2_col_inv = new double[M2.getPhase().length];
        double[] M3_row = new double[M3.getPhase()[0].length];
        double[] M3_row_inv = new double[M3.getPhase()[0].length];
        double[] M4_row = new double[M4.getPhase()[0].length];
        double[] M4_row_inv = new double[M4.getPhase()[0].length];
        double[] M4_col = new double[M4.getPhase().length];
        double[] M4_col_inv = new double[M4.getPhase().length];

        // Buscar matrices inversa M2
        for (int i = 0; i < M2.getPhase().length; i++) {
            M2_col[i] = M2.getPhase()[i][0];
            M2_col_inv[i] = -M2.getPhase()[i][0];
        }

        // Buscar matrices inversa M3
        for (int i = 0; i < M3.getPhase()[0].length; i++) {
            M3_row[i] = M3.getPhase()[0][i];
            M3_row_inv[i] = -M3.getPhase()[0][i];
        }

        // Buscar matrices inversa M4 COL
        for (int i = 0; i < M4.getPhase().length; i++) {
            M4_col[i] = M4.getPhase()[i][0];
            M4_col_inv[i] = -M4.getPhase()[i][0];
        }

        // Buscar matrices inversa M4 ROW
        for (int i = 0; i < M4.getPhase()[0].length; i++) {
            M4_row[i] = M4.getPhase()[0][i];
            M4_row_inv[i] = -M4.getPhase()[0][i];
        }

        // Join M1 and M2
        double[] current_col = new double[M1.getPhase().length];
        // Build current col by cubic spline
        double[] x = new double[M1.getPhase()[0].length];
        for (int i = 0; i < x.length; i++) {
            x[i] = i;
        }
        double x_val = (x.length);
        for (int i = 0; i < M1.getPhase().length; i++) {
            if (M1.getCoef() == null) {
                double[] y = M1.getPhase()[i];
                PolynomialSplineFunction psf = interp.interpolate(x, y);
                PolynomialFunction[] splines = psf.getPolynomials();
                PolynomialFunction last = splines[splines.length - 1];
                current_col[i] = last.getCoefficients()[3] * Math.pow(x_val - x[x.length - 2], 3) + last.getCoefficients()[2] * Math.pow(x_val - x[x.length - 2], 2) + last.getCoefficients()[1] * (x_val - x[x.length - 2]) + last.getCoefficients()[0];
            } else {
                current_col[i] = M1.Phase_Funtion(M1.getCoef(), x_val, i);
            }

        }
        double area = current_col.length;
        double DC1 = 0;
        double DC2 = 0;
        for (int i = 0; i < current_col.length; i++) {
            DC1 += (current_col[i] - M2_col[i]);
            DC2 += (current_col[i] - M2_col_inv[i]);
        }
        DC1 = DC1 / area;
        DC2 = DC2 / area;
        double RMS1 = 0;
        double RMS2 = 0;
        for (int i = 0; i < current_col.length; i++) {
            RMS1 += Math.pow((current_col[i] - M2_col[i]) - DC1, 2);
            RMS2 += Math.pow((current_col[i] - M2_col_inv[i]) - DC2, 2);
        }
        RMS1 = RMS1 / area;
        RMS2 = RMS2 / area;
        if (RMS1 < RMS2) {
            for (int i = 0; i < M1_zoom.getPhase().length; i++) {
                for (int j = M1_zoom.getPhase()[0].length; j < matrix_zoom[0].length; j++) {
                    matrix_zoom[i][j] = M2_zoom.getPhase()[i][j - M1_zoom.getPhase()[0].length] + DC1;
                }
            }
            for (int i = 0; i < M1.getPhase().length; i++) {
                for (int j = M1.getPhase()[0].length; j < matrix[0].length; j++) {
                    matrix[i][j] = M2.getPhase()[i][j - M1.getPhase()[0].length] + DC1;
                }
            }

        } else {
            for (int i = 0; i < M1_zoom.getPhase().length; i++) {
                for (int j = M1_zoom.getPhase()[0].length; j < matrix_zoom[0].length; j++) {
                    matrix_zoom[i][j] = -M2_zoom.getPhase()[i][j - M1_zoom.getPhase()[0].length] + DC2;
                }
            }
            for (int i = 0; i < M1.getPhase().length; i++) {
                for (int j = M1.getPhase()[0].length; j < matrix[0].length; j++) {
                    matrix[i][j] = -M2.getPhase()[i][j - M1.getPhase()[0].length] + DC2;
                }
            }
        }
        /**
         * ***************** END *****************
         */

        // Join M1 and M3
        double[] current_row = new double[M1.getPhase()[0].length];
        // Build current col by cubic spline
        x = new double[M1.getPhase().length];
        for (int i = 0; i < x.length; i++) {
            x[i] = i;
        }
        x_val = (x.length);
        for (int i = 0; i < M1.getPhase()[0].length; i++) {
            // Crear y
            double[] y = new double[M1.getPhase().length];

            if (M1.getCoef() == null) {
                for (int j = 0; j < M1.getPhase().length; j++) {
                    y[j] = M1.getPhase()[j][i];
                }
                PolynomialSplineFunction psf = interp.interpolate(x, y);
                PolynomialFunction[] splines = psf.getPolynomials();
                PolynomialFunction last = splines[splines.length - 1];
                current_row[i] = last.getCoefficients()[3] * Math.pow(x_val - x[x.length - 2], 3) + last.getCoefficients()[2] * Math.pow(x_val - x[x.length - 2], 2) + last.getCoefficients()[1] * (x_val - x[x.length - 2]) + last.getCoefficients()[0];
            } else {
                current_row[i] = M1.Phase_Funtion(M1.getCoef(), i, x_val);
            }

        }
        area = current_row.length;
        DC1 = 0;
        DC2 = 0;
        for (int i = 0; i < current_row.length; i++) {
            DC1 += (current_row[i] - M3_row[i]);
            DC2 += (current_row[i] - M3_row_inv[i]);
        }
        DC1 = DC1 / area;
        DC2 = DC2 / area;
        RMS1 = 0;
        RMS2 = 0;
        for (int i = 0; i < current_row.length; i++) {
            RMS1 += Math.pow((current_row[i] - M3_row[i]) - DC1, 2);
            RMS2 += Math.pow((current_row[i] - M3_row_inv[i]) - DC2, 2);
        }
        RMS1 = RMS1 / area;
        RMS2 = RMS2 / area;
        if (RMS1 < RMS2) {
            for (int i = M1_zoom.getPhase().length; i < matrix_zoom.length; i++) {
                for (int j = 0; j < M1_zoom.getPhase()[0].length; j++) {
                    matrix_zoom[i][j] = M3_zoom.getPhase()[i - M1_zoom.getPhase().length][j] + DC1;
                }
            }
            for (int i = M1.getPhase().length; i < matrix.length; i++) {
                for (int j = 0; j < M1.getPhase()[0].length; j++) {
                    matrix[i][j] = M3.getPhase()[i - M1.getPhase().length][j] + DC1;
                }
            }

        } else {
            for (int i = M1_zoom.getPhase().length; i < matrix_zoom.length; i++) {
                for (int j = 0; j < M1_zoom.getPhase()[0].length; j++) {
                    matrix_zoom[i][j] = -M3_zoom.getPhase()[i - M1_zoom.getPhase().length][j] + DC2;
                }
            }
            for (int i = M1.getPhase().length; i < matrix.length; i++) {
                for (int j = 0; j < M1.getPhase()[0].length; j++) {
                    matrix[i][j] = -M3.getPhase()[i - M1.getPhase().length][j] + DC2;
                }
            }
        }
        /**
         * ***************** END *****************
         */

        // Join matrix and M4
        current_row = new double[M2.getPhase()[0].length];
        current_col = new double[M3.getPhase().length];
        // Build current col by cubic spline
        double[] x1 = new double[M2.getPhase().length];
        double[] x2 = new double[M3.getPhase()[0].length];
        for (int i = 0; i < x1.length; i++) {
            x1[i] = i;
        }
        for (int i = 0; i < x2.length; i++) {
            x2[i] = i;
        }

        double x_val1 = x1.length;
        double x_val2 = x2.length;

        // Calcular los valores del spline para cada fila y columna
        for (int i = M1.getPhase()[0].length; i < matrix[0].length; i++) {
//            if (M2.getCoef() == null) {
            // Crear y
            double[] y = new double[M2.getPhase().length];
            for (int j = 0; j < M2.getPhase().length; j++) {
                y[j] = matrix[j][i];
            }
            PolynomialSplineFunction psf = interp.interpolate(x1, y);
            PolynomialFunction[] splines = psf.getPolynomials();
            PolynomialFunction last = splines[splines.length - 1];
            current_row[i - M1.getPhase()[0].length] = last.getCoefficients()[3] * Math.pow(x_val1 - x[x.length - 2], 3) + last.getCoefficients()[2] * Math.pow(x_val1 - x[x.length - 2], 2) + last.getCoefficients()[1] * (x_val1 - x[x.length - 2]) + last.getCoefficients()[0];
//            } else {
//                current_row[i - M1.getMatrix()[0].length] = Phase_Funtion(M2.getCoef(), i - M1.getMatrix()[0].length, x_val1);
//            }

        }

        // built column Spline Cubic
        for (int i = M1.getPhase().length; i < matrix.length; i++) {
//            if (M3.getCoef() == null) {
            double[] y = new double[M3.getPhase()[0].length];
            for (int j = 0; j < M3.getPhase()[0].length; j++) {
                y[j] = matrix[i][j];
            }

            PolynomialSplineFunction psf = interp.interpolate(x2, y);
            PolynomialFunction[] splines = psf.getPolynomials();
            PolynomialFunction last = splines[splines.length - 1];
            current_col[i - M1.getPhase().length] = last.getCoefficients()[3] * Math.pow(x_val - x[x.length - 2], 3) + last.getCoefficients()[2] * Math.pow(x_val - x[x.length - 2], 2) + last.getCoefficients()[1] * (x_val - x[x.length - 2]) + last.getCoefficients()[0];
//            } else {
//                current_col[i - M1.getMatrix().length] = Phase_Funtion(M3.getCoef(), x_val2, i - M1.getMatrix().length);
//            }

        }
//////////        // CALCULO DEL RMS SEPARADO EL KE MENOR TENGA ESE ES EL IDEAL (ENTRE CADA FILA Y CADA COLUM Y SUS INVERSAS)
//////////        double area_row = current_row.length;
//////////        double area_col = current_col.length;
//////////        double DC1_row = 0;
//////////        double DC2_row = 0;
//////////        for (int i = 0; i < current_row.length; i++) {
//////////            DC1_row += (current_row[i] - M4_row[i]);
//////////            DC2_row += (current_row[i] - M4_row_inv[i]);
//////////        }
//////////        double DC1_col = 0;
//////////        double DC2_col = 0;
//////////        for (int i = 0; i < current_col.length; i++) {
//////////            DC1_col += (current_col[i] - M4_col[i]);
//////////            DC2_col += (current_col[i] - M4_col_inv[i]);
//////////        }
//////////        DC1_row = DC1_row / area_row;
//////////        DC2_row = DC2_row / area_row;
//////////        DC1_col = DC1_col / area_col;
//////////        DC2_col = DC2_col / area_col;
//////////
//////////        double RMS1_row = 0;
//////////        double RMS2_row = 0;
//////////        double RMS1_col = 0;
//////////        double RMS2_col = 0;
//////////        for (int i = 0; i < current_row.length; i++) {
//////////            RMS1_row += Math.pow((current_row[i] - M4_row[i]) - DC1_row, 2);
//////////            RMS2_row += Math.pow((current_row[i] - M4_row_inv[i]) - DC2_row, 2);
//////////        }
//////////        for (int i = 0; i < current_col.length; i++) {
//////////            RMS1_col += Math.pow((current_col[i] - M4_col[i]) - DC1_col, 2);
//////////            RMS2_col += Math.pow((current_col[i] - M4_col_inv[i]) - DC2_col, 2);
//////////        }
//////////        RMS1_row = RMS1_row / area_row;
//////////        RMS2_row = RMS2_row / area_row;
//////////        RMS1_col = RMS1_col / area_col;
//////////        RMS2_col = RMS2_col / area_col;
//////////        if (RMS1_row < RMS2_row && RMS1_row < RMS1_col && RMS1_row < RMS2_col) {
//////////            for (int i = M1.getPhase().length; i < matrix.length; i++) {
//////////                for (int j = M1.getPhase()[0].length; j < matrix[0].length; j++) {
//////////                    matrix[i][j] = M4.getPhase()[i - M1.getPhase().length][j - M1.getPhase()[0].length] + DC1_row;
//////////                }
//////////            }
//////////        }
//////////        if (RMS1_col < RMS2_col && RMS1_col < RMS1_row && RMS1_col < RMS2_row) {
//////////            for (int i = M1.getPhase().length; i < matrix.length; i++) {
//////////                for (int j = M1.getPhase()[0].length; j < matrix[0].length; j++) {
//////////                    matrix[i][j] = M4.getPhase()[i - M1.getPhase().length][j - M1.getPhase()[0].length] + DC1_col;
//////////                }
//////////            }
//////////        }
//////////        if (RMS2_row < RMS1_row && RMS2_row < RMS1_col && RMS2_row < RMS2_col) {
//////////            for (int i = M1.getPhase().length; i < matrix.length; i++) {
//////////                for (int j = M1.getPhase()[0].length; j < matrix[0].length; j++) {
//////////                    matrix[i][j] = -M4.getPhase()[i - M1.getPhase().length][j - M1.getPhase()[0].length] + DC2_row;
//////////                }
//////////            }
//////////        }
//////////        if (RMS2_col < RMS1_col && RMS2_col < RMS1_row && RMS2_col < RMS2_row) {
//////////            for (int i = M1.getPhase().length; i < matrix.length; i++) {
//////////                for (int j = M1.getPhase()[0].length; j < matrix[0].length; j++) {
//////////                    matrix[i][j] = -M4.getPhase()[i - M1.getPhase().length][j - M1.getPhase()[0].length] + DC2_col;
//////////                }
//////////            }
//////////        }

        // Join M4
        area = current_row.length + current_col.length;
        DC1 = 0;
        DC2 = 0;
        for (int i = 0; i < current_row.length; i++) {
            DC1 += (current_row[i] - M4_row[i]);
            DC2 += (current_row[i] - M4_row_inv[i]);
        }
        for (int i = 0; i < current_col.length; i++) {
            DC1 += (current_col[i] - M4_col[i]);
            DC2 += (current_col[i] - M4_col_inv[i]);
        }
        DC1 = DC1 / area;
        DC2 = DC2 / area;
        RMS1 = 0;
        RMS2 = 0;
        for (int i = 0; i < current_row.length; i++) {
            RMS1 += Math.pow((current_row[i] - M4_row[i]) - DC1, 2);
            RMS2 += Math.pow((current_row[i] - M4_row_inv[i]) - DC2, 2);
        }
        for (int i = 0; i < current_col.length; i++) {
            RMS1 += Math.pow((current_col[i] - M4_col[i]) - DC1, 2);
            RMS2 += Math.pow((current_col[i] - M4_col_inv[i]) - DC2, 2);
        }
        RMS1 = RMS1 / area;
        RMS2 = RMS2 / area;
        if (RMS1 < RMS2) {
            for (int i = M1_zoom.getPhase().length; i < matrix_zoom.length; i++) {
                for (int j = M1_zoom.getPhase()[0].length; j < matrix_zoom[0].length; j++) {
                    matrix_zoom[i][j] = M4_zoom.getPhase()[i - M1_zoom.getPhase().length][j - M1_zoom.getPhase()[0].length] + DC1;
                }
            }
            for (int i = M1.getPhase().length; i < matrix.length; i++) {
                for (int j = M1.getPhase()[0].length; j < matrix[0].length; j++) {
                    matrix[i][j] = M4.getPhase()[i - M1.getPhase().length][j - M1.getPhase()[0].length] + DC1;
                }
            }

        } else {
            for (int i = M1_zoom.getPhase().length; i < matrix_zoom.length; i++) {
                for (int j = M1_zoom.getPhase()[0].length; j < matrix_zoom[0].length; j++) {
                    matrix_zoom[i][j] = -M4_zoom.getPhase()[i - M1_zoom.getPhase().length][j - M1_zoom.getPhase()[0].length] + DC2;
                }
            }
            for (int i = M1.getPhase().length; i < matrix.length; i++) {
                for (int j = M1.getPhase()[0].length; j < matrix[0].length; j++) {
                    matrix[i][j] = -M4.getPhase()[i - M1.getPhase().length][j - M1.getPhase()[0].length] + DC2;
                }
            }
        }
        /**
         * ***************** END *****************
         */
        result = new Solution_Node(ID, matrix_zoom);
        return result;
    }

    public double[] rango(double xi, double xn, double d) {
        int n = (int) ((xn - xi) / d);
        double[] r = new double[n];
        for (int i = 0; i < n; i++) {
            r[i] = xi + d * i;
        }
        return r;
    }

    public void borrarDirectorio(File directorio) {
        File[] ficheros = directorio.listFiles();

        for (int x = 0; x < ficheros.length; x++) {
            if (ficheros[x].isDirectory()) {
                borrarDirectorio(ficheros[x]);
            }
            ficheros[x].delete();

        }
    }

    public void borrarDirectorio_Seleted(File directorio, ArrayList<String> selected) {
        File[] ficheros = directorio.listFiles();

        for (int x = 0; x < ficheros.length; x++) {
            if (ficheros[x].isDirectory()) {
                borrarDirectorio(ficheros[x]);
            }
            String file_name = ficheros[x].getName().substring(0, ficheros[x].getName().length() - 4);
            if (selected.contains(file_name)) {
                ficheros[x].delete();
            }
        }
    }
    /*
    * Imresize  de Image 
    */

    public static My_Image Amplificar(BufferedImage phase, int rows, int cols, double ordenX, double ordenY) {

        BufferedImage scaledImage = new BufferedImage(cols, rows, BufferedImage.TYPE_BYTE_GRAY);
        final AffineTransform at = AffineTransform.getScaleInstance(ordenX, ordenY);
        final AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        scaledImage = ato.filter(phase, scaledImage);
        My_Image temp = new My_Image(scaledImage);
        temp.Update();
        return temp;
    }

    public My_Image Convolucion(My_Image Img, double[][] kernel) {

        int r = Img.getImage().getHeight();
        int c = Img.getImage().getWidth();

        int N = kernel.length;
        int K = Math.round(N / 2);

//        double matrix[][] = new double[Img.getImage().getHeight()][Img.getImage().getWidth()];
        My_Image tmp = (My_Image) Img.clone();
        double matrix[][] = tmp.getPhase();

        for (int i = K; i <= (r - K - 1); i++) {
            for (int j = K; j <= (c - K - 1); j++) {
                matrix[i][j] = 0;
//                System.out.println("i, j" + i + "  " + j);
                // For para la convolucion
                for (int f = 0; f <= N - 1; f++) {
                    for (int t = 0; t <= N - 1; t++) {
                        matrix[i][j] += Img.getPhase()[i - K + f][j - K + t] * kernel[f][t];
//                        System.out.print((i - K + f) + " " + (j - K + t) + ";");
                    }
                }
//                System.out.println("");
            }
        }
        // Construyo el interferograma recuperado
        int[][] Interferogram = new int[Img.getImage().getHeight()][Img.getImage().getWidth()];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                Interferogram[i][j] = (int) (127 - 128 * Math.cos(matrix[i][j]));
            }
        }

        BufferedImage img_tmp = new BufferedImage(Img.getImage().getWidth(), Img.getImage().getHeight(), Img.getImage().getType());
        My_Image temp = new My_Image(img_tmp, Interferogram, matrix);
//        temp.Update();
//        temp.setPhase(matrix);
        return temp;
    }

    private void Generar_Temperatura() {
        int method_selected = jComboBox_Temperature.getSelectedIndex();

        // Exponencial
        if (method_selected == 0) {
            text_temperatura_final.setEnabled(true);

            text_alpha.setEnabled(false);

            text_K.setEnabled(false);
            if (!text_iteraciones.getText().isEmpty() && !text_temperatura.getText().isEmpty() && !text_temperatura_final.getText().isEmpty()) {
                int N = Integer.parseInt(text_iteraciones.getText());
                double T0 = Double.parseDouble(text_temperatura.getText());
                double Tf = Double.parseDouble(text_temperatura_final.getText());

                Temperature = new double[N];
                double[] x = rango(0, N - 1, 1);
                double alpha = 0.000001;

                // TEmepartura Function
                for (int i = 0; i < N; i++) {
                    double argumento = 100 * Tf / T0;
                    double beta = Math.log(argumento) / N + N * alpha;
                    double citma = Math.log(T0);
                    Temperature[i] = Math.exp(-alpha * i * i + beta * i + citma);
                }
                Graficas migrafica = new Graficas("Temperature");
                migrafica.agregarGrafica("Iterations", x, Temperature);
                ChartPanel cp = migrafica.obtienePanel();
                jPanel_Temperature.removeAll();
                jPanel_Temperature.add(cp, BorderLayout.CENTER);
                jPanel_Temperature.validate();
            }
        }
        // Geometrica
        if (method_selected == 1) {
            text_alpha.setEnabled(true);
            text_temperatura_final.setEnabled(false);
            text_K.setEnabled(false);

            if (!text_iteraciones.getText().isEmpty() && !text_temperatura.getText().isEmpty() && !text_alpha.getText().isEmpty()) {
                int N = Integer.parseInt(text_iteraciones.getText());
                double T0 = Double.parseDouble(text_temperatura.getText());
                double alpha = Double.parseDouble(text_alpha.getText());

                Temperature = new double[N];
                double[] x = rango(0, N - 1, 1);
                Temperature[0] = T0;

                // TEmepartura Function
                for (int i = 1; i < N; i++) {
                    Temperature[i] = alpha * Temperature[i - 1];
                }
                Graficas migrafica = new Graficas("Temperature");
                migrafica.agregarGrafica("Iterations", x, Temperature);
                ChartPanel cp = migrafica.obtienePanel();
                jPanel_Temperature.removeAll();
                jPanel_Temperature.add(cp, BorderLayout.CENTER);
                jPanel_Temperature.validate();
            }
        }
        // Toledo&Cuevas
        if (method_selected == 2) {
            text_K.setEnabled(true);
            text_temperatura_final.setEnabled(false);
            text_alpha.setEnabled(false);

            if (!text_iteraciones.getText().isEmpty() && !text_temperatura.getText().isEmpty() && !text_K.getText().isEmpty()) {
                int N = Integer.parseInt(text_iteraciones.getText());
                double T0 = Double.parseDouble(text_temperatura.getText());
                double K = Double.parseDouble(text_K.getText());

                Temperature = new double[N];
                double[] x = rango(0, N - 1, 1);

                // TEmepartura Function
                for (int i = 0; i < N; i++) {
                    Temperature[i] = T0 * Math.exp(-i / K);
                }
                Graficas migrafica = new Graficas("Temperature");
                migrafica.agregarGrafica("Iterations", x, Temperature);
                ChartPanel cp = migrafica.obtienePanel();
                jPanel_Temperature.removeAll();
                jPanel_Temperature.add(cp, BorderLayout.CENTER);
                jPanel_Temperature.validate();
            }

        }
        // Lundy & Mees
        if (method_selected == 3) {
            text_temperatura_final.setEnabled(true);

            text_alpha.setEnabled(false);
            text_K.setEnabled(false);

            if (!text_iteraciones.getText().isEmpty() && !text_temperatura.getText().isEmpty() && !text_temperatura_final.getText().isEmpty()) {
                int N = Integer.parseInt(text_iteraciones.getText());
                double T0 = Double.parseDouble(text_temperatura.getText());
                double Tf = Double.parseDouble(text_temperatura_final.getText());

                Temperature = new double[N];
                double[] x = rango(0, N - 1, 1);
                Temperature[0] = T0;
                double beta = (T0 - Tf) / (N * T0 * Tf);

                // TEmepartura Function
                for (int i = 1; i < N; i++) {
                    Temperature[i] = Temperature[i - 1] / (1 + beta * Temperature[i - 1]);
                }
                Graficas migrafica = new Graficas("Temperature");
                migrafica.agregarGrafica("Iterations", x, Temperature);
                ChartPanel cp = migrafica.obtienePanel();
                jPanel_Temperature.removeAll();
                jPanel_Temperature.add(cp, BorderLayout.CENTER);
                jPanel_Temperature.validate();
            }

        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane Main_TPanel;
    private javax.swing.JPanel ag_panel;
    private javax.swing.JButton btn_aceptar_windows_fringes;
    private javax.swing.JButton btn_anadir;
    private javax.swing.JButton btn_aplicar_windows_fringes;
    private javax.swing.JButton btn_graficar_fitness;
    private javax.swing.JButton btn_run_indep;
    private javax.swing.JButton btn_simular;
    private javax.swing.JButton btn_simular_temperatura;
    private javax.swing.JLabel cio_logo;
    private javax.swing.JPanel inicio_panel;
    private javax.swing.JButton jButton_open_file;
    private javax.swing.JComboBox jComboBox_2_corrida;
    private javax.swing.JComboBox jComboBox_Fase1;
    private javax.swing.JComboBox jComboBox_Filtros;
    private javax.swing.JComboBox jComboBox_Temperature;
    private javax.swing.JComboBox jComboBox_Vecindad;
    private javax.swing.JComboBox jComboBox_Windows;
    public static javax.swing.JInternalFrame jInternalFrame_fase;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_Generaciones;
    private javax.swing.JLabel jLabel_Image;
    private javax.swing.JLabel jLabel_Tf;
    private javax.swing.JLabel jLabel_Tf1;
    private javax.swing.JLabel jLabel_Tf2;
    private javax.swing.JLabel jLabel_grafica_fitness;
    private javax.swing.JLabel jLabel_grafica_temperature1;
    private javax.swing.JLabel jLabel_image_recuperada;
    private javax.swing.JLabel jLabel_main_name;
    private javax.swing.JLabel jLabel_salida;
    private javax.swing.JLabel jLabel_x_max;
    private javax.swing.JLabel jLabel_x_min;
    private javax.swing.JLabel jLabel_y_max;
    private javax.swing.JLabel jLabel_y_min;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel_Centro;
    private javax.swing.JPanel jPanel_Centro_left;
    private javax.swing.JPanel jPanel_Centro_right;
    private javax.swing.JPanel jPanel_Imagen;
    private javax.swing.JPanel jPanel_Inicio_Sup;
    private javax.swing.JPanel jPanel_PDI;
    private javax.swing.JPanel jPanel_Parametros;
    private javax.swing.JPanel jPanel_Simulacion;
    private javax.swing.JPanel jPanel_Temperatura;
    private javax.swing.JPanel jPanel_Temperature;
    private javax.swing.JPanel jPanel_btn_simular;
    private javax.swing.JPanel jPanel_estadisticas;
    private javax.swing.JPanel jPanel_grafica_fitness;
    private javax.swing.JPanel jPanel_inicio_Inferior;
    private javax.swing.JPanel jPanel_resultados;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinner_Kvecinos;
    private javax.swing.JSpinner jSpinner_Min;
    private javax.swing.JSpinner jSpinner_max;
    public static javax.swing.JTextArea jTextArea_Salida;
    private javax.swing.JTextField jTextField_Columna;
    private javax.swing.JTextField jTextField_Fila;
    private javax.swing.JTextField jTextField_funcion;
    private javax.swing.JLabel label_rs_imagen_original;
    private javax.swing.JButton rs_Restaurar;
    private javax.swing.JButton rs_ejecutar;
    private javax.swing.JButton rs_filtros1;
    private javax.swing.JButton rs_filtros3;
    private javax.swing.JPanel rs_panel;
    private javax.swing.JTextField text_Franjas;
    private javax.swing.JTextField text_K;
    private javax.swing.JTextField text_Tolerancia;
    private javax.swing.JTextField text_alpha;
    private javax.swing.JTextField text_factorV_Final;
    private javax.swing.JTextField text_factorV_Inicial;
    private javax.swing.JTextField text_factorV_fijo;
    private javax.swing.JTextField text_factor_delta;
    private javax.swing.JTextField text_generaciones;
    private javax.swing.JTextField text_iteraciones;
    private javax.swing.JTextField text_poly_orden;
    private javax.swing.JTextField text_resolution_scsale;
    private javax.swing.JTextField text_suavidad;
    private javax.swing.JTextField text_temperatura;
    private javax.swing.JTextField text_temperatura_final;
    public static javax.swing.ButtonGroup vecindad_group;
    private javax.swing.JPanel vmo_panel;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the tree
     */
    public Arbolphsh<WindowsFringes> getTree() {
        return tree;
    }
}
