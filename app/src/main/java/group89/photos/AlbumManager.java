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
    private static AlbumManager instance;
    private String selectedAlbum;
    private final String appDir;
    private List<Album> albums;

    public AlbumManager(String appDir) {
        this.albums = new ArrayList<>();
        this.appDir = appDir;
        instance = this;

        loadAlbums();
    }

    public void setSelectedAlbum(String albumName) {
        this.selectedAlbum = albumName;
    }

    public String getSelectedAlbum() {
        return this.selectedAlbum;
    }

    public static AlbumManager getInstance() {
        return instance;
    }

    public void addAlbum(Album newAlbum) {
        this.albums.add(newAlbum);
    }

    public boolean deleteAlbum(String albumName) {
        for (Album a : albums) {
            if (a.getName().equals(albumName)) {
                return albums.remove(a);
            }
        }

        return false;
    }

    public Album getAlbumByName(String albumName) {
        for (Album a : albums) {
            if (a.getName().equals(albumName)) {
                return a;
            }
        }

        return null;
    }

    public Photo getMatchingPhoto(Photo other) {
        for (Photo p : getAlbumByName(selectedAlbum).getPhotos()) {
            if (p.equals(other)) {
                return p;
            }
        }
        return null;
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
            Log.d("INFO", "Path: " + managerPath);
        }
    }
}
