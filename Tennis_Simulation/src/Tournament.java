import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tournament {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("surface")
    @Expose
    private String surface;
    @SerializedName("type")
    @Expose
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSurface() {
        return surface;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
