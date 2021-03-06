package userinterface;

import impresario.IModel;
import utilities.Debug;

public class ViewFactory {

    public static View createView(String viewName, IModel model) {
        try {
            return (View) Class.forName("userinterface." + viewName).getConstructor(IModel.class).newInstance(model);
        } catch (Exception ex) {
            Debug.logErr("No view found for " + viewName);
            ex.printStackTrace();
        }
        return null;
    }
}
