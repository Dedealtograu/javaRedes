package servidor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Server extends JFrame{
	private JTextField enterField;
	private JTextArea displayArea;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	private int counter = -1;
	
	public Server() {
		super("Servidor");
		
		enterField = new JTextField();
		enterField.setEditable(false);
		enterField.addActionListener(
				new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent event) {
						sendData(event.getActionCommand());
						enterField.setText("");
					}
				});
		add(enterField, BorderLayout.NORTH);
		
		displayArea = new JTextArea();
		add(new JScrollPane(displayArea), BorderLayout.CENTER);
		setSize(300, 150);
		setVisible(true);
	} // fim do contrutor
	
	public void runServer(){
		try {
			server = new ServerSocket(12356, 100);
			
			while (true) {
				try {
					waitForConnection();
					getStreams();
					processConnection();
				} catch (EOFException eofException) {
					displayMessage("Conexão com Servidor encerrada");
					
				}
				finally {
					closeConnection();
					++counter;
				}
			}
		} catch (IOException ioException ) {
			ioException.printStackTrace();
		}
	}
	
	private void waitForConnection() throws IOException{
		displayMessage("Esperando por conexão\n");
		connection = server.accept();
		displayMessage("Conexão "+ counter + " recebida de: "+ 
				connection.getInetAddress().getHostName());
	}
	
	private void getStreams() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		
		input = new ObjectInputStream(connection.getInputStream());
		
		displayMessage("\nTendo Fluxo I/O");
	}
	
	private void processConnection() throws IOException{
		String message = "Sucesso na conexão";
		sendData(message);
		
		setTextFieldEditable(true);
		
		do {
			try {
				message = (String) input.readObject();
				displayMessage("\n" + message);
			} catch (ClassNotFoundException classNotFoundException) {
				displayMessage("\nObjeto recebido de tipo desconhecido");
				
			}
		} while (!message.equals("CLIENTE>>> TERMINADO"));
	}
	
	private void closeConnection() {
		displayMessage("Terminando conexão\n");
		setTextFieldEditable(false);
		
		try {
			output.close();
			input.close();
			connection.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	private void sendData(String message) {
		try {
			output.writeObject("SERVIDOR>>> "+ message);
			output.flush();
			displayMessage("\nSERVIDOR>>> "+ message);
		} catch (IOException ioException) {
			displayArea.append("\nErro ao escrever objeto");
		}
	}
	
	private void displayMessage(final String messageToDisplay) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				
				displayArea.append(messageToDisplay);
			}
		});
	}
	
	private void setTextFieldEditable(final boolean editable) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				
				enterField.setEditable(editable);
			}
		});
	}
}









