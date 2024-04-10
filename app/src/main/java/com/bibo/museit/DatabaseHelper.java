    package com.bibo.museit;

    ;

    import android.content.ContentValues;
    import android.content.Context;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;

    import androidx.annotation.Nullable;

    public class DatabaseHelper extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 2;
        private static final String DATABASE_NAME = "MusicMetadata";
        private static final String TABLE_NAME = "Museit";

        private static final String COL_ID = "ID";
        private static final String COL_TITLE = "TITLE";

        private static final String COL_FILE_PATH = "FILE_PATH";

        public DatabaseHelper(@Nullable Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_TITLE + " TEXT, " +
                    COL_FILE_PATH + " TEXT)";
            db.execSQL(createTableQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

        public boolean insertdata(String TITLE,String FILE_PATH) {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_TITLE, TITLE);
            contentValues.put(COL_FILE_PATH, FILE_PATH);
            long result=sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
            if(result==-1){
                return false;

            }else {
                return true;
            }


        }


        public Cursor getData(){
            SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
            Cursor c=sqLiteDatabase.rawQuery(" Select * from " + TABLE_NAME,null);
            return c;



        }



        public int deleteData(String id) {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            return sqLiteDatabase.delete(TABLE_NAME, COL_ID + " = ?", new String[]{id});
        }



        public String getFilepathByTitle(String title) {
            SQLiteDatabase db = this.getReadableDatabase();
            String[] projection = {COL_FILE_PATH};
            String selection = COL_TITLE + " = ?";
            String[] selectionArgs = {title};
            Cursor cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);
            String filePath = null;
            if (cursor != null && cursor.moveToFirst()) {
                int filePathIndex = cursor.getColumnIndex(COL_FILE_PATH); // Retrieve the index of the file path column
                filePath = cursor.getString(filePathIndex); // Retrieve the file path using the index
                cursor.close();
            }
            return filePath;
        }

    }
