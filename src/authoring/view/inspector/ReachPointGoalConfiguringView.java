package authoring.view.inspector;

import authoring.AuthoringController;
import authoring.view.AbstractView;
import game_object.character.ICharacter;
import game_object.core.ISprite;
import goal.position.ReachPointGoal;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ReachPointGoalConfiguringView extends AbstractView {
	VBox goalVBox;
	CheckBox reachPointCheckBox;
	AuthoringController myController;
	
	public ReachPointGoalConfiguringView(AuthoringController controller) {
		super(controller);
		myController = controller;
	}
	
	public void setUpReachPointGoalCheckBox(ISprite sprite) {
		reachPointCheckBox = new CheckBox("Assign sprite to be goal point of the level");
		reachPointCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue ov,Boolean old_val, Boolean new_val) {
                 ReachPointGoal reachGoal = new ReachPointGoal((ICharacter) sprite, sprite.getPosition());
                 myController.getEnvironment().getCurrentLevel().getAllGoals().add(reachGoal);
            }
        });
		
		goalVBox = new VBox();
		Label goalLevel = new Label("Goals");
		goalLevel.setLabelFor(reachPointCheckBox);
		goalVBox.getChildren().add(goalLevel);
		goalVBox.getChildren().add(reachPointCheckBox);
	}
	
	public Parent getUI() {
		return goalVBox;
	}

	@Override
	protected void initUI() {
	}

	@Override
	protected void updateLayoutSelf() {	
	}

	
}
