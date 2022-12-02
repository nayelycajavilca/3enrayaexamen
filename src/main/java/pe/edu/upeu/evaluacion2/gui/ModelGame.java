/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.upeu.evaluacion2.gui;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import pe.edu.upeu.evaluacion2.dao.ResultadoDAO;
import pe.edu.upeu.evaluacion2.dao.ResultadoDaoI;
import pe.edu.upeu.evaluacion2.modelo.ResultadoTO;

/**
 *
 * @author USUARIO
 */
public class ModelGame {

    private String turno;
    private boolean end;
    private boolean draw;
    private JLabel cuadroj1;
    private JLabel cuadroj2;
    private String[][] tablero;
    private int cantMovidas;
    private int victoriasJ1;
    private int victoriasJ2;

    public ModelGame() {
        turno = "X";
        end = false;
        draw = false;
        tablero = new String[3][3];
        cantMovidas = 0;
        victoriasJ1 = 0;
        victoriasJ2 = 0;
    }

    public void marcarCasilla(int i, int j, JLabel[][] casillas, Vista view) {

        if (!end) {
            if (tablero[i][j] == null) {
                tablero[i][j] = turno;
                casillas[i][j].setText(turno);
                cantMovidas++;
                verificarEstado();
                if (!end) {
                    if (turno.equals("X")) {
                        turno = "O";
                        ResultadoTO to = view.uTO;
                        view.lblNombre.setText(to.getNombreJugador2()+" (O)");
                    } else {
                        turno = "X";
                        ResultadoTO to = view.uTO;
                        view.lblNombre.setText(to.getNombreJugador1()+" (X)");
                    }
                } else {
                    terminarJuego(view);
                    view.lblNombre.setText("");
                }
            }
        }
    }

    private void verificarEstado() {
        verificarFilas();
        if (!end) {
            verificarColumnas();
            if (!end) {
                verificarDiagonalP();
                if (!end) {
                    verificarDiagonalS();
                    if (!end) {
                        if (cantMovidas == 9) {
                            draw = true;
                            end = true;
                        }
                    }

                }
            }
        }
    }

    private void verificarFilas() {
        for (int i = 0; i < 3 && !end; i++) {
            boolean win = true;
            for (int j = 0; j < 3 && win; j++) {
                if (tablero[i][j] == null || !tablero[i][j].equals(turno)) {
                    win = false;
                }
            }
            if (win) {
                end = true;
            }
        }
    }

    private void verificarColumnas() {
        for (int j = 0; j < 3 && !end; j++) {
            boolean win = true;
            for (int i = 0; i < 3 && win; i++) {
                if (tablero[i][j] == null || !tablero[i][j].equals(turno)) {
                    win = false;
                }
            }
            if (win) {
                end = true;
            }
        }
    }

    private void verificarDiagonalP() {

        boolean win = true;
        for (int i = 0; i < 3 && win; i++) {
            if (tablero[i][i] == null || !tablero[i][i].equals(turno)) {
                win = false;
            }
        }
        if (win) {
            end = true;
        }

    }

    private void verificarDiagonalS() {

        boolean win = true;
        int j = 2;
        for (int i = 0; i < 3 && win; i++, j--) {
            if (tablero[i][j] == null || !tablero[i][j].equals(turno)) {
                win = false;
            }
        }
        if (win) {
            end = true;
        }

    }

    private void terminarJuego(Vista view) {
        if (draw) {
            ResultadoTO to = view.uTO;
            System.out.println("id: " + to.getIdResultado());
            to.setGanador("Empate");
            to.setPunto(0);
            to.setEstado("Finalizado");
            ResultadoDaoI rDao = new ResultadoDAO();
            rDao.update(to);
            view.ListarResultado();
            JOptionPane.showMessageDialog(null, "Empate");
        } else {
            if (turno.equals("X")) {
                victoriasJ1++;
                cuadroj1.setText(String.valueOf(victoriasJ1));
                ResultadoTO to = view.uTO;
                System.out.println("id" + to.getIdResultado());
                to.setGanador(to.getNombreJugador1());
                to.setPunto(1);
                to.setEstado("Finalizado");
                ResultadoDaoI rDao = new ResultadoDAO();
                rDao.update(to);
                view.ListarResultado();
                JOptionPane.showMessageDialog(null, "Victoria de " + to.getNombreJugador1());
            } else {
                victoriasJ2++;
                cuadroj2.setText(String.valueOf(victoriasJ2));
                ResultadoTO to = view.uTO;
                System.out.println("id" + to.getIdResultado());
                to.setGanador(to.getNombreJugador2());
                to.setPunto(1);
                to.setEstado("Finalizado");
                ResultadoDaoI rDao = new ResultadoDAO();
                rDao.update(to);
                view.ListarResultado();
                JOptionPane.showMessageDialog(null, "victoria de " + to.getNombreJugador2());
            }
        }
    }

    void setJugadores(JLabel j1, JLabel j2) {
        cuadroj1 = j1;
        cuadroj2 = j2;
    }

    void resetGame(JLabel[][] casillas) {
        turno = "X";
        end = false;
        draw = false;
        cantMovidas = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tablero[i][j] = null;
                casillas[i][j].setText("");
            }
        }
    }

    void AnularGame(JLabel[][] casillas,Vista view) {
        turno = "X";
        end = false;
        draw = false;
        cantMovidas = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tablero[i][j] = null;
                casillas[i][j].setText("");

            }
        }
        ResultadoTO to = view.uTO;
        System.out.println("id: " + to.getIdResultado());
        to.setGanador("Anulado");
        to.setPunto(0);
        to.setEstado("Anulado");
        ResultadoDaoI rDao = new ResultadoDAO();
        rDao.update(to);
        view.ListarResultado();
        JOptionPane.showMessageDialog(null, "Partida anulada");
    }
}
