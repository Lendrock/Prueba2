
package org.leandromaldonado.system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Lendrock
 */


public class Main extends Application {
    private static String URL = "/org/leandromaldonado/";
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage escenario) throws Exception{
        Parent raiz = FXMLLoader.load(getClass().getResource(URL +"view/Totito.fxml"));
        Scene escena = new Scene(raiz);
        escenario.setScene(escena);
        escenario.show();
    }


    
}