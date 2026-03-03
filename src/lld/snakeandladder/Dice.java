package lld.snakeandladder;

public class Dice {
	int minvalue;
	int maxValue;

	public Dice(int minvalue, int maxValue) {
		this.minvalue = minvalue;
		this.maxValue = maxValue;
	}

	int roll(){
		return (int) (Math.random()*(maxValue-minvalue+1)+minvalue);
	}
}
