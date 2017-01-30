import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Player {
	private int playerNo;
	private int playerScore;
	private ArrayList<String>playedPlayers;
	private ArrayList<String>notPlayedPlayers;
	private ArrayList<String>copy1;
	private ArrayList<String>copy2;
	
	public Player(int i,int j){
		playerNo = i;
		playerScore = 0;
		playedPlayers = new ArrayList<String>();
		notPlayedPlayers = new ArrayList<String>();
		fillNotPlayed(i,j);
	}
	
	public int getPlayerNo(){
		return playerNo;
	}

	public int getPlayerScore(){
		calculateScore();
		return playerScore;
	}
	
	public ArrayList<String> getPlayedPlayers(){
		return playedPlayers;
	}
	
	public void addPlayedPlayer(int played){
		playedPlayers.add(Integer.toString(played));
		notPlayedPlayers.remove(Integer.toString(played));
	}
	
	public void calculateScore(){
		playerScore = 0;
		ArrayList<String>temp = new ArrayList<String>(playedPlayers);
		System.err.println(playedPlayers);
		for(int i = 0; i<temp.size(); i++){
			int occurences = Collections.frequency(temp,temp.get(i));
			if(occurences>1){
				playerScore = playerScore - (occurences*5);
				temp.removeAll(Collections.singleton(temp.get(i)));
			}
			else{
				temp.remove(i);
				playerScore+=2;	
			}
			i=-1;
		}
	}
	
	public void fillNotPlayed(int i,int j){
		for(int k = 1; k<j+1; k++){
			if(k!=i){
				notPlayedPlayers.add(Integer.toString(k));
			}
		}
	}
	
	public ArrayList<String> getNotPlayedPlayers(){
		ArrayList<String> copy = new ArrayList<String>(notPlayedPlayers);
		return copy;
	}
	
	public int getNewPlayer(ArrayList<Integer>notGrouped, int numPlayers){
		Random r = new Random();
		int player = 0;
		int i = 0;
//		System.out.println(notPlayedPlayers);
//		System.out.println(notGrouped);
		while(!notGrouped.contains(player)){
//			if(notPlayedPlayers.isEmpty()){
//				fillNotPlayed(getPlayerNo(),numPlayers);
//			}
			if(i==notPlayedPlayers.size()){
				return 100;
			}
			player = Integer.parseInt(notPlayedPlayers.get(i));
			i++;
		
		}
		addPlayedPlayer(player);
		return player;
	}

	public void setPlayedPlayers(){	
		notPlayedPlayers.clear();
		notPlayedPlayers = new ArrayList<String>(copy1);
		playedPlayers = new ArrayList<String>(copy2);
	}
	
	
	public void makeCopy(){
		copy1 = new ArrayList<String>(notPlayedPlayers);
		copy2 = new ArrayList<String>(playedPlayers);
	}
	
}
