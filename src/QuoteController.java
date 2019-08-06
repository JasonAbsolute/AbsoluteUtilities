import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.ResourceBundle;

public class QuoteController implements Initializable {

    @FXML
    private TextField quantityTextField;
    @FXML
    private Window mainStage;
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
    @FXML
    private TextField multiplierTextField;


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
        jFXComboBoxOutsideServices.getItems().addAll("Material", "Heat Treat", "Grinding", "Plating", "Laser", "Wielding", "Transportation", "Misc");
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
    public void clearEverything() {
        //TODO Make the table reset when button is clicked
        listOfQuantities.clear();
        listOfServices.clear();
        cycleCostList.clear();
        setupCostList.clear();
        companyName = null;
        rate = 0;
        setup = 0;
        cycle = 0;
        multiplier = 0;
        rateTextField.clear();
        setUpTextField.clear();
        cycleTextField.clear();
        quoteForCompany.clear();
        multiplierTextField.clear();
        masterTableView.getColumns().clear();
        System.out.println("everything was cleared");
    }

    /**
     * clears just the table and quantity
     */
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
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        File outPutFile;
        try {
            FileChooser fileToSave = new FileChooser();
            fileToSave.setTitle("Quote File");
            fileToSave.setInitialFileName(companyName + ".txt");
            fileToSave.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Txt File", "*.txt"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
            outPutFile = fileToSave.showSaveDialog(mainStage);
            FileWriter fileWriter = new FileWriter(outPutFile);
            if (companyName == null) {
                inputError.setTitle("Input For Company Name");
                inputError.setHeaderText("Naming file");
                inputError.setContentText("The file was not specified a name/company");
                inputError.showAndWait();
                fileWriter.write("Quote For: no name was given\r\n");
            } else {
                fileWriter.write("Quote For: " + companyName + "\r\n");
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            fileWriter.write("Date Quoted: " + formatter.format(date) + "\r\n");
            fileWriter.write("Service                ");
            for (int i = 0; i < listOfQuantities.size(); i++) {
                fileWriter.write("Cost Of " + listOfQuantities.get(i) + "     ");
            }
            fileWriter.write("\r\n");
            writeTheSetUpAndCycleCost(fileWriter);
            for (int j = 0; j < listOfServices.size(); j++) {
                fileWriter.write(listOfServices.get(j).getService() + " ");
                for (int charCount = 25; charCount > listOfServices.get(j).getService().length(); charCount--) {
                    fileWriter.write(" ");
                }
                for (int k = 0; k < listOfServices.get(j).getCost().size(); k++) {
                    fileWriter.write("" + String.format("%.2f", listOfServices.get(j).getCost().get(k)));
                    String costLengthFormated = decimalFormat.format(listOfServices.get(j).getCost().get(k));
                    System.out.println(costLengthFormated + "    " + costLengthFormated.length());
                    for (int charCount = 16; charCount > costLengthFormated.length(); charCount--) {
                        fileWriter.write(" ");
                    }
                }
                fileWriter.write("\r\n");
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
                String lengthForFormat = decimalFormat.format(totalCostOfPart);
                for (int j = 16; j > lengthForFormat.length(); j--) {
                    fileWriter.write(" ");
                }
                totalCostOfPart = 0;
            }
            fileWriter.write("\r\nMultiplier Total");
            for (int charCount = 26; charCount > "Multiplier Total".length(); charCount--) {
                fileWriter.write(" ");
            }
            for (int i = 0; i < listOfQuantities.size(); i++) {
                for (int j = 0; j < listOfServices.size(); j++) {
                    double costOfPart = Double.parseDouble(listOfServices.get(j).getCost(i));
                    totalCostOfPart += costOfPart;
                }
                totalCostOfPart += cycleCostList.get(i);
                totalCostOfPart += setupCostList.get(i);
                totalCostOfPart = ((multiplier / 100) + 1) * totalCostOfPart;
                fileWriter.write("" + String.format("%.2f", totalCostOfPart));
                String lengthForFormatMulti = decimalFormat.format(totalCostOfPart);
                System.out.println(lengthForFormatMulti + "  " + lengthForFormatMulti.length());
                for (int j = 16; j > lengthForFormatMulti.length(); j--) {
                    fileWriter.write(" ");
                }
                totalCostOfPart = 0;
            }
            fileWriter.close();
        } catch (IOException error) {
            System.out.println("Something went wrong when writing the quote out");
        }
    }

    /**
     * eveytime the user enters a letter to the name field it adds to a string
     */
    @FXML
    public void saveCompanyName() {
        companyName = quoteForCompany.getText();
        System.out.println("Company name was set to: " + companyName);
    }

    /**
     * exits the proggram
     */
    @FXML
    public void stopProgram() {
        System.exit(0);
    }

    /**
     * adds the rate which the part requires
     */
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

    /**
     * sets the setup
     */
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

    /**
     * sets the cycle
     */
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

    /**
     * sets the cycle and setup
     * both can be done individually
     */
    @FXML
    public void setCycleAndSetup() {
        setCycle();
        setSetup();
        System.out.println("Both Cycle and Setup were set up");
    }

    /**
     * sets the multiplier to the qoute
     * this can change at any moment
     */
    @FXML
    public void addMultiplier() {
        multiplier = Double.parseDouble(multiplierTextField.getText());
        System.out.println("Multiplier was set to: " + multiplier);
    }

    /**
     * This method writes to the Setup and Cycle to the file.
     *
     * @param fileWriter
     */
    private void writeTheSetUpAndCycleCost(FileWriter fileWriter) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
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
                String lengthForFormat = decimalFormat.format(cost);
                for (int j = 16; j > lengthForFormat.length(); j--) {
                    fileWriter.write(" ");
                }
                setupCostList.add(i, cost);
            }
            fileWriter.write("\r\n");
            fileWriter.write("Cycle (Minutes) ");
            int cycleLength = "Cycle (Minutes)".length();
            for (int charCount = 25; charCount > cycleLength; charCount--) {
                fileWriter.write(" ");
            }
            for (int i = 0; i < listOfQuantities.size(); i++) {
                double partAmount = rate/60*cycle;
                fileWriter.write(String.format("%.2f", partAmount));
                String lengthForFormat = decimalFormat.format(partAmount);
                for (int j = 16; j > lengthForFormat.length(); j--) {
                    fileWriter.write(" ");
                }
                cycleCostList.add(partAmount);
            }
            fileWriter.write("\r\n");
        } catch (Exception error) {

        }
    }

}
