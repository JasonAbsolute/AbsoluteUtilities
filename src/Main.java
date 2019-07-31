import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    /**
     * This method launches the JavaFX application
     *
     * @param primaryStage Stage to use
     * @throws Exception Generic exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("QuoteController.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Absolute Utilities");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(
                new Image( Main.class.getResourceAsStream("img/AbsoluteUtilitiesLogo.PNG")));
        primaryStage.show();
    }

    /**
     * This is the main method for the application
     *
     * @param args Generic argument
     */
    public static void main(String[] args) {
        launch(args);
    }
}
