package userinterface;

import impresario.IModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Scout;
import model.ScoutCollection;
import model.ScoutTableModel;
import utilities.Alerts;
import utilities.Utilities;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

public class ScoutCollectionView extends View {
    protected final int COL_WIDTH = 100;

    protected TableView<ScoutTableModel> tableOfScouts;
    protected ScoutTableModel selection; //Selected scout

    public ScoutCollectionView(IModel model) {
        super(model, "ScoutCollectionView");
        setTitle("Search results");

        tableOfScouts = new TableView<>();
        tableOfScouts.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        List<TableColumn<ScoutTableModel, String>> tableColumns = Arrays.asList(
                //new TableColumn<>("Id"),
                new TableColumn<>("Last Name"),
                new TableColumn<>("First Name"),
                new TableColumn<>("Middle Name"),
                new TableColumn<>("Date Of Birth"),
                new TableColumn<>("Phone Number"),
                new TableColumn<>("Email"),
                new TableColumn<>("Troop Id"),
                new TableColumn<>("Status"),
                new TableColumn<>("Date Status Updated")
        );

        tableColumns.forEach(column -> {
            column.setMinWidth(COL_WIDTH);
            column.setCellValueFactory(new PropertyValueFactory<>(Utilities.toCamelCase(column.getText())));
        });

        tableOfScouts.getColumns().addAll(tableColumns);
        tableOfScouts.setOnMousePressed(e -> {
            if (e.isPrimaryButtonDown() && e.getClickCount() >= 2) {
                selection = tableOfScouts.getSelectionModel().getSelectedItem();
            }
        });

        addContent(tableOfScouts);

        footButt(makeButt("Update", e -> {
            if (selection == null) {
                Alerts.errorMessage("Must select a scout!");
            } else {
                myModel.stateChangeRequest("ScoutUpdate", selection.getId());
            }
        }));

        footButt(makeButt("Delete", e -> {
            if (selection == null) {
                Alerts.errorMessage("Must select a scout!");
            } else {
                Optional<ButtonType> confirmation = Alerts.confirmMessage("Are you sure you want to delete Scout " + selection.getFirstName() + " " + selection.getLastName() + "?");
                if (confirmation.get() == ButtonType.OK) {
                    myModel.stateChangeRequest("ScoutDelete", selection.getId());
                }
            }
        }));

        cancelButton();

        getEntryTableModelValues();
    }

    protected void getEntryTableModelValues() {
        ObservableList<ScoutTableModel> tableData = FXCollections.observableArrayList();

        ScoutCollection collection = (ScoutCollection) myModel.getState("ScoutList");
        Vector<Scout> entryList = (Vector<Scout>) collection.getState("Scouts");

        for (Scout scout : entryList) {
            tableData.add(new ScoutTableModel(scout.getEntryListView()));
        }

        tableOfScouts.setItems(tableData);
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
