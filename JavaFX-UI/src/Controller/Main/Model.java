package Controller.Main;

import Controller.Utility.StringParser;
import CoreParts.impl.DtoComponents.DtoCell;
import CoreParts.impl.DtoComponents.DtoSheetCell;
import CoreParts.smallParts.CellLocation;
import CoreParts.smallParts.CellLocationFactory;
import expression.api.EffectiveValue;
import javafx.beans.property.*;
import javafx.scene.control.Label;


import java.util.HashMap;
import java.util.Map;

public class Model {

    private final Map<Label, StringProperty> cellLabelToProperties = new HashMap<>();

    private DtoSheetCell sheetCell;
    private BooleanProperty isCellLabelClicked;
    private StringProperty latestUpdatedVersionProperty;
    private StringProperty originalValueLabelProperty;
    private StringProperty totalVersionsProperty;
    private BooleanProperty isColumnSelected;
    private BooleanProperty isRowSelected;
    private BooleanProperty readingXMLSuccessProperty;
    private StringProperty cellLocationProperty;


    Model(DtoSheetCell sheetCell) {
        this.sheetCell = sheetCell;

        isCellLabelClicked = new SimpleBooleanProperty(false);
        latestUpdatedVersionProperty = new SimpleStringProperty("");
        originalValueLabelProperty = new SimpleStringProperty("");
        totalVersionsProperty = new SimpleStringProperty("");
        isColumnSelected = new SimpleBooleanProperty(false);
        isRowSelected = new SimpleBooleanProperty(false);
        readingXMLSuccessProperty = new SimpleBooleanProperty(false);
        cellLocationProperty = new SimpleStringProperty("");
    }

    private DtoSheetCell runTimeAnalysisSheetCell;
    private final Map<Label, StringProperty> cellLocationToPropertiesRunTimeAnalysis = new HashMap<>();


    public void bindCellLabelToPropertiesRunTimeAnalysis () {
        cellLocationToPropertiesRunTimeAnalysis.forEach((label, property) -> {
            label.textProperty().bind(property);

        });
    }

    public void setPropertiesByDtoSheetCellRunTimeAnalsys(DtoSheetCell sheetCell) {
        this.runTimeAnalysisSheetCell = sheetCell;
        cellLocationToPropertiesRunTimeAnalysis.forEach((label, property) -> {
            CellLocation cellLocation = CellLocationFactory.fromCellId(label.getId());
            EffectiveValue effectiveValue = sheetCell.getViewSheetCell().get(cellLocation);

            String value = StringParser.convertValueToLabelText(effectiveValue);

            property.set(value);

        });
    }

    public void setCellLabelToPropertiesRunTimeAnalysis(Map<CellLocation,Label> cellLocationLabelMap) {

        cellLocationLabelMap.forEach((cellLocation, label) -> {
            cellLocationToPropertiesRunTimeAnalysis.put(label, new SimpleStringProperty());

        });

    }


    public void setCellLabelToProperties(Map<CellLocation,Label> cellLocationLabelMap) {

        cellLocationLabelMap.forEach((cellLocation, label) -> {
            cellLabelToProperties.put(label, new SimpleStringProperty());

        });

    }

    public void bindCellLabelToProperties() {
        cellLabelToProperties.forEach((label, property) -> {
            label.textProperty().bind(property);

        });
    }

    public void setPropertiesByDtoSheetCell(DtoSheetCell sheetCell) {
        this.sheetCell = sheetCell;
        cellLabelToProperties.forEach((label, property) -> {
            CellLocation cellLocation = CellLocationFactory.fromCellId(label.getId());
            EffectiveValue effectiveValue = sheetCell.getViewSheetCell().get(cellLocation);

            String value = StringParser.convertValueToLabelText(effectiveValue);

            property.set(value);

        });
    }


    public StringProperty getLatestUpdatedVersionProperty() {
        return latestUpdatedVersionProperty;
    }

    public void setLatestUpdatedVersionProperty(DtoCell cell) {

        String latestVersion = "";

        if(cell != null) {

            latestVersion = Integer.toString(cell.getLatestVersion());
        }

        latestUpdatedVersionProperty.set(latestVersion);
    }

    public BooleanProperty getIsCellLebalClickedProperty() {
        return isCellLabelClicked;
    }

    public void setIsCellLabelClicked(boolean isCellLabelClicked) {
        this.isCellLabelClicked.set(isCellLabelClicked);
    }

    public StringProperty getOriginalValueLabelProperty() {
        return originalValueLabelProperty;
    }

    public void setCellLocationProperty(String cellLocation) {
        cellLocationProperty.set(cellLocation);
    }

    public StringProperty getCellLocationProperty() {
        return cellLocationProperty;
    }

    public void setOriginalValueLabelProperty(DtoCell cell) {

        String OriginalValue = "";

        if(cell != null) {
            OriginalValue = cell.getOriginalValue();
        }

        originalValueLabelProperty.set(OriginalValue);
    }

    public StringProperty getTotalVersionsProperty() {
        return totalVersionsProperty;
    }

    public void setTotalVersionsProperty(int totalVersionsInTheSystem) {
        String totalVersions = "";

        if(totalVersionsInTheSystem != 0) {
            totalVersions = Integer.toString(totalVersionsInTheSystem);
        }

        totalVersionsProperty.set(totalVersions);
    }

    public void setColumnSelected(boolean b) {
        this.isColumnSelected.set(b);
    }

    public void setRowSelected(boolean b) {
        this.isRowSelected.set(b);
    }

    public BooleanProperty getIsColumnSelectedProperty() {
        return isColumnSelected;
    }

    public BooleanProperty getIsRowSelectedProperty() {
        return isRowSelected;
    }

    public void setReadingXMLSuccess(boolean b) {
        readingXMLSuccessProperty.set(b);
    }

    public BooleanProperty getReadingXMLSuccess() {
        return readingXMLSuccessProperty;
    }
}
