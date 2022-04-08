package zatecwork.demo.services

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import zatecwork.demo.persistence.TodoEntity
import zatecwork.demo.persistence.TodoRepository
import zatecwork.demo.service.TodoService


@SpringBootTest
class TodoServiceImplTest(
    @Autowired private val todoService: TodoService

    ) {

    @MockkBean
    private lateinit var todoRepository: TodoRepository

    private fun aTodo(
      task: String = "a sample task",
      completed: Boolean = false
    ) = TodoEntity(
        task = task,
        completed = completed
    )


    @Test
    fun `will create or update by calling repository save `() {
            // given
            val  aTestTodo = aTodo()
            every { todoRepository.save(aTestTodo)} returns aTestTodo

         todoService.createOrUpdate(aTestTodo)

         verify { todoRepository.save(aTestTodo)}

    }

    @Test
    fun `can get existing todo by id`() {
        val aTestTodo: TodoEntity = aTodo()
        every { todoRepository.findByIdOrNull(4711) } returns aTestTodo

        val result: TodoEntity = todoService.getById(4711)
        assertThat(result).isEqualTo(aTestTodo)
    }


    @Test
    fun `will throw if trying to get todo by id that not exists`() {
        val aTestTodo: TodoEntity = aTodo()
        every { todoRepository.findByIdOrNull(4711) } returns null

        Assertions.assertThrows(IllegalArgumentException::class.java) {
            todoService.getById(4711)
        }
    }

    @Test
    fun `can find all completed`() {

    val anCompleted = aTodo(completed = true)
        every {todoRepository.findAllByCompleted(true)} returns listOf(anCompleted)

        val result = todoService.getByCompleted(true)
        assertThat(result).containsExactly(anCompleted)



    }


    @Test
    fun `can remove all completed`() {

        justRun {todoRepository.deleteAllByCompleted(any())}

        todoService.removeAllCompleted()
        verify { todoRepository.deleteAllByCompleted(true) }



    }

    @Test
    fun `can find all todos`() {

        val aTestTodo = aTodo()
        every { todoRepository.save(aTestTodo)} returns aTestTodo

        val aTestTodos = aTodo("eating",false)
        every{todoRepository.save(aTestTodos)} returns aTestTodos

        every {todoRepository.findAll()} returns listOf (aTestTodo,aTestTodos)
        val result  = todoService.getAll()
        assertThat(result).containsExactly(aTestTodo,aTestTodos)


    }


}