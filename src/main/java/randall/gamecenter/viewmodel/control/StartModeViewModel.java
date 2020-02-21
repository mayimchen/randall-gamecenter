package randall.gamecenter.viewmodel.control;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import java.time.LocalDateTime;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import randall.gamecenter.model.StartMode;

@Slf4j
@Service
public class StartModeViewModel {
  private final ObjectProperty<StartMode> startMode = new SimpleObjectProperty<>();
  private final IntegerProperty hours = new SimpleIntegerProperty(0);
  private final IntegerProperty minutes = new SimpleIntegerProperty(0);

  private final CompositeDisposable disposable = new CompositeDisposable();

  private LocalDateTime targetTimestamp;

  @PreDestroy void onDestroy() {
    disposable.clear();
  }

  public void bindStartMode(ComboBox<StartMode> comboBox) {
    comboBox.setItems(FXCollections.observableArrayList(StartMode.values()));
    disposable.add(JavaFxObservable.valuesOf(comboBox.valueProperty())
        .subscribe(startMode::setValue));
  }

  public void bindHours(Spinner<Integer> spinner) {
    spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0));
    disposable.add(JavaFxObservable.valuesOf(startMode)
        .map(StartMode.NORMAL::equals)
        .subscribe(spinner::setDisable));
    disposable.add(JavaFxObservable.valuesOf(spinner.getValueFactory().valueProperty())
        .map(integer -> spinner.isDisabled() ? 0 : integer)
        .subscribe(hours::setValue));
  }

  public void bindMinutes(Spinner<Integer> spinner) {
    spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
    disposable.add(JavaFxObservable.valuesOf(startMode)
        .map(StartMode.NORMAL::equals)
        .subscribe(spinner::setDisable));
    disposable.add(JavaFxObservable.valuesOf(spinner.getValueFactory().valueProperty())
        .map(integer -> spinner.isDisabled() ? 0 : integer)
        .subscribe(minutes::setValue));
  }

  public void computeStatTime() {
    LocalDateTime now = LocalDateTime.now();
    int hoursValue = hours.getValue();
    int minutesValue = minutes.getValue();
    switch (startMode.get()) {
      case DELAY:
        targetTimestamp = now.plusHours(hoursValue).plusMinutes(minutesValue);
        break;
      case TIMING:
        targetTimestamp = now.withHour(hoursValue).withMinute(minutesValue);
        if (targetTimestamp.isBefore(now)) {
          targetTimestamp = targetTimestamp.plusDays(1);
        }
        break;
      case NORMAL:
        targetTimestamp = now;
        break;
    }
  }

  public boolean timeUp() {
    return targetTimestamp.isBefore(LocalDateTime.now());
  }
}
