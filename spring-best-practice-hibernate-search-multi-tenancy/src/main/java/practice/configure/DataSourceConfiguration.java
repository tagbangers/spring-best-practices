package practice.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

	@Autowired
	private AdditionalProperties additionalProperties;
	
	@Bean(name = {"dataSource", "dataSource1"})
	@ConfigurationProperties(prefix = "spring.additional.datasource1")
	public DataSource dataSource1() {
		DataSourceBuilder factory = DataSourceBuilder
				.create(this.additionalProperties.getDatasource1().getClassLoader())
				.driverClassName(this.additionalProperties.getDatasource1().getDriverClassName())
				.url(this.additionalProperties.getDatasource1().getUrl());
		return factory.build();
	}

	@Bean(name = "dataSource2")
	@ConfigurationProperties(prefix = "spring.additional.datasource2")
	public DataSource dataSource2() {
		DataSourceBuilder factory = DataSourceBuilder
				.create(this.additionalProperties.getDatasource2().getClassLoader())
				.driverClassName(this.additionalProperties.getDatasource2().getDriverClassName())
				.url(this.additionalProperties.getDatasource2().getUrl());
		return factory.build();
	}
}
