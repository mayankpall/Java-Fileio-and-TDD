package com.bridgelabz.javaioandtdd.javaio;

import java.io.*;
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

    public String toFileFormat(){
        return firstName + "," + lastName + "," + address + "," + city + "," + state + "," + zip + "," + phoneNumber + "," + email;
    }

    public static User fromFileFormat(String data) {
        String[] values = data.split(",");
        return new User(values[0], values[1], values[2], values[3], values[4], Integer.parseInt(values[5]), Integer.parseInt(values[6]), values[7]);
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


    public void writeInFile(){
       try(BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/mayankpal/Desktop/JAVA/javaclassroom/src/com/bridgelabz/javaio/reader.txt"))) {

           for(User user: arrayList){
               writer.write(user.toFileFormat());
               writer.newLine();
           }
           System.out.println("Success");

       }
       catch (Exception e){
           System.out.println(e.getMessage());
       }
    }

    public void loadFromFile(){
        try(BufferedReader reader = new BufferedReader(new FileReader("/Users/mayankpal/Desktop/JAVA/javaclassroom/src/com/bridgelabz/javaio/reader.txt"))) {
            arrayList.clear();
            String line;

            while ((line = reader.readLine()) != null){
                arrayList.add(User.fromFileFormat(line));
            }

            System.out.println("Success");
        }
        catch (IOException io){
            System.out.println(io.getMessage());
        }
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


public class AddressBookSystem {
    public static void main(String[] args) {

                AddressBook addressBook = new AddressBook();

                // Adding contacts
                addressBook.addContact(new User("Mayank", "Pal", "123 Street", "New York", "NY", 10001, 987654321, "mayank@example.com"));
                addressBook.addContact(new User("Vibhor", "Gupta", "456 Road", "Los Angeles", "CA", 90001, 987654322, "vibhor@example.com"));
                addressBook.addContact(new User("Kanika", "Agarwal", "789 Street", "Chicago", "IL", 60601, 987654323, "kanika@example.com"));
                addressBook.addContact(new User("Arnav", "Saharan", "101 Ny", "New York", "NY", 10002, 987654324, "arnav@example.com"));

                // Display all contacts
                System.out.println("\n All Contacts:");
                addressBook.getUsers().forEach(System.out::println);

                // Sorting by Name
                System.out.println("\n Sorted by Name:");
                addressBook.getSortedUsersByName().forEach(System.out::println);

                // Sorting by City
                System.out.println("\n Sorted by City:");
                addressBook.getSortedByCity().forEach(System.out::println);

                // Sorting by State
                System.out.println("\n Sorted by State:");
                addressBook.getSortedByState().forEach(System.out::println);


                // Edit an existing contact
                addressBook.editExistingContact("Arnav", "Arnavv");


                // Display updated contacts
                System.out.println("\n Contacts after Editing & Deleting:");
                addressBook.getUsers().forEach(System.out::println);

                // Create an AddressBook System with multiple address books
                Systems system = new Systems();
                system.addAddressBook("Personal", addressBook);

                // Searching users by City
                System.out.println("\n Users in New York:");
                system.searchUserByCity("New York").forEach(System.out::println);

                // Searching users by State
                System.out.println("\n Users in California:");
                system.searchUserByState("CA").forEach(System.out::println);

                // Counting users by City
                System.out.println("\n Count of users in each City:");
                system.countUsersByCity().forEach((city, count) -> System.out.println(city + " = " + count));

                // Counting users by State
                System.out.println("\n Count of users in each State:");
                system.countUsersByState().forEach((state, count) -> System.out.println(state + " = " + count));

                addressBook.loadFromFile();


                addressBook.addContact(new User("Mayank", "Pal", "123 Street", "New York", "NY", 10001, 987654321, "mayank@example.com"));
                addressBook.addContact(new User("Vibhor", "Gupta", "456 Road", "Los Angeles", "CA", 90001, 987654322, "vibhor@example.com"));


                addressBook.writeInFile();
            }
        }

