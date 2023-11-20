
package t2010a.cookpad_clone.model.shop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Origin implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("status")
    @Expose
    private String status;
    private final static long serialVersionUID = 1718344492624772413L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Origin withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Origin withCountry(String country) {
        this.country = country;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Origin withStatus(String status) {
        this.status = status;
        return this;
    }

}
