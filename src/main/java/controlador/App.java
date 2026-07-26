package controlador;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 909, 604);
        stage.setTitle("Proyecto Final POO");
        stage.setScene(scene);
        stage.setResizable(false); 
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        // AGREGAMOS "/vista/" AL INICIO.
        // La barra diagonal (/) le indica a Java que busque desde la raíz de la carpeta resources
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/vista/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}