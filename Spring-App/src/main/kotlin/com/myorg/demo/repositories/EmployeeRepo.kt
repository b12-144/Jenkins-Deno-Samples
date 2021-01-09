package com.myorg.demo.repositories
importcom.myorg.demo.helpers.Helper
importcom.myorg.demo.helpers.Logger
importcom.myorg.demo.interfaces.IEmployeeRepository
importcom.myorg.demo.entities.EEmployee
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

@Repository
open class EmployeeRepo(@field:PersistenceContext val entityManager: EntityManager) : IEmployeeRepository {

    override fun getNextID(): Long {
        val maxID = entityManager.createQuery("select max(e.id) from EEmployee e where e.id >= :myOrg") //usei o myOrg só para testar o setParameter :-)
                .setParameter("myOrg", 0L)
                .singleResult as Long
        return maxID + 1
    }

    @Transactional
    override fun getAll(): List<EEmployee> {
        val list: List<EEmployee> = entityManager.createQuery("SELECT a FROM " + EEmployee::class.java.name + " a", EEmployee::class.java).resultList
        return list
    }

    override fun getByID(id: Long): EEmployee? {
        return entityManager.find(EEmployee::class.java, id)
    }

    @Transactional
    override fun add(employee: EEmployee): Long {
        entityManager.persist(employee)
        return employee.id;
    }

    @Transactional
    override fun replace(id: Long, employee: EEmployee): Boolean {
        val emp = entityManager.find(EEmployee::class.java, id) ?: return false
        entityManager.merge(employee)
        return true;
    }

//    fun patch(patch: JsonPatch?, targetEmployee: Employee?): Boolean {
//        throw UnsupportedOperationException("Not supported yet.") //To change body of generated methods, choose Tools | Templates.
//    }

    @Transactional
    override fun removeByID(employeeID: Long): Boolean {
        val employee = entityManager.find(EEmployee::class.java, employeeID)
        employee ?: return false
        entityManager.remove(employee)
        return true
    }
}
