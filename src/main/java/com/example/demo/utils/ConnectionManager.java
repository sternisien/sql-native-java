package com.example.demo.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class ConnectionManager {
  private static Connection con;

  /**
   * Function to get an instance {@link Connection} to get a database connection. This, init for the
   * first time or when a connection has been closed by a previous operation.
   *
   * @return {@link Connection} specific connection to the database
   * @throws SQLException
   */
  public static Connection getInstance() throws SQLException {
    if (Objects.isNull(con) || con.isClosed()) {
      con = initConnection();
    }

    return con;
  }

  /**
   * Function to get a connection to our database
   *
   * @return {@link Connection} specific connection to the database
   * @throws SQLException
   */
  private static Connection initConnection() throws SQLException {
    return DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/training?user=postgres&password=seb");
  }
}
