package Main_Project;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.swing.JPanel;

public class Graficas {

    JFreeChart grafica;
    XYSeriesCollection datos = new XYSeriesCollection();

    String titulo, tx, ty;

    public Graficas(String titulo) {
        this.titulo = titulo;
        grafica = ChartFactory.createXYLineChart(titulo, tx, ty, datos, PlotOrientation.VERTICAL, true, true, true);
    }

    public void agregarGrafica(String id, double[] x, double[] y) {
        XYSeries s = new XYSeries(id);
        int n = x.length;
        for (int i = 0; i < n; i++) {
            s.add(x[i], y[i]);
        }
        datos.addSeries(s);
    }

    public ChartPanel obtienePanel() {
        return new ChartPanel(grafica);
    }
}
