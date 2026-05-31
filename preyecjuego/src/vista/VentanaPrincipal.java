/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import controlador.ControlJuego;
import controlador.ControlTeclado;
import javax.swing.JFrame;
import javax.swing.Timer;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {

        ControlJuego control = new ControlJuego();
        PanelJuego panel = new PanelJuego(control);
        ControlTeclado teclado = new ControlTeclado(control, panel);

        setTitle("Bomberman");

        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(panel);
        teclado.configurarControles();
        pack();
        setLocationRelativeTo(null);

        Timer timer = new Timer(100, e -> {
            control.actualizarJuego();
            panel.repaint();
        });

        timer.start();
    }
}
    
