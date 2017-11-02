/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import static Entidades.seven.EstructurasFormulas_.id;
import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "USUARIOSINDICES")
public class UsuariosIndices implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @JoinColumn(name = "USUARIO", referencedColumnName = "SECUENCIA")
    @NotNull
    @ManyToOne(optional = false)
    private Usuarios usuario;
    @JoinColumn(name = "INDICE", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    @NotNull
    private Indices indice;

    public UsuariosIndices() {
    }

    public UsuariosIndices(BigInteger secuencia, Usuarios usuario, Indices indice) {
        this.secuencia = secuencia;
        this.usuario = usuario;
        this.indice = indice;
    }

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

    public Indices getIndice() {
        return indice;
    }

    public void setIndice(Indices indice) {
        this.indice = indice;
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
        if (!(object instanceof UsuariosIndices)) {
            return false;
        }
        UsuariosIndices other = (UsuariosIndices) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.UsuariosIndices[ secuencia=" + secuencia + " ]";
    }

}
