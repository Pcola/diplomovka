package sk.spu.diplomovka;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import  com.fazecast.jSerialComm.SerialPort;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private ComboBox comboboxComPort;
    @FXML
    private ComboBox comboboxBaudRate;
    @FXML
    private ComboBox comboboxDataBits;
    @FXML
    private ComboBox comboboxStopBits;
    @FXML
    private ComboBox comboboxParityBits;
    @FXML
    public ProgressBar progressBarComStatus;
    @FXML
    private Button buttonOpen;
    @FXML
    private Button buttonClose;
    @FXML
    private Button buttonSend;
    @FXML
    private TextArea textArea;
    @FXML
    private ComboBox comboboxEndLine;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboboxBaudRate.getItems().add("4800");
        comboboxBaudRate.getItems().add("9600");
        comboboxBaudRate.getItems().add("38400");
        comboboxBaudRate.getItems().add("57600");
        comboboxBaudRate.getItems().add("115200");

        comboboxDataBits.getItems().add("6");
        comboboxDataBits.getItems().add("7");
        comboboxDataBits.getItems().add("8");

        comboboxStopBits.getItems().add("1");
        comboboxStopBits.getItems().add("1.5");
        comboboxStopBits.getItems().add("2");

        comboboxParityBits.getItems().add("NO_PARITY");
        comboboxParityBits.getItems().add("EVEN_PARITY");
        comboboxParityBits.getItems().add("ODD_PARITY");
        comboboxParityBits.getItems().add("MARK_PARITY");
        comboboxParityBits.getItems().add("SPACE_PARITY");

        comboboxEndLine.getItems().add("None");
        comboboxEndLine.getItems().add("New Line");
        comboboxEndLine.getItems().add("Carriage Return");
        comboboxEndLine.getItems().add("Both");

        comboboxBaudRate.getSelectionModel().select(1);
        comboboxDataBits.getSelectionModel().select(2);
        comboboxStopBits.getSelectionModel().select(0);
        comboboxParityBits.getSelectionModel().select(0);
        comboboxEndLine.getSelectionModel().select(0);
        comboboxComPort.setDisable(false);

        progressBarComStatus.setProgress(0);
        buttonOpen.setDisable(false);
        buttonClose.setDisable(true);
        buttonSend.setDisable(true);

        loadPorts();
    }

    public void loadPorts(){
        SerialPort []portList = SerialPort.getCommPorts();
        for(SerialPort port : portList){
            comboboxComPort.getItems().add(port);
        }
    }
}