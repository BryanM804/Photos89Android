package group89.photos;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class Photo {
    private List<String> personTags;
    private List<String> locationTags;
    // I would think this has the same restriction of only one location per photo
    // But it never says that anywhere in the assignment so no need to overcomplicate
    private Uri image;

    public Photo() {
        this.personTags = new ArrayList<>();
        this.locationTags = new ArrayList<>();


    }
}
