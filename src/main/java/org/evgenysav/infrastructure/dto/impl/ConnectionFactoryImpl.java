package org.evgenysav.infrastructure.dto.impl;

import lombok.SneakyThrows;
import org.evgenysav.infrastructure.core.annotations.InitMethod;
import org.evgenysav.infrastructure.core.annotations.Property;
import org.evgenysav.infrastructure.dto.ConnectionFactory;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactoryImpl implements ConnectionFactory {

    @Property("url")
    private String url;

    @Property("username")
    private String username;

    @Property("password")
    private String password;

    private Connection connection;

    @SneakyThrows
    @InitMethod
    public void initConnection() {
        connection = DriverManager.getConnection(url, username, password);
    }


    @Override
    public Connection getConnection() {
        return connection;
    }
}
