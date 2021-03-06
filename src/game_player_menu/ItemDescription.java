package game_player_menu;

import javafx.scene.layout.Pane;

/**
 * @author samuelcurtis
 *This is a generic class that can be used to describe all sorts of items. In the case of this project,
 *it will likely be a component of a Game that each game will have a name, description, and image associated
 *with it. 
 */
public class ItemDescription  {
	
	private String myName;
	private String myDescription;
	private String myImagePath;
	
	public ItemDescription(String name, String description, String imgPath) {
		myName = name;
		myDescription = description;
		myImagePath = imgPath;
	}
	
	public String getName(){
		return myName;
	}
	
	public String getDescriptionn(){
		return myDescription;
	}
	
	public String getImagePath(){
		return myImagePath;
	}
	
	public void setName(String name){
		myName = name;
	}
	
	public void setDescription(String description){
		myDescription = description;
	}
	
	public void setImagePath(String imgPath){
		myImagePath = imgPath;
	}

}
