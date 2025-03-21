package com.bridgelabz.javaioandtdd.tdd.cabinvoicegenerator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {
    @Test
    public void testCalculateFare_NormalFare() {
        CabInvoiceGenerator  generator = new CabInvoiceGenerator();

        double fare = generator.calculateFare(2, 5);

        assertEquals(25, fare);
    }

    @Test
    public void testCalculateFare_MinimumFare() {
        CabInvoiceGenerator generator = new CabInvoiceGenerator();

        double fare = generator.calculateFare(0.1, 1);

        assertEquals(5, fare);
    }

    @Test
    public void testCalculatePremiumFare() {
        CabInvoiceGenerator generator = new CabInvoiceGenerator();


        double fare = generator.calculatePremiumFare(2, 5);

        assertEquals(40, fare);
    }

    @Test
    public void testCalculatePremiumFare_MinimumFare() {
        CabInvoiceGenerator generator = new CabInvoiceGenerator();


        double fare = generator.calculatePremiumFare(0.1, 1);

        assertEquals(20, fare, 0.001);
    }

    @Test
    public void testGenerateInvoice_NormalFare() {
        CabInvoiceGenerator generator = new CabInvoiceGenerator();


        double[][] rides = {
                {2, 10},
                {5, 20},
                {1, 3}
        };


        String expectedInvoice = "Invoice: \n"
                + "Total Number of Rides: 3\n"
                + "Ride Type: Normal\n"
                + "Total Fare: Rs. 65.0\n"
                + "Average Fare Per Ride: Rs. 21.67\n";

        assertEquals(expectedInvoice, generator.generateInvoice(rides, false));
    }

    @Test
    public void testGenerateInvoice_PremiumFare() {
        CabInvoiceGenerator generator = new CabInvoiceGenerator();

        double[][] rides = {
                {2, 10},
                {5, 20},
                {1, 3}
        };

        // Test Premium Ride Invoice Generation
        String expectedInvoice = "Invoice: \n"
                + "Total Number of Rides: 3\n"
                + "Ride Type: Premium\n"
                + "Total Fare: Rs. 115.0\n"
                + "Average Fare Per Ride: Rs. 38.33\n";

        assertEquals(expectedInvoice, generator.generateInvoice(rides, true));
    }


}
