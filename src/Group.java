
public class Group {
	
	private Player one,two,three,four;
	
	public Group(Player i, Player j, Player k, Player l){
		one = i;
		two = j;
		three = k;
		four = l;
	}
	
	public Player getPlayerOne(){
		return one;
	}
	
	public Player getPlayerTwo(){
		return two;
	}
	
	public Player getPlayerThree(){
		return three;
	}
	public Player getPlayerFour(){
		return four;
	}
	public String returnGroup(int groupSize){
		String group = "";
		if(groupSize==2){
			group = Integer.toString(one.getPlayerNo())+" "+Integer.toString(two.getPlayerNo());
		}
		else if(groupSize==3){
			group = Integer.toString(one.getPlayerNo())+" "+Integer.toString(two.getPlayerNo())+" "+Integer.toString(three.getPlayerNo());
		}
		else if(groupSize==4){
			group = Integer.toString(one.getPlayerNo())+" "+Integer.toString(two.getPlayerNo())+" "+Integer.toString(three.getPlayerNo())+" "+Integer.toString(four.getPlayerNo());
		}
		
		return group;
	}
}
