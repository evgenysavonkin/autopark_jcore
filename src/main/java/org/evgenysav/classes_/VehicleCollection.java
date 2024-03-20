package org.evgenysav.classes_;

import org.evgenysav.classes.Rent;
import org.evgenysav.classes.Vehicle;
import org.evgenysav.classes.VehicleType;
import org.evgenysav.infrastructure.core.annotations.Autowired;
import org.evgenysav.infrastructure.core.annotations.InitMethod;

import java.util.ArrayList;
import java.util.List;

public class VehicleCollection {

    private List<VehicleType> vehicleTypes = new ArrayList<>();
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Rent> rents = new ArrayList<>();

    @Autowired
    private ParserVehicleFromFile parser;

    public VehicleCollection() {

    }

    @InitMethod
    public void init() {
        vehicleTypes = parser.loadTypes("types");
        rents = parser.loadRents("rents");
        vehicles = parser.loadVehicles("vehicles", vehicleTypes, rents);
    }


    public List<VehicleType> getVehicleTypes() {
        return vehicleTypes;
    }

    public void setVehicleTypes(List<VehicleType> vehicleTypes) {
        this.vehicleTypes = vehicleTypes;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public List<Rent> getRents() {
        return rents;
    }

    public void setRents(List<Rent> rents) {
        this.rents = rents;
    }

    public ParserVehicleFromFile getParser() {
        return parser;
    }

    public void setParser(ParserVehicleFromFile parser) {
        this.parser = parser;
    }
}
