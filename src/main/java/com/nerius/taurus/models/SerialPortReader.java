package com.nerius.taurus.models;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Arrays;

@Component

public class SerialPortReader {

    private boolean isHumanHere;

    private int humanWentOutCounter = 0;
    public boolean startPort(OnSensorAnswerReceived onSensorAnswerReceived) {
        SerialPort comPort = SerialPort.getCommPort("COM8");
        comPort.setBaudRate(9600);

        if (comPort.openPort()) {
            System.out.println("Port opened successfully.");
            comPort.flushIOBuffers();
        } else {
            System.out.println("Unable to open the port.");
            return false;
        }

        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                    return;
                }
                byte[] newData = new byte[comPort.bytesAvailable()];
                comPort.readBytes(newData, newData.length);
                System.err.println(newData[0]);
                if (newData[0] == 49){
                    humanWentOutCounter = 0;
                    if (!isHumanHere){
                        onSensorAnswerReceived.onHumanCame();
                    }
                    isHumanHere = true;
                }else {
                    humanWentOutCounter++;
                    if (humanWentOutCounter >= 30 && isHumanHere){
                        humanWentOutCounter = 0;
                        isHumanHere = false;
                        onSensorAnswerReceived.onHumanGoOut();
                    }
                }
            }
        });
        return true;
    }


    public interface OnSensorAnswerReceived {
        void onHumanCame();

        void onHumanGoOut();
    }
}
