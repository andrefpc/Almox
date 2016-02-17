package br.gov.rj.rio.comlurb.almox.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class RestClient extends AsyncTask<String, String, String> {

	private ArrayList<NameValuePair> params;
	private ArrayList<NameValuePair> headers;
	private static final RequestMethod method = Versao.method;
	private String url = Versao.serverURL;

	private ProgressDialog dialogoProgresso;
	private String resposta;
	private OnPostExecuteListener mPostExecuteListener = null;

	private int responseCode;
	private String message;

	private String response;

	public String getResponse() {
		return response;
	}

	public String getErrorMessage() {
		return message;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public RestClient() {
		params = new ArrayList<NameValuePair>();
		headers = new ArrayList<NameValuePair>();
	}

	public void AddParam(String name, String value) {
		params.add(new BasicNameValuePair(name, value));
	}

	public void AddHeader(String name, String value) {
		headers.add(new BasicNameValuePair(name, value));
	}

	public enum RequestMethod {
		GET, POST
	}

	public static interface OnPostExecuteListener {
		void onPostExecute(String result);
	}

	public void Execute() throws Exception {
		switch (method) {
			case GET: {
				// add parameters
				String combinedParams = "";
				if (!params.isEmpty()) {
					combinedParams += "?";
					for (NameValuePair p : params) {
						String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(), "UTF-8");
						if (combinedParams.length() > 1) {
							combinedParams += "&" + paramString;
						} else {
							combinedParams += paramString;
						}
					}
				}

				HttpGet request = new HttpGet(url + combinedParams);

				// add headers
				for (NameValuePair h : headers) {
					request.addHeader(h.getName(), h.getValue());
				}

				executeRequest(request, url);
				break;
			}
			case POST: {
				HttpPost request = new HttpPost(url);

				// add headers
				for (NameValuePair h : headers) {
					request.addHeader(h.getName(), h.getValue());
				}

				if (!params.isEmpty()) {
					request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				}

				executeRequest(request, url);
				break;
			}
		}
	}

	private void executeRequest(HttpUriRequest request, String url) {
		HttpClient client = new DefaultHttpClient();

		HttpResponse httpResponse;

		try {
			httpResponse = client.execute(request);
			responseCode = httpResponse.getStatusLine().getStatusCode();
			message = httpResponse.getStatusLine().getReasonPhrase();

			HttpEntity entity = httpResponse.getEntity();

			if (entity != null) {

				InputStream instream = entity.getContent();
				response = convertStreamToString(instream);
				// Closing the input stream will trigger connection release
				instream.close();
			}

		} catch (ClientProtocolException e) {
			client.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (IOException e) {
			client.getConnectionManager().shutdown();
			e.printStackTrace();
		}
	}

	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public RestClient(Context context, OnPostExecuteListener postExecuteListener, ProgressDialog pd) throws IllegalArgumentException {
		params = new ArrayList<NameValuePair>();
		headers = new ArrayList<NameValuePair>();
		this.dialogoProgresso = pd;
		mPostExecuteListener = postExecuteListener;
		if (mPostExecuteListener == null) {
			throw new IllegalArgumentException("Param cannot be null.");
		}
	}

	public RestClient(Context context, OnPostExecuteListener postExecuteListener) throws IllegalArgumentException {
		this.dialogoProgresso = new ProgressDialog(context);
		params = new ArrayList<NameValuePair>();
		headers = new ArrayList<NameValuePair>();
		mPostExecuteListener = postExecuteListener;

		// O di�logo de progresso ficava aparecendo v�rias vezes durante a
		// autentica��o, por isso comentei ele.
		// Depois � preciso verificar onde ele realmente deve ficar para que n�o
		// d� esse bug.

		dialogoProgresso.setTitle("Aguarde");
		dialogoProgresso.setMessage("Conectando ao servidor...");
		dialogoProgresso.setProgressStyle(ProgressDialog.STYLE_SPINNER);

		//if (mPostExecuteListener == null) {
		//	throw new IllegalArgumentException("Param cannot be null.");
		//}
	}

	public RestClient(OnPostExecuteListener postExecuteListener) throws Exception {
		mPostExecuteListener = postExecuteListener;
		params = new ArrayList<NameValuePair>();
		headers = new ArrayList<NameValuePair>();
		if (mPostExecuteListener == null) {
			throw new Exception("Param cannot be null.");
		}
	}

	@Override
	protected void onPreExecute() {
		if (dialogoProgresso != null) {
			dialogoProgresso.show();
			dialogoProgresso.setCancelable(false);
		}
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... arg0) {
		try {
			Execute();
		} catch (Exception e) {
			e.printStackTrace();
			if (dialogoProgresso != null) {
				dialogoProgresso.dismiss();
			}
			resposta = "Erro ao conectar ao servidor.";
			return resposta;
		}
		if (dialogoProgresso != null) {
			dialogoProgresso.dismiss();
		}
		return response;
	}

	@Override
	protected void onPostExecute(String result) {
		if (mPostExecuteListener != null) {
			try {
				mPostExecuteListener.onPostExecute(result);
				if (dialogoProgresso != null) {
					dialogoProgresso.dismiss();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if ((dialogoProgresso != null) && dialogoProgresso.isShowing()) {
			dialogoProgresso.dismiss();
		}
	}
}