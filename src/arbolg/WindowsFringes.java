/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arbolg;

/**
 *
 * @author Iosvani
 */
public class WindowsFringes {
    private String name;
    private double[][] windows;
    
    public WindowsFringes(){
        this.name = null;
        windows = null;
    }
     public WindowsFringes(String name, double[][] windows){
         this.name = name;
         this.windows = windows;
     }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the windows
     */
    public double[][] getWindows() {
        return windows;
    }

    /**
     * @param windows the windows to set
     */
    public void setWindows(double[][] windows) {
        this.windows = windows;
    }
    
}
