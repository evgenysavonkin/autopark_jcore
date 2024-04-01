package org.evgenysav.infrastructure.servlets;

import org.evgenysav.infrastructure.core.impl.ApplicationContext;
import org.evgenysav.infrastructure.dto.classes_dto.VehicleTypeDto;
import org.evgenysav.infrastructure.dto.entity.Types;
import org.evgenysav.infrastructure.dto.service.TypesService;
import org.evgenysav.util.DtoConverter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/viewTypes")
public class ViewCarsTypesServlet extends HttpServlet {

    private TypesService typesService;

    @Override
    public void init() throws ServletException {
        super.init();
        ApplicationContext applicationContext = (ApplicationContext) getServletContext().getAttribute("applicationContext");
        typesService = applicationContext.getObject(TypesService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        synchronized (this) {
            req.setAttribute("types", getTypesDto());
        }

        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/jsp/viewTypesJSP.jsp");
        dispatcher.forward(req, resp);
    }

    private List<VehicleTypeDto> getTypesDto() {
        List<Types> typesFromDB = typesService.getAll();
        return DtoConverter.getVehiclesTypeDto(typesFromDB);
    }
}
