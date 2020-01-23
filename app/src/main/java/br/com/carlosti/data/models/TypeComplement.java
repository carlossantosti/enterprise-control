package br.com.carlosti.data.models;

import java.io.Serializable;

public class TypeComplement implements Serializable {
    private Integer empresa;
    private String tipoComplemento;
    private String descricaoTipoComplemento;

    public Integer getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Integer empresa) {
        this.empresa = empresa;
    }

    public String getTipoComplemento() {
        return tipoComplemento;
    }

    public void setTipoComplemento(String tipoComplemento) {
        this.tipoComplemento = tipoComplemento;
    }

    public String getDescricaoTipoComplemento() {
        return descricaoTipoComplemento;
    }

    public void setDescricaoTipoComplemento(String descricaoTipoComplemento) {
        this.descricaoTipoComplemento = descricaoTipoComplemento;
    }
}
