package t2010a.cookpad_clone.model.client_model;

import java.io.Serializable;

public class Category implements Serializable {
    private long id;
    private String name;
    private String status;

    public Category(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(long id) {
        this.id = id;
    }

    public Category(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
