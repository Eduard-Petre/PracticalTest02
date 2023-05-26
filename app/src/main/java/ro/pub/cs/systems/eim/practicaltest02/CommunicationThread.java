package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CommunicationThread extends Thread{
    private final ServerThread serverThread;
    private final Socket socket;

    public CommunicationThread (ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run () {
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (operation / operands)!");

            String info = bufferedReader.readLine();
            String [] params = info.split(",");
            String op = params[0];
            int op1 = Integer.parseInt(params[1]);
            int op2 = Integer.parseInt(params[2]);

            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Operation = " + op + "; operand1 = " + op1 + "; operand2 = " + op2);
            int result = 0;
            int checkOperand1 = 0;
            if(op.equals("add")) {
                result = op1 + op2;
                checkOperand1 = result - op2;
            } else if (op.equals("mul")) {
                result = op1 * op2;
                checkOperand1 = result / op2;
                Thread.sleep(2000);
            } else {
                Log.i(Constants.TAG, "[COMMUNICATION THREAD] Invalid operation!!!");
            }
            if (checkOperand1 != op1) {

                // Send the result back to the client
                printWriter.println("Result is over allowed maximum");
                printWriter.flush();
            } else {

                // Send the result back to the client
                printWriter.println(result);
                printWriter.flush();
            }
        } catch (Exception e) {

        }
    }
}
