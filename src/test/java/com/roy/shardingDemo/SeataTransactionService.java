package com.roy.shardingDemo;

import org.apache.shardingsphere.shardingjdbc.api.yaml.YamlShardingDataSourceFactory;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.*;

/**
 * @author ：楼兰
 * @date ：Created in 2021/4/1
 * @description:
 **/

public class SeataTransactionService {

    private final DataSource dataSource;

    SeataTransactionService(final String yamlConfigFile) throws IOException, SQLException {
        File configFile = new File(SeataTransactionService.class.getResource(yamlConfigFile).getFile());
        dataSource = YamlShardingDataSourceFactory.createDataSource(configFile);
    }
    /**
     * Init.
     */
    void init() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS t_order");
            statement.execute("CREATE TABLE t_order (order_id BIGINT AUTO_INCREMENT, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id))");
            // for PostgreSQL
//            statement.execute("CREATE TABLE IF NOT EXISTS t_order (order_id BIGINT PRIMARY KEY NOT NULL, user_id INT NOT NULL, status VARCHAR(50))");
        }
    }

    /**
     * Clean up.
     */
    void cleanup() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS t_order");
        }
    }

    /**
     * Execute XA.
     *
     * @throws SQLException SQL exception
     */
    void insert() throws SQLException {
        TransactionTypeHolder.set(TransactionType.XA);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO t_order (user_id, status) VALUES (?, ?)");
            doInsert(preparedStatement);
            connection.commit();
        } finally {
            TransactionTypeHolder.clear();
        }
    }

    /**
     * Execute XA with exception.
     *
     * @throws SQLException SQL exception
     */
    void insertFailed() throws SQLException {
        TransactionTypeHolder.set(TransactionType.XA);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO t_order (user_id, status) VALUES (?, ?)");
            doInsert(preparedStatement);
            connection.rollback();
        } finally {
            TransactionTypeHolder.clear();
        }
    }

    private void doInsert(final PreparedStatement preparedStatement) throws SQLException {
        for (int i = 0; i < 10; i++) {
            preparedStatement.setObject(1, i);
            preparedStatement.setObject(2, "init");
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Select all.
     *
     * @return record count
     */
    int selectAll() throws SQLException {
        int result = 0;
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT COUNT(1) AS count FROM t_order");
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        }
        return result;
    }
}