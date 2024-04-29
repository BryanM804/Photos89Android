package group89.photos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Album implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private List<Photo> photos;

    public Album(String name) {
        this.name = name;
        this.photos = new ArrayList<>();
    }

    public String toString() {
        return this.name;
    }

    public void addPhoto(Photo newPhoto) {
        photos.add(newPhoto);
    }
    public void removePhoto(Photo removingPhoto) {
        photos.remove(removingPhoto);
    }
    public void rename(String newName) {
        this.name = newName;
    }

    public List<Photo> getPhotos() {
        return this.photos;
    }
    public String getName() {
        return this.name;
    }
}
