package userinterface;

import impresario.IModel;
import utilities.Debug;

public class TreeTypeAddView extends View {

    public TreeTypeAddView(IModel model) {
        super(model, "TreeTypeAddView");

        setTitle("Add a Tree Type");

        addContent("Barcode Prefix",
                makeField("Barcode Prefix", 2));

        addContent("Type Description",
                makeField("Type Description", 25));

        addContent("Cost",
                makeField("Cost", 20));

        submitButton();
        cancelButton();
        myModel.subscribe("UpdateStatusMessage", this);
    }

    @Override
    public void submit() {
        if (scrapeFields()) {
            myModel.stateChangeRequest("TreeTypeAddSubmit", props);
        } else {
            Debug.logErr("Failed submission: scrapeFields failed");
        }
    }

    @Override
    public void updateState(String key, Object value) {

    }


}
