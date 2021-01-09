package com.myorg.demo

importcom.myorg.demo.entities.EEmployee
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.net.URI

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmployeeControllerTests(@Autowired val restTemplate: TestRestTemplate) {

    private val robin = EEmployee(name="Robin", age=18)
    private var id: Long = 0

    @BeforeAll
    fun `Assert employee added`() {
        val entity = restTemplate.postForEntity(
                "/api/employees", robin, Any::class.java)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(entity.headers["Location"]).isNotNull
        val entityFromLocation = restTemplate.getForEntity(
                entity.headers.getFirst("Location"), EEmployee::class.java)
        assertThat(entityFromLocation.body?.name).isEqualTo(robin.name)
        assertThat(entityFromLocation.body?.age).isEqualTo(robin.age)
        id = entityFromLocation.body?.id!!
    }

    @Test
    fun `Assert employee returned`() {
        val entity = restTemplate.getForEntity("/api/employees/$id", EEmployee::class.java)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body?.name).isEqualTo(robin.name)
        assertThat(entity.body?.age).isEqualTo(robin.age)
    }

    @Test
    fun `Assert employee updated`() {
        val robinWithSalary = EEmployee(id=id, name="Robin", age=18, salary=BigDecimal(1000))
        restTemplate.put("/api/employees/$id", robinWithSalary)
        val entity = restTemplate.getForEntity("/api/employees/$id", EEmployee::class.java)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body?.name).isEqualTo(robinWithSalary.name)
        assertThat(entity.body?.age).isEqualTo(robinWithSalary.age)
        assertThat(entity.body?.salary).isEqualTo(robinWithSalary.salary)
    }

    @AfterAll
    fun `Assert employee deleted`() {
        restTemplate.delete("/api/employees/$id")
        val entity = restTemplate.getForEntity("/api/employees/$id", EEmployee::class.java)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }
}