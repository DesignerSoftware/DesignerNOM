package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "VWMENSAJESAPBOV8")
public class VWMensajeSAPBOV8 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CONSECUTIVO")
    private BigInteger consecutivo;
    @Column(name = "MENSAJE")
    private String mensaje;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATETIME")
    private Date datetime;
    @Column(name = "CODCOMP")
    private short codcomp;

    public BigInteger getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(BigInteger consecutivo) {
        this.consecutivo = consecutivo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public short getCodcomp() {
        return codcomp;
    }

    public void setCodcomp(short codcomp) {
        this.codcomp = codcomp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (consecutivo != null ? consecutivo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VWMensajeSAPBOV8)) {
            return false;
        }
        VWMensajeSAPBOV8 other = (VWMensajeSAPBOV8) object;
        if ((this.consecutivo == null && other.consecutivo != null) || (this.consecutivo != null && !this.consecutivo.equals(other.consecutivo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.VWMensajesAPBOV8[ consecutivo=" + consecutivo + " ]";
    }

}
