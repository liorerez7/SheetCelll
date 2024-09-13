
package Controller.Grid;

import Controller.Main.MainController;
import Controller.Utility.StringParser;
import CoreParts.impl.DtoComponents.DtoCell;
import CoreParts.impl.DtoComponents.DtoSheetCell;
import CoreParts.smallParts.CellLocation;
import CoreParts.smallParts.CellLocationFactory;
import expression.api.EffectiveValue;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import java.util.*;



public class GridController {

    @FXML
    private GridPane grid;

    @FXML
    private Map<CellLocation, Label> cellLocationToLabel = new HashMap<>();
    private Map<CellLocation, CustomCellLabel> cellLocationToCustomCellLabel = new HashMap<>();

    private MainController mainController;
    private NeighborsHandler neighborsHandler;
    private final static int DELTA_EXTENSION_GRID = 2;
    private final static int MIN_CELL_SIZE = 30;
    private final static int MAX_CELL_SIZE = 300;
    private final static int CELL_SIZE_CHANGE = 10;


    private ProgressBar progressBar;

    public ProgressBar getProgressBar() {
        // Create a new ProgressBar
        progressBar = new ProgressBar(0);
        progressBar.setMaxWidth(200);
        progressBar.setPrefHeight(30);

        // Add some margin to the right
        GridPane.setMargin(progressBar, new Insets(0, 0, 0, 100));  // 100px to the right

        // Place it in the center of the grid
        grid.add(progressBar, 0, 0, grid.getColumnCount(), grid.getRowCount()); // Span across the grid

        return progressBar;
    }

    public void removeProgressBar() {
        if (progressBar != null) {
            Platform.runLater(() -> grid.getChildren().remove(progressBar)); // Remove the progress bar from the grid
            progressBar = null;
        }
    }


    public void initializeEmptyGrid(DtoSheetCell sheetCell, GridPane grid) {

        grid.getStylesheets().add(Objects.requireNonNull(getClass().getResource("ExelBasicGrid.css")).toExternalForm());
        grid.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE); // Allow the grid to resize dynamically
        grid.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        int numCols = sheetCell.getNumberOfColumns();
        int numRows = sheetCell.getNumberOfRows();
        int cellWidth = sheetCell.getCellWidth() * DELTA_EXTENSION_GRID;
        int cellLength = sheetCell.getCellLength() * DELTA_EXTENSION_GRID;

        clearGrid(grid);
        setupColumnConstraints(grid, numCols, cellWidth);
        setupRowConstraints(grid, numRows, cellLength);
        addColumnHeaders(grid, numCols, cellWidth, cellLength);
        addRowHeaders(grid, numRows, cellWidth, cellLength);
    }

    public Map<CellLocation, Label> initializeGrid(DtoSheetCell sheetCell) {
        neighborsHandler = new NeighborsHandler();
        initializeEmptyGrid(sheetCell,grid);
        int numCols = sheetCell.getNumberOfColumns();
        int numRows = sheetCell.getNumberOfRows();
        int cellWidth = sheetCell.getCellWidth();
        int cellLength = sheetCell.getCellLength();

        cellWidth = cellWidth * DELTA_EXTENSION_GRID;
        cellLength = cellLength * DELTA_EXTENSION_GRID;


        Map<CellLocation, EffectiveValue> viewSheetCell = sheetCell.getViewSheetCell();
        // Add cells with Label
        for (int row = 1; row <= numRows; row++) {
            for (int col = 1; col <= numCols; col++) {
                Label cell = new Label();
                //cell.getStyleClass().add("cell-label");
                CustomCellLabel customCellLabel = new CustomCellLabel(cell);
                customCellLabel.applyDefaultStyles();
                customCellLabel.setAlignment(Pos.CENTER);
                customCellLabel.setTextAlignment(TextAlignment.CENTER);
                cellLocationToCustomCellLabel.put(CellLocationFactory.fromCellId((char) ('A' + col - 1), String.valueOf(row)), customCellLabel);

                setLabelSize(cell, cellWidth, cellLength);
                // Bind the Label's textProperty to the EffectiveValue
                char colChar = (char) ('A' + col - 1);
                String rowString = String.valueOf(row);
                cell.setId(colChar + rowString);

                CellLocation location = new CellLocation(colChar, rowString);
                EffectiveValue effectiveValue = viewSheetCell.get(location);

                if (effectiveValue != null) {
                    String textForLabel = StringParser.convertValueToLabelText(effectiveValue);
                    cell.setText(textForLabel);
                }
                cell.setOnMouseClicked(event -> onCellClicked(cell.getId()));
                cellLocationToLabel.put(location, cell);
                grid.add(cell,col, row);
            }
        }

        return cellLocationToLabel;
    }

    public void initializeCirclePopUp(GridPane grid, DtoSheetCell sheetCell, List<CellLocation> neighbors) {
        initializeEmptyGrid(sheetCell, grid);
        int numCols = sheetCell.getNumberOfColumns();
        int numRows = sheetCell.getNumberOfRows();
        int cellWidth = sheetCell.getCellWidth();
        int cellLength = sheetCell.getCellLength();
        cellWidth = cellWidth * DELTA_EXTENSION_GRID;
        cellLength = cellLength * DELTA_EXTENSION_GRID;


        Map<CellLocation, EffectiveValue> viewSheetCell = sheetCell.getViewSheetCell();

        for (int row = 1; row <= numRows; row++) {
            for (int col = 1; col <= numCols; col++) {
                Label cell = new Label();

                CustomCellLabel customCellLabel = new CustomCellLabel(cell);
                customCellLabel.applyDefaultStyles();
                customCellLabel.setAlignment(Pos.CENTER);
                customCellLabel.setTextAlignment(TextAlignment.CENTER);
                setLabelSize(cell, cellWidth, cellLength);

                // Bind the Label's textProperty to the EffectiveValue
                char colChar = (char) ('A' + col - 1);
                String rowString = String.valueOf(row);
                cell.setId(colChar + rowString);
                CellLocation location = new CellLocation(colChar, rowString);

                if (neighbors.contains(location)) {
                    // Highlight the circular dependency cell by setting the background color
                    cell.setText(colChar + rowString);
                    cell.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

                    // Apply background color animation
                    applyBackgroundColorAnimation(cell);
                } else {
                    EffectiveValue effectiveValue = viewSheetCell.get(location);
                    if (effectiveValue != null) {
                        String textForLabel = StringParser.convertValueToLabelText(effectiveValue);
                        cell.setText(textForLabel);
                    }
                }

                grid.add(cell, col, row);
            }
        }
    }

    private void applyBackgroundColorAnimation(Label cell) {
        // Create a Timeline for background color animation
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(cell.backgroundProperty(), new Background(new BackgroundFill(Color.RED, null, null)))),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(cell.backgroundProperty(), new Background(new BackgroundFill(Color.WHITE, null, null)))),
                new KeyFrame(Duration.seconds(1), new KeyValue(cell.backgroundProperty(), new Background(new BackgroundFill(Color.RED, null, null))))
        );

        timeline.setCycleCount(Timeline.INDEFINITE);  // Loop the animation
        timeline.play();  // Start the animation
    }

    private void setLabelSize(Label label, int width, int height) {

        label.setMinWidth(width);
        label.setMinHeight(height);
        label.setPrefWidth(width);
        label.setPrefHeight(height);
        label.setMaxWidth(width);
        label.setMaxHeight(height);
        label.setPadding(Insets.EMPTY);  // Removes padding
    }

    private void onCellClicked(String location) {
        mainController.cellClicked(location);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void initializePopupGrid(GridPane grid, DtoSheetCell sheetCell) {
        initializeEmptyGrid(sheetCell, grid);
        int numCols = sheetCell.getNumberOfColumns();
        int numRows = sheetCell.getNumberOfRows();
        int cellWidth = sheetCell.getCellWidth();
        int cellLength = sheetCell.getCellLength();
        cellWidth = cellWidth * DELTA_EXTENSION_GRID;
        cellLength = cellLength * DELTA_EXTENSION_GRID;


        Map<CellLocation, EffectiveValue> viewSheetCell = sheetCell.getViewSheetCell();
        for (int row = 1; row <= numRows; row++) {
            for (int col = 1; col <= numCols; col++) {
                Label cell = new Label();
                CustomCellLabel customCellLabel = new CustomCellLabel(cell);
                customCellLabel.applyDefaultStyles();
                customCellLabel.setAlignment(Pos.CENTER);
                customCellLabel.setTextAlignment(TextAlignment.CENTER);
                setLabelSize(cell, cellWidth, cellLength);

                // Bind the Label's textProperty to the EffectiveValue
                char colChar = (char) ('A' + col - 1);
                String rowString = String.valueOf(row);
                cell.setId(colChar + rowString);
                CellLocation location = new CellLocation(colChar, rowString);
                EffectiveValue effectiveValue = viewSheetCell.get(location);
                if (effectiveValue != null) {
                    String textForLabel = StringParser.convertValueToLabelText(effectiveValue);
                    cell.setText(textForLabel);
                }
                grid.add(cell, col, row);
            }
        }
    }

    private void clearGrid(GridPane grid) {
        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();
        grid.getChildren().clear();
    }

    public void hideGrid() {
        // Hide all children of the grid except the progress bar
        for (Node child : grid.getChildren()) {
            if (!(child instanceof ProgressBar)) {
                child.setVisible(false);
                child.setManaged(false); // Prevent taking up space in layout
            }
        }
    }

    public void showGrid() {
        // Show all children of the grid
        for (Node child : grid.getChildren()) {
            child.setVisible(true);
            child.setManaged(true); // Ensure layout space is managed properly
        }
    }


    private void setupColumnConstraints(GridPane grid, int numCols, int cellWidth) {
        for (int i = 0; i < numCols + 1; i++) { // +1 for header column
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setMinWidth(cellWidth);
            colConstraints.setPrefWidth(cellWidth);

            colConstraints.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(colConstraints);
        }
    }

    private void setupRowConstraints(GridPane grid, int numRows, int cellLength) {
        for (int i = 0; i < numRows + 1; i++) { // +1 for header row
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setMinHeight(cellLength);
            rowConstraints.setPrefHeight(cellLength);
            rowConstraints.setMaxHeight(cellLength);
            rowConstraints.setVgrow(Priority.ALWAYS);
            grid.getRowConstraints().add(rowConstraints);
        }
    }

    private void addColumnHeaders(GridPane grid, int numCols, int cellWidth, int cellLength) {
        for (int col = 1; col <= numCols; col++) {
            Label header = new Label(String.valueOf((char) ('A' + col - 1)));
            setLabelSize(header, cellWidth, cellLength);
            header.getStyleClass().add("header-label");
            grid.add(header, col, 0);
        }
    }

    private void addRowHeaders(GridPane grid, int numRows, int cellWidth, int cellLength) {
        for (int row = 1; row <= numRows; row++) {
            Label header = new Label(String.valueOf(row));
            setLabelSize(header, cellWidth, cellLength);
            header.getStyleClass().add("header-label");
            grid.add(header, 0, row);
        }
    }

    public void showAffectedCells(List<CellLocation> requestedRange) {
        neighborsHandler.showAffectedCells(requestedRange, cellLocationToLabel, cellLocationToCustomCellLabel);
    }

    public void showNeighbors(DtoCell cell) {
        neighborsHandler.showNeighbors(cell, cellLocationToLabel, cellLocationToCustomCellLabel);
    }

    public void clearAllHighlights() {
        neighborsHandler.clearAllHighlights(cellLocationToLabel, cellLocationToCustomCellLabel);
    }

    public void changingGridConstraints(String RowOrColumn, int increaseOrDecrease) {

        // Calculate the value to change
        int valueToChange = increaseOrDecrease * CELL_SIZE_CHANGE;
        boolean isColumn = isColumn(RowOrColumn);

        int index = isColumn ? RowOrColumn.charAt(0) - 'A' + 1 : Integer.parseInt(RowOrColumn);

        if (isColumn) {
            updateColumnConstraints(index, valueToChange);
        } else {
            updateRowConstraints(index, valueToChange);
        }
    }

    private boolean isColumn(String RowOrColumn) {
        return RowOrColumn.charAt(0) >= 'A' && RowOrColumn.charAt(0) <= 'Z';
    }

    private void updateColumnConstraints(int columnIndex, int valueToChange) {
        ColumnConstraints columnConstraint = grid.getColumnConstraints().get(columnIndex);
        double newWidth = columnConstraint.getPrefWidth() + valueToChange;
        newWidth = Math.max(MIN_CELL_SIZE, Math.min(newWidth, MAX_CELL_SIZE));
        columnConstraint.setPrefWidth(newWidth);
        columnConstraint.setMinWidth(newWidth);
        columnConstraint.setMaxWidth(newWidth);

        updateColumnHeadersAndCells(columnIndex, (int) newWidth);
    }

    private void updateRowConstraints(int rowIndex, int valueToChange) {
        RowConstraints rowConstraint = grid.getRowConstraints().get(rowIndex);
        double newHeight = rowConstraint.getPrefHeight() + valueToChange;
        newHeight = Math.max(MIN_CELL_SIZE, Math.min(newHeight, MAX_CELL_SIZE));
        rowConstraint.setPrefHeight(newHeight);
        rowConstraint.setMinHeight(newHeight);
        rowConstraint.setMaxHeight(newHeight);

        updateRowHeadersAndCells(rowIndex, (int) newHeight);
    }

    private void updateColumnHeadersAndCells(int columnIndex, int newWidth) {
        for (Node node : grid.getChildren()) {
            Integer colIndex = GridPane.getColumnIndex(node);
            if (colIndex != null && colIndex == columnIndex) {
                if (GridPane.getRowIndex(node) == 0) { // Header row
                    Label header = (Label) node;
                    setLabelSize(header, newWidth, (int) header.getPrefHeight());
                } else { // Data cells
                    Label cell = (Label) node;
                    setLabelSize(cell, newWidth, (int) cell.getPrefHeight());
                }
            }
        }
    }

    private void updateRowHeadersAndCells(int rowIndex, int newHeight) {
        for (Node node : grid.getChildren()) {
            Integer row = GridPane.getRowIndex(node);
            if (row != null && row == rowIndex) {
                if (GridPane.getColumnIndex(node) == 0) { // Header column
                    Label header = (Label) node;
                    setLabelSize(header, (int) header.getPrefWidth(), newHeight);
                } else { // Data cells
                    Label cell = (Label) node;
                    setLabelSize(cell, (int) cell.getPrefWidth(), newHeight);
                }
            }
        }
    }

    public void changeTextAlignment(String alignment, String selectedColumnLabel) {

        int selectedColumnIndex = selectedColumnLabel.charAt(0)- 'A' + 1;

        Pos pos = null;
        alignment = alignment.toLowerCase();

        switch (alignment){
            case "left":
                pos = Pos.CENTER_LEFT;
                break;
            case "center":
                pos = Pos.CENTER;
                break;
            case "right":
                pos = Pos.CENTER_RIGHT;
                break;
        }

        for (Node node : grid.getChildren()) {
            Integer colIndex = GridPane.getColumnIndex(node);
            if (colIndex != null && colIndex == selectedColumnIndex) {
                if(GridPane.getRowIndex(node) != 0){ // not including the headers
                    Label cell = (Label) node;

                    cell.setAlignment(pos);
                    if (pos == Pos.CENTER) {
                        cell.setTextAlignment(TextAlignment.CENTER);
                    } else if (pos == Pos.CENTER_LEFT) {
                        cell.setTextAlignment(TextAlignment.LEFT);
                    } else if (pos == Pos.CENTER_RIGHT) {
                        cell.setTextAlignment(TextAlignment.RIGHT);
                    }
                }
            }
        }
    }

    public void changeBackgroundTextColor(Color value, String location) {

        Label cell = cellLocationToLabel.get(CellLocationFactory.fromCellId(location));
        Pos alignment = cell.getAlignment();

        CustomCellLabel customCellLabel = cellLocationToCustomCellLabel.get(CellLocationFactory.fromCellId(location));
       // CustomCellLabel customCellLabel = new CustomCellLabel(cell);
        customCellLabel.setBackgroundColor(value);
        customCellLabel.setAlignment(alignment);
    }

    public void changeTextColor(Color value, String location) {
        Label cell = cellLocationToLabel.get(CellLocationFactory.fromCellId(location));
        if (cell != null) {
            CustomCellLabel customCellLabel = cellLocationToCustomCellLabel.get(CellLocationFactory.fromCellId(location));
            //CustomCellLabel customCellLabel = new CustomCellLabel(cell);
            customCellLabel.setTextColor(value);
            //cell.setTextFill(value);
        }
    }

    public Map<CellLocation, CustomCellLabel> getCellLocationToCustomCellLabel() {
        return cellLocationToCustomCellLabel;
    }
}


