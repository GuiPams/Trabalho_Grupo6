package pt.ubi.di.pdm.trabalhogrupo6;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// entidade Room da tabela "tasks" no SQLite
@Entity(tableName = "tasks")
public class TaskEntity {

    // chave primaria
    @PrimaryKey(autoGenerate = true)
    private int id;

    // coluna para o título da tarefa
    @ColumnInfo(name = "title")
    private String title;

    // coluna para a descrição da tarefa
    @ColumnInfo(name = "description")
    private String description;

    // coluna para o estado da tarefa
    @ColumnInfo(name = "is_completed")
    private boolean isCompleted;

    // construtor para criar uma nova tarefa
    public TaskEntity(String title, String description, boolean isCompleted) {
        this.title = title;
        this.description = description;
        this.isCompleted = isCompleted;
    }

    // getters e setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public boolean isCompleted() {
        return isCompleted;
    }
    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
