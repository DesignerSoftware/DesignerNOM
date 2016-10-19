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
@Table(name = "SOANTECEDENTES")
public class SoAntecedentes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CODIGO")
    private BigInteger codigo;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @JoinColumn(name = "SOTIPOSANTECEDENTES", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private SoTiposAntecedentes tipoantecedente;

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getCodigo() {
        return codigo;
    }

    public void setCodigo(BigInteger codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public SoTiposAntecedentes getTipoantecedente() {
        return tipoantecedente;
    }

    public void setTipoantecedente(SoTiposAntecedentes tipoantecedente) {
        this.tipoantecedente = tipoantecedente;
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
        if (!(object instanceof SoAntecedentes)) {
            return false;
        }
        SoAntecedentes other = (SoAntecedentes) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.SoAntecedentes[ secuencia=" + secuencia + " ]";
    }
    
}
