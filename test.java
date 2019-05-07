import java.util.Vector;

public class test {

	public static void main(String[] args) {
		//movies
		//1555122 monster house
		//1210799 jurassic world
		//552026  date night
		//80416 after hours
		
		//actors
		// 434839 george clooney
		// 1964390 seth rogen
		// 3189096 angeline jolie

		//directors
		// 198480 james cameron
		// 61677  cameron hap

		
		//writers 
		// 596414 harris wittles
		// 435170 amy phoeler

		//testAllLikes();
		DBinteract.likeWriter(3, 596414);
		Vector<movie> ml = DBinteract.surpriseMe(3);
		if(ml.isEmpty()){
			System.out.println("ml is empty");
		}
		System.out.println("MOVIES");
		for(int i=0;i<ml.size();i++){
			movie m = ml.get(i);
			System.out.println("id: " + m.id + "\t\t name: " + m.displayTitle);
		}
		
	}
	public static void testAllLikes(){
		int numIncluded = 0;
		
		Object[] arr = DBinteract.getAllLikes(1);	
		@SuppressWarnings("unchecked")
		Vector<movie> ml = (Vector<movie>) arr[0];
		@SuppressWarnings("unchecked")

		Vector<actor> al = (Vector<actor>) arr[1];
		@SuppressWarnings("unchecked")

		Vector<director> dl = (Vector<director>) arr[2];
		@SuppressWarnings("unchecked")

		Vector<writer> wl = (Vector<writer>) arr[3];
		
		System.out.println("MOVIES");
		for(int i=0;i<ml.size();i++){
			movie m = ml.get(i);
			System.out.println("id: " + m.id + "\t\t name: " + m.displayTitle);
			numIncluded ++;
		}
		System.out.println("ACTORS");
		for(int i=0;i<al.size();i++){
			actor m = al.get(i);
			System.out.println("id: " + m.id + "\t\t name: " + m.displayName);
			numIncluded ++;
		}System.out.println("DIRECTORS");
		for(int i=0;i<dl.size();i++){
			director m = dl.get(i);
			System.out.println("id: " + m.id + "\t\t name: " + m.displayName);
			numIncluded ++;
		}
		System.out.println("WRITERS");
		for(int i=0;i<wl.size();i++){
			writer m = wl.get(i);
			System.out.println("id: " + m.id + "\t\t name: " + m.displayName);
			numIncluded ++;
		}
		System.out.println("num: " + numIncluded);
	}

}
