package com.myorg.demo.services

importcom.myorg.demo.config.KotlinExampleProperties
importcom.myorg.demo.entities.EEmployee
importcom.myorg.demo.helpers.Logger
importcom.myorg.demo.repositories.EmployeeRepo

import org.springframework.stereotype.Service

@Service
class EmployeeService(private val repository:EmployeeRepo) {

    //    @Autowired private EmployeeListRepository repository;

    fun getNextID(): Long{
        return repository.getNextID()
    }

    fun getAll(): List<EEmployee>{
        //Logger.logDebug("nome:"+yamlProperties.name);
        return repository.getAll()
    }

    fun getByID(id: Long): EEmployee? {
        return repository.getByID(id)
    }

    fun add(employee: EEmployee): Long {
        return repository.add(employee)
    }

    fun replace(id: Long, employee: EEmployee): Boolean {
        return repository.replace(id, employee)
    }

//    @Throws(JsonPatchException::class, JsonProcessingException::class)
//    fun patch(patch: JsonPatch?, targetEmployee: Employee?): Boolean {
//        return repository.patch(patch, targetEmployee)
//    }

    fun removeByID(employeeID: Long): Boolean {
        return repository.removeByID(employeeID)
    }
}
