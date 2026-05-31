/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import controlador.ControlJuego;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import modelo.BloqueDestruible;
import modelo.Bomba;
import modelo.Enemigo;
import modelo.Explosion;
import modelo.Jugador;
import modelo.Posicion;
import modelo.PowerUp;
import modelo.TipoPowerUp;

public class PanelJuego extends JPanel {

    private ControlJuego control;

    private final int TAM = ControlJuego.TAM_CASILLA;

    public PanelJuego(ControlJuego control) {

        this.control = control;

        setBackground(Color.BLACK);
        setFocusable(true);
        setPreferredSize(new Dimension(
                control.getMapa().getColumnas() * TAM,
                control.getMapa().getFilas() * TAM
        ));
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        int[][] mapa = control.getMapa().getMatriz();

        dibujarSuelo(g, mapa);
        
        // DIBUJAR MUROS FIJOS
        for(int fila=0; fila<mapa.length; fila++) {

            for(int col=0; col<mapa[fila].length; col++) {

                if(mapa[fila][col] == 1) {

                    dibujarMuroFijo(g, fila, col);
                }
            }
        }
        // DIBUJAR BLOQUES DESTRUIBLES
        for(BloqueDestruible b : control.getBloques()) {

            if(!b.isDestruido()) {
                dibujarBloqueDestruible(g, b);
            }
        }

        for(PowerUp powerUp : control.getPowerUps()) {
            dibujarPowerUp(g, powerUp);
        }

        g.setColor(Color.RED);

        for(Bomba b : control.getBombas()){

            g.fillOval(
                    b.getColumna()*TAM,
                    b.getFila()*TAM,
                    TAM,
                    TAM
            );
        }

        g.setColor(Color.YELLOW);

        for(Explosion explosion : control.getExplosiones()) {

            for(Posicion celda : explosion.getCeldasAfectadas()) {
                g.fillRect(
                        celda.getX() * TAM,
                        celda.getY() * TAM,
                        TAM,
                        TAM
                );
            }
        }

        dibujarJugador(g, control.getJugador1(), colorJugador(control.getJugador1(), Color.CYAN));
        dibujarJugador(g, control.getJugador2(), colorJugador(control.getJugador2(), Color.MAGENTA));

        for(Enemigo enemigo : control.getEnemigos()) {
            dibujarEnemigo(g, enemigo);
        }

        dibujarHud(g);

        if(control.estaEnMenu()) {
            dibujarMenu(g);
        }

        if(control.isJuegoTerminado()) {
            dibujarPantallaFinal(g);
        }
    }

    private void dibujarSuelo(Graphics g, int[][] mapa) {

        g.setColor(new Color(12, 112, 0));

        for(int fila=0; fila<mapa.length; fila++) {
            for(int col=0; col<mapa[fila].length; col++) {
                g.fillRect(col * TAM, fila * TAM, TAM, TAM);
            }
        }
    }

    private void dibujarMuroFijo(Graphics g, int fila, int columna) {

        int x = columna * TAM;
        int y = fila * TAM;

        g.setColor(new Color(185, 190, 185));
        g.fillRect(x, y, TAM, TAM);
        g.setColor(Color.WHITE);
        g.drawLine(x + 2, y + 2, x + TAM - 3, y + 2);
        g.drawLine(x + 2, y + 2, x + 2, y + TAM - 3);
        g.setColor(Color.BLACK);
        g.drawLine(x + 4, y + TAM - 3, x + TAM - 3, y + TAM - 3);
        g.drawLine(x + TAM - 3, y + 4, x + TAM - 3, y + TAM - 3);
    }

    private void dibujarBloqueDestruible(Graphics g, BloqueDestruible bloque) {

        int x = bloque.getColumna() * TAM;
        int y = bloque.getFila() * TAM;

        g.setColor(new Color(222, 226, 220));
        g.fillRect(x, y, TAM, TAM);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, TAM - 1, TAM - 1);
        g.drawLine(x + 2, y + 16, x + TAM - 3, y + 16);
        g.drawLine(x + 2, y + 33, x + TAM - 3, y + 33);
        g.drawLine(x + 18, y + 2, x + 18, y + 16);
        g.drawLine(x + 34, y + 17, x + 34, y + 33);
        g.drawLine(x + 18, y + 34, x + 18, y + TAM - 3);
    }

    private void dibujarPowerUp(Graphics g, PowerUp powerUp) {

        int x = powerUp.getColumna() * TAM + 10;
        int y = powerUp.getFila() * TAM + 10;

        g.setColor(colorPowerUp(powerUp.getTipo()));
        g.fillRect(x, y, TAM - 20, TAM - 20);
        g.setColor(Color.WHITE);
        g.drawRect(x, y, TAM - 21, TAM - 21);
    }

    private Color colorPowerUp(TipoPowerUp tipo) {

        switch(tipo) {
            case MAS_BOMBAS:
                return Color.BLUE;
            case MAYOR_RANGO:
                return Color.ORANGE;
            case ESCUDO:
                return Color.RED;
            default:
                return Color.WHITE;
        }
    }

    private Color colorJugador(Jugador jugador, Color colorBase) {

        if(jugador.tieneEscudoActivo()) {
            return new Color(255, 215, 0);
        }

        return colorBase;
    }

    private void dibujarJugador(Graphics g, Jugador jugador, Color color) {

        if(jugador.isVivo()) {
            g.setColor(color);
        } else {
            g.setColor(Color.DARK_GRAY);
        }

        g.fillOval(
                jugador.getPosicion().getX() + 5,
                jugador.getPosicion().getY() + 5,
                40,
                40
        );

        g.setColor(Color.WHITE);
        g.drawString(
                jugador.getNombre(),
                jugador.getPosicion().getX() + 2,
                jugador.getPosicion().getY() + 48
        );
    }

    private void dibujarEnemigo(Graphics g, Enemigo enemigo) {

        if(enemigo.isVivo()) {
            g.setColor(new Color(245, 145, 65));
        } else {
            g.setColor(Color.GRAY);
        }

        int x = enemigo.getPosicion().getX();
        int y = enemigo.getPosicion().getY();

        g.fillOval(x + 8, y + 8, 34, 34);
        g.setColor(Color.BLACK);
        g.fillOval(x + 17, y + 20, 5, 5);
        g.fillOval(x + 28, y + 20, 5, 5);
    }

    private void dibujarHud(Graphics g) {

        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, getWidth(), 28);
        g.setColor(Color.WHITE);
        g.drawString("J1: WASD + SPACE", 15, 18);
        g.drawString("J2: FLECHAS + ENTER", 170, 18);
        g.drawString("R: REINICIAR", getWidth() - 120, 18);
        g.drawString(textoEstadoJugador("J1", control.getJugador1()), 15, getHeight() - 12);
        g.drawString(textoEstadoJugador("J2", control.getJugador2()), 300, getHeight() - 12);
    }

    private String textoEstadoJugador(String etiqueta, Jugador jugador) {

        long escudo = jugador.getTiempoEscudoRestante() / 1000;

        return etiqueta
                + " Rango:" + jugador.getRangoExplosion()
                + " Bombas:" + jugador.getMejorasBombas()
                + " Escudo:" + escudo + "s";
    }

    private void dibujarMenu(Graphics g) {

        dibujarOverlay(g);
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(30f));
        g.drawString("BOMBERMAN", getWidth() / 2 - 100, getHeight() / 2 - 60);
        g.setFont(g.getFont().deriveFont(15f));
        g.drawString("Jugador 1: WASD + SPACE", getWidth() / 2 - 105, getHeight() / 2 - 15);
        g.drawString("Jugador 2: Flechas + ENTER", getWidth() / 2 - 105, getHeight() / 2 + 10);
        g.drawString("Presiona P para iniciar", getWidth() / 2 - 90, getHeight() / 2 + 50);
    }

    private void dibujarPantallaFinal(Graphics g) {

        dibujarOverlay(g);
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(28f));
        g.drawString(control.getMensajeFinal(), getWidth() / 2 - 100, getHeight() / 2 - 20);
        g.setFont(g.getFont().deriveFont(15f));
        g.drawString("Presiona R para reiniciar", getWidth() / 2 - 95, getHeight() / 2 + 25);
    }

    private void dibujarOverlay(Graphics g) {

        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
