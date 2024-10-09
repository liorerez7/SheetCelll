package chat.servlets.SheetComponentsServlets.RangeServlets;

import CoreParts.api.SheetManager;
import EngineManager.Engine;
import chat.constants.Constants;
import chat.utils.ServletUtils;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DeleteRangeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");

        if (name != null) {

            Engine engine = ServletUtils.getEngineManager(getServletContext());
            String sheetName = (String) request.getSession(false).getAttribute(Constants.SHEET_NAME);
            SheetManager sheetManager = engine.getSheetCell(sheetName);
            //SheetManager sheetManager = ServletUtils.getEngine(getServletContext());
            sheetManager.deleteRange(name);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print("Invalid range name");
        }
    }
}
