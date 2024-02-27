package org.evgenysav;

import java.time.LocalDate;

public class Rent {
    private int id;
    private LocalDate rentalDate;
    private double rentCost;

    public Rent() {
    }

    public Rent(LocalDate rentalDate, double rentCost) {
        this.rentalDate = rentalDate;
        this.rentCost = rentCost;
    }

    public Rent(int id, LocalDate rentalDate, double rentCost) {
        this.id = id;
        this.rentalDate = rentalDate;
        this.rentCost = rentCost;
    }

    public LocalDate getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(LocalDate rentalDate) {
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
