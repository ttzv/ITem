package com.ttzv.itemBootstrap;

import ttzv.propsicl.Propsicl;

public class BootConfig extends Propsicl {

    public static final String CONFIG_URL = "configURL";

    @Override
    public void defaultPropsVals() {
        defPropSet(CONFIG_URL, "ftp://192.168.1.113/item/config/config.xml");
    }

    public static BootConfig getInstance(){
        return CfgHolder.INSTANCE;
    }

    private static class CfgHolder{
        private static final BootConfig INSTANCE = new BootConfig();
    }
}
