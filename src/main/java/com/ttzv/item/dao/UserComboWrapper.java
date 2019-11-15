package com.ttzv.item.dao;

import com.ttzv.item.entity.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserComboWrapper {

    private Map<String, City> cities;
    private Map<String, Phone> phones;
    private Map<String, UserDetail> details;

    public UserComboWrapper(List<City> cities, List<Phone> phones, List<UserDetail> details) {
        this.cities = (Map<String, City>) convertToMap(cities);
        this.phones = (Map<String, Phone>) phones;
        this.details = (Map<String, UserDetail>) details;
    }

    public City getCityOf(User user){
        return this.cities.get(user.getCity());
    }
    public Phone getPhoneOf(User user){
        return this.phones.get(user.getGUID());
    }
    public UserDetail getDetailOf(User user){
        return this.details.get(user.getGUID());
    }

    private Map<String, ? extends DynamicEntityCompatible> convertToMap(List<? extends DynamicEntityCompatible> decList){
        Map<String, ? extends DynamicEntityCompatible> returnedMap = decList.stream().collect(Collectors.toMap(DynamicEntityCompatible::getUniqueIdentifier, dec -> dec));
        return returnedMap;
    }
}
