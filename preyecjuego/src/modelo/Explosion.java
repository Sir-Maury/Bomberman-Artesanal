/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Explosion {

    private List<Posicion> celdasAfectadas;
    private long tiempoCreacion;
    private long duracion;

    public Explosion(List<Posicion> celdasAfectadas, long duracion) {

        this.celdasAfectadas = new ArrayList<>(celdasAfectadas);
        this.tiempoCreacion = System.currentTimeMillis();
        this.duracion = duracion;
    }

    public List<Posicion> getCeldasAfectadas() {
        return Collections.unmodifiableList(celdasAfectadas);
    }

    public boolean estaActiva() {
        return System.currentTimeMillis() - tiempoCreacion < duracion;
    }

    public boolean afectaCelda(int fila, int columna) {

        for(Posicion celda : celdasAfectadas) {
            if(celda.getY() == fila && celda.getX() == columna) {
                return true;
            }
        }

        return false;
    }
}
