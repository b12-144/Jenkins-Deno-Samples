package com.myorg.demo.entities

import lombok.Data
import java.net.URI


@Data
data class EDatasource(
        val driverClassName: String? = null,
        val url: URI? = null,
        val userName: String? = null,
        val password: String? = null
)

