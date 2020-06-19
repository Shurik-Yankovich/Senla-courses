package bookstore.model;

public class Customer {

    private String fullName;
    private String phoneNumber;
    private String address;

    public Customer(String fullName, String phoneNumber, String address) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Customer: '" + fullName + '\'' +
                ", phoneNumber: " + phoneNumber +
                ", address: '" + address + "'\n";
    }
}