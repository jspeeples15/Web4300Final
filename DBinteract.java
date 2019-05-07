import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.Collection;
import java.util.Random;
public class DBinteract {
	//creates account with username and password, returns the id of that account.
	//that id an be used for session tracking as we continue.
	public static int createAccount(String username, String password){
		String query = "insert into users values (";
		int id = getMaxId();
		query += id + " , \"";
		query += username + "\" , \"";
		query += password + "\");";
		DatabaseAccessInterface.create(query);
		DatabaseAccessInterface.disconnect();
		return id;
	}
	
	//returns the  highest id of a user account + 1
	//to be used to generate the new ID when an account is created.
	public static int getMaxId(){
		int id = 1;
		String query = "select MAX(id) from users ";
		ResultSet rs = DatabaseAccessInterface.retrieve(query);
		
		
		try {
			while(rs.next())id = rs.getInt(1);
			int done = id + 1;
			DatabaseAccessInterface.disconnect();
			return done;
		} catch (SQLException e) {
			DatabaseAccessInterface.disconnect();
			return -1;
		}
	}
	
	//used to confirm the login info of a user
	//returns the userID if successful
	//returns 0 if failed login
	//returns -1 if multiple accounts match, this would be a big error and idk how to resolve it if it happens
	public static int login(String username, String password){
		int success = 0;
		int numResults = 0;
		
		String query = "select * from users where username = \'" + username + "\' and pw = \'" + password + "\'" ;
		ResultSet rs = DatabaseAccessInterface.retrieve(query);
		try {
			
			while(rs.next()){
				numResults++;
				success = rs.getInt("id");			}
		} catch (SQLException e) {
			DatabaseAccessInterface.disconnect();
			return -1;
		}
		if(numResults<1){
			DatabaseAccessInterface.disconnect();
			return 0;
		}
		else if(numResults>1){
			DatabaseAccessInterface.disconnect();
			return -1;
		}
		else{
			DatabaseAccessInterface.disconnect();
			return success;
		}
	}
	
	//this gets the entirety of the existing likes for the user
	//returns array of 4 vectors 0.movies 1.actors 2.directors 3.writers
	public static Object[] getAllLikes(int user_id){
		Object[] arr = new Object[4];
		arr[0] = getLikesMovies(user_id);
		arr[1] = getLikesActors(user_id);
		arr[2] = getLikesDirectors(user_id);
		arr[3] = getLikesWriters(user_id);

		
		return arr;
	}
	
	//this allows a user to like a specific actor
	public static int likeActor(int user_id, int actor_id){
		String query = "insert into useractors values (";
		query += user_id + "," + actor_id + ");";
		int q = DatabaseAccessInterface.create(query);
		DatabaseAccessInterface.disconnect();
		return q;
	}
	
	//allows the user to unlike a specific actor
	public static int unlikeActor(int user_id, int actor_id){
		String query = "delete from useractors where user_id = ";
		query += user_id + " AND actor_id = " + actor_id;
		int q = DatabaseAccessInterface.delete(query);	
		DatabaseAccessInterface.disconnect();
		return q;
	}
	
	//returns an actor vector, full of actor objects
	//these actors are the ones currently liked by the user
	public static Vector<actor> getLikesActors(int user_id){
		Vector<actor> results = new Vector<actor>();
		boolean started = false;
		String query = "select a.* from users u, actors a, useractors x where u.id = x.user_id and a.actorid = x.actor_id and u.id = " + user_id;
		ResultSet rs = DatabaseAccessInterface.retrieve(query);
		actor a = null;
		actor prevA = null;
		try {
			
			while(rs.next()){
				a = new actor(rs.getInt(1),rs.getString(2),rs.getString(3));
				if(started && !a.displayName.equalsIgnoreCase(prevA.displayName))results.add(a);
				else if(!started)results.add(a);
				prevA = a;
				started = true;
			}
			
		} catch (SQLException e) {
			DatabaseAccessInterface.disconnect();
			return null;
		}
		DatabaseAccessInterface.disconnect();
		return results;

			
	}
	//user can like a specific movie using this
	public static int likeMovie(int user_id, int movie_id){
		String query = "insert into usermovies values (";
		query += user_id + "," + movie_id + ");";
		int q = DatabaseAccessInterface.create(query);
		DatabaseAccessInterface.disconnect();
		return q;
	}
	
	//user can unlike a movie from their list
	public static int unlikeMovie(int user_id, int movie_id){
		String query = "delete from usermovies where user_id = ";
		query += user_id + " AND movie_id = " + movie_id + ");";
		int q = DatabaseAccessInterface.delete(query);	
		DatabaseAccessInterface.disconnect();
		return q;
	}
	
	//returns the list of user liked movies
	public static Vector<movie> getLikesMovies(int user_id){
		Vector<movie> results = new Vector<movie>();
		boolean started = false;
		String query = "select m.* from users u, movies m, usermovies x where u.id = x.user_id and m.movieid = x.movie_id and user_id = " + user_id;
		ResultSet rs = DatabaseAccessInterface.retrieve(query);
		movie m = null;
		movie prevM = new movie();
		try {
			
			while(rs.next()){
				m = new movie(rs.getInt(1),rs.getString(2),rs.getString(3));
				if(started && !m.displayTitle.equalsIgnoreCase(prevM.displayTitle))results.add(m);
				else if(!started)results.add(m);
				prevM = m;
				started = true;
			}
			
		} catch (SQLException e) {
			return null;
		}
		DatabaseAccessInterface.disconnect();
		return results;

			
	}
	
	//likes a specific director for the user
	public static int likeDirector(int user_id, int director_id){
		String query = "insert into userdirectors values (";
		query += user_id + "," + director_id + ");";
		int q = DatabaseAccessInterface.create(query);
		DatabaseAccessInterface.disconnect();
		return q;
	}
	
	//unlikes a director from the user's list
	public static int unlikeDirector(int user_id, int director_id){
		String query = "delete from userdirectors where user_id = ";
		query += user_id + " AND director_id = " + director_id;
		int q = DatabaseAccessInterface.delete(query);	
		DatabaseAccessInterface.disconnect();
		return q;
	}
	
	
	//returns the list of all liked directors by a user
	public static Vector<director> getLikesDirectors(int user_id){
		Vector<director> results = new Vector<director>();
		boolean started = false;
		String query = "select d.* from users u, directors d, userdirectors x where u.id = x.user_id and d.directorid = x.director_id and u.id = " + user_id;
		ResultSet rs = DatabaseAccessInterface.retrieve(query);
		director d = null;
		director prevD = new director();
		try {
			
			while(rs.next()){
				d = new director(rs.getInt(1),rs.getString(2));
				if(started && !d.displayName.equalsIgnoreCase(prevD.displayName))results.add(d);
				else if(!started)results.add(d);
				prevD = d;
				started = true;
			}
			
		} catch (SQLException e) {
			return null;
		}
		DatabaseAccessInterface.disconnect();
		return results;

			
	}
	
	//likes writers for a user
	public static int likeWriter(int user_id, int writer_id){
		String query = "insert into userwriters values (";
		query += user_id + "," + writer_id + ");";
		int q = DatabaseAccessInterface.create(query);
		DatabaseAccessInterface.disconnect();
		return q;
	}
	
	//unlikes writers for a user
	public static int unlikeWriter(int user_id, int writer_id){
		String query = "delete from userwriters where user_id = ";
		query += user_id + " AND writer_id = " + writer_id;
		int q = DatabaseAccessInterface.delete(query);	
		DatabaseAccessInterface.disconnect();
		return q;
	}
	
	//gets a list of user-liked writers
	public static Vector<writer> getLikesWriters(int user_id){
		Vector<writer> results = new Vector<writer>();
		boolean started = false;
		String query = "select w.* from users u, writers w, userwriters x where u.id = x.user_id and w.writerid = x.writer_id and u.id = " + user_id;
		ResultSet rs = DatabaseAccessInterface.retrieve(query);
		writer w = null;
		writer prevW = null;
		try {
			
			while(rs.next()){
				w = new writer(rs.getInt(1),rs.getString(2));
				if(started && !w.displayName.equalsIgnoreCase(prevW.displayName))results.add(w);
				else if(!started)results.add(w);
				prevW = w;
				started = true;
			}
			
		} catch (SQLException e) {
			return null;
		}
		DatabaseAccessInterface.disconnect();
		return results;
		

			
	}
	
	//returns a list of movies, making the search condition, which would be the title 
	public static Vector<movie> searchMoviesByTitle(String search, int limit){
		boolean started = false;
		Vector<movie> results = new Vector<movie>();
		String query = "select distinct * from movies where title like '_" + search + "%' limit " + limit + ";";
		ResultSet rs = DatabaseAccessInterface.retrieve(query);
		movie m = null;
		movie prevM = null;
		try {
			
			while(rs.next()){
				m = new movie(rs.getInt(1),rs.getString(2),rs.getString(3));
				if(started && !m.displayTitle.equalsIgnoreCase(prevM.displayTitle))results.add(m);
				else if(!started)results.add(m);
				prevM = m;
				started = true;
			}
			
		} catch (SQLException e) {
			return null;
		}
		DatabaseAccessInterface.disconnect();
		return results;
	}
	public static String convertName(String name){
		int spaceLoc = -1;
		boolean hasComma = false;
		for(int i=0;i<name.length();i++){
			if(name.charAt(i) == ' '){
				spaceLoc = i;
			}
			if(name.charAt(i)==',')hasComma = true;
			
		}
		if(spaceLoc == -1 || hasComma) return name;
		name = name.substring(spaceLoc+1) + ", " + name.substring(0, spaceLoc);
		return name;
	}
	
	//returns a list of movies, searching for a specific director
	public static Vector<movie> searchMoviesByDirector(String name, int limit){
		boolean started = false;
		Vector<movie> results = new Vector<movie>();
		String query = "select distinct m.* from movies m, directors d, movies2directors x where m.movieid = x.movieid and d.directorid = x.directorid and d.name like '%" + name + "%' limit " + limit + ";";
		ResultSet rs = DatabaseAccessInterface.retrieve(query);
		movie m = null;
		movie prevM = null;
		try {
			
			while(rs.next()){
				m = new movie(rs.getInt(1),rs.getString(2),rs.getString(3));
				if(started && !m.displayTitle.equalsIgnoreCase(prevM.displayTitle))results.add(m);
				else if(!started)results.add(m);
				prevM = m;
				started = true;
			}
			
		} catch (SQLException e) {
			return null;
		}
		DatabaseAccessInterface.disconnect();
		return results;
	}
	//returns a list of movies, written by a certain writer
	public static Vector<movie> searchMoviesByWriter(String name, int limit){
		boolean started = false;
		Vector<movie> results = new Vector<movie>();
		String query = "select distinct m.* from movies m, writers w, movies2writers x where m.movieid = x.writerid and w.writerid = x.writerid and w.name like '%" + name + "%' limit " + limit + ";";
		ResultSet rs = DatabaseAccessInterface.retrieve(query);
		movie m = null;
		movie prevM = null;
		try {
			
			while(rs.next()){
				m = new movie(rs.getInt(1),rs.getString(2),rs.getString(3));
				if(started && !m.displayTitle.equalsIgnoreCase(prevM.displayTitle))results.add(m);
				else if(!started)results.add(m);
				prevM = m;
				started = true;
			}
			
		} catch (SQLException e) {
			return null;
		}
		DatabaseAccessInterface.disconnect();
		return results;
	}
	
	//returns a list of movies featuring a specific actor who was searched for
	public static Vector<movie> searchMoviesByActor(String name, int limit){
		boolean started = false;
		Vector<movie> results = new Vector<movie>();
		String query = "select distinct m.* from movies m, actors a, movies2actors x where m.movieid = x.movieid and a.actorid = a.actorid and a.name like '%" + name + "%' limit " + limit + ";";
		ResultSet rs = DatabaseAccessInterface.retrieve(query);
		movie m = null;
		movie prevM = null;
		try {
			
			while(rs.next()){
				m = new movie(rs.getInt(1),rs.getString(2),rs.getString(3));
				if(started && !m.displayTitle.equalsIgnoreCase(prevM.displayTitle))results.add(m);
				else if(!started)results.add(m);
				prevM = m;
				started = true;
			}
			
		} catch (SQLException e) {
			return null;
		}
		DatabaseAccessInterface.disconnect();
		return results;
	}
	
	//search directors by their name
	public static Vector<director> searchDirectors(String name){
		name = convertName(name);
		boolean started = false;
		Vector<director> results = new Vector<director>();
		String query = "select distinct * from directors where name like '%" + name + "%' limit 1000;";
		ResultSet rs = DatabaseAccessInterface.retrieve(query);
		director d = null;
		director prevD = null;
		try {
			
			while(rs.next()){
				d = new director(rs.getInt(1),rs.getString(2));
				if(started && !d.displayName.equalsIgnoreCase(prevD.displayName))results.add(d);
				else if(!started)results.add(d);
				prevD = d;
				started = true;
			}
			
		} catch (SQLException e) {
			return null;
		}
		DatabaseAccessInterface.disconnect();
		return results;
	}
	
	//search actors by their name
	public static Vector<actor> searchActors(String name){
		name = convertName(name);
		boolean started = false;
		Vector<actor> results = new Vector<actor>();
		String query = "select distinct * from actors where name like '%" + name + "%' limit 1000;";
		ResultSet rs = DatabaseAccessInterface.retrieve(query);
		actor a = null;
		actor prevA = new actor();
		try {
			
			while(rs.next()){
				a = new actor(rs.getInt(1),rs.getString(2), rs.getString(3));
				if(started && !a.displayName.equalsIgnoreCase(prevA.displayName))results.add(a);
				else if(!started)results.add(a);
				prevA = a;
				started = true;
			}
			
		} catch (SQLException e) {
			return null;
		}
		DatabaseAccessInterface.disconnect();
		return results;
	}
	
	
	//search writers by their name
	public static Vector<writer> searchWriters(String name){
		name = convertName(name);
		boolean started = false;
		Vector<writer> results = new Vector<writer>();
		String query = "select distinct * from writers where name like '%" + name + "%' limit 1000;";
		ResultSet rs = DatabaseAccessInterface.retrieve(query);
		writer w = null;
		writer prevW = new writer();
		try {
			
			while(rs.next()){
				w = new writer(rs.getInt(1),rs.getString(2));
				if(started && !w.displayName.equalsIgnoreCase(prevW.displayName))results.add(w);
				else if(!started)results.add(w);
				prevW = w;
				started = true;
			}
			
		} catch (SQLException e) {
			return null;
		}
		DatabaseAccessInterface.disconnect();
		return results;
	}
	
	//search for writers by the movies/tv shows they've written
	public static Vector<writer> searchWritersByMovie(int movie_id){
		boolean started = false;
		Vector<writer> results = new Vector<writer>();
		String query = "select w.* from movies m, writers w, movies2writers x where m.movieid = x.movieid and w.writerid = x.writerid and m.movieid = " + movie_id;
		ResultSet rs = DatabaseAccessInterface.retrieve(query);
		writer w = null;
		writer prevW = new writer();
		try {
			
			while(rs.next()){
				w = new writer(rs.getInt(1),rs.getString(2));
				if(started && !w.displayName.equalsIgnoreCase(prevW.displayName))results.add(w);
				else if(!started)results.add(w);
				prevW = w;
				started = true;
			}
			
		} catch (SQLException e) {
			return null;
		}
		DatabaseAccessInterface.disconnect();
		return results;
	}
	
	//search for directors based upon a certain movie
	public static Vector<director> searchDirectorsByMovie(int movie_id){
		boolean started = false;
		Vector<director> results = new Vector<director>();
		String query = "select d.* from movies m, directors d, movies2directors x where m.movieid = x.movieid and d.directorid = x.directorid and m.movieid = " + movie_id;
		ResultSet rs = DatabaseAccessInterface.retrieve(query);
		director d= null;
		director prevD = new director();
		try {
			
			while(rs.next()){
				d = new director(rs.getInt(1),rs.getString(2));
				if(started && !d.displayName.equalsIgnoreCase(prevD.displayName))results.add(d);
				else if(!started)results.add(d);
				prevD = d;
				started = true;
			}
			
		} catch (SQLException e) {
			return null;
		}
		DatabaseAccessInterface.disconnect();
		return results;
	}
	
	//search for the actors in a specific movies
	public static Vector<actor> searchActorsByMovie(int movie_id){
		boolean started = false;
		Vector<actor> results = new Vector<actor>();
		String query = "select a.* from movies m, actors a, movies2actors x where m.movieid = x.movieid and a.actorid = x.actorid and m.movieid = " + movie_id;
		ResultSet rs = DatabaseAccessInterface.retrieve(query);
		actor a = null;
		actor prevA = new actor();
		try {
			
			while(rs.next()){
				a = new actor(rs.getInt(1),rs.getString(2),rs.getString(3));
				if(started && !a.displayName.equalsIgnoreCase(prevA.displayName))results.add(a);
				else if(!started)results.add(a);
				prevA = a;
				started = true;
			}
			
		} catch (SQLException e) {
			return null;
		}
		DatabaseAccessInterface.disconnect();
		return results;
	}
	public static Vector<movie> searchMoviesByKeyword(String keyword){
		boolean started = false;
		Vector<movie> results = new Vector<movie>();
		String query = "select distinct  * from movies m, keywords k where m.movieid = k.movieid and k.keyword like '%" + keyword + "%';";
		ResultSet rs = DatabaseAccessInterface.retrieve(query);
		movie m = null;
		movie prevM = null;
		try {
			
			while(rs.next()){
				m = new movie(rs.getInt(1),rs.getString(2),rs.getString(3));
				if(started && !m.displayTitle.equalsIgnoreCase(prevM.displayTitle))results.add(m);
				else if(!started)results.add(m);
				prevM = m;
				started = true;
			}
			
		} catch (SQLException e) {
			return null;
		}
		DatabaseAccessInterface.disconnect();
		return results;	}
	
	//returns all the keywords of the movies liked by a user
	public static Vector<keyword> getUserKeywords(int user_id){
		Vector<keyword> results = new Vector<keyword>();
		boolean started = false;
		String query = "select distinct k.keyword from usermovies u, keywords k where u.movie_id = k.movieid and u.user_id = " + user_id;
		ResultSet rs = DatabaseAccessInterface.retrieve(query);
		keyword k = null;
		keyword prevK = null;
		try {
			
			while(rs.next()){
				k = new keyword(rs.getInt(1),rs.getString(2));
				if(started && !k.word.equalsIgnoreCase(prevK.word))results.add(k);
				else if(!started)results.add(k);
				prevK = k;
				started = true;
			}
			
		} catch (SQLException e) {
			return null;
		}
		DatabaseAccessInterface.disconnect();
		return results;
		
		
	}
	
	//suggests up to 100 movies, based on the various likes of the user
	public static Vector<movie> getSuggestions(int user_id){
		Vector<movie> result = new Vector<movie>();
		Vector<movie> a = suggestActor(user_id);
		Vector<movie> b = suggestDirector(user_id);
		Vector<movie> c = suggestWriter(user_id);
		Vector<movie> d = suggestMovie(user_id);
		if (a!=null){
			result.addAll(a);
		}
		if (b!=null){
			result.addAll(b);
		}
		if (c!=null){
			result.addAll(c);
		}
		if (d!=null){
			result.addAll(d);
		}
		//result = scoreMovies(user_id, result);
		return result;
	}
	
	//returns one random movie that would be suggested
	public static Vector<movie> surpriseMe(int user_id){
		Vector<movie> results = new Vector<movie>();
		Random rand = new Random();
		int r = rand.nextInt(3);
		Vector<actor> likesA = getLikesActors(user_id);
		Vector<director> likesD = getLikesDirectors(user_id);
		Vector<writer> likesW = getLikesWriters(user_id);
		if(likesA.size()<=0 && likesD.size()<=0 && likesW.size()<=0)return results;
		if(r==0 && likesA.size()<=0)r = 1;
		if(r==1 && likesD.size()<=0)r = 2;
		if(r==2 && likesW.size()<=0)r = 0;
		if(r==0){
			Vector<movie> temp = suggestActor(user_id);
			if(temp!=null){
				results = temp;
			}
		}
		else if(r==1){
			Vector<movie> temp = suggestDirector(user_id);
			if(temp!=null){
				results = temp;
			}
		}
		else if(r==2){
			Vector<movie> temp = suggestWriter(user_id);
			if(temp!=null){
				results = temp;
			}
			
		}
		if(results.isEmpty() ){
			return surpriseMe(1);
		}
		int z = rand.nextInt(results.size());
		
		movie m = results.get(z);
		Vector<movie> result = new Vector<movie>();
		result.add(m);
		return result;
	}
	//suggests movies based on actors liked by a user
	public static Vector<movie> suggestActor(int user_id){
		boolean started = false;
		Vector<movie> results = new Vector<movie>();
		Vector<actor> likes = getLikesActors(user_id);
		if(likes.size()<=0)return null;
		Random rand = new Random();
		int r = rand.nextInt(likes.size());
		actor a = likes.get(r);
		String query = "select m.* from movies m, actors a, movies2actors x where a.name like  '%" +a.name + "%' and m.movieid = x.movieid and a.actorid = x.actorid order by rand() limit 10";		
		ResultSet rs = DatabaseAccessInterface.retrieve(query);
		movie m = null;
		movie prevM = null;
		try {
			
			while(rs.next()){
				m = new movie(rs.getInt(1),rs.getString(2),rs.getString(3));
				if(started && !m.displayTitle.equalsIgnoreCase(prevM.displayTitle))results.add(m);
				else if(!started)results.add(m);
				prevM = m;
				started = true;
			}
			
		} catch (SQLException e) {
			return null;
		}
		DatabaseAccessInterface.disconnect();
		return results;
		
	}
	//suggest movies based on director's liked by a user
	public static Vector<movie> suggestDirector(int user_id){
		boolean started = false;
		Vector<movie> results = new Vector<movie>();
		Vector<director> likes = getLikesDirectors(user_id);
		if(likes.size()<=0)return null;
		Random rand = new Random();
		int r = rand.nextInt(likes.size());
		director d = likes.get(r);
		String query = "select m.* from movies m, directors d, movies2directors x where d.name like  '%" + d.name + "%' and m.movieid = x.movieid and d.directorid = x.directorid order by rand() limit 10";	
		ResultSet rs = DatabaseAccessInterface.retrieve(query);
		movie m = null;
		movie prevM = null;
		try {
			
			while(rs.next()){
				m = new movie(rs.getInt(1),rs.getString(2),rs.getString(3));
				if(started && !m.displayTitle.equalsIgnoreCase(prevM.displayTitle))results.add(m);
				else if(!started)results.add(m);
				prevM = m;
				started = true;
			}
			
		} catch (SQLException e) {
			return null;
		}
		DatabaseAccessInterface.disconnect();
		return results;
		
	}
	
	//suggest movies based upon writers liked by a user
	public static Vector<movie> suggestWriter(int user_id){
		boolean started = false;
		Vector<movie> results = new Vector<movie>();
		Vector<writer> likes = getLikesWriters(user_id);
		if(likes.size()<=0)return null;
		Random rand = new Random();
		int r = rand.nextInt(likes.size());
		writer w = likes.get(r);
		String query = "select m.* from movies m, writers w, movies2writers x where w.name like  '%" + w.name + "%' and m.movieid = x.movieid and w.writerid = x.writerid order by rand() limit 10";	
		ResultSet rs = DatabaseAccessInterface.retrieve(query);
		movie m = null;
		movie prevM = null;
		try {
			
			while(rs.next()){
				m = new movie(rs.getInt(1),rs.getString(2),rs.getString(3));
				if(started && !m.displayTitle.equalsIgnoreCase(prevM.displayTitle))results.add(m);
				else if(!started)results.add(m);
				prevM = m;
				started = true;
			}
			
		} catch (SQLException e) {
			return null;
		}
		DatabaseAccessInterface.disconnect();
		return results;
		
	}
	//suggests movies similar to liked movie by the user
	public static Vector<movie> suggestMovie(int user_id){
		Vector<movie> results = new Vector<movie>();
		Vector<movie> likes = getLikesMovies(user_id);
		if(likes.size()<=0){
			return null;
		}
		Random rand = new Random();
		int z = rand.nextInt(likes.size());
		movie m = likes.elementAt(z);
		int r = rand.nextInt(3);
		if(r==0){
			Vector<actor> actors = searchActorsByMovie(m.id);
			if(actors.size()<=0)return results;
			actor a = actors.elementAt(rand.nextInt(actors.size()));
			results.addAll(searchMoviesByActor(a.name, 10));
			
		}
		else if(r==1){
			Vector<director> directors = searchDirectorsByMovie(m.id);
			if(directors.size()<=0)return results;
			director d = directors.elementAt(rand.nextInt(directors.size()));
			results.addAll(searchMoviesByDirector(d.name,10));
		}
		else if(r==2){
			Vector<writer> writers = searchWritersByMovie(m.id);
			if(writers.size() <= 0)return results;
			writer w = writers.elementAt(rand.nextInt(writers.size()));
			results.addAll(searchMoviesByWriter(w.name,10));
			
		}
		return results;
		
	}
	//scores the recommended movies (takes a while)
	public static Vector<movie> scoreMovies(int user_id, Vector<movie> movies){
		for(int i=0;i<movies.size();i++){
			movies.elementAt(i).setScore(scoreMovie(user_id, movies.elementAt(i)));
		}
		return movies;
	}
	//scores a specific movies
	public static int scoreMovie(int user_id, movie m){
		int score = 0;
		score += actorScore(user_id,m);
		score += directorScore(user_id, m);
		score += writerScore(user_id, m);
		return score;
	}
	//adds to the score by actors in common
	public static int actorScore(int user_id, movie m){
		int z = 0;
		Vector<actor> likes = getLikesActors(user_id);
		Vector<actor> inMovies = searchActorsByMovie(m.id);
		
		for(int i=0;i<likes.size();i++){
			actor a = likes.get(i);
			for(int j=0;j<inMovies.size();j++){
				actor b = inMovies.get(j);
				if(a.id == b.id)z++;
			}
		}
		
		return z;
	}
	
	// adds to the score by directors in common
	public static int directorScore(int user_id, movie m){
		int z = 0;
		Vector<director> likes = getLikesDirectors(user_id);
		Vector<director> inMovies = searchDirectorsByMovie(m.id);
		
		for(int i=0;i<likes.size();i++){
			director a = likes.get(i);
			for(int j=0;j<inMovies.size();j++){
				director b = inMovies.get(j);
				if(a.id == b.id)z++;
			}
		}
		
		return z;
	}
	
	//adds to the score by writers in common
	public static int writerScore(int user_id, movie m){
		int z = 0;
		Vector<writer> likes = getLikesWriters(user_id);
		Vector<writer> inMovies = searchWritersByMovie(m.id);
		
		for(int i=0;i<likes.size();i++){
			writer a = likes.get(i);
			for(int j=0;j<inMovies.size();j++){
				writer b = inMovies.get(j);
				if(a.id == b.id)z++;
			}
		}
		
		return z;	
	}
}
