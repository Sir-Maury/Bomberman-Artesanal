/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

public class Mapa {

    private static final int FILAS = 13;
    private static final int COLUMNAS = 15;

    private final int[][] matriz;

    public Mapa() {

        matriz = new int[FILAS][COLUMNAS];
        construirMapaClasico();
    }

    private void construirMapaClasico() {

        for(int fila = 0; fila < FILAS; fila++) {
            for(int columna = 0; columna < COLUMNAS; columna++) {

                if(esBorde(fila, columna) || esPilarInterno(fila, columna)) {
                    matriz[fila][columna] = 1;
                } else {
                    matriz[fila][columna] = 0;
                }
            }
        }
    }

    private boolean esBorde(int fila, int columna) {
        return fila == 0
                || columna == 0
                || fila == FILAS - 1
                || columna == COLUMNAS - 1;
    }

    private boolean esPilarInterno(int fila, int columna) {
        return fila % 2 == 0 && columna % 2 == 0;
    }

    public int[][] getMatriz() {

        return matriz;
    }

    public int getFilas() {
        return matriz.length;
    }

    public int getColumnas() {
        return matriz[0].length;
    }

    public boolean estaDentro(int fila, int columna) {
        return fila >= 0
                && fila < getFilas()
                && columna >= 0
                && columna < getColumnas();
    }

    public boolean esMuro(int fila,int columna){

        if(!estaDentro(fila, columna)) {
            return true;
        }

        return matriz[fila][columna] == 1;
    }
}
