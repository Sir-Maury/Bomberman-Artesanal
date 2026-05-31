/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.function.Supplier;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import modelo.Direccion;
import modelo.Jugador;
import vista.PanelJuego;

public class ControlTeclado {

    private ControlJuego control;
    private PanelJuego panel;

    public ControlTeclado(ControlJuego control, PanelJuego panel) {

        this.control = control;
        this.panel = panel;
    }

    public void configurarControles() {

        registrarMovimiento("j1Arriba", KeyEvent.VK_W, control::getJugador1, Direccion.ARRIBA);
        registrarMovimiento("j1Abajo", KeyEvent.VK_S, control::getJugador1, Direccion.ABAJO);
        registrarMovimiento("j1Izquierda", KeyEvent.VK_A, control::getJugador1, Direccion.IZQUIERDA);
        registrarMovimiento("j1Derecha", KeyEvent.VK_D, control::getJugador1, Direccion.DERECHA);
        registrarBomba("j1Bomba", KeyEvent.VK_SPACE, control::getJugador1);

        registrarMovimiento("j2Arriba", KeyEvent.VK_UP, control::getJugador2, Direccion.ARRIBA);
        registrarMovimiento("j2Abajo", KeyEvent.VK_DOWN, control::getJugador2, Direccion.ABAJO);
        registrarMovimiento("j2Izquierda", KeyEvent.VK_LEFT, control::getJugador2, Direccion.IZQUIERDA);
        registrarMovimiento("j2Derecha", KeyEvent.VK_RIGHT, control::getJugador2, Direccion.DERECHA);
        registrarBomba("j2Bomba", KeyEvent.VK_ENTER, control::getJugador2);
        registrarFlujoPartida();
    }

    private void registrarMovimiento(
            String nombreAccion,
            int tecla,
            Supplier<Jugador> obtenerJugador,
            Direccion direccion) {

        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(tecla, 0), nombreAccion);

        panel.getActionMap().put(nombreAccion, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Jugador jugador = obtenerJugador.get();
                control.moverJugador(jugador, direccion);
                panel.repaint();
            }
        });
    }

    private void registrarBomba(String nombreAccion, int tecla, Supplier<Jugador> obtenerJugador) {

        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(tecla, 0), nombreAccion);

        panel.getActionMap().put(nombreAccion, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Jugador jugador = obtenerJugador.get();
                control.colocarBomba(jugador);
                panel.repaint();
            }
        });
    }

    private void registrarFlujoPartida() {

        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "iniciarPartida");
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "reiniciarPartida");

        panel.getActionMap().put("iniciarPartida", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                control.iniciarPartida();
                panel.repaint();
            }
        });

        panel.getActionMap().put("reiniciarPartida", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                control.reiniciarPartida();
                panel.repaint();
            }
        });
    }
}
