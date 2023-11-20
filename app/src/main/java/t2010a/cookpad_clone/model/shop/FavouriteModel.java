package t2010a.cookpad_clone.model.shop;

import java.io.Serializable;
import java.util.List;

public class FavouriteModel implements Serializable {
    private List<Favourite> content;

    public List<Favourite> getContent() {
        return content;
    }

    public void setContent(List<Favourite> content) {
        this.content = content;
    }
}
