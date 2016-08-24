package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "USUARIOSINTERFASES")
public class UsuariosInterfases implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "USUARIOLOCAL")
    private String usuariolocal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "PASSWORDLOCAL")
    private String passwordlocal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "SERVERNAMELOCAL")
    private String servernamelocal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "PORTNUMBERLOCAL")
    private String portnumberlocal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "SIDLOCAL")
    private String sidlocal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "USUARIOREMOTO")
    private String usuarioremoto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "PASSWORDREMOTO")
    private String passwordremoto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "SERVERNAMEREMOTO")
    private String servernameremoto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "PORTNUMBERREMOTO")
    private String portnumberremoto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "SIDREMOTO")
    private String sidremoto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "INTERFASE")
    private String interfase;

    public UsuariosInterfases() {
    }

    public UsuariosInterfases(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public UsuariosInterfases(BigInteger secuencia, String usuariolocal, String passwordlocal, String servernamelocal, String portnumberlocal, String sidlocal, String usuarioremoto, String passwordremoto, String servernameremoto, String portnumberremoto, String sidremoto, String interfase) {
        this.secuencia = secuencia;
        this.usuariolocal = usuariolocal;
        this.passwordlocal = passwordlocal;
        this.servernamelocal = servernamelocal;
        this.portnumberlocal = portnumberlocal;
        this.sidlocal = sidlocal;
        this.usuarioremoto = usuarioremoto;
        this.passwordremoto = passwordremoto;
        this.servernameremoto = servernameremoto;
        this.portnumberremoto = portnumberremoto;
        this.sidremoto = sidremoto;
        this.interfase = interfase;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getUsuariolocal() {
        return usuariolocal;
    }

    public void setUsuariolocal(String usuariolocal) {
        this.usuariolocal = usuariolocal;
    }

    public String getPasswordlocal() {
        return passwordlocal;
    }

    public void setPasswordlocal(String passwordlocal) {
        this.passwordlocal = passwordlocal;
    }

    public String getServernamelocal() {
        return servernamelocal;
    }

    public void setServernamelocal(String servernamelocal) {
        this.servernamelocal = servernamelocal;
    }

    public String getPortnumberlocal() {
        return portnumberlocal;
    }

    public void setPortnumberlocal(String portnumberlocal) {
        this.portnumberlocal = portnumberlocal;
    }

    public String getSidlocal() {
        return sidlocal;
    }

    public void setSidlocal(String sidlocal) {
        this.sidlocal = sidlocal;
    }

    public String getUsuarioremoto() {
        return usuarioremoto;
    }

    public void setUsuarioremoto(String usuarioremoto) {
        this.usuarioremoto = usuarioremoto;
    }

    public String getPasswordremoto() {
        return passwordremoto;
    }

    public void setPasswordremoto(String passwordremoto) {
        this.passwordremoto = passwordremoto;
    }

    public String getServernameremoto() {
        return servernameremoto;
    }

    public void setServernameremoto(String servernameremoto) {
        this.servernameremoto = servernameremoto;
    }

    public String getPortnumberremoto() {
        return portnumberremoto;
    }

    public void setPortnumberremoto(String portnumberremoto) {
        this.portnumberremoto = portnumberremoto;
    }

    public String getSidremoto() {
        return sidremoto;
    }

    public void setSidremoto(String sidremoto) {
        this.sidremoto = sidremoto;
    }

    public String getInterfase() {
        return interfase;
    }

    public void setInterfase(String interfase) {
        this.interfase = interfase;
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
        if (!(object instanceof UsuariosInterfases)) {
            return false;
        }
        UsuariosInterfases other = (UsuariosInterfases) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.UsuariosInterfases[ secuencia=" + secuencia + " ]";
    }
    
}
