package com.bridgelabz.javaioandtdd.annotations;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

class User{
    private  String firstName;
    private  String lastName;
    private String address;
    private String city;
    private String state;
    private int zip;
    private int phoneNumber;
    private String email;

    User(String firstName, String lastName, String address, String city, String state, int zip, int phoneNumber, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public int getZip() {
        return zip;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getState() {
        return state;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || obj.getClass() != getClass()) return false;
        User user = (User) obj;
        return firstName.equals(user.firstName) && lastName.equals(user.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastName.toLowerCase() , firstName.toLowerCase());
    }

    @Override
    public String toString() {
        return
                "firstName='" + firstName + ' ' +
                ", lastName='" + lastName + ' ' +
                ", address='" + address + ' ' +
                ", city='" + city + ' ' +
                ", state='" + state + ' ' +
                ", zip=" + zip +
                ", phoneNumber=" + phoneNumber +
                ", email='" + email + ' ';

    }

}


class AddressBook{
    ArrayList<User>  arrayList = new ArrayList<>();


    void addContact(User newUser){

        boolean isDuplicate = arrayList.stream().anyMatch(user -> user.equals(newUser));

        if(isDuplicate){
            System.out.println("Already Exist");
        }
        else{
            arrayList.add(newUser);
            System.out.println("Success. Added you user to AddressBook");
        }


    }

    void editExistingContact(String existing , String newName){
        for(int iterator = 0; iterator < arrayList.size(); iterator++){
            if(arrayList.get(iterator) .equals(existing)){
                arrayList.get(iterator).setFirstName(newName);
            }
        }

        System.out.println("Successfully edited the name of your contact");
    }

    void deleteUser(String userName){
        for(User iterator : arrayList){
            if(iterator.equals(userName)){
                arrayList.remove(iterator);
                break;
            }
        }
        System.out.println("Successfully Deleted the name of your contact");

    }


    void addMultipleContacts(ArrayList<User> newContacts){
        arrayList.addAll(newContacts);
        System.out.println("Successfully Added the contacts to AddressBook");
    }

    public List<User> getUsers(){
        return arrayList;
    }

    public List<User> getSortedUsersByName() {
        return arrayList.stream()
                .sorted(Comparator.comparing(User::getFirstName)
                        .thenComparing(User::getLastName))
                .collect(Collectors.toList());
    }

    public List<User> getSortedByCity(){
        return arrayList.stream().sorted(Comparator.comparing(User::getCity)).collect(Collectors.toList());
    }

    public List<User> getSortedByState(){
        return arrayList.stream().sorted(Comparator.comparing(User::getState)).collect(Collectors.toList());
    }


}


class Systems{
    Map<String , AddressBook> systemsMap = new HashMap<>();

//    ArrayList<AddressBook> systemArray = new ArrayList<>();
//    void addAddressBooks(ArrayList<AddressBook> newAddressBooks){
//        systemArray.addAll(systemArray);
//    }

    void addAddressBook(String nameOfAddressBook, AddressBook newAddressBook){
        if(systemsMap.containsKey(nameOfAddressBook)){
            System.out.println("Already exist");
        }
        else {
            systemsMap.put(nameOfAddressBook, newAddressBook);
        }

    }

    public Map<String, List<User>> getUserByCity(){
        return systemsMap.values().stream().flatMap(ab->ab.getUsers().stream())
//                .filter(user -> user.getCity().equalsIgnoreCase(city)) //uc 8 with parameters in methods
                .collect(Collectors.groupingBy(user -> user.getCity()));
    }

    public Map<String, List<User>> getUserByState(){
        return systemsMap.values().stream().flatMap(ab->ab.getUsers().stream())
//                .filter(user -> user.getState().equalsIgnoreCase(state)) //uc 8
                .collect(Collectors.groupingBy(User::getState));
    }


    public List<User> searchUserByCity(String city){
        return getUserByCity().getOrDefault(city,new ArrayList<>());
    }

    public List<User> searchUserByState(String state){
        return getUserByState().getOrDefault(state,new ArrayList<>());
    }

    public Map<String, Long> countUsersByCity() {
        return systemsMap.values().stream()
                .flatMap(ab -> ab.getUsers().stream())
                .collect(Collectors.groupingBy(user -> user.getCity(), Collectors.counting()));
    }

    public Map<String, Long> countUsersByState() {
        return systemsMap.values().stream()
                .flatMap(ab -> ab.getUsers().stream())
                .collect(Collectors.groupingBy(User::getState, Collectors.counting()));
    }


}
class CSVHandler {
    public static void writeToCSV(String filePath, List<User> contacts) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {

            String[] header = {"First Name", "Last Name", "Address", "City", "State", "Zip", "Phone Number", "Email"};
            writer.writeNext(header);

            for (User user : contacts) {
                String[] data = {
                        user.getFirstName(), user.getLastName(), user.getAddress(), user.getCity(),
                        user.getState(), String.valueOf(user.getZip()), String.valueOf(user.getPhoneNumber()), user.getEmail()
                };
                writer.writeNext(data);
            }
            System.out.println("Data successfully written to CSV file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<User> readFromCSV(String filePath) {
        List<User> users = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> records = reader.readAll();
            records.remove(0);
            for (String[] record : records) {

                User user = new User(record[0], record[1], record[2], record[3], record[4],
                        Integer.parseInt(record[5]), Integer.parseInt(record[6]), record[7]);
                users.add(user);
            }
            System.out.println("Data successfully read from CSV file.");
        } catch (IOException | NumberFormatException | CsvException e) {
            e.printStackTrace();
        }
        return users;
    }
}
class JSONHandler {
    public static void writeToJson(String filePath, List<User> contacts) {
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation() // Optional, if used with @Expose
                .enableComplexMapKeySerialization() // Enables complex key serialization
                .create();

        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(contacts, writer); // Writes the List<User> as JSON
            System.out.println("Data successfully written to JSON file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<User> readFromJson(String filePath) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        try (FileReader reader = new FileReader(filePath)) {
            Type listType = new TypeToken<List<User>>() {}.getType();
            return gson.fromJson(reader, listType); // Gson will use public getters
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}

public class AddressBookSystem {
    public static void main(String[] args) {
        // Define file paths
        String csvFilePath = "/Users/mayankpal/Desktop/JAVA/javaclassroom/src/com/bridgelabz/annotations/contacts.csv";
        String jsonFilePath = "/Users/mayankpal/Desktop/JAVA/javaclassroom/src/com/bridgelabz/annotations/contactss.json";

        // Create a list of users
        List<User> users = new ArrayList<>();
        users.add(new User("Mayank", "Pal", "123 Street", "New York", "NY", 10001, 1234567, "mayank@example.com"));
        users.add(new User("Saksham", "Pal", "456 Avenue", "Los Angeles", "CA", 90001, 9876543, "saksham@example.com"));
        users.add(new User("Samarth", "Pal", "789 Boulevard", "Chicago", "IL", 60601, 55566677, "samarth@example.com"));

        try {
//             Perform CSV operations
            System.out.println("\n---- CSV Operations ----");
            CSVHandler.writeToCSV(csvFilePath, users);
            List<User> usersFromCSV = CSVHandler.readFromCSV(csvFilePath);
            System.out.println("\nUsers Read from CSV:");
            for (User user : usersFromCSV) {
                System.out.println(user);
            }

            // Perform JSON operations
            System.out.println("\n---- JSON Operations ----");

            JSONHandler.writeToJson(jsonFilePath, users);
            List<User> usersFromJSON = JSONHandler.readFromJson(jsonFilePath);
            System.out.println("\nUsers Read from JSON:");
            for (User user : usersFromJSON) {
                System.out.println(user);
            }
        } catch (Exception e) {
            System.out.println("An error occurred during file operations: " + e.getMessage());
        }
    }
}

