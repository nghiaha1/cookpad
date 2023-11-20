
package t2010a.cookpad_clone.model.client_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Content implements Serializable {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("eatNumber")
    @Expose
    private Integer eatNumber;
    @SerializedName("cookingTime")
    @Expose
    private Integer cookingTime;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("origin")
    @Expose
    private Origin origin;
    @SerializedName("ingredient")
    @Expose
    private List<Ingredient> ingredient = null;
    @SerializedName("making")
    @Expose
    private List<Making> making = null;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("thumbnails")
    @Expose
    private String thumbnails;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("likes")
    @Expose
    private Integer likes;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("category")
    @Expose
    private Category category;

    private boolean isLikeStatus;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


    public boolean isLikeStatus() {
        return isLikeStatus;
    }

    public void setLikeStatus(boolean likeStatus) {
        isLikeStatus = likeStatus;
    }

    private final static long serialVersionUID = 3025351350287674927L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEatNumber() {
        return eatNumber;
    }

    public void setEatNumber(Integer eatNumber) {
        this.eatNumber = eatNumber;
    }

    public Integer getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(Integer cookingTime) {
        this.cookingTime = cookingTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Origin getOrigin() {
        return origin;
    }

    private List<UserLikes> userIdLikes = new ArrayList<>();

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public List<Ingredient> getIngredient() {
        return ingredient;
    }

    public void setIngredient(List<Ingredient> ingredient) {
        this.ingredient = ingredient;
    }

    public List<Making> getMaking() {
        return making;
    }

    public void setMaking(List<Making> making) {
        this.making = making;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(String thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<UserLikes> getUserIdLikes() {
        return userIdLikes;
    }

    public void setUserIdLikes(List<UserLikes> userIdLikes) {
        this.userIdLikes = userIdLikes;
    }
}
