package br.com.carlosti.data.models;

import java.io.Serializable;

public class Product implements Serializable {
    private Integer empresa;
    private String produto;
    private String descricaoProduto;
    private String apelidoProduto;
    private Integer grupoProduto;
    private Integer subGrupoProduto;
    private String situacao;
    private Double pesoLiquido;
    private String classificacaoFiscal;
    private String codigoBarras;
    private String colecao;

    public Integer getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Integer empresa) {
        this.empresa = empresa;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public String getDescricaoProduto() {
        return descricaoProduto;
    }

    public void setDescricaoProduto(String descricaoProduto) {
        this.descricaoProduto = descricaoProduto;
    }

    public String getApelidoProduto() {
        return apelidoProduto;
    }

    public void setApelidoProduto(String apelidoProduto) {
        this.apelidoProduto = apelidoProduto;
    }

    public Integer getGrupoProduto() {
        return grupoProduto;
    }

    public void setGrupoProduto(Integer grupoProduto) {
        this.grupoProduto = grupoProduto;
    }

    public Integer getSubGrupoProduto() {
        return subGrupoProduto;
    }

    public void setSubGrupoProduto(Integer subGrupoProduto) {
        this.subGrupoProduto = subGrupoProduto;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Double getPesoLiquido() {
        return pesoLiquido;
    }

    public void setPesoLiquido(Double pesoLiquido) {
        this.pesoLiquido = pesoLiquido;
    }

    public String getClassificacaoFiscal() {
        return classificacaoFiscal;
    }

    public void setClassificacaoFiscal(String classificacaoFiscal) {
        this.classificacaoFiscal = classificacaoFiscal;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getColecao() {
        return colecao;
    }

    public void setColecao(String colecao) {
        this.colecao = colecao;
    }
}
