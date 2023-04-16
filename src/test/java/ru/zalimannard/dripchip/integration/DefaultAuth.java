package ru.zalimannard.dripchip.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultAuth {

    @Autowired
    private AccountToAuthConverter accountToAuthConverter;

    @Value("${application.init.accounts.admin.email}")
    private String adminEmail;
    @Value("${application.init.accounts.admin.password}")
    private String adminPassword;

    public String adminAuth() {
        return accountToAuthConverter.convert(adminEmail, adminPassword);
    }

}
