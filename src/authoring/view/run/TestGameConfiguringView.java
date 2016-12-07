package authoring.view.run;

import authoring.AuthoringController;
import authoring.constants.UIConstants;
import authoring.ui.SliderBox;
import authoring.updating.IPublisher;
import authoring.updating.ISubscriber;
import authoring.view.AbstractView;
import authoring.view.inspector.InspectorView;
import game_engine.physics.PhysicsParameterSetOptions;
import game_engine.physics.PhysicsParameters;
import game_object.level.Level;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class TestGameConfiguringView extends AbstractView implements ISubscriber {
	
	private VBox myBox;
	private SliderBox gravityBox;
	private SliderBox afBox;
	private SliderBox gfBox;
	private SliderBox tmaxBox;
	private SliderBox tminBox;
	private Level myLevel;

	public TestGameConfiguringView(AuthoringController controller) {
		super(controller);
	}
	
	public void setLevel(Level level) {
		myLevel = level;
		this.updateUI();
	}
	
	@Override
	public void didUpdate(IPublisher target) {
		if (target instanceof AuthoringController) {
			this.updateUI();
		}
	}
	
	@Override
	public Parent getUI() {
		return myBox;
	}
	
	/**
	 * updates physics value when there is a current level
	 */
	private void updateUI() {
		if (myLevel == null) return;
		PhysicsParameters param = myLevel.getPhysicsParameters();
		gravityBox.setValue(param.getGravity());
		afBox.setValue(param.getAirFriction());
		gfBox.setValue(param.getGroundFriction());
		tminBox.setValue(param.getMinThreshold());
		tmaxBox.setValue(param.getMaxThreshold());
	}
	
	@Override
	protected void initUI() {
		this.getController().addSubscriber(this);
		myBox = new VBox();
		myBox.setSpacing(10);
		myBox.setPadding(new Insets(UIConstants.TEST_GAME_PADDING, UIConstants.TEST_GAME_PADDING, UIConstants.TEST_GAME_PADDING, UIConstants.TEST_GAME_PADDING));
		//myBox.setStyle("-fx-background-color: linen;");
		fillInBox();
	}

	@Override
	protected void updateLayoutSelf() {
		myBox.setPrefWidth(this.getWidth());
		myBox.setPrefHeight(this.getHeight());
	}
	
	private void fillInBox() {
		gravityBox = new SliderBox(
				"Gravity", 
				UIConstants.GRAVITY_SLIDER_MIN, 
				UIConstants.GRAVITY_SLIDER_MAX, 
				UIConstants.GRAVITY_DEFAULT_VALUE, 
				UIConstants.GRAVITY_INTERVALS, 
				(obv, oldVal, newVal) -> {
			this.setParameter(PhysicsParameterSetOptions.GRAVITY, newVal.doubleValue());
		});
		afBox = new SliderBox(
				"Air Friction", 
				UIConstants.AIR_FRICTION_SLIDER_MIN, 
				UIConstants.AIR_FRICTION_SLIDER_MAX, 
				UIConstants.AIR_FRICTION_DEFAULT_VALUE, 
				UIConstants.AIR_FRICTION_INTERVALS, 
				(obv, oldVal, newVal) -> {
			this.setParameter(PhysicsParameterSetOptions.AIRFRICTION, newVal.doubleValue());
		});
		gfBox = new SliderBox(
				"Ground Friction", 
				UIConstants.GROUND_FRICTION_SLIDER_MIN, 
				UIConstants.GROUND_FRICTION_SLIDER_MAX, 
				UIConstants.GROUND_FRICTION_DEFAULT_VALUE, 
				UIConstants.GROUND_FRICTION_INTERVALS, 
				(obv, oldVal, newVal) -> {
			this.setParameter(PhysicsParameterSetOptions.GROUNDFRICTION, newVal.doubleValue());
		});
		gfBox.getBox().setFocusTraversable(false);
		tmaxBox = new SliderBox(
				"Max Threshold", 
				UIConstants.MAX_THRESHOLD_SLIDER_MIN,
				UIConstants.MAX_THRESHOLD_SLIDER_MAX,
				UIConstants.MAX_THRESHOLD_DEFAULT_VALUE,
				UIConstants.MAX_THRESHOLD_INTERVALS,
				(obv, oldVal, newVal) -> {
			this.setParameter(PhysicsParameterSetOptions.MAXTHRESHOLD, newVal.doubleValue());
		});
		tmaxBox.getBox().setFocusTraversable(false);
		tminBox = new SliderBox(
				"Min Threshold", 
				UIConstants.MIN_THRESHOLD_SLIDER_MIN,
				UIConstants.MIN_THRESHOLD_SLIDER_MAX,
				UIConstants.MIN_THRESHOLD_DEFAULT_VALUE,
				UIConstants.MIN_THRESHOLD_INTERVALS,
				(obv, oldVal, newVal) -> {
			this.setParameter(PhysicsParameterSetOptions.MINTHRESHOLD, newVal.doubleValue());
		});
		tminBox.getBox().setFocusTraversable(false);
		
		myBox.getChildren().addAll(
				gravityBox.getBox(), afBox.getBox(), gfBox.getBox(), tminBox.getBox(), tmaxBox.getBox());
		myBox.getChildren().forEach(box->box.setFocusTraversable(true));
	}
	
	private void setParameter(PhysicsParameterSetOptions option, double value) {
		if (this.getParentView() instanceof InspectorView) {
			this.getController().setParameter(myLevel, option, value);
		}
		else if (this.getParentView() instanceof TestGameView) {
			this.getController().getTestGameController().setParameter(option, value);
		}
	}

}
