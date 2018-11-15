package file;

public enum Vals {
    //default
    SMTP_HOST("smtpHost"),
    SMTP_PORT("smtpStartTLS"),
    SMTP_TLS("smtpPort"),
    //custom
    LIST_MSG("listMsg"),
    LOCATION_MSG("locMsg"),
    SMTP_LOGIN("smtpLogin"),
    SMTP_PASS("smtpPass");


    private String name;

    Vals(String name){
        this.name = name;
    }

}
