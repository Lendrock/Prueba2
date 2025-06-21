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
    Button button1, button2, button3, button4, button5, button6, button7, button8,
            button9;
    @FXML
    private Text Ganador;

    private int Turno = 0; // 0 para X (Jugador 1), 1 para O (Jugador 2 o CPU)
    private boolean vsCPU = false; // Indica si el juego es contra la CPU
    private char[][] boardState = new char[3][3]; // Representación lógica del tablero

    ArrayList<Button> buttons;

    private Image circleImage;
    private Image crossImage; // Nueva variable para la imagen de la X

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            circleImage = new Image(getClass().getResourceAsStream("/org/leandromaldonado/image/circle.png"));
            crossImage = new Image(getClass().getResourceAsStream("/org/leandromaldonado/image/X.png")); // Cargar la imagen para la X
        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage());
            // Considera mostrar un mensaje de error al usuario si las imágenes no cargan
        }

        buttons = new ArrayList<>(Arrays.asList(button1, button2, button3, button4,
                button5, button6, button7, button8, button9));

        buttons.forEach(button -> {
            setupButton(button);
            button.setFocusTraversable(false);
        });

        initializeBoard(); // Inicializar el estado lógico del tablero
        Ganador.setText("Selecciona modo de juego");
        disableAllButtons(); // Deshabilitar botones hasta que se seleccione el modo
    }

    // --- Métodos de Modo de Juego (Nuevos) ---

    @FXML
    void startVsPlayer(ActionEvent event) {
        vsCPU = false;
        restartGame(null); // Reiniciar el juego para un nuevo partido
        Ganador.setText("Turno de X");
    }

    @FXML
    void startVsCPU(ActionEvent event) {
        vsCPU = true;
        restartGame(null); // Reiniciar el juego para un nuevo partido
        Ganador.setText("Tu turno");
    }

    // --- Lógica del Juego ---

    @FXML
    void restartGame(ActionEvent event) {
        buttons.forEach(this::resetButton);
        initializeBoard(); // Resetear el estado lógico del tablero
        Ganador.setText(vsCPU ? "Tu turno" : "Tu de X");
        Turno = 0; // Siempre empieza X
        enableAllButtons(); // Habilitar botones al reiniciar
    }

    public void resetButton(Button button) {
        button.setDisable(false);
        button.setText("");
        button.setGraphic(null); // Asegura que los gráficos se limpien
    }

    private void setupButton(Button button) {
        button.setOnMouseClicked(mouseEvent -> {
            if (!button.isDisabled()) { // Solo si el botón no está deshabilitado
                int index = buttons.indexOf(button);
                int row = index / 3;
                int col = index % 3;

                if (boardState[row][col] == ' ') { // Verificar que la casilla esté vacía lógicamente
                    setPlayerSymbol(button, row, col);
                    checkIfGameIsOver();

                    if (vsCPU && !isGameOver() && Turno == 1) { // Si es contra CPU y no ha terminado, turno de la CPU
                        cpuMove();
                        checkIfGameIsOver();
                    }
                }
            }
        });
    }

    public void setPlayerSymbol(Button button, int row, int col) {
        if (Turno % 2 == 0) { // Turno de X
            if (crossImage != null) {
                ImageView imageView = new ImageView(crossImage);
                imageView.setFitWidth(50); // Ajusta el tamaño de la imagen
                imageView.setFitHeight(50); // Ajusta el tamaño de la imagen
                button.setGraphic(imageView);
                button.setText(""); // Limpiar el texto si hay gráfico
            } else {
                button.setText("X"); // Fallback a texto si la imagen no carga
            }
            boardState[row][col] = 'X'; // Actualizar el estado lógico del tablero
            Turno = 1;
            Ganador.setText(vsCPU ? "Turno de O (CPU)" : "Turno de O");
        } else { // Turno de O
            if (circleImage != null) {
                ImageView imageView = new ImageView(circleImage);
                imageView.setFitWidth(50); // Ajusta el tamaño de la imagen
                imageView.setFitHeight(50); // Ajusta el tamaño de la imagen
                button.setGraphic(imageView);
                button.setText(""); // Limpiar el texto si hay gráfico
            } else {
                button.setText("O"); // Fallback a texto si la imagen no carga
            }
            boardState[row][col] = 'O'; // Actualizar el estado lógico del tablero
            Turno = 0;
            Ganador.setText("Tu turno");
        }
        button.setDisable(true); // Deshabilitar el botón después del movimiento
    }

    public void checkIfGameIsOver() {
        if (checkWin('X')) {
            Ganador.setText("¡X Gano!");
            disableAllButtons();
        } else if (checkWin('O')) {
            Ganador.setText("¡O Gano!");
            disableAllButtons();
        } else if (isBoardFull()) {
            Ganador.setText("¡Empate!");
            disableAllButtons();
        }
    }

    private boolean isGameOver() {
        return checkWin('X') || checkWin('O') || isBoardFull();
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (boardState[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private void disableAllButtons() {
        buttons.forEach(button -> button.setDisable(true));
    }

    private void enableAllButtons() {
        buttons.forEach(button -> button.setDisable(false));
    }

    // --- Lógica del Tablero (Similar a TotitoC/TotitoP) ---

    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boardState[i][j] = ' ';
            }
        }
    }

    private boolean checkWin(char player) {
        // Check rows and columns
        for (int i = 0; i < 3; i++) {
            if ((boardState[i][0] == player && boardState[i][1] == player && boardState[i][2] == player) ||
                    (boardState[0][i] == player && boardState[1][i] == player && boardState[2][i] == player)) {
                return true;
            }
        }
        // Check diagonals
        if ((boardState[0][0] == player && boardState[1][1] == player && boardState[2][2] == player) ||
                (boardState[0][2] == player && boardState[1][1] == player && boardState[2][0] == player)) {
            return true;
        }
        return false;
    }

    // --- Lógica de la CPU (Integrada de TotitoC) ---

    private void cpuMove() {
        Random random = new Random();
        int row, col;
        do {
            row = random.nextInt(3);
            col = random.nextInt(3);
        } while (boardState[row][col] != ' '); // Busca una casilla vacía

        // Simular el clic en el botón de la UI para el movimiento de la CPU
        Button cpuButton = buttons.get(row * 3 + col);
        setPlayerSymbol(cpuButton, row, col);
    }
}