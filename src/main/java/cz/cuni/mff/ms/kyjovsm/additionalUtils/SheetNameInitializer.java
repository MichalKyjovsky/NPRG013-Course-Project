package cz.cuni.mff.ms.kyjovsm.additionalUtils;

import cz.cuni.mff.ms.kyjovsm.applicationExceptions.FXMLLoaderException;
import cz.cuni.mff.ms.kyjovsm.ui.App;
import cz.cuni.mff.ms.kyjovsm.ui.SheetBuilderController;
import cz.cuni.mff.ms.kyjovsm.workbook.SheetBuilder;
import cz.cuni.mff.ms.kyjovsm.workbook.WorkbookBuilder;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

public class SheetNameInitializer {
    @FXML
    private MenuButton selectMonthButton;
    @FXML
    private Button submitButton;
    @FXML
    private TextField inputLine;
    private Stage dialogWindow;
    private String sheetNameInitializerClassName = "cz.cuni.mff.ms.kyjovsm.additionalUtils.SheetNameInitializer";
    private AlertBox alertBox;
    private SheetBuilder sheetBuilder;

    public SheetNameInitializer() {
        sheetBuilder = new SheetBuilder();
        alertBox = new AlertBox();
        dialogWindow = new Stage();
    }

    public Stage getDialogWindow() {
        return dialogWindow;
    }


    /**
     * Based on user input method will initiate new  tracking month for given input and
     * new sheet based on the given information will be created.
     * @throws FXMLLoaderException
     */
    public void setNewTrackingMonth() throws FXMLLoaderException {
        Tools tool = new Tools();
        dialogWindow.setResizable(false);
        String relatedFxmlSheet = "additionalUtils/SheetNameInitializer.fxml";
        try {
             dialogWindow.setScene(new Scene(tool.loadFXML(Class.forName(sheetNameInitializerClassName), relatedFxmlSheet)));
        } catch (Exception e) {
            throw new FXMLLoaderException(relatedFxmlSheet);
        }
        dialogWindow.show();
    }


    /**
     * When user press "ADD NEW COLUMN" method starts initiation process.
     * @throws FXMLLoaderException
     */
    public void addNewColumn() throws FXMLLoaderException {
        dialogWindow.setResizable(false);
        Tools tool = new Tools();
        String RelatedFxmlColumn = "additionalUtils/ColumnNameInitializer.fxml";
        try {
            dialogWindow.setScene(new Scene(tool.loadFXML(Class.forName(sheetNameInitializerClassName), RelatedFxmlColumn)));
        } catch(Exception e) {
            throw new FXMLLoaderException(RelatedFxmlColumn);
        }
        dialogWindow.show();
    }

    /**
     * When a new tracking month is provided by user, method
     * wul then secure new sheet initiation and creation.
     */
    @FXML
    private void submitMonth() {
        List<MenuItem> menuItems = selectMonthButton.getItems();

        for (MenuItem mi : menuItems){
            mi.setOnAction(e -> {
                int index = menuItems.indexOf(mi) + 1;
                try {
                    Stage stage = (Stage) selectMonthButton.getScene().getWindow();
                    sheetBuilder.createNewSheet(index);
                    stage.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

    /**
     * When name of new column is provided by user, new column is then
     * initiated and refactoring is performed.
     */
    @FXML
    private void submitName() {
        Stage stage = (Stage) submitButton.getScene().getWindow();
        String newColumnName = inputLine.getCharacters().toString();

        if (newColumnName.isBlank() || newColumnName.isEmpty()) {
            alertBox.displayAlertBox(AlertBox.ALERT_BOX_EMPTY_INPUT);
        }
        else if (!newColumnName.matches("[a-zA-z][a-zA-Z0-9_-]*")){
            alertBox.displayAlertBox(AlertBox.ALERT_BOX_INVALID_INPUT);
        }
        else{
            sheetBuilder.createNewColumn(newColumnName);
            stage.close();
        }
    }
}
