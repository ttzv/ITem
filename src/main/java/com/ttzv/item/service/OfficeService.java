package com.ttzv.item.service;

import com.ttzv.item.entity.Office;

import java.util.List;

public interface OfficeService {

    List<Office> getOffices();
    void saveOffice(Office office);
    void deleteOffice(Office office);
    void updateOffice(Office office);
    List<String> getUniqueOfficeNames();

}
