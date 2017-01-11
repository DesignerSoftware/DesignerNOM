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
public class DetallesPeriodicidades implements Serializable {

    private static final long serialVersionUID = 1L;
   @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @JoinColumn(name = "PERIODICIDAD", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Periodicidades periodicidad;
    @Size(max = 1)
    @Column(name = "TIPODIA")
    private String tipodia;
    @Column(name="ANO")
    private short ano;
    @Column(name="MES")
    private short mes;
    @Column(name="DIA")
    private short dia;
    @Transient
    private String estadoTipoDia;

    public DetallesPeriodicidades() {
    }

    public DetallesPeriodicidades(BigInteger secuencia, Periodicidades periodicidad, String tipodia, short ano, short mes, short dia) {
        this.secuencia = secuencia;
        this.periodicidad = periodicidad;
        this.tipodia = tipodia;
        this.ano = ano;
        this.mes = mes;
        this.dia = dia;
    }

    public DetallesPeriodicidades(BigInteger secuencia) {
        this.secuencia = secuencia;
    }
    
    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Periodicidades getPeriodicidad() {
        return periodicidad;
    }

    public void setPeriodicidad(Periodicidades periodicidad) {
        this.periodicidad = periodicidad;
    }

    public String getTipodia() {
        return tipodia;
    }

    public void setTipodia(String tipodia) {
        this.tipodia = tipodia;
    }

    public short getAno() {
        return ano;
    }

    public void setAno(short ano) {
        this.ano = ano;
    }

    public short getMes() {
        return mes;
    }

    public void setMes(short mes) {
        this.mes = mes;
    }

    public short getDia() {
        return dia;
    }

    public void setDia(short dia) {
        this.dia = dia;
    }

    public String getEstadoTipoDia() {
        getTipodia();
        if(estadoTipoDia == null){
          if (tipodia.equalsIgnoreCase("S")) {
                estadoTipoDia = "SEMANAL";
            } else if (tipodia.equalsIgnoreCase("M")) {
                estadoTipoDia = "MENSUAL";
            } else if (tipodia.equalsIgnoreCase("A")) {
                estadoTipoDia = "ANUAL";
            }  
        }
        return estadoTipoDia;
    }

    public void setEstadoTipoDia(String estadoTipoDia) {
        this.estadoTipoDia = estadoTipoDia;
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
        if (!(object instanceof DetallesPeriodicidades)) {
            return false;
        }
        DetallesPeriodicidades other = (DetallesPeriodicidades) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.DetallesPeriodicidades[ secuencia=" + secuencia + " ]";
    }
    
}
