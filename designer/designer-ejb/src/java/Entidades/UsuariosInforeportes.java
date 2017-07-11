package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user
 */
@Entity
@Table(name = "USUARIOSINFOREPORTES")
public class UsuariosInforeportes implements Serializable {

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
    @JoinColumn(name = "INFOREPORTE", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    @NotNull
    private Inforeportes inforeporte;

    public UsuariosInforeportes() {
    }

    public UsuariosInforeportes(BigInteger secuencia) {
        this.secuencia = secuencia;
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

    public Inforeportes getInforeporte() {
        return inforeporte;
    }

    public void setInforeporte(Inforeportes inforeporte) {
        this.inforeporte = inforeporte;
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
        if (!(object instanceof UsuariosInforeportes)) {
            return false;
        }
        UsuariosInforeportes other = (UsuariosInforeportes) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Usuariosinforeportes[ secuencia=" + secuencia + " ]";
    }

}
