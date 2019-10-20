package com.ttzv.itemBootstrap;

import ttzv.propsicl.Propsicl;

public class Config extends Propsicl {

    public static final String CONFIG_URL = "configURL";

    @Override
    public void defaultPropsVals() {
        defPropSet(CONFIG_URL, "ftp://localhost/item/config/config.xml");
    }

    public static Config getInstance(){
        return CfgHolder.INSTANCE;
    }

    private static class CfgHolder{
        private static final Config INSTANCE = new Config();
    }
}
