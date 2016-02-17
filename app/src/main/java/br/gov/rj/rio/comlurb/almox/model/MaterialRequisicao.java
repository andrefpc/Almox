package br.gov.rj.rio.comlurb.almox.model;

import java.io.Serializable;

/**
 * Created by andre fpc on 04/12/2015.
 */
public class MaterialRequisicao implements Serializable {

    private int id;
    private int idRequisicao;
    private int idMaterial;
    private String material;
    private String qtd;
    private String itemAtendido;
    private String qtdAtendido;

    public MaterialRequisicao(int id, int idRequisicao, int idMaterial, String material, String qtd, String itemAtendido, String qtdAtendido) {
        this.id = id;
        this.idRequisicao = idRequisicao;
        this.idMaterial = idMaterial;
        this.material = material;
        this.qtd = qtd;
        this.itemAtendido = itemAtendido;
        this.qtdAtendido = qtdAtendido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdRequisicao() {
        return idRequisicao;
    }

    public void setIdRequisicao(int idRequisicao) {
        this.idRequisicao = idRequisicao;
    }

    public int getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(int idMaterial) {
        this.idMaterial = idMaterial;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getQtd() {
        return qtd;
    }

    public void setQtd(String qtd) {
        this.qtd = qtd;
    }

    public String getItemAtendido() {
        return itemAtendido;
    }

    public void setItemAtendido(String itemAtendido) {
        this.itemAtendido = itemAtendido;
    }

    public String getQtdAtendido() {
        return qtdAtendido;
    }

    public void setQtdAtendido(String qtdAtendido) {
        this.qtdAtendido = qtdAtendido;
    }
}
