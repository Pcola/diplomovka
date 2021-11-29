package sk.spu.diplomovka;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import  com.fazecast.jSerialComm.SerialPort;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {
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
    public TextFlow textflow;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboboxComPort.getItems().addAll(loadPorts());
        comboboxBaudRate.getItems().addAll("4800","9600","38400","57600","115200");
        comboboxDataBits.getItems().addAll("6","7","8");
        comboboxStopBits.getItems().addAll("1","1.5","2");
        comboboxParityBits.getItems().addAll("NO_PARITY","EVEN_PARITY","ODD_PARITY","MARK_PARITY","SPACE_PARITY");

        comboboxBaudRate.getSelectionModel().select(1);
        comboboxDataBits.getSelectionModel().select(2);
        comboboxStopBits.getSelectionModel().select(0);
        comboboxParityBits.getSelectionModel().select(0);
        comboboxComPort.setDisable(false);
        progressBarComStatus.setProgress(0);
        buttonOpen.setDisable(false);
        buttonClose.setDisable(true);
        buttonSend.setDisable(true);

        buttonOpen.getStyleClass().setAll("btn","btn-success");
        buttonClose.getStyleClass().setAll("btn","btn-default");
        buttonSend.getStyleClass().setAll("btn","btn-default");

        loadPorts();
    }

    public SerialPort[] loadPorts(){
        SerialPort []portList = SerialPort.getCommPorts();
        return portList;
    }

    public void connectToPort(ActionEvent event){
        try {
            serialPort1 =loadPorts()[comboboxComPort.getSelectionModel().getSelectedIndex()];
            serialPort1.setBaudRate(Integer.parseInt(comboboxBaudRate.getSelectionModel().getSelectedItem().toString()));
            serialPort1.setNumDataBits(Integer.parseInt(comboboxDataBits.getSelectionModel().getSelectedItem().toString()));
            serialPort1.setNumStopBits(Integer.parseInt(comboboxStopBits.getSelectionModel().getSelectedItem().toString()));
            serialPort1.setParity(comboboxParityBits.getSelectionModel().getSelectedIndex());
            serialPort1.openPort();

            if (serialPort1.isOpen()){
                comboboxComPort.setDisable(true);
                progressBarComStatus.setProgress(1);
                buttonOpen.setDisable(true);
                buttonClose.setDisable(false);
                buttonSend.setDisable(false);

                buttonOpen.getStyleClass().setAll("btn","btn-default");
                buttonClose.getStyleClass().setAll("btn","btn-danger");
                buttonSend.getStyleClass().setAll("btn","btn-success");

                Text msg = new Text(serialPort1.getDescriptivePortName() + " Success to OPEN");
                showTextFlow(msg,"success");
            }
            else{
                Text msg = new Text(serialPort1.getDescriptivePortName() + " Failed to OPEN");
                showTextFlow(msg,"danger");
            }
        }
        catch (ArrayIndexOutOfBoundsException exception){
            Text msg = new Text("Please choose port! " + exception.getMessage());
            showTextFlow(msg,"danger");
        }
        catch (Exception exception){
            Text msg = new Text(exception.getMessage());
            showTextFlow(msg,"danger");
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

            buttonOpen.getStyleClass().setAll("btn","btn-success");
            buttonClose.getStyleClass().setAll("btn","btn-default");
            buttonSend.getStyleClass().setAll("btn","btn-default");
        }
    }

    public void sendData(ActionEvent event){
        outputStream = serialPort1.getOutputStream();
        String dataToSend = textArea.getText() + "\r\n";

        try{
            outputStream.write(dataToSend.getBytes());
        }
        catch (IOException exception){
            Text msg = new Text(exception.getMessage());
            showTextFlow(msg,"danger");
        }
    }

    public void hideTextFlow(){
        PauseTransition visiblePause = new PauseTransition(
                Duration.seconds(3)
        );
        visiblePause.setOnFinished(
                event -> textflow.setVisible(false)
        );
        visiblePause.play();
    }

    private void showTextFlow(Text msg,String type){
        textflow.setVisible(true);
        textflow.getChildren().clear();
        textflow.getStyleClass().setAll("alert","alert-"+type);
        textflow.getChildren().add(msg);
        hideTextFlow();
    }
}