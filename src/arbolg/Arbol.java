/*
 * Arbol.java
 *
 * Created on October 27, 2009, 11:06 AM
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

/**
 *
 * @author 
 */
public interface Arbol <E>  {
     E raiz() throws ArbolVacioException;
     E padre(E elElemento) throws ArbolVacioException, NodoNoEncontrado;
     E primerHijo(E elEemento) throws ArbolVacioException, NodoNoEncontrado;
     E siguienteHermano(E elElemento) throws ArbolVacioException, NodoNoEncontrado;
     void insertaHijo(E elPadre, E elElemento) throws ArbolVacioException, NodoNoEncontrado;
     void insertaHermano( E hermano, E elElemento) throws ArbolVacioException,NodoNoEncontrado, RaizException;
     boolean elimina(E elElemento) throws ArbolVacioException, NodoNuloException;
     boolean esVacio();
     void Vaciar();
}
