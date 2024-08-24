package CoreParts.impl.controller.commands;

import CoreParts.api.Engine;
import CoreParts.impl.UtilisUI.MenuHandler;
import CoreParts.impl.UtilisUI.MenuTypes;

public class LoadSheetFromXML extends SheetEngineCommand {

    public LoadSheetFromXML(Engine engine, MenuHandler menuHandler) {
        super(engine, menuHandler);
    }

    @Override
    public void execute() throws Exception {
        String path = inputHandler.getFilePathInput();
        if (path == null){
            menuHandler.setMenuStatus(MenuTypes.FIRST_MENU);
        }
        try{
            engine.readSheetCellFromXML(path);
            System.out.println("Sheet loaded successfully");
        }catch (Exception e){
            throw new IllegalArgumentException("path is not valid");
        }
        menuHandler.setMenuStatus(MenuTypes.SECOND_MENU);
    }
}
