package authoring.view.menu.menu_element;

import authoring.AuthoringController;
import authoring.view.menu.AbstractGameMenuElement;
import game_object.background.Background;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author rachelbransom
 *Menu item to change the canvas background
 */
public class ChangeLevelBackgroundElement extends AbstractGameMenuElement{

	private static final String MENU_NAME = "Change Level Background";

	protected ChangeLevelBackgroundElement(AuthoringController controller) {
		super(MENU_NAME, controller);
	}

	@Override
	protected void setFunctionality() {
		myMenuItem.setOnAction(event -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Choose Background Image");
        	File imageFile = fileChooser.showOpenDialog(new Stage());
			String imagePath = imageFile.toURI().toString();
			myController.getEnvironment().getCurrentLevel().getBackground().clearImagePaths();
			myController.getEnvironment().getCurrentLevel().getBackground().appendImagePath(imagePath);
			myController.getCanvasViewController().refresh();
		});
	}
}
