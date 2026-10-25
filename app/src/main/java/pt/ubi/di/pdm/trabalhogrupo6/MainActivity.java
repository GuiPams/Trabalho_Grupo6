package pt.ubi.di.pdm.trabalhogrupo6;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// activity principal que demonstra a utilização do Room com threads secundarias
public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskActionListener {

    private AppDatabase db;
    private EditText etTaskTitle;
    private RecyclerView rvTasks;
    private TextView tvEmpty;
    private TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // instância da base de dados Room
        db = AppDatabase.getInstance(this);

        // componentes da interface
        etTaskTitle = findViewById(R.id.etTaskTitle);
        rvTasks = findViewById(R.id.rvTasks);
        tvEmpty = findViewById(R.id.tvEmpty);
        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnDeleteAll = findViewById(R.id.btnDeleteAll);

        // configurar RecyclerView e Adapter
        taskAdapter = new TaskAdapter(this);
        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        rvTasks.setAdapter(taskAdapter);

        // configurar ouvintes dos botoes
        btnAdd.setOnClickListener(v -> insertTask());
        btnDeleteAll.setOnClickListener(v -> deleteAllTasks());

        // carregar automaticamente as tarefas guardadas ao abrir a app
        loadTasks();
    }

    // READ carrega automaticamente todas as tarefas do Room em background
    private void loadTasks() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<TaskEntity> tasks = db.taskDao().getAllTasks();

            runOnUiThread(() -> {
                taskAdapter.setTasks(tasks);
                if (tasks.isEmpty()) {
                    rvTasks.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.VISIBLE);
                } else {
                    rvTasks.setVisibility(View.VISIBLE);
                    tvEmpty.setVisibility(View.GONE);
                }
            });
        });
    }

    // CREATE Insere a tarefa escrita na caixa de texto em background
    private void insertTask() {
        String title = etTaskTitle.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "Por favor, escreve o nome da tarefa.", Toast.LENGTH_SHORT).show();
            return;
        }

        AppDatabase.databaseWriteExecutor.execute(() -> {
            TaskEntity newTask = new TaskEntity(title, false);
            db.taskDao().insert(newTask);

            runOnUiThread(() -> {
                etTaskTitle.setText(""); // Limpa o campo de texto
                loadTasks(); // atualiza a lista imediatamente
                Toast.makeText(MainActivity.this, "Tarefa adicionada!", Toast.LENGTH_SHORT).show();
            });
        });
    }

    // UPDATE atualiza o estado da CheckBox em background
    @Override
    public void onToggleCompleted(TaskEntity task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            db.taskDao().update(task);

            runOnUiThread(this::loadTasks); // atualiza a lista imediatamente
        });
    }

    // DELETE elimina uma tarefa especifica em background
    @Override
    public void onDeleteTask(TaskEntity task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            db.taskDao().delete(task);

            runOnUiThread(() -> {
                loadTasks(); // atualiza a lista imediatamente
                Toast.makeText(MainActivity.this, "Tarefa eliminada.", Toast.LENGTH_SHORT).show();
            });
        });
    }

    // DELETE ALL apaga todas as tarefas da base de dados
    private void deleteAllTasks() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            db.taskDao().deleteAll();

            runOnUiThread(() -> {
                loadTasks(); // atualiza a lista imediatamente
                Toast.makeText(MainActivity.this, "Todas as tarefas foram apagadas.", Toast.LENGTH_SHORT).show();
            });
        });
    }
}
