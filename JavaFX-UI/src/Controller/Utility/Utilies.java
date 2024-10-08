package Controller.Utility;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.skin.MenuButtonSkin;
import javafx.scene.paint.Color;

public class Utilies {

    public static void switchStyleClass(Node node, String newStyleClass, String... classesToRemove) {
        // Remove any of the classes in the classesToRemove array if they exist
        for (String styleClass : classesToRemove) {
            if (node.getStyleClass().contains(styleClass)) {
                node.getStyleClass().remove(styleClass);
            }
        }

        // Add the new style class
        node.getStyleClass().add(newStyleClass); // Add your new class here
    }

    public static void setComboBoxHeaderTextColor(ComboBox<Label> comboBox, Color color) {
        comboBox.setCellFactory(cb -> new ListCell<Label>() {
            @Override
            protected void updateItem(Label item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getText());
                    setTextFill(Color.BLACK); // Set text color for dropdown items
                }
            }
        });

        comboBox.setButtonCell(new ListCell<Label>() {
            @Override
            protected void updateItem(Label item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(comboBox.getPromptText());
                    setTextFill(color); // Set text color for ComboBox header
                } else {
                    setText(item.getText());
                    setTextFill(color); // Set text color for ComboBox header
                }
            }
        });
    }
    public static void setMenuButtonTextColor(MenuButton menuButton, Color color) {
        // Set the text color for the MenuButton button
        menuButton.setStyle("-fx-text-fill: " + toHexString(color) + ";");

        // Get the skin of the MenuButton to access the items
//        MenuButtonSkin skin = (MenuButtonSkin) menuButton.getSkin();
//        if (skin != null) {
//            for (MenuItem menuItem : menuButton.getItems()) {
//                // Customize each MenuItem to set the text color
//                menuItem.setStyle("-fx-text-fill: " + toHexString(color) + ";");
//            }
//        }
    }

    // Helper method to convert Color to Hex String
    private static String toHexString(Color color) {
        return String.format("#%02x%02x%02x",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
    }


}
