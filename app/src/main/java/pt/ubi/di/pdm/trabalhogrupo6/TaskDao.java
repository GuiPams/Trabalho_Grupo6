package pt.ubi.di.pdm.trabalhogrupo6;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// DAO (Data Access Object) que define as operações CRUD para a tabela "tasks".
@Dao
public interface TaskDao {

    // CREATE insere uma nova tarefa e devolver o ID gerado
    @Insert
    long insert(TaskEntity task);

    // READ seleciona todas as tarefas ordenadas por ID decrescente
    @Query("SELECT * FROM tasks ORDER BY id DESC")
    List<TaskEntity> getAllTasks();

    // READ seleciona uma tarefa pelo ID
    @Query("SELECT * FROM tasks WHERE id = :taskId LIMIT 1")
    TaskEntity getTaskById(int taskId);

    // UPDATE atualiza os dados de uma tarefa existente
    @Update
    void update(TaskEntity task);

    // DELETE elimina uma tarefa específica
    @Delete
    void delete(TaskEntity task);

    // DELETE elimina todas as tarefas da tabela
    @Query("DELETE FROM tasks")
    void deleteAll();
}
