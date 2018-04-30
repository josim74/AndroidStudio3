package josim74.github.com.roomlib;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by BITM Trainer 601 on 2/7/2018.
 */

@Entity(tableName = "tbl_student")
public class Student {
    @PrimaryKey(autoGenerate = true)
    private int studentId;
    @ColumnInfo(name = "col_st_name")
    private String studentName;
    @ColumnInfo(name = "col_st_dept")
    private String studentDept;

    @ColumnInfo(name = "col_st_city")
    private String studentCity;
    @Ignore
    private int count = 0;

    public Student(String studentName, String studentDept, String studentCity) {
        this.studentName = studentName;
        this.studentDept = studentDept;
        this.studentCity = studentCity;
    }

    @Ignore
    public Student(int studentId, String studentName, String studentDept, String studentCity) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentDept = studentDept;
        this.studentCity = studentCity;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentDept() {
        return studentDept;
    }

    public void setStudentDept(String studentDept) {
        this.studentDept = studentDept;
    }

    public String getStudentCity() {
        return studentCity;
    }

    public void setStudentCity(String studentCity) {
        this.studentCity = studentCity;
    }
}
