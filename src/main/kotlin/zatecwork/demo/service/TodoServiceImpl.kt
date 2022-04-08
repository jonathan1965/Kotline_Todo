package zatecwork.demo.service

import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.data.repository.findByIdOrNull
import zatecwork.demo.persistence.TodoEntity
import org.springframework.stereotype.Service
import zatecwork.demo.persistence.TodoRepository
import org.springframework.data.repository.findByIdOrNull



@Service
class TodoServiceImpl(
    private val todoRepository: TodoRepository
    ): TodoService{

    override fun createOrUpdate(todo: TodoEntity) {
        todoRepository.save(todo)
    }

    override fun removeById(id: Int) {
        with(todoRepository.findByIdOrNull(id)) {
            requireNotNull(this){"could not find todo with id '$id'"}
            todoRepository.delete(this)
        }

    }

    override fun removeAllCompleted() {
        todoRepository.deleteAllByCompleted(true)
    }

    override fun getById(id: Int): TodoEntity {
        return todoRepository.findByIdOrNull(id) ?: throw IllegalArgumentException("could not find todo with id '$id'")
    }

    override fun getByCompleted(completed: Boolean): List<TodoEntity> {
        return todoRepository.findAllByCompleted(completed)
    }

    override fun getAll(): List<TodoEntity> {
        return todoRepository.findAll()
    }

}