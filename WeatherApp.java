import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeatherApp extends JFrame {

    private static final String API_KEY = "273bf12e1aa11f3d595e9367ccc57951";
    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather";

    private final JTextField locationField;
    private final JTextArea weatherArea;

    public WeatherApp() {
        // Set up the frame
        setTitle("Weather App");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Set up the input field
        JLabel locationLabel = new JLabel("Enter a location:");
        locationField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new SearchButtonListener());

        JPanel inputPanel = new JPanel();
        inputPanel.add(locationLabel);
        inputPanel.add(locationField);
        inputPanel.add(searchButton);

        // Set up the weather display area
        JLabel weatherLabel = new JLabel("Current weather:");
        weatherArea = new JTextArea(10, 30);
        weatherArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(weatherArea);

        JPanel weatherPanel = new JPanel();
        weatherPanel.add(weatherLabel);
        weatherPanel.add(scrollPane);

        // Add the panels to the frame
        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(weatherPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String location = locationField.getText().trim();

            if (location.isEmpty()) {
                JOptionPane.showMessageDialog(WeatherApp.this,
                        "Please enter a location",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                String url = API_URL + "?q=" + URLEncoder.encode(location, StandardCharsets.UTF_8)
                        + "&appid=" + API_KEY + "&units=metric";
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Response response = client.newCall(request).execute();
                String responseData = response.body().string();

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseData);

                double temperature = jsonNode.get("main").get("temp").asDouble();
                int humidity = jsonNode.get("main").get("humidity").asInt();
                double windSpeed = jsonNode.get("wind").get("speed").asDouble();
                double windDirection = jsonNode.get("wind").get("deg").asDouble();
                JsonNode weatherNode = jsonNode.get("weather").get(0);
                String description = weatherNode.get("description").asText();

                String weatherString = String.format("Temperature: %.1f °C\n" +
                                "Humidity: %d%%\n" +
                                "Wind: %.1f m/s, %.1f°\n" +
                                "Conditions: %s",
                        temperature, humidity, windSpeed, windDirection, description);

                weatherArea.setText(weatherString);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(WeatherApp.this,
                        "Error retrieving weather data: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        new WeatherApp();
    }
}
