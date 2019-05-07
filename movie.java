
public class movie {
	int id;
	String title;
	String displayTitle;
	String year; //I know this is weird, but thats how the DB saves it
	private int score;
	public movie(){}
	public movie(int id, String title, String year){
		this.id = id;
		this.title = title;
		this.displayTitle = getDisplayTitle(title);
		this.year = year;
		this.score = 0;
	}
	public int getScore(){
		return this.score;
	}
	public void setScore(int score){
		this.score = score;
	}
	public String getDisplayTitle(String title){
		String displayTitle = "";
		int numQuotes = 0;
		for(int i = 0;i<title.length();i++){
			if(title.charAt(i) == '"'){
				numQuotes++;
				continue;
			}
			if(numQuotes == 2)break;
			displayTitle += title.charAt(i);
			
		}
		return displayTitle;
	}
}
