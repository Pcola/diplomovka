package sk.spu.diplomovka;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import  com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    SerialPort serialPort1;
    OutputStream outputStream;

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

    public void connectToPort(ActionEvent event){
        Alert alert;
        try {
            SerialPort []portLists = SerialPort.getCommPorts();
            serialPort1 = portLists[comboboxComPort.getSelectionModel().getSelectedIndex()];
            serialPort1.setBaudRate(Integer.parseInt(comboboxBaudRate.getSelectionModel().getSelectedItem().toString()));
            serialPort1.setNumDataBits(Integer.parseInt(comboboxDataBits.getSelectionModel().getSelectedItem().toString()));
            serialPort1.setNumStopBits(Integer.parseInt(comboboxStopBits.getSelectionModel().getSelectedItem().toString()));
            serialPort1.setParity(comboboxParityBits.getSelectionModel().getSelectedIndex());
            serialPort1.openPort();

            if (serialPort1.isOpen()){
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Open serial port");
                alert.setContentText(serialPort1.getDescriptivePortName() + " Success to OPEN");
                alert.show();

                comboboxComPort.setDisable(true);
                progressBarComStatus.setProgress(1);
                buttonOpen.setDisable(true);
                buttonClose.setDisable(false);
                buttonSend.setDisable(false);
            }
            else{
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("FAILED");
                alert.setContentText(serialPort1.getDescriptivePortName() + " Failed to OPEN");
                alert.show();
            }
        }
        catch (ArrayIndexOutOfBoundsException exception){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Please choose port! \n" + exception.getMessage());
            alert.show();
        }
        catch (Exception exception){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText(exception.getMessage());
            alert.show();
        }
    }

    public void closePort(){
        if(serialPort1.isOpen()){
            serialPort1.closePort();

            comboboxComPort.setDisable(false);
            progressBarComStatus.setProgress(0);
            buttonOpen.setDisable(false);
            buttonClose.setDisable(true);
            buttonSend.setDisable(true);
        }
    }

    public void sendData(ActionEvent event){
        outputStream = serialPort1.getOutputStream();
        String dataToSend = textArea.getText() + "\r\n";

        try{
            outputStream.write(dataToSend.getBytes());
        }
        catch (IOException exception){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText(exception.getMessage());
            alert.show();
        }
    }
}