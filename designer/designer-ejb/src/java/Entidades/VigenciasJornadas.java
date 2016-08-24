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
@Table(name = "VIGENCIASJORNADAS")
public class VigenciasJornadas implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "FECHAVIGENCIA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechavigencia;
    @JoinColumn(name = "TIPODESCANSO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private TiposDescansos tipodescanso;
    @JoinColumn(name = "JORNADATRABAJO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private JornadasLaborales jornadatrabajo;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empleados empleado;

    public VigenciasJornadas() {
    }

    public VigenciasJornadas(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public VigenciasJornadas(BigInteger secuencia, Date fechavigencia) {
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

    public TiposDescansos getTipodescanso() {
        if(tipodescanso == null){
            return new TiposDescansos();
        }
        return tipodescanso;
    }

    public void setTipodescanso(TiposDescansos tipodescanso) {
        this.tipodescanso = tipodescanso;
    }

    public JornadasLaborales getJornadatrabajo() {
        if(jornadatrabajo==null){
            return new JornadasLaborales();
        }
        return jornadatrabajo;
    }

    public void setJornadatrabajo(JornadasLaborales jornadatrabajo) {
        this.jornadatrabajo = jornadatrabajo;
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
        if (!(object instanceof VigenciasJornadas)) {
            return false;
        }
        VigenciasJornadas other = (VigenciasJornadas) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Vigenciasjornadas[ secuencia=" + secuencia + " ]";
    }
    
}
