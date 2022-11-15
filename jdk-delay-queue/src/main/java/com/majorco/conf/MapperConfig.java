package com.majorco.conf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author xxxiao
 **/
@Configuration
@MapperScan("com.majorco.mapper")
public class MapperConfig {

}
