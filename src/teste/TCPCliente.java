package teste;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class TCPCliente {

	public static void main(String[] args) throws Exception {
		String sentenca;
		String sentencaModificada;
		
		BufferedReader cadeiaUsuario = new BufferedReader(new InputStreamReader(System.in));
		
		Socket clienteSocket = new Socket("localhost", 6789);
		
		DataOutputStream clinteParaServidor = 
				new DataOutputStream(clienteSocket.getOutputStream());
		
		BufferedReader cadeiaServidor = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
		
		sentenca  = cadeiaUsuario.readLine();
		
		clinteParaServidor.writeBytes(sentenca + '\n');
		
		sentencaModificada = cadeiaServidor.readLine();
		
		System.out.println("Para o servidor " + sentencaModificada);
		
		clienteSocket.close();
	}

}
