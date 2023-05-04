import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class WeatherApp extends Application {

    private WeatherService weatherService = new WeatherService();

    @Override
    public void start(Stage primaryStage) {
        TextField locationField = new TextField();
        Label tempLabel = new Label();
        Label descLabel = new Label();
        Button getWeatherButton;
    }
}
