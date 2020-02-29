package cz.cuni.mff.ms.kyjovsm.workbook;

public class SheetBuilder{
    private static String nameOfTheDocument;
    private static final String FILE_SUFFIX = ".xslx";

    public static void setNameOfTheDocument(String name){
        if(!name.endsWith(FILE_SUFFIX)) {
            nameOfTheDocument = name + FILE_SUFFIX;
        }
        else{
            nameOfTheDocument = name;
        }
    }

    public static String getNameOfTheDocument() {
        return nameOfTheDocument;
    }

    public void setCellValue(String value){
        if(!value.strip().isEmpty())
            System.out.println(value);
    }
}
