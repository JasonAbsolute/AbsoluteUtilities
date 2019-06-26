import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class QuoteController {

    @FXML
    private TextField materialCostTextField;
    @FXML
    private TextField setupCostTextField;
    @FXML
    private TextField quantityTextField;
    @FXML
    private Text textListOfQuantity;

    private ArrayList<Integer> listOfQuantities = new ArrayList();
   // private StringBuilder sb = new StringBuilder("Parts to be Quoted: ");
    private Alert inputError = new Alert(Alert.AlertType.ERROR);


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

        } else {
            inputError.setTitle("Input for Quantity");
            inputError.setHeaderText("Quantity Error");
            inputError.setContentText("Quantity has an invalid input please make sure only WHOLE numbers are entered.\n" +
                    "EX: 100, 200, 5000");
            inputError.showAndWait();
        }
        quantityTextField.clear();
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
     * @param input
     * @return
     */
    private Boolean isValidInput2(String input){
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

}
