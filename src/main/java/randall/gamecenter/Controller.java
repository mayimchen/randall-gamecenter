package randall.gamecenter;

import com.google.common.base.Strings;
import helper.DateTimeHelper;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import org.ini4j.Ini;
import org.ini4j.Profile;
import org.ini4j.Wini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import randall.common.ui.Dialogs;
import randall.common.util.IOHelper;
import randall.common.util.Networks;

import static randall.gamecenter.Share.ALL_IP_ADDRESS;
import static randall.gamecenter.Share.BASIC_SECTION_NAME;
import static randall.gamecenter.Share.DB_SERVER_PROCESS_CODE;
import static randall.gamecenter.Share.DB_SERVER_SECTION_NAME_2;
import static randall.gamecenter.Share.DEFAULT_AUTO_RUN_BACKUP;
import static randall.gamecenter.Share.DEFAULT_CLOSE_WUXING_ENABLED;
import static randall.gamecenter.Share.DEFAULT_DB_NAME;
import static randall.gamecenter.Share.DEFAULT_GAME_DIRECTORY;
import static randall.gamecenter.Share.DEFAULT_GAME_NAME;
import static randall.gamecenter.Share.DEFAULT_IP_2_ENABLED;
import static randall.gamecenter.Share.ERROR_STATE;
import static randall.gamecenter.Share.LOGIN_GATE_PROCESS_CODE;
import static randall.gamecenter.Share.LOGIN_GATE_SECTION_NAME_2;
import static randall.gamecenter.Share.LOGIN_SERVER_PROCESS_CODE;
import static randall.gamecenter.Share.LOGIN_SRV_SECTION_NAME_2;
import static randall.gamecenter.Share.LOG_SERVER_PROCESS_CODE;
import static randall.gamecenter.Share.LOG_SERVER_SECTION_2;
import static randall.gamecenter.Share.M2_SERVER_CONFIG_FILE;
import static randall.gamecenter.Share.M2_SERVER_PROCESS_CODE;
import static randall.gamecenter.Share.M2_SERVER_SECTION_NAME_1;
import static randall.gamecenter.Share.M2_SERVER_SECTION_NAME_2;
import static randall.gamecenter.Share.MAX_RUN_GATE_COUNT;
import static randall.gamecenter.Share.ONLINE_USER_LIMIT;
import static randall.gamecenter.Share.PLUG_TOP_PROCESS_CODE;
import static randall.gamecenter.Share.PRIMARY_IP_ADDRESS;
import static randall.gamecenter.Share.QUIT_CODE;
import static randall.gamecenter.Share.RUNNING_STATE;
import static randall.gamecenter.Share.RUN_GATE_PROCESS_CODE;
import static randall.gamecenter.Share.RUN_GATE_SECTION_NAME_2;
import static randall.gamecenter.Share.SECOND_IP_ADDRESS;
import static randall.gamecenter.Share.SEL_GATE_PROCESS_CODE;
import static randall.gamecenter.Share.SEL_GATE_SECTION_NAME_2;
import static randall.gamecenter.Share.SERVER_CONFIG_FILE;
import static randall.gamecenter.Share.STARTING_STATE;
import static randall.gamecenter.Share.STOPPED_STATE;
import static randall.gamecenter.Share.STOPPING_STATE;

/**
 * 控制器。
 *
 * @author mrzhqiang
 */
public final class Controller {
  private static final Logger LOGGER = LoggerFactory.getLogger("randall");

  /* 控制面板 */
  public TabPane mainTabPane;
  public CheckBox dbServerCheckBox;
  public CheckBox loginSrvCheckBox;
  public CheckBox m2ServerCheckBox;
  public CheckBox logServerCheckBox;
  public CheckBox gameGateCheckBox1;
  public CheckBox gameGateCheckBox2;
  public CheckBox gameGateCheckBox3;
  public CheckBox gameGateCheckBox4;
  public CheckBox gameGateCheckBox5;
  public CheckBox gameGateCheckBox6;
  public CheckBox gameGateCheckBox7;
  public CheckBox gameGateCheckBox8;
  public CheckBox selGateCheckBox1;
  public CheckBox selGateCheckBox2;
  public CheckBox loginGateCheckBox;
  public CheckBox loginGateCheckBox2;
  public CheckBox plugTopCheckBox;
  public ComboBox<StartMode> startModeComboBox;
  public Spinner<Integer> hoursSpinner;
  public Spinner<Integer> minutesSpinner;
  public TextArea gameInfoTextArea;
  public Button startGameButton;
  /* 配置向导 */
  public TabPane configTabPane;
  public TextField primaryAddressTextField;
  public CheckBox doubleAddressCheckBox;
  public TextField secondAddressTextField;
  public CheckBox dynamicAddressCheckBox;
  public TextField gameNameTextField;
  public TextField dbNameTextField;
  public TextField gameDirTextField;
  public Spinner<Integer> allPortPlusSpinner;
  public CheckBox closeWuxingCheckBox;
  public CheckBox openLoginGateCheckBox;
  public CheckBox openLoginGateCheckBox2;
  public TextField loginGateFormXTextField;
  public TextField loginGateFormYTextField;
  public TextField loginGatePortTextField;
  public CheckBox openSelGateCheckBox1;
  public CheckBox openSelGateCheckBox2;
  public TextField selGateFormXTextField;
  public TextField selGateFormYTextField;
  public TextField selGatePortTextField1;
  public TextField selGatePortTextField2;
  public CheckBox openRunGateCheckBox1;
  public CheckBox openRunGateCheckBox2;
  public CheckBox openRunGateCheckBox3;
  public CheckBox openRunGateCheckBox4;
  public CheckBox openRunGateCheckBox5;
  public CheckBox openRunGateCheckBox6;
  public CheckBox openRunGateCheckBox7;
  public CheckBox openRunGateCheckBox8;
  public TextField runGateFormXTextField;
  public TextField runGateFormYTextField;
  public TextField runGatePortTextField1;
  public TextField runGatePortTextField2;
  public TextField runGatePortTextField3;
  public TextField runGatePortTextField4;
  public TextField runGatePortTextField5;
  public TextField runGatePortTextField6;
  public TextField runGatePortTextField7;
  public TextField runGatePortTextField8;
  public CheckBox openLoginSrvCheckBox;
  public TextField loginSrvFormXTextField;
  public TextField loginSrvFormYTextField;
  public TextField loginSrvGatePortTextField;
  public TextField loginSrvMonPortTextField;
  public TextField loginSrvServerPortTextField;
  public CheckBox openDbServerCheckBox;
  public TextField dbServerFormXTextField;
  public TextField dbServerFormYTextField;
  public TextField dbServerGatePortTextField;
  public TextField dbServerServerPortTextField;
  public CheckBox openLogServerCheckBox;
  public TextField logServerFormXTextField;
  public TextField logServerFormYTextField;
  public TextField logServerGatePortTextField;
  public CheckBox openM2ServerCheckBox;
  public TextField m2ServerFormXTextField;
  public TextField m2ServerFormYTextField;
  public TextField m2ServerGatePortTextField;
  public TextField m2ServerServerPortTextField;
  public CheckBox openPlugTopCheckBox;
  public TextField plugTopFormXTextField;
  public TextField plugTopFormYTextField;
  /* 数据备份 */
  public TableView<BackupManager.BackupObject> dataBackupTableView;
  public TableColumn<BackupManager.BackupObject, String> dataDirectoryTableColumn;
  public TableColumn<BackupManager.BackupObject, String> backupDirectoryTableColumn;
  public RadioButton dayBackupModeRadioButton;
  public RadioButton intervalBackupModeRadioButton;
  public ToggleGroup backupModeToggleGroup;
  public Spinner<Integer> dayModeHoursSpinner;
  public Spinner<Integer> intervalModeHoursSpinner;
  public Spinner<Integer> dayModeMinutesSpinner;
  public Spinner<Integer> intervalModeMinutesSpinner;
  public CheckBox backupFunctionCheckBox;
  public CheckBox compressFunctionCheckBox;
  public CheckBox autoRunBackupCheckBox;
  public TextField dataDirectoryTextField;
  public TextField backupDirectoryTextField;
  public Button modifyBackupButton;
  public Button deleteBackupButton;
  public Button addBackupButton;
  public Button saveBackupButton;
  public Button startBackupButton;
  public Label backupMessageLabel;
  /* 开区数据清理 */
  public CheckBox deleteRoleDataCheckBox;
  public CheckBox deleteNPCMakeDataCheckBox;
  public CheckBox deleteAccountDataCheckBox;
  public CheckBox deleteEMailDataCheckBox;
  public CheckBox deleteGuildDataCheckBox;
  public CheckBox deleteAccountLoggerCheckBox;
  public CheckBox clearSabacDataCheckBox;
  public CheckBox deleteM2ServerLoggerCheckBox;
  public CheckBox clearGlobalVariateCheckBox;
  public CheckBox deleteGameLoggerCheckBox;
  public CheckBox resetItemIDCountCheckBox;
  public CheckBox clearRoleRelationDataCheckBox;
  public Button startClearDataButton;

  private boolean opened = false;
  private boolean gateStopped;
  private long gateStopTick;
  // 0 -- default; 1 -- starting; 2 -- running; 3 -- stopping; 9 -- error
  public int startState = 0;
  // 0 -- disabled; 1 -- enabled;
  private int backupState = 0;

  private long refTick;
  private long showTick;
  private long runTick;
  private long runTime;

  private Timer startGameTimer = new Timer();
  private Timer stopGameTimer = new Timer();
  private Timer checkRunTimer = new Timer();

  private final Share share = new Share();

  @FXML
  public void initialize() {
    // 额外添加的监听器
    addListener();
    opened = false;
    mainTabPane.getSelectionModel().select(0);
    configTabPane.getSelectionModel().select(0);
    startState = 0;
    backupState = 0;
    gameInfoTextArea.clear();
    refTick = System.currentTimeMillis();
    share.loadConfig();
    loadBackupList();
    refBackupListToView();
    if (!startService()) {
      return;
    }
    refGameConsole();
    autoRunBackupCheckBox.setSelected(share.autoRunBakEnabled);
    opened = true;
    if (share.autoRunBakEnabled) {
      onStartBackupClicked();
    }
  }

  private void addListener() {
    startModeComboBox.setItems(FXCollections.observableArrayList(StartMode.values()));
    startModeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
      hoursSpinner.setDisable(newValue.equals(StartMode.NORMAL));
      minutesSpinner.setDisable(newValue.equals(StartMode.NORMAL));
    });
    startModeComboBox.getSelectionModel().select(StartMode.NORMAL);
    hoursSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0));
    minutesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
    allPortPlusSpinner.setValueFactory(
        new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 65535, 0));
    startModeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
      if (StartMode.NORMAL.equals(newValue)) {
        runTime = 0;
      } else if (StartMode.DELAY.equals(newValue)) {
        Integer hours = hoursSpinner.getValue();
        Integer minutes = minutesSpinner.getValue();
        runTime = Duration.ofHours(hours)
            .plus(Duration.ofMinutes(minutes))
            .toMillis();
      } else if (StartMode.TIMING.equals(newValue)) {
        Integer hours = hoursSpinner.getValue();
        Integer minutes = minutesSpinner.getValue();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dateTime = LocalDateTime.of(now.toLocalDate(), LocalTime.of(hours, minutes));
        // 如果指定时间在现在之前，那么就认为是第二天的时刻，所以时间要加一天
        if (dateTime.isBefore(now)) {
          dateTime = dateTime.plusDays(1);
        }
        // System.currentTimeMillis() 方法获取的本来就是 UTC 时间戳
        runTime = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
      }
    });
    doubleAddressCheckBox.selectedProperty().addListener((observable, oldValue, newValue) ->
        secondAddressTextField.setDisable(!newValue));
    dynamicAddressCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
      primaryAddressTextField.setDisable(newValue);
      secondAddressTextField.setDisable(newValue);
    });
    allPortPlusSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
      loginGatePortTextField.setText(String.valueOf(share.config.loginGate.gatePort + newValue));
      selGatePortTextField1.setText(String.valueOf(share.config.selGate.gatePort[0] + newValue));
      selGatePortTextField2.setText(String.valueOf(share.config.selGate.gatePort[1] + newValue));
      runGatePortTextField1.setText(String.valueOf(share.config.runGate.gatePort[0] + newValue));
      runGatePortTextField2.setText(String.valueOf(share.config.runGate.gatePort[1] + newValue));
      runGatePortTextField3.setText(String.valueOf(share.config.runGate.gatePort[2] + newValue));
      runGatePortTextField4.setText(String.valueOf(share.config.runGate.gatePort[3] + newValue));
      runGatePortTextField5.setText(String.valueOf(share.config.runGate.gatePort[4] + newValue));
      runGatePortTextField6.setText(String.valueOf(share.config.runGate.gatePort[5] + newValue));
      runGatePortTextField7.setText(String.valueOf(share.config.runGate.gatePort[6] + newValue));
      runGatePortTextField8.setText(String.valueOf(share.config.runGate.gatePort[7] + newValue));
      loginSrvGatePortTextField.setText(String.valueOf(share.config.loginSrv.gatePort + newValue));
      loginSrvServerPortTextField.setText(
          String.valueOf(share.config.loginSrv.serverPort + newValue));
      loginSrvMonPortTextField.setText(String.valueOf(share.config.loginSrv.monPort + newValue));
      dbServerGatePortTextField.setText(String.valueOf(share.config.dbServer.gatePort + newValue));
      dbServerServerPortTextField.setText(
          String.valueOf(share.config.dbServer.serverPort + newValue));
      logServerGatePortTextField.setText(String.valueOf(share.config.logServer.port + newValue));
      m2ServerGatePortTextField.setText(String.valueOf(share.config.m2Server.gatePort + newValue));
      m2ServerServerPortTextField.setText(
          String.valueOf(share.config.m2Server.msgSrvPort + newValue));
    });
    dataDirectoryTableColumn.setCellValueFactory(param -> param.getValue().sourceDir);
    backupDirectoryTableColumn.setCellValueFactory(param -> param.getValue().destinationDir);
    dataBackupTableView.setItems(share.backupManager.backupList);
    dataBackupTableView.getSelectionModel().selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> {
          dataDirectoryTextField.setText(newValue.sourceDir.get());
          backupDirectoryTextField.setText(newValue.destinationDir.get());
          backupFunctionCheckBox.setSelected(newValue.backupEnabled);
          compressFunctionCheckBox.setSelected(newValue.compressEnabled);
          if (newValue.backupMode == 0) {
            backupModeToggleGroup.selectToggle(dayBackupModeRadioButton);
            dayModeHoursSpinner.getValueFactory().setValue(newValue.hours);
            dayModeMinutesSpinner.getValueFactory().setValue(newValue.minutes);
          } else {
            backupModeToggleGroup.selectToggle(intervalBackupModeRadioButton);
            intervalModeHoursSpinner.getValueFactory().setValue(newValue.hours);
            intervalModeMinutesSpinner.getValueFactory().setValue(newValue.minutes);
          }
          deleteBackupButton.setDisable(false);
          modifyBackupButton.setDisable(false);
        });
    dayModeHoursSpinner.setValueFactory(
        new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0));
    dayModeMinutesSpinner.setValueFactory(
        new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
    intervalModeHoursSpinner.setValueFactory(
        new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99, 0));
    intervalModeMinutesSpinner.setValueFactory(
        new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
    dayBackupModeRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
      intervalModeHoursSpinner.setDisable(newValue);
      intervalModeMinutesSpinner.setDisable(newValue);
      dayModeHoursSpinner.setDisable(!newValue);
      dayModeMinutesSpinner.setDisable(!newValue);
    });
    intervalBackupModeRadioButton.selectedProperty()
        .addListener((observable, oldValue, newValue) -> {
          dayModeHoursSpinner.setDisable(newValue);
          dayModeMinutesSpinner.setDisable(newValue);
          intervalModeHoursSpinner.setDisable(!newValue);
          intervalModeMinutesSpinner.setDisable(!newValue);
        });
    //backupModeToggleGroup.selectToggle(dayBackupModeRadioButton);
  }

  private void refGameConsole() {
    opened = false;

    // 刷新控制台按钮的选中状态
    m2ServerCheckBox.setSelected(share.config.m2Server.getStart);
    dbServerCheckBox.setSelected(share.config.dbServer.getStart);
    loginSrvCheckBox.setSelected(share.config.loginSrv.getStart);
    logServerCheckBox.setSelected(share.config.logServer.getStart);
    loginGateCheckBox.setSelected(share.config.loginGate.getStart);
    selGateCheckBox1.setSelected(share.config.selGate.getStart1);
    selGateCheckBox2.setSelected(share.config.selGate.getStart2);
    gameGateCheckBox1.setSelected(share.config.runGate.getStart[0]);
    gameGateCheckBox2.setSelected(share.config.runGate.getStart[1]);
    gameGateCheckBox3.setSelected(share.config.runGate.getStart[2]);
    gameGateCheckBox4.setSelected(share.config.runGate.getStart[3]);
    gameGateCheckBox5.setSelected(share.config.runGate.getStart[4]);
    gameGateCheckBox6.setSelected(share.config.runGate.getStart[5]);
    gameGateCheckBox7.setSelected(share.config.runGate.getStart[6]);
    gameGateCheckBox8.setSelected(share.config.runGate.getStart[7]);
    plugTopCheckBox.setSelected(share.config.plugTop.getStart);

    // 第一步 基本设置
    gameDirTextField.setText(share.gameDirectory);
    dbNameTextField.setText(share.heroDBName);
    gameNameTextField.setText(share.gameName);
    primaryAddressTextField.setText(share.extIPAddr);
    secondAddressTextField.setText(share.extIPAddr2);
    dynamicAddressCheckBox.setSelected(share.ip2Enabled);
    closeWuxingCheckBox.setSelected(share.closeWuXinEnabled);
    // 第二步 登录网关
    loginGateFormXTextField.setText(String.valueOf(share.config.loginGate.mainFormX));
    loginGateFormYTextField.setText(String.valueOf(share.config.loginGate.mainFormY));
    openLoginGateCheckBox.setSelected(share.config.loginGate.getStart);
    loginGatePortTextField.setText(String.valueOf(share.config.loginGate.gatePort));
    // 第三步 角色网关
    selGateFormXTextField.setText(String.valueOf(share.config.selGate.mainFormX));
    selGateFormYTextField.setText(String.valueOf(share.config.selGate.mainFormY));
    openSelGateCheckBox1.setSelected(share.config.selGate.getStart1);
    openSelGateCheckBox2.setSelected(share.config.selGate.getStart2);
    selGatePortTextField1.setText(String.valueOf(share.config.selGate.gatePort[0]));
    selGatePortTextField2.setText(String.valueOf(share.config.selGate.gatePort[1]));
    // 第四步 游戏网关
    runGateFormXTextField.setText(String.valueOf(share.config.runGate.mainFormX));
    runGateFormYTextField.setText(String.valueOf(share.config.runGate.mainFormY));
    openRunGateCheckBox1.setSelected(share.config.runGate.getStart[0]);
    openRunGateCheckBox2.setSelected(share.config.runGate.getStart[1]);
    openRunGateCheckBox3.setSelected(share.config.runGate.getStart[2]);
    openRunGateCheckBox4.setSelected(share.config.runGate.getStart[3]);
    openRunGateCheckBox5.setSelected(share.config.runGate.getStart[4]);
    openRunGateCheckBox6.setSelected(share.config.runGate.getStart[5]);
    openRunGateCheckBox7.setSelected(share.config.runGate.getStart[6]);
    openRunGateCheckBox8.setSelected(share.config.runGate.getStart[7]);
    runGatePortTextField1.setText(String.valueOf(share.config.runGate.gatePort[0]));
    runGatePortTextField2.setText(String.valueOf(share.config.runGate.gatePort[1]));
    runGatePortTextField3.setText(String.valueOf(share.config.runGate.gatePort[2]));
    runGatePortTextField4.setText(String.valueOf(share.config.runGate.gatePort[3]));
    runGatePortTextField5.setText(String.valueOf(share.config.runGate.gatePort[4]));
    runGatePortTextField6.setText(String.valueOf(share.config.runGate.gatePort[5]));
    runGatePortTextField7.setText(String.valueOf(share.config.runGate.gatePort[6]));
    runGatePortTextField8.setText(String.valueOf(share.config.runGate.gatePort[7]));
    // 第五步 登录服务器
    loginSrvFormXTextField.setText(String.valueOf(share.config.loginSrv.mainFormX));
    loginSrvFormYTextField.setText(String.valueOf(share.config.loginSrv.mainFormY));
    openLoginSrvCheckBox.setSelected(share.config.loginSrv.getStart);
    loginSrvGatePortTextField.setText(String.valueOf(share.config.loginSrv.gatePort));
    loginSrvServerPortTextField.setText(String.valueOf(share.config.loginSrv.serverPort));
    loginSrvMonPortTextField.setText(String.valueOf(share.config.loginSrv.monPort));
    // 第六步 数据库服务器
    dbServerFormXTextField.setText(String.valueOf(share.config.dbServer.mainFormX));
    dbServerFormYTextField.setText(String.valueOf(share.config.dbServer.mainFormY));
    openDbServerCheckBox.setSelected(share.config.dbServer.getStart);
    dbServerGatePortTextField.setText(String.valueOf(share.config.dbServer.gatePort));
    dbServerServerPortTextField.setText(String.valueOf(share.config.dbServer.serverPort));
    // 第七步 游戏日志服务器
    logServerFormXTextField.setText(String.valueOf(share.config.logServer.mainFormX));
    logServerFormYTextField.setText(String.valueOf(share.config.logServer.mainFormY));
    openLogServerCheckBox.setSelected(share.config.logServer.getStart);
    logServerGatePortTextField.setText(String.valueOf(share.config.logServer.port));
    // 第八步 游戏主引擎服务器
    m2ServerFormXTextField.setText(String.valueOf(share.config.m2Server.mainFormX));
    m2ServerFormYTextField.setText(String.valueOf(share.config.m2Server.mainFormY));
    openM2ServerCheckBox.setSelected(share.config.m2Server.getStart);
    m2ServerGatePortTextField.setText(String.valueOf(share.config.m2Server.gatePort));
    m2ServerServerPortTextField.setText(String.valueOf(share.config.m2Server.msgSrvPort));
    // 第九步 排行榜插件
    plugTopFormXTextField.setText(String.valueOf(share.config.plugTop.mainFormX));
    plugTopFormYTextField.setText(String.valueOf(share.config.plugTop.mainFormY));
    openPlugTopCheckBox.setSelected(share.config.plugTop.getStart);

    opened = true;
  }

  private boolean startService() {
    mainOutMessage("正在启动游戏客户端控制器...");
    showTick = System.currentTimeMillis();
    mainOutMessage("游戏控制台启动完成...");
    return true;
  }

  private void mainOutMessage(String message) {
    LOGGER.info(message);
    gameInfoTextArea.appendText(String.format("[%s] -- %s" + System.lineSeparator(),
        DateTimeHelper.format(Date.from(Instant.now())),
        message));
  }

  public void onDBServerClicked() {
    share.config.dbServer.getStart = dbServerCheckBox.isSelected();
  }

  public void onLoginSrvClicked() {
    share.config.loginSrv.getStart = loginSrvCheckBox.isSelected();
  }

  public void onM2ServerClicked() {
    share.config.m2Server.getStart = m2ServerCheckBox.isSelected();
  }

  public void onLogServerClicked() {
    share.config.logServer.getStart = logServerCheckBox.isSelected();
  }

  public void onRunGate1Clicked() {
    share.config.runGate.getStart[0] = gameGateCheckBox1.isSelected();
  }

  public void onRunGate2Clicked() {
    share.config.runGate.getStart[1] = gameGateCheckBox2.isSelected();
  }

  public void onRunGate3Clicked() {
    share.config.runGate.getStart[2] = gameGateCheckBox3.isSelected();
  }

  public void onRunGate4Clicked() {
    share.config.runGate.getStart[3] = gameGateCheckBox4.isSelected();
  }

  public void onRunGate5Clicked() {
    share.config.runGate.getStart[4] = gameGateCheckBox5.isSelected();
  }

  public void onRunGate6Clicked() {
    share.config.runGate.getStart[5] = gameGateCheckBox6.isSelected();
  }

  public void onRunGate7Clicked() {
    share.config.runGate.getStart[6] = gameGateCheckBox7.isSelected();
  }

  public void onRunGate8Clicked() {
    share.config.runGate.getStart[7] = gameGateCheckBox8.isSelected();
  }

  public void onSelGate1Clicked() {
    share.config.selGate.getStart1 = selGateCheckBox1.isSelected();
  }

  public void onSelGate2Clicked() {
    share.config.selGate.getStart2 = selGateCheckBox2.isSelected();
  }

  public void onLoginGateClicked() {
    share.config.loginGate.getStart = loginGateCheckBox.isSelected();
  }

  public void onLoginGate2Clicked() {
    //share.config.loginGate.getStart = loginGateCheckBox2.isSelected();
  }

  public void onPlugTopClicked() {
    share.config.plugTop.getStart = plugTopCheckBox.isSelected();
  }

  public void onStartGameClicked() {
    switch (startState) {
      case STOPPED_STATE:
        Dialogs.confirm("是否确认启动游戏服务器？")
            .ifPresent(buttonType -> startGame());
        break;
      case STARTING_STATE:
        Dialogs.confirm("是否确认中止启动游戏服务器？")
            .ifPresent(buttonType -> cancelStartGame());
        break;
      case RUNNING_STATE:
        Dialogs.confirm("是否确认停止游戏服务器？")
            .ifPresent(buttonType -> stopGame());
        break;
      case STOPPING_STATE:
        Dialogs.confirm("是否确认中止停止游戏服务器？")
            .ifPresent(buttonType -> cancelStopGame());
        break;
      case ERROR_STATE:
        break;
    }
  }

  private void cancelStopGame() {
    stopGameTimer.cancel();
    stopGameTimer = new Timer();
    startState = RUNNING_STATE;
    startGameButton.setText(share.textStopGame);
  }

  private void stopGame() {
    startGameButton.setText(share.textCancelStopGame);
    mainOutMessage("正在开始停止服务器...");
    // todo cancel task and do not new Timer
    checkRunTimer.cancel();
    checkRunTimer = new Timer();
    stopGameTimer.schedule(new StopGameTask(), 1000, 1000);
    gateStopped = false;
    startState = STOPPING_STATE;
  }

  private void cancelStartGame() {
    startGameTimer.cancel();
    startGameTimer = new Timer();
    startState = RUNNING_STATE;
    startGameButton.setText(share.textStopGame);
  }

  private void startGame() {
    runTick = System.currentTimeMillis();
    share.dbServer.getStart = share.config.dbServer.getStart;
    share.dbServer.reStart = true;
    share.dbServer.directory = share.gameDirectory + "DBServer\\";
    share.dbServer.programFile = share.config.dbServer.programFile;
    share.dbServer.mainFormX = share.config.dbServer.mainFormX;
    share.dbServer.mainFormY = share.config.dbServer.mainFormY;

    share.loginServer.getStart = share.config.loginSrv.getStart;
    share.loginServer.reStart = true;
    share.loginServer.directory = share.gameDirectory + "LoginSrv\\";
    share.loginServer.programFile = share.config.loginSrv.programFile;
    share.loginServer.mainFormX = share.config.loginSrv.mainFormX;
    share.loginServer.mainFormY = share.config.loginSrv.mainFormY;

    share.logServer.getStart = share.config.logServer.getStart;
    share.logServer.reStart = true;
    share.logServer.directory = share.gameDirectory + "LogServer\\";
    share.logServer.programFile = share.config.logServer.programFile;
    share.logServer.mainFormX = share.config.logServer.mainFormX;
    share.logServer.mainFormY = share.config.logServer.mainFormY;

    share.m2Server.getStart = share.config.m2Server.getStart;
    share.m2Server.reStart = true;
    share.m2Server.directory = share.gameDirectory + "Mir200\\";
    share.m2Server.programFile = share.config.m2Server.programFile;
    share.m2Server.mainFormX = share.config.m2Server.mainFormX;
    share.m2Server.mainFormY = share.config.m2Server.mainFormY;

    for (int i = 0; i < share.runGate.size(); i++) {
      share.runGate.get(i).startStatus = 0;
      share.runGate.get(i).getStart = share.config.runGate.getStart[i];
      share.runGate.get(i).reStart = true;
      share.runGate.get(i).directory = share.gameDirectory + "RunGate\\";
      share.runGate.get(i).programFile = share.config.runGate.programFile;
      if ((i + 1) % 2 == 0) {
        share.runGate.get(i).mainFormX = share.config.runGate.mainFormX + 276;
      } else {
        share.runGate.get(i).mainFormX = share.config.runGate.mainFormX;
      }
      if (i == 2 || i == 3 || i == 6 || i == 7) {
        share.runGate.get(i).mainFormY = share.config.runGate.mainFormY + 187;
      } else {
        share.runGate.get(i).mainFormY = share.config.runGate.mainFormY;
      }
    }

    share.selGate.getStart = share.config.selGate.getStart1;
    share.selGate.reStart = true;
    share.selGate.directory = share.gameDirectory + "SelGate\\";
    share.selGate.programFile = share.config.selGate.programFile;
    share.selGate.mainFormX = share.config.selGate.mainFormX;
    share.selGate.mainFormY = share.config.selGate.mainFormY;

    share.selGate1.getStart = share.config.selGate.getStart2;
    share.selGate1.reStart = true;
    share.selGate1.directory = share.gameDirectory + "SelGate\\";
    share.selGate1.programFile = share.config.selGate.programFile;
    share.selGate1.mainFormX = share.config.selGate.mainFormX;
    share.selGate1.mainFormY = share.config.selGate.mainFormY;

    share.loginGate.getStart = share.config.loginGate.getStart;
    share.loginGate.reStart = true;
    share.loginGate.directory = share.gameDirectory + "LoginGate\\";
    share.loginGate.programFile = share.config.loginGate.programFile;
    share.loginGate.mainFormX = share.config.loginGate.mainFormX;
    share.loginGate.mainFormY = share.config.loginGate.mainFormY;

    share.loginGate2.getStart = share.config.loginGate.getStart && share.ip2Enabled;
    share.loginGate2.reStart = true;
    share.loginGate2.directory = share.gameDirectory + "M2Server\\";
    share.loginGate2.programFile = share.config.loginGate.programFile;
    share.loginGate2.mainFormX = share.config.loginGate.mainFormX;
    share.loginGate2.mainFormY = share.config.loginGate.mainFormY;

    share.plugTop.getStart = share.config.plugTop.getStart;
    share.plugTop.reStart = true;
    share.plugTop.directory = share.gameDirectory + "Mir200\\";
    share.plugTop.programFile = share.config.plugTop.programFile;
    share.plugTop.mainFormX = share.config.plugTop.mainFormX;
    share.plugTop.mainFormY = share.config.plugTop.mainFormY;

    dbServerCheckBox.setSelected(share.config.dbServer.getStart);
    loginSrvCheckBox.setSelected(share.config.loginSrv.getStart);
    m2ServerCheckBox.setSelected(share.config.m2Server.getStart);
    logServerCheckBox.setSelected(share.config.logServer.getStart);
    loginGateCheckBox.setSelected(share.config.loginGate.getStart);
    selGateCheckBox1.setSelected(share.config.selGate.getStart1);
    selGateCheckBox2.setSelected(share.config.selGate.getStart2);
    gameGateCheckBox1.setSelected(share.config.runGate.getStart[0]);
    gameGateCheckBox2.setSelected(share.config.runGate.getStart[1]);
    gameGateCheckBox3.setSelected(share.config.runGate.getStart[2]);
    gameGateCheckBox4.setSelected(share.config.runGate.getStart[3]);
    gameGateCheckBox5.setSelected(share.config.runGate.getStart[4]);
    gameGateCheckBox6.setSelected(share.config.runGate.getStart[5]);
    gameGateCheckBox7.setSelected(share.config.runGate.getStart[6]);
    gameGateCheckBox8.setSelected(share.config.runGate.getStart[7]);

    startGameButton.setText(share.textCancelStartGame);
    startState = STARTING_STATE;

    startGameTimer.schedule(new StartGameTask(), 1000, 1000);
  }

  private void loadBackupList() {
    deleteBackupButton.setDisable(true);
    modifyBackupButton.setDisable(true);
    try {
      Path path = Paths.get(share.gameDirectory, share.backupListFile);
      if (Files.notExists(path)) {
        IOHelper.create(path);
      }
      Ini ini = new Wini(path.toFile());
      Collection<Profile.Section> sections = ini.values();
      int index = 0;
      for (Profile.Section section : sections) {
        String source = section.get("Source", "");
        String destination = section.get("Save", "");
        if (Strings.isNullOrEmpty(source) || Strings.isNullOrEmpty(destination)) {
          continue;
        }
        BackupManager.BackupObject object = new BackupManager.BackupObject();
        object.index = index;
        index++;
        object.sourceDir.set(source);
        object.destinationDir.set(destination);
        object.hours = section.get("Hour", Integer.class, 0);
        object.minutes = section.get("Min", Integer.class, 0);
        object.backupMode = section.get("BackMode", Integer.class, 0);
        object.backupEnabled = section.get("GetBack", Boolean.class, true);
        object.compressEnabled = section.get("Zip", Boolean.class, true);
        share.backupManager.addToList(object);
      }
    } catch (IOException e) {
      Dialogs.error("读取备份文件列表出错！", e).show();
    }
  }

  private void refBackupListToView() {
    // 不做任何事，因为我们已经在视图上绑定好了数据
  }

  public void onDestroy() {
    startGameTimer.cancel();
    stopGameTimer.cancel();
    checkRunTimer.cancel();
    if (share.backupManager != null) {
      share.backupManager.stop();
    }
  }

  public void onOpenLoginGateClicked() {
    share.config.loginGate.getStart = openLoginGateCheckBox.isSelected();
  }

  public void onOpenLoginGate2Clicked() {
    //share.config.loginGate.getStart = openLoginGateCheckBox2.isSelected();
  }

  public void onOpenSelGate1Clicked() {
    share.config.selGate.getStart1 = openSelGateCheckBox1.isSelected();
  }

  public void onOpenSelGate2Clicked() {
    share.config.selGate.getStart2 = openSelGateCheckBox2.isSelected();
  }

  public void onOpenRunGate1Clicked() {
    share.config.runGate.getStart[0] = openRunGateCheckBox1.isSelected();
  }

  public void onOpenRunGate2Clicked() {
    share.config.runGate.getStart[1] = openRunGateCheckBox2.isSelected();
  }

  public void onOpenRunGate3Clicked() {
    share.config.runGate.getStart[2] = openRunGateCheckBox3.isSelected();
  }

  public void onOpenRunGate4Clicked() {
    share.config.runGate.getStart[3] = openRunGateCheckBox4.isSelected();
  }

  public void onOpenRunGate5Clicked() {
    share.config.runGate.getStart[4] = openRunGateCheckBox5.isSelected();
  }

  public void onOpenRunGate6Clicked() {
    share.config.runGate.getStart[5] = openRunGateCheckBox6.isSelected();
  }

  public void onOpenRunGate7Clicked() {
    share.config.runGate.getStart[6] = openRunGateCheckBox7.isSelected();
  }

  public void onOpenRunGate8Clicked() {
    share.config.runGate.getStart[7] = openRunGateCheckBox8.isSelected();
  }

  public void onOpenLoginSrvClicked() {
    share.config.loginSrv.getStart = openLoginSrvCheckBox.isSelected();
  }

  public void onOpenDBServerClicked() {
    share.config.dbServer.getStart = openDbServerCheckBox.isSelected();
  }

  public void onOpenLogServerClicked() {
    share.config.loginSrv.getStart = openLogServerCheckBox.isSelected();
  }

  public void onOpenM2ServerClicked() {
    share.config.m2Server.getStart = openM2ServerCheckBox.isSelected();
  }

  public void onOpenPlugTopClicked() {
    share.config.plugTop.getStart = openPlugTopCheckBox.isSelected();
  }

  public void onReloadAllConfigClicked() {
    share.loadConfig();
    refGameConsole();
    Dialogs.alert("配置重加载完成...").show();
  }

  public void onNextBasicConfigClicked() {
    String gameDir = gameDirTextField.getText().trim();
    if (Strings.isNullOrEmpty(gameDir)) {
      Dialogs.warn("游戏目录输入不正确！！").show();
      gameDirTextField.requestFocus();
      return;
    }
    if (!gameDir.endsWith("\\")) {
      Dialogs.warn("游戏目录必须以“\\”结尾！！").show();
      gameDirTextField.requestFocus();
      return;
    }
    String gameName = gameNameTextField.getText().trim();
    if (Strings.isNullOrEmpty(gameName)) {
      Dialogs.warn("游戏名称输入不正确！！").show();
      gameNameTextField.requestFocus();
      return;
    }
    String dbName = dbNameTextField.getText().trim();
    if (Strings.isNullOrEmpty(dbName)) {
      Dialogs.warn("数据库名称输入不正确！！").show();
      dbNameTextField.requestFocus();
      return;
    }
    String ipAddress1 = primaryAddressTextField.getText().trim();
    if (Strings.isNullOrEmpty(ipAddress1) || !Networks.isAddressV4(ipAddress1)) {
      Dialogs.warn("游戏 IP 地址输入不正确！！").show();
      primaryAddressTextField.requestFocus();
      return;
    }
    boolean doubleAddress = doubleAddressCheckBox.isSelected();
    String ipAddress2 = secondAddressTextField.getText().trim();
    if (doubleAddress && (Strings.isNullOrEmpty(ipAddress2) || !Networks.isAddressV4(ipAddress2))) {
      Dialogs.warn("游戏 IP 地址输入不正确！！").show();
      secondAddressTextField.requestFocus();
      return;
    }

    share.gameDirectory = gameDir;
    share.gameName = gameName;
    share.heroDBName = dbName;
    share.extIPAddr = ipAddress1;
    share.ip2Enabled = doubleAddress;
    share.extIPAddr2 = ipAddress2;
    share.closeWuXinEnabled = closeWuxingCheckBox.isSelected();

    configTabPane.getSelectionModel().selectNext();
  }

  public void onDefaultBasicConfigClicked() {
    share.gameDirectory = DEFAULT_GAME_DIRECTORY;
    share.heroDBName = DEFAULT_DB_NAME;
    share.gameName = DEFAULT_GAME_NAME;
    share.extIPAddr = PRIMARY_IP_ADDRESS;
    share.extIPAddr2 = SECOND_IP_ADDRESS;
    share.autoRunBakEnabled = DEFAULT_AUTO_RUN_BACKUP;
    share.ip2Enabled = DEFAULT_IP_2_ENABLED;
    share.closeWuXinEnabled = DEFAULT_CLOSE_WUXING_ENABLED;
    refGameConsole();
  }

  public void onPreviousLoginGateConfigClicked() {
    configTabPane.getSelectionModel().selectPrevious();
  }

  public void onNextLoginGateConfigClicked() {
    int port = Integer.parseInt(loginGatePortTextField.getText().trim());
    if (!Networks.isPort(port)) {
      Dialogs.warn("网关端口设置错误！！").show();
      loginGatePortTextField.requestFocus();
      return;
    }
    share.config.loginGate.gatePort = port;
    configTabPane.getSelectionModel().selectNext();
  }

  public void onDefaultLoginGateConfigClicked() {
    share.config.loginGate = new Share.LoginGateConfig();
    refGameConsole();
  }

  public void onPreviousSelGateConfigClicked() {
    configTabPane.getSelectionModel().selectPrevious();
  }

  public void onNextSelGateConfigClicked() {
    int port1 = Integer.parseInt(selGatePortTextField1.getText().trim());
    if (!Networks.isPort(port1)) {
      Dialogs.warn("网关端口设置错误！！").show();
      selGatePortTextField1.requestFocus();
      return;
    }
    share.config.selGate.gatePort[0] = port1;

    int port2 = Integer.parseInt(selGatePortTextField2.getText().trim());
    if (!Networks.isPort(port2)) {
      Dialogs.warn("网关端口设置错误！！").show();
      selGatePortTextField2.requestFocus();
      return;
    }
    share.config.selGate.gatePort[1] = port2;

    configTabPane.getSelectionModel().selectNext();
  }

  public void onDefaultSelGateConfigClicked() {
    share.config.selGate = new Share.SelGateConfig();
    refGameConsole();
  }

  public void onPreviousRunGateConfigClicked() {
    configTabPane.getSelectionModel().selectPrevious();
  }

  public void onNextRunGateConfigClicked() {
    int port1 = Integer.parseInt(runGatePortTextField1.getText().trim());
    if (!Networks.isPort(port1)) {
      Dialogs.warn("网关一端口设置错误！！").show();
      runGatePortTextField1.requestFocus();
      return;
    }
    int port2 = Integer.parseInt(runGatePortTextField2.getText().trim());
    if (!Networks.isPort(port2)) {
      Dialogs.warn("网关二端口设置错误！！").show();
      runGatePortTextField2.requestFocus();
      return;
    }
    int port3 = Integer.parseInt(runGatePortTextField3.getText().trim());
    if (!Networks.isPort(port3)) {
      Dialogs.warn("网关三端口设置错误！！").show();
      runGatePortTextField3.requestFocus();
      return;
    }
    int port4 = Integer.parseInt(runGatePortTextField4.getText().trim());
    if (!Networks.isPort(port4)) {
      Dialogs.warn("网关四端口设置错误！！").show();
      runGatePortTextField4.requestFocus();
      return;
    }
    int port5 = Integer.parseInt(runGatePortTextField5.getText().trim());
    if (!Networks.isPort(port5)) {
      Dialogs.warn("网关五端口设置错误！！").show();
      runGatePortTextField5.requestFocus();
      return;
    }
    int port6 = Integer.parseInt(runGatePortTextField6.getText().trim());
    if (!Networks.isPort(port6)) {
      Dialogs.warn("网关六端口设置错误！！").show();
      runGatePortTextField6.requestFocus();
      return;
    }
    int port7 = Integer.parseInt(runGatePortTextField7.getText().trim());
    if (!Networks.isPort(port7)) {
      Dialogs.warn("网关七端口设置错误！！").show();
      runGatePortTextField7.requestFocus();
      return;
    }
    int port8 = Integer.parseInt(runGatePortTextField8.getText().trim());
    if (!Networks.isPort(port8)) {
      Dialogs.warn("网关八端口设置错误！！").show();
      runGatePortTextField8.requestFocus();
      return;
    }
    share.config.runGate.gatePort[0] = port1;
    share.config.runGate.gatePort[1] = port2;
    share.config.runGate.gatePort[2] = port3;
    share.config.runGate.gatePort[3] = port4;
    share.config.runGate.gatePort[4] = port5;
    share.config.runGate.gatePort[5] = port6;
    share.config.runGate.gatePort[6] = port7;
    share.config.runGate.gatePort[7] = port8;

    configTabPane.getSelectionModel().selectNext();
  }

  public void onDefaultRunGateConfigClicked() {
    share.config.runGate = new Share.RunGateConfig();
    refGameConsole();
  }

  public void onPreviousLoginSrvConfigClicked() {
    configTabPane.getSelectionModel().selectPrevious();
  }

  public void onNextLoginSrvConfigClicked() {
    int gatePort = Integer.parseInt(loginSrvGatePortTextField.getText().trim());
    if (!Networks.isPort(gatePort)) {
      Dialogs.warn("网关端口设置错误！！").show();
      loginGatePortTextField.requestFocus();
      return;
    }
    int serverPort = Integer.parseInt(loginSrvServerPortTextField.getText().trim());
    if (!Networks.isPort(serverPort)) {
      Dialogs.warn("通讯端口设置错误！！").show();
      loginSrvServerPortTextField.requestFocus();
      return;
    }
    int monPort = Integer.parseInt(loginSrvMonPortTextField.getText().trim());
    if (!Networks.isPort(monPort)) {
      Dialogs.warn("监控端口设置错误！！").show();
      loginSrvMonPortTextField.requestFocus();
      return;
    }

    share.config.loginSrv.gatePort = gatePort;
    share.config.loginSrv.serverPort = serverPort;
    share.config.loginSrv.monPort = monPort;

    configTabPane.getSelectionModel().selectNext();
  }

  public void onDefaultLoginSrvConfigClicked() {
    share.config.loginSrv = new Share.LoginSrvConfig();
    refGameConsole();
  }

  public void onPreviousDbServerConfigClicked() {
    configTabPane.getSelectionModel().selectPrevious();
  }

  public void onNextDbServerConfigClicked() {
    int gatePort = Integer.parseInt(dbServerGatePortTextField.getText().trim());
    if (!Networks.isPort(gatePort)) {
      Dialogs.warn("网关端口设置错误！！").show();
      dbServerGatePortTextField.requestFocus();
      return;
    }
    int serverPort = Integer.parseInt(dbServerServerPortTextField.getText().trim());
    if (!Networks.isPort(serverPort)) {
      Dialogs.warn("通讯端口设置错误！！").show();
      dbServerServerPortTextField.requestFocus();
      return;
    }

    share.config.dbServer.gatePort = gatePort;
    share.config.dbServer.serverPort = serverPort;

    configTabPane.getSelectionModel().selectNext();
  }

  public void onDefaultDbServerConfigClicked() {
    share.config.dbServer = new Share.DBServerConfig();
    refGameConsole();
  }

  public void onPreviousLogServerConfigClicked() {
    configTabPane.getSelectionModel().selectPrevious();
  }

  public void onNextLogServerConfigClicked() {
    int port = Integer.parseInt(logServerGatePortTextField.getText().trim());
    if (!Networks.isPort(port)) {
      Dialogs.warn("端口设置错误！！").show();
      logServerGatePortTextField.requestFocus();
      return;
    }
    share.config.logServer.port = port;

    configTabPane.getSelectionModel().selectNext();
  }

  public void onDefaultLogServerConfigClicked() {
    share.config.logServer = new Share.LogServerConfig();
    refGameConsole();
  }

  public void onPreviousM2ServerConfigClicked() {
    configTabPane.getSelectionModel().selectPrevious();
  }

  public void onNextM2ServerConfigClicked() {
    int gatePort = Integer.parseInt(m2ServerGatePortTextField.getText().trim());
    if (!Networks.isPort(gatePort)) {
      Dialogs.warn("网关端口设置错误！！").show();
      m2ServerGatePortTextField.requestFocus();
      return;
    }
    int serverPort = Integer.parseInt(m2ServerServerPortTextField.getText().trim());
    if (!Networks.isPort(serverPort)) {
      Dialogs.warn("通讯端口设置错误！！").show();
      m2ServerServerPortTextField.requestFocus();
      return;
    }
    share.config.m2Server.gatePort = gatePort;
    share.config.m2Server.msgSrvPort = serverPort;

    configTabPane.getSelectionModel().selectNext();
  }

  public void onDefaultM2ServerConfigClicked() {
    share.config.m2Server = new Share.M2ServerConfig();
    refGameConsole();
  }

  public void onPreviousPlugTopConfigClicked() {
    configTabPane.getSelectionModel().selectPrevious();
  }

  public void onNextPlugTopConfigClicked() {
    configTabPane.getSelectionModel().selectNext();
  }

  public void onDefaultPlugTopConfigClicked() {
    share.config.plugTop = new Share.PlugTopConfig();
    refGameConsole();
  }

  public void onPreviousSaveConfigClicked() {
    configTabPane.getSelectionModel().selectPrevious();
  }

  public void onSaveConfigClicked() {
    share.saveConfig();
    Dialogs.alert("配置文件已经保存完毕...")
        .showAndWait()
        .filter(ButtonType.OK::equals)
        .flatMap(buttonType -> Dialogs.confirm("是否生成新的游戏服务器配置文件？"))
        .ifPresent(buttonType -> {
          onGenerateConfigClicked();
          configTabPane.getSelectionModel().selectFirst();
          mainTabPane.getSelectionModel().selectFirst();
        });
  }

  public void onGenerateConfigClicked() {
    generateGameConfig();
    refGameConsole();
    Dialogs.alert("引擎配置文件已经生成完毕...").show();
  }

  private void generateGameConfig() {
    IOHelper.mkdir(Paths.get(share.gameDirectory));
    generateDBServerConfig();
    generateLoginServerConfig();
    generateM2ServerConfig();
    generateLogServerConfig();
    generateRunGateConfig();
    generateSelGateConfig();
    generateLoginGateConfig();
  }

  private void generateMultiRunGateConfig(int index) {
    if (index > 0 && index < MAX_RUN_GATE_COUNT) {
      Path runGateDir = Paths.get(share.gameDirectory, "RunGate\\");
      IOHelper.mkdir(runGateDir);

      try {
        Path runGateConfigPath = Paths.get(runGateDir.toString(), SERVER_CONFIG_FILE);
        IOHelper.create(runGateConfigPath);
        Ini ini = new Wini(runGateConfigPath.toFile());
        ini.put(RUN_GATE_SECTION_NAME_2, "Title", share.gameName);
        ini.put(RUN_GATE_SECTION_NAME_2, "GateAddr", ALL_IP_ADDRESS);
        ini.put(RUN_GATE_SECTION_NAME_2, "GatePort", share.config.runGate.gatePort[index]);
        ini.store();
      } catch (IOException e) {
        Dialogs.error("生成游戏网关[" + (index + 1) + "]配置出错！！", e).show();
      }
    }
  }

  private void generateMultiSelGateConfig(int index) {
    if (index != 0 && index != 1) {
      return;
    }
    Path selGateDir = Paths.get(share.gameDirectory, "SelGate\\");
    IOHelper.mkdir(selGateDir);

    try {
      Path selGateConfigPath = Paths.get(selGateDir.toString(), SERVER_CONFIG_FILE);
      IOHelper.create(selGateConfigPath);
      Ini ini = new Wini(selGateConfigPath.toFile());
      ini.put(SEL_GATE_SECTION_NAME_2, "Title", share.gameName);
      ini.put(SEL_GATE_SECTION_NAME_2, "GateAddr", ALL_IP_ADDRESS);
      ini.put(SEL_GATE_SECTION_NAME_2, "GatePort", share.config.selGate.gatePort[index]);
      if (share.ip2Enabled) {
        if (index == 0) {
          ini.put(SEL_GATE_SECTION_NAME_2, "ServerAddr", PRIMARY_IP_ADDRESS);
        } else {
          ini.put(SEL_GATE_SECTION_NAME_2, "ServerAddr", SECOND_IP_ADDRESS);
        }
      }
      ini.store();
    } catch (IOException e) {
      Dialogs.error("生成角色网关[" + index + "]出错！！", e).show();
    }
  }

  private void generateMultiLoginGateConfig(int index) {
    if (index != 0 && index != 1) {
      return;
    }
    Path loginGateDir = Paths.get(share.gameDirectory, "LoginGate\\");
    IOHelper.mkdir(loginGateDir);

    try {
      Path loginGateConfigPath = Paths.get(loginGateDir.toString(), SERVER_CONFIG_FILE);
      IOHelper.create(loginGateConfigPath);
      Ini ini = new Wini(loginGateConfigPath.toFile());
      ini.put(LOGIN_SRV_SECTION_NAME_2, "Title", share.gameName);
      ini.put(LOGIN_SRV_SECTION_NAME_2, "GatePort", share.config.loginGate.gatePort);
      if (share.ip2Enabled) {
        if (index == 0) {
          ini.put(LOGIN_SRV_SECTION_NAME_2, "GateAddr", share.extIPAddr);
          ini.put(LOGIN_SRV_SECTION_NAME_2, "ServerAddr", PRIMARY_IP_ADDRESS);
        } else {
          ini.put(LOGIN_SRV_SECTION_NAME_2, "GateAddr", share.extIPAddr2);
          ini.put(LOGIN_SRV_SECTION_NAME_2, "ServerAddr", SECOND_IP_ADDRESS);
        }
      } else {
        ini.put(LOGIN_SRV_SECTION_NAME_2, "GateAddr", ALL_IP_ADDRESS);
      }
      ini.store();
    } catch (IOException e) {
      Dialogs.error("生成角色网关[" + index + "]出错！！", e).show();
    }
  }

  private void generateLoginGateConfig() {
    Path loginGateDir = Paths.get(share.gameDirectory, "LoginGate\\");
    IOHelper.mkdir(loginGateDir);

    try {
      Path configPath = Paths.get(loginGateDir.toString(), SERVER_CONFIG_FILE);
      IOHelper.create(configPath);
      Ini ini = new Wini(configPath.toFile());
      ini.put(LOGIN_GATE_SECTION_NAME_2, "Title", share.gameName);
      ini.put(LOGIN_GATE_SECTION_NAME_2, "ServerAddr", PRIMARY_IP_ADDRESS);
      ini.put(LOGIN_GATE_SECTION_NAME_2, "ServerPort", share.config.loginSrv.gatePort);
      ini.put(LOGIN_GATE_SECTION_NAME_2, "GateAddr", ALL_IP_ADDRESS);
      ini.put(LOGIN_GATE_SECTION_NAME_2, "GatePort", share.config.loginGate.gatePort);
      ini.store();
    } catch (IOException e) {
      Dialogs.error("生成登陆网关配置文件出错！！", e).show();
    }
  }

  private void generateSelGateConfig() {
    Path selGateDir = Paths.get(share.gameDirectory, "SelGate\\");
    IOHelper.mkdir(selGateDir);

    try {
      Path configPath = Paths.get(selGateDir.toString(), SERVER_CONFIG_FILE);
      IOHelper.create(configPath);
      Ini ini = new Wini(configPath.toFile());
      ini.put(SEL_GATE_SECTION_NAME_2, "Title", share.gameName);
      ini.put(SEL_GATE_SECTION_NAME_2, "ServerAddr", PRIMARY_IP_ADDRESS);
      ini.put(SEL_GATE_SECTION_NAME_2, "ServerPort", share.config.dbServer.gatePort);
      ini.put(SEL_GATE_SECTION_NAME_2, "GateAddr", ALL_IP_ADDRESS);
      ini.put(SEL_GATE_SECTION_NAME_2, "GatePort", share.config.selGate.gatePort[0]);
      ini.store();
    } catch (IOException e) {
      Dialogs.error("生成角色网关配置文件出错！！", e).show();
    }
  }

  private void generateRunGateConfig() {
    Path runGateDir = Paths.get(share.gameDirectory, "RunGate\\");
    IOHelper.mkdir(runGateDir);

    try {
      Path configPath = Paths.get(runGateDir.toString(), SERVER_CONFIG_FILE);
      IOHelper.create(configPath);
      Ini ini = new Wini(configPath.toFile());
      ini.put(RUN_GATE_SECTION_NAME_2, "Title", share.gameName);
      ini.put(RUN_GATE_SECTION_NAME_2, "ServerAddr", PRIMARY_IP_ADDRESS);
      ini.put(RUN_GATE_SECTION_NAME_2, "ServerPort", share.config.m2Server.gatePort);
      ini.put(RUN_GATE_SECTION_NAME_2, "GateAddr", ALL_IP_ADDRESS);
      ini.put(RUN_GATE_SECTION_NAME_2, "GatePort", share.config.runGate.gatePort[0]);
      ini.put(RUN_GATE_SECTION_NAME_2, "CenterAddr", PRIMARY_IP_ADDRESS);
      ini.put(RUN_GATE_SECTION_NAME_2, "CenterPort", share.config.loginSrv.serverPort);
      ini.store();
    } catch (IOException e) {
      Dialogs.error("生成游戏网关配置文件出错！！", e).show();
    }
  }

  private void generateLogServerConfig() {
    Path logSrvDir = Paths.get(share.gameDirectory, "LogServer\\");
    IOHelper.mkdir(logSrvDir);

    try {
      Path logSrvPath = Paths.get(logSrvDir.toString(), "LogData.ini");
      IOHelper.create(logSrvPath);
      Ini ini = new Wini(logSrvPath.toFile());
      ini.put(LOG_SERVER_SECTION_2, "ServerName", share.gameName);
      ini.put(LOG_SERVER_SECTION_2, "Port", share.config.logServer.port);
      ini.put(LOG_SERVER_SECTION_2, "BaseDir", "BaseDir\\");
      ini.store();
    } catch (IOException e) {
      Dialogs.error("生成日志服务器配置文件出错！！", e).show();
    }

    IOHelper.mkdir(Paths.get(logSrvDir.toString(), "BaseDir\\"));
  }

  private void generateM2ServerConfig() {
    Path m2SrvDir = Paths.get(share.gameDirectory, "Mir200\\");
    IOHelper.mkdir(m2SrvDir);

    try {
      Path m2SrvPath = Paths.get(m2SrvDir.toString(), M2_SERVER_CONFIG_FILE);
      IOHelper.create(m2SrvPath);
      Ini ini = new Wini(m2SrvPath.toFile());
      ini.put(M2_SERVER_SECTION_NAME_1, "ServerName", share.gameName);
      ini.put(M2_SERVER_SECTION_NAME_1, "DBName", share.heroDBName);
      ini.put(M2_SERVER_SECTION_NAME_1, "GateAddr", ALL_IP_ADDRESS);
      ini.put(M2_SERVER_SECTION_NAME_1, "GatePort", share.config.m2Server.gatePort);
      ini.put(M2_SERVER_SECTION_NAME_1, "DBAddr", PRIMARY_IP_ADDRESS);
      ini.put(M2_SERVER_SECTION_NAME_1, "DBPort", share.config.dbServer.serverPort);
      ini.put(M2_SERVER_SECTION_NAME_1, "IDSAddr", PRIMARY_IP_ADDRESS);
      ini.put(M2_SERVER_SECTION_NAME_1, "IDSPort", share.config.loginSrv.serverPort);
      ini.put(M2_SERVER_SECTION_NAME_1, "MsgSrvAddr", ALL_IP_ADDRESS);
      ini.put(M2_SERVER_SECTION_NAME_1, "MsgSrvPort", share.config.m2Server.msgSrvPort);
      ini.put(M2_SERVER_SECTION_NAME_1, "LogServerAddr", PRIMARY_IP_ADDRESS);
      ini.put(M2_SERVER_SECTION_NAME_1, "LogServerPort", share.config.logServer.port);
      ini.put(M2_SERVER_SECTION_NAME_1, "CloseWuXin", share.closeWuXinEnabled);

      ini.put(M2_SERVER_SECTION_NAME_2, "GuildDir", "GuildBase\\Guilds\\");
      ini.put(M2_SERVER_SECTION_NAME_2, "GuildFile", "GuildBase\\GuildList.txt");
      ini.put(M2_SERVER_SECTION_NAME_2, "ConLogDir", "ConLog\\");
      ini.put(M2_SERVER_SECTION_NAME_2, "CastleDir", "Castle\\");
      ini.put(M2_SERVER_SECTION_NAME_2, "CastleFile", "Castle\\List.txt");
      ini.put(M2_SERVER_SECTION_NAME_2, "GameDataDir", "Envir\\");
      ini.put(M2_SERVER_SECTION_NAME_2, "EnvirDir", "Envir\\");
      ini.put(M2_SERVER_SECTION_NAME_2, "MapDir", "Map\\");
      ini.put(M2_SERVER_SECTION_NAME_2, "NoticeDir", "Notice\\");
      ini.put(M2_SERVER_SECTION_NAME_2, "LogDir", "Log\\");
      ini.put(M2_SERVER_SECTION_NAME_2, "EMailDir", "EMail\\");
      ini.store();
    } catch (IOException e) {
      Dialogs.error("生成服务端核心配置出错！！", e).show();
    }

    // todo 批量创建文件
    IOHelper.mkdir(Paths.get(m2SrvDir.toString(), "GuildBase\\"));
    IOHelper.mkdir(Paths.get(m2SrvDir.toString(), "GuildBase\\Guilds\\"));
    IOHelper.mkdir(Paths.get(m2SrvDir.toString(), "ConLog\\"));
    IOHelper.mkdir(Paths.get(m2SrvDir.toString(), "Castle\\"));
    IOHelper.mkdir(Paths.get(m2SrvDir.toString(), "Envir\\"));
    IOHelper.mkdir(Paths.get(m2SrvDir.toString(), "Map\\"));
    IOHelper.mkdir(Paths.get(m2SrvDir.toString(), "Notice\\"));
    IOHelper.mkdir(Paths.get(m2SrvDir.toString(), "Log\\"));
    IOHelper.mkdir(Paths.get(m2SrvDir.toString(), "EMail\\"));

    IOHelper.write(Paths.get(m2SrvDir.toString(), "!servertable.txt"), PRIMARY_IP_ADDRESS);
  }

  private void generateLoginServerConfig() {
    Path loginSrvDir = Paths.get(share.gameDirectory, "LoginSrv\\");
    IOHelper.mkdir(loginSrvDir);

    try {
      Path loginSrvPath = Paths.get(loginSrvDir.toString(), "Logsrv.ini");
      IOHelper.create(loginSrvPath);
      Ini ini = new Wini(loginSrvPath.toFile());
      ini.put(LOGIN_SRV_SECTION_NAME_2, "ServerAddr", ALL_IP_ADDRESS);
      ini.put(LOGIN_SRV_SECTION_NAME_2, "ServerPort", share.config.loginSrv.serverPort);
      ini.put(LOGIN_SRV_SECTION_NAME_2, "GateAddr", ALL_IP_ADDRESS);
      ini.put(LOGIN_SRV_SECTION_NAME_2, "GatePort", share.config.loginSrv.gatePort);
      ini.put(LOGIN_SRV_SECTION_NAME_2, "MonAddr", ALL_IP_ADDRESS);
      ini.put(LOGIN_SRV_SECTION_NAME_2, "MonPort", share.config.loginSrv.monPort);
      ini.put(LOGIN_SRV_SECTION_NAME_2, "CloseWuXin", share.closeWuXinEnabled);
      ini.put(LOGIN_SRV_SECTION_NAME_2, "IDDir", "DB\\");
      ini.put(LOGIN_SRV_SECTION_NAME_2, "CountLogDir", "ChrLog\\");
      ini.store();
    } catch (IOException e) {
      Dialogs.error("生成登陆服务器配置文件出错", e).show();
    }

    StringBuilder builder = new StringBuilder(PRIMARY_IP_ADDRESS);
    if (share.ip2Enabled) {
      builder.append(System.lineSeparator()).append(SECOND_IP_ADDRESS);
    }
    IOHelper.write(Paths.get(loginSrvDir.toString(), "!serveraddr.txt"), builder.toString());

    String content = String.format("%s %s %d", share.gameName, share.gameName, ONLINE_USER_LIMIT);
    IOHelper.write(Paths.get(loginSrvDir.toString(), "!UserLimit.txt"), content);

    builder = new StringBuilder(PRIMARY_IP_ADDRESS);
    if (share.config.selGate.getStart1) {
      builder.append(String.format(" %s %d", share.extIPAddr, share.config.selGate.gatePort[0]));
    }
    builder.append(System.lineSeparator());
    if (share.ip2Enabled) {
      builder.append(SECOND_IP_ADDRESS);
      if (share.config.selGate.getStart2) {
        builder.append(String.format(" %s %d", share.extIPAddr2, share.config.selGate.gatePort[1]));
      }
    } else {
      if (share.config.selGate.getStart2) {
        builder.append(String.format(" %s %d", share.extIPAddr, share.config.selGate.gatePort[1]));
      }
    }
    IOHelper.write(Paths.get(loginSrvDir.toString(), "!addrtable.txt"), builder.toString());
    IOHelper.mkdir(Paths.get(loginSrvDir.toString(), "ChrLog\\"));
    IOHelper.mkdir(Paths.get(loginSrvDir.toString(), "DB\\"));
  }

  private void generateDBServerConfig() {
    Path dbServerDir = Paths.get(share.gameDirectory, "DBServer\\");
    IOHelper.mkdir(dbServerDir);
    Path dbFileDir = Paths.get(dbServerDir.toString(), "DB\\");
    IOHelper.mkdir(dbFileDir);

    try {
      Path dbSrcPath = Paths.get(dbServerDir.toString(), "Dbsrc.ini");
      IOHelper.create(dbSrcPath);
      Ini ini = new Wini(dbSrcPath.toFile());
      ini.put(DB_SERVER_SECTION_NAME_2, "ServerName", share.gameName);
      ini.put(DB_SERVER_SECTION_NAME_2, "ServerAddr", PRIMARY_IP_ADDRESS);
      ini.put(DB_SERVER_SECTION_NAME_2, "ServerPort", share.config.dbServer.serverPort);
      ini.put(DB_SERVER_SECTION_NAME_2, "GateAddr", ALL_IP_ADDRESS);
      ini.put(DB_SERVER_SECTION_NAME_2, "GatePort", share.config.dbServer.gatePort);
      ini.put(DB_SERVER_SECTION_NAME_2, "IDSAddr", PRIMARY_IP_ADDRESS);
      ini.put(DB_SERVER_SECTION_NAME_2, "IDSPort", share.config.loginSrv.serverPort);
      ini.put(DB_SERVER_SECTION_NAME_2, "DBName", share.heroDBName);
      ini.put(DB_SERVER_SECTION_NAME_2, "DBDir", "DB\\");
      ini.store();
    } catch (IOException e) {
      Dialogs.error("生成数据库服务器配置文件出错！！", e).show();
    }

    StringBuilder builder = new StringBuilder(PRIMARY_IP_ADDRESS);
    if (share.ip2Enabled) {
      builder.append(System.lineSeparator()).append(SECOND_IP_ADDRESS);
    }
    IOHelper.write(Paths.get(dbServerDir.toString(), "!addrtable.txt"), builder.toString());

    builder = new StringBuilder(PRIMARY_IP_ADDRESS);
    for (int i = 0; i < share.config.runGate.getStart.length; i++) {
      if (share.config.runGate.getStart[i]) {
        builder.append(
            String.format(" %s %d", share.extIPAddr, share.config.runGate.gatePort[i]));
      }
    }
    builder.append(System.lineSeparator());
    if (share.ip2Enabled) {
      builder.append(SECOND_IP_ADDRESS);
      for (int i = 0; i < share.config.runGate.getStart.length; i++) {
        if (share.config.runGate.getStart[i]) {
          builder.append(
              String.format(" %s %d", share.extIPAddr2, share.config.runGate.gatePort[i]));
        }
      }
    }
    IOHelper.write(Paths.get(dbServerDir.toString(), "!serverinfo.txt"), builder.toString());
    IOHelper.write(Paths.get(dbServerDir.toString(), "FUserName.txt"), ";创建人物过滤字符，一行一个过滤");
  }

  public void onModifyBackupClicked() {
    BackupManager.BackupObject object = dataBackupTableView.getSelectionModel().getSelectedItem();
    if (object != null) {
      String source = dataDirectoryTextField.getText().trim();
      if (Strings.isNullOrEmpty(source)) {
        Dialogs.warn("请选择数据目录！！").show();
        return;
      }
      String destination = backupDirectoryTextField.getText().trim();
      if (Strings.isNullOrEmpty(destination)) {
        Dialogs.warn("请选择备份目录！！").show();
        return;
      }
      int hours;
      int minutes;
      if (dayBackupModeRadioButton.isSelected()) {
        hours = dayModeHoursSpinner.getValue();
        minutes = dayModeMinutesSpinner.getValue();
      } else {
        hours = intervalModeHoursSpinner.getValue();
        minutes = intervalModeMinutesSpinner.getValue();
      }
      object.sourceDir.set(source);
      object.destinationDir.set(destination);
      object.hours = hours;
      object.minutes = minutes;
      object.backupEnabled = backupFunctionCheckBox.isSelected();
      object.compressEnabled = compressFunctionCheckBox.isSelected();
      if (dayBackupModeRadioButton.isSelected()) {
        object.backupMode = 0;
      } else {
        object.backupMode = 1;
      }
      Dialogs.alert("修改成功！！").show();
    }
  }

  public void onDeleteBackupClicked() {
    BackupManager.BackupObject object = dataBackupTableView.getSelectionModel().getSelectedItem();
    if (object != null) {
      share.backupManager.backupList.remove(object);
      Dialogs.alert("删除成功！").show();
    } else {
      Dialogs.alert("删除失败！").show();
    }
  }

  public void onAddBackupClicked() {
    String source = dataDirectoryTextField.getText().trim();
    if (Strings.isNullOrEmpty(source)) {
      Dialogs.warn("请选择数据目录！！").show();
      return;
    }
    String destination = backupDirectoryTextField.getText().trim();
    if (Strings.isNullOrEmpty(destination)) {
      Dialogs.warn("请选择备份目录！！").show();
      return;
    }
    if (share.backupManager.findObject(source).isPresent()) {
      Dialogs.warn("此数据目录已在备份列表内！！");
      return;
    }
    int hours;
    int minutes;
    if (dayBackupModeRadioButton.isSelected()) {
      hours = dayModeHoursSpinner.getValue();
      minutes = dayModeMinutesSpinner.getValue();
    } else {
      hours = intervalModeHoursSpinner.getValue();
      minutes = intervalModeMinutesSpinner.getValue();
    }
    BackupManager.BackupObject object = new BackupManager.BackupObject();
    object.index = share.backupManager.backupList.size();
    object.sourceDir.set(source);
    object.destinationDir.set(destination);
    object.hours = hours;
    object.minutes = minutes;
    object.backupEnabled = backupFunctionCheckBox.isSelected();
    object.compressEnabled = compressFunctionCheckBox.isSelected();
    if (dayBackupModeRadioButton.isSelected()) {
      object.backupMode = 0;
    } else {
      object.backupMode = 1;
    }
    share.backupManager.backupList.add(object);
    refBackupListToView();
    Dialogs.alert("增加成功！！").show();
  }

  public void onSaveBackupClicked() {
    saveBackupButton.setDisable(true);
    Path path = Paths.get(share.gameDirectory, share.backupListFile);
    try {
      IOHelper.delete(path);
      IOHelper.create(path);
      Ini ini = new Wini(path.toFile());
      for (int i = 0; i < share.backupManager.backupList.size(); i++) {
        BackupManager.BackupObject object = share.backupManager.backupList.get(i);
        ini.put(String.valueOf(i), "Source", object.sourceDir.get());
        ini.put(String.valueOf(i), "Save", object.destinationDir.get());
        ini.put(String.valueOf(i), "Hour", object.hours);
        ini.put(String.valueOf(i), "Min", object.minutes);
        ini.put(String.valueOf(i), "BackMode", object.backupMode);
        ini.put(String.valueOf(i), "GetBack", object.backupEnabled);
        ini.put(String.valueOf(i), "Zip", object.compressEnabled);
      }
      ini.store();
    } catch (IOException e) {
      Dialogs.error("保存备份配置失败！", e).show();
    }
    Dialogs.alert("保存成功！").show();
    saveBackupButton.setDisable(false);
  }

  public void onStartBackupClicked() {
    switch (share.backupStartStatus) {
      case 0:
        share.backupStartStatus = 1;
        startBackupButton.setText("停止");
        share.backupManager.start();
        backupMessageLabel.setTextFill(Color.GREEN);
        backupMessageLabel.setText("数据备份功能启动中...");
        break;
      case 1:
        share.backupStartStatus = 0;
        startBackupButton.setText("启动");
        share.backupManager.stop();
        backupMessageLabel.setTextFill(Color.RED);
        backupMessageLabel.setText("数据备份功能已停止...");
        break;
    }
  }

  public void onAutoRunBackupClicked() {
    share.autoRunBakEnabled = autoRunBackupCheckBox.isSelected();
    share.ini.put(BASIC_SECTION_NAME, "AutoRunBak", share.autoRunBakEnabled);
    try {
      share.ini.store();
    } catch (IOException e) {
      LOGGER.error("保存备份配置失败！", e);
    }
  }

  public void onChooseDataDirectoryClicked() {
    DirectoryChooser chooser = new DirectoryChooser();
    chooser.setTitle("请选择你要备份的数据目录");
    chooser.setInitialDirectory(new File(share.gameDirectory));
    File file = chooser.showDialog(null);
    if (file != null) {
      dataDirectoryTextField.setText(file.getAbsolutePath());
    }
  }

  public void onChooseBackupDirectoryClicked() {
    DirectoryChooser chooser = new DirectoryChooser();
    chooser.setTitle("请选择备份文件的输出目录");
    chooser.setInitialDirectory(new File(share.gameDirectory));
    File file = chooser.showDialog(null);
    if (file != null) {
      backupDirectoryTextField.setText(file.getAbsolutePath());
    }
  }

  public void onStartClearDataClicked() {
    if (startState == STOPPED_STATE) {
      startClearDataButton.setDisable(true);
      File homeDirectory = new File(share.gameDirectory);
      if (deleteRoleDataCheckBox.isSelected()) {
        IOHelper.delete(Paths.get(homeDirectory.getPath(), "DBServer\\DB\\Hum.DB"));
        IOHelper.delete(Paths.get(homeDirectory.getPath(), "DBServer\\DB\\Mir.DB"));
        IOHelper.delete(Paths.get(homeDirectory.getPath(), "DBServer\\DB\\Mir.DB.idx"));
      }
      if (deleteAccountDataCheckBox.isSelected()) {
        IOHelper.delete(Paths.get(homeDirectory.getPath(), "LoginSrv\\DB\\Id.DB"));
        IOHelper.delete(Paths.get(homeDirectory.getPath(), "LoginSrv\\DB\\Id.DB.idx"));
      }
      if (deleteGuildDataCheckBox.isSelected()) {
        IOHelper.deleteAll(Paths.get(homeDirectory.getPath(), "Mir200\\GuildBase\\Guilds\\"));
        Path guildListPath = Paths.get(homeDirectory.getPath(), "Mir200\\GuildBase\\GuildList.txt");
        if (Files.exists(guildListPath)) {
          IOHelper.write(guildListPath, "");
        }
      }
      if (clearSabacDataCheckBox.isSelected()) {
        List<String> castleList =
            IOHelper.lines(Paths.get(homeDirectory.getPath(), "Mir200\\Castle\\List.txt"));
        castleList.stream()
            .map(s -> String.format("Mir200\\Castle\\%s\\AttackSabukWall.txt", s))
            .map(s -> Paths.get(homeDirectory.getPath(), s))
            .forEach(file -> IOHelper.write(file, ""));
        castleList.stream()
            .map(s -> String.format("Mir200\\Castle\\%s\\SabukW.txt", s))
            .map(s -> new File(homeDirectory, s))
            .filter(File::exists)
            .forEach(file -> {
              try {
                Ini ini = new Wini(file);
                ini.put("Setup", "OwnGuild", "");
                ini.put("Setup", "ChangeDate", "");
                ini.put("Setup", "WarDate", "");
                ini.put("Setup", "IncomeToday", "");
                ini.put("Setup", "TotalGold", "");
                ini.put("Setup", "TodayIncome", "");

                ini.put("Defense", "MainDoorHP", "10000");
                ini.put("Defense", "LeftWallHP", "5000");
                ini.put("Defense", "CenterWallHP", "5000");
                ini.put("Defense", "RightWallHP", "5000");
                ini.put("Defense", "Archer_1_HP", "2000");
                ini.put("Defense", "Archer_2_HP", "2000");
                ini.put("Defense", "Archer_3_HP", "2000");
                ini.put("Defense", "Archer_4_HP", "2000");
                ini.put("Defense", "Archer_5_HP", "2000");
                ini.put("Defense", "Archer_6_HP", "2000");
                ini.put("Defense", "Archer_7_HP", "2000");
                ini.put("Defense", "Archer_8_HP", "2000");
                ini.put("Defense", "Archer_9_HP", "2000");
                ini.put("Defense", "Archer_10_HP", "2000");
                ini.put("Defense", "Archer_11_HP", "2000");
                ini.put("Defense", "Archer_12_HP", "2000");
                ini.store();
              } catch (IOException e) {
                LOGGER.error("清理沙巴克数据的配置文件出错！", e);
              }
            });
      }
      if (clearGlobalVariateCheckBox.isSelected()) {
        IOHelper.delete(Paths.get(homeDirectory.getPath(), "Mir200\\Global.ini"));
      }
      if (resetItemIDCountCheckBox.isSelected()) {
        File mir2SetupFile = new File(homeDirectory, "Mir200\\!Setup.txt");
        if (mir2SetupFile.exists()) {
          try {
            Ini ini = new Wini(mir2SetupFile);
            ini.put("Setup", "ItemNumber", 10000);
            ini.put("Setup", "ItemNumberEx", 2000000000);
            ini.store();
          } catch (IOException e) {
            LOGGER.error("复位物品 ID 计数出错！", e);
          }
        }
      }
      if (clearRoleRelationDataCheckBox.isSelected()) {
        IOHelper.write(Paths.get(homeDirectory.getPath(), "Mir200\\Envir\\UnForceMaster.txt"), "");
        IOHelper.write(Paths.get(homeDirectory.getPath(), "Mir200\\Envir\\UnFriend.txt"), "");
        IOHelper.write(Paths.get(homeDirectory.getPath(), "Mir200\\Envir\\UnMarry.txt"), "");
        IOHelper.write(Paths.get(homeDirectory.getPath(), "Mir200\\Envir\\UnMaster.txt"), "");
      }
      if (deleteNPCMakeDataCheckBox.isSelected()) {
        IOHelper.deleteAll(Paths.get(homeDirectory.getPath(), "Mir200\\Envir\\Market_Upg\\"));
      }
      if (deleteEMailDataCheckBox.isSelected()) {
        IOHelper.delete(Paths.get(homeDirectory.getPath(), "Mir200\\EMail\\EMailData.dat"));
        IOHelper.delete(Paths.get(homeDirectory.getPath(), "Mir200\\EMail\\EMailName.txt"));
      }
      if (deleteAccountLoggerCheckBox.isSelected()) {
        IOHelper.deleteAll(Paths.get(homeDirectory.getPath(), "LoginSrv\\ChrLog\\"));
      }
      if (deleteM2ServerLoggerCheckBox.isSelected()) {
        IOHelper.deleteAll(Paths.get(homeDirectory.getPath(), "Mir200\\Log\\"));
        IOHelper.deleteAll(Paths.get(homeDirectory.getPath(), "Mir200\\ConLog\\"));
      }
      if (deleteGameLoggerCheckBox.isSelected()) {
        IOHelper.deleteAll(Paths.get(homeDirectory.getPath(), "LogServer\\BaseDir\\"));
      }
      startClearDataButton.setDisable(false);
      Dialogs.alert("全部清理完成！").show();
    } else {
      Dialogs.warn("请将服务器处于停止状态下再进行操作！").show();
    }
  }

  public enum StartMode {
    NORMAL("正常启动"),
    DELAY("延时启动"),
    TIMING("定时启动"),
    ;

    public final String name;

    StartMode(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return name;
    }
  }

  public class StartGameTask extends TimerTask {
    @Override
    public void run() {
      if (share.dbServer.getStart) {
        switch (share.dbServer.startStatus) {
          case 0:
            share.dbServer.disposable = share.dbServer.start()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(Controller.this::handleProcessMessage);
            share.dbServer.startStatus = 1;
            return;
          case 1:
            LOGGER.debug("正在等待数据库服务器启动..");
            return;
        }
      }
      // todo 重构为 RxJava 的 Interval 数据流
      if (share.loginServer.getStart) {
        switch (share.loginServer.startStatus) {
          case 0:
            share.loginServer.disposable = share.loginServer.start()
                .observeOn(JavaFxScheduler.platform())
                .doOnError(throwable -> {
                  Dialogs.error(throwable).show();
                  share.loginServer.startStatus = 9;
                })
                .subscribe(Controller.this::handleProcessMessage);
            share.loginServer.startStatus = 1;
            return;
          case 1:
            LOGGER.debug("正在等待登陆服务器启动..");
            return;
        }
      }
      if (share.logServer.getStart) {
        switch (share.logServer.startStatus) {
          case 0:
            share.logServer.disposable = share.logServer.start()
                .observeOn(JavaFxScheduler.platform())
                .doOnError(throwable -> {
                  Dialogs.error(throwable).show();
                  share.logServer.startStatus = 9;
                })
                .subscribe(Controller.this::handleProcessMessage);
            share.logServer.startStatus = 1;
            return;
          case 1:
            LOGGER.debug("正在等待日志服务器启动..");
            return;
        }
      }
      if (share.m2Server.getStart) {
        switch (share.m2Server.startStatus) {
          case 0:
            share.m2Server.disposable = share.m2Server.start()
                .observeOn(JavaFxScheduler.platform())
                .doOnError(throwable -> {
                  Dialogs.error(throwable).show();
                  share.m2Server.startStatus = 9;
                })
                .subscribe(Controller.this::handleProcessMessage);
            share.m2Server.startStatus = 1;
            return;
          case 1:
            LOGGER.debug("正在等待核心服务器启动..");
            return;
        }
      }
      if (getStartRunGate()) {
        return;
      }
      boolean startRunGateOK = true;
      for (int i = 0; i < share.runGate.size(); i++) {
        Share.Program runGateProgram = share.runGate.get(i);
        if (runGateProgram.getStart) {
          if (runGateProgram.startStatus == 0) {
            generateMultiRunGateConfig(i);
            runGateProgram.disposable = runGateProgram.start()
                .subscribeOn(JavaFxScheduler.platform())
                .doOnError(throwable -> {
                  Dialogs.error(throwable).show();
                  runGateProgram.startStatus = 9;
                })
                .subscribe(Controller.this::handleProcessMessage);
            runGateProgram.startStatus = 1;
            startRunGateOK = false;
          }
        }
      }
      if (!startRunGateOK) {
        LOGGER.debug("正在等待游戏网关全部启动..");
        return;
      }
      if (share.selGate.getStart) {
        switch (share.selGate.startStatus) {
          case 0:
            generateMultiSelGateConfig(0);
            share.selGate.disposable = share.selGate.start()
                .observeOn(JavaFxScheduler.platform())
                .doOnError(throwable -> {
                  Dialogs.error(throwable).show();
                  share.selGate.startStatus = 9;
                })
                .subscribe(Controller.this::handleProcessMessage);
            share.selGate.startStatus = 1;
            return;
          case 1:
            LOGGER.debug("正在等待角色网关一启动..");
            return;
        }
      }
      if (share.selGate1.getStart) {
        switch (share.selGate1.startStatus) {
          case 0:
            generateMultiSelGateConfig(1);
            share.selGate1.disposable = share.selGate1.start()
                .observeOn(JavaFxScheduler.platform())
                .doOnError(throwable -> {
                  Dialogs.error(throwable).show();
                  share.selGate1.startStatus = 9;
                })
                .subscribe(Controller.this::handleProcessMessage);
            share.selGate1.startStatus = 1;
            return;
          case 1:
            LOGGER.debug("正在等待角色网关二启动..");
            return;
        }
      }

      StartMode startMode = startModeComboBox.getValue();
      if (StartMode.DELAY.equals(startMode)) {
        // 是否已经等了那么久
        if ((System.currentTimeMillis() - runTick) < runTime) {
          return;
        }
      } else if (StartMode.TIMING.equals(startMode)) {
        // 是否到达指定时间
        if (System.currentTimeMillis() < runTime) {
          return;
        }
      }

      if (share.loginGate.getStart) {
        switch (share.loginGate.startStatus) {
          case 0:
            generateMultiLoginGateConfig(0);
            share.loginGate.disposable = share.loginGate.start()
                .observeOn(JavaFxScheduler.platform())
                .doOnError(throwable -> {
                  Dialogs.error(throwable).show();
                  share.loginGate.startStatus = 9;
                })
                .subscribe(Controller.this::handleProcessMessage);
            share.loginGate.startStatus = 1;
            return;
          case 1:
            LOGGER.debug("正在等待登陆网关一启动..");
            return;
        }
      }
      if (share.loginGate2.getStart) {
        switch (share.loginGate2.startStatus) {
          case 0:
            generateMultiLoginGateConfig(1);
            share.loginGate2.disposable = share.loginGate2.start()
                .observeOn(JavaFxScheduler.platform())
                .doOnError(throwable -> {
                  Dialogs.error(throwable).show();
                  share.loginGate2.startStatus = 9;
                })
                .subscribe(Controller.this::handleProcessMessage);
            share.loginGate2.startStatus = 1;
            return;
          case 1:
            LOGGER.debug("正在等待登陆网关一启动..");
            return;
        }
      }

      if (share.plugTop.getStart) {
        switch (share.plugTop.startStatus) {
          case 0:
            share.plugTop.disposable = share.plugTop.start()
                .observeOn(JavaFxScheduler.platform())
                .doOnError(throwable -> {
                  Dialogs.error(throwable).show();
                  share.plugTop.startStatus = 9;
                })
                .subscribe(Controller.this::handleProcessMessage);
            share.plugTop.startStatus = 1;
            return;
          case 1:
            LOGGER.debug("正在等待游戏排行榜插件启动..");
            return;
        }
      }

      startGameTimer.cancel();
      startGameTimer = new Timer();
      checkRunTimer.schedule(new CheckRunTask(), 1000, 1000);
      Platform.runLater(() -> startGameButton.setText(share.textStopGame));
      startState = RUNNING_STATE;
    }

    private boolean getStartRunGate() {
      for (int i = 0; i < share.runGate.size(); i++) {
        Share.Program program = share.runGate.get(i);
        if (program.getStart && program.startStatus == 1) {
          return true;
        }
      }
      return false;
    }
  }

  private void handleProcessMessage(String message) {
    String[] split = message.split(":", 2);
    int code = Integer.parseInt(split[0]);
    UUID processCode = UUID.fromString(split[1]);
    switch (code) {
      case DB_SERVER_PROCESS_CODE:
        if (share.dbServer.processCode == null) {
          share.dbServer.processCode = processCode;
          mainOutMessage("正在启动数据库服务器...");
          return;
        }
        share.dbServer.startStatus = 2;
        mainOutMessage("启动数据库服务器成功！");
        break;
      case LOGIN_SERVER_PROCESS_CODE:
        if (share.loginServer.processCode == null) {
          share.loginServer.processCode = processCode;
          mainOutMessage("正在启动账号登陆服务器...");
          return;
        }
        share.loginServer.startStatus = 2;
        mainOutMessage("启动账号登陆服务器成功！");
        break;
      case LOG_SERVER_PROCESS_CODE:
        if (share.logServer.processCode == null) {
          share.logServer.processCode = processCode;
          mainOutMessage("正在启动日志服务器...");
          return;
        }
        share.logServer.startStatus = 2;
        mainOutMessage("启动日志服务器成功！");
        break;
      case M2_SERVER_PROCESS_CODE:
        if (share.m2Server.processCode == null) {
          share.m2Server.processCode = processCode;
          mainOutMessage("正在启动游戏引擎服务器...");
          return;
        }
        share.m2Server.startStatus = 2;
        mainOutMessage("启动游戏引擎服务器成功！");
        break;
      case LOGIN_GATE_PROCESS_CODE:
        if (share.loginGate.getStart && share.loginGate.startStatus == 1) {
          if (share.loginGate.processCode == null) {
            share.loginGate.processCode = processCode;
            mainOutMessage("正在启动登陆网关一...");
            return;
          }
          if (processCode.equals(share.loginGate.processCode)) {
            share.loginGate.startStatus = 2;
            mainOutMessage("启动登陆网关一成功！");
            return;
          }
        }
        if (share.loginGate2.getStart && share.loginGate2.startStatus == 1) {
          if (share.loginGate2.processCode == null) {
            share.loginGate2.processCode = processCode;
            mainOutMessage("正在启动登陆网关二...");
            return;
          }
          if (processCode.equals(share.loginGate2.processCode)) {
            share.loginGate2.startStatus = 2;
            mainOutMessage("启动登陆网关二成功！");
            return;
          }
        }
        break;
      case SEL_GATE_PROCESS_CODE:
        if (share.selGate.getStart && share.selGate.startStatus == 1) {
          if (share.selGate.processCode == null) {
            share.selGate.processCode = processCode;
            mainOutMessage("正在启动角色网关一...");
            return;
          }
          if (processCode.equals(share.selGate.processCode)) {
            share.selGate.startStatus = 2;
            mainOutMessage("启动角色网关一成功！");
            return;
          }
        }
        if (share.selGate1.getStart && share.selGate1.startStatus == 1) {
          if (share.selGate1.processCode == null) {
            share.selGate1.processCode = processCode;
            mainOutMessage("正在启动角色网关二...");
            return;
          }
          if (processCode.equals(share.selGate.processCode)) {
            share.selGate1.startStatus = 2;
            mainOutMessage("启动角色网关二成功！");
            return;
          }
        }
        break;
      case RUN_GATE_PROCESS_CODE:
        for (int i = 0; i < share.runGate.size(); i++) {
          Share.Program program = share.runGate.get(i);
          if (program.getStart && program.startStatus == 1) {
            if (program.processCode == null) {
              program.processCode = processCode;
              mainOutMessage("正在启动游戏网关[" + (i + 1) + "]...");
              return;
            }
            if (processCode.equals(program.processCode)) {
              program.startStatus = 2;
              mainOutMessage("启动游戏网关[" + (i + 1) + "]成功！");
              return;
            }
          }
        }
        break;
      case PLUG_TOP_PROCESS_CODE:
        if (share.plugTop.processCode == null) {
          share.plugTop.processCode = processCode;
          mainOutMessage("正在启动游戏排行榜引擎...");
          return;
        }
        share.plugTop.startStatus = 2;
        mainOutMessage("启动游戏排行榜引擎成功！");
        break;
    }
  }

  public class CheckRunTask extends TimerTask {
    @Override public void run() {
      if (share.dbServer.getStart) {
        if (share.dbServer.process == null || !share.dbServer.process.isAlive()) {
          share.dbServer.disposable = share.dbServer.start()
              .observeOn(JavaFxScheduler.platform())
              .subscribe(Controller.this::handleProcessMessage);
          mainOutMessage("数据库异常关闭，已被重新启动...");
        }
      }
      if (share.loginServer.getStart) {
        if (share.loginServer.process == null || !share.loginServer.process.isAlive()) {
          share.loginServer.disposable = share.loginServer.start()
              .observeOn(JavaFxScheduler.platform())
              .subscribe(Controller.this::handleProcessMessage);
          mainOutMessage("登录服务器异常关闭，已被重新启动...");
        }
      }
      if (share.logServer.getStart) {
        if (share.logServer.process == null || !share.logServer.process.isAlive()) {
          share.logServer.disposable = share.logServer.start()
              .observeOn(JavaFxScheduler.platform())
              .subscribe(Controller.this::handleProcessMessage);
          mainOutMessage("日志服务器异常关闭，已被重新启动...");
        }
      }
      if (share.m2Server.getStart) {
        if (share.m2Server.process == null || !share.m2Server.process.isAlive()) {
          share.m2Server.disposable = share.m2Server.start()
              .observeOn(JavaFxScheduler.platform())
              .subscribe(Controller.this::handleProcessMessage);
          mainOutMessage("游戏引擎服务器异常关闭，已被重新启动...");
        }
      }
      for (int i = 0; i < share.runGate.size(); i++) {
        Share.Program program = share.runGate.get(i);
        if (program.getStart) {
          if (program.process == null || !program.process.isAlive()) {
            generateMultiRunGateConfig(i);
            program.processCode = null;
            program.disposable = program.start()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(Controller.this::handleProcessMessage);
            mainOutMessage("游戏网关[" + (i + 1) + "]异常关闭，已被重新启动...");
          }
        }
      }
      if (share.selGate.getStart) {
        if (share.selGate.process == null || !share.selGate.process.isAlive()) {
          generateMultiSelGateConfig(0);
          share.selGate.processCode = null;
          share.selGate.disposable = share.selGate.start()
              .observeOn(JavaFxScheduler.platform())
              .subscribe(Controller.this::handleProcessMessage);
          mainOutMessage("角色网关一异常关闭，已被重新启动...");
        }
      }
      if (share.selGate1.getStart) {
        if (share.selGate1.process == null || !share.selGate1.process.isAlive()) {
          generateMultiSelGateConfig(1);
          share.selGate1.processCode = null;
          share.selGate1.disposable = share.selGate1.start()
              .observeOn(JavaFxScheduler.platform())
              .subscribe(Controller.this::handleProcessMessage);
          mainOutMessage("角色网关二异常关闭，已被重新启动...");
        }
      }
      if (share.loginGate.getStart) {
        if (share.loginGate.process == null || !share.loginGate.process.isAlive()) {
          generateMultiLoginGateConfig(0);
          share.loginGate.processCode = null;
          share.loginGate.disposable = share.loginGate.start()
              .observeOn(JavaFxScheduler.platform())
              .subscribe(Controller.this::handleProcessMessage);
          mainOutMessage("登录网关一异常关闭，已被重新启动...");
        }
      }
      if (share.loginGate2.getStart) {
        if (share.loginGate2.process == null || !share.loginGate2.process.isAlive()) {
          generateMultiLoginGateConfig(1);
          share.loginGate2.processCode = null;
          share.loginGate2.disposable = share.loginGate2.start()
              .observeOn(JavaFxScheduler.platform())
              .subscribe(Controller.this::handleProcessMessage);
          mainOutMessage("登录网关二异常关闭，已被重新启动...");
        }
      }
      if (share.plugTop.getStart) {
        if (share.plugTop.process == null || !share.plugTop.process.isAlive()) {
          share.plugTop.processCode = null;
          share.plugTop.disposable = share.plugTop.start()
              .observeOn(JavaFxScheduler.platform())
              .subscribe(Controller.this::handleProcessMessage);
          mainOutMessage("排行榜插件异常关闭，已被重新启动...");
        }
      }
    }
  }

  public class StopGameTask extends TimerTask {

    @Override public void run() {
      if (share.loginGate.getStart && share.loginGate.startStatus > 1) {
        if (share.loginGate.process != null && share.loginGate.process.isAlive()) {
          if (share.loginGate.startStatus == 3) {
            if ((System.currentTimeMillis() - share.stopTick) > share.stopTimeout) {
              share.loginGate.stop();
              mainOutMessage("正常关闭超时，登陆网关一已被强行停止...");
            }
            return;
          }
          share.loginGate.sendMessage(QUIT_CODE);
          share.stopTick = System.currentTimeMillis();
          share.loginGate.startStatus = 3;
          return;
        } else {
          share.loginGate.startStatus = 0;
          mainOutMessage("登陆网关一已停止...");
        }
      }

      if (share.loginGate2.getStart && share.loginGate2.startStatus > 1) {
        if (share.loginGate2.process != null && share.loginGate2.process.isAlive()) {
          if (share.loginGate2.startStatus == 3) {
            if ((System.currentTimeMillis() - share.stopTick) > share.stopTimeout) {
              share.loginGate2.stop();
              mainOutMessage("正常关闭超时，登陆网关二已被强行停止...");
            }
            return;
          }
          share.loginGate2.sendMessage(QUIT_CODE);
          share.stopTick = System.currentTimeMillis();
          share.loginGate2.startStatus = 3;
          return;
        } else {
          share.loginGate2.startStatus = 0;
          mainOutMessage("登陆网关二已停止...");
        }
      }

      if (share.selGate.getStart && share.selGate.startStatus > 1) {
        if (share.selGate.process != null && share.selGate.process.isAlive()) {
          if (share.selGate.startStatus == 3) {
            if ((System.currentTimeMillis() - share.stopTick) > share.stopTimeout) {
              share.selGate.stop();
              mainOutMessage("正常关闭超时，角色网关一已被强行停止...");
            }
            return;
          }
          share.selGate.sendMessage(QUIT_CODE);
          share.stopTick = System.currentTimeMillis();
          share.selGate.startStatus = 3;
          return;
        } else {
          share.selGate.startStatus = 0;
          mainOutMessage("角色网关一已停止...");
        }
      }

      if (share.selGate1.getStart && share.selGate1.startStatus > 1) {
        if (share.selGate1.process != null && share.selGate1.process.isAlive()) {
          if (share.selGate1.startStatus == 3) {
            if ((System.currentTimeMillis() - share.stopTick) > share.stopTimeout) {
              share.selGate1.stop();
              mainOutMessage("正常关闭超时，角色网关二已被强行停止...");
            }
            return;
          }
          share.selGate1.sendMessage(QUIT_CODE);
          share.stopTick = System.currentTimeMillis();
          share.selGate1.startStatus = 3;
          return;
        } else {
          share.selGate1.startStatus = 0;
          mainOutMessage("角色网关二已停止...");
        }
      }

      for (int i = 0; i < share.runGate.size(); i++) {
        Share.Program program = share.runGate.get(i);
        if (program.getStart && program.startStatus > 1) {
          if (program.process != null && program.process.isAlive()) {
            if (program.startStatus == 3) {
              if ((System.currentTimeMillis() - share.stopTick) > share.stopTimeout) {
                program.stop();
                mainOutMessage("正常关闭超时，游戏网关[" + (i + 1) + "]已被强行停止...");
              }
              return;
            }
            program.sendMessage(QUIT_CODE);
            // fixme 全局变量不能用于多个游戏网关的判断，这里会出现 bug
            share.stopTick = System.currentTimeMillis();
            program.startStatus = 3;
            return;
          } else {
            program.startStatus = 0;
            mainOutMessage("游戏网关[" + (i + 1) + "]已停止...");
          }
        }
      }

      if (getStopRunGate()) {
        gateStopped = false;
        return;
      }

      if (share.m2Server.getStart && share.m2Server.startStatus > 1) {
        if (!gateStopped) {
          gateStopped = true;
          gateStopTick = System.currentTimeMillis() + 5000;
          mainOutMessage("网关已全部关闭，延时5秒关闭游戏引擎...");
          return;
        }
        if (gateStopTick > System.currentTimeMillis()) {
          return;
        }

        if (share.m2Server.process != null && share.m2Server.process.isAlive()) {
          if (share.m2Server.startStatus == 3) {
            return;
          }
          share.m2Server.sendMessage(QUIT_CODE);
          share.stopTick = System.currentTimeMillis();
          share.m2Server.startStatus = 3;
          return;
        } else {
          share.m2Server.startStatus = 0;
          mainOutMessage("游戏引擎主程序已停止...");
        }
      }

      if (share.loginServer.getStart && share.loginServer.startStatus > 1) {
        if (share.loginServer.process != null && share.loginServer.process.isAlive()) {
          if (share.loginServer.startStatus == 3) {
            if ((System.currentTimeMillis() - share.stopTick) > share.stopTimeout) {
              // todo 1000 delay
              share.loginServer.stop();
              mainOutMessage("正常关闭超时，登陆服务器已被强行停止...");
            }
            return;
          }
          share.loginServer.sendMessage(QUIT_CODE);
          share.stopTick = System.currentTimeMillis();
          share.loginServer.startStatus = 3;
          return;
        } else {
          share.loginServer.startStatus = 0;
          mainOutMessage("登陆服务器已停止...");
        }
      }

      if (share.logServer.getStart && share.logServer.startStatus > 1) {
        if (share.logServer.process != null && share.logServer.process.isAlive()) {
          if (share.logServer.startStatus == 3) {
            if ((System.currentTimeMillis() - share.stopTick) > share.stopTimeout) {
              share.logServer.stop();
              mainOutMessage("正常关闭超时，日志服务器已被强行停止...");
            }
            return;
          }
          share.logServer.sendMessage(QUIT_CODE);
          share.stopTick = System.currentTimeMillis();
          share.logServer.startStatus = 3;
          return;
        } else {
          share.logServer.startStatus = 0;
          mainOutMessage("日志服务器已停止...");
        }
      }

      if (share.dbServer.getStart && share.dbServer.startStatus > 1) {
        if (share.dbServer.process != null && share.dbServer.process.isAlive()) {
          if (share.dbServer.startStatus == 3) {
            if ((System.currentTimeMillis() - share.stopTick) > share.stopTimeout) {
              share.dbServer.stop();
              mainOutMessage("正常关闭超时，数据库服务器已被强行停止...");
            }
            return;
          }
          share.dbServer.sendMessage(QUIT_CODE);
          share.stopTick = System.currentTimeMillis();
          share.dbServer.startStatus = 3;
          return;
        } else {
          share.dbServer.startStatus = 0;
          mainOutMessage("数据库服务器已停止...");
        }
      }

      mainOutMessage("所有程序停止完毕！");
      stopGameTimer.cancel();
      stopGameTimer = new Timer();
      Platform.runLater(() -> startGameButton.setText(share.textStartGame));
      startState = STOPPED_STATE;
    }

    private boolean getStopRunGate() {
      for (int i = 0; i < share.runGate.size(); i++) {
        Share.Program program = share.runGate.get(i);
        if (program.getStart && program.startStatus > 1) {
          return true;
        }
      }
      return false;
    }
  }
}
