package practice.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Component
public class DataSourceInitializer {

	@Autowired
	private DataSource dataSource1;

	@Autowired
	private DataSource dataSource2;

	@PostConstruct
	public void init1() {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource("/schema.sql"));
		populator.addScript(new ClassPathResource("/data1.sql"));
		DatabasePopulatorUtils.execute(populator, this.dataSource1);
	}

	@PostConstruct
	public void init2() {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource("/schema.sql"));
		populator.addScript(new ClassPathResource("/data2.sql"));
		DatabasePopulatorUtils.execute(populator, this.dataSource2);
	}
}
