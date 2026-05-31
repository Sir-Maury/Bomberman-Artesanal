/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

public class Enemigo {

    private Posicion posicion;
    private boolean vivo;
    private long ultimoMovimiento;

    public Enemigo(int x, int y) {

        posicion = new Posicion(x, y);
        vivo = true;
        ultimoMovimiento = 0;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public int getFila() {
        return posicion.getY() / 50;
    }

    public int getColumna() {
        return posicion.getX() / 50;
    }

    public boolean isVivo() {
        return vivo;
    }

    public void eliminar() {
        vivo = false;
    }

    public long getUltimoMovimiento() {
        return ultimoMovimiento;
    }

    public void setUltimoMovimiento(long ultimoMovimiento) {
        this.ultimoMovimiento = ultimoMovimiento;
    }
}
