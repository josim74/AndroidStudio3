package josim74.github.com.roomlib;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText nameET, deptET, cityET;
    private ListView listView;
    private StudentAdapter adapter;
    private List<Student>students = new ArrayList<>();
    private MainViewModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        model = ViewModelProviders.of(this).get(MainViewModel.class);
        nameET = findViewById(R.id.nameET);
        deptET = findViewById(R.id.deptET);
        listView = findViewById(R.id.studentLV);
        cityET = findViewById(R.id.cityET);
        updateUI();
    }

    public void saveStudent(View view) {
        String name = nameET.getText().toString();
        String dept = deptET.getText().toString();
        String city = cityET.getText().toString();
        Student student = new Student(name,dept,city);

        long insertedRow = StudentDatabase.getInstance(this).getStudentDao().insertStudent(student);
        if(insertedRow > 0){
            Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI() {
        //students = StudentDatabase.getInstance(this).getStudentDao().getAllStudents();
        /*StudentDatabase.getInstance(this).getStudentDao().getAllLiveStudents()
                .observe(this, new Observer<List<Student>>() {
                    @Override
                    public void onChanged(@Nullable List<Student> students) {
                        adapter = new StudentAdapter(MainActivity.this,students);
                        listView.setAdapter(adapter);
                    }
                });*/
        model.getStudents().observe(this, new Observer<List<Student>>() {
            @Override
            public void onChanged(@Nullable List<Student> students) {
                adapter = new StudentAdapter(MainActivity.this,students);
                listView.setAdapter(adapter);
            }
        });
    }
}
