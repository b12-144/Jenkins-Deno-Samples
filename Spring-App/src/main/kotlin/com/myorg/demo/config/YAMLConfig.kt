package com.myorg.demo.config
importcom.myorg.demo.entities.EDatasource
import lombok.Data
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*
import javax.sql.DataSource

//@Configuration
//@EnableConfigurationProperties
//@ConfigurationProperties
//@Data
//class YAMLConfig {
//    lateinit  var name: String
//    lateinit var environment: String
//    lateinit var servers: List<String>
//    //lateinit var datasource:EDatasource
//    //private val datasource: EDatasource = EDatasource()
//
////    @get:Bean
////    val dataSource: DataSource
////        get() {
////            val dataSourceBuilder = DataSourceBuilder.create()
////            //val driverClassName: String? = datasource.driverClassName;
////            dataSourceBuilder.driverClassName(datasource.driverClassName)
////            dataSourceBuilder.url(datasource.url.toString())
////            dataSourceBuilder.username(datasource.userName)
////            dataSourceBuilder.password(datasource.password)
////            return dataSourceBuilder.build() as DataSource
////        }
//
////    @get:Bean
////    val dataSource: DataSource
////        get() {
////            val dataSourceBuilder = DataSourceBuilder.create()
////            //val driverClassName: String? = datasource.driverClassName;
////            dataSourceBuilder.driverClassName("org.postgresql.Driver")
////            dataSourceBuilder.url("jdbc:postgresql://yourURL:9090/postgres")
////            dataSourceBuilder.username("userName")
////            dataSourceBuilder.password("yourPassword")
////            return dataSourceBuilder.build() as DataSource
////        }
//
//    init {
//        //val driverClassName: String = datasource.getDriverClassName()
//    }
//}
