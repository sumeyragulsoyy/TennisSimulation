import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.*;
import java.io.*;
import static java.util.stream.Collectors.toMap;

public class Game {

    public static void main(String[] args){
        Gson gson=new GsonBuilder().setPrettyPrinting().create(); // create converter
        HashMap<Integer,Integer> gainedExperience = new HashMap<Integer,Integer>(); // player id-gained experience as a hash
        HashMap<Integer,Player> Id_Player =new HashMap<>();
        HashMap<Integer, ArrayList<Player>> playersToGainedExperience = new HashMap<Integer, ArrayList<Player>>(); // gained experience,player objects arraylist

        InputClass inputClass=null;
        try {
            inputClass=gson.fromJson(new FileReader("src/input.json"),InputClass.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Game tennis=new Game();
        tennis.setGainedExperince(inputClass.getPlayers(),gainedExperience,Id_Player);
        tennis.playTournaments(inputClass.getTournaments(),inputClass.getPlayers(),gainedExperience,Id_Player);

        Map<Integer, Integer> sorted=gainedExperience // sort by gained experience through game, player id with highest gained experience
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));

        List<Result> resultt=new ArrayList<Result>(); //fill the resultt list with result objects

        for (Map.Entry<Integer, Integer> entry : sorted.entrySet()) { // fill the playerToGainedExperience
           if  (playersToGainedExperience.containsKey(entry.getValue())){ // if contains same gained experience score add object to arraylist
               playersToGainedExperience.get(entry.getValue()).add(Id_Player.get(entry.getKey()));
           }else{
               playersToGainedExperience.put(entry.getValue(),new ArrayList<>(Arrays.asList(Id_Player.get(entry.getKey()))));
           }
        }
        for (Map.Entry<Integer, ArrayList<Player>> entry : playersToGainedExperience.entrySet()) { // sort values of playersToGainedExperience
            Comparator<Player> comparator = Comparator.comparing(e -> e.getExperience());
            entry.getValue().sort(comparator.reversed()); // sort each arraylist player objects according to initial experience score
        }

        Map<Integer, ArrayList<Player>> reverseSortedMap = new TreeMap<Integer, ArrayList<Player>>(Collections.reverseOrder());
        reverseSortedMap.putAll(playersToGainedExperience);


        int order=1;
        for (Map.Entry<Integer, ArrayList<Player>> entry : reverseSortedMap.entrySet()) { // add ordered object to resultt
            for(int i=0;i<entry.getValue().size();i++){
                //order,playerID,gained experience, total experience
                resultt.add(new Result(order,entry.getValue().get(i).getId(),entry.getKey(),entry.getKey()+entry.getValue().get(i).getExperience()));
                order++;
            }
        }

        Output output=new Output();
        output.setResults(resultt); // give the filled list to output class that will write to output.json
        String json=gson.toJson(output);
        //System.out.println(json);
        PrintWriter out=null;
        try {
            out=new PrintWriter("src/output.json");
            out.println(json);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            out.close();
        }
    }

    public void setGainedExperince(List<Player> players,HashMap<Integer,Integer> gainedExperience, HashMap<Integer,Player> Id_Player ){
        for (int i=0;i<players.size();i++){
            gainedExperience.put(players.get(i).getId(),0); //initialize 0 as a starting gained experience
            Id_Player.put(players.get(i).getId(),players.get(i));
        }
    }

    public void playTournaments(List<Tournament> tournaments,List<Player> players,HashMap<Integer,Integer> gainedExperience,HashMap<Integer,Player> Id_Player){
        for (int i=0;i<tournaments.size();i++){
            playTournament(tournaments.get(i),players,gainedExperience,Id_Player);
           // System.out.println(gainedExperience);
        }

    }
    public void playTournament(Tournament tournament,List<Player> players,HashMap<Integer,Integer> gainedExperience,HashMap<Integer,Player> Id_Player){
        if (tournament.getType().equals("elimination")){
            playElimination(players,tournament.getSurface(),gainedExperience,20,10); // winner +20,loser +10
        }else{
            playLeague(players,tournament.getSurface(),gainedExperience,Id_Player,10,1); // winner +10,loser +1
        }
    }

    public void playElimination(List<Player> players,String surface,HashMap<Integer,Integer> gainedExperience,int winner,int loser){
         List<Player> playersCopy = new ArrayList<>(players); // copy of players
         while(true) {
             List<Player> eliminated= new ArrayList<>(); // iterate objects from tour to next tour
             for (int i = 0; i <playersCopy.size(); i = i + 2) { // 2-2 8 matches
                 Player elim = getScore(playersCopy.get(i), playersCopy.get(i + 1), surface, gainedExperience, winner, loser); // take points from 1-1 match
                 eliminated.add(elim);
             }
             for(int j=0;j<eliminated.size();j++){
                 playersCopy.remove(eliminated.get(j)); // removes the loser from array list
             }
             if (playersCopy.size()==1){ //until 1 player
                 return;
             }
         }
    }

    public void playLeague(List<Player> players,String surface,HashMap<Integer,Integer> gainedExperience, HashMap<Integer,Player> Id_Player,int winner,int loser){
        //System.out.println(players);
        ArrayList<int[]> order=LeagueOrder(players); // order ={[2,9], [5,8], [7,7]}
        for(int i=0;i<order.size();i++){ //each 1-1 matches
            getScore(Id_Player.get(order.get(i)[0]),Id_Player.get(order.get(i)[1]),surface,gainedExperience,winner,loser);
        }
    }

    public Player getScore(Player player1,Player player2,String surface,HashMap<Integer,Integer> gainedExperience,int winner,int loser){ // each 1-1 match score calculation
        Player eliminated; // loser index
        int score1=1; //  match point
        int score2=1;
        if (player1.getHand().equals("left")){
            score1 +=2;
        }
        if (player2.getHand().equals("left")){
            score2 +=2;
        }
        if (player1.getExperience()+gainedExperience.get(player1.getId()) > player2.getExperience()+gainedExperience.get(player2.getId())){
            score1 +=3;
        }
        if (player1.getExperience()+gainedExperience.get(player1.getId()) < player2.getExperience()+gainedExperience.get(player2.getId())){
            score2 +=3;
        }
        switch(surface){
            case "clay":
                if (player1.getSkills().getClay() > player2.getSkills().getClay()){
                    score1 +=4;
                }
                if (player1.getSkills().getClay() < player2.getSkills().getClay()){
                    score2 +=4;
                }
                break;
            case "grass":
                if (player1.getSkills().getGrass() > player2.getSkills().getGrass()){
                    score1 +=4;
                }
                if (player1.getSkills().getGrass() < player2.getSkills().getGrass()){
                    score2 +=4;
                }
                break;
            case "hard":
                if (player1.getSkills().getHard() > player2.getSkills().getHard()){
                    score1 +=4;
                }
                if (player1.getSkills().getHard() < player2.getSkills().getHard()){
                    score2 +=4;
                }
        }
        if (score1>=score2){ // player 1 is winner if there is tie ,player 1 is winner
            gainedExperience.put(player1.getId(),gainedExperience.get(player1.getId())+winner);
            gainedExperience.put(player2.getId(),gainedExperience.get(player2.getId())+loser);
            eliminated=player2;
             // player 2 loser
        }else{
            gainedExperience.put(player1.getId(),gainedExperience.get(player1.getId())+loser);
            gainedExperience.put(player2.getId(),gainedExperience.get(player2.getId())+winner);
            eliminated=player1; // player 1 is loser
        }
        return eliminated;
        }

    public ArrayList<int[]>  LeagueOrder(List<Player> players){ // league all 1-1 order
        ArrayList<int[]> oneToOneMatches = new ArrayList<int[]>(); //{ [3,6],[5,9],[2,2] } ,
        HashMap<Integer, ArrayList<Integer>> AllMatches = new HashMap<Integer, ArrayList<Integer>>(); // will be empty at last
        ArrayList<Integer> playerIds = new ArrayList<Integer>(); // all ID of players
        for(int i=0;i<players.size();i++){
            playerIds.add(players.get(i).getId()); // initially get all IDs of players
        }
        for (int i=0;i<playerIds.size()-1;i++){ // put key to hashmap
            ArrayList<Integer> matches = new ArrayList<Integer>();
            AllMatches.put(playerIds.get(i),matches);
            for(int j=i+1;j<playerIds.size();j++){ // add value of key
                AllMatches.get(playerIds.get(i)).add(playerIds.get(j));
            }
        }
        Random randomGenerator = new Random();
        while (true) {
            List<Integer> keys = new ArrayList<>(AllMatches.keySet()); // take hashmap key values as a arraylist [12,56,23,78]
            int randomInt = randomGenerator.nextInt(keys.size()); //take random index in keys
            int player1_ID = keys.get(randomInt); // ID 1
            int randomInt2 = randomGenerator.nextInt(AllMatches.get(player1_ID).size()); //random index of values of player1 id key
            int player2_ID = AllMatches.get(player1_ID).get(randomInt2);
            int[] z=new int[2];
            z[0]=player1_ID;
            z[1]=player2_ID;
            oneToOneMatches.add(z); // 1-1 match added to oneToOneMatches
            AllMatches.get(player1_ID).remove(new Integer(player2_ID)); // 5->[45,54,12],remove 12 number object
            if (AllMatches.get(player1_ID).size() == 0) { // remove player1 id from hashmap because this player match all possible players
                AllMatches.remove(player1_ID);
            }
            if (AllMatches.size() == 0) {
                return oneToOneMatches;
            }
        }
    }



}
