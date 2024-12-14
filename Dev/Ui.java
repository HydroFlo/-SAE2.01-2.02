import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import fr.ulille.but.sae_s2_2024.MultiGrapheOrienteValue;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Ui extends Application {
  public final static String CSF_FILE_NAME = "correspondance.csv";
  Label unite;
  HBox crit2;
  Button calc;
  Spinner<Integer> limiteInput;

  String argVDep = "";
  String argVArr = "";
  String argCritOpti = "CO2";
  String argCritLim = "";
  int argCritLimVal = 0;
  int argResLim = 5;

  ListView<String> results;

  String[] data = new String[] {
    "Lille;Paris;Train;40;752;1",
    "Lille;Paris;Bus;20;5640;3",
    "Lille;Paris;Avion;622;15604;4.5",
    "Paris;Osaka;Avion;918;798875;13",
    "Paris;Narita;Avion;851;806428;12",
    "Osaka;Kyoto;Train;17;412;1.16",
    "Osaka;Kyoto;Bus;20;3090;1.5",
    "Osaka;Nagoya;Avion;203;14110;4.93",
    "Osaka;Narita;Avion;140;40753;1",
    "Kyoto;Tokyo;Train;80;1936;2.16",
    "Kyoto;Nagoya;Train;30;596;1.5",
    "Nagoya;Tokyo;Train;70;640;2.79",
    "Nagoya;Tokyo;Avion;180;30212;1",
    "Tokyo;Narita;Train;11;200;2.30",
    "Tokyo;Narita;Bus;13;1500;1.28"
  };

  public void start(Stage stage) {
    class CellRenderer extends ListCell<String> {
        @Override
        public void updateItem(String text, boolean empty) {
            super.updateItem(text, empty);
            setText(text);
            setWrapText(true);
            setPrefWidth(50.0);
        }
    };

    VBox root = new VBox();
    HBox depArr = new HBox();
    HBox depBox = new HBox();
    HBox arrBox = new HBox();
    HBox crit1 = new HBox();
    crit2 = new HBox();
    VBox resBox = new VBox();

    Label depLabel = new Label("Départ: ");
    depLabel.setFont(new Font(18));
    ComboBox<String> depDropdown = new ComboBox<String>();
    depDropdown.getItems().addAll("Lille", "Paris", "Osaka", "Narita", "Kyoto", "Nagoya", "Tokyo");
    Label arrLabel = new Label("Arrivée: ");
    arrLabel.setFont(new Font(18));
    ComboBox<String> arrDropdown = new ComboBox<String>();
    arrDropdown.getItems().addAll("Lille", "Paris", "Osaka", "Narita", "Kyoto", "Nagoya", "Tokyo");

    ComboBox<String> crit1Dropdown = new ComboBox<String>();
    crit1Dropdown.getItems().addAll("CO2", "Temps", "Prix");
    crit1Dropdown.getSelectionModel().selectFirst();
    Label critAOpti = new Label("Critère à optimiser:");
    ComboBox<String> crit2Dropdown = new ComboBox<String>();
    crit2Dropdown.getItems().addAll("(Aucun)", "CO2", "Temps", "Prix");
    crit2Dropdown.getSelectionModel().selectFirst();
    Label limite = new Label("Limite (optionel):");
    limiteInput = new Spinner<Integer>(1,100000,10);
    limiteInput.editableProperty().set(true);
    limiteInput.setMaxWidth(92);
    limiteInput.setVisible(false);
    unite = new Label("");

    calc = new Button("Calculer");
    calc.setMinSize(100, 40);
    calc.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
    calc.setDisable(true);

    Label nbLimLabel = new Label("Nombre de résultats à afficher au maximum:  ");
    FlowPane nbRes = new FlowPane();
    Spinner<Integer> nbResValue = new Spinner<>(1,20,5);
    nbResValue.editableProperty().set(true);
    nbResValue.setMaxSize(70,20);
    nbRes.getChildren().addAll(nbLimLabel,nbResValue);
    nbRes.setAlignment(Pos.BOTTOM_RIGHT);
    
    results = new ListView<String>();

    results.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
      @Override
      public ListCell<String> call(ListView<String> list) {
        return new CellRenderer();
      }
    });

    class Calculer implements EventHandler<ActionEvent> {
      public void handle(ActionEvent e) {
        results.getItems().clear();
        Plateforme p;
        GenGraph gen = new GenGraph(CSF_FILE_NAME);
        Voyageur user;
        if(!argCritLim.isEmpty() && !argCritLim.equals("(Aucun)")){
          user = new Voyageur(TypeCout.valueOf(argCritOpti.toUpperCase()), TypeCout.valueOf(argCritLim.toUpperCase()), argCritLimVal);
        }
        else {
          user = new Voyageur(TypeCout.valueOf(argCritOpti.toUpperCase()));
        }
        p = new Plateforme(data, argResLim, user, CSF_FILE_NAME);
        MultiGrapheOrienteValue g = p.graphSelonMod(data, 3);
        Sommet depart = gen.rechercheSommet(p.getEnsembleSommet(), argVDep);
        Sommet arrivee = gen.rechercheSommet(p.getEnsembleSommet(), argVArr);
        if(p.voyagePossible(g, depart, arrivee)){
          String[] item = p.affichage(p.plusCourtChemin(depart, arrivee, g)).split("\n");
          results.getItems().addAll(item);
        }
        else {
          results.getItems().add("Aucun résultat trouvé.");
        }
      }
    }

    depDropdown.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
      argVDep = newValue;

      if(!argVDep.isEmpty() && !argVArr.isEmpty()) calc.setDisable(false);
      if(argVDep.equals(argVArr)) calc.setDisable(true);
    });

    arrDropdown.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
      argVArr = newValue;

      if(!argVDep.isEmpty() && !argVArr.isEmpty()) calc.setDisable(false);
      if(argVDep.equals(argVArr)) calc.setDisable(true);
    });

    crit1Dropdown.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
      argCritOpti = newValue;

      if(!argVDep.isEmpty() && !argVArr.isEmpty()) calc.setDisable(false);
      if(argVDep.equals(argVArr)) calc.setDisable(true);
    });

    crit2Dropdown.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
      argCritLim = newValue;

      if (newValue.equals("CO2")) {
        unite = new Label("kg CO2e");
        limiteInput.setVisible(true);
      } else if (newValue.equals("Temps")) {
        unite = new Label("heures");
        limiteInput.setVisible(true);
      } else if (newValue.equals("Prix")) {
        unite = new Label("€");
        limiteInput.setVisible(true);
      } else {
        unite = new Label("");
        limiteInput.setVisible(false);
      }
      crit2.getChildren().set(3, unite);
    });

    limiteInput.valueProperty().addListener((options, oldValue, newValue) -> {
      argCritLimVal = newValue;
    });

    limiteInput.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
      try {
        argCritLimVal = Integer.parseInt(newValue);
        if(argCritLimVal < 1 || argCritLimVal > 100000){
          argCritLimVal = Integer.parseInt(oldValue);
          limiteInput.getEditor().setText(oldValue);
        }
      } catch (NumberFormatException e) {
        limiteInput.getEditor().setText(oldValue);
      }
    });

    nbResValue.valueProperty().addListener((options, oldValue, newValue) -> {
      argResLim = newValue;
    });

    nbResValue.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
      try {
        argResLim = Integer.parseInt(newValue);
        if(argResLim < 1 || argResLim > 20){
          argResLim = Integer.parseInt(oldValue);
          nbResValue.getEditor().setText(oldValue);
        }
      } catch (NumberFormatException e) {
        nbResValue.getEditor().setText(oldValue);
      }
    });


    calc.setOnAction(new Calculer());

    crit1.getChildren().addAll(critAOpti, crit1Dropdown);
    crit1.setSpacing(10);
    crit1.setAlignment(Pos.BASELINE_LEFT);
    crit1.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
    crit1.setPadding(new Insets(5,5,5,5));
    crit2.getChildren().addAll(limite, crit2Dropdown, limiteInput, unite);
    crit2.setSpacing(10);
    crit2.setAlignment(Pos.BASELINE_LEFT);
    crit2.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
    crit2.setPadding(new Insets(5,5,5,5));
    depBox.getChildren().addAll(depLabel, depDropdown);
    arrBox.getChildren().addAll(arrLabel, arrDropdown);
    depArr.getChildren().addAll(depBox, arrBox);
    depArr.setPadding(new Insets(3, 3, 3, 3));
    depArr.setAlignment(Pos.BASELINE_CENTER);
    depArr.setSpacing(40.0);
    resBox.getChildren().addAll(nbRes, results);
    resBox.setAlignment(Pos.BOTTOM_RIGHT);
    root.getChildren().addAll(depArr, crit1, crit2, calc, resBox);
    root.setAlignment(Pos.TOP_CENTER);
    root.setSpacing(10);
    root.setPadding(new Insets(5,5,5,5));

    Scene scene = new Scene(root, 600, 400);
    stage.setScene(scene);
    stage.setTitle("App de voyage");
    stage.show();
  }

  public static void main(String[] args) {
    Application.launch(args);
  }
}