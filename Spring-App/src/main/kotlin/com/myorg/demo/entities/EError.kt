package com.myorg.demo.entities

import lombok.Data

@Data
class EError {
    public val className: String? = null
    public val methodName: String? = null
    public val message: String? = null
    public val exception: Exception? = null
}
