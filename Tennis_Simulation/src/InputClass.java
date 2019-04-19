import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InputClass {

    @SerializedName("players")
    @Expose
    private List<Player> players = null;
    @SerializedName("tournaments")
    @Expose
    private List<Tournament> tournaments = null;

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Tournament> getTournaments() {
        return tournaments;
    }

    public void setTournaments(List<Tournament> tournaments) {
        this.tournaments = tournaments;
    }

}
