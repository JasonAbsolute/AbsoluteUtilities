import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.ResourceBundle;

public class QuoteController implements Initializable {

    @FXML
    private TextField quantityTextField;
    @FXML
    private Text textListOfQuantity;
    @FXML
    private JFXComboBox jFXComboBoxOutsideServices;
    @FXML
    private Button addServiceToTableButton;
    @FXML
    private TableView<GeneralServices> masterTableView;
    @FXML
    private TextField costPerPartTextBox;
    @FXML
    private StackPane stackPane;


    private ArrayList<Integer> listOfQuantities = new ArrayList();
    // private StringBuilder sb = new StringBuilder("Parts to be Quoted: ");
    private Alert inputError = new Alert(Alert.AlertType.ERROR);
    private ArrayList<GeneralServices> listOfServices = new ArrayList<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Setting up combo box");
        jFXComboBoxOutsideServices.getItems().addAll("Material", "Setup Costs (hours)", "Cycle Time (mins)", "Heat Treat", "Grinding", "Plating", "Laser", "Wielding", "Transportation", "Misc");
        System.out.println("ComboBox was setup");
        masterTableView.setEditable(true);
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
            GeneralServices serviceToAdd = new GeneralServices(jFXComboBoxOutsideServices.getValue().toString(), listOfQuantities);
            serviceToAdd.outsideServiceQoutes(costPerPartTextBox.getText());
            listOfServices.add(serviceToAdd);
            masterTableView.getItems().add(serviceToAdd);
            costPerPartTextBox.clear();
        } catch (Exception error) {
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
        TableColumn<GeneralServices, String> serviceColumn = new TableColumn("Service");
        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("service"));
        masterTableView.getColumns().add(serviceColumn);
        for (int i = 0; i < listOfQuantities.size(); i++) {
            TableColumn columnToAdd = new TableColumn("Cost of " + listOfQuantities.get(i));
            columnToAdd.setCellValueFactory(new PropertyValueFactory<>("numberToReturn"));
            masterTableView.getColumns().add(columnToAdd);
        }
    }

    @FXML
    public void clearTableContents() {
        masterTableView.getItems().clear();
        listOfServices.clear();
        listOfQuantities.clear();
        textListOfQuantity.setText("Parts to be Quoted");
    }

    @FXML
    public void quoteButtonPressed() {
        double totalCostOfPart = 0;
        printQouteToConsole();
        try {
            File outputFile = new File("TestFile.txt");
            FileWriter fileWriter = new FileWriter("TestFile.txt");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            fileWriter.write("Date Quoted: " + formatter.format(date) + "\n");
            fileWriter.write("Service                ");
            for (int i = 0; i < listOfQuantities.size(); i++) {
                fileWriter.write("Cost Of " + listOfQuantities.get(i) + "     ");
            }
            fileWriter.write("\n");
            for (int j = 0; j < listOfServices.size(); j++) {
                fileWriter.write(listOfServices.get(j).getService() + " ");
                for (int charCount = 25; charCount > listOfServices.get(j).getService().length(); charCount--) {
                    fileWriter.write(" ");
                }
                for (int k = 0; k < listOfServices.get(j).getCost().size(); k++) {
                    fileWriter.write("" + String.format("%.2f", listOfServices.get(j).getCost().get(k)));
                    for (int charCount = 16; charCount > listOfServices.get(j).getCost(k).length(); charCount--) {
                        fileWriter.write(" ");
                    }
                }
                fileWriter.write("\n");
            }

            fileWriter.write("Total Cost ");
            for (int i = 0; i < listOfQuantities.size(); i++) {
                for (int charCount = 25; charCount > "Total Cost".length(); charCount--) {
                    fileWriter.write(" ");
                }
                for (int j = 0; j < listOfServices.size(); j++) {
                    double costOfPart = Double.parseDouble(listOfServices.get(j).getCost(i));
                    totalCostOfPart += costOfPart;
                }
                fileWriter.write("" + String.format("%.2f", totalCostOfPart));
                for (int k = 0; k < listOfServices.get(i).getCost().size(); k++) {
                    for (int charCount = 8; charCount > Double.toString(totalCostOfPart).length(); charCount--) {
                        fileWriter.write(" ");
                    }
                }
                totalCostOfPart = 0;
            }
            fileWriter.close();
        } catch (IOException error) {
            System.out.println("Something went wrong when writing the quote out");
        }
    }

    @FXML
    public void loadDialogForServices() {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Services"));
        content.setBody(new Text("This is where all the serives will go in the format of " +
                "xxx.xx , xxx.xx there must be the amount of entrys as there are quotes for parts"));


        JFXDialog jfxDialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        jfxDialog.show();
    }

    private void printQouteToConsole() {
        System.out.print("Service                ");
        for (int i = 0; i < listOfQuantities.size(); i++) {
            System.out.print("Cost Of " + listOfQuantities.get(i) + "     ");
        }
        System.out.println("");
        for (int j = 0; j < listOfServices.size(); j++) {
            System.out.print(listOfServices.get(j).getService() + " ");
            for (int charCount = 25; charCount > listOfServices.get(j).getService().length(); charCount--) {
                System.out.print(" ");
            }
            for (int k = 0; k < listOfServices.get(j).getCost().size(); k++) {
                System.out.print(listOfServices.get(j).getCost().get(k));
                for (int charCount = 16; charCount > listOfServices.get(j).getCost(k).length(); charCount--) {
                    System.out.print(" ");
                }
            }
            System.out.println("");
        }
    }
}
