package lld.resturantrating.customer.service;

import lld.resturantrating.customer.model.Customer;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    Map<String, Customer> customerMap;

    public UserService() {
        this.customerMap = new HashMap<>();
    }

    public Customer  add(Customer customer){
        customerMap.put(customer.getMobileNo(),customer);
        return customer;
    }
}
