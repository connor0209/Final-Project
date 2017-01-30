import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/* for this search the group size is currently fixed to 3 players*/

public class PairedSearch {
	private ArrayList<Player> players;
	private ArrayList<ArrayList<String>> savedPlayerState;
	private ArrayList<Group> groups;
	private ArrayList<ArrayList<Group>> printGroups;

	public static void main(String args[]) {
		PairedSearch bss = new PairedSearch();
		bss.search(5, 3, 3);
	}

	public void search(int rounds, int groupSize, int numGroups) {
		groups = new ArrayList<Group>();
		boolean test = false;
		while (test == false) {
			printGroups = new ArrayList<ArrayList<Group>>();
			createPlayers(groupSize * numGroups);
			test = createGroups(groupSize, numGroups);
			if(test!=false){
				for (int i = 0; i < rounds - 1; i++) {
					if (createGroups(groupSize, numGroups) == false && test == true) {
						test = false;
					}
				}
			}
		}
		printGroups(groupSize);
		printPlayedPlayers();

	}

	public void createPlayers(int numPlayers) {
		players = new ArrayList<Player>();
		for (int i = 1; i < numPlayers + 1; i++) {
			Player p = new Player(i, numPlayers);
			players.add(p);
		}

	}

	public boolean createGroups(int groupSize, int numGroups) {
		groups.clear();
		int maxRuns = 0;
		Random r = new Random();
		ArrayList<Integer> notGrouped = new ArrayList<Integer>();
		for (int i = 1; i < groupSize * numGroups + 1; i++) {
			notGrouped.add(i);
		}

		for (int i = 0; i < numGroups; i++) {
			if(maxRuns>10){
				return false;
			}

			Player p1 = players.get(notGrouped.get(r.nextInt(notGrouped.size())) - 1);
			notGrouped.remove(new Integer(p1.getPlayerNo()));
			if (p1.getNotPlayedPlayers().isEmpty() ) {
				p1.fillNotPlayed(p1.getPlayerNo(), groupSize * numGroups);
			}

			int player2 = p1.getNewPlayer(notGrouped, groupSize * numGroups);
			if (player2 == 100) {
				return false;
			}
			Player p2 = players.get(player2 - 1);
			notGrouped.remove(new Integer(p2.getPlayerNo()));
			if (p2.getNotPlayedPlayers().isEmpty()) {
				p2.fillNotPlayed(p2.getPlayerNo(), groupSize * numGroups);
				System.out.println("FILL");
			}
			p2.addPlayedPlayer(p1.getPlayerNo());

			if (groupSize > 2) {
				System.out.println(p2.getNotPlayedPlayers());
				int player3 = getThirdGroupMember(p1, p2, notGrouped, maxRuns);
				if (player3 == 100) {
					i = -1;
					maxRuns++;
					groups.clear();
					loadPlayerState();
					notGrouped = new ArrayList<Integer>();
					for (int l = 1; l < groupSize * numGroups + 1; l++) {
						notGrouped.add(l);
					}
				} else {
					Player p3 = players.get(player3 - 1);
					notGrouped.remove(new Integer(p3.getPlayerNo()));
					if (p3.getNotPlayedPlayers().isEmpty()) {
						p3.fillNotPlayed(p3.getPlayerNo(), groupSize * numGroups);
					}
					p3.addPlayedPlayer(p1.getPlayerNo());
					p3.addPlayedPlayer(p2.getPlayerNo());
					p1.addPlayedPlayer(p3.getPlayerNo());
					p2.addPlayedPlayer(p3.getPlayerNo());
//					if(groupSize == 4){
//						int player4 = getFourthGroupMember(p1,p2,p3,notGrouped);
//						if (player4 == 100) {
//							i = -1;
//							groups.clear();
//							loadPlayerState();
//							notGrouped = new ArrayList<Integer>();
//							for (int l = 1; l < groupSize * numGroups + 1; l++) {
//								notGrouped.add(l);
//							}
//						} else {
//							Player p4 = players.get(player4 - 1);
//							notGrouped.remove(new Integer(p4.getPlayerNo()));
//							if (p4.getNotPlayedPlayers().isEmpty()|| p4.getNotPlayedPlayers().size()<2 && maxRuns>5) {
//								p4.fillNotPlayed(p4.getPlayerNo(), groupSize * numGroups);
//							}
//							p3.addPlayedPlayer(p4.getPlayerNo());
//							p2.addPlayedPlayer(p4.getPlayerNo());
//							p1.addPlayedPlayer(p4.getPlayerNo());
//							p4.addPlayedPlayer(p1.getPlayerNo());
//							p4.addPlayedPlayer(p2.getPlayerNo());
//							p4.addPlayedPlayer(p3.getPlayerNo());
//							Group g4 = new Group(p1, p2, p3, p4);
//							groups.add(g4);
//						}
//					}
					if(groupSize==3){
					Group g3 = new Group(p1, p2, p3, null);
					groups.add(g3);
					}
				}
			}
			else {
				Group g2 = new Group(p1, p2, null, null);
				groups.add(g2);
			}
		}
		savePlayerState();
		ArrayList<Group> temp = new ArrayList<Group>(groups);
		printGroups.add(temp);
		groups.clear();
		return true;
	}

	public int getThirdGroupMember(Player p1, Player p2, ArrayList<Integer> notGrouped, int maxRuns) {
//		Random r = new Random();
//		if(maxRuns>8){
//			if(p1.getNotPlayedPlayers().size()>p2.getNotPlayedPlayers().size()){
//				return Integer.parseInt(p1.getNotPlayedPlayers().get(r.nextInt(p1.getNotPlayedPlayers().size())));
//			}
//			else{
//				System.out.println(p2.getNotPlayedPlayers());
//				return Integer.parseInt(p2.getNotPlayedPlayers().get(r.nextInt(p2.getNotPlayedPlayers().size())));
//			}
//		}
		
		for (int i = 0; i < p2.getNotPlayedPlayers().size(); i++) {
			if (p1.getNotPlayedPlayers().contains(p2.getNotPlayedPlayers().get(i))
					&& notGrouped.contains(Integer.parseInt(p2.getNotPlayedPlayers().get(i)))) {
				return Integer.parseInt(p2.getNotPlayedPlayers().get(i));
			}
		}
		return 100;
	}

	public int getFourthGroupMember(Player p1, Player p2, Player p3, ArrayList<Integer> notGrouped) {
		for (int i = 0; i < p2.getNotPlayedPlayers().size(); i++) {
			if (p1.getNotPlayedPlayers().contains(p2.getNotPlayedPlayers().get(i))
					&& notGrouped.contains(Integer.parseInt(p2.getNotPlayedPlayers().get(i)))
					&& p3.getNotPlayedPlayers().contains(p2.getNotPlayedPlayers().get(i))) {
				return Integer.parseInt(p2.getNotPlayedPlayers().get(i));
			}
		}
		return 100;
	}

	public void printGroups(int groupSize) {

		for (ArrayList<Group> temp : printGroups) {
			for (Group g : temp) {
				System.out.println(g.returnGroup(groupSize));
			}
			System.out.println("--------------------------------------------------");
		}
	}
	
	public void printPlayedPlayers() {

		for (Player temp : players) {
			System.out.println(temp.getPlayedPlayers());
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

}
