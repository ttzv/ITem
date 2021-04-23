package com.ttzv.item.properties;

public class Update4jCfg extends Propsicl {

    public static String UPDATE4J_BASE_URI = "baseURI";
    public static String UPDATE4J_BASE_PATH = "basePath";

    @Override
    public void defaultPropsVals() {
        defPropSet(UPDATE4J_BASE_URI, "ftp://10.0.1.117/item/files/");
        defPropSet(UPDATE4J_BASE_PATH,"${user.dir}/business/");
    }

    public static Update4jCfg getInstance(){
        return CfgU4J.INSTANCE;
    }

    private static class CfgU4J {
        public static final Update4jCfg INSTANCE = new Update4jCfg();
    }
}
