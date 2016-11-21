package authoring;

import authoring.controller.CanvasViewController;
import authoring.controller.ComponentController;
import authoring.updating.AbstractPublisher;
import authoring.view.canvas.SpriteView;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import serializing.Marshaller;

public class AuthoringController extends AbstractPublisher {
	
	private AuthorEnvironment myEnvironment;
	private SpriteView selectedSpriteView;
	private Scene myScene;
	
	private CanvasViewController canvasViewController;
	private ComponentController componentController;
	private Marshaller marshaller;
	
	public AuthoringController(AuthorEnvironment environment) {
		myEnvironment = environment;
		canvasViewController = new CanvasViewController();
		componentController = new ComponentController();
		marshaller = new Marshaller();
	}
	
	public CanvasViewController getCanvasViewController() {
		return canvasViewController;
	}
	
	public ComponentController getComponentController() {
		return componentController;
	}
	
	public Marshaller getMarshaller() {
		return marshaller;
	}
	
	public AuthorEnvironment getEnvironment() {
		return myEnvironment;
	}
	
	public void selectSpriteView(SpriteView spriteView) {
		if (spriteView == null) return;
		if (selectedSpriteView != null) {
			selectedSpriteView.indicateDeselection();
		}
		spriteView.indicateSelection();
		selectedSpriteView = spriteView;
		this.notifySubscribers();
	}
	
	public SpriteView getSelectedSpriteView() {
		return selectedSpriteView;
	}
	
	public void setScene(Scene scene) {
		myScene = scene;
		myScene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE) {
				canvasViewController.delete(selectedSpriteView);
			}
		});
	}
	
	public void setMouseCursor(Cursor type) {
		myScene.setCursor(type);
	}

}
