package org.evgenysav.classes;

import java.util.Date;

public class Rent {
    private int id;
    private Date rentalDate;
    private double rentCost;

    public Rent() {
    }

    public Rent(Date rentalDate, double rentCost) {
        this.rentalDate = rentalDate;
        this.rentCost = rentCost;
    }

    public Rent(int id, Date rentalDate, double rentCost) {
        this.id = id;
        this.rentalDate = rentalDate;
        this.rentCost = rentCost;
    }

    public Date getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(Date rentalDate) {
        this.rentalDate = rentalDate;
    }

    public double getRentCost() {
        return rentCost;
    }

    public void setRentCost(double rentCost) {
        this.rentCost = rentCost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
