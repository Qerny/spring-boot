/*
 * Copyright 2012-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.autoconfigure.sql.init;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jdbc.init.DataSourceScriptDatabaseInitializer;
import org.springframework.boot.jdbc.init.dependency.DataSourceInitializationDependencyConfigurer;
import org.springframework.boot.sql.init.AbstractScriptDatabaseInitializer;
import org.springframework.boot.sql.init.DatabaseInitializationSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.util.StringUtils;

@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(AbstractScriptDatabaseInitializer.class)
@ConditionalOnSingleCandidate(DataSource.class)
@Import(DataSourceInitializationDependencyConfigurer.class)
class DataSourceInitializationConfiguration {

	@Bean
	DataSourceScriptDatabaseInitializer dataSourceScriptDatabaseInitializer(DataSource dataSource,
			SqlInitializationProperties initializationProperties) {
		DatabaseInitializationSettings settings = SettingsCreator.createFrom(initializationProperties);
		return new DataSourceScriptDatabaseInitializer(determineDataSource(dataSource,
				initializationProperties.getUsername(), initializationProperties.getPassword()), settings);
	}

	private static DataSource determineDataSource(DataSource dataSource, String username, String password) {
		if (StringUtils.hasText(username) && StringUtils.hasText(password)) {
			DataSourceBuilder.derivedFrom(dataSource).username(username).password(password)
					.type(SimpleDriverDataSource.class).build();
		}
		return dataSource;
	}

}
