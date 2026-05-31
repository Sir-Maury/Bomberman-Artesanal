/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

import excepciones.MovimientoInvalidoException;

/**
 *
 * @author USUARIO
 */
public interface Movible {
    void moverArriba() throws MovimientoInvalidoException;

    void moverAbajo() throws MovimientoInvalidoException;

    void moverIzquierda() throws MovimientoInvalidoException;

    void moverDerecha() throws MovimientoInvalidoException;
}
