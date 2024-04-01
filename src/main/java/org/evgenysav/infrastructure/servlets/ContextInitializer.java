package org.evgenysav.infrastructure.servlets;

import lombok.SneakyThrows;
import org.evgenysav.classes.Fixer;
import org.evgenysav.classes_.MechanicService;
import org.evgenysav.classes_.Workroom;
import org.evgenysav.infrastructure.core.impl.ApplicationContext;
import org.evgenysav.infrastructure.dto.ConnectionFactory;
import org.evgenysav.infrastructure.dto.service.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class ContextInitializer implements ServletContextListener {

    private static final String TRUNCATE_SQL = """
            truncate TABLE vehicles CASCADE; 
            truncate TABLE combustion_engine CASCADE;
            truncate TABLE electrical_engine CASCADE;
            truncate TABLE order_entries CASCADE;
            truncate TABLE orders CASCADE;
            truncate TABLE rents CASCADE;
            truncate TABLE types CASCADE;""";

    @SneakyThrows
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Map<Class<?>, Class<?>> interfaceToImplementation = new HashMap<>();
        interfaceToImplementation.put(Fixer.class, MechanicService.class);
        ApplicationContext context = new ApplicationContext("org.evgenysav", interfaceToImplementation);
        ConnectionFactory connectionFactory = context.getObject(ConnectionFactory.class);
        try (Connection connection = connectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(TRUNCATE_SQL);
        }

        initAndInsertValuesInDB(context);
        sce.getServletContext().setAttribute("applicationContext", context);
    }

    private void initAndInsertValuesInDB(ApplicationContext applicationContext) {
        applicationContext.getObject(VehicleService.class);
        applicationContext.getObject(ElectricalEngineService.class);
        applicationContext.getObject(CombustionEngineService.class);
        applicationContext.getObject(TypesService.class);
        applicationContext.getObject(RentsService.class);
        applicationContext.getObject(ColorService.class);
        applicationContext.getObject(Workroom.class);
    }
}
