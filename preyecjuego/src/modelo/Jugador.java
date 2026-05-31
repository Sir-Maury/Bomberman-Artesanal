/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import excepciones.MovimientoInvalidoException;
import interfaces.Movible;

public class Jugador implements Movible {

    private String nombre;
    private Posicion posicion;
    private final int TAM_CASILLA;
    private boolean vivo;
    private long ultimoTiempoBomba;
    private int rangoExplosion;
    private int mejorasBombas;
    private long escudoHasta;
    

    public Jugador() {
        this("Jugador", 50, 50);
    }

    public Jugador(int x, int y) {
        this("Jugador", x, y);
    }

    public Jugador(String nombre, int x, int y) {

        this.nombre = nombre;
        posicion = new Posicion(x, y);
        TAM_CASILLA = 50;
        vivo = true;
        ultimoTiempoBomba = 0;
        rangoExplosion = 1;
        mejorasBombas = 0;
        escudoHasta = 0;
    }

    @Override
    public void moverArriba() throws MovimientoInvalidoException {

        if(posicion.getY() - TAM_CASILLA < 0)
        throw new MovimientoInvalidoException(
                "Limite superior");

       posicion.setY(posicion.getY() - TAM_CASILLA);
    }

    @Override
    public void moverAbajo() {

        posicion.setY(posicion.getY() + TAM_CASILLA);
    }

    @Override
    public void moverIzquierda() throws MovimientoInvalidoException {

        if(posicion.getX() - TAM_CASILLA < 0)
        throw new MovimientoInvalidoException(
                "Limite izquierdo");

       posicion.setX(posicion.getX() - TAM_CASILLA);
    }

    @Override
    public void moverDerecha() {

        posicion.setX(posicion.getX() + TAM_CASILLA);
    }

    public Posicion getPosicion() {

        return posicion;
    }

    public String getNombre() {
        return nombre;
    }

    public int getFila() {
        return posicion.getY() / TAM_CASILLA;
    }

    public int getColumna() {
        return posicion.getX() / TAM_CASILLA;
    }

    public boolean isVivo() {
        return vivo;
    }

    public void eliminar() {
        vivo = false;
    }

    public long getUltimoTiempoBomba() {
        return ultimoTiempoBomba;
    }

    public void setUltimoTiempoBomba(long ultimoTiempoBomba) {
        this.ultimoTiempoBomba = ultimoTiempoBomba;
    }

    public int getRangoExplosion() {
        return rangoExplosion;
    }

    public void aumentarRangoExplosion() {
        rangoExplosion++;
    }

    public void mejorarBombas() {
        mejorasBombas++;
    }

    public int getTiempoEntreBombas() {

        int tiempo = 800 - mejorasBombas * 150;

        if(tiempo < 200) {
            return 200;
        }

        return tiempo;
    }

    public int getMejorasBombas() {
        return mejorasBombas;
    }

    public void activarEscudo(long duracion) {
        escudoHasta = System.currentTimeMillis() + duracion;
    }

    public boolean tieneEscudoActivo() {
        return System.currentTimeMillis() < escudoHasta;
    }

    public long getTiempoEscudoRestante() {

        long restante = escudoHasta - System.currentTimeMillis();

        if(restante < 0) {
            return 0;
        }

        return restante;
    }
}
    

