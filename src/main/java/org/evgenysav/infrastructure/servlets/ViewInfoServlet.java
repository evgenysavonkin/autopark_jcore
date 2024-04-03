package org.evgenysav.infrastructure.servlets;

import org.evgenysav.infrastructure.core.impl.ApplicationContext;
import org.evgenysav.infrastructure.dto.classes_dto.RentDto;
import org.evgenysav.infrastructure.dto.classes_dto.VehicleDto;
import org.evgenysav.infrastructure.dto.entity.Rents;
import org.evgenysav.infrastructure.dto.entity.Vehicles;
import org.evgenysav.infrastructure.dto.service.RentsService;
import org.evgenysav.infrastructure.dto.service.VehicleService;
import org.evgenysav.util.DtoConverter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/info")
public class ViewInfoServlet extends HttpServlet {

    private VehicleService vehicleService;
    private RentsService rentsService;

    @Override
    public void init() throws ServletException {
        super.init();
        ApplicationContext applicationContext = (ApplicationContext) getServletContext().getAttribute("applicationContext");
        vehicleService = applicationContext.getObject(VehicleService.class);
        rentsService = applicationContext.getObject(RentsService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        synchronized (this) {
            int id = Integer.parseInt(req.getParameter("id"));
            List<VehicleDto> vehiclesWithId = new ArrayList<>();
            for (VehicleDto vehicleDto : getVehiclesDto()) {
                if (vehicleDto.getVehicleId() == id) {
                    vehiclesWithId.add(vehicleDto);
                }
            }

            if (vehiclesWithId.isEmpty()) {
                this.getServletContext().getRequestDispatcher("/viewCars").forward(req, resp);
                return;
            } else {
                req.setAttribute("cars", vehiclesWithId);
            }

            req.setAttribute("rents", getRentsDto().stream()
                    .filter(rent -> rent.getVehicleId() == id)
                    .toList());
        }

        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/jsp/viewCarInfoJSP.jsp");
        dispatcher.forward(req, resp);
    }

    private List<VehicleDto> getVehiclesDto() {
        List<Vehicles> vehicleListFromDB = vehicleService.getAll();
        return DtoConverter.getVehiclesDto(vehicleListFromDB);
    }

    private List<RentDto> getRentsDto() {
        List<Rents> rentsListFromDB = rentsService.getAll();
        return DtoConverter.getRentsDto(rentsListFromDB);
    }
}
