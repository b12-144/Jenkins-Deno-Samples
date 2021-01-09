package com.myorg.demo.helpers

importcom.myorg.demo.entities.EError


object Logger {

    fun logDebug(txt: String) {
        println(txt)
    }

    fun logError(ex: Exception) {
//        var rootCause: Throwable? = ex
//        while (rootCause!!.cause != null && rootCause.cause !== rootCause) {
//            rootCause = rootCause.cause
//        }
//        val className = rootCause.stackTrace[0].className
//        val methodName = rootCause.stackTrace[0].methodName
//        println(String.format("Exception caused by %s.%s", className, methodName))
        ex.printStackTrace()
    }

    fun logError(error: EError) {
        val lastWordOfClassName = error.className!!.substring(error.className.lastIndexOf(".") + 1)
        println(String.format("Exception caused by %s. Exception:", lastWordOfClassName, error.methodName))
        if (error.exception != null) error.exception.printStackTrace()
    }
}
