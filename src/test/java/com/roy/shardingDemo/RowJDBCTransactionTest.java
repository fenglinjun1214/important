package com.roy.shardingDemo;

import org.junit.Test;
import org.omg.IOP.TransactionService;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author ：楼兰
 * @date ：Created in 2021/4/1
 * @description: 纯JDBC方式实现ShardingSphere分布式事务
 **/


public class RowJDBCTransactionTest {

    //测试XA事务管理器
    @Test
    public void xaTransactionTest() throws IOException, SQLException {
        XATransactionService service = new XATransactionService("/META-INF/sharding-databases-tables.yaml");
        service.init();

        service.insert();
        service.insertFailed();
        service.cleanup();
    }

    @Test
    public void seataTransactionTest() throws IOException, SQLException {
        SeataTransactionService service = new SeataTransactionService("/META-INF/sharding-databases-tables.yaml");
        service.init();

        service.insert();
        service.insertFailed();
        service.cleanup();
    }
}
