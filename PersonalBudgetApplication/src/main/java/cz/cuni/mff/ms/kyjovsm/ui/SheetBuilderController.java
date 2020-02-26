package cz.cuni.mff.ms.kyjovsm.ui;

import cz.cuni.mff.ms.kyjovsm.exceptions.WorkbookBuilderException;
import cz.cuni.mff.ms.kyjovsm.workbook.SheetBuilder;
import cz.cuni.mff.ms.kyjovsm.workbook.WorkbookBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class SheetBuilderController {
    private static SheetBuilder sheetBuilder;
    private static WorkbookController workbookController;
    private static WorkbookBuilder workbookBuilder;
    @FXML
    private Button addSheetButton;
    @FXML
    private Button saveWorkbookButton;



    public void createNewSheet(){
        //TODO:implement sheet initiation -> Do it in constructor you genius
        System.err.println("NEEDS TO BE IMPLEMENTED");
    }

    public void saveWorkbook(){
        Stage dialogWindow = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(WorkbookController.nameOfDoc);
        //TODO: Should do fine
//        try {
            File a = fileChooser.showSaveDialog(dialogWindow);
//            workbookBuilder.saveWorkbook(a);
            System.out.println(a);
//        }catch (WorkbookBuilderException wbe){
//            wbe.getMessage();
//        }
    }



}
