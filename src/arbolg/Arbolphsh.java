/*
 * Arbolphsh.java
 *
 * Created on October 28, 2009, 8:07 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package arbolg;

import Exceptions.RaizException;
import Exceptions.NodoNuloException;
import Exceptions.NodoNoEncontrado;
import Exceptions.ArbolVacioException;
import java.util.ArrayList;

/**
 *
 * @author
 */
public class Arbolphsh<E> implements Arbol<E> {

    private Nodo<E> raiz;
    private ArrayList<String> names_nodos = new ArrayList<>();
    private int Num_hojas;

    /**
     * Creates a new instance of Arbolphsh
     */
    public Arbolphsh() {// crea un arbol vacio
        raiz = null;
    }

    public Arbolphsh(E datoRaiz) {// crea un arbol con un nodo, la raiz
        raiz = new Nodo<E>(datoRaiz);
    }

    public Arbolphsh(Nodo Raiz) {// crea un arbol con un nodo, la raiz
        raiz = Raiz;
    }

    public boolean esVacio() {
        return getRaiz() == null;
    }

    public void Vaciar() {
        raiz = null;
    }

    public E raiz() throws ArbolVacioException {
        return getRaiz().getInfo();
    }

    //devuelve el padre del elemento que esta como parametro
    public E padre(E elElemento) throws ArbolVacioException, NodoNoEncontrado {
        return null;
    }

//devuelve la informacion del primer hijo del elemento
    public E primerHijo(E elElemento) throws ArbolVacioException, NodoNoEncontrado {
        if (esVacio()) {
            throw new ArbolVacioException();
        } else {
            Nodo<E> actual = buscar(elElemento);
            if (actual == null || actual.getPH() == null) {
                throw new NodoNoEncontrado();
            }
            actual = actual.getPH();
            return actual.getInfo();
        }
    }

    //devuelve la informacion del siguiente hermano del elemento
    public E siguienteHermano(E elElemento) throws ArbolVacioException, NodoNoEncontrado {
        if (esVacio()) {
            throw new ArbolVacioException();
        } else // el arbol no es vacio, hay que buscar cual es el nodo que contiene el elElemento
        {
            Nodo<E> actual = buscar(elElemento);
            if (actual == null || actual.getSH() == null) {
                throw new NodoNoEncontrado();
            } else// encontro el nodo con informacion "elElemento"
            {
                return actual.getSH().getInfo();
            }
        }
    }

    public Nodo<E> buscar(E elemento) {
        return buscarR(getRaiz(), elemento);
    }

    private Nodo<E> buscarR(Nodo<E> r, E elemento) {
        if (r.getInfo() == elemento) {
            return r;
        }
        if (r.getPH() == null && r.getSH() == null) {
            return null;
        }
        if (r.getPH() != null) {
            Nodo<E> actual = buscarR(r.getPH(), elemento);
            if (actual != null) {
                return actual;
            }
        }
        if (r.getSH() != null) {
            Nodo<E> actual = buscarR(r.getSH(), elemento);
            return actual;
        }
        return null;
    }

    // inserta un nodo con informacion "elElemento" como hijo del nodo que tiene informacion "elPadre"
    public void insertaHijo(E elPadre, E elElemento) throws ArbolVacioException, NodoNoEncontrado {
        if (esVacio()) {
            throw new ArbolVacioException();
        } else // el arbol no es vacio
        {
            Nodo<E> actual = buscar(elPadre);//hay que buscar cual es el nodo que contiene al padre
            if (actual == null) {
                throw new NodoNoEncontrado();
            } else// encontro el nodo padre
            {
                Nodo<E> nuevo = new Nodo<E>(elElemento);//creo el nodo
                if (actual.getPH() == null) {// el padre no tenia hijos, se inserta como pH
                    actual.setPH(nuevo);
                } else {// el padre tiene al menos un hijo, se inserta como el ultimo de los hermanos en la lista de los siguientes hermanos
                    Nodo<E> temp = actual.getPH();
                    while (temp.getSH() != null) {
                        temp = temp.getSH();
                    }
                    temp.setSH(nuevo);
                }
            }
        }
    }

    // inserta un nodo con la informacion "elElemento" como siguiente hermano de el nodo que tiene informacion "hermano"
    public void insertaHermano(E hermano, E elElemento) throws ArbolVacioException, NodoNoEncontrado, RaizException {
        if (esVacio()) {
            throw new ArbolVacioException();
        } else // el arbol no es vacio, hay que buscar cual es el nodo que contiene el "hermano"
        {
            Nodo<E> actual = buscar(hermano);
            if (actual == null) {
                throw new NodoNoEncontrado();
            } else if (actual == getRaiz()) {
                throw new RaizException();//la raiz no puede tener hermanos.
            } else// encontro el nodo con informacion hermano
            {
                Nodo<E> nuevo = new Nodo<E>(elElemento);//creo el nodo y se pone como el ultimo de los hermanos insertados
                while (actual.getSH() != null) {
                    actual = actual.getSH();
                }
                actual.setSH(nuevo);
            }
        }
    }

    public boolean elimina(E x) throws ArbolVacioException, NodoNuloException {
        return false;
    }

    //recorridos
    public void preOrden() {
        preOrdenR(getRaiz());
    }

    private void preOrdenR(Nodo<E> nodo) {
        if (nodo != null) {
            System.out.print(nodo.getInfo());
            System.out.print(',');
            Nodo<E> ph = nodo.getPH();
            preOrdenR(ph);
            while (ph != null) {
                ph = ph.getSH();
                preOrdenR(ph);
            }
        }
    }

    public void postOrden() {
        Num_hojas = 0;
        postOrdenR(getRaiz());
    }

    private void postOrdenR(Nodo<E> nodo) {
        if (nodo != null) {
            Nodo<E> ph = nodo.getPH();
            postOrdenR(ph);
            while (ph != null) {
                ph = ph.getSH();
                postOrdenR(ph);
            }
//            names_nodos.add(nodo.getInfo().toString());
            System.out.print(nodo.getInfo());
            System.out.print(',');
        }else{
            Num_hojas ++;
        }
    }

    public void inOrden() {
        inOrdenR(getRaiz());
    }

    private void inOrdenR(Nodo<E> nodo) {
        if (nodo != null) {
            Nodo<E> ph = nodo.getPH();
            inOrdenR(ph);
            System.out.print(nodo.getInfo());
            System.out.print(',');
            while (ph != null) {
                ph = ph.getSH();
                inOrdenR(ph);
            }
        }
    }

    /**
     * @return the raiz
     */
    public Nodo<E> getRaiz() {
        return raiz;
    }

    /**
     * @return the Num_hojas
     */
    public int getNum_hojas() {
        return Num_hojas;
    }

}
