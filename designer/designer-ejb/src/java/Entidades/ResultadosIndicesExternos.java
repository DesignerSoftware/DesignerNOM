/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
public class ResultadosIndicesExternos implements Serializable {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @JoinColumn(name = "INDICESEXTERNOS", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    @NotNull
    private IndicesExternos indiceexterno;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MES")
    private Short mes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ANO")
    private Short ano;
    @Basic(optional = false)
    @NotNull
    @Column (name = "VALOR")
    private BigInteger valor;
    @Size(min = 1, max = 100)
    @Column(name = "REFERENCIA")
    private String referencia;
    @Transient
    private String estadoMes;

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public IndicesExternos getIndiceexterno() {
        return indiceexterno;
    }

    public void setIndiceexterno(IndicesExternos indiceexterno) {
        this.indiceexterno = indiceexterno;
    }

    public Short getMes() {
        return mes;
    }

    public void setMes(Short mes) {
        this.mes = mes;
    }

    public Short getAno() {
        return ano;
    }

    public void setAno(Short ano) {
        this.ano = ano;
    }

    public BigInteger getValor() {
        return valor;
    }

    public void setValor(BigInteger valor) {
        this.valor = valor;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getEstadoMes() {
        int value = mes.intValue();
        if(value == 1){
         estadoMes = "ENERO";
        } else if(value == 2){
         estadoMes = "FEBRERO";  
        }else if(value == 3){
         estadoMes = "MARZO";  
        }else if(value == 4){
         estadoMes = "ABRIL";  
        }else if(value == 5){
         estadoMes = "MAYO";  
        }else if(value == 6){
         estadoMes = "JUNIO";  
        }else if(value == 7){
         estadoMes = "JULIO";  
        }else if(value == 8){
         estadoMes = "AGOSTO";  
        }else if(value == 9){
         estadoMes = "SEPTIEMBRE";  
        }else if(value == 10){
         estadoMes = "OCTUBRE";  
        }else if(value == 11){
         estadoMes = "NOVIEMBRE";  
        }else if(value == 12){
         estadoMes = "DICIEMBRE";  
        }
        return estadoMes;
    }

    public void setEstadoMes(String estadomes) {
        this.estadoMes = estadomes;
        if(estadoMes.equals("ENERO")){
            mes = 1;
        } else if(estadoMes.equals("FEBRERO")){
            mes = 2;
        } else if(estadoMes.equals("MARZO")){
            mes = 3;
        } else if(estadoMes.equals("ABRIL")){
            mes = 4;
        } else if(estadoMes.equals("MAYO")){
            mes = 5;
        } else if(estadoMes.equals("JUNIO")){
            mes = 6;
        } else if(estadoMes.equals("JULIO")){
            mes = 7;
        } else if(estadoMes.equals("AGOSTO")){
            mes = 8;
        } else if(estadoMes.equals("SEPTIEMBRE")){
            mes = 9;
        } else if(estadoMes.equals("OCTUBRE")){
            mes = 10;
        } else if(estadoMes.equals("NOVIEMBRE")){
            mes = 11;
        } else if(estadoMes.equals("DICIEMBRE")){
            mes = 12;
        } 
        
    }

    
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (secuencia != null ? secuencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ResultadosIndicesExternos)) {
            return false;
        }
        ResultadosIndicesExternos other = (ResultadosIndicesExternos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.ResultadosIndicesExternos[ id=" + secuencia + " ]";
    }

}
