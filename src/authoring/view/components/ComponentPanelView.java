package authoring.view.components;

import java.io.File;
import java.util.Optional;
import java.util.ResourceBundle;

import authoring.AuthoringController;
import authoring.view.AbstractView;
import game_object.constants.GameObjectConstants;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import resources.ResourceBundles;

public class ComponentPanelView extends AbstractView {
    private HBox myContent;
    private TabPane myTabPane;
    private Button myComponentCreationButton;
    private ImageView myButtonImageView;
    private ResourceBundle myComponentProperties;
    private ResourceBundle myCanvasProperties;
    private ResourceBundle myLanguageResourceBundle;

    private ComponentPanelView(AuthoringController controller) {
        super(controller);
    }

    @Override
    protected void initUI() {
    	myComponentProperties = ResourceBundles.componentProperties;
    	myCanvasProperties = ResourceBundles.canvasProperties;
    	myLanguageResourceBundle = super.getController().getEnvironment().getLanguageResourceBundle();
        myContent = new HBox();
        myTabPane = new TabPane();
        myButtonImageView = new ImageView(GameObjectConstants.UPLOAD);
        myButtonImageView.setPreserveRatio(true);
        myComponentCreationButton = new Button();
        myComponentCreationButton.setPrefHeight(Double.parseDouble(myCanvasProperties.getString("BOTTOM_HEIGHT")) + Double.parseDouble(myCanvasProperties.getString("CREATION_BUTTON_HEIGHT_ABOVE_BOTTOM")));
        myComponentCreationButton.setPrefWidth(Double.parseDouble(myCanvasProperties.getString("CREATION_BUTTON_WIDTH")));
        myComponentCreationButton.setGraphic(myButtonImageView);
        setComponentCreationButtonAction();

        myContent.getChildren().addAll(myTabPane, myComponentCreationButton);
        addUI(myContent);
    }

    @Override
    protected void updateLayoutSelf() {
        double listWidth = getWidth() * Double.parseDouble(myComponentProperties.getString("LIST_WIDTH_RATIO"));
        double buttonWidth = getWidth() - listWidth;
        myContent.setPrefWidth(getWidth());
        myContent.setPrefHeight(getHeight());
        myContent.setSpacing(buttonWidth * (Double.parseDouble(myComponentProperties.getString("CONTENT_SPACING"))));

        myTabPane.setPrefWidth(listWidth);
        myTabPane.setPrefHeight(getHeight());
        getSubViews().forEach(subView -> {
            subView.setWidth(listWidth);
            subView.setHeight(getHeight() - Double.parseDouble(myComponentProperties.getString("SUB_VIEW_HEIGHT_DIFFERENT")));
        });

        double newImageWidth = buttonWidth * Double.parseDouble(myComponentProperties.getString("BUTTON_WIDTH_RATIO"));
        double newImageHeight = getHeight() * Double.parseDouble(myComponentProperties.getString("BUTTON_HEIGHT_RATIO"));
        if (myButtonImageView.getImage().getWidth() / newImageWidth >
                myButtonImageView.getImage().getHeight() / newImageHeight) {
            myButtonImageView.setFitHeight(0);
            myButtonImageView.setFitWidth(newImageWidth);
        } else {
            myButtonImageView.setFitWidth(0);
            myButtonImageView.setFitHeight(newImageHeight);
        }
    }

    public void addTab(String tabName, ComponentListView componentListView) {
        Tab newTab = new Tab(tabName);
        newTab.getStyleClass().add("tab");
        newTab.setClosable(false);
        final ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(componentListView.getUI());
        scrollPane.setFitToHeight(true);
        newTab.setContent(scrollPane);
        myTabPane.getTabs().add(newTab);
        addSubView(componentListView);
    }

    private void setComponentCreationButtonAction() {
        myComponentCreationButton.setOnAction(event -> {
            File imageFile = showFileSelectionDialog();
            if (imageFile == null) return;
            String componentTitle = showStringInputDialog(myLanguageResourceBundle.getString("componentName"));
            String componentDescription = showStringInputDialog(myLanguageResourceBundle.getString("componentDescription"));
            if (!(imageFile == null || componentTitle == null || componentDescription == null)) {
                int selectedTabIndex = myTabPane.getSelectionModel().getSelectedIndex();
                ComponentListView componentListView = (ComponentListView) getSubViews().get(selectedTabIndex);
                componentListView.addComponent(imageFile.toURI().toString(), componentTitle, componentDescription);
                updateLayout();
            }
        });
    }

    private File showFileSelectionDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(myLanguageResourceBundle.getString("chooseSprite"));
        File file = fileChooser.showOpenDialog(new Stage());
        return file;
    }

    private String showStringInputDialog(String dialogMessage) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(null);
        dialog.setContentText(dialogMessage);
        Optional<String> result = dialog.showAndWait();
        return result.isPresent() ? result.get() : null;
    }
    
}
