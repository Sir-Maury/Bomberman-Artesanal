/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

public class PowerUp {

    private int fila;
    private int columna;
    private TipoPowerUp tipo;

    public PowerUp(int fila, int columna, TipoPowerUp tipo) {

        this.fila = fila;
        this.columna = columna;
        this.tipo = tipo;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public TipoPowerUp getTipo() {
        return tipo;
    }
}
