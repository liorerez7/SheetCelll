//package CoreParts.impl.DtoComponents;
//
//import CoreParts.api.Cell;
//import CoreParts.api.sheet.SheetCell;
//import CoreParts.api.sheet.SheetCellViewOnly;
//import CoreParts.smallParts.CellLocation;
//import Utility.RefDependencyGraph;
//import expression.api.EffectiveValue;
//import expression.impl.Range;
//
//import java.util.*;
//
//public class DtoSheetCell implements SheetCellViewOnly {
//
//    private Map<CellLocation,EffectiveValue> sheetCell = new HashMap<>();
//    private Map<Integer, Map<CellLocation, EffectiveValue>> versionToCellsChanges;
//    private Map<String,List<CellLocation>> ranges = new HashMap<>();
//    private String name;
//    private int versionNumber;
//    private int currentNumberOfRows;
//    private int currentNumberOfCols;
//    private int currentCellLength;
//    private int currentCellWidth;
//
//    // Constructor to populate DtoSheetCell from SheetCellImp
//    public DtoSheetCell(SheetCell sheetCellImp) {
//        for (Map.Entry<CellLocation, Cell> entry : sheetCellImp.getSheetCell().entrySet()) {
//            EffectiveValue effectiveValue;
//            effectiveValue= entry.getValue().getEffectiveValue().evaluate(sheetCellImp);
//
//            sheetCell.put(entry.getKey(), effectiveValue);
//        }
//        versionToCellsChanges = sheetCellImp.getVersions();
//        copyBasicTypes(sheetCellImp);
//        Set<Range> systemRanges = sheetCellImp.getSystemRanges();
//        systemRanges.forEach(range -> {
//            ranges.put(range.getRangeName(),range.getCellLocations());
//        });
//    }
//
//    public DtoSheetCell(SheetCell sheetCell, int requestedVersion) {
//
//        Set<CellLocation> markedLocations = new HashSet<>();
//        copyBasicTypes(sheetCell);
//        Map<CellLocation, EffectiveValue> sheetCellChanges = sheetCell.getVersions().get(requestedVersion);
//
//        while (requestedVersion > 0) {
//            for (Map.Entry<CellLocation, EffectiveValue> entry : sheetCellChanges.entrySet()) {
//                CellLocation location = entry.getKey();
//                if(markedLocations.contains(location)) {
//                    continue;
//                }
//                this.sheetCell.put(location, entry.getValue());
//                markedLocations.add(location);
//            }
//
//            requestedVersion--;
//            sheetCellChanges = sheetCell.getVersions().get(requestedVersion);
//       }
//    }
//
//
//    @Override
//    public int getActiveCellsCount() {
//        return 0;
//    }
//
//    /* NEED TO CHANGE! */
//    @Override
//    public Map<CellLocation, Cell> getSheetCell() {
//        return null;
//    }
//
//    @Override
//    public RefDependencyGraph getGraph() {
//        return null;
//    }
//
//    @Override
//    public Cell getCell(CellLocation location) {
//        return null;
//    }
//
//
//    @Override
//    public boolean isCellPresent(CellLocation location) {
//        return false;
//    }
//
//    @Override
//    public Map<CellLocation, EffectiveValue> getViewSheetCell() {
//        return sheetCell;
//    }
//
//    @Override
//    public boolean isRangePresent(String rangeName) {
//        return false;
//    }
//
//    @Override
//    public Range getRange(String rangeName) {
//        return null;
//    }
//
//    @Override
//    public Map<String,List<CellLocation>> getRanges() {
//        return ranges;
//    }
//
//    @Override
//    public List<CellLocation> getRequestedRange(String name) {
//        return ranges.get(name);
//    }
//
//    public void copyBasicTypes(SheetCell sheetCell) {
//        this.name = sheetCell.getSheetName();
//        this.versionNumber = sheetCell.getLatestVersion();
//        this.currentNumberOfRows = sheetCell.getNumberOfRows();
//        this.currentNumberOfCols = sheetCell.getNumberOfColumns();
//        this.currentCellLength = sheetCell.getCellLength();
//        this.currentCellWidth = sheetCell.getCellWidth();
//    }
//
//    public int getCellWidth() {
//        return currentCellWidth;
//    }
//
//    @Override
//    public int getNumberOfRows() {
//        return currentNumberOfRows;
//    }
//
//    @Override
//    public int getNumberOfColumns() {
//        return currentNumberOfCols;
//    }
//
//    @Override
//    public String getSheetName() {
//        return name;
//    }
//
//    @Override
//    public int getLatestVersion() {
//        return versionNumber;
//    }
//
//    public int getCellLength() {
//        return currentCellLength;
//    }
//
//    public EffectiveValue getEffectiveValue(CellLocation cellLocation) {
//        return sheetCell.get(cellLocation);
//    }
//
//    public Map<Integer, Map<CellLocation, EffectiveValue>> getVersionToCellsChanges() {
//        return versionToCellsChanges;
//    }
//
//}



package CoreParts.impl.DtoComponents;

import CoreParts.api.Cell;
import CoreParts.api.sheet.SheetCell;
import CoreParts.api.sheet.SheetCellViewOnly;
import CoreParts.smallParts.CellLocation;
import Utility.RefDependencyGraph;
import expression.api.EffectiveValue;
import expression.impl.Range;

import java.util.*;

public class DtoSheetCell {

    private Map<CellLocation,EffectiveValue> sheetCell = new HashMap<>();
    private Map<Integer, Map<CellLocation, EffectiveValue>> versionToCellsChanges;
    private Map<String,List<CellLocation>> ranges = new HashMap<>();
    private String name;
    private int versionNumber;
    private int currentNumberOfRows;
    private int currentNumberOfCols;
    private int currentCellLength;
    private int currentCellWidth;

    // Constructor to populate DtoSheetCell from SheetCellImp
    public DtoSheetCell(SheetCell sheetCellImp) {
        for (Map.Entry<CellLocation, Cell> entry : sheetCellImp.getSheetCell().entrySet()) {
            EffectiveValue effectiveValue;
            effectiveValue= entry.getValue().getEffectiveValue().evaluate(sheetCellImp);

            sheetCell.put(entry.getKey(), effectiveValue);
        }
        versionToCellsChanges = sheetCellImp.getVersions();
        copyBasicTypes(sheetCellImp);
        Set<Range> systemRanges = sheetCellImp.getSystemRanges();
        systemRanges.forEach(range -> {
            ranges.put(range.getRangeName(),range.getCellLocations());
        });
    }

    public DtoSheetCell(SheetCell sheetCell, int requestedVersion) {

        Set<CellLocation> markedLocations = new HashSet<>();
        copyBasicTypes(sheetCell);
        Map<CellLocation, EffectiveValue> sheetCellChanges = sheetCell.getVersions().get(requestedVersion);

        while (requestedVersion > 0) {
            for (Map.Entry<CellLocation, EffectiveValue> entry : sheetCellChanges.entrySet()) {
                CellLocation location = entry.getKey();
                if(markedLocations.contains(location)) {
                    continue;
                }
                this.sheetCell.put(location, entry.getValue());
                markedLocations.add(location);
            }

            requestedVersion--;
            sheetCellChanges = sheetCell.getVersions().get(requestedVersion);
        }
    }

    public Map<CellLocation, EffectiveValue> getViewSheetCell() {
        return sheetCell;
    }

    public Map<String,List<CellLocation>> getRanges() {
        return ranges;
    }

    public void copyBasicTypes(SheetCell sheetCell) {
        this.name = sheetCell.getSheetName();
        this.versionNumber = sheetCell.getLatestVersion();
        this.currentNumberOfRows = sheetCell.getNumberOfRows();
        this.currentNumberOfCols = sheetCell.getNumberOfColumns();
        this.currentCellLength = sheetCell.getCellLength();
        this.currentCellWidth = sheetCell.getCellWidth();
    }

    public int getCellWidth() {
        return currentCellWidth;
    }

    public int getNumberOfRows() {
        return currentNumberOfRows;
    }

    public int getNumberOfColumns() {
        return currentNumberOfCols;
    }

    public String getSheetName() {
        return name;
    }

    public int getLatestVersion() {
        return versionNumber;
    }

    public int getCellLength() {
        return currentCellLength;
    }

    public EffectiveValue getEffectiveValue(CellLocation cellLocation) {
        return sheetCell.get(cellLocation);
    }

    public Map<Integer, Map<CellLocation, EffectiveValue>> getVersionToCellsChanges() {
        return versionToCellsChanges;
    }

}
