package br.gov.rj.rio.comlurb.almox.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import br.gov.rj.rio.comlurb.almox.util.RestClient.RequestMethod;

public class Versao {
	public static final int DESENV = 1;
	public static final int PROD = 3;
	public static final int VERSAO = DESENV;
	public static final boolean modoTeste = false;
	public static final String serverURL = "http://comlurbdev.rio.rj.gov.br/projetos/almoxWS/controle/RequisicaoControle.php?";
	public static final RequestMethod method = RequestMethod.POST;
	
	public static String getHost() {
		switch (VERSAO) {
			case PROD:
				return "http://comlurbweb.rio.rj.gov.br/extranet/lixoZero/";
			case DESENV:
				return "http://comlurbdev.rio.rj.gov.br/projetos/lixoZero/";
			default:
				return "http://comlurbdev.rio.rj.gov.br/";
		}
	}
	
	public static String getUrlServices() {
		switch (VERSAO) {
			case PROD:
				return getHost() + "WS/WSLxz_1.asmx?WSDL";
			case DESENV:
				return getHost() + "WS/wslxz_1.asmx?WSDL";
			default:
				return getHost() + "projetos/lixoZero/WS/WSLixoZero.asmx?WSDL";
		}
	}
	
	public static String getVersionName(Context c) {
		try {
			PackageInfo packageInfo = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
			String versao = packageInfo.versionName;
			switch (VERSAO) {
				case DESENV:
					return versao + "D";
				case PROD:
					return versao;
				default:
					return versao;
			}
		} catch (NameNotFoundException e) {
			return "";
		}
	}
	
	public static String getVersionCode(Context c) {
		try {
			return c.getPackageManager().getPackageInfo(c.getPackageName(), 0).versionCode + "";
		} catch (NameNotFoundException e) {
			return null;
		}
	}
}
