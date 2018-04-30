package josim74.github.com.roomlib;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by BITM Trainer 601 on 2/7/2018.
 */

@Database(entities = {Student.class}, version = 2)
public abstract class StudentDatabase extends RoomDatabase {
    private static StudentDatabase db;
    public abstract StudentDAO getStudentDao();

    static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("alter table tbl_student add column col_st_city text");
        }
    };

    public static StudentDatabase getInstance(Context context){
        if(db != null){
            return db;
        }
        db = Room.databaseBuilder(context,StudentDatabase.class,"student_db")
                .allowMainThreadQueries()
                .addMigrations(MIGRATION_1_2)
                .build();
        return db;
    }
}
