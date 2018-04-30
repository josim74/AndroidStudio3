package josim74.github.com.roomlib;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by BITM Trainer 601 on 2/7/2018.
 */

@Dao
public interface StudentDAO {
    @Insert
    long insertStudent(Student student);

    @Insert
    long[] insertStudents(Student... students);

    @Query("select * from tbl_student")
    List<Student>getAllStudents();

    @Query("select * from tbl_student")
    LiveData<List<Student>>getAllLiveStudents();

    @Query("select * from tbl_student where studentId like:id")
    Student getStudentById(int id);

    @Update
    int updateStudent(Student student);

    @Delete
    int deleteStudent(Student student);
}
