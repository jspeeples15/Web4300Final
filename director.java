
public class director {
	int id;
	String name;
	String displayName;
	public director(){}
	public director(int id, String name){
		this.id = id;
		this.name = fixName(name);
		this.displayName = getDisplayName(name);
	}
	public String fixName(String name){
		for(int i=0;i<name.length();i++){
			if(name.charAt(i) == '\''){
				name = name.substring(0, i-1) + "\\" + name.substring(i);
			}
		}
		return name;
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
