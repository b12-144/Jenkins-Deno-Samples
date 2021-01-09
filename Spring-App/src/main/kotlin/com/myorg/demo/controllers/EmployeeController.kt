package com.myorg.demo.controllers
importcom.myorg.demo.helpers.Logger
importcom.myorg.demo.entities.EEmployee
importcom.myorg.demo.services.EmployeeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI


@RestController
@RequestMapping("/api/employees")
@Schema(description = "Employees")
class EmployeeController(private val employeeService: EmployeeService) {

    @Operation(summary="Get a list of employees", description = "Returns a list of employees")
    @ApiResponses(value = [ApiResponse(responseCode="200", description = "Successful Operation",content = [(Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = EEmployee::class)))))]),
        ApiResponse(responseCode="500", description = "Server error")])
    @GetMapping
    fun getAll(): ResponseEntity<*>{
        try {
            val list = employeeService.getAll()
            return ResponseEntity.ok(list)
        } catch (e: Exception) {
            Logger.logError(e);
            return ResponseEntity<Any>(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @Operation(summary="Return a single employee")
    @ApiResponses(value = [ApiResponse(responseCode="200", description = "Successful Operation"),
        ApiResponse(responseCode="404", description = "Not found"),
        ApiResponse(responseCode="500", description = "Server error")])
    @GetMapping("/{id}")
    fun getByID(@PathVariable  id: Long): ResponseEntity<*> {
        try {
            val employee = employeeService.getByID(id)
            return if (employee != null) ResponseEntity.ok(employee) else ResponseEntity<Any>(HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            Logger.logError(e);
            return ResponseEntity<Any>(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @Operation(summary="Creates a new employee")
    @ApiResponses(value = [ApiResponse(responseCode="201", description = "Successful Operation"),
        ApiResponse(responseCode="400", description = "Bad request"),
        ApiResponse(responseCode="500", description = "Server error")])
    @PostMapping
    fun add(@RequestBody employee: EEmployee): ResponseEntity<*> {
        try {
            val newId = employeeService.add(employee)
            if (newId > 0) {
                val location = URI("/api/employees/$newId")
                return ResponseEntity.created(location).build<Any>()
            } else {
                return ResponseEntity<Any>(HttpStatus.BAD_REQUEST)
            }
        } catch (e: Exception) {
            Logger.logError(e);
            return ResponseEntity<Any>(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @Operation(summary="Updates the employee")
    @ApiResponses(value = [ApiResponse(responseCode="200", description = "Successful Operation"),
        ApiResponse(responseCode="400", description = "Bad request"),
        ApiResponse(responseCode="500", description = "Server error")])
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long,  @RequestBody employee: EEmployee): ResponseEntity<*> {
        try {//com o put, fazemos a substituição total
            return if (employeeService.replace(id, employee)) ResponseEntity<Any>(HttpStatus.OK) //status 200, mas poderia retornar NO_Content (204)
            else ResponseEntity<Any>(HttpStatus.BAD_REQUEST)
        } catch (e: Exception) {
            Logger.logError(e);
            return ResponseEntity<Any>(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

//    @PatchMapping(path = ["/employees/{id}"])
//    @Throws(JsonProcessingException::class, JsonPatchException::class)
//    fun patch(@PathVariable id: Long, @RequestBody patch: JsonPatch?): ResponseEntity<*> {
//        //com o patch, fazemos a substituição parcial
//        val employee = employeeService!!.getByID(id)
//        return if (employeeService.patch(patch, employee)) ResponseEntity<Any>(HttpStatus.NO_CONTENT) //status 204
//        else ResponseEntity<Any>(HttpStatus.BAD_REQUEST)
//    }

    @Operation(summary="Removes the employee")
    @ApiResponses(value = [ApiResponse(responseCode="200", description = "Successful Operation"),
        ApiResponse(responseCode="404", description = "Not found"),
        ApiResponse(responseCode="500", description = "Server error")])
    @DeleteMapping("/{id}")
    fun remove(@PathVariable id: Long): ResponseEntity<*> {
        try {
            return if (employeeService.removeByID(id)) ResponseEntity<Any>(HttpStatus.OK) //status 200, mas poderia retornar NO_Content (204)
            else ResponseEntity<Any>(HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            Logger.logError(e);
            return ResponseEntity<Any>(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
