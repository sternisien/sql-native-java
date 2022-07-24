package com.example.demo.dao;

import com.example.demo.utils.ConnectionManager;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AbstractDao<T> {

  private final ConnectionManager connectionManager;

  public AbstractDao(ConnectionManager connectionManager) {
    this.connectionManager = connectionManager;
  }

  protected T findById(Long id, Class<T> clazz)
      throws NoSuchMethodException, InvocationTargetException, InstantiationException,
          IllegalAccessException, SQLException {
    StringBuilder sqlQuery = new StringBuilder("SELECT");
    StringBuilder from = new StringBuilder(" FROM ");
    StringBuilder jointure = new StringBuilder(" WHERE ");
    Map<Table, List<Column>> columnsForSelect = new HashMap<>();

    Field[] fields = clazz.getDeclaredFields();
    Table parentTable = clazz.getAnnotation(Table.class);
    Column parentPrimaryKey =
        Arrays.stream(fields)
            .filter(field -> field.getAnnotation(Id.class) != null)
            .findFirst()
            .map(field -> field.getAnnotation(Column.class))
            .orElseThrow();

    List<Column> parentColumns =
        Arrays.stream(fields)
            .filter(field -> field.getAnnotation(Column.class) != null)
            .map(field -> field.getAnnotation(Column.class))
            .collect(Collectors.toList());

    columnsForSelect.put(parentTable, parentColumns);

    List<Field> fieldsForFk =
        Arrays.stream(fields)
            .filter(field -> field.getAnnotation(JoinColumn.class) != null)
            .collect(Collectors.toList());

    fieldsForFk.stream()
        .forEach(
            field -> {
              Class<?> clazzChild = field.getType();
              Table childTable = clazzChild.getAnnotation(Table.class);
              AtomicReference<Column> primaryKeyChildTable = new AtomicReference<>();
              List<Column> tableColumnsOther =
                  Arrays.stream(clazzChild.getDeclaredFields())
                      .map(
                          childField -> {
                            Column chieldColumn = childField.getAnnotation(Column.class);
                            if (field.getAnnotation(ManyToOne.class) != null
                                && childField.getAnnotation(Id.class) != null) {
                              primaryKeyChildTable.set(chieldColumn);
                            }
                            return chieldColumn;
                          })
                      .collect(Collectors.toList());
              columnsForSelect.put(clazzChild.getAnnotation(Table.class), tableColumnsOther);

              if (field.getAnnotation(OneToMany.class) != null) {
                jointure
                    .append(parentTable.name())
                    .append(".")
                    .append(parentPrimaryKey.name())
                    .append("=")
                    .append(childTable.name())
                    .append(".")
                    .append(field.getAnnotation(JoinColumn.class).name());
              } else if (field.getAnnotation(ManyToOne.class) != null) {
                jointure
                    .append(parentTable.name())
                    .append(".")
                    .append(field.getAnnotation(JoinColumn.class).name())
                    .append("=")
                    .append(childTable.name())
                    .append(".")
                    .append(primaryKeyChildTable.get().name());
              }
            });

    columnsForSelect.forEach(
        (table, columns) -> {
          // alias to avoid ambiguous field between table
          from.append(table.name() + " as " + table.name() + ", ");
          IntStream.range(0, columns.size())
              .forEach(
                  idx -> {
                    sqlQuery.append(
                        StringUtils.SPACE
                            + String.format("%s.%s", table.name(), columns.get(idx).name()));

                    sqlQuery.append(StringUtils.SPACE + ",");
                  });
        });

    String query = StringUtils.removeEnd(sqlQuery.toString().trim(), ",");
    query = query.concat(StringUtils.removeEnd(from.toString().trim(), ","));
    query = query.concat(jointure.toString());

    Connection c = ConnectionManager.getInstance();
    Statement statement = c.createStatement();
    statement.execute(query);
    return null;
  }
}
