package utot.utot.customobjects;

import io.realm.RealmObject;
import utot.utot.customobjects.Picture;

public class Poem extends RealmObject {
	private int primaryKey;
	private String poem;
	private int status; //0 - saved, 1- discarded or 2 - shared
	private Picture pic;

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getPoem(){
		return this.poem;
	}
	
	public void setPoem(String poem){
		this.poem = poem;
	}
	public int getStatus(){
		return this.status;
	}
	public void setStatus(int status){
		this.status = status;
	}
	public Picture getPic(){
		return this.pic;
	}
	public void setPic(Picture pic){
		this.pic = pic;
	}
}