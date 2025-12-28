package com.smarthome.telemetry.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.smarthome.telemetry.repository",
        includeFilters = @org.springframework.context.annotation.ComponentScan.Filter(
                type = org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE,
                classes = com.smarthome.telemetry.repository.SensorRepository.class
        ),
        entityManagerFactoryRef = "sensorsEntityManagerFactory",
        transactionManagerRef = "sensorsTransactionManager"
)
public class SensorsDatabaseConfig {

    @Bean(name = "sensorsDataSource")
    public DataSource sensorsDataSource(
            @Value("${sensors.datasource.url}") String url,
            @Value("${sensors.datasource.username}") String username,
            @Value("${sensors.datasource.password}") String password,
            @Value("${sensors.datasource.driver-class-name}") String driverClassName) {
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName(driverClassName)
                .build();
    }

    @Bean(name = "sensorsEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean sensorsEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("sensorsDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.smarthome.telemetry.entity")
                .persistenceUnit("sensors")
                .properties(java.util.Map.of(
                        "hibernate.hbm2ddl.auto", "none",
                        "hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect"
                ))
                .build();
    }

    @Bean(name = "sensorsTransactionManager")
    public PlatformTransactionManager sensorsTransactionManager(
            @Qualifier("sensorsEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}

