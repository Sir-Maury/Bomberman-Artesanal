/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import modelo.BloqueDestruible;
import modelo.Bomba;
import modelo.Direccion;
import modelo.Enemigo;
import modelo.EstadoJuego;
import modelo.Explosion;
import modelo.Jugador;
import modelo.Mapa;
import modelo.Posicion;

public class ControlJuego {

    public static final int TAM_CASILLA = 50;
    private static final int RANGO_BOMBA = 1;
    private static final long TIEMPO_EXPLOSION = 1200;
    private static final double PROBABILIDAD_BLOQUE = 0.45;
    private static final int MAX_BOMBAS_ACTIVAS = 3;
    private static final long TIEMPO_ENTRE_BOMBAS = 800;
    private static final long TIEMPO_MOVIMIENTO_ENEMIGO = 600;
    private static final int DISTANCIA_PERSECUCION = 5;

    private Jugador jugador1;
    private Jugador jugador2;
    private List<BloqueDestruible> bloques;
    private Mapa mapa;
    private List<Bomba> bombas;
    private List<Explosion> explosiones;
    private List<Enemigo> enemigos;
    private EstadoJuego estadoJuego;
    private String mensajeFinal;
    private Random random;

    public ControlJuego() {

        random = new Random();
        estadoJuego = EstadoJuego.EN_MENU;
        inicializarPartida();
    }

    public void inicializarPartida() {

        jugador1 = new Jugador("Jugador 1", 50, 50);
        jugador2 = new Jugador("Jugador 2", 650, 550);
        bloques = new java.util.ArrayList<>();
        mapa = new Mapa();
        bombas = new java.util.ArrayList<>();
        explosiones = new java.util.ArrayList<>();
        enemigos = new java.util.ArrayList<>();
        mensajeFinal = "";

        generarBloquesDestruibles();
        generarEnemigos();
    }

    public Jugador getJugador() {

        return jugador1;
    }

    public Jugador getJugador1() {

        return jugador1;
    }

    public Jugador getJugador2() {

        return jugador2;
    }
    public List<BloqueDestruible> getBloques(){

        return bloques;
    }
    public Mapa getMapa() {

        return mapa;
    }
    public List<Bomba> getBombas(){

        return bombas;
    }

    public List<Explosion> getExplosiones(){

        return explosiones;
    }

    public List<Enemigo> getEnemigos(){

        return enemigos;
    }

    public boolean isJuegoTerminado() {
        return estadoJuego == EstadoJuego.FINALIZADO;
    }

    public boolean estaEnMenu() {
        return estadoJuego == EstadoJuego.EN_MENU;
    }

    public boolean estaJugando() {
        return estadoJuego == EstadoJuego.JUGANDO;
    }

    public String getMensajeFinal() {
        return mensajeFinal;
    }

    public void iniciarPartida() {

        if(estadoJuego == EstadoJuego.EN_MENU) {
            estadoJuego = EstadoJuego.JUGANDO;
        }
    }

    public void reiniciarPartida() {

        inicializarPartida();
        estadoJuego = EstadoJuego.JUGANDO;
    }

    public void actualizarJuego() {

        if(!estaJugando()) {
            return;
        }

        actualizarBombas();
        actualizarExplosiones();
        actualizarEnemigos();
        verificarImpactoExplosionesActivas();
    }

    public boolean moverJugador(Direccion direccion) {
        return moverJugador(jugador1, direccion);
    }

    public boolean moverJugador(Jugador jugador, Direccion direccion) {

        if(!estaJugando() || !jugador.isVivo()) {
            return false;
        }

        int nuevaX = jugador.getPosicion().getX();
        int nuevaY = jugador.getPosicion().getY();

        switch(direccion) {
            case ARRIBA -> nuevaY -= TAM_CASILLA;
            case ABAJO -> nuevaY += TAM_CASILLA;
            case IZQUIERDA -> nuevaX -= TAM_CASILLA;
            case DERECHA -> nuevaX += TAM_CASILLA;
        }

        if(!puedeMover(jugador, nuevaX, nuevaY)) {
            return false;
        }

        jugador.getPosicion().setX(nuevaX);
        jugador.getPosicion().setY(nuevaY);
        verificarImpactoExplosionesActivas();
        return true;
    }

    public void colocarBomba(){
        colocarBomba(jugador1);
    }

    public void colocarBomba(Jugador jugador){

        if(!estaJugando() || !jugador.isVivo()) {
            return;
        }

        long tiempoActual = System.currentTimeMillis();

        if(contarBombasActivas(jugador) >= MAX_BOMBAS_ACTIVAS
                || tiempoActual - jugador.getUltimoTiempoBomba() < TIEMPO_ENTRE_BOMBAS) {
            return;
        }

        int fila =
          jugador.getPosicion().getY()/TAM_CASILLA;

        int columna =
          jugador.getPosicion().getX()/TAM_CASILLA;

        if(existeBombaEn(fila, columna)) {
            return;
        }

        bombas.add(
           new Bomba(fila, columna, jugador)
        );

        jugador.setUltimoTiempoBomba(tiempoActual);
    }

    public boolean puedeMover(int nuevaX, int nuevaY){
        return puedeMover(jugador1, nuevaX, nuevaY);
    }

    public boolean puedeMover(Jugador jugador, int nuevaX, int nuevaY){

        int fila = nuevaY / TAM_CASILLA;
        int columna = nuevaX / TAM_CASILLA;

        if(mapa.esMuro(fila, columna)){
            return false;
        }

        if(existeBombaEn(fila, columna)) {
            return false;
        }

        if(existeOtroJugadorEn(jugador, fila, columna)) {
            return false;
        }

        for(BloqueDestruible b : bloques){

            if(!b.isDestruido()){

                if(b.getFila()==fila &&
                    b.getColumna()==columna){

                return false;
                }
            }
        }

        return true;
    }

    private boolean existeOtroJugadorEn(Jugador jugador, int fila, int columna) {

        Jugador otroJugador = jugador == jugador1 ? jugador2 : jugador1;

        return otroJugador.isVivo()
                && otroJugador.getFila() == fila
                && otroJugador.getColumna() == columna;
    }

    private int contarBombasActivas(Jugador jugador) {

        int total = 0;

        for(Bomba bomba : bombas) {
            if(bomba.getPropietario() == jugador) {
                total++;
            }
        }

        return total;
    }

    private void generarBloquesDestruibles() {

        for(int fila = 1; fila < mapa.getFilas() - 1; fila++) {
            for(int columna = 1; columna < mapa.getColumnas() - 1; columna++) {

                if(!puedeGenerarBloqueEn(fila, columna)) {
                    continue;
                }

                if(random.nextDouble() < PROBABILIDAD_BLOQUE) {
                    bloques.add(new BloqueDestruible(fila, columna));
                }
            }
        }
    }

    private void generarEnemigos() {

        agregarEnemigoEn(1, 7);
        agregarEnemigoEn(5, 7);
        agregarEnemigoEn(11, 1);
    }

    private void agregarEnemigoEn(int fila, int columna) {

        liberarCelda(fila, columna);
        enemigos.add(new Enemigo(columna * TAM_CASILLA, fila * TAM_CASILLA));
    }

    private void liberarCelda(int fila, int columna) {

        bloques.removeIf(bloque ->
                bloque.getFila() == fila && bloque.getColumna() == columna);
    }

    private boolean puedeGenerarBloqueEn(int fila, int columna) {

        if(mapa.esMuro(fila, columna)) {
            return false;
        }

        return !esZonaInicialJugador(fila, columna);
    }

    private boolean esZonaInicialJugador(int fila, int columna) {

        return (fila == 1 && columna == 1)
                || (fila == 1 && columna == 2)
                || (fila == 2 && columna == 1)
                || (fila == 11 && columna == 13)
                || (fila == 11 && columna == 12)
                || (fila == 10 && columna == 13);
    }

    private boolean existeBombaEn(int fila, int columna) {

        for(Bomba bomba : bombas) {
            if(bomba.getFila() == fila && bomba.getColumna() == columna) {
                return true;
            }
        }

        return false;
    }

    private void crearExplosion(Bomba bomba) {

        List<Posicion> celdas = calcularCeldasExplosion(bomba);
        Explosion explosion = new Explosion(celdas, TIEMPO_EXPLOSION);

        explosiones.add(explosion);
        destruirBloques(explosion);
        verificarImpactoJugadores(explosion);
        encenderBombasAlcanzadas(explosion);
    }

    private void encenderBombasAlcanzadas(Explosion explosion) {

        List<Bomba> bombasAlcanzadas = new java.util.ArrayList<>();

        for(Bomba bomba : bombas) {
            if(explosion.afectaCelda(bomba.getFila(), bomba.getColumna())) {
                bombasAlcanzadas.add(bomba);
            }
        }

        for(Bomba bomba : bombasAlcanzadas) {
            explotarBomba(bomba);
        }
    }

    private List<Posicion> calcularCeldasExplosion(Bomba bomba) {

        List<Posicion> celdas = new java.util.ArrayList<>();
        agregarCeldaSiExiste(celdas, bomba.getFila(), bomba.getColumna());

        agregarLineaExplosion(celdas, bomba.getFila(), bomba.getColumna(), -1, 0);
        agregarLineaExplosion(celdas, bomba.getFila(), bomba.getColumna(), 1, 0);
        agregarLineaExplosion(celdas, bomba.getFila(), bomba.getColumna(), 0, -1);
        agregarLineaExplosion(celdas, bomba.getFila(), bomba.getColumna(), 0, 1);

        return celdas;
    }

    private void agregarLineaExplosion(
            List<Posicion> celdas,
            int filaInicial,
            int columnaInicial,
            int cambioFila,
            int cambioColumna) {

        for(int distancia = 1; distancia <= RANGO_BOMBA; distancia++) {

            int fila = filaInicial + cambioFila * distancia;
            int columna = columnaInicial + cambioColumna * distancia;

            if(mapa.esMuro(fila, columna)) {
                return;
            }

            agregarCeldaSiExiste(celdas, fila, columna);

            if(existeBloqueActivoEn(fila, columna)) {
                return;
            }
        }
    }

    private void agregarCeldaSiExiste(List<Posicion> celdas, int fila, int columna) {

        if(mapa.estaDentro(fila, columna)) {
            celdas.add(new Posicion(columna, fila));
        }
    }

    private boolean existeBloqueActivoEn(int fila, int columna) {

        for(BloqueDestruible bloque : bloques) {
            if(!bloque.isDestruido()
                    && bloque.getFila() == fila
                    && bloque.getColumna() == columna) {
                return true;
            }
        }

        return false;
    }

    private void destruirBloques(Explosion explosion) {

        for(BloqueDestruible bloque : bloques) {

            if(bloque.isDestruido()) {
                continue;
            }

            if(explosion.afectaCelda(bloque.getFila(), bloque.getColumna())) {
                bloque.destruir();
            }
        }
    }

    private void verificarImpactoJugadores(Explosion explosion) {

        verificarImpactoJugador(jugador1, explosion);
        verificarImpactoJugador(jugador2, explosion);
        verificarImpactoEnemigos(explosion);
        verificarGanador();
    }

    private void verificarImpactoEnemigos(Explosion explosion) {

        for(Enemigo enemigo : enemigos) {
            if(enemigo.isVivo()
                    && explosion.afectaCelda(enemigo.getFila(), enemigo.getColumna())) {
                enemigo.eliminar();
            }
        }
    }

    private void verificarImpactoJugador(Jugador jugador, Explosion explosion) {

        if(jugador.isVivo()
                && explosion.afectaCelda(jugador.getFila(), jugador.getColumna())) {
            jugador.eliminar();
        }
    }

    private void verificarImpactoExplosionesActivas() {

        if(!estaJugando()) {
            return;
        }

        for(Explosion explosion : explosiones) {
            verificarImpactoJugadores(explosion);
        }
    }

    private void verificarGanador() {

        if(isJuegoTerminado()) {
            return;
        }

        if(!jugador1.isVivo() && !jugador2.isVivo()) {
            estadoJuego = EstadoJuego.FINALIZADO;
            mensajeFinal = "Empate";
        } else if(!jugador1.isVivo()) {
            estadoJuego = EstadoJuego.FINALIZADO;
            mensajeFinal = "Gana Jugador 2";
        } else if(!jugador2.isVivo()) {
            estadoJuego = EstadoJuego.FINALIZADO;
            mensajeFinal = "Gana Jugador 1";
        }
    }

    private void actualizarEnemigos() {

        if(!estaJugando()) {
            return;
        }

        long tiempoActual = System.currentTimeMillis();

        for(Enemigo enemigo : enemigos) {

            if(!enemigo.isVivo()) {
                continue;
            }

            verificarContactoConJugadores(enemigo);

            if(tiempoActual - enemigo.getUltimoMovimiento() < TIEMPO_MOVIMIENTO_ENEMIGO) {
                continue;
            }

            moverEnemigo(enemigo);
            enemigo.setUltimoMovimiento(tiempoActual);
            verificarContactoConJugadores(enemigo);
        }
    }

    private void moverEnemigo(Enemigo enemigo) {

        Direccion direccion = elegirDireccionEnemigo(enemigo);

        if(direccion == null) {
            return;
        }

        int nuevaX = enemigo.getPosicion().getX();
        int nuevaY = enemigo.getPosicion().getY();

        switch(direccion) {
            case ARRIBA -> nuevaY -= TAM_CASILLA;
            case ABAJO -> nuevaY += TAM_CASILLA;
            case IZQUIERDA -> nuevaX -= TAM_CASILLA;
            case DERECHA -> nuevaX += TAM_CASILLA;
        }

        if(puedeMoverEnemigo(enemigo, nuevaX, nuevaY)) {
            enemigo.getPosicion().setX(nuevaX);
            enemigo.getPosicion().setY(nuevaY);
        }
    }

    private Direccion elegirDireccionEnemigo(Enemigo enemigo) {

        List<Direccion> direccionesSeguras = obtenerDireccionesSeguras(enemigo);

        if(direccionesSeguras.isEmpty()) {
            return null;
        }

        Jugador objetivo = obtenerJugadorObjetivo(enemigo);

        if(objetivo != null && distancia(enemigo, objetivo) <= DISTANCIA_PERSECUCION) {
            Direccion persecucion = elegirDireccionHacia(enemigo, objetivo, direccionesSeguras);

            if(persecucion != null) {
                return persecucion;
            }
        }

        return direccionesSeguras.get(random.nextInt(direccionesSeguras.size()));
    }

    private List<Direccion> obtenerDireccionesSeguras(Enemigo enemigo) {

        List<Direccion> direcciones = new java.util.ArrayList<>();

        for(Direccion direccion : Direccion.values()) {

            int nuevaX = enemigo.getPosicion().getX();
            int nuevaY = enemigo.getPosicion().getY();

            switch(direccion) {
                case ARRIBA -> nuevaY -= TAM_CASILLA;
                case ABAJO -> nuevaY += TAM_CASILLA;
                case IZQUIERDA -> nuevaX -= TAM_CASILLA;
                case DERECHA -> nuevaX += TAM_CASILLA;
            }

            int fila = nuevaY / TAM_CASILLA;
            int columna = nuevaX / TAM_CASILLA;

            if(puedeMoverEnemigo(enemigo, nuevaX, nuevaY)
                    && !hayExplosionEn(fila, columna)) {
                direcciones.add(direccion);
            }
        }

        return direcciones;
    }

    private Jugador obtenerJugadorObjetivo(Enemigo enemigo) {

        if(jugador1.isVivo() && jugador2.isVivo()) {
            return distancia(enemigo, jugador1) <= distancia(enemigo, jugador2)
                    ? jugador1
                    : jugador2;
        }

        if(jugador1.isVivo()) {
            return jugador1;
        }

        if(jugador2.isVivo()) {
            return jugador2;
        }

        return null;
    }

    private Direccion elegirDireccionHacia(
            Enemigo enemigo,
            Jugador objetivo,
            List<Direccion> direccionesPermitidas) {

        int diferenciaFila = objetivo.getFila() - enemigo.getFila();
        int diferenciaColumna = objetivo.getColumna() - enemigo.getColumna();

        List<Direccion> candidatas = new java.util.ArrayList<>();

        if(Math.abs(diferenciaColumna) >= Math.abs(diferenciaFila)) {
            candidatas.add(diferenciaColumna < 0 ? Direccion.IZQUIERDA : Direccion.DERECHA);
            candidatas.add(diferenciaFila < 0 ? Direccion.ARRIBA : Direccion.ABAJO);
        } else {
            candidatas.add(diferenciaFila < 0 ? Direccion.ARRIBA : Direccion.ABAJO);
            candidatas.add(diferenciaColumna < 0 ? Direccion.IZQUIERDA : Direccion.DERECHA);
        }

        for(Direccion direccion : candidatas) {
            if(direccionesPermitidas.contains(direccion)) {
                return direccion;
            }
        }

        return null;
    }

    private int distancia(Enemigo enemigo, Jugador jugador) {

        return Math.abs(enemigo.getFila() - jugador.getFila())
                + Math.abs(enemigo.getColumna() - jugador.getColumna());
    }

    private boolean puedeMoverEnemigo(Enemigo enemigo, int nuevaX, int nuevaY) {

        int fila = nuevaY / TAM_CASILLA;
        int columna = nuevaX / TAM_CASILLA;

        if(mapa.esMuro(fila, columna) || existeBombaEn(fila, columna)) {
            return false;
        }

        if(existeBloqueActivoEn(fila, columna)) {
            return false;
        }

        for(Enemigo otro : enemigos) {
            if(otro != enemigo
                    && otro.isVivo()
                    && otro.getFila() == fila
                    && otro.getColumna() == columna) {
                return false;
            }
        }

        return true;
    }

    private boolean hayExplosionEn(int fila, int columna) {

        for(Explosion explosion : explosiones) {
            if(explosion.afectaCelda(fila, columna)) {
                return true;
            }
        }

        return false;
    }

    private void verificarContactoConJugadores(Enemigo enemigo) {

        verificarContactoConJugador(enemigo, jugador1);
        verificarContactoConJugador(enemigo, jugador2);
        verificarGanador();
    }

    private void verificarContactoConJugador(Enemigo enemigo, Jugador jugador) {

        if(jugador.isVivo()
                && enemigo.getFila() == jugador.getFila()
                && enemigo.getColumna() == jugador.getColumna()) {
            jugador.eliminar();
        }
    }

    public void actualizarBombas() {

        List<Bomba> bombasPorExplotar = new java.util.ArrayList<>();

        for(Bomba bomba : bombas) {

            long tiempoActual = System.currentTimeMillis();

            if(tiempoActual - bomba.getTiempoColocacion() >= 3000) {
                bombasPorExplotar.add(bomba);
            }
        }

        for(Bomba bomba : bombasPorExplotar) {
            explotarBomba(bomba);
        }
    }

    private void explotarBomba(Bomba bomba) {

        if(bombas.remove(bomba)) {
            crearExplosion(bomba);
        }
    }

    private void actualizarExplosiones() {

        Iterator<Explosion> it = explosiones.iterator();

        while(it.hasNext()) {
            Explosion explosion = it.next();

            if(!explosion.estaActiva()) {
                it.remove();
            }
        }
    }
}
