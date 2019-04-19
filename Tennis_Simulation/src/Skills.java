import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Skills {

    @SerializedName("clay")
    @Expose
    private Integer clay;
    @SerializedName("grass")
    @Expose
    private Integer grass;
    @SerializedName("hard")
    @Expose
    private Integer hard;

    public Integer getClay() {
        return clay;
    }

    public void setClay(Integer clay) {
        this.clay = clay;
    }

    public Integer getGrass() {
        return grass;
    }

    public void setGrass(Integer grass) {
        this.grass = grass;
    }

    public Integer getHard() {
        return hard;
    }

    public void setHard(Integer hard) {
        this.hard = hard;
    }

}
