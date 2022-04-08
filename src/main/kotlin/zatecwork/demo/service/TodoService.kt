package zatecwork.demo.service


import zatecwork.demo.persistence.TodoEntity

interface TodoService {

    fun createOrUpdate(todo: TodoEntity)

    fun removeById(id:Int)

    fun removeAllCompleted()

    fun getById(id:Int): TodoEntity

    fun getByCompleted(completed:Boolean):List<TodoEntity>

    fun getAll():List<TodoEntity>



}