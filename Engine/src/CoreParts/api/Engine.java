package CoreParts.api;

import CoreParts.impl.DtoComponents.DtoCell;
import CoreParts.impl.DtoComponents.DtoSheetCell;

public interface Engine {
    DtoCell getRequestedCell(String cellId);
    DtoSheetCell getSheetCell();
    DtoSheetCell getSheetCell(int versionNumber);
    void readSheetCellFromXML(String path) throws Exception;
    void updateCell(String newValue, char col, String row) throws Exception;
    void save(String path) throws Exception;
    void load(String path) throws Exception;
    void UpdateNewRange(String name, String range) throws IllegalArgumentException;


}
