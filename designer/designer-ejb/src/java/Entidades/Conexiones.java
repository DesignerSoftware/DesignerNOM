package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "CONEXIONES")
public class Conexiones implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SID")
    private BigInteger sid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "DIRECCIONIP")
    private String direccionip;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "USUARIOSO")
    private String usuarioso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "ESTACION")
    private String estacion;
    @Column(name = "ULTIMAENTRADA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaentrada;
    @Size(max = 50)
    @Column(name = "USUARIOBD")
    private String usuariobd;

    public Conexiones() {
    }

    public Conexiones(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Conexiones(BigDecimal BigInteger, BigInteger sid, String direccionip, String usuarioso, String estacion) {
        this.secuencia = secuencia;
        this.sid = sid;
        this.direccionip = direccionip;
        this.usuarioso = usuarioso;
        this.estacion = estacion;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getSid() {
        return sid;
    }

    public void setSid(BigInteger sid) {
        this.sid = sid;
    }

    public String getDireccionip() {
        return direccionip;
    }

    public void setDireccionip(String direccionip) {
        this.direccionip = direccionip;
    }

    public String getUsuarioso() {
        return usuarioso;
    }

    public void setUsuarioso(String usuarioso) {
        this.usuarioso = usuarioso;
    }

    public String getEstacion() {
        return estacion;
    }

    public void setEstacion(String estacion) {
        this.estacion = estacion;
    }

    public Date getUltimaentrada() {
        return ultimaentrada;
    }

    public void setUltimaentrada(Date ultimaentrada) {
        this.ultimaentrada = ultimaentrada;
    }

    public String getUsuariobd() {
        return usuariobd;
    }

    public void setUsuariobd(String usuariobd) {
        this.usuariobd = usuariobd;
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
        if (!(object instanceof Conexiones)) {
            return false;
        }
        Conexiones other = (Conexiones) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Conexiones[ secuencia=" + secuencia + " ]";
    }
    
}
