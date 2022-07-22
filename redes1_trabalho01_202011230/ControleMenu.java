import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class ControleMenu {

    @FXML
    void startImageButton(MouseEvent event) {
      Principal.changeScreenPrograma(event);
    }

}
