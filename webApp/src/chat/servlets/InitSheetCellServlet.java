package chat.servlets;

import CoreParts.api.Engine;
import CoreParts.impl.DtoComponents.DtoSheetCell;
import CoreParts.smallParts.CellLocation;
import chat.constants.Constants;
import chat.utils.ServletUtils;
import chat.utils.jsonSerializableClasses.CellLocationMapSerializer;
import chat.utils.jsonSerializableClasses.DtoSheetCellSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import expression.impl.variantImpl.EffectiveValue;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class InitSheetCellServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String xmlAddress = request.getParameter("xmlAddress");

        Engine engine = ServletUtils.getEngine(getServletContext());

        try{
            engine.readSheetCellFromXML(xmlAddress);
        }
        catch (Exception e){
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.getOutputStream().print("Error: " + e.getMessage());
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        Engine engine = ServletUtils.getEngine(getServletContext());
        DtoSheetCell dtoSheetCell = engine.getSheetCell();

        PrintWriter out = response.getWriter();
        String jsonResponse = Constants.GSON_INSTANCE.toJson(dtoSheetCell);
        out.print(jsonResponse);
        out.flush();

    }
}
