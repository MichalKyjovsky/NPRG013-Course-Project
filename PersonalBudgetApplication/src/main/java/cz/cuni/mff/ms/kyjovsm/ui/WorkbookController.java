package cz.cuni.mff.ms.kyjovsm.ui;

import cz.cuni.mff.ms.kyjovsm.additionalUtils.AlertBox;
import cz.cuni.mff.ms.kyjovsm.additionalUtils.Tools;
import cz.cuni.mff.ms.kyjovsm.applicationExceptions.FXMLLoaderException;
import cz.cuni.mff.ms.kyjovsm.workbook.SheetBuilder;
import cz.cuni.mff.ms.kyjovsm.workbook.WorkbookBuilder;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class WorkbookController {

    @FXML
    Button submitMonthButton;
    @FXML
    Button submitButton;
    @FXML
    TextField inputField;
    private Stage window;
    private AlertBox alertBox;
    private Scene workBookInitializer;
    private final String workbookControllerClassName = "cz.cuni.mff.ms.kyjovsm.ui.WorkbookController";
    private final Tools tool;

    public static void setNameOfDoc(String nameOfDoc) {
        WorkbookController.nameOfDoc = nameOfDoc;
    }

    private static String nameOfDoc;

    public WorkbookController(){
        tool = new Tools();
    }

    public Stage getElement(){
        return this.window;
    }


    /**
     * Method enables user to invoke setup dialog of new Workbook
     * @throws FXMLLoaderException
     */
    void createWorkbook() throws FXMLLoaderException{
        String relatedFxmlWorkbook = "ui/Workbook.fxml";
        try {
            window = new Stage();
            workBookInitializer = new Scene(tool.loadFXML(Class.forName(workbookControllerClassName), relatedFxmlWorkbook));
            window.setScene(workBookInitializer);
            window.show();
        }catch (Exception e){
            throw new FXMLLoaderException(relatedFxmlWorkbook);
        }
    }


    /**
     * Method enables user to setup new Workbook name
     * @throws FXMLLoaderException
     */
    public void setUpNameOfDocument() throws FXMLLoaderException{
        nameOfDoc = inputField.getText();
        alertBox = new AlertBox();
        String relatedFxmlInitialMonthDialog = "ui/InitialMonthDialog.fxml";
        try {
            Stage stage = (Stage) submitButton.getScene().getWindow();

            if (!nameOfDoc.matches("[a-zA-Z0-9][a-zA-Z0-9_ -]*")) {
                alertBox.displayAlertBox(AlertBox.ALERT_BOX_EMPTY_INPUT);
                inputField.setText("");
            } else {
                SheetBuilder.setNameOfTheDocument(nameOfDoc);
                workBookInitializer = new Scene(tool.loadFXML(Class.forName(workbookControllerClassName), relatedFxmlInitialMonthDialog));
                stage.setScene(workBookInitializer);
                stage.show();
            }
        }
        catch (Exception e){
            throw new FXMLLoaderException(relatedFxmlInitialMonthDialog);
        }
    }


    /**
     * Method enables to user set up initial tracking month
     */
    @FXML
    private void setupInitialMonth(){
        Stage stage = null;
        alertBox = new AlertBox();
        try {
            stage = (Stage) submitMonthButton.getScene().getWindow();
        }catch (Exception e){
            e.printStackTrace();
        }

        String monthInput = inputField.getCharacters().toString();
        try {
            if (!monthInput.isEmpty() && monthInput.matches("[0-9]*")) {

                int month = Integer.parseInt(monthInput);

                if(month >= 1 && month <=12) {
                    try {
                        WorkbookBuilder workbookBuilder = new WorkbookBuilder();
                        WorkbookBuilder.setInitialMonth(month);
                        String sheetBuilderControllerClassName = "cz.cuni.mff.ms.kyjovsm.ui.SheetBuilderController";
                        (Class.forName(sheetBuilderControllerClassName));
                        String relatedFxmlSheet = "ui/Sheet.fxml";
                        App.changeScene(new Scene(tool.loadFXML(Class.forName(sheetBuilderControllerClassName), relatedFxmlSheet)));
                        SheetBuilderController.setBudget_tracker(workbookBuilder.createInitialWorkbook());
                        stage.close();
                    }catch (Exception e){
                        e.printStackTrace();
                       // throw new FXMLLoaderException(relatedFxmlSheet);
                    }
                }
                else{
                    alertBox.displayAlertBox(AlertBox.ALERT_BOX_INVALID_INPUT);
                    inputField.setText("");
                }
            }
            else{
                alertBox.displayAlertBox(AlertBox.ALERT_BOX_INVALID_INPUT);
                inputField.setText("");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

