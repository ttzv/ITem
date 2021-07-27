package com.ttzv.item.dao;

import com.ttzv.item.entity.Office;

import java.util.List;

public interface OfficeDao {

    List<Office> getOffices();
    void saveOffice(Office office);
    void deleteOffice(Office office);
    void updateOffice(Office office);
    List<String> officeNames();
}
