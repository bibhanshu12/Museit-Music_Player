    package com.bibo.museit;

    import androidx.annotation.RequiresApi;
    import androidx.appcompat.app.AlertDialog;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.appcompat.widget.AppCompatButton;

    import android.Manifest;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.database.Cursor;
    import android.os.Build;
    import android.os.Bundle;
    import android.os.Environment;
    import android.util.Log;
    import android.view.View;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.ListView;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.karumi.dexter.Dexter;
    import com.karumi.dexter.DexterBuilder;
    import com.karumi.dexter.PermissionToken;
    import com.karumi.dexter.listener.PermissionDeniedResponse;
    import com.karumi.dexter.listener.PermissionGrantedResponse;
    import com.karumi.dexter.listener.PermissionRequest;
    import com.karumi.dexter.listener.SettingsClickListener;
    import com.karumi.dexter.listener.single.PermissionListener;

    import java.io.File;
    import java.util.ArrayList;

    public class MainActivity extends AppCompatActivity {

        AppCompatButton appCompatButton;
    //    RecyclerView recyclerView;
        ListView listView;
        Button getsongs;

        DatabaseHelper databaseHelper;
        TextView textView;


        @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            textView=findViewById(R.id.name);
            getsongs=findViewById(R.id.getsongs);
            databaseHelper = new DatabaseHelper(this);
            String name="",mail="";
            Intent intent=getIntent();
            name=intent.getStringExtra("Name");
            mail=intent.getStringExtra("mail");
    //        recyclerView=findViewById(R.id.recycleviewMain);
            listView=findViewById(R.id.listviewmain);


           DexterBuilder dexterBuilder = Dexter.withContext(this).withPermission(Manifest.permission.READ_MEDIA_AUDIO).withListener(new PermissionListener() {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                    getsongs.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Fetch songs from the internal storage directory accessible with READ_MEDIA_AUDIO permission
                            File internalStorageDirectory = getFilesDir(); // Use getFilesDir() to get the internal storage directory of the app
                            fetchAndSaveSongs(Environment.getExternalStorageDirectory(), databaseHelper);
                            fetchAndDisplaySongs();
                        }
                    });


                    // Display songs in the ListView
                    fetchAndDisplaySongs();



                }
                @Override
                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    try {
                        Toast.makeText(MainActivity.this, "Permission denied , you can't access the music", Toast.LENGTH_SHORT).show();
                        Thread.sleep(2000);

                    } catch (InterruptedException ignored) {
                        finish();
                    }
                }


                @Override
                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                }
            });

           dexterBuilder.check();











    //        button.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View v) {
    //                Intent intent=new Intent(MainActivity.this,M`````` apsActivity.class);

    //                startActivity(intent);
    //                finish();
    //            }
    //        });


        }
        // Define the FetchSongs method to recursively fetch and store MP3 files in the database
        public void fetchAndSaveSongs(File directory, DatabaseHelper dbHelper) {
            if (directory.exists() && directory.isDirectory()) {
                File[] files = directory.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isDirectory() && !file.isHidden()) {
                            fetchAndSaveSongs(file, dbHelper); // Recursive call for subdirectories
                        } else {
                            String fileName = file.getName();
                            // Check if the file is an audio file by checking its extension
                            if (fileName.endsWith(".mp3")) {
                                // Insert song metadata into the database
                                boolean insertionResult = dbHelper.insertdata(fileName, file.getAbsolutePath());
                                if (insertionResult) {
                                    // Data inserted successfully
                                    Log.d("Insertion", "Data inserted for file: " + fileName);
                                } else {
                                    // Failed to insert data
                                    Log.e("Insertion", "Failed to insert data for file: " + fileName);
                                }
                            }
                        }
                    }
                }
            } else {
                Log.e("FetchSongs", "Directory does not exist or is not a directory");
            }
        }




        private void fetchAndDisplaySongs() {
            // Fetch songs from the database
            Cursor cursor = databaseHelper.getData();

            // List to hold song titles
            ArrayList<String> songList = new ArrayList<>();

            // Add songs to the list
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String title = cursor.getString(1);
                    songList.add(title);
                } while (cursor.moveToNext());
                cursor.close();
            }

            // Create adapter and set it to ListView
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, songList);
            listView.setAdapter(adapter);
        }


        @Override
        public void onBackPressed() {
            AlertDialog.Builder alertdialog= new AlertDialog.Builder(this);
            alertdialog.setTitle("Exit?");
            alertdialog.setMessage("Are you sure to Exit ?");
            alertdialog.setIcon(R.drawable.baseline_exit_to_app_24);
            alertdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.super.onBackPressed();
                }
            });
            alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertdialog.show();
        }


    }