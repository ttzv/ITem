package com.ttzv.item.entity;

import java.util.List;

public class PhoneAccessLayer implements EntityAccessLayer<Phone> {
    @Override
    public List<Phone> sort(List<Phone> entities, String order) {
        return null;
    }

    @Override
    public List<Phone> sortBy(List<Phone> entities, String order, String key) {
        return null;
    }

    @Override
    public List<Phone> searchGlobally(String text) {
        return null;
    }

    @Override
    public boolean refreshEntityList() {
        return false;
    }

    @Override
    public List<Phone> getAllEntities() {
        return null;
    }

    @Override
    public Phone getEntity(String id) {
        return null;
    }

    @Override
    public boolean updateEntity(Phone entity) {
        return false;
    }

    @Override
    public boolean deleteEntity(Phone entity) {
        return false;
    }
}
