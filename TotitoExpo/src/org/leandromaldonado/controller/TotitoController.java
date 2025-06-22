package org.leandromaldonado.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;

public class TotitoController implements Initializable {

    @FXML
    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    @FXML
    private Text Ganador;

    private int Turno = 0;
    private boolean vsCPU = false; 
    private char[][] tableroEstado = new char[3][3]; 

    ArrayList<Button> botones;

    private Image imagenCirculo;
    private Image imagenCruz; 

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            imagenCirculo = new Image(getClass().getResourceAsStream("/org/leandromaldonado/image/circle.png"));
            imagenCruz = new Image(getClass().getResourceAsStream("/org/leandromaldonado/image/X.png"));
        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage());
        }

        botones = new ArrayList<>(Arrays.asList(btn1, btn2, btn3, btn4,
                btn5, btn6, btn7, btn8, btn9));

        botones.forEach(btn -> {
            configBoton(btn);
            btn.setFocusTraversable(false);
        });

        inicializarTablero(); 
        Ganador.setText("Selecciona modo de juego");
        deshabilitarBotones();
    }

    @FXML
    void startVsPlayer(ActionEvent event) {
        vsCPU = false;
        reiniciarJuego(null); 
        Ganador.setText("Turno de X");
    }

    @FXML
    void startVsCPU(ActionEvent event) {
        vsCPU = true;
        reiniciarJuego(null); 
        Ganador.setText("Tu turno");
    }

    @FXML
    void reiniciarJuego(ActionEvent event) {
        botones.forEach(this::botonReiniciar);
        inicializarTablero(); 
        Ganador.setText(vsCPU ? "Tu turno" : "Tu de X");
        Turno = 0; // 
        habilitarBotones(); 
    }

    public void botonReiniciar(Button btn) {
        btn.setDisable(false);
        btn.setText("");
        btn.setGraphic(null);
    }

    private void configBoton(Button button) {
        button.setOnMouseClicked(mouseEvent -> {
            if (!button.isDisabled()) { 
                int index = botones.indexOf(button);
                int fila = index / 3;
                int columna = index % 3;

                if (tableroEstado[fila][columna] == ' ') { 
                    simboloJugador(button, fila, columna);
                    comprobarJuegoTerminado();

                    if (vsCPU && !juegoPerdido() && Turno == 1) { 
                        movimientoCPU();
                        comprobarJuegoTerminado();
                    }
                }
            }
        });
    }

    public void simboloJugador(Button btn, int fila, int columna) {
        if (Turno % 2 == 0) { 
            if (imagenCruz != null) {
                ImageView imageView = new ImageView(imagenCruz);
                imageView.setFitWidth(50); 
                imageView.setFitHeight(50); 
                btn.setGraphic(imageView);
                btn.setText(""); 
            } else {
                btn.setText("X"); 
            }
            tableroEstado[fila][columna] = 'X'; 
            Turno = 1;
            Ganador.setText(vsCPU ? "Turno de O (CPU)" : "Turno de O");
        } else { 
            if (imagenCirculo != null) {
                ImageView imageView = new ImageView(imagenCirculo);
                imageView.setFitWidth(50); 
                imageView.setFitHeight(50);
                btn.setGraphic(imageView);
                btn.setText(""); 
            } else {
                btn.setText("O"); 
            }
            tableroEstado[fila][columna] = 'O'; 
            Turno = 0;
            Ganador.setText("Tu turno");
        }
        btn.setDisable(true); 
    }

    public void comprobarJuegoTerminado() {
        if (comprobarGanador('X')) {
            Ganador.setText("¡X Gano!");
            deshabilitarBotones();
        } else if (comprobarGanador('O')) {
            Ganador.setText("¡O Gano!");
            deshabilitarBotones();
        } else if (tableroLleno()) {
            Ganador.setText("¡Empate!");
            deshabilitarBotones();
        }
    }

    private boolean juegoPerdido() {
        return comprobarGanador('X') || comprobarGanador('O') || tableroLleno();
    }

    private boolean tableroLleno() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tableroEstado[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private void deshabilitarBotones() {
        botones.forEach(button -> button.setDisable(true));
    }

    private void habilitarBotones() {
        botones.forEach(button -> button.setDisable(false));
    }

    private void inicializarTablero() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tableroEstado[i][j] = ' ';
            }
        }
    }

    private boolean comprobarGanador(char jugador) {
        // comprobar en filas y columnas
        for (int i = 0; i < 3; i++) {
            if ((tableroEstado[i][0] == jugador && tableroEstado[i][1] == jugador && tableroEstado[i][2] == jugador) ||
                    (tableroEstado[0][i] == jugador && tableroEstado[1][i] == jugador && tableroEstado[2][i] == jugador)) {
                return true;
            }
        }
        // comprobar en diagonal
        if ((tableroEstado[0][0] == jugador && tableroEstado[1][1] == jugador && tableroEstado[2][2] == jugador) ||
                (tableroEstado[0][2] == jugador && tableroEstado[1][1] == jugador && tableroEstado[2][0] == jugador)) {
            return true;
        }
        return false;
    }

    private void movimientoCPU() {
        Random random = new Random();
        int fila, columna;
        do {
            fila = random.nextInt(3);
            columna = random.nextInt(3);
        } while (tableroEstado[fila][columna] != ' '); 

        Button cpuBoton = botones.get(fila * 3 + columna);
        simboloJugador(cpuBoton, fila, columna);
    }
}