/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author USUARIO
 */
public class Bomba {

    private int fila;
    private int columna;
    private Jugador propietario;
    private long tiempoColocacion;
    private int rangoExplosion;

    public Bomba(int fila, int columna, Jugador propietario){

        this.fila = fila;
        this.columna = columna;
        this.propietario = propietario;
        this.rangoExplosion = propietario.getRangoExplosion();
        tiempoColocacion = System.currentTimeMillis();
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }
    public long getTiempoColocacion() {
        return tiempoColocacion;
    }

    public Jugador getPropietario() {
        return propietario;
    }

    public int getRangoExplosion() {
        return rangoExplosion;
    }
}
