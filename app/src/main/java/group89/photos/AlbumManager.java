package group89.photos;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AlbumManager implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String managerFile = "albumManager.dat";
    private final String appDir;
    private List<Album> albums;

    public AlbumManager(String appDir) {
        this.albums = new ArrayList<>();
        this.appDir = appDir;

        loadAlbums();
    }

    public void addAlbum(Album newAlbum) {
        this.albums.add(newAlbum);
    }

    public boolean deleteAlbum(String albumName) {
        for (Album a : this.albums) {
            if (a.getName().equals(albumName)) {
                return this.albums.remove(a);
            }
        }

        return false;
    }
    
    public List<Album> getAlbums() {
        return this.albums;
    }

    public void saveAlbums() {
        File managerPath = new File(appDir + File.separator + managerFile);

        try {
            if (!managerPath.isFile()) {
                boolean created = managerPath.createNewFile();
                Log.d("INFO", "File creation status: "+ created);
            }

            ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(managerPath));
            outStream.writeObject(this);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            Log.d("Error", "Could not save albums!\n" + e);
        }

    }

    public void loadAlbums() {
        File managerPath = new File(appDir + File.separator + managerFile);

        try {
            if (!managerPath.isFile()) {
                managerPath.createNewFile();
                return;
            }

            ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(managerPath));

            AlbumManager loadedManager = (AlbumManager) inStream.readObject();
            inStream.close();

            this.albums = loadedManager.albums;

        } catch (Exception e) {
            Log.d("Error", "Could not load albums!\n" + e);
        }
    }
}
