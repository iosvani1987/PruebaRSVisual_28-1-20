/*
 * Nodo.java
 *
 * Created on October 28, 2009, 8:10 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package arbolg;
/**
 *

 */
public class Nodo <E> {
  private E info;
//  private Nodo<E> padre;
  private Nodo<E> pH;
  private Nodo<E> sH;
  
  public Nodo ( E newinfo){
    info=newinfo;
    pH=null;
    sH=null;
  }   
  public Nodo ( E newinfo, Nodo<E> padre){
    info=newinfo;
    pH=null;
    sH=null;
//    this.padre = padre;
  }  

  public E getInfo() {  
        return info;
  }

  public void setInfo(E info) {
        this.info = info;
  }

  public Nodo<E> getPH() {
        return pH;
  }

  public void setPH(Nodo<E> pH) {
        this.pH = pH;
  }

  public Nodo<E> getSH() {
        return sH;
  }

  public void setSH(Nodo<E> sH) {
        this.sH = sH;
 }

}
