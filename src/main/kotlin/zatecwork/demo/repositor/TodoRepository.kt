package zatecwork.demo.repositor

import org.springframework.data.jpa.repository.JpaRepository
import zatecwork.demo.persistence.TodoEntity

interface TodoRepository: JpaRepository<TodoEntity, Int> {
    fun findAllByCompleted(completed: Boolean): List<TodoEntity>
    fun deleteAllByCompleted(completed: Boolean)
}