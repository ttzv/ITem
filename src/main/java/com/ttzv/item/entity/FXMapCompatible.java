package com.ttzv.item.entity;

import java.util.Map;

public interface FXMapCompatible extends DynamicEntityCompatible {

    /**
     * Returns complete map of DynamicEntity object
     * @return Entity map of DynamicEntity object (preferably with replaced keys)
     */
    public Map getFXMap();
    public Map<String, String> getFXNameToIdPairs();

}
