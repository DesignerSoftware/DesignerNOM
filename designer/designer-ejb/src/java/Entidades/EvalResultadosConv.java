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
@Table(name = "EVALRESULTADOSCONV")
public class EvalResultadosConv implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "PUNTAJEOBTENIDO")
    private Integer puntajeobtenido;
    @Column(name = "FECHAPERIODODESDE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaperiododesde;
    @Column(name = "FECHAPERIODOHASTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaperiodohasta;
    @Size(max = 50)
    @Column(name = "NOMBREPRUEBA")
    private String nombreprueba;
    @Size(max = 1)
    @Column(name = "ESTADOEVAL")
    private String estadoeval;
    @JoinColumn(name = "EVALCONVOCATORIA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Evalconvocatorias evalconvocatoria;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empleados empleado;

    public EvalResultadosConv() {
    }

    public EvalResultadosConv(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Integer getPuntajeobtenido() {
        return puntajeobtenido;
    }

    public void setPuntajeobtenido(Integer puntajeobtenido) {
        this.puntajeobtenido = puntajeobtenido;
    }

    public Date getFechaperiododesde() {
        return fechaperiododesde;
    }

    public void setFechaperiododesde(Date fechaperiododesde) {
        this.fechaperiododesde = fechaperiododesde;
    }

    public Date getFechaperiodohasta() {
        return fechaperiodohasta;
    }

    public void setFechaperiodohasta(Date fechaperiodohasta) {
        this.fechaperiodohasta = fechaperiodohasta;
    }

    public String getNombreprueba() {
        return nombreprueba;
    }

    public void setNombreprueba(String nombreprueba) {
        this.nombreprueba = nombreprueba;
    }

    public String getEstadoeval() {
        return estadoeval;
    }

    public void setEstadoeval(String estadoeval) {
        this.estadoeval = estadoeval;
    }

    public Evalconvocatorias getEvalconvocatoria() {
        return evalconvocatoria;
    }

    public void setEvalconvocatoria(Evalconvocatorias evalconvocatoria) {
        this.evalconvocatoria = evalconvocatoria;
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
        if (!(object instanceof EvalResultadosConv)) {
            return false;
        }
        EvalResultadosConv other = (EvalResultadosConv) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.EvalResultadosConv[ secuencia=" + secuencia + " ]";
    }
    
}
