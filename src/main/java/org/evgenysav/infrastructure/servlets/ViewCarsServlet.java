package org.evgenysav.infrastructure.servlets;

import lombok.SneakyThrows;
import org.evgenysav.infrastructure.core.impl.ApplicationContext;
import org.evgenysav.infrastructure.dto.classes_dto.VehicleDto;
import org.evgenysav.infrastructure.dto.entity.Vehicles;
import org.evgenysav.infrastructure.dto.service.VehicleService;
import org.evgenysav.util.DtoConverter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/viewCars")
public class ViewCarsServlet extends HttpServlet {

    private VehicleService vehicleService;

    @SneakyThrows
    @Override
    public void init() throws ServletException {
        super.init();
        ApplicationContext applicationContext = (ApplicationContext) getServletContext().getAttribute("applicationContext");
        vehicleService = applicationContext.getObject(VehicleService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("cars", getVehiclesDto());
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/jsp/viewCarsJSP.jsp");
        dispatcher.forward(req, resp);
    }

    private List<VehicleDto> getVehiclesDto() {
        List<Vehicles> vehicleListFromDB = vehicleService.getAll();
        return DtoConverter.getVehiclesDto(vehicleListFromDB);
    }
}
