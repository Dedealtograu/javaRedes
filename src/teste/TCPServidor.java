package teste;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServidor {

	public static void main(String[] args) throws Exception {
		String clienteCentenca;
		String sentencaCpturada;
		
		ServerSocket benVindoSacket = new ServerSocket(6789);
		
		while (true) {
			Socket conexaoSocket = benVindoSacket.accept();
			
			BufferedReader cadeiaCliente = 
					new BufferedReader(new InputStreamReader(conexaoSocket.getInputStream())); 
			DataOutputStream servidorParaCliente =
					new DataOutputStream(conexaoSocket.getOutputStream());
			
			clienteCentenca = cadeiaCliente.readLine();
			
			sentencaCpturada = clienteCentenca.toUpperCase() + '\n';
			
			servidorParaCliente.writeBytes(sentencaCpturada);
		}
	}

}
