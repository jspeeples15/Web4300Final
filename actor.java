
public class actor {
	int id;
	String name;
	String displayName;
	String sex;
	public actor(){
		id = -1;
		name = "";
		displayName = "";
		sex = "M";
	}
	public actor(int id, String name, String sex){
		this.id = id;
		this.name = name;
		this.displayName = getDisplayName(name);
		if(sex.equalsIgnoreCase("f") || sex.equalsIgnoreCase("m"))this.sex = sex;
		else sex = null;
		
	}
	public String getDisplayName(String name){
		int fstart = -1;
		int fend = -1;
		int comma = -1;
		boolean found = false;
		boolean space = false;
		String displayName = "";
		for(int i=0;i<name.length();i++){
			if(found && name.charAt(i) == ' '){
				fend = i;
				space = true;
				break;
			}
			if(name.charAt(i) == ','){
				found = true;
				comma = i;
				fstart = i+1;
				if(i+1 != name.length() && name.charAt(i+1) == ' '){
					fstart++;
					i++;
					
				}
			}
			
			
		}
		if(fend == -1)fend = name.length();
		int z =fstart;
		boolean printed = false;
		for(int i =0; i<name.length();i++){
			if(z == fend){
				i++;
				z = 0;
				displayName += " ";
				printed = true;
			}
			if(z!=comma)displayName += name.charAt(z);
			z++;
			if(printed && z == fstart) z = fend + 1;
			//if(z>=name.length())break;
		}
		return displayName;
	}
}
