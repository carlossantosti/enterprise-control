package br.com.carlosti.data.models;

import java.io.Serializable;

public class ProductGroup implements Serializable {
    private Integer empresa;
    private Integer grupoProduto;
    private String descricaoGrupoProduto;
    private Double percDesconto;
    private String tipoComplemento;

    public Integer getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Integer empresa) {
        this.empresa = empresa;
    }

    public Integer getGrupoProduto() {
        return grupoProduto;
    }

    public void setGrupoProduto(Integer grupoProduto) {
        this.grupoProduto = grupoProduto;
    }

    public String getDescricaoGrupoProduto() {
        return descricaoGrupoProduto;
    }

    public void setDescricaoGrupoProduto(String descricaoGrupoProduto) {
        this.descricaoGrupoProduto = descricaoGrupoProduto;
    }

    public Double getPercDesconto() {
        return percDesconto;
    }

    public void setPercDesconto(Double percDesconto) {
        this.percDesconto = percDesconto;
    }

    public String getTipoComplemento() {
        return tipoComplemento;
    }

    public void setTipoComplemento(String tipoComplemento) {
        this.tipoComplemento = tipoComplemento;
    }
}
