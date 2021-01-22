package com.epam.esm.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@PropertySource(value = "classpath:application-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
@EnableTransactionManagement
@Profile({"prod", "dev"})
public class DatabaseConfig {
    @Value("${jdbcUrl}")
    private String jdbcUrl;
    @Value("${dataSource.user}")
    private String dataSourceUser;
    @Value("${dataSource.password}")
    private String dataSourcePassword;
    @Value("${driverClassName}")
    private String driverClassName;
    @Value("${dataSource.maximumPoolSize}")
    private int maximumPoolSize;
    @Value("${dataSource.poolName}")
    private String poolName;

    @Bean
    public DataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(dataSourceUser);
        config.setPassword(dataSourcePassword);
        config.setDriverClassName(driverClassName);
        config.setMaximumPoolSize(maximumPoolSize);
        config.setPoolName(poolName);
        return new HikariDataSource(config);
    }

    @Bean
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(createDataSource());
    }

    @Bean
    public DataSourceTransactionManager createTransactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(createDataSource());
        return transactionManager;
    }
}
