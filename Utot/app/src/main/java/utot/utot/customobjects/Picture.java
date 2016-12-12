package utot.utot.customobjects;

import io.realm.RealmObject;

public class Picture extends RealmObject {
	private String resourceName;
	private boolean isUsed;
	
	public String getResourceName(){
		return this.resourceName;
	}
	public void setResourceName(String resourceName){
		this.resourceName = resourceName;
	}
	public boolean isUsed(){
		return this.isUsed;
	}
	public void setIsUsed(boolean isUsed){
		this.isUsed = isUsed;
	}
}