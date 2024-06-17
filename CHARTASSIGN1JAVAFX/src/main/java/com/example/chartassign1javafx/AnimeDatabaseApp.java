package com.example.chartassign1javafx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AnimeDatabaseApp extends Application {

    private Stage primaryStage;
    private Scene mainScene, pieScene, barScene;
    private TableView<GenreInfo> genreTable;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        Image appIcon = new Image(getClass().getResourceAsStream("/com/example/chartassign1javafx/nezukoicon.jpg"));
        primaryStage.getIcons().add(appIcon);

        String dbUrl = "jdbc:mysql://localhost:3306/anime_db";
        String dbUser = "root";
        String dbPassword = "";
        Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        genreTable = initialiseTableView();

        PieChart pieChart = generatePieChart(connection);
        BarChart<String, Number> barChart = generateBarChart(connection);

        Button showPieChartButton = new Button("Show Pie Chart");
        showPieChartButton.setOnAction(e -> primaryStage.setScene(pieScene));

        Button showBarChartButton = new Button("Show Bar Chart");
        showBarChartButton.setOnAction(e -> primaryStage.setScene(barScene));

        Button backToTableButton1 = new Button("Back to Table View");
        backToTableButton1.setOnAction(e -> primaryStage.setScene(mainScene));

        Button showPieChartButton2 = new Button("Switch to Pie Chart View");
        showPieChartButton2.setOnAction(e -> primaryStage.setScene(pieScene));

        Button backToTableButton3 = new Button("Back to Table View");
        backToTableButton3.setOnAction(e -> primaryStage.setScene(mainScene));

        StackPane buttonPane1 = new StackPane(showPieChartButton, showBarChartButton);
        buttonPane1.setAlignment(Pos.CENTER);
        VBox tableLayout = new VBox(10);
        tableLayout.getChildren().addAll(genreTable, buttonPane1);
        tableLayout.setAlignment(Pos.CENTER);
        mainScene = new Scene(tableLayout, 800, 600);
        mainScene.getStylesheets().add(getClass().getResource("/com/example/chartassign1javafx/styles.css").toExternalForm());

        StackPane buttonPane2 = new StackPane(backToTableButton1);
        buttonPane2.setAlignment(Pos.CENTER);
        VBox pieLayout = new VBox(10);
        pieLayout.getChildren().addAll(pieChart, buttonPane2);
        pieLayout.setAlignment(Pos.CENTER);
        pieScene = new Scene(pieLayout, 800, 600);
        pieScene.getStylesheets().add(getClass().getResource("/com/example/chartassign1javafx/styles.css").toExternalForm());

        StackPane buttonPane3 = new StackPane(showPieChartButton2);
        buttonPane3.setAlignment(Pos.CENTER);
        VBox barLayout = new VBox(10);
        barLayout.getChildren().addAll(barChart, buttonPane3);
        barLayout.setAlignment(Pos.CENTER);
        barScene = new Scene(barLayout, 800, 600);
        barScene.getStylesheets().add(getClass().getResource("/com/example/chartassign1javafx/styles.css").toExternalForm());

        connection.close();

        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Anime Genres Information");
        primaryStage.show();
    }

    private TableView<GenreInfo> initialiseTableView() throws Exception {
        TableView<GenreInfo> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<GenreInfo, String> genreColumn = new TableColumn<>("Genre");
        TableColumn<GenreInfo, String> descriptionColumn = new TableColumn<>("Description");
        TableColumn<GenreInfo, Double> ratingColumn = new TableColumn<>("Average Rating (out of 10)");

        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));

        tableView.getColumns().addAll(genreColumn, descriptionColumn, ratingColumn);

        String query = "SELECT g.genre_name AS genre, g.description AS description, gr.average_rating AS rating " +
                "FROM genres g " +
                "JOIN genre_ratings gr ON g.id = gr.genre_id " +
                "WHERE g.genre_name NOT IN ('Ecchi', 'Yaoi', 'Yuri')";

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/anime_db", "root", "");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        List<GenreInfo> genreList = new ArrayList<>();
        while (resultSet.next()) {
            String genre = resultSet.getString("genre");
            String description = resultSet.getString("description");
            double rating = resultSet.getDouble("rating");
            GenreInfo genreData = new GenreInfo(genre, description, rating);
            genreList.add(genreData);
        }

        tableView.getItems().addAll(genreList);

        resultSet.close();
        statement.close();
        connection.close();

        return tableView;
    }

    private PieChart generatePieChart(Connection connection) throws Exception {
        String query = "SELECT genre_name AS genre, average_rating AS rating FROM genres " +
                "INNER JOIN genre_ratings ON genres.id = genre_ratings.genre_id";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        while (resultSet.next()) {
            String genre = resultSet.getString("genre");
            double rating = resultSet.getDouble("rating");
            pieData.add(new PieChart.Data(genre, rating));
        }

        resultSet.close();
        statement.close();

        PieChart pieChart = new PieChart(pieData);
        pieChart.setTitle("Genre Ratings Pie Chart");

        return pieChart;
    }

    private BarChart<String, Number> generateBarChart(Connection connection) throws Exception {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Genre");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Average Rating");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Genre Ratings Bar Chart");

        String query = "SELECT genre_name AS genre, average_rating AS rating FROM genres " +
                "INNER JOIN genre_ratings ON genres.id = genre_ratings.genre_id";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Genre Ratings");

        while (resultSet.next()) {
            String genre = resultSet.getString("genre");
            double rating = resultSet.getDouble("rating");
            series.getData().add(new XYChart.Data<>(genre, rating));
        }

        resultSet.close();
        statement.close();

        barChart.getData().add(series);

        return barChart;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static class GenreInfo {
        private final String genre;
        private final String description;
        private final double rating;

        public GenreInfo(String genre, String description, double rating) {
            this.genre = genre;
            this.description = description;
            this.rating = rating;
        }

        public String getGenre() {
            return genre;
        }

        public String getDescription() {
            return description;
        }

        public double getRating() {
            return rating;
        }
    }
}
