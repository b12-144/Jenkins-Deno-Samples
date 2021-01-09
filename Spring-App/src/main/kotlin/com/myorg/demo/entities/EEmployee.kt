package com.myorg.demo.entities
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name="employees")
data class EEmployee(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,
        @Column(name = "name", length = 200, nullable = false, unique = true)
        val name: String? = null,
        @Schema(description = "Salário do funcionário", example = "1000")
        val salary: BigDecimal? = null,
        val age:Int = 0,
        @Transient //we do not save this column in the database
        val token: String? = null
)

