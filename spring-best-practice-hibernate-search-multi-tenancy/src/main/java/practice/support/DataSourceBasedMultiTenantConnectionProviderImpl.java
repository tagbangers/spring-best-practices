package practice.support;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
public class DataSourceBasedMultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

	@Autowired
	private DataSource dataSource1;

	@Autowired
	private DataSource dataSource2;

	private Map<String, DataSource> dataSourceMap = new HashMap<>();

	@PostConstruct
	public void init() {
		dataSourceMap.put(CurrentTenantIdentifierResolverImpl.TENANT_1_IDENTIFIER, dataSource1);
		dataSourceMap.put(CurrentTenantIdentifierResolverImpl.TENANT_2_IDENTIFIER, dataSource2);
	}

	@Override
	protected DataSource selectAnyDataSource() {
		return dataSource1;
	}

	@Override
	protected DataSource selectDataSource(String tenantIdentifier) {
		return dataSourceMap.get(tenantIdentifier);
	}
}
