package josim74.github.com.roomlib;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by BITM Trainer 601 on 2/7/2018.
 */

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<Student>>students;
    private StudentDatabase db;

    public MainViewModel(@NonNull Application application) {
        super(application);
        db = StudentDatabase.getInstance(application);
    }

    public LiveData<List<Student>>getStudents(){
        students = db.getStudentDao().getAllLiveStudents();
        return students;
    }
}
