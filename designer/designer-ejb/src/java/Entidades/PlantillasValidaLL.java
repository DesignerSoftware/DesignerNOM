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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user
 */
@Entity
@Table(name = "PLANTILLASVALIDALL")
public class PlantillasValidaLL implements Serializable {

   private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @JoinColumn(name = "TIPOTRABAJADOR", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    @NotNull
    private TiposTrabajadores tipotrabajador;
    @JoinColumn(name = "CONTRATO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Contratos contrato;

    public PlantillasValidaLL(BigInteger secuencia, TiposTrabajadores tipotrabajador, Contratos contrato) {
        this.secuencia = secuencia;
        this.tipotrabajador = tipotrabajador;
        this.contrato = contrato;
    }

    public PlantillasValidaLL() {
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public TiposTrabajadores getTipotrabajador() {
        return tipotrabajador;
    }

    public void setTipotrabajador(TiposTrabajadores tipotrabajador) {
        this.tipotrabajador = tipotrabajador;
    }

    public Contratos getContrato() {
        return contrato;
    }

    public void setContrato(Contratos contrato) {
        this.contrato = contrato;
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
        if (!(object instanceof PlantillasValidaLL)) {
            return false;
        }
        PlantillasValidaLL other = (PlantillasValidaLL) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.PlantillasValidaLL[ secuencia=" + secuencia + " ]";
    }
    
}
