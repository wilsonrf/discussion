package com.wilsonfranca.discussion.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by wilson.franca on 02/11/17.
 */

@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "com.wilsonfranca.discussion")
public class MongoRepositoryConfiguration {


}
