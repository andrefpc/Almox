package br.gov.rj.rio.comlurb.almox.model;

import java.io.Serializable;

public class Usuario implements Serializable {

	private static final long serialVersionUID = -6538000297671277099L;

	private int id;

	private String matricula;

	private String senha;

	private String setor;

	private String token;

	public Usuario() {

	}

	public Usuario(int id, String matricula, String senha, String setor, String token) {
		this.id = id;
		this.matricula = matricula;
		this.senha = senha;
		this.setor = setor;
		this.token = token;
	}

	public Usuario(int id, String matricula, String senha, String setor) {
		super();
		this.id = id;
		this.matricula = matricula;
		this.senha = senha;
		this.setor = setor;
	}

	public Usuario(String matricula, String senha, String setor) {
		this.matricula = matricula;
		this.senha = senha;
		this.setor = setor;
	}

	public Usuario(String matricula, String senha) {
		this(matricula, senha, null);
	}

	public Usuario(String matricula) {
		this.matricula = matricula;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSetor() {
		return setor;
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
