
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
@Cacheable(false)
public class VWActualesJornadas implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empleados empleado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHAVIGENCIA")
    @Temporal(TemporalType.DATE)
    private Date fechaVigencia;
    @JoinColumn(name = "JORNADATRABAJO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private JornadasLaborales jornadaTrabajo;
    @Column(name = "TIPODESCANSO")
    private BigInteger tipoDescanso;

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public Date getFechaVigencia() {
        return fechaVigencia;
    }

    public void setFechaVigencia(Date fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
    }

    public JornadasLaborales getJornadaTrabajo() {
        return jornadaTrabajo;
    }

    public void setJornadaTrabajo(JornadasLaborales jornadaTrabajo) {
        this.jornadaTrabajo = jornadaTrabajo;
    }

    public BigInteger getTipoDescanso() {
        return tipoDescanso;
    }

    public void setTipoDescanso(BigInteger tipoDescanso) {
        this.tipoDescanso = tipoDescanso;
    }
}
