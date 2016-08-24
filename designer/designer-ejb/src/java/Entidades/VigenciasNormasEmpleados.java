package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user
 */
@Entity
@Table(name = "VIGENCIASNORMASEMPLEADOS")
public class VigenciasNormasEmpleados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHAVIGENCIA")
    private Date fechavigencia;
    @JoinColumn(name = "NORMALABORAL", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private NormasLaborales normalaboral;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empleados empleado;

    public VigenciasNormasEmpleados() {
    }

    public VigenciasNormasEmpleados(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public VigenciasNormasEmpleados(BigInteger secuencia, Date fechavigencia) {
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

    public NormasLaborales getNormalaboral() {
        return normalaboral;
    }

    public void setNormalaboral(NormasLaborales normalaboral) {
        this.normalaboral = normalaboral;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
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
        if (!(object instanceof VigenciasNormasEmpleados)) {
            return false;
        }
        VigenciasNormasEmpleados other = (VigenciasNormasEmpleados) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Vigenciasnormasempleados[ secuencia=" + secuencia + " ]";
    }
}
