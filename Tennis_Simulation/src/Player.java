import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Player {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("hand")
    @Expose
    private String hand;
    @SerializedName("experience")
    @Expose
    private Integer experience;
    @SerializedName("skills")
    @Expose
    private Skills skills;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHand() {
        return hand;
    }

    public void setHand(String hand) {
        this.hand = hand;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Skills getSkills() {
        return skills;
    }

    public void setSkills(Skills skills) {
        this.skills = skills;
    }

}
