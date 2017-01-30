import java.util.ArrayList;
import java.util.Random;

public class HeuristicSearch {
	private ArrayList<HGroup> groups;
	private ArrayList<ArrayList<HGroup>> currentGroups;
	private ArrayList<ArrayList<HGroup>> finalGroups;
	private ArrayList<HPlayer> players;
	private ArrayList<HPlayer> finalPlayers;
	ArrayList<Integer> notGrouped;
	private int numPlayers;

	public static void main(String args[]) {
		HeuristicSearch bss = new HeuristicSearch();
		bss.search(16, 5, 4, 4);
	}

	public void search(int numPlayer,int rounds, int groupSize, int numGroups) {
		//finalGroups = new ArrayList<ArrayList<HGroup>>();
		numPlayers = numPlayer;
		int runs = 0;
		int groupScore = Integer.MIN_VALUE;
		while(runs<10){//10
			groups = new ArrayList<HGroup>();
			currentGroups = new ArrayList<ArrayList<HGroup>>();
			createPlayers(numPlayers);
			createGroups(rounds,numGroups,groupSize);
			if(getGroupsScore(groupSize)>groupScore){
				groupScore = getGroupsScore(groupSize);
				finalGroups = new ArrayList<ArrayList<HGroup>>(currentGroups);
				finalPlayers = new ArrayList<HPlayer>(players);
			}
			runs++;
		}
		printGroups(groupSize);		
		System.out.println("FINAL GROUP SCORE IS: "+getFinalScore());
		
	}
	
	public void createGroups(int rounds, int numGroups, int groupSize){
		populateNotGrouped();
		savePlayerState();
		for(int r = 0 ; r<rounds; r++){
			checkNotPlayedPlayers();
			int runs=0;
			int roundScore = Integer.MIN_VALUE;
			ArrayList<HGroup> temp = new ArrayList<HGroup>();
			while(runs<30){//30
				loadPlayerState();
				populateNotGrouped();
				if(groupSize==2){
					sizeTwoGroups(numGroups);
				}
				if(groupSize==3){
					sizeThreeGroups(numGroups);
				}
				if(groupSize==4){
					sizeFourGroups(numGroups);
				}
				if(getCurrentScore()>roundScore){
					temp = new ArrayList<HGroup>(groups);
					groups.clear();	
				}
				groups.clear();
				runs++;
			}
			currentGroups.add(temp);
			savePlayerState();
		}		
	}
	
	public void sizeTwoGroups(int numGroups){
		for(int g = 0; g<numGroups; g++){
			int test = 0;
			int bestScore = Integer.MIN_VALUE;
			HGroup gr = new HGroup(null,null,null,null); 
			while(test<5){//5	
				HPlayer p1 = getPlayerOne(notGrouped);
				HPlayer p2 = getPlayerTwo(notGrouped,p1);
				HGroup temp = new HGroup(p1,p2,null,null);
				if(temp.getGroupScore(2)>bestScore){
					bestScore=temp.getGroupScore(2);
					gr = new HGroup(p1,p2,null,null);
				}
				removeGroupTwo(p1,p2);
				test++;
			}
			addGroupTwo(gr.getPlayerOne(), gr.getPlayerTwo());						
			groups.add(gr);
		}
		
	}
	
	public void sizeThreeGroups(int numGroups){
		for(int g = 0; g<numGroups; g++){
			int test = 0;
			int bestScore = Integer.MIN_VALUE;
			HGroup gr = new HGroup(null,null,null,null); 
			while(test<200){//200
				HPlayer p1 = getPlayerOne(notGrouped);
				HPlayer p2 = getPlayerTwo(notGrouped,p1);
				HPlayer p3 = getPlayerThree(notGrouped,p1,p2);
				HGroup temp = new HGroup(p1,p2,p3,null);
				if(temp.getGroupScore(3)>bestScore){
					bestScore=temp.getGroupScore(3);
					gr = new HGroup(p1,p2,p3,null);
				}
				removeGroupThree(p1,p2,p3);
				test++;
			}
			addGroupThree(gr.getPlayerOne(), gr.getPlayerTwo(),gr.getPlayerThree());						
			groups.add(gr);
		}
	}
	
	public void sizeFourGroups(int numGroups){
		for(int g = 0; g<numGroups; g++){
			int test = 0;
			int bestScore = Integer.MIN_VALUE;
			HGroup gr = new HGroup(null,null,null,null); 
			while(test<200){//200
				HPlayer p1 = getPlayerOne(notGrouped);
				HPlayer p2 = getPlayerTwo(notGrouped,p1);
				HPlayer p3 = getPlayerThree(notGrouped,p1,p2);
				HPlayer p4 = getPlayerFour(notGrouped,p1,p2,p3);
				HGroup temp = new HGroup(p1,p2,p3,p4);
				if(temp.getGroupScore(4)>bestScore){
					bestScore=temp.getGroupScore(4);
					gr = new HGroup(p1,p2,p3,p4);
				}
				removeGroupFour(p1,p2,p3,p4);
				test++;
			}
			addGroupFour(gr.getPlayerOne(), gr.getPlayerTwo(),gr.getPlayerThree(),gr.getPlayerFour());						
			groups.add(gr);
		}
	}
	
	public HPlayer getPlayerOne(ArrayList<Integer> notGrouped){
		Random r = new Random();
		HPlayer p1 = players.get(notGrouped.get(r.nextInt(notGrouped.size())) - 1);
		notGrouped.remove(new Integer(p1.getPlayerNo()));
		return p1;
		
	}
	
	public void populateNotGrouped(){
		notGrouped = new ArrayList<Integer>();
		for (int i = 1; i < numPlayers+1; i++) {
			notGrouped.add(i);
		}
	}
	
	public HPlayer getPlayerTwo(ArrayList<Integer> notGrouped, HPlayer p1){
		int newPlayer = p1.getNewPlayer(notGrouped,numPlayers);
		if(newPlayer == 100){
			int score=Integer.MIN_VALUE;
			int playerNo=0;
			for(int i = 0; i<notGrouped.size(); i++){
				int temp = p1.getScoreWithNewPlayer(notGrouped.get(i));
				if(temp>score){
					score = temp;
					playerNo = notGrouped.get(i);
				}
			}
			HPlayer sp = players.get(playerNo-1);
			sp.addPlayedPlayer(p1.getPlayerNo());
			p1.addPlayedPlayer(sp.getPlayerNo());
			notGrouped.remove(new Integer(sp.getPlayerNo()));
			return sp;
		}
		else{
		HPlayer p = players.get(newPlayer-1);
		p.addPlayedPlayer(p1.getPlayerNo());
		p1.addPlayedPlayer(p.getPlayerNo());
		notGrouped.remove(new Integer(p.getPlayerNo()));
		return p;
		}
		
	}
	
	public HPlayer getPlayerThree(ArrayList<Integer> notGrouped, HPlayer p1, HPlayer p2){
		HPlayer p3 = null;
		int bestScore = Integer.MIN_VALUE;
		int playerNo = 0;
//		for (int i = 0; i < p2.getNotPlayedPlayers().size(); i++) {
//			if (p1.getNotPlayedPlayers().contains(p2.getNotPlayedPlayers().get(i))
//					&& notGrouped.contains(Integer.parseInt(p2.getNotPlayedPlayers().get(i)))) {
//				p3 =  players.get(Integer.parseInt(p2.getNotPlayedPlayers().get(i))-1);
//			}
//		}
		for(int i = 0;i<notGrouped.size(); i++){
			int temp = p1.getScoreWithNewPlayer(notGrouped.get(i))+p2.getScoreWithNewPlayer(notGrouped.get(i)) + players.get(notGrouped.get(i)-1).getScoreWithTwoNewPlayers(p1.getPlayerNo(), p2.getPlayerNo());
			if(temp>bestScore){
				bestScore=temp;
				playerNo = notGrouped.get(i);
			}	
		}
		p3 = players.get(playerNo-1);
		p1.addPlayedPlayer(p3.getPlayerNo());
		p2.addPlayedPlayer(p3.getPlayerNo());
		p3.addPlayedPlayer(p1.getPlayerNo());
		p3.addPlayedPlayer(p2.getPlayerNo());
		notGrouped.remove(new Integer(p3.getPlayerNo()));
		return p3;
		}
	
	public HPlayer getPlayerFour(ArrayList<Integer> notGrouped, HPlayer p1, HPlayer p2, HPlayer p3){
		HPlayer p4 = null;
		int bestScore = Integer.MIN_VALUE;
		int playerNo = 0;
//		for (int i = 0; i < p2.getNotPlayedPlayers().size(); i++) {
//			if (p1.getNotPlayedPlayers().contains(p2.getNotPlayedPlayers().get(i))
//					&& notGrouped.contains(Integer.parseInt(p2.getNotPlayedPlayers().get(i)))) {
//				p3 =  players.get(Integer.parseInt(p2.getNotPlayedPlayers().get(i))-1);
//			}
//		}
		for(int i = 0;i<notGrouped.size(); i++){
			int temp = p1.getScoreWithNewPlayer(notGrouped.get(i))+p2.getScoreWithNewPlayer(notGrouped.get(i))+p3.getScoreWithNewPlayer(notGrouped.get(i))+players.get(notGrouped.get(i)-1).getScoreWithThreeNewPlayers(p1.getPlayerNo(), p2.getPlayerNo(), p3.getPlayerNo());
			if(temp>bestScore){
				bestScore=temp;
				playerNo = notGrouped.get(i);
			}	
		}
		p4 = players.get(playerNo-1);
		p1.addPlayedPlayer(p4.getPlayerNo());
		p2.addPlayedPlayer(p4.getPlayerNo());
		p3.addPlayedPlayer(p4.getPlayerNo());
		p4.addPlayedPlayer(p1.getPlayerNo());
		p4.addPlayedPlayer(p2.getPlayerNo());
		p4.addPlayedPlayer(p3.getPlayerNo());
		notGrouped.remove(new Integer(p4.getPlayerNo()));
		return p4;
		}
		
	
	public void createPlayers(int numPlayers) {
		players = new ArrayList<HPlayer>();
		for (int i = 1; i < numPlayers + 1; i++) {
			HPlayer p = new HPlayer(i, numPlayers);
			players.add(p);
		}

	}
	
	public void savePlayerState() {
		for (int i = 0; i < players.size(); i++) {
			players.get(i).makeCopy();
		}
	}

	public void loadPlayerState() {
		for (int i = 0; i < players.size(); i++) {
			players.get(i).setPlayedPlayers();

		}
	}
	
	public void printGroups(int groupSize) {
		for (ArrayList<HGroup> temp : finalGroups) {
			for (HGroup g : temp) {
				System.out.println(g.returnGroup(groupSize));
			}
			System.out.println("--------------------------------------------------");
		}
	}
	
	public int getGroupsScore(int groupSize){
		int score = 0;
		for(ArrayList<HGroup>g1:currentGroups){
			for(HGroup g2 : g1){
				score += g2.getGroupScore(groupSize);
			}
		}
		return score;
	}
	
	public int getFinalScore(){
		int score = 0;
		for(HPlayer p1:finalPlayers){
			score += p1.getPlayerScore();	
			//System.out.println(p1.getPlayedPlayers());
		}
		return score;
	}
	
	public int getCurrentScore(){
		int score = 0;
		for(HPlayer p1:players){
			score += p1.getPlayerScore();	
			//System.out.println(p1.getPlayedPlayers());
		}
		return score;
	}
	
	public void addGroupTwo(HPlayer p1, HPlayer p2){
		p1.addPlayedPlayer(p2.getPlayerNo());
		p2.addPlayedPlayer(p1.getPlayerNo());
		notGrouped.remove(new Integer(p1.getPlayerNo()));
		notGrouped.remove(new Integer(p2.getPlayerNo()));
		
	}
	
	public void removeGroupTwo(HPlayer p1, HPlayer p2){
		p1.removePlayedPlayer(p2.getPlayerNo());
		p2.removePlayedPlayer(p1.getPlayerNo());
		notGrouped.add(new Integer(p1.getPlayerNo()));
		notGrouped.add(new Integer(p2.getPlayerNo()));
		
	}
	
	public void addGroupThree(HPlayer p1, HPlayer p2, HPlayer p3){
		p1.addPlayedPlayer(p2.getPlayerNo());
		p1.addPlayedPlayer(p3.getPlayerNo());
		p2.addPlayedPlayer(p1.getPlayerNo());
		p2.addPlayedPlayer(p3.getPlayerNo());
		p3.addPlayedPlayer(p1.getPlayerNo());
		p3.addPlayedPlayer(p2.getPlayerNo());
		notGrouped.remove(new Integer(p1.getPlayerNo()));
		notGrouped.remove(new Integer(p2.getPlayerNo()));
		notGrouped.remove(new Integer(p3.getPlayerNo()));
		
	}
	
	public void removeGroupThree(HPlayer p1, HPlayer p2, HPlayer p3){
		p1.removePlayedPlayer(p2.getPlayerNo());
		p1.removePlayedPlayer(p3.getPlayerNo());
		p2.removePlayedPlayer(p1.getPlayerNo());
		p2.removePlayedPlayer(p3.getPlayerNo());
		p3.removePlayedPlayer(p1.getPlayerNo());
		p3.removePlayedPlayer(p2.getPlayerNo());
		notGrouped.add(new Integer(p1.getPlayerNo()));
		notGrouped.add(new Integer(p2.getPlayerNo()));
		notGrouped.add(new Integer(p3.getPlayerNo()));		
	}
	
	public void addGroupFour(HPlayer p1, HPlayer p2, HPlayer p3, HPlayer p4){
		p1.addPlayedPlayer(p2.getPlayerNo());
		p1.addPlayedPlayer(p3.getPlayerNo());
		p1.addPlayedPlayer(p4.getPlayerNo());
		p2.addPlayedPlayer(p1.getPlayerNo());
		p2.addPlayedPlayer(p3.getPlayerNo());
		p2.addPlayedPlayer(p4.getPlayerNo());
		p3.addPlayedPlayer(p1.getPlayerNo());
		p3.addPlayedPlayer(p2.getPlayerNo());
		p3.addPlayedPlayer(p4.getPlayerNo());
		p4.addPlayedPlayer(p1.getPlayerNo());
		p4.addPlayedPlayer(p2.getPlayerNo());
		p4.addPlayedPlayer(p3.getPlayerNo());
		notGrouped.remove(new Integer(p1.getPlayerNo()));
		notGrouped.remove(new Integer(p2.getPlayerNo()));
		notGrouped.remove(new Integer(p3.getPlayerNo()));
		notGrouped.remove(new Integer(p4.getPlayerNo()));
		
	}
	
	public void removeGroupFour(HPlayer p1, HPlayer p2, HPlayer p3, HPlayer p4){
		p1.removePlayedPlayer(p2.getPlayerNo());
		p1.removePlayedPlayer(p3.getPlayerNo());
		p1.removePlayedPlayer(p4.getPlayerNo());
		p2.removePlayedPlayer(p1.getPlayerNo());
		p2.removePlayedPlayer(p3.getPlayerNo());
		p2.removePlayedPlayer(p4.getPlayerNo());
		p3.removePlayedPlayer(p1.getPlayerNo());
		p3.removePlayedPlayer(p2.getPlayerNo());
		p3.removePlayedPlayer(p4.getPlayerNo());
		p4.removePlayedPlayer(p1.getPlayerNo());
		p4.removePlayedPlayer(p2.getPlayerNo());
		p4.removePlayedPlayer(p3.getPlayerNo());
		notGrouped.add(new Integer(p1.getPlayerNo()));
		notGrouped.add(new Integer(p2.getPlayerNo()));
		notGrouped.add(new Integer(p3.getPlayerNo()));	
		notGrouped.add(new Integer(p4.getPlayerNo()));	
	}
	
	public void checkNotPlayedPlayers(){
		for(HPlayer p:players){
			//System.out.println(p.getNotPlayedPlayers());
			if(p.getNotPlayedPlayers().isEmpty()){
				System.out.println("FILL ME UP");
				p.fillNotPlayed(p.getPlayerNo(), numPlayers);
				//System.out.println(p.getNotPlayedPlayers());
			}
		}
	}
}
