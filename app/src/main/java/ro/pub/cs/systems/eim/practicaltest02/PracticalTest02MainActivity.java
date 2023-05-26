package ro.pub.cs.systems.eim.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class PracticalTest02MainActivity extends AppCompatActivity {

    private ServerThread serverThread;

    private Button buttonConnect;
    private Button buttonAdd;
    private Button buttonMul;
    private EditText editTextPortClient;
    private EditText editTextPortServer;
    private EditText editTextAddress;
    private EditText editTextOperand1;
    private EditText editTextOperand2;
    private TextView textViewResult;

    private class ConnectButtonListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = editTextPortServer.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }

            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }
    }


    private class OperationButtonListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {

            // Retrieves the client address and port. Checks if they are empty or not
            //  Checks if the server thread is alive. Then creates a new client thread with the address, port, city and information type
            //  and starts it
            String clientAddress = editTextAddress.getText().toString();
            String clientPort = editTextPortClient.getText().toString();
            if (clientAddress.isEmpty() || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            String operation = "";
            if (view.getId() == R.id.buttonAdd) {
                operation = "add";
            } else {
                operation = "mul";
            }
            String operand1 = editTextOperand1.getText().toString();
            String operand2 = editTextOperand2.getText().toString();
            if (operation.isEmpty() || operand1.isEmpty() || operand2.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }

            textViewResult.setText(Constants.EMPTY_STRING);

            String body = operation + "," + operand1 + "," + operand2 + "\n";

            ClientThread clientThread = new ClientThread(clientAddress, Integer.parseInt(clientPort), body, textViewResult);
            clientThread.start();
        }
    }

    private ConnectButtonListener connectButtonListener = new ConnectButtonListener();
    private OperationButtonListener operationButtonListener = new OperationButtonListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        Log.i(Constants.TAG, "Started the main activity");

        buttonConnect = findViewById(R.id.buttonConnect);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonMul = findViewById(R.id.buttonMul);
        editTextPortClient = findViewById(R.id.editTextPortClient);
        editTextPortServer = findViewById(R.id.editTextPortServer);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextOperand1 = findViewById(R.id.editTextOperand1);
        editTextOperand2 = findViewById(R.id.editTextOperand2);
        textViewResult = findViewById(R.id.textViewResult);

        buttonConnect.setOnClickListener(connectButtonListener);
        buttonAdd.setOnClickListener(operationButtonListener);
        buttonMul.setOnClickListener(operationButtonListener);
    }

    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "Stopped the main activity");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}