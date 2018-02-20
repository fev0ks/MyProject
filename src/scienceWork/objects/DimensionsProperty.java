package scienceWork.objects;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.awt.*;


public class DimensionsProperty extends Dimension {
    private int height=0;
    private int width=0;
    public DimensionsProperty(Dimension dimension) {
        this.width=(int)dimension.getWidth();
        this.height=(int)dimension.getHeight();
    }

    public StringProperty toPropertyString(){
        return new SimpleStringProperty(width+"x"+height);
    }
}
