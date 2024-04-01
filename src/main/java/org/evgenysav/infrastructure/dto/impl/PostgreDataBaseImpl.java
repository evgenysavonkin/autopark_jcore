package org.evgenysav.infrastructure.dto.impl;

import lombok.SneakyThrows;
import org.evgenysav.infrastructure.core.Context;
import org.evgenysav.infrastructure.core.Scanner;
import org.evgenysav.infrastructure.core.annotations.Autowired;
import org.evgenysav.infrastructure.core.annotations.InitMethod;
import org.evgenysav.infrastructure.dto.ConnectionFactory;
import org.evgenysav.infrastructure.dto.DataBaseService;
import org.evgenysav.infrastructure.dto.annotations.Column;
import org.evgenysav.infrastructure.dto.annotations.FK;
import org.evgenysav.infrastructure.dto.annotations.ID;
import org.evgenysav.infrastructure.dto.annotations.Table;
import org.evgenysav.infrastructure.dto.enums.SqlFieldType;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class PostgreDataBaseImpl implements DataBaseService {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private Context context;

    private static final StringBuilder stringBuilder = new StringBuilder();

    private Map<String, String> classToSql;
    private Map<String, String> insertPatternByClass;
    private static final String SEQ_NAME = "id_seq";

    private static final String CHECK_SEQ_SQL_PATTERN =
            "SELECT EXISTS (\n" +
                    "SELECT FROM information_schema.sequences \n" +
                    "WHERE sequence_schema = 'public'\n" +
                    "AND sequence_name = '%s'\n" +
                    ");";

    private static final String CREATE_ID_SEQ_PATTERN =
            "CREATE SEQUENCE %S\n" +
                    "INCREMENT 1\n" +
                    "START 1;";

    private static final String CHECK_TABLE_SQL_PATTERN =
            "SELECT EXISTS (\n" +
                    "SELECT FROM information_schema.tables \n" +
                    "WHERE table_schema = 'public'\n" +
                    "AND table_name = '%s'\n" +
                    ");";

    private static final String CREATE_TABLE_SQL_PATTERN =
            "CREATE TABLE %s (\n" +
                    "%s integer PRIMARY KEY DEFAULT nextval('%s'), " +
                    "%s\n);";

    private static final String INSERT_SQL_PATTERN =
            "INSERT INTO %s(%s)\n" +
                    "VALUES (%s)\n" +
                    "RETURNING %s ;";

    private static final String DELETE_SQL_PATTERN =
            "DELETE FROM %s WHERE %s = %d";

    private static final String FOREIGN_KEY_PATTERN = "FOREIGN KEY (%s) REFERENCES %s (%s)\n " +
            "ON DELETE CASCADE\n ON UPDATE CASCADE);";

    private Map<String, String> insertByClassPattern;

    private Set<Class<?>> entitiesAnnotatedWithTable;

    {
        classToSql = new HashMap<>();
        insertPatternByClass = new HashMap<>();
        insertByClassPattern = new HashMap<>();
    }

    @SneakyThrows
    public void remove(Object obj, String fieldName, Long fieldId) {
        String tableName = obj.getClass().getAnnotation(Table.class).name();
        String SQL = String.format(DELETE_SQL_PATTERN, tableName, fieldName, fieldId);
        try (Connection connection = connectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(SQL);
        }
    }

    @SneakyThrows
    public Long save(Object obj) {
        String tableName = obj.getClass().getAnnotation(Table.class).name();

        String idFieldName = "";
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(ID.class)) {
                idFieldName = field.getName();
                break;
            }
        }

        Object[] values = getColumnValues(obj);
        String SQL = String.format(
                insertByClassPattern.get(tableName), values);

        Long id;
        try (Connection connection = connectionFactory.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL)) {
            if (resultSet.next()) {
                id = resultSet.getLong(idFieldName);
            } else {
                throw new RuntimeException("Id of " + obj + " wasn't received");
            }
        }

        Field idField = obj.getClass().getDeclaredField(idFieldName);
        idField.setAccessible(true);
        idField.set(obj, id);

        return id;
    }

    @SneakyThrows
    public <T> Optional<T> get(Long id, Class<T> clazz) {
        String idField = getIdField(clazz);
        String SQL = "SELECT * FROM " + clazz.getAnnotation(Table.class).name() +
                " WHERE " + idField + " = " + id;

        try (Connection connection = connectionFactory.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL)) {
            T instance = makeObject(resultSet, clazz, idField);
            return Optional.of(instance);
        }
    }

    @SneakyThrows
    public <T> List<T> getAll(Class<T> clazz) {
        String idField = getIdField(clazz);
        String tableName = getTableNameFromEntity(clazz);

        String SQL = "SELECT * FROM " + tableName;
        List<T> resultList = new ArrayList<>();

        try (Connection connection = connectionFactory.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL)) {
            while (resultSet.next()) {
                resultList.add(makeObject(resultSet, clazz, idField));
            }
        }
        return resultList;
    }

    private <T> String getTableNameFromEntity(Class<T> clazz) {
        return clazz.getAnnotation(Table.class).name();
    }

    private <T> String getIdField(Class<T> clazz) {
        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new RuntimeException("Class isn't annotated with @Table");
        }

        String idField = "";
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ID.class)) {
                idField = field.getName();
                break;
            }
        }

        return idField;
    }

    @SneakyThrows
    private <T> T makeObject(ResultSet resultSet, Class<T> clazz, String idField) {
        T instance = clazz.newInstance();
        Field instanceIdField = instance.getClass().getDeclaredField(idField);
        instanceIdField.setAccessible(true);
        instanceIdField.set(instance, resultSet.getLong(1));

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                String columnFieldName = field.getAnnotation(Column.class).name();
                field.setAccessible(true);

                if (field.getType().equals(Long.class)) {
                    field.set(instance, resultSet.getLong(columnFieldName));
                    continue;
                }
                if (field.getType().equals(Integer.class)) {
                    field.set(instance, resultSet.getInt(columnFieldName));
                    continue;
                }
                if (field.getType().equals(Double.class)) {
                    field.set(instance, resultSet.getDouble(columnFieldName));
                    continue;
                }
                if (field.getType().equals(Date.class)) {
                    field.set(instance, resultSet.getDate(columnFieldName));
                    continue;
                }

                field.set(instance, resultSet.getObject(columnFieldName));
            }
        }

        return instance;
    }


    @SneakyThrows
    private Object[] getColumnValues(Object o) {
        List<Object> columnValues = new ArrayList<>();
        for (Field field : o.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                field.setAccessible(true);
                columnValues.add(field.get(o));
            }
        }

        return columnValues.toArray();
    }


    @InitMethod
    private void init() {
        Arrays.stream(SqlFieldType.values())
                .forEach(entry -> classToSql.put(entry.getType().getCanonicalName(), entry.getSqlType()));

        Arrays.stream(SqlFieldType.values())
                .forEach(entry -> insertPatternByClass.put(entry.getType().getCanonicalName(), entry.getInsertPattern()));

        if (!isSequenceExists()) {
            createSequence();
        }

        Set<Class<?>> entities = getClassesWithTableAnnotation();
        validateEntities(entities);

        for (Class<?> entity : entities) {
            if (!isTableExistsInDB(entity)) {
                createTableInDBIfNotExists(entity);
            }
        }

        entities.forEach(this::initInsertByClassPattern);
    }

    private void initInsertByClassPattern(Class<?> clazz) {
        String tableName = clazz.getAnnotation(Table.class).name();
        String idFieldName = null;
        List<Field> columnFields = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ID.class)) {
                idFieldName = field.getName();
            }
            if (field.isAnnotationPresent(Column.class)) {
                columnFields.add(field);
            }
        }

        stringBuilder.setLength(0);
        StringBuilder valuesBuilder = new StringBuilder();
        int counter = 0;
        for (Field f : columnFields) {
            stringBuilder.append(f.getAnnotation(Column.class).name());
            valuesBuilder.append(insertPatternByClass.get(f.getType().getCanonicalName()));

            if (counter == columnFields.size() - 1) {
                break;
            } else {
                stringBuilder.append(",");
                valuesBuilder.append(",");
            }

            counter++;
        }

        String insertFields = stringBuilder.toString();
        String values = valuesBuilder.toString();
        stringBuilder.setLength(0);

        String SQL = String.format(INSERT_SQL_PATTERN, tableName, insertFields, values, idFieldName);
        insertByClassPattern.put(tableName, SQL);
    }

    @SneakyThrows
    private void createTableInDBIfNotExists(Class<?> entity) {
        stringBuilder.setLength(0);
        Table tableAnnotation = entity.getAnnotation(Table.class);
        String tableName = tableAnnotation.name();
        String idField = null;
        Field[] declaredFields = entity.getDeclaredFields();

        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(ID.class)) {
                idField = field.getName();
                break;
            }
        }

        Field FKField = null;
        List<Field> columnFields = Arrays.stream(declaredFields)
                .filter(f -> f.isAnnotationPresent(Column.class))
                .toList();

        int counter = 0;
        String foreignKeyColumnName = null;
        for (Field field : columnFields) {
            if (field.isAnnotationPresent(FK.class)) {
                FKField = field;
                foreignKeyColumnName = field.getAnnotation(Column.class).name();
            }

            Column columnAnnotation = field.getAnnotation(Column.class);
            String fieldName = columnAnnotation.name();
            stringBuilder.append(fieldName).append(" ");
            String fieldType = classToSql.get(field.getType().getCanonicalName());
            stringBuilder.append(fieldType).append(" ");

            if (!columnAnnotation.nullable()) {
                stringBuilder.append(" NOT NULL");
            }
            if (columnAnnotation.unique()) {
                stringBuilder.append(" UNIQUE");
            }
            if (counter != columnFields.size() - 1) {
                stringBuilder.append(",\n");
            }

            counter++;
        }


        String fields = stringBuilder.toString();
        String SQL = String.format(CREATE_TABLE_SQL_PATTERN, tableName, idField, SEQ_NAME, fields);
        stringBuilder.setLength(0);

        if (FKField != null) {
            FK fkAnnotation = FKField.getAnnotation(FK.class);
            String tableReferenceName = fkAnnotation.tableName();
            String tableReferenceId = fkAnnotation.id();
            Class<?> referenceClass = null;
            if (entitiesAnnotatedWithTable != null) {
                for (Class<?> entityWithTable : entitiesAnnotatedWithTable) {
                    if (entityWithTable.getAnnotation(Table.class).name().equals(tableReferenceName)) {
                        referenceClass = entityWithTable;
                        break;
                    }
                }
            }

            if (referenceClass != null) {
                if (!isTableExistsInDB(referenceClass)) {
                    createTableInDBIfNotExists(referenceClass);
                }
            }

            String buffSQL = SQL;
            buffSQL = buffSQL.substring(0, buffSQL.length() - 3);
            buffSQL += ",\n";
            String foreignKeyToAdd = String.format(FOREIGN_KEY_PATTERN, foreignKeyColumnName, tableReferenceName, tableReferenceId);
            SQL = buffSQL + foreignKeyToAdd;
        }

        try (Connection connection = connectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(SQL);
        }
    }

    @SneakyThrows
    private boolean isTableExistsInDB(Class<?> entity) {
        Table tableAnnotation = entity.getAnnotation(Table.class);
        String tableName = tableAnnotation.name();
        String SQL = String.format(CHECK_TABLE_SQL_PATTERN, tableName);
        try (Connection connection = connectionFactory.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL);) {
            if (resultSet.next()) {
                return resultSet.getBoolean("exists");
            } else {
                return false;
            }
        }

    }

    private void validateEntities(Set<Class<?>> entities) {
        boolean flag = false;
        for (var entity : entities) {
            Field[] fields = entity.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(ID.class) && field.getType() == Long.class) {
                    flag = true;
                    break;
                }
            }

            if (!flag) {
                throw new RuntimeException("Entity doesn't have @ID and type isn't type of Long");
            }

            List<String> uniqueFieldsList = new ArrayList<>();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    Class<?> type = field.getType();
                    if (type.isPrimitive()) {
                        throw new RuntimeException("Field can't be primitive");
                    }

                    if (uniqueFieldsList.contains(field.getName())) {
                        throw new RuntimeException("Expected only unique names of fields");
                    }

                    uniqueFieldsList.add(field.getName());
                }
            }
        }
    }

    private Set<Class<?>> getClassesWithTableAnnotation() {
        Scanner scanner = context.getConfig().getScanner();
        Reflections reflections = scanner.getReflections();
        entitiesAnnotatedWithTable = reflections.getTypesAnnotatedWith(Table.class);
        return entitiesAnnotatedWithTable;
    }

    @SneakyThrows
    private boolean isSequenceExists() {
        String SQL = String.format(CHECK_SEQ_SQL_PATTERN, SEQ_NAME);
        try (Connection connection = connectionFactory.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL)) {
            if (resultSet.next()) {
                return resultSet.getBoolean("exists");
            } else {
                return false;
            }
        }
    }

    @SneakyThrows
    private void createSequence() {
        try (Connection connection = connectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            String SQL = String.format(CREATE_ID_SEQ_PATTERN, SEQ_NAME);
            statement.executeUpdate(SQL);
        }
    }
}
