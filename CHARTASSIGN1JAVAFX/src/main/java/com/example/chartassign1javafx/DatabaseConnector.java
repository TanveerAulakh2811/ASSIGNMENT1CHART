package com.example.chartassign1javafx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnector extends Application {

    private Stage stage;
    private Scene tableScene, pieChartScene;
    private TableView<ImmigrationData> tableView;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        Image icon = new Image(getClass().getResourceAsStream("/com/example/chartassign1javafx/nezukoicon.jpg"));

        // Set application icon
        stage.getIcons().add(icon);

        // Connect to MySQL database (replace with your database credentials)
        String url = "jdbc:mysql://localhost:3306/immigration_db";
        String user = "root";
        String password = "";
        Connection conn = DriverManager.getConnection(url, user, password);

        // Set up TableView for immigration data
        tableView = createTableView(conn);

        // Set up PieChart for immigration data
        PieChart pieChart = createPieChart(conn);

        // Button to switch to PieChart scene
        Button switchToPieChartButton = new Button("Switch to Pie Chart");
        switchToPieChartButton.setOnAction(e -> stage.setScene(pieChartScene));

        // Button to switch back to TableView scene
        Button switchToTableViewButton = new Button("Back to Table View");
        switchToTableViewButton.setOnAction(e -> stage.setScene(tableScene));

        // Layout for TableView scene
        VBox tableLayout = new VBox(10);
        tableLayout.getChildren().addAll(tableView, switchToPieChartButton);
        tableScene = new Scene(tableLayout, 800, 600);

        // Layout for PieChart scene
        VBox pieChartLayout = new VBox(10);
        pieChartLayout.getChildren().addAll(pieChart, switchToTableViewButton);
        pieChartScene = new Scene(pieChartLayout, 800, 600);

        // Close database connection
        conn.close();

        // Set initial scene
        stage.setScene(tableScene);
        stage.setTitle("Immigration Statistics");
        stage.show();
    }

    private TableView<ImmigrationData> createTableView(Connection conn) throws Exception {
        TableView<ImmigrationData> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Define columns
        TableColumn<ImmigrationData, String> yearColumn = new TableColumn<>("Year");
        TableColumn<ImmigrationData, Integer> numberColumn = new TableColumn<>("Number of Immigrants");

        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));

        tableView.getColumns().addAll(yearColumn, numberColumn);

        // Query data from database
        String query = "SELECT year, number FROM immigration_stats";

        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);

        // Populate table with data
        List<ImmigrationData> immigrationList = new ArrayList<>();
        while (resultSet.next()) {
            String year = resultSet.getString("year");
            int number = resultSet.getInt("number");
            ImmigrationData immigrationData = new ImmigrationData(year, number);
            immigrationList.add(immigrationData);
        }

        tableView.getItems().addAll(immigrationList);

        // Close ResultSet and Statement
        resultSet.close();
        stmt.close();

        return tableView;
    }

    private PieChart createPieChart(Connection conn) throws Exception {
        // Query data from database
        String query = "SELECT year, number FROM immigration_stats";

        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);

        // Prepare data for pie chart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        while (resultSet.next()) {
            String year = resultSet.getString("year");
            int number = resultSet.getInt("number");
            pieChartData.add(new PieChart.Data(year, number));
        }

        // Close ResultSet and Statement
        resultSet.close();
        stmt.close();

        // Create PieChart
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Immigration Statistics Pie Chart");

        return pieChart;
    }

    public static void main(String[] args) {
        launch(args);
    }

    // ImmigrationData class for TableView
    public static class ImmigrationData {
        private final String year;
        private final int number;

        public ImmigrationData(String year, int number) {
            this.year = year;
            this.number = number;
        }

        public String getYear() {
            return year;
        }

        public int getNumber() {
            return number;
        }
    }
}
