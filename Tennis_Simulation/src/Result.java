import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.omg.CORBA.PUBLIC_MEMBER;

public class Result {

    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("player_id")
    @Expose
    private Integer playerId;
    @SerializedName("gained_experience")
    @Expose
    private Integer gainedExperience;
    @SerializedName("total_experience")
    @Expose
    private Integer totalExperience;

    public Result(Integer order, Integer playerId, Integer gainedExperience, Integer totalExperience) {
        this.order = order;
        this.playerId = playerId;
        this.gainedExperience = gainedExperience;
        this.totalExperience = totalExperience;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public Integer getGainedExperience() {
        return gainedExperience;
    }

    public void setGainedExperience(Integer gainedExperience) {
        this.gainedExperience = gainedExperience;
    }

    public Integer getTotalExperience() {
        return totalExperience;
    }

    public void setTotalExperience(Integer totalExperience) {
        this.totalExperience = totalExperience;
    }

}
