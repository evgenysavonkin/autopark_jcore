package org.evgenysav.infrastructure.dto;

import java.sql.Connection;

public interface ConnectionFactory {
    Connection getConnection();
}
