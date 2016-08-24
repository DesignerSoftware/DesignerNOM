package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrador
 */
@Entity
@Cacheable(false)
public class VWEstadosExtras implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "EERDETALLE")
    private BigInteger eerdetalle;
    @NotNull
    @JoinColumn(name = "TURNOSEMPLEADOS", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private TurnosEmpleados turnoempleado;
    @Basic(optional = false)
    @NotNull
    @Size(max = 4)
    @Column(name = "CODIGO")
    private int codigo;
    @Basic(optional = false)
    @NotNull
    @Size(max = 30)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "FECHAPAGO")
    @Temporal(TemporalType.DATE)
    private Date fechapago;
    @Size(max = 1)
    @Column(name = "APROBADO")
    private String aprobado;
    @Size(max = 10)
    @Column(name = "OPCION")
    private String opcion;
    @Size(max = 2)
    @Column(name = "HORAS")
    private int horas;
    @Size(max = 2)
    @Column(name = "MINUTOS")
    private int minutos;
    @Basic(optional = false)
    @NotNull
    @Size(max = 200)
    @Column(name = "CONCEPTODESCRIPCION")
    private String conceptodescripcion;

    public BigInteger getEerdetalle() {
        return eerdetalle;
    }

    public void setEerdetalle(BigInteger eerdetalle) {
        this.eerdetalle = eerdetalle;
    }

    public TurnosEmpleados getTurnoempleado() {
        return turnoempleado;
    }

    public void setTurnoempleado(TurnosEmpleados turnoempleado) {
        this.turnoempleado = turnoempleado;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechapago() {
        return fechapago;
    }

    public void setFechapago(Date fechapago) {
        this.fechapago = fechapago;
    }

    public String getAprobado() {
        return aprobado;
    }

    public void setAprobado(String aprobado) {
        this.aprobado = aprobado;
    }

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public int getMinutos() {
        return minutos;
    }

    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }

    public String getConceptodescripcion() {
        return conceptodescripcion;
    }

    public void setConceptodescripcion(String conceptodescripcion) {
        this.conceptodescripcion = conceptodescripcion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eerdetalle != null ? eerdetalle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VWEstadosExtras)) {
            return false;
        }
        VWEstadosExtras other = (VWEstadosExtras) object;
        if ((this.eerdetalle == null && other.eerdetalle != null) || (this.eerdetalle != null && !this.eerdetalle.equals(other.eerdetalle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.VWEstadosExtras[ eerdetalle=" + eerdetalle + " ]";
    }

}
