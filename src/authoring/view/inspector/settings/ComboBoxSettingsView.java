package authoring.view.inspector.settings;

import java.util.List;

import authoring.AuthoringController;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class ComboBoxSettingsView extends AbstractSettingsView {

    private List<String> myOptions;
    private String mySelection;
    private ComboBox<String> myBox;
    private ChangeListener<String> myListener;

    public ComboBoxSettingsView(AuthoringController controller, String title, String selection, List<String> options, ChangeListener<String> changeListener) {
        super(controller);
        myLabel.setText(title);
        mySelection = selection;
        myOptions = options;
        myListener = changeListener;
    }

    @Override
    public void initializeSettings() {
        ObservableList<String> options = FXCollections.observableArrayList(myOptions);
        myBox = new ComboBox<>(options);
        myBox.setValue(mySelection);
        myBox.valueProperty().addListener(myListener);
        myContent.getChildren().add(myBox);
    }

    public void setBoxSelection(String selection) {
        myBox.setValue(selection);
    }

    @Override
    protected void updateLayoutSelf() {
        super.updateLayoutSelf();
        myBox.setPrefWidth(this.getWidth());
    }

}
