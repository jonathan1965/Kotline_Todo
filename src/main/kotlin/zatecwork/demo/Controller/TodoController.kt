package zatecwork.demo.Controller

import com.sun.tools.javac.comp.Todo
import org.springframework.web.bind.annotation.*
import zatecwork.demo.persistence.TodoEntity
import zatecwork.demo.service.TodoService

@RestController
class TodoController(
    private val todoService: TodoService
    ) {

    @GetMapping("/all")
    fun getAll() =
        todoService.getAll().toResponse()


    @DeleteMapping("/{id}")
    fun deleteTodo(
        @PathVariable id:Int
    ) {
        todoService.removeById(id)
    }
    @PutMapping
    fun putTodo(
        @RequestBody todoRequest: TodoRequest
    ){
        todoService.createOrUpdate(todoRequest.toEntity())
    }
}


data class TodoRequest(
    val task: String,
    val completed: Boolean = false

) {
     fun toEntity() = TodoEntity (
         task = task,
         completed = completed
     )

    data class TodoResponse(
        val id:Int,
        val task: String,
        val completed: Boolean,

    ) {
        fun TodoEntity.toResponse() =
            TodoResponse(id, task, completed)
        fun List<TodoEntity>.toResponse()  =
            map { it.toResponse() }
    }

}
