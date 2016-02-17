package br.gov.rj.rio.comlurb.almox.service;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import br.gov.rj.rio.comlurb.almox.controler.SignatureActivity;
import br.gov.rj.rio.comlurb.almox.model.Usuario;
import br.gov.rj.rio.comlurb.almox.util.RestClient;

public class ServicoRestFul {

	public static boolean checkConnection(Context context){
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

		return isConnected;
	}

	public static void execute(Context context, RestClient client){
		if(checkConnection(context)) {
			client.execute();
		}else{
			Toast.makeText(context, "Sem Conexão com a internet!", Toast.LENGTH_LONG).show();
		}
	}

	public static void consultaValidacaoRestful(Usuario usuario, Context context, RestClient.OnPostExecuteListener postExecuteListener) {
		RestClient client = new RestClient(context, postExecuteListener);
		client.AddParam("service", "consultarValidacao");
		client.AddParam("matricula", "649184");
		//client.AddParam("senha", "");
		//client.AddParam("idSistema", "");

		execute(context, client);
	}

	public static void totalRegistrosValidacao(Usuario usuario, Context context, RestClient.OnPostExecuteListener postExecuteListener) {
		RestClient client = new RestClient(context, postExecuteListener);
		client.AddParam("service", "totalRegistrosValidacao");
		client.AddParam("matricula", "649184");
		//client.AddParam("senha", "");
		//client.AddParam("idSistema", "");

		execute(context, client);
	}

	public static void materialRequisicao(Usuario usuario, Context context, RestClient.OnPostExecuteListener postExecuteListener, String id) {
		RestClient client = new RestClient(context, postExecuteListener);
		client.AddParam("service", "requisicaoMaterial");
		client.AddParam("id", id);
		//client.AddParam("senha", "");
		//client.AddParam("idSistema", "");

		execute(context, client);
	}


	public static void editarQtdMaterial(Usuario usuario, Context context, RestClient.OnPostExecuteListener postExecuteListener, String id, String qtd) {
		RestClient client = new RestClient(context, postExecuteListener);
		client.AddParam("service", "editarItemValidacao");
		client.AddParam("id", id);
		client.AddParam("qtd", qtd);
		//client.AddParam("senha", "");
		//client.AddParam("idSistema", "");

		execute(context, client);
	}

	public static void consultaRecebimentoRestful(Usuario usuario, Context context, RestClient.OnPostExecuteListener postExecuteListener, String pagina, String qtdPorPag) {
		RestClient client = new RestClient(context, postExecuteListener);
		client.AddParam("service", "consultarRecebimento");
		client.AddParam("matricula", "649184");
		client.AddParam("pagina", pagina);
		client.AddParam("qtdPorPag", qtdPorPag);
		//client.AddParam("senha", "");
		//client.AddParam("idSistema", "");

		execute(context, client);
	}
}
