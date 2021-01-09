package com.myorg.demo.interfaces
importcom.myorg.demo.entities.EEmployee

interface IEmployeeRepository {
    fun getNextID(): Long
    fun getAll(): List<EEmployee>
    fun getByID(id: Long): EEmployee?
    fun add(employee: EEmployee): Long
    fun replace(id: Long, employee: EEmployee): Boolean
    //fun patch(patch: JsonPatch?, targetEmployee: Employee?): Boolean
    fun removeByID(employeeID: Long): Boolean
}
