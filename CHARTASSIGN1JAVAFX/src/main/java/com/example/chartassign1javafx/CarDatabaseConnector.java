package com.example.chartassign1javafx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CarDatabaseConnector extends Application {

    @FXML
    private TableView<CarData> carTableView;
    @FXML
    private TableColumn<CarData, String> brandColumn;
    @FXML
    private TableColumn<CarData, String> modelColumn;
    @FXML
    private TableColumn<CarData, Double> ratingColumn;
    @FXML
    private BarChart<String, Number> carBarChart;
    @FXML
    private Button switchToBarChartButton;
    @FXML
    private Button switchToTableViewButton;

    private Stage stage;
    private Scene carTableScene;
    private Scene barChartScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/car_layout.fxml"));
        Parent root = loader.load();

        CarDatabaseConnector controller = loader.getController();
        controller.setStage(primaryStage);

        primaryStage.setTitle("Car Models Information");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/chartassign1javafx/caricon.jpg")));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public void setStage(Stage stage) throws SQLException {
        this.stage = stage;
        initialize();
    }

    @FXML
    private void initialize() throws SQLException {
        Connection conn = DBUtility.getConnection();

        // Set up TableView columns
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));

        // Populate TableView
        ObservableList<CarData> carData = DBUtility.getCarData(conn);
        carTableView.setItems(carData);

        // Populate BarChart
        XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
        dataSeries.setName("Average Ratings");

        for (CarData car : carData) {
            dataSeries.getData().add(new XYChart.Data<>(car.getModel(), car.getRating()));
        }

        carBarChart.getData().add(dataSeries);

        // Close database connection
        conn.close();
    }

    @FXML
    private void switchToBarChart() {
        if (barChartScene == null) {
            barChartScene = new Scene(carBarChart.getParent());
        }
        stage.setScene(barChartScene);
    }

    @FXML
    private void switchToTableView() {
        if (carTableScene == null) {
            carTableScene = new Scene(carTableView.getParent());
        }
        stage.setScene(carTableScene);
    }

    // Database Utility Class
    public static class DBUtility {
        private static final String URL = "jdbc:mysql://localhost:3306/car_db";
        private static final String USER = "root";
        private static final String PASSWORD = "";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }

        public static ObservableList<CarData> getCarData(Connection conn) throws SQLException {
            ObservableList<CarData> carData = FXCollections.observableArrayList();
            String query = "SELECT c.brand_name AS brand, c.model_name AS model, cr.average_rating AS rating " +
                    "FROM cars c " +
                    "JOIN car_ratings cr ON c.id = cr.car_id";

            try (Statement stmt = conn.createStatement();
                 ResultSet resultSet = stmt.executeQuery(query)) {

                while (resultSet.next()) {
                    String brand = resultSet.getString("brand");
                    String model = resultSet.getString("model");
                    double rating = resultSet.getDouble("rating");
                    carData.add(new CarData(brand, model, rating));
                }
            }

            return carData;
        }
    }

    // CarData Class
    public static class CarData {
        private final String brand;
        private final String model;
        private final double rating;

        public CarData(String brand, String model, double rating) {
            this.brand = brand;
            this.model = model;
            this.rating = rating;
        }

        public String getBrand() {
            return brand;
        }

        public String getModel() {
            return model;
        }

        public double getRating() {
            return rating;
        }
    }
}


