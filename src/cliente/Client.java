package cliente;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Client extends JFrame {
	private JTextField enterField;
	private JTextArea displayArea;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String chatServer;
	private Socket client;
	
	public Client(String host) {
		
		super("Cliente");
		
		chatServer = host;
		
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
	}
	
	public void runClient(){
		try {
			connectToServer();
			getStreams();
			processConnection();
			
		}
		catch (EOFException eofException) {
			displayMessage("\nConexão do Clinte Terminada");
		}catch (IOException ioException) {
			ioException.printStackTrace();
		}finally {
			closeConnection();
		}
	}
	
	private void connectToServer() throws IOException {
		displayMessage("Tentando conexão\n");
		
		client = new Socket(InetAddress.getByName(chatServer), 6789);
		
		displayMessage("Conectado em: " + client.getInetAddress().getHostName());
	}

	private void getStreams() throws IOException {
		output = new ObjectOutputStream(client.getOutputStream());
		output.flush();
		
		input = new ObjectInputStream(client.getInputStream());
		
		displayMessage("\nTendo Fluxo I/O");
	}
	
	private void processConnection() throws IOException{
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
		displayMessage("Fechando conexão\n");
		setTextFieldEditable(false);
		
		try {
			output.close();
			input.close();
			client.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	private void sendData(String message) {
		try {
			output.writeObject("CLIENTE>>> "+ message);
			output.flush();
			displayMessage("\nCLIENTE>>> "+ message);
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
