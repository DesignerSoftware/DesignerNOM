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
import javax.validation.constraints.NotNull;

/**
 *
 * @author user
 */
@Entity
public class UsuariosProcesos implements Serializable {

     private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @JoinColumn(name = "USUARIO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    @NotNull
    private Usuarios usuario;
    @JoinColumn(name = "PROCESO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    @NotNull
    private Procesos proceso;

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public Procesos getProceso() {
        return proceso;
    }

    public void setProceso(Procesos proceso) {
        this.proceso = proceso;
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
        if (!(object instanceof UsuariosProcesos)) {
            return false;
        }
        UsuariosProcesos other = (UsuariosProcesos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.UsuariosProcesos[ secuencia=" + secuencia + " ]";
    }
    
}
