package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "VIGENCIASPROYECTOS")
public class VigenciasProyectos implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHAINICIAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechainicial;
    @Column(name = "FECHAFINAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechafinal;
    @Column(name = "CANTIDADPERSONAACARGO")
    private Short cantidadpersonaacargo;
    @JoinColumn(name = "PRY_ROL", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private PryRoles pryRol;
    @JoinColumn(name = "PROYECTO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Proyectos proyecto;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empleados empleado;
    @JoinColumn(name = "PRY_CARGOPROYECTO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Cargos pryCargoproyecto;

    public VigenciasProyectos() {
    }

    public VigenciasProyectos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public VigenciasProyectos(BigInteger secuencia, Date fechainicial) {
        this.secuencia = secuencia;
        this.fechainicial = fechainicial;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Date getFechainicial() {
        return fechainicial;
    }

    public void setFechainicial(Date fechainicial) {
        this.fechainicial = fechainicial;
    }

    public Date getFechafinal() {
        return fechafinal;
    }

    public void setFechafinal(Date fechafinal) {
        this.fechafinal = fechafinal;
    }

    public Short getCantidadpersonaacargo() {
        return cantidadpersonaacargo;
    }

    public void setCantidadpersonaacargo(Short cantidadpersonaacargo) {
        this.cantidadpersonaacargo = cantidadpersonaacargo;
    }

    public PryRoles getPryRol() {
        return pryRol;
    }

    public void setPryRol(PryRoles pryRol) {
        this.pryRol = pryRol;
    }

    public Proyectos getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyectos proyecto) {
        this.proyecto = proyecto;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public Cargos getPryCargoproyecto() {
        return pryCargoproyecto;
    }

    public void setPryCargoproyecto(Cargos pryCargoproyecto) {
        this.pryCargoproyecto = pryCargoproyecto;
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
        if (!(object instanceof VigenciasProyectos)) {
            return false;
        }
        VigenciasProyectos other = (VigenciasProyectos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.VigenciasProyectos[ secuencia=" + secuencia + " ]";
    }
    
}
