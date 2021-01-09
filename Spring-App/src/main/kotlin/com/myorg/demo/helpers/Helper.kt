package com.myorg.demo.helpers

importcom.myorg.demo.entities.EError


object Helper {
    fun prepareError(ex: Exception?): EError {
        val error = EError()
        val stackElement = Thread.currentThread().stackTrace[2]
//        error.setClassName(stackElement.className)
//        error.setMethodName(stackElement.methodName)
//        error.setException(ex)
        return error
    }
}
