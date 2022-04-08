package zatecwork.demo.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.put
import zatecwork.demo.Controller.TodoRequest
import zatecwork.demo.persistence.TodoEntity
import zatecwork.demo.persistence.TodoRepository


@AutoConfigureMockMvc
@SpringBootTest
class TodoControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val todoRepository: TodoRepository
) {
   private fun aTodo(
       task: String = "a sample task",
       completed: Boolean= false
   )  = TodoEntity(
       task =  task,
       completed = completed
   )
     private val testData = listOf(
         aTodo(task="cleaning up", completed = true),
         aTodo(task = "cooking a meal"),
         aTodo(task = "coding kotlin")
     )

    @Test
    fun `can remove todo`() {
            val persistedTestData = todoRepository.saveAll(testData)

        mockMvc.delete("/todo/${persistedTestData.last().id}")
            .andExpect {
                status {isOk()}
            }

        Assertions.assertThat(todoRepository.findAll().map { it.task })
            .containsExactly(
                persistedTestData[0].task,
                persistedTestData[1].task
            )
    }

    @Test
    fun `can put todo `() {
            // given
            mockMvc.put("/todo") {
                contentType = MediaType.APPLICATION_JSON
                content = TodoRequest(task ="test").asJsonString()
            }.andExpect {
                status {isOk()}
            }

    }

    @BeforeEach
    fun clearData() {
        todoRepository.deleteAll()
    }


    private fun TodoRequest.asJsonString() =
        jacksonObjectMapper().writeValueAsString(this)
}