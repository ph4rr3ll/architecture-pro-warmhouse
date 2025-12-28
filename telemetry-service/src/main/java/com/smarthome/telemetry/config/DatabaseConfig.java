package com.smarthome.telemetry.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
        excludeFilters = @org.springframework.context.annotation.ComponentScan.Filter(
                type = org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE,
                classes = com.smarthome.telemetry.repository.SensorRepository.class
        ),
        entityManagerFactoryRef = "telemetryEntityManagerFactory",
        transactionManagerRef = "telemetryTransactionManager"
)
public class DatabaseConfig {

    @Primary
    @Bean(name = "telemetryDataSource")
    public DataSource telemetryDataSource(
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String username,
            @Value("${spring.datasource.password}") String password,
            @Value("${spring.datasource.driver-class-name}") String driverClassName) {
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName(driverClassName)
                .build();
    }

    @Primary
    @Bean(name = "telemetryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean telemetryEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("telemetryDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.smarthome.telemetry.entity")
                .persistenceUnit("telemetry")
                .build();
    }

    @Primary
    @Bean(name = "telemetryTransactionManager")
    public PlatformTransactionManager telemetryTransactionManager(
            @Qualifier("telemetryEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}

