package t2010a.cookpad_clone.model.shop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Id implements Serializable {
    @SerializedName("shoppingCartId")
    @Expose
    private Integer shoppingCartId;
    @SerializedName("productId")
    @Expose
    private Integer productId;
    private final static long serialVersionUID = -2537977758633377421L;

    public Integer getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(Integer shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

}
