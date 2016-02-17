package br.gov.rj.rio.comlurb.almox.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by andre fpc on 04/12/2015.
 */
public class Requisicao implements Serializable {

    private int id;
    private int idUnidadeRequisitante;
    private String unidadeRequisitante;
    private int idAlmoxarifado;
    private String almoxarifado;
    private DateType dtRequisicao;
    private DateType dtAtendida;
    private String atendida;
    private String cod;
    private String localUR;
    private String siglaUA;
    private String obs;
    private DateType dtValidada;
    private String validada;

    public Requisicao(int id, int idUnidadeRequisitante, String unidadeRequisitante, int idAlmoxarifado, String almoxarifado, DateType dtRequisicao, DateType dtAtendida, String atendida, String cod, String localUR, String siglaUA, String obs, DateType dtValidada, String validada) {
        this.id = id;
        this.idUnidadeRequisitante = idUnidadeRequisitante;
        this.unidadeRequisitante = unidadeRequisitante;
        this.idAlmoxarifado = idAlmoxarifado;
        this.almoxarifado = almoxarifado;
        this.dtRequisicao = dtRequisicao;
        this.dtAtendida = dtAtendida;
        this.atendida = atendida;
        this.cod = cod;
        this.localUR = localUR;
        this.siglaUA = siglaUA;
        this.obs = obs;
        this.dtValidada = dtValidada;
        this.validada = validada;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUnidadeRequisitante() {
        return idUnidadeRequisitante;
    }

    public void setIdUnidadeRequisitante(int idUnidadeRequisitante) {
        this.idUnidadeRequisitante = idUnidadeRequisitante;
    }

    public String getUnidadeRequisitante() {
        return unidadeRequisitante;
    }

    public void setUnidadeRequisitante(String unidadeRequisitante) {
        this.unidadeRequisitante = unidadeRequisitante;
    }

    public int getIdAlmoxarifado() {
        return idAlmoxarifado;
    }

    public void setIdAlmoxarifado(int idAlmoxarifado) {
        this.idAlmoxarifado = idAlmoxarifado;
    }

    public String getAlmoxarifado() {
        return almoxarifado;
    }

    public void setAlmoxarifado(String almoxarifado) {
        this.almoxarifado = almoxarifado;
    }

    public DateType getDtRequisicao() {
        return dtRequisicao;
    }

    public void setDtRequisicao(DateType dtRequisicao) {
        this.dtRequisicao = dtRequisicao;
    }

    public DateType getDtAtendida() {
        return dtAtendida;
    }

    public void setDtAtendida(DateType dtAtendida) {
        this.dtAtendida = dtAtendida;
    }

    public String getAtendida() {
        return atendida;
    }

    public void setAtendida(String atendida) {
        this.atendida = atendida;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getLocalUR() {
        return localUR;
    }

    public void setLocalUR(String localUR) {
        this.localUR = localUR;
    }

    public String getSiglaUA() {
        return siglaUA;
    }

    public void setSiglaUA(String siglaUA) {
        this.siglaUA = siglaUA;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public DateType getDtValidada() {
        return dtValidada;
    }

    public void setDtValidada(DateType dtValidada) {
        this.dtValidada = dtValidada;
    }

    public String getValidada() {
        return validada;
    }

    public void setValidada(String validada) {
        this.validada = validada;
    }
}


