package com.bridgelabz.javaioandtdd.tdd.cabinvoicegenerator;

public class CabInvoiceGenerator {

    private static final double NORMAL_RATE_PER_KM = 10;
    private static final double NORMAL_RATE_PER_MIN = 1;
    private static final double PREMIUM_RATE_PER_KM = 15;
    private static final double PREMIUM_RATE_PER_MIN = 2;
    private static final double MINIMUM_FARE = 5;
    private static final double PREMIUM_MINIMUM_FARE = 20;


    public double calculateFare(double distance, double time) {
        double fare = (distance * NORMAL_RATE_PER_KM) + (time * NORMAL_RATE_PER_MIN);
        return fare < MINIMUM_FARE ? MINIMUM_FARE : fare;
    }


    public double calculatePremiumFare(double distance, double time) {
        double fare = (distance * PREMIUM_RATE_PER_KM) + (time * PREMIUM_RATE_PER_MIN);
        return fare < PREMIUM_MINIMUM_FARE ? PREMIUM_MINIMUM_FARE : fare;
    }


    public double calculateTotalFare(double[][] rides, boolean isPremium) {
        double totalFare = 0;
        for (double[] ride : rides) {
            if (isPremium) {
                totalFare += calculatePremiumFare(ride[0], ride[1]);
            } else {
                totalFare += calculateFare(ride[0], ride[1]);
            }
        }
        return totalFare;
    }


    public String generateInvoice(double[][] rides, boolean isPremium) {
        double totalFare = calculateTotalFare(rides, isPremium);
        int totalRides = rides.length;
        double averageFare = totalFare / totalRides;

        String rideType = isPremium ? "Premium" : "Normal";

        return "Invoice: \n"
                + "Total Number of Rides: " + totalRides + "\n"
                + "Ride Type: " + rideType + "\n"
                + "Total Fare: Rs. " + totalFare + "\n"
                + "Average Fare Per Ride: Rs. " + averageFare + "\n";
    }


    public static void main(String[] args) {
        CabInvoiceGenerator generator = new CabInvoiceGenerator();


        double[][] rides = {
                {2, 10},  
                {5, 20},  
                {1, 3}    
        };


        System.out.println(generator.generateInvoice(rides, false));


        System.out.println(generator.generateInvoice(rides, true));
    }
}
