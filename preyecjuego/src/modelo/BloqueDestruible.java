/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import interfaces.Destruible;

public class BloqueDestruible implements Destruible {

    private int fila;
    private int columna;
    private boolean destruido;

    public BloqueDestruible(int fila,int columna){

        this.fila = fila;
        this.columna = columna;
        destruido = false;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    @Override
    public boolean isDestruido() {
        return destruido;
    }

    @Override
    public void destruir() {
        destruido = true;
    }
    
}
