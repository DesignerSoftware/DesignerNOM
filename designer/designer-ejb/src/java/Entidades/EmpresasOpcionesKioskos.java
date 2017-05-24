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
@Table(name = "EMPRESASOPCIONESKIOSKOS")
public class EmpresasOpcionesKioskos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    @NotNull
    private Empresas empresa;
    @JoinColumn(name = "OPCIONKIOSKO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    @NotNull
    private OpcionesKioskos opcionkiosko;

    public EmpresasOpcionesKioskos() {
    }

    public EmpresasOpcionesKioskos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Empresas getEmpresa() {
        if(empresa == null){
            empresa = new Empresas();
        }
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public OpcionesKioskos getOpcionkiosko() {
        if(opcionkiosko == null){
            opcionkiosko = new OpcionesKioskos();
        }
        return opcionkiosko;
    }

    public void setOpcionkiosko(OpcionesKioskos opcionkiosko) {
        this.opcionkiosko = opcionkiosko;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (secuencia != null ? secuencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the secuencia fields are not set
        if (!(object instanceof EmpresasOpcionesKioskos)) {
            return false;
        }
        EmpresasOpcionesKioskos other = (EmpresasOpcionesKioskos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.EmpresasOpcionesKioskos[ secuencia=" + secuencia + " ]";
    }

}
