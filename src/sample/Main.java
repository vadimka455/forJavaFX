package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
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
import javafx.scene.text.TextAlignment;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.prefs.Preferences;
 class Main extends Application {
    private AnchorPane pane1 = new AnchorPane();
    private AnchorPane pane2 = new AnchorPane();
    private AnchorPane pane3 = new AnchorPane();
    private AnchorPane pane4 = new AnchorPane();
    private ToggleButton toggleButton = new ToggleButton("Dark Theme");
    private HTMLEditor htmlEditor = new HTMLEditor();
    private CheckBox checkbox1 = new CheckBox("color");
    private CheckBox checkbox2 = new CheckBox("weight");
    private Button button1 = new Button("Add\nif\nmax");
    private Button button2 = new Button("Remove\ngreater");
    private Button button3 = new Button("Remove\nfirst");
    private Button button4 = new Button("Remove\nlast");
    private long buttonsClick = 0;
    private boolean doThis = true;

    public static void main(String[] args) {

        launch(args);
    }

    private void setSoundsForButton(Button button) {
        button.setOnMouseClicked(event -> new Audio(Main.class.getResourceAsStream("/ChuToy.wav")).play());
    }

    private void showhelp() {
        Button button = new Button("Ok");
        button.setDefaultButton(true);
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        Label label = new Label("Пишите разрабу, он поможет :D");
        vBox.getChildren().addAll(label, button);
        Stage stage = new Stage();
        stage.setScene(new Scene(vBox));
        if (toggleButton.isPressed()) {
            stage.getScene().getStylesheets().add("LightTheme.css");
        } else {
            stage.getScene().getStylesheets().add("DarkTheme.css");
        }
        button.setOnAction(event -> stage.hide());
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        stage.requestFocus();
        stage.show();
    }

    private void anchorSetPosition(Node node) {
        AnchorPane.setRightAnchor(node, 0.0);
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
    }

    private void ifmore10(Button button) {
        if (button.getText().equals(button3.getText())|| button.getText().equals(button4.getText())) {
            if (Process.getSteal().clubni.size() == 0 ) {
                buttonsClick++;
            } else {
                buttonsClick = 0;
            }
            if (buttonsClick >= 10) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Коллекция давно пуста...Нечего тут на кнопки понапрасну тыкать :/", ButtonType.OK);
                new Audio(Main.class.getResourceAsStream("/ifmore10.wav")).play();
                alert.showAndWait();

            }
        }


    }

    private void addListenerOpenButton(MenuItem button, Stage primaryStage) {
        button.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
            Preferences prefs = Preferences.userNodeForPackage(Main.class);
            String filePath = prefs.get("filePath", null);
            if (!filePath.isEmpty()) {
                fileChooser.setInitialFileName(filePath);
            }
            fileChooser.setTitle("Open Resource File");
            File fileChoose = fileChooser.showOpenDialog(primaryStage);
            if (!fileChoose.getName().isEmpty()) {
                try {
                    Process.start(fileChoose);
                    prefs.put("filePath", fileChoose.getPath());
                    pane3.getChildren().remove(htmlEditor);
                    new Audio(Main.class.getResourceAsStream("/open.wav")).play();
                    buttonsClick=0;
                    setTreeView(Process.getSteal().clubni, pane4);
                } catch (Exception ignored) {
                }
            }
        });
    }

    private void setStartedObject() {
        try {
            Process.getSteal().stealPotatoes();
        } catch (Exception ignored) {
        }
    }

    private void setTreeView(LinkedList<Potatoes> clubni, Pane pane4) {
        Iterator<Potatoes> iterator = clubni.iterator();
        Potatoes potatoes;
        TreeItem<String> rootItem = new TreeItem<>("Клубни");
        rootItem.setExpanded(true);
        while (iterator.hasNext()) {
            potatoes = iterator.next();
            TreeItem<String> rootItem2 = new TreeItem<>("Картошка");
            rootItem2.setExpanded(true);
            TreeItem<String> item;
            if (checkbox1.isSelected()) {
                item = new TreeItem<>("color: " + potatoes.getColor());
                rootItem2.getChildren().add(item);
            }
            if (checkbox2.isSelected()) {
                item = new TreeItem<>("weight: " + potatoes.getWeight());
                rootItem2.getChildren().add(item);
            }
            rootItem.getChildren().add(rootItem2);
        }
        TreeView<String> tree = new TreeView<>(rootItem);
        tree.setStyle("-fx-background-color: #1d1d1d");
        pane4.getChildren().add(tree);
        anchorSetPosition(tree);

    }

    private void writeForm(Button button) {
        pane3.getChildren().remove(htmlEditor);
        htmlEditor.setPrefSize(0.68 * 930, 300);
        htmlEditor.setHtmlText("<p style=\"text-align: center;\"><strong>Введите значения</strong></p>\n" +
                "<hr />\n" +
                "<div>Color: &nbsp;</div>\n" +
                "<hr />\n" +
                "<div>Weight: &nbsp;</div>\n" +
                "<hr />");
        Button sendText = new Button("SendText");
        sendText.setDefaultButton(true);
        VBox vBox = new VBox();
        anchorSetPosition(vBox);
        vBox.getChildren().addAll(htmlEditor, sendText);
        pane3.getChildren().add(vBox);
        sendText.setOnAction(event1 -> {
            pane3.getChildren().removeAll(vBox);
            htmlEditor.getHtmlText();
            String[] values = parserHTMLcode(htmlEditor.getHtmlText(), button);
            if (!doThis) {
                doThis = true;
                return;
            }
            if (button.getText().equals(button1.getText())) {
                String string = "{\"weight\":" + values[1] + ",\"color\":" + values[0] + "}";
                buttonsClick=0;
                if (!ifHaveMorethanwant(Process.getSteal().clubni)) {
                    return;
                }
                Commands.add_if_max(string, Process.getSteal().clubni);

            }
            if (button.getText().equals(button2.getText())) {
                String string = "{\"weight\":" + values[1] + ",\"color\":" + values[0] + "}";
                if (Process.getSteal().clubni.size()==0){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Коллекция и так пуста...", ButtonType.OK);
                    alert.showAndWait();
                    vBox.getChildren().remove(sendText);
                    return;
                }
                Commands.remove_greater(string, Process.getSteal().clubni);
            }
            setTreeView(Process.getSteal().clubni, pane4);
            vBox.getChildren().remove(sendText);
        });
    }

    private void addListenerEditButtons(Button button) {
        button.setOnAction(event -> {
            writeForm(button);
        });
    }

    private String[] parserHTMLcode(String HTMLcode, Button button) {
        String string1 = "<html dir=\"ltr\"><head></head><body contenteditable=\"true\"><p style=\"text-align: center;\"><strong>Введите значения</strong></p>\n" +
                "<hr>\n" +
                "<div>Color: &nbsp;";
        String string2 = "</div>\n" +
                "<hr>\n" +
                "<div>Weight: &nbsp;";
        String string3 = "</div>\n" +
                "<hr></body></html>";
        HTMLcode = HTMLcode.replace(string1, "");
        HTMLcode = HTMLcode.replace(string2, "lolblup");
        HTMLcode = HTMLcode.replace(string3, "");
        String[] strings = HTMLcode.split("blup");
        double value;
        String text = "Ошибка ввода. Некорректный ввод";
        try {
            value = Double.parseDouble(strings[1]);
            if (value <= 0) {
                text = "Объем не может быть неположительным";
                throw new Exception();
            }else if (strings[0].equals("lol")) {
                text = "Цвет не норм";
                throw new Exception();
            }else {
                strings[0]=strings[0].replace("lol","");
            }
        } catch (Exception e) {
            ButtonType buttonTypeAgain = new ButtonType("Again", ButtonBar.ButtonData.OTHER);
            Alert alert = new Alert(Alert.AlertType.NONE, text, buttonTypeAgain, ButtonType.CLOSE);
            new Audio(Main.class.getResourceAsStream("/error.wav")).play();
            alert.showAndWait();
            doThis = false;
            if (alert.getResult() == buttonTypeAgain) {
                writeForm(button);
            }
        }
        return strings;


    }

    private void addListenerSaveAs(MenuItem menuItem, Stage primaryStage) {

        menuItem.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                    "JSON files", "*.json"));
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                Commands.save(Process.getSteal().clubni, file);
                Preferences.userNodeForPackage(Main.class).put("filePath", file.getPath());
            }
        });
    }

    private void addListenerCheckBoxs(CheckBox checkBox) {
        checkBox.setOnAction(event -> setTreeView(Process.getSteal().clubni, pane4));
    }

    private boolean ifHaveMorethanwant(LinkedList<Potatoes> clubni) {
        if (clubni.size() > 600) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Воу-воу-воу. Их уже и так много!! Хватит!", ButtonType.OK);
            new Audio(Main.class.getResourceAsStream("/ifmore10.wav")).play();
            alert.showAndWait();
            return false;
        }
        return true;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Process.shutdown(new File(Preferences.userNodeForPackage(Main.class).get("filePath", null)));

        pane1.setStyle("-fx-background-color:#3a3a3a;-fx-alignment: baseline-right");
        pane3.setStyle("-fx-border-color:#3a3a3a; -fx-border-width: 0 1 0 1 ");

        GridPane root = new GridPane();
        root.add(pane1, 0, 0, 3, 1);
        root.add(pane2, 0, 1);
        root.add(pane3, 1, 1);
        root.add(pane4, 2, 1);
        root.setStyle("-fx-background-color:#1d1d1d");


        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(11);
        ColumnConstraints columnConstraints1 = new ColumnConstraints();
        columnConstraints1.setPercentWidth(65);
        ColumnConstraints columnConstraints2 = new ColumnConstraints();
        columnConstraints2.setPercentWidth(25);
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(6);
        RowConstraints rowConstraints1 = new RowConstraints();
        rowConstraints1.setPercentHeight(94);
        root.getColumnConstraints().addAll(columnConstraints, columnConstraints1, columnConstraints2);
        root.getRowConstraints().addAll(rowConstraints, rowConstraints1);


        button1.setPrefSize(0.11 * 930, -1);
        button1.setTextAlignment(TextAlignment.CENTER);
        button2.setTextAlignment(TextAlignment.CENTER);
        button3.setTextAlignment(TextAlignment.CENTER);
        button4.setTextAlignment(TextAlignment.CENTER);
        button2.setPrefSize(0.11 * 930, -1);
        button3.setPrefSize(0.11 * 930, -1);
        button4.setPrefSize(0.11 * 930, -1);
        button1.setTooltip(new Tooltip("Добавляет, если больше имеющихся"));
        button2.setTooltip(new Tooltip("Удаляет все элементы, которые больше текущего"));
        button3.setTooltip(new Tooltip("Удаляет первый"));
        button4.setTooltip(new Tooltip("Удаляет последний"));
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


        htmlEditor.lookup(".bottom-toolbar").setVisible(false);
        htmlEditor.lookup(".bottom-toolbar").setManaged(false);
        htmlEditor.lookup(".top-toolbar").setManaged(false);
        htmlEditor.lookup(".top-toolbar").setVisible(false);

        htmlEditor.setPrefSize(0.68 * 930, 300);
        htmlEditor.setHtmlText("Тут что-то будет...");
        anchorSetPosition(htmlEditor);

        MenuBar menuBar = new MenuBar();
        Menu file = new Menu("File");
        Menu smthelse = new Menu("Ничего");
        Menu menuhelp = new Menu("Help");
        menuBar.getMenus().addAll(file, smthelse, menuhelp);
        //smthelse.setOnAction(event -> new Audio(new File("content/sounds/nothing.wav")).play());  //TODO --> заменить на что-то кнопку "Ничего"
        MenuItem fileOpen = new MenuItem("Open");
        MenuItem fileSave = new MenuItem("Save");
        MenuItem fileSaveAs = new MenuItem("Save as ...");
        MenuItem fileDef = new MenuItem("Default objects");
        MenuItem fileExit = new MenuItem("Exit");
        MenuItem helpInstr = new MenuItem("Instruction");
        fileOpen.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        fileSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        fileDef.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN));
        fileExit.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));
        fileSaveAs.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
        helpInstr.setAccelerator(new KeyCodeCombination(KeyCode.F1));
        file.getItems().addAll(fileOpen, fileDef, fileSave, fileSaveAs, fileExit);
        menuhelp.getItems().add(helpInstr);
        addListenerOpenButton(fileOpen, primaryStage);
        addListenerSaveAs(fileSaveAs, primaryStage);

        fileDef.setOnAction(event -> {
            pane3.getChildren().clear();
            try {
                if (!ifHaveMorethanwant(Process.getSteal().clubni)) {
                    return;
                }
                Commands.add_default_elements(Process.getSteal().clubni);
                setTreeView(Process.getSteal().clubni, pane4);

            } catch (Exception ignored) {
            }
        });
        fileSave.setOnAction(event -> Commands.save(Process.getSteal().clubni, new File(Preferences.userNodeForPackage(Main.class).get("filePath", null))));
        fileExit.setOnAction(event -> {
            new Audio(Main.class.getResourceAsStream("/Exit.wav")).play();

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
            pane3.getChildren().remove(htmlEditor);
            Commands.remove_first(Process.getSteal().clubni);
            ifmore10(button3);
            setTreeView(Process.getSteal().clubni, pane4);
        });
        button4.setOnAction(event -> {
            pane3.getChildren().clear();
            Commands.remove_last(Process.getSteal().clubni);
            ifmore10(button4);
            setTreeView(Process.getSteal().clubni, pane4);
        });
        toggleButton.setOnAction(event -> {

            new Audio(Main.class.getResourceAsStream("/thems.wav")).play();
            try {
                Thread.sleep(3000);
            } catch (Exception ignored) {
            }

            if (toggleButton.isSelected()) {
                toggleButton.setText("LightTheme");
                primaryStage.getScene().getStylesheets().remove("DarkTheme.css");
                primaryStage.getScene().getStylesheets().add("LightTheme.css");
                pane1.setStyle("-fx-background-color:#919191;-fx-alignment: baseline-right");
                pane3.setStyle("-fx-border-color:#919191; -fx-border-width: 0 1 0 1 ");
                root.setStyle("-fx-background-color:white");

            } else {
                toggleButton.setText("DarkTheme");

                pane1.setStyle("-fx-background-color:#3a3a3a;-fx-alignment: baseline-right");
                pane3.setStyle("-fx-border-color:#3a3a3a; -fx-border-width: 0 1 0 1 ");
                root.setStyle("-fx-background-color:#1d1d1d");
                primaryStage.getScene().getStylesheets().remove("LightTheme.css");
                primaryStage.getScene().getStylesheets().add("DarkTheme.css");
            }
        });
        primaryStage.setTitle("Bear in black");
        primaryStage.setScene(new Scene(root, 930, 800));
        //primaryStage.setAlwaysOnTop(true);
        primaryStage.setResizable(false);
        primaryStage.getScene().getStylesheets().add("DarkTheme.css");
        Image image = new Image("mainicon.png");
        primaryStage.getIcons().add(image);
        primaryStage.show();
        setStartedObject();
        setTreeView(Process.getSteal().clubni, pane4);
        new Audio(Main.class.getResourceAsStream("/Welcome.wav")).play();
        Alert alert = new Alert(Alert.AlertType.NONE, "Добро пожаловать в мое приложение", ButtonType.OK);
        alert.showAndWait();
    }
}
