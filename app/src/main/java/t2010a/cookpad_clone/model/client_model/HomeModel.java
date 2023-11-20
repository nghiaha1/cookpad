
package t2010a.cookpad_clone.model.client_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class HomeModel implements Serializable {

    @SerializedName("totalItems")
    @Expose
    private Integer totalItems;
    @SerializedName("totalPage")
    @Expose
    private Integer totalPage;
    @SerializedName("currentPage")
    @Expose
    private Integer currentPage;
    @SerializedName("content")
    @Expose
    private List<Content> content;
    private final static long serialVersionUID = -1592607066885925278L;

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

}
