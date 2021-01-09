package com.myorg.demo.config

import lombok.Data
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.net.URI

@ConstructorBinding
@ConfigurationProperties("example")
data class KotlinExampleProperties(
        val name: String,
        val girlFriend: String
)
//        val myService: MyService) {
//
//    data class MyService(
//            val apiToken: String,
//            val uri: URI
//    )
//}
