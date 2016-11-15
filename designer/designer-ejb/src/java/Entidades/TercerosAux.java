/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author user
 */
@Entity
public class TercerosAux implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "NOMBRETERCERO")
    private String nombretercero;
    @Column(name = "NITTERCERO")
    private Long nittercero;

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Long getNittercero() {
        return nittercero;
    }

    public void setNittercero(Long nittercero) {
        this.nittercero = nittercero;
    }

    public String getNombretercero() {
        return nombretercero;
    }

    public void setNombretercero(String nombretercero) {
        this.nombretercero = nombretercero;
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
        if (!(object instanceof TercerosAux)) {
            return false;
        }
        TercerosAux other = (TercerosAux) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.TercerosAux[ secuencia=" + secuencia + " ]";
    }

}
