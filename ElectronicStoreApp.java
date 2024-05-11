import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ElectronicStoreApp extends Application {

    private Button resetButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ElectronicStore electronicStore = ElectronicStore.createStore();
        primaryStage.setTitle("Electronic Store Application - " + electronicStore.getName());
        BorderPane borderPane = getBorderPane(electronicStore); // Create the main layout using a BorderPane
        Scene scene = new Scene(borderPane, 800, 400); // Create a scene with the main layout and set its size
        resetButton.setOnAction(new EventHandler<ActionEvent>() { // Add an event handler for the reset button
            @Override
            public void handle(ActionEvent event) {
                BorderPane borderPane = getBorderPane(electronicStore); // Get a new instance of the main layout
                Scene scene = new Scene(borderPane,800,400); // Create a new scene with the updated main layout
                primaryStage.setScene(scene); // Set the scene of the primaryStage to the updated scene
            }
        });
        primaryStage.setScene(scene); // Set the scene of the primaryStage
        primaryStage.setResizable(false); // Set the resizable property of primaryStage to false
        primaryStage.show(); // Show the primaryStage
    }

    private BorderPane getBorderPane(ElectronicStore electronicStore) {
        BorderPane borderPane = new BorderPane(); // Create a BorderPane as the main layout

        VBox leftBox = new VBox(); // Create a VBox for the left section of the BorderPane
        Text text = new Text("Store Summary:"); // Create a Text node
        leftBox.setAlignment(Pos.TOP_CENTER); // Set the alignment of the VBox to TOP_CENTER
        leftBox.getChildren().add(text); // Add the Text node to the VBox
        GridPane gridPane = new GridPane(); // Create a GridPane for displaying summary information

        Label label1 = new Label("# Sales:"); // Create a Label for displaying number of sales
        gridPane.add(label1, 0, 1); // Add the Label to the GridPane at column 0, row 1
        TextField salesField = new TextField(); // Create a TextField for displaying the number of sales
        salesField.setPrefWidth(70); // Set the preferred width of the TextField
        salesField.setText("0"); // Set the initial value of the TextField
        gridPane.add(salesField, 1, 1); // Add the TextField to the GridPane at column 1, row 1

// Create and configure UI components for displaying store revenue
        Label label2 = new Label("Revenue:");
        gridPane.add(label2, 0, 2);
        TextField revenueField = new TextField();
        revenueField.setPrefWidth(70);
        revenueField.setText("0.00");
        gridPane.add(revenueField, 1, 2);

// Create and configure UI components for displaying revenue per sale
        Label label3 = new Label("$ / Sale:");
        gridPane.add(label3, 0, 3);
        TextField saleDollarField = new TextField();
        saleDollarField.setPrefWidth(70);
        saleDollarField.setText("N/A");
        gridPane.add(saleDollarField, 1, 3);

        gridPane.setAlignment(Pos.TOP_CENTER);
        leftBox.setMargin(gridPane, new Insets(5, 5, 5, 5));
        leftBox.getChildren().add(gridPane);

// Create and configure UI components for displaying most popular items
        Text text1 = new Text("Most Popular Items:");
        leftBox.getChildren().add(text1);
        ListView popularItemsList = new ListView();
        popularItemsList.setPrefHeight(200);
        for (int i = 0; i < 3; i++) {
            if (electronicStore.stock[i] == null) continue;
            popularItemsList.getItems().add(electronicStore.stock[i].toString());
        }
        leftBox.setMargin(popularItemsList, new Insets(5, 5, 5, 5));
        leftBox.getChildren().add(popularItemsList);

// Create and configure UI components for the "Reset Store" button
        resetButton = new Button("Reset Store");
        leftBox.getChildren().add(resetButton);

        leftBox.setPrefWidth(200);
        borderPane.setLeft(leftBox);
        BorderPane.setMargin(leftBox, new Insets(5, 5, 5, 5));

// Create and configure UI components for displaying store stock
        VBox centerBox = new VBox();
        Text storeStockText = new Text("Store Stock:");
        centerBox.setAlignment(Pos.TOP_CENTER);
        centerBox.getChildren().add(storeStockText);
        ListView storeStockList = new ListView();
        storeStockList.setPrefHeight(300);
        for (Product product : electronicStore.stock) {
            if (product == null) break;
            storeStockList.getItems().add(product);
        }

        centerBox.setMargin(storeStockList, new Insets(5, 5, 5, 5)); // Set margin for storeStockList in centerBox
        centerBox.getChildren().add(storeStockList); // Add storeStockList to centerBox
        centerBox.setPrefWidth(350); // Set preferred width for centerBox
        Button addButton = new Button("Add to Cart"); // Create a new Button with text "Add to Cart"
        addButton.setDisable(true); // Set addButton initially disabled
        centerBox.getChildren().add(addButton); // Add addButton to centerBox
        storeStockList.setOnMouseClicked(new EventHandler<MouseEvent>() { // Set mouse click event handler for storeStockList
            @Override
            public void handle(MouseEvent event) {
                Object obj = storeStockList.getSelectionModel().getSelectedItem();
                if (obj != null) {
                    addButton.setDisable(false); // Enable addButton when an item is selected in storeStockList
                } else {
                    addButton.setDisable(true); // Disable addButton when no item is selected in storeStockList
                }
            }
        });
        borderPane.setCenter(centerBox); // Set centerBox as the center node of borderPane
        borderPane.setAlignment(centerBox, Pos.TOP_CENTER); // Set alignment for centerBox in borderPane
        BorderPane.setMargin(centerBox, new Insets(5, 5, 5, 5)); // Set margin for centerBox in borderPane

// Code for right box (currentCartList and related buttons)
        VBox rightBox = new VBox(); // Create a new VBox for right side of borderPane
        Text currentCartText = new Text("Current Cart ($0.00):"); // Create a new Text node for displaying current cart total
        rightBox.setAlignment(Pos.TOP_CENTER); // Set alignment for rightBox
        rightBox.getChildren().add(currentCartText); // Add currentCartText to rightBox
        ListView currentCartList = new ListView(); // Create a new ListView for displaying current cart items
        currentCartList.setPrefHeight(300); // Set preferred height for currentCartList
        rightBox.getChildren().add(currentCartList); // Add currentCartList to rightBox
        centerBox.setMargin(currentCartList, new Insets(5, 5, 5, 5)); // Set margin for currentCartList in centerBox
        HBox hBox = new HBox(); // Create a new HBox for buttons in rightBox
        hBox.setAlignment(Pos.TOP_CENTER); // Set alignment for hBox
        Button removeButton = new Button("Remove from Cart"); // Create a new Button with text "Remove from Cart"
        removeButton.setDisable(true); // Set removeButton initially disabled
        hBox.getChildren().add(removeButton); // Add removeButton to hBox
        Button completeButton = new Button("Complete Sale"); // Create a new Button with text "Complete Sale"
        completeButton.setDisable(true); // Set completeButton initially disabled
        hBox.getChildren().add(completeButton); // Add completeButton to hBox
        rightBox.getChildren().add(hBox); // Add hBox to rightBox
        rightBox.setPrefWidth(250); // Set preferred width for rightBox
        borderPane.setRight(rightBox); // Set rightBox as the right node of borderPane
        BorderPane.setMargin(rightBox, new Insets(5, 5, 5, 5)); // Set margin for rightBox in borderPane

        currentCartList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Object obj = currentCartList.getSelectionModel().getSelectedItem();
                if (obj != null) {
                    removeButton.setDisable(false);
                } else {
                    removeButton.setDisable(true);
                }
            }
        });

        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Object obj = storeStockList.getSelectionModel().getSelectedItem();
                if (obj != null) {
                    currentCartList.getItems().add(obj);
                    completeButton.setDisable(false);
                    storeStockList.getItems().remove(obj);
                    String t = currentCartText.getText();
                    double currentVal = Double.parseDouble(t.substring(t.indexOf('$')+1,t.indexOf(')')));
                    currentVal += ((Product)obj).getPrice();
                    currentCartText.setText("Current Cart ($"+currentVal+"):");
                    if (storeStockList.getItems().size() == 0) {
                        addButton.setDisable(true);
                    }
                }
            }
        });

        removeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Object obj = currentCartList.getSelectionModel().getSelectedItem();
                if (obj != null) {
                    currentCartList.getItems().remove(obj);
                    storeStockList.getItems().add(obj);
                    String t = currentCartText.getText();
                    double currentVal = Double.parseDouble(t.substring(t.indexOf('$')+1,t.indexOf(')')));
                    currentVal -= ((Product)obj).getPrice();
                    currentCartText.setText("Current Cart ($"+currentVal+"):");
                    if (currentCartList.getItems().size() == 0) {
                        removeButton.setDisable(true);
                        completeButton.setDisable(true);
                    }

                }
            }
        });

        return borderPane;
    }
}