package com.ttzv.item.entity;

import java.util.List;

public class CityAccessLayer implements EntityAccessLayer<City> {
    @Override
    public List<City> sort(List<City> entities, Integer order) {
        return null;
    }

    @Override
    public List<City> sortBy(List<City> entities, Integer order, String key) {
        return null;
    }

    @Override
    public List<City> searchGlobally(String text) {
        return null;
    }

    @Override
    public boolean refreshEntityList() {
        return false;
    }

    @Override
    public List<City> getAllEntities() {
        return null;
    }

    @Override
    public City getEntity(String id) {
        return null;
    }

    @Override
    public boolean updateEntity(City entity) {
        return false;
    }

    @Override
    public boolean deleteEntity(City entity) {
        return false;
    }
}
