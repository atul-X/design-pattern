package lld.resturantrating.customer.model;

public class Customer {
    private int id;
    private String name;
    private Gender gender;
    private String mobileNo;
    private String pinCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Customer{" + "name='" + name + '\'' + ", gender=" + gender + ", mobileNo='" + mobileNo + '\'' + ", pinCode='" + pinCode + '\'' + '}';
    }
}
