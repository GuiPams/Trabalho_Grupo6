package pt.ubi.di.pdm.trabalhogrupo6

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

// Interface DAO (Data Access Object) equivalente em Kotlin.
@Dao
interface TaskDao {

    // CREATE: Insere uma nova tarefa e devolve o ID gerado
    @Insert
    fun insert(task: TaskEntity): Long

    // READ: Seleciona todas as tarefas ordenadas por ID decrescente
    @Query("SELECT * FROM tasks ORDER BY id DESC")
    fun getAllTasks(): List<TaskEntity>

    // READ: Seleciona uma tarefa pelo ID com segurança contra nulos
    @Query("SELECT * FROM tasks WHERE id = :taskId LIMIT 1")
    fun getTaskById(taskId: Int): TaskEntity?

    // UPDATE: Atualiza os dados de uma tarefa existente
    @Update
    fun update(task: TaskEntity)

    // DELETE: Elimina uma tarefa específica
    @Delete
    fun delete(task: TaskEntity)

    // DELETE: Elimina todas as tarefas da tabela
    @Query("DELETE FROM tasks")
    fun deleteAll()
}
