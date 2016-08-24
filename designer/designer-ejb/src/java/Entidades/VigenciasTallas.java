package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "VIGENCIASTALLAS")
public class VigenciasTallas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHAVIGENCIA")
    @Temporal(TemporalType.DATE)
    private Date fechavigencia;
    @Size(max = 20)
    @Column(name = "TALLA")
    private String talla;
    @Size(max = 40)
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empleados empleado;
    @JoinColumn(name = "TIPOTALLA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private TiposTallas tipoTalla;

    public VigenciasTallas() {
    }

    public VigenciasTallas(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public VigenciasTallas(BigInteger secuencia, Date fechavigencia) {
        this.secuencia = secuencia;
        this.fechavigencia = fechavigencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Date getFechavigencia() {
        return fechavigencia;
    }

    public void setFechavigencia(Date fechavigencia) {
        this.fechavigencia = fechavigencia;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        if (talla != null) {
            this.talla = talla.toUpperCase();
        } else {
            this.talla = talla;
        }
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        if (observaciones != null) {
            this.observaciones = observaciones.toUpperCase();
        } else {
            this.observaciones = observaciones;
        }
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
        if (!(object instanceof VigenciasTallas)) {
            return false;
        }
        VigenciasTallas other = (VigenciasTallas) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.VigenciasTallas[ secuencia=" + secuencia + " ]";
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public TiposTallas getTipoTalla() {
        return tipoTalla;
    }

    public void setTipoTalla(TiposTallas tipoTalla) {
        this.tipoTalla = tipoTalla;
    }

}
