package com.ttzv.item.dao;

import com.ttzv.item.entity.*;

import javax.naming.NamingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserComboWrapper {

    private User currentUser;

    private Map<String, City> cities;
    private Map<String, Phone> phones;
    private Map<String, UserDetail> details;

    private EntityDAO<City> cityEntityDAO;
    private EntityDAO<Phone> phoneEntityDAO;
    private EntityDAO<UserDetail> userDetailEntityDAO;

    public UserComboWrapper(EntityDAO<City> cityEntityDAO, EntityDAO<Phone> phoneEntityDAO, EntityDAO<UserDetail> userDetailEntityDAO) throws SQLException, IOException, NamingException, GeneralSecurityException {
        List<City> cities = new ArrayList<>();
        List<Phone> phones = new ArrayList<>();
        List<UserDetail> details = new ArrayList<>();

        if (cityEntityDAO != null) {
            this.cityEntityDAO = cityEntityDAO;
            cities = cityEntityDAO.getAllEntities();
        }
        if (phoneEntityDAO != null) {
            this.phoneEntityDAO = phoneEntityDAO;
            phones = phoneEntityDAO.getAllEntities();
        }
        if (userDetailEntityDAO != null){
            this.userDetailEntityDAO = userDetailEntityDAO;
            details = userDetailEntityDAO.getAllEntities();
        }

        this.cities = (Map<String, City>) convertToMap(cities);
        this.phones = (Map<String, Phone>) convertToMap(phones);
        this.details = (Map<String, UserDetail>) convertToMap(details);
    }

    public City getCityOf(User user){
        if(this.cities.size() <= 0){
            return null;
        }
        City city = this.cities.get(user.getCity());
        if(city != null)
            return city;
        else
            return new City(user.getCity());
    }
    public Phone getPhoneOf(User user){
        if(this.phones.size() <= 0){
            return null;
        }
        Phone phone = this.phones.get(user.getGUID());
        if(phone != null)
            return phone;
        else
            return new Phone(user.getGUID());
    }
    public UserDetail getDetailOf(User user){
        if(this.details.size() <= 0){
            return null;
        }
        UserDetail userDetail = this.details.get(user.getGUID());
        if(userDetail != null)
            return userDetail;
        else
            return new UserDetail(user.getGUID());
    }

    private Map<String, ? extends DynamicEntityCompatible> convertToMap(List<? extends DynamicEntityCompatible> decList){
        Map<String, ? extends DynamicEntityCompatible> returnedMap = decList.stream().collect(Collectors.toMap(DynamicEntityCompatible::getUniqueIdentifier, dec -> dec));
        return returnedMap;
    }

    public void updateCity(City city) throws IOException, SQLException {
        if(this.cityEntityDAO != null) {
            this.cityEntityDAO.updateEntity(city);
        } else {
            System.err.println("City DAO not set");
        }
    }

    public void updatePhone(Phone phone) throws IOException, SQLException {
        if(this.phoneEntityDAO != null) {
            this.phoneEntityDAO.updateEntity(phone);
        } else {
            System.err.println("Phone DAO not set");
        }
    }

    public void updateUserDetail(UserDetail userDetail) throws IOException, SQLException {
        if(this.userDetailEntityDAO != null) {
            this.userDetailEntityDAO.updateEntity(userDetail);
        } else {
            System.err.println("UserDetail DAO no set");
        }
    }

    public void setCityEntityDAO(EntityDAO<City> cityEntityDAO) {
        this.cityEntityDAO = cityEntityDAO;
    }

    public void setPhoneEntityDAO(EntityDAO<Phone> phoneEntityDAO) {
        this.phoneEntityDAO = phoneEntityDAO;
    }

    public void setUserDetailEntityDAO(EntityDAO<UserDetail> userDetailEntityDAO) {
        this.userDetailEntityDAO = userDetailEntityDAO;
    }

    public void refresh() throws SQLException, IOException, NamingException, GeneralSecurityException {
        this.details = (Map<String, UserDetail>) convertToMap(this.userDetailEntityDAO.getAllEntities());
        this.cities = (Map<String, City>) convertToMap(cityEntityDAO.getAllEntities());
        this.phones = (Map<String, Phone>) convertToMap(phoneEntityDAO.getAllEntities());
    }

}
