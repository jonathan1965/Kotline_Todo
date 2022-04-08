package zatecwork.demo.persistence

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.findByIdOrNull

@DataJpaTest
class TodoRepositoryTest (
    @Autowired private val todoRepository: TodoRepository
){
    private fun aTodo(
        task: String = "a sample task",
        completed: Boolean = false,
    ):TodoEntity = TodoEntity(
        task = task,
        completed = completed
    )

    @Test
    fun `can store a todo`() {
        val saved : TodoEntity = todoRepository.save(aTodo(completed = true))
        Assertions.assertThat(todoRepository.findByIdOrNull(saved.id)).isNotNull

    }

    private val testData = listOf(
        aTodo(task = "cleaning up", completed =  true),
        aTodo(task = "cooking a meal", completed = true),
        aTodo(task = "coding Kotlin", completed = true)
    )

    @Test
    fun `can read a todo`() {
        val testData1 = todoRepository.saveAll(testData)
        val testDataTodo = todoRepository.findByIdOrNull(testData1[0].id)
        Assertions.assertThat(testDataTodo?.task).isEqualTo("cleaning up")
        Assertions.assertThat(testDataTodo?.completed).isTrue

    }

    @Test
    fun `can remove a todo`() {
        val testDATA = todoRepository.saveAll(testData)

        todoRepository.delete(testData[1])

        Assertions.assertThat(todoRepository.findAll()).containsExactly(
            testData[0],
            testData[2],
        )

    }

    @Test
    fun `can update a todo`() {
        val todoEntity = todoRepository.save(aTodo(completed = false))

        todoRepository.save(todoEntity.apply {completed = true})

        val updateTodo = todoRepository.findByIdOrNull(todoEntity.id)
        Assertions.assertThat(updateTodo?.completed).isTrue



    }

    @Test
    fun `can get all completed todos`() {

        val testData = todoRepository.saveAll(testData)
        val completed = todoRepository.findAllByCompleted(true)

        Assertions.assertThat(completed.map { it.task }).containsExactly("cleaning up")
    }

        @Test
        fun `can get all inComplete todos`() {

            val testData = todoRepository.saveAll(testData)
            val completed = todoRepository.findAllByCompleted(false)

            Assertions.assertThat(completed.map{it.task}).containsExactly("cooking a meal")

        }
}