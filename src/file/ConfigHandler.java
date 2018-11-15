package file;

import sender.Sender;

import java.io.*;
import java.util.Properties;

public class ConfigHandler {

    private File cfgFile;
    private File defCfgFile;

    private Properties properties;
    private Properties defProps;
    private Sender sender;

    private String fileSeparator;
    private String cfgFilePath;
    private String defCfgFilePath;


    public ConfigHandler(Sender sender){
        this.sender = sender;

        this.fileSeparator = System.getProperty("file.separator");
        cfgFilePath = "cfg" + fileSeparator + "config.properties";
        defCfgFilePath = "cfg" + fileSeparator + "default.properties";

        cfgFile = new File(cfgFilePath);
        defCfgFile = new File(defCfgFilePath);

        this.defProps = new Properties();
        this.properties = new Properties();
    }


    //if cfg exist == true
    private void loadCfgFile(File file, Properties properties) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        properties.load(fis);
        fis.close();
    }

    private void saveCfgFile(File file, Properties properties) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        properties.store(fos, "");
        fos.close();
    }

    private void useProperties(Properties properties) {
        if (properties.containsKey(Vals.SMTP_PORT.toString()))
        sender.setSmtpPort(properties.get(Vals.SMTP_PORT.toString()).toString());

        if (properties.containsKey(Vals.SMTP_TLS.toString()))
        sender.setSmtpStartTLS(properties.get(Vals.SMTP_TLS.toString()).toString());

        if (properties.containsKey(Vals.SMTP_HOST.toString()))
        sender.setSmtpHost(properties.get(Vals.SMTP_HOST.toString()).toString());

        if (properties.containsKey(Vals.SMTP_LOGIN.toString()))
        sender.setSenderAddress(properties.get(Vals.SMTP_LOGIN.toString()).toString());

        if (properties.containsKey(Vals.SMTP_PASS.toString()))
        sender.setSenderPassword(properties.get(Vals.SMTP_PASS.toString()).toString());
    }

    /**
     * configure default properties object
     */
    private void createDefaultProps() throws IOException {
        defProps.put(Vals.SMTP_HOST.toString(), "smtp.gmail.com");
        defProps.put(Vals.SMTP_TLS.toString(), "true");
        defProps.put(Vals.SMTP_PORT.toString(), "587");
        saveCfgFile(defCfgFile, defProps);
    }

    public void firstRun() throws IOException {
        if((!defCfgFile.exists() || defCfgFile.length() == 0) && (!cfgFile.exists() || cfgFile.length() == 0)){
            defCfgFile.getParentFile().mkdir();
            defCfgFile.createNewFile();
            cfgFile.createNewFile();
            createDefaultProps();
            System.out.println("created default");
        }

        if(cfgFile.exists()){
            if(cfgFile.length() == 0){
                System.out.println(cfgFile.getName() + "is empty, loading defaults");
            } else {
               loadCfgFile(cfgFile, properties);
            }
        }
        if(!properties.isEmpty()){
            useProperties(properties);
        } else if(defCfgFile.exists()){
            if(defCfgFile.length() != 0) {
                loadCfgFile(defCfgFile, defProps);
                useProperties(defProps);
            } else {
                System.out.println(defCfgFile.getName() + "is empty, loading defaults");
            }
        }
    }

    public void saveCustomProperties() throws IOException {
        saveCfgFile(cfgFile, properties);
    }

    public Properties getProperties() {
        return properties;
    }

}
