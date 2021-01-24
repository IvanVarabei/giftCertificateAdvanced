package com.epam.esm.config;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@ComponentScan("com.epam.esm")
public class EmbeddedTestConfig {
    @Bean
    public DataSource createDataSource() throws IOException {
        EmbeddedPostgres postgres = EmbeddedPostgres.start();
        return postgres.getPostgresDatabase();
    }

    @Bean
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load();
        flyway.migrate();
        return flyway;
    }
}