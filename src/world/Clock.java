package world;

public class Clock {
	private int hour=0;
	private int minute=0;
	public int getHour() {
		return hour;
	}
	public int getMinute() {
		return minute;
	}
	
	@Override
	public String toString() {
		return String.format("%03d", hour) + ":" + String.format("%02d", minute);
	}
	
	public void tick(){
		minute+=10;
		if(minute==60){
			minute=0;
			hour++;
		}
	}
}
