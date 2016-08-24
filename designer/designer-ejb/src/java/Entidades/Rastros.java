package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "RASTROS")
public class Rastros implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Size(max = 100)
    @Column(name = "TABLA")
    private String tabla;
    @Size(max = 100)
    @Column(name = "SECUENCIATABLA")
    private String secuenciatabla;
    @Size(max = 100)
    @Column(name = "DIRECCION")
    private String direccion;
    @Size(max = 100)
    @Column(name = "ESTACION")
    private String estacion;
    @Size(max = 100)
    @Column(name = "USUARIOSO")
    private String usuarioso;
    @Size(max = 100)
    @Column(name = "USUARIOID")
    private String usuarioid;
    @Size(max = 100)
    @Column(name = "USUARIOBD")
    private String usuariobd;
    @Size(max = 100)
    @Column(name = "PROXIMOUSUARIO")
    private String proximousuario;
    @Column(name = "FECHARASTRO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecharastro;
    @Size(max = 1)
    @Column(name = "MANIPULACION")
    private String manipulacion;
    @Transient
    private String nombreManipulacion;

    public Rastros() {
    }

    public Rastros(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String getSecuenciatabla() {
        return secuenciatabla;
    }

    public void setSecuenciatabla(String secuenciatabla) {
        this.secuenciatabla = secuenciatabla;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEstacion() {
        return estacion;
    }

    public void setEstacion(String estacion) {
        this.estacion = estacion;
    }

    public String getUsuarioso() {
        return usuarioso.trim();
    }

    public void setUsuarioso(String usuarioso) {
        this.usuarioso = usuarioso;
    }

    public String getUsuarioid() {
        return usuarioid;
    }

    public void setUsuarioid(String usuarioid) {
        this.usuarioid = usuarioid;
    }

    public String getUsuariobd() {
        return usuariobd;
    }

    public void setUsuariobd(String usuariobd) {
        this.usuariobd = usuariobd;
    }

    public String getProximousuario() {
        return proximousuario;
    }

    public void setProximousuario(String proximousuario) {
        this.proximousuario = proximousuario;
    }

    public Date getFecharastro() {
        return fecharastro;
    }

    public void setFecharastro(Date fecharastro) {
        this.fecharastro = fecharastro;
    }

    public String getManipulacion() {
        return manipulacion;
    }

    public void setManipulacion(String manipulacion) {
        this.manipulacion = manipulacion;
    }

    public String getNombreManipulacion() {
        if (nombreManipulacion == null) {
            if (manipulacion.equals("I")) {
                nombreManipulacion = "Registro insertado";
            } else if (manipulacion.equals("U")) {
                nombreManipulacion = "Registro modificado";
            } else if (manipulacion.equals("D")) {
                nombreManipulacion = "Registro borrado";
            }
        }
        return nombreManipulacion;
    }

    public void setNombreManipulacion(String nombreManipulacion) {
        this.nombreManipulacion = nombreManipulacion;
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
        if (!(object instanceof Rastros)) {
            return false;
        }
        Rastros other = (Rastros) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Rastros[ secuencia=" + secuencia + " ]";
    }
}
