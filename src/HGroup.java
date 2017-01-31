
public class HGroup {
	
	private HPlayer one,two,three,four;
	
	public HGroup(HPlayer i, HPlayer j, HPlayer k, HPlayer l){
		one = i;
		two = j;
		three = k;
		four = l;
	}
	
	public HPlayer getPlayerOne(){
		return one;
	}
	
	public HPlayer getPlayerTwo(){
		return two;
	}
	
	public HPlayer getPlayerThree(){
		return three;
	}
	public HPlayer getPlayerFour(){
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
		else if(groupSize==5){
			if(getPlayerFour()!=null){
				group = Integer.toString(one.getPlayerNo())+" "+Integer.toString(two.getPlayerNo())+" "+Integer.toString(three.getPlayerNo())+" "+Integer.toString(four.getPlayerNo());
			}
			else{
				group = Integer.toString(one.getPlayerNo())+" "+Integer.toString(two.getPlayerNo())+" "+Integer.toString(three.getPlayerNo());
			}
		}
		
		return group;
	}
	
	public int getGroupScore(int groupSize){
		int score=0;
		if(groupSize==2){
			score = one.getPlayerScore() + two.getPlayerScore();
		}
		else if(groupSize==3){
			score = one.getPlayerScore() + two.getPlayerScore() + three.getPlayerScore();
		}
		else if(groupSize==4){
			score = one.getPlayerScore() + two.getPlayerScore() + three.getPlayerScore() + four.getPlayerScore();
		}
		
		return score;
	}
}
