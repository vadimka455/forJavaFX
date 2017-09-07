package sample;

import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Entity.Potatoes;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main extends Application {

    private static Locale locale = Constans.RUS;
    private static AnchorPane pane4 = new AnchorPane();
    private static CheckBox checkbox1 = new CheckBox();
    private static CheckBox checkbox2 = new CheckBox();
    private static Commands commands = new Commands();
    private static Connect connect = commands.getConnect();
    private MenuBar menuBar = new MenuBar();
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("GUITexts", locale);
    private Menu file = new Menu();
    private Menu nothing = new Menu();
    private Menu nothingLanguage = new Menu("Language");
    private Menu menuhelp = new Menu();
    private MenuItem fileConnect = new MenuItem(resourceBundle.getString("connect"));
    private MenuItem nothingLanguageRus = new MenuItem("Russia");
    private MenuItem nothingLanguageCat = new MenuItem("Cat");
    private MenuItem nothingLanguageEsp = new MenuItem("Spain");
    private MenuItem nothingLanguageNL = new MenuItem("Netherlandish");
    private MenuItem fileSaveAs = new MenuItem(resourceBundle.getString("saveAs"));
    private MenuItem fileDef = new MenuItem("Default objects");
    private MenuItem fileExit = new MenuItem(resourceBundle.getString("exit"));
    private MenuItem helpInstr = new MenuItem(resourceBundle.getString("instructions"));
    private AnchorPane pane1 = new AnchorPane();
    private AnchorPane pane2 = new AnchorPane();
    private AnchorPane pane3 = new AnchorPane();
    private ToggleButton toggleButton = new ToggleButton();
    private Button button1 = new Button();
    private Button button2 = new Button();
    private Button button3 = new Button();
    private Button button4 = new Button();
    private long buttonsClick = 0;
    private GridPane grid = new GridPane();
    private Text scenetitle = new Text(resourceBundle.getString("enterItem"));
    private Label color = new Label(resourceBundle.getString("colorObject"));
    private Button sendItem = new Button(resourceBundle.getString("sendItem"));
    private Label weight = new Label(resourceBundle.getString("weightObject"));
    private TextField colorTextField = new TextField();
    private Properties config = new Properties();

    public static void main(String[] args) {
        LauncherImpl.launchApplication(Main.class, MyPreloader.class, args);
    }

    static void setTreeView(LinkedList<Potatoes> clubni) {
        ResourceBundle resourceBundle1 = ResourceBundle.getBundle("GUITexts", locale);
        Iterator<Potatoes> iterator = clubni.iterator();
        Potatoes potatoes;
        TreeItem<String> rootItem = new TreeItem<>(resourceBundle1.getString("tubers"));
        rootItem.setExpanded(true);
        while (iterator.hasNext()) {
            potatoes = iterator.next();
            TreeItem<String> rootItem2 = new TreeItem<>(resourceBundle1.getString("potatoe") + " #" + potatoes.getId());
            rootItem2.setExpanded(true);
            TreeItem<String> item;
            if (checkbox1.isSelected()) {
                item = new TreeItem<>(resourceBundle1.getString("colorObject") + ": " + potatoes.getColor());
                rootItem2.getChildren().add(item);
            }
            if (checkbox2.isSelected()) {
                item = new TreeItem<>(resourceBundle1.getString("weightObject") + ": " + potatoes.getWeight());
                rootItem2.getChildren().add(item);
            }
            String zonedDateTime = "";
            DateTimeFormatter rusFormatter = DateTimeFormatter.ofPattern("E, dd MMMM yyyy HH:mm", Constans.RUS);
            DateTimeFormatter elseFormatter = DateTimeFormatter.ofPattern("E, MMMM dd, yyyy HH:mm", locale);
            if (locale == Constans.RUS) {
                zonedDateTime = potatoes.getZonedDateTime().format(rusFormatter);
            } else {
                zonedDateTime = potatoes.getZonedDateTime().format(elseFormatter);
            }
            rootItem2.getChildren().add(new TreeItem<>(resourceBundle1.getString("when") + ": " + zonedDateTime));
            rootItem.getChildren().add(rootItem2);
        }
        TreeView<String> tree = new TreeView<>(rootItem);
        tree.setStyle("-fx-background-color: #1d1d1d");
        pane4.getChildren().add(tree);
        AnchorPane.setRightAnchor(tree, 0.0);
        AnchorPane.setTopAnchor(tree, 0.0);
        AnchorPane.setLeftAnchor(tree, 0.0);
        AnchorPane.setBottomAnchor(tree, 0.0);

    }

    private void setSoundsForButton(Button button) {
        button.setOnMouseClicked(event -> new Audio(Main.class.getResourceAsStream("/ChuToy.wav")).play());
    }

    private void showhelp() {
        Alert alert = new Alert(Alert.AlertType.NONE, resourceBundle.getString("helpmessage"), ButtonType.OK);
//        DialogPane dialogPane = alert.getDialogPane();
//        for (ButtonType type : dialogPane.getButtonTypes()) {
//            ((Button) dialogPane.lookupButton(type)).setOnAction(e -> {
//                dialogPane.getScene().getWindow().hide();
//            });
//        }
//        TODO посмотреть этот кусок
//        dialogPane.getScene().setRoot(new Label());
//        Scene scene = new Scene(dialogPane);
//        Stage dialog = new Stage();
//        dialog.setScene(scene);
//        dialog.setTitle(resourceBundle.getString("instructions"));
//        dialog.getIcons().add(new Image("alerticon.png"));
//        dialog.showAndWait();
        //new Audio(Main.class.getResourceAsStream("/ifmore10.wav")).play();
        alert.showAndWait();
    }

    private void anchorSetPosition(Node node) {
        AnchorPane.setRightAnchor(node, 0.0);
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
    }

    private void ifmore10(Button button) {
        if (button.getText().equals(button3.getText()) || button.getText().equals(button4.getText())) {
            if (connect.getListOfPotatoes().size() == 0) {
                buttonsClick++;
            } else {
                buttonsClick = 0;
            }
            if (buttonsClick >= 10) {
                Alert alert = new Alert(Alert.AlertType.NONE, resourceBundle.getString("youAttack"), ButtonType.OK);
                //new Audio(Main.class.getResourceAsStream("/ifmore10.wav")).play();
                alert.showAndWait();

            }
        }


    }

    private void addListenerConnectButton(MenuItem button) {
        button.setOnAction(event -> commands.getPotatoes());
    }

    private void setStartedObject() {
        try {
            new Processing().getSteal().stealPotatoes();
        } catch (Exception ignored) {
        }
    }

    private void writeForm2(Button button) {
        TextField weightTextField = new TextField("");
        Label wrongcolor = new Label("");

        Label wrongWeight = new Label("");
        pane3.getChildren().remove(grid);
        grid = new GridPane();
//        grid.getChildren().removeAll(wrongcolor,wrongWeight);
//        pane3=new AnchorPane();


        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));


        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        scenetitle.setFill(Color.BROWN);
        grid.add(scenetitle, 0, 0, 3, 1);

        wrongcolor.setTextFill(Color.RED);
        grid.add(wrongcolor, 2, 1);

        color.setTextFill(Color.web("#008287"));
        grid.add(color, 0, 1);


        colorTextField.setPromptText(resourceBundle.getString("colorOfPotatoe"));
        colorTextField.textProperty().addListener((observableValue1, oldText1, newText1) -> {
            if (!newText1.isEmpty()) {
                if (!weightTextField.getText().isEmpty()) {
                    sendItem.setDisable(false);
                }
                sendItem.setDisable(false);
                if (newText1.length() <= 20) {
                    colorTextField.setText(newText1);
                    wrongcolor.setText("");
                } else {
                    colorTextField.setText(oldText1);
                    wrongcolor.setText(resourceBundle.getString("max20sym"));
                }
            } else {
                sendItem.setDisable(true);
            }
        });


        grid.add(colorTextField, 1, 1);

        grid.add(weight, 0, 2);
        weight.setTextFill(Color.web("#008287"));
        wrongWeight.setTextFill(Color.RED);
        wrongcolor.setFont(Font.font("Comic Sans MS", FontPosture.ITALIC, -1));
        wrongWeight.setFont(Font.font("Comic Sans MS", FontPosture.ITALIC, -1));
        grid.add(wrongWeight, 2, 2);
        weightTextField.setPromptText(resourceBundle.getString("weightOfPotatoes"));
        String numberMatcher = "^?\\d+$";
        weightTextField.textProperty().addListener((observableValue, oldText, newText) -> {
            if (!newText.isEmpty()) {
                if (!colorTextField.getText().isEmpty()) {
                    sendItem.setDisable(false);
                }
                if (!newText.matches(numberMatcher)) {
                    weightTextField.setText(oldText);
                    wrongWeight.setText(resourceBundle.getString("onlyInt"));
                } else {
                    try {
                        int value = Integer.parseInt(newText);
                        weightTextField.setText(String.valueOf(value));
                        wrongWeight.setText("");
                    } catch (NumberFormatException e) {
                        weightTextField.setText(oldText);
                    }
                }
            }
        });
        grid.add(weightTextField, 1, 2);
        sendItem.setDefaultButton(true);
        HBox hBoxSendItem = new HBox(10);
        hBoxSendItem.setAlignment(Pos.BOTTOM_LEFT);
        hBoxSendItem.getChildren().add(sendItem);
        grid.add(hBoxSendItem, 2, 4);
        pane3.getChildren().add(grid);
        sendItem.setOnAction(event -> {
            if (!(colorTextField.getText().isEmpty() || weightTextField.getText().isEmpty())) {
                pane3.getChildren().removeAll(grid);
                String[] values = {colorTextField.getText(), weightTextField.getText()};
                String string = "{\"weight\":" + values[1] + ",\"color\":\"" + values[0] + "\"}";

                if (button.getText().equals(button1.getText())) {
                    commands.add_if_max(string, connect.getListOfPotatoes());
                }
                if (button.getText().equals(button2.getText())) {
                    commands.remove_greater(string, connect.getListOfPotatoes());
                }
            } else {
                sendItem.setDisable(true);
                if (colorTextField.getText().isEmpty()) {
                    wrongcolor.setText("Введите цвет");
                }
                if (weightTextField.getText().isEmpty()) {
                    wrongWeight.setText("Введите вес");
                }

            }

        });
    }

    private void addListenerEditButtons(Button button) {
        button.setOnAction(event -> {
            writeForm2(button);
        });
    }

    /*private void firstLaunching(){
        try {
            config.load(Main.class.getResourceAsStream("/Config.properties"));

            if ("false".equals(config.getProperty("firstLaunching"))){
                //
                //TODO сделать предзагрузку
            }else{
                config.setProperty("firstLaunching","false");
                VBox vBox = new VBox();
                HBox hBox = new HBox();
                Label label = new Label("Выберите язык: ");
                ChoiceBox cb = new ChoiceBox( );
                cb.getItems().addAll("Русский", "Español", "Català","Nederlands");
                HBox hBox1=new HBox();
                Label label1 = new Label("Выберите тему: ");
                ChoiceBox cb1 = new ChoiceBox();
                cb1.getItems().addAll("Dark", "Light");

                hBox1.getChildren().addAll(label1,cb1);
                hBox.getChildren().addAll(label, cb);
                Button button= new Button("Apply");
                vBox.getChildren().addAll(hBox,hBox1,button);
                Stage stage = new Stage();
                stage.setScene(new Scene(vBox,400,200));
                button.setOnAction(event -> {
                    stage.hide();
                    String result1 = cb.getSelectionModel().getSelectedItem().toString();
                    String result2 = cb1.getSelectionModel().getSelectedItem().toString();
                    config.setProperty("Theme",result2);
                    config.setProperty("language",result1);
                    try {
                        config.store(new FileOutputStream(Main.class.getResource("/Config.properties").getPath()),"UTF-8");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });

                stage.showAndWait();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private void addListenerForLanguageButton(MenuItem menuItem) {
        menuItem.setOnAction(event -> {
            switch (menuItem.getText()) {
                case ("Russia"): {
                    locale = Constans.RUS;
                    break;
                }
                case ("Spain"): {
                    locale = Constans.ES_HN;
                    break;
                }
                case ("Cat"): {
                    locale = Constans.CAT;
                    break;
                }
                case ("Netherlandish"): {
                    locale = Constans.NL;
                    break;
                }
            }
            resourceBundle = ResourceBundle.getBundle("GUITexts", locale);
            setLanguageForButton();
        });

    }

    private void setLanguageForButton() {
        file.setText(resourceBundle.getString("fileGui"));
        fileConnect.setText(resourceBundle.getString("connect"));
        fileExit.setText(resourceBundle.getString("exit"));
        fileSaveAs.setText(resourceBundle.getString("saveAs"));
        nothing.setText(resourceBundle.getString("Settings"));
        menuhelp.setText(resourceBundle.getString("help"));
        helpInstr.setText(resourceBundle.getString("instructions"));
        button1.setText(resourceBundle.getString("addIfMax"));
        button2.setText(resourceBundle.getString("removeGreater"));
        button3.setText(resourceBundle.getString("removeFirst"));
        button4.setText(resourceBundle.getString("removeLast"));
        checkbox1.setText(resourceBundle.getString("colorObject"));
        checkbox2.setText(resourceBundle.getString("weightObject"));
        if (toggleButton.isSelected()) {
            toggleButton.setText(resourceBundle.getString("lightTheme"));
        } else {
            toggleButton.setText(resourceBundle.getString("darkTheme"));
        }
        if (!connect.getListOfPotatoes().isEmpty()) {
            setTreeView(connect.getListOfPotatoes());
        }
        scenetitle = new Text(resourceBundle.getString("enterItem"));
        color = new Label(resourceBundle.getString("colorObject"));
        sendItem = new Button(resourceBundle.getString("sendItem"));
        weight = new Label(resourceBundle.getString("weightObject"));
        button1.setTooltip(new Tooltip(resourceBundle.getString("helpAddIfMax")));
        button2.setTooltip(new Tooltip(resourceBundle.getString("helpRemoveGreater")));
        button3.setTooltip(new Tooltip(resourceBundle.getString("helpRemoveFirst")));
        button4.setTooltip(new Tooltip(resourceBundle.getString("helpRemoveLast")));
    }

    private void addListenerSaveAs(MenuItem menuItem, Stage primaryStage) {

        menuItem.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                    "JSON files", "*.json"));
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                commands.save(new Processing().getSteal().getClubni(), file);
            }
        });
    }

    private void addListenerCheckBoxs(CheckBox checkBox) {
        checkBox.setOnAction(event -> {
            if (!connect.getListOfPotatoes().isEmpty()) {
                setTreeView(connect.getListOfPotatoes());
            }
        });
    }

    private boolean ifHaveMorethanwant(LinkedList<Potatoes> clubni) {
        return true;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setLanguageForButton();
//        firstLaunching();
        Image image1 = new Image("mainicon.png");
        Image image2 = new Image("mainicon2.png");
        System.out.println("Welcome to the league of Draven");
        pane1.setStyle("-fx-background-color:#3a3a3a;-fx-alignment: baseline-right");
        pane1.setStyle("-fx-background-color:#3a3a3a;-fx-alignment: baseline-right");
        pane3.setStyle("-fx-border-color:#3a3a3a; -fx-border-width: 0 1 0 1 ");

        GridPane root = new GridPane();
        root.add(pane1, 0, 0, 3, 1);
        root.add(pane2, 0, 1);
        root.add(pane3, 1, 1);
        root.add(pane4, 2, 1);
        root.setStyle("-fx-background-color:#1d1d1d");


        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(14);
        ColumnConstraints columnConstraints1 = new ColumnConstraints();
        columnConstraints1.setPercentWidth(55);
        ColumnConstraints columnConstraints2 = new ColumnConstraints();
        columnConstraints2.setPercentWidth(31);
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(6);
        RowConstraints rowConstraints1 = new RowConstraints();
        rowConstraints1.setPercentHeight(94);
        root.getColumnConstraints().addAll(columnConstraints, columnConstraints1, columnConstraints2);
        root.getRowConstraints().addAll(rowConstraints, rowConstraints1);


        button1.setPrefSize(0.14 * 930, -1);
        button1.setTextAlignment(TextAlignment.CENTER);
        button2.setTextAlignment(TextAlignment.CENTER);
        button3.setTextAlignment(TextAlignment.CENTER);
        button4.setTextAlignment(TextAlignment.CENTER);
        button2.setPrefSize(0.14 * 930, -1);
        button3.setPrefSize(0.14 * 930, -1);
        button4.setPrefSize(0.14 * 930, -1);
        button1.setTooltip(new Tooltip(resourceBundle.getString("helpAddIfMax")));
        button2.setTooltip(new Tooltip(resourceBundle.getString("helpRemoveGreater")));
        button3.setTooltip(new Tooltip(resourceBundle.getString("helpRemoveFirst")));
        button4.setTooltip(new Tooltip(resourceBundle.getString("helpRemoveLast")));
        checkbox1.setSelected(true);
        checkbox2.setSelected(true);
        setSoundsForButton(button1);
        setSoundsForButton(button2);
        setSoundsForButton(button3);
        setSoundsForButton(button4);


        FlowPane flowPane = new FlowPane(Orientation.VERTICAL);
        flowPane.getChildren().addAll(button1, button2, button3, button4);
        flowPane.setVgap(1);
        flowPane.getChildren().add(checkbox1);
        flowPane.getChildren().add(checkbox2);
        flowPane.setAlignment(Pos.TOP_CENTER);
        pane2.getChildren().add(flowPane);
        anchorSetPosition(flowPane);
        menuBar.getMenus().addAll(file, nothing, menuhelp);
        fileConnect.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
        nothingLanguage.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
//        fileDef.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN));
        fileExit.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));
        fileSaveAs.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
        helpInstr.setAccelerator(new KeyCodeCombination(KeyCode.F1));
        file.getItems().addAll(fileConnect, fileSaveAs, fileExit);
        nothingLanguage.getItems().addAll(nothingLanguageCat, nothingLanguageEsp, nothingLanguageRus, nothingLanguageNL);
        menuhelp.getItems().add(helpInstr);
        nothing.getItems().add(nothingLanguage);
        addListenerConnectButton(fileConnect);
        addListenerSaveAs(fileSaveAs, primaryStage);


        addListenerForLanguageButton(nothingLanguageCat);
        addListenerForLanguageButton(nothingLanguageEsp);
        addListenerForLanguageButton(nothingLanguageRus);
        addListenerForLanguageButton(nothingLanguageNL);
        fileDef.setOnAction(event -> {
            pane3.getChildren().clear();
            try {
                if (!ifHaveMorethanwant(new Processing().getSteal().getClubni())) {
                    return;
                }
                commands.add_default_elements(new Processing().getSteal().getClubni());

            } catch (Exception ignored) {
            }
        });
        fileExit.setOnAction(event -> {
            //new Audio(Main.class.getResourceAsStream("/Exit.wav")).play();

            try {
                double millis = 3000;
                double opacity = 1.0F;
                while (millis > 0) {
                    Thread.sleep(3);
                    millis -= 3;
                    opacity -= 0.001F;
                    primaryStage.setOpacity(opacity);
                }
            } catch (Exception ignored) {
            }
            System.exit(1);
            primaryStage.hide();
        });
        helpInstr.setOnAction((ActionEvent event) -> showhelp());


        addListenerCheckBoxs(checkbox1);
        addListenerCheckBoxs(checkbox2);


        pane1.getChildren().add(menuBar);
        AnchorPane.setLeftAnchor(menuBar, 10.0);
        AnchorPane.setTopAnchor(menuBar, 5.0);
        AnchorPane.setBottomAnchor(menuBar, 5.0);


        pane1.getChildren().add(toggleButton);
        AnchorPane.setRightAnchor(toggleButton, 10.0);
        AnchorPane.setTopAnchor(toggleButton, 5.0);
        AnchorPane.setBottomAnchor(toggleButton, 5.0);
        addListenerEditButtons(button1);
        addListenerEditButtons(button2);
        button3.setOnAction(event -> {
            pane3.getChildren().clear();
            commands.remove_first(new Processing().getSteal().getClubni());
            ifmore10(button3);
        });
        button4.setOnAction(event -> {
            pane3.getChildren().clear();
            commands.remove_last(new Processing().getSteal().getClubni());
            ifmore10(button4);
        });
        toggleButton.setOnAction(event -> {


            if (toggleButton.isSelected()) {
                toggleButton.setText(resourceBundle.getString("lightTheme"));
                primaryStage.getScene().getStylesheets().remove("DarkTheme.css");
                primaryStage.getScene().getStylesheets().add("LightTheme.css");
                pane1.setStyle("-fx-background-color:#919191;-fx-alignment: baseline-right"); //TODO вынести в CSS
                pane3.setStyle("-fx-border-color:#919191; -fx-border-width: 0 1 0 1 ");
                root.setStyle("-fx-background-color:white");
                primaryStage.setTitle("Bear in white");
                primaryStage.getIcons().remove(image1);
                primaryStage.getIcons().add(image2);


            } else {
                toggleButton.setText(resourceBundle.getString("darkTheme"));

                pane1.setStyle("-fx-background-color:#3a3a3a;-fx-alignment: baseline-right");
                pane3.setStyle("-fx-border-color:#3a3a3a; -fx-border-width: 0 1 0 1 ");
                root.setStyle("-fx-background-color:#1d1d1d");
                primaryStage.getScene().getStylesheets().remove("LightTheme.css");
                primaryStage.getScene().getStylesheets().add("DarkTheme.css");
                primaryStage.setTitle("Bear in black");
                primaryStage.getIcons().remove(image2);
                primaryStage.getIcons().add(image1);
            }
        });
        primaryStage.setTitle("Bear in black");
        primaryStage.setScene(new Scene(root, 930, 800));
        //primaryStage.setAlwaysOnTop(true);
        primaryStage.setResizable(false);
        primaryStage.getScene().getStylesheets().add("DarkTheme.css");
        primaryStage.getIcons().add(image1);
        primaryStage.show();


        setStartedObject();
        primaryStage.setOnCloseRequest(event -> {

            event.consume();
            System.exit(1);

        });
    }

    @Override
    public void init() throws Exception {
        checkbox1.setSelected(true);
        checkbox2.setSelected(true);
        commands.getPotatoes();
        Thread.sleep(5000);

    }


}
