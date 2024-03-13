package org.evgenysav.infrastructure.dto;

import org.evgenysav.infrastructure.core.Context;
import org.evgenysav.infrastructure.core.annotations.Autowired;

import java.util.Map;

public class PostgreDataBase {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private Context context;

    private Map<String, String> classToSql;
    private Map<String, String> insertPatternByClass;

    private static final String SEQ_NAME = "id_seq";


}
