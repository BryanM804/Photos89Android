package group89.photos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import group89.photos.activities.AddAlbum;

public class Photos extends AppCompatActivity{

    private AlbumManager albumManager;
    private ListView albumList;
    private ActivityResultLauncher<Intent> startForResultAdd;
    private ActivityResultLauncher<Intent> startForResultEdit; // This may not have to be here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Toolbar mainToolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);

        // App specific storage path            VVVV
        this.albumManager = new AlbumManager(this.getFilesDir().toString());
        albumManager.loadAlbums();

        // Populate ListView
        this.albumList = findViewById(R.id.albumList);
        this.albumList.setAdapter(new ArrayAdapter<>(this, R.layout.album, this.albumManager.getAlbums()));
        // TODO: add on click that will open the album in a child activity

        // This is default code not sure what it even does
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.startForResultAdd = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        this.applyChanges(result);
                    }
                });
        this.startForResultEdit = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        this.applyChanges(result);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater USHousingMarket = getMenuInflater();
        USHousingMarket.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.album_add) {
            this.addAlbum();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void addAlbum() {
        Intent addAlbum = new Intent(this, AddAlbum.class);
        this.startForResultAdd.launch(addAlbum);
    }

    private void applyChanges(ActivityResult res) {
        Intent intent = res.getData();
        Bundle albumInfo = intent.getExtras();
        if (albumInfo == null) return;

        String newAlbumName = albumInfo.getString("albumName");
        this.albumManager.addAlbum(new Album(newAlbumName));
        this.albumManager.saveAlbums();

        // Adding the adapter again updates the list
        this.albumList.setAdapter(new ArrayAdapter<>(this, R.layout.album, this.albumManager.getAlbums()));
    }
}