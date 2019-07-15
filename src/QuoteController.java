import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.ResourceBundle;

public class QuoteController implements Initializable {

    @FXML
    private TextField materialCostTextField;
    @FXML
    private TextField setupCostTextField;
    @FXML
    private TextField quantityTextField;
    @FXML
    private Text textListOfQuantity;
    @FXML
    private JFXComboBox jFXComboBoxOutsideServices;
    @FXML
    private Button addServiceToTableButton;
    @FXML
    private TableView<OutsideService> masterTableView;
    @FXML
    private TextField costPerPartTextBox;


    private ArrayList<Integer> listOfQuantities = new ArrayList();
    // private StringBuilder sb = new StringBuilder("Parts to be Quoted: ");
    private Alert inputError = new Alert(Alert.AlertType.ERROR);
    private ArrayList<OutsideService> listOfServices = new ArrayList<>();

    //Three text fields that will be saved as variables
    private double materialCost;
    private double settingUpCost;
    private double cycleTimeCost;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Setting up combo box");
        jFXComboBoxOutsideServices.getItems().addAll("Heat Treat", "Grinding", "Plating","Laser","Wielding", "Transportation", "Misc");
        System.out.println("ComboBox was setup");
        masterTableView.setEditable(true);

    }


    @FXML
    public void validateMaterialCostTextField() {
        boolean validInput = isValidInput(materialCostTextField.getText());
        if (validInput) {
            System.out.println("Valid input");
        } else {
            inputError.setTitle("Input for Material Cost");
            inputError.setHeaderText("Material Cost Error");
            inputError.setContentText("Material Cost has an invalid input please make sure only numbers are entered.\n" +
                    "EX: 100, 200.1 , 1.11");
            inputError.showAndWait();
        }
    }

    @FXML
    public void validateSetupCostTextField() {
        boolean validInput = isValidInput(setupCostTextField.getText());
        if (validInput) {
            System.out.println("Valid input");
        } else {
            inputError.setTitle("Input for Setup Cost");
            inputError.setHeaderText("Setup Cost Error");
            inputError.setContentText("Setup Cost has an invalid input please make sure only numbers are entered.\n" +
                    "EX: 100, 200.1 , 1.11");
            inputError.showAndWait();
        }
    }

    @FXML
    public void validateCycleTimeTextField() {
        boolean validInput = isValidInput(setupCostTextField.getText());
        if (validInput) {
            System.out.println("Valid input");
        } else {
            inputError.setTitle("Input for Cycle Time");
            inputError.setHeaderText("Cycle Time Error");
            inputError.setContentText("Cycle Time has an invalid input please make sure only numbers are entered.\n" +
                    "EX: 100, 200.1 , 1.11");
            inputError.showAndWait();
        }
    }

    @FXML
    public void addQuantityToList() {
        boolean validInput = isValidInput2(quantityTextField.getText());
        StringBuilder sb = new StringBuilder("Parts to be Quoted: ");
        if (validInput) {
            System.out.println("Valid input");
            listOfQuantities.add(Integer.parseInt(quantityTextField.getText()));
            for (int q : listOfQuantities) {
                sb.append(q + ", ");
                System.out.println(q);
            }
            textListOfQuantity.setText(sb.toString());
            //clears the table for new info
            setUpTableWithPartAmounts();

        } else {
            inputError.setTitle("Input for Quantity");
            inputError.setHeaderText("Quantity Error");
            inputError.setContentText("Quantity has an invalid input please make sure only WHOLE numbers are entered.\n" +
                    "EX: 100, 200, 5000");
            inputError.showAndWait();
        }
        quantityTextField.clear();
    }

    @FXML
    public void addServiceToTable() {
       try {
           OutsideService serviceToAdd = new OutsideService(jFXComboBoxOutsideServices.getValue().toString(), listOfQuantities);
           serviceToAdd.outsideServiceQoutes(costPerPartTextBox.getText());
           listOfServices.add(serviceToAdd);
           masterTableView.getItems().add(serviceToAdd);
           costPerPartTextBox.clear();
       } catch (Exception error){
           System.out.println("There was an error with the adding to table");
           inputError.setTitle("Input For Service");
           inputError.setHeaderText("Service Error");
           inputError.setContentText("The input for Service was not done correctly. " +
                   "\nMake sure the service has a service and the input is valid");
           inputError.showAndWait();
       }


    }


    /**
     * Private helper to validate that the text field is a number and
     * does not contain any strings
     *
     * @param input
     * @return
     */
    private Boolean isValidInput(String input) {
        char[] chars = input.toCharArray();
        boolean validInput = false;
        for (char c : chars) {
            if (!Character.isDigit(c) && c != '.') {
                return false;
            } else {
                validInput = true;
            }
        }
        return validInput;
    }

    /**
     * same as is validInput helper method but this is for whole numbers so doubles will not work
     *
     * @param input
     * @return
     */
    private Boolean isValidInput2(String input) {
        char[] chars = input.toCharArray();
        boolean validInput = false;
        for (char c : chars) {
            if (!Character.isDigit(c)) {
                return false;
            } else {
                validInput = true;
            }
        }
        return validInput;
    }

    private void setUpTableWithPartAmounts() {
        masterTableView.getColumns().clear();
        masterTableView.getColumns().clear();
        Collections.sort(listOfQuantities);
        System.out.println("Made it here");
        TableColumn<OutsideService, String> serviceColumn = new TableColumn("Service");
        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("service"));
        masterTableView.getColumns().add(serviceColumn);
        for (int i = 0; i < listOfQuantities.size(); i++) {
            TableColumn columnToAdd = new TableColumn("Cost of " + listOfQuantities.get(i));
            columnToAdd.setCellValueFactory(new PropertyValueFactory<>("numberToReturn"));
            masterTableView.getColumns().add(columnToAdd);
        }
    }

    @FXML
    public void clearTableContents(){
        masterTableView.getColumns().clear();
        listOfServices.clear();
        listOfQuantities.clear();
        textListOfQuantity.setText("Parts to be Quoted");
    }
}
