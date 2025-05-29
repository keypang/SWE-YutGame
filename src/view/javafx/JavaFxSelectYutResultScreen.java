package view.javafx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * JavaFx 기반의 윷 선택 팝업 화면을 구성하는 클래스임. 사용자가 지정된 윷 결과 목록 중 하나를 선택할 수 있도록 UI 버튼을 제공함.
 */

public class JavaFxSelectYutResultScreen {

  public interface YutSelectListener {
    void onYutSelected(String result);
  }

  public static void show(Stage parentStage, String[] yutResult) {
    Stage dialog = new Stage();
    dialog.initOwner(parentStage);
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.initStyle(StageStyle.UTILITY);
    dialog.setTitle("지정윷 선택");

    HBox buttonContainer = new HBox(20);
    buttonContainer.setAlignment(Pos.CENTER);
    buttonContainer.setPadding(new Insets(30));
    buttonContainer.setStyle("-fx-background-color: #fff5e6;");

    for (String result : yutResult) {
      Button button = new Button(result);
      button.setFont(Font.font("SansSerif", 18));
      button.setPrefSize(100, 70);
      button.setStyle(
          "-fx-background-color: #78c878; -fx-text-fill: white; -fx-font-weight: bold;"
      );

      buttonContainer.getChildren().add(button);
    }

    Scene scene = new Scene(buttonContainer, 800, 200);
    dialog.setScene(scene);
    dialog.centerOnScreen();
    dialog.showAndWait();
  }
}
