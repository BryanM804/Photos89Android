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

import group89.photos.activities.AddEditAlbum;
import group89.photos.activities.SearchPhotos;
import group89.photos.activities.ViewAlbum;

public class Photos extends AppCompatActivity {

    private AlbumManager albumManager;
    private ListView albumList;
    private ActivityResultLauncher<Intent> startForResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Toolbar mainToolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);

        // App specific storage path            VVVV
        albumManager = new AlbumManager(this.getFilesDir().toString());

        // Populate ListView
        albumList = findViewById(R.id.albumList);
        albumList.setAdapter(new ArrayAdapter<>(this, R.layout.album, albumManager.getAlbums()));
        albumList.setOnItemClickListener((list, view, pos, id) -> openAlbum(pos));

        // This is default code not sure what it even does
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        startForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        applyChanges(result);
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
            addAlbum();
            return true;
        } else if (item.getItemId() == R.id.search_button) {
            openSearch();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }

    private void addAlbum() {
        Intent addAlbum = new Intent(this, AddEditAlbum.class);
        startForResult.launch(addAlbum);
    }

    public void openSearch() {
        Intent searchIntent = new Intent(this, SearchPhotos.class);
        startActivity(searchIntent);
    }

    private void openAlbum(int index) {
        Album selectedAlbum = albumManager.getAlbums().get(index);
        albumManager.setSelectedAlbum(selectedAlbum.getName());
        Bundle bundle = new Bundle();

        bundle.putString("albumName", selectedAlbum.getName());

        Intent openAlbumIntent = new Intent(this, ViewAlbum.class);
        openAlbumIntent.putExtras(bundle);
        startForResult.launch(openAlbumIntent);
    }

    private void applyChanges(ActivityResult res) {
        Intent intent = res.getData();
        Bundle albumInfo = intent.getExtras();
        if (albumInfo == null) return;

        String newAlbumName = albumInfo.getString("albumName");

        albumManager.addAlbum(new Album(newAlbumName));
        albumManager.saveAlbums();

        updateList();
    }

    private void updateList() {
        // Adding the adapter again updates the list
        albumList.setAdapter(new ArrayAdapter<>(this, R.layout.album, albumManager.getAlbums()));
    }
}