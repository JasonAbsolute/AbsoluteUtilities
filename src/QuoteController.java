import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

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
    private TextField quoteForCompany;
    @FXML
    private TextField rateTextField;
    @FXML
    private TextField setUpTextField;
    @FXML
    private TextField cycleTextField;


    private ArrayList<Integer> listOfQuantities = new ArrayList();
    // private StringBuilder sb = new StringBuilder("Parts to be Quoted: ");
    private Alert inputError = new Alert(Alert.AlertType.ERROR);
    private ArrayList<GeneralServices> listOfServices = new ArrayList<>();
    private ArrayList<Double> cycleCostList = new ArrayList<>();
    private ArrayList<Double> setupCostList = new ArrayList<>();
    private String companyName;
    private double rate;
    private double setup;
    private double cycle;
    private double multiplier;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Setting up combo box");
        jFXComboBoxOutsideServices.getItems().addAll("Material", "Heat Treat", "Grinding", "Plating", "Laser", "Wielding", "Transportation", "Misc");
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
            FileWriter fileWriter;
            if (companyName == null) {
                inputError.setTitle("Input For Company Name");
                inputError.setHeaderText("Nameing file");
                inputError.setContentText("The file was not specifed a name/company it was \n saved as NoCompanyNamed.txt");
                inputError.showAndWait();
                fileWriter = new FileWriter("NoCompanyNamed.txt");
                fileWriter.write("Quote For: no name was given\n");
            } else {
                fileWriter = new FileWriter(companyName + ".txt");
                fileWriter.write("Quote For: " + companyName + "\n");
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            fileWriter.write("Date Quoted: " + formatter.format(date) + "\n");
            fileWriter.write("Service                ");
            for (int i = 0; i < listOfQuantities.size(); i++) {
                fileWriter.write("Cost Of " + listOfQuantities.get(i) + "     ");
            }


            fileWriter.write("\n");

            writeTheSetUpAndCycleCost(fileWriter);


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
            for (int charCount = 25; charCount > "Total Cost".length(); charCount--) {
                fileWriter.write(" ");
            }
            for (int i = 0; i < listOfQuantities.size(); i++) {
                for (int j = 0; j < listOfServices.size(); j++) {
                    double costOfPart = Double.parseDouble(listOfServices.get(j).getCost(i));
                    totalCostOfPart += costOfPart;
                }
                totalCostOfPart += cycleCostList.get(i);
                totalCostOfPart += setupCostList.get(i);
                fileWriter.write("" + String.format("%.2f", totalCostOfPart));
                fileWriter.write("            ");
                totalCostOfPart = 0;
            }
            fileWriter.close();
        } catch (IOException error) {
            System.out.println("Something went wrong when writing the quote out");
        }
    }

    private void printQouteToConsole() {
        System.out.println(companyName);
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

    @FXML
    public void saveCompanyName() {
        companyName = quoteForCompany.getText();
    }

    @FXML
    public void stopProgram() {
        System.exit(0);
    }

    @FXML
    public void addRate() {
        if (isValidInput(rateTextField.getText())) {
            rate = Double.parseDouble((rateTextField.getText()));
            System.out.println("Rate was set at: " + rate);
        } else {
            inputError.setTitle("Input For Rate");
            inputError.setHeaderText("Rate Error");
            inputError.setContentText("The input for Rate was not done correctly. " +
                    "\nMake sure the Rate is correct");
            inputError.showAndWait();
        }
    }

    @FXML
    public void setSetup() {
        if (isValidInput(setUpTextField.getText())) {
            setup = Double.parseDouble((setUpTextField.getText()));
            System.out.println("Setup(Hours) was setup");
        } else {
            inputError.setTitle("Input For Setup");
            inputError.setHeaderText("Rate Error");
            inputError.setContentText("The input for Rate was not done correctly. " +
                    "\nMake sure the Rate is correct");
            inputError.showAndWait();
        }
    }

    @FXML
    public void setCycle() {
        if (isValidInput(cycleTextField.getText())) {
            cycle = Double.parseDouble((cycleTextField.getText()));
            System.out.println("Cycle(Minutes) was setup");
        } else {
            inputError.setTitle("Input For Setup");
            inputError.setHeaderText("Rate Error");
            inputError.setContentText("The input for Rate was not done correctly. " +
                    "\nMake sure the Rate is correct");
            inputError.showAndWait();
        }
    }

    @FXML
    public void setCycleAndSetup() {
        setCycle();
        setSetup();
        System.out.println("Both Cycle and Setup were set up");
    }

    private void writeTheSetUpAndCycleCost(FileWriter fileWriter) {
        try {
            fileWriter.write("Setup (Hours) ");
            int setupLength = "Setup (Hours)".length();
            for (int charCount = 25; charCount > setupLength; charCount--) {
                fileWriter.write(" ");
            }
            for (int i = 0; i < listOfQuantities.size(); i++) {
                double partAmount = listOfQuantities.get(i);
                double cost = (rate * setup) / partAmount;
                fileWriter.write(String.format("%.2f", cost));
                fileWriter.write("            ");
                setupCostList.add(i, cost);
            }
            fileWriter.write("\n");
            fileWriter.write("Cycle (Minutes) ");
            int cycleLength = "Cycle (Minutes)".length();
            for (int charCount = 25; charCount > cycleLength; charCount--) {
                fileWriter.write(" ");
            }
            for (int i = 0; i < listOfQuantities.size(); i++) {
                double partAmount = listOfQuantities.get(i);
                fileWriter.write(String.format("%.2f", (rate / 60 * cycle)));
                fileWriter.write("            ");
                cycleCostList.add((rate / 60 * cycle));
            }
            fileWriter.write("\n");
        } catch (Exception error) {

        }
    }

}
