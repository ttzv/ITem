package com.ttzv.item.uiUtils;

import com.ttzv.item.entity.Office;
import javafx.scene.control.ListCell;

public class OfficeFormatCell extends ListCell<Office> {

    public OfficeFormatCell() {}

    @Override
    protected void updateItem(Office item, boolean empty) {
        super.updateItem(item, empty);

        setText(item == null ? "" : (
                item.getName() == null ? "New..." : item.getName() + " " + item.getName2())
        );
    }
}