package CustomUIElements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class GenericCard<T> extends JPanel {
    protected final T data;

    public GenericCard(T data) {
        this.data = data;
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public T getData() {
        return data;
    }

    public void leftClickHandler(MouseEvent e) {

    }

    public void rightClickHandler(MouseEvent e) {

    }

    public abstract String getSearchableText();
}
