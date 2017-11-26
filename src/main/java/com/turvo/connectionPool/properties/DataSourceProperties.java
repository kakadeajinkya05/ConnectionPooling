package com.turvo.connectionPool.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataSourceProperties {

	private String driver1;
	private String url1;
	private String username1;
	private String password1;
	
	private String driver2;
	private String url2;
	private String username2;
	private String password2;
	
}
