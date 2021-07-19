package com.ttzv.item.service;

import com.ttzv.item.dao.OfficeDAO;
import com.ttzv.item.dao.OfficeDaoImpl;
import com.ttzv.item.entity.Office;

import java.util.List;

public class OfficeServiceImpl implements OfficeService{

    private OfficeDAO officeDAO;

    public OfficeServiceImpl(){
        officeDAO = new OfficeDaoImpl();
    }

    @Override
    public List<Office> getOffices() {
        return officeDAO.getOffices();
    }

    @Override
    public void saveOffice(Office office) {
        officeDAO.saveOffice(office);
    }

    @Override
    public void deleteOffice(Office office) {
        officeDAO.deleteOffice(office);
    }
}
