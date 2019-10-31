package com.ttzv.item.entity;

import java.util.List;

public class UserAccessLayer implements EntityAccessLayer<User> {
    @Override
    public List<User> sort(List<User> entities, String order) {
        return null;
    }

    @Override
    public List<User> sortBy(List<User> entities, String order, String key) {
        return null;
    }

    @Override
    public List<User> searchGlobally(String text) {
        return null;
    }

    @Override
    public boolean refreshEntityList() {
        return false;
    }

    @Override
    public List<User> getAllEntities() {
        return null;
    }

    @Override
    public User getEntity(String id) {
        return null;
    }

    @Override
    public boolean updateEntity(User entity) {
        return false;
    }

    @Override
    public boolean deleteEntity(User entity) {
        return false;
    }
}
