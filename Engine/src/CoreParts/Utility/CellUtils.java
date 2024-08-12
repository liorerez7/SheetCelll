package CoreParts.Utility;

import CoreParts.impl.CellImp;
import Operation.impl.Operation;
import Operation.impl.Number;

import java.util.ArrayList;
import java.util.List;



public class CellUtils {

    public static boolean trySetNumericValue(CellImp cell, String value) {
        try {
            Double numericValue = Double.parseDouble(value);
            cell.setEffectiveValue(new Number(numericValue));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static List<String> processFunction(String newValue) {
            return getCellAsStringRepresention(newValue);
    }

    public static boolean isPotentialOperation(String newValue) // TODO : implement function
    {
        if (newValue.startsWith("{") && newValue.endsWith("}")) {
            return true;
        }
        else{
            return false;
        }
    }

    public static String extractFunctionName(String input) {


        String content = input.substring(1, input.length() - 1).trim();
        // Split the content by space
        String[] parts = content.split(" ");

        if (parts.length > 0) {
            return parts[0]; // The function name is the first part
        }

        throw new IllegalArgumentException("Invalid function format");
    }

    private static List<String> getCellAsStringRepresention(String newValue) {
        List<String> cells = new ArrayList<>();
        String content = newValue.substring(1, newValue.length() - 1).trim();
        // Split the content by space
        String[] parts = content.split(" ");
        for (int i = 1; i < parts.length; i++) {
            cells.add(parts[i]);
        }
        return cells;
    }

    public  static void checkIfCellsAreOfSameType(List<CellImp> cells) {
    Class clazz = cells.get(0).getEffectiveValue().getClass();
    for (CellImp cell :cells){
        if (cell.getEffectiveValue().getClass() != clazz){
            throw new IllegalArgumentException("Cells are not of the same type");
        }
    }


    /* TODO : other implenetation option:

            try {
                cellToBeUpdated.effectiveValue.evaluate();
                return true;    // can be evaluated
            }
            catch(Exception e){
                return false;   // can't be evaluated
               }

            TODO: Reason: some methods are using diffrenet types, for example
                           EQUAL.
    */
    }
}
