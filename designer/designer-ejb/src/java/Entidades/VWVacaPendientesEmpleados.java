package Entidades;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.persistence.Column;
import javax.persistence.Basic;
import java.math.BigInteger;
import java.util.Date;
import java.io.Serializable;

/**
 *
 * @author user
 */
@Entity
@Table(name = "VWVACAPENDIENTESEMPLEADOS")
public class VWVacaPendientesEmpleados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "RFVACACION")
    private BigInteger rfvacacion;
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EMPLEADO")
    private BigInteger empleado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "ESTADO")
    private String estado;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "DIASPENDIENTES")
    private BigInteger diaspendientes;
    @Column(name = "INICIALCAUSACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date inicialcausacion;
    @Column(name = "FINALCAUSACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date finalcausacion;

   
    public BigInteger getEmpleado() {
        return empleado;
    }

    public void setEmpleado(BigInteger empleado) {
        this.empleado = empleado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigInteger getDiaspendientes() {
        return diaspendientes;
    }

    public void setDiaspendientes(BigInteger diaspendientes) {
        this.diaspendientes = diaspendientes;
    }

    public Date getInicialcausacion() {
        return inicialcausacion;
    }

    public void setInicialcausacion(Date inicialcausacion) {
        this.inicialcausacion = inicialcausacion;
    }

    public Date getFinalcausacion() {
        return finalcausacion;
    }

    public void setFinalcausacion(Date finalcausacion) {
        this.finalcausacion = finalcausacion;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getRfvacacion() {
        return rfvacacion;
    }

    public void setRfvacacion(BigInteger rfvacacion) {
        this.rfvacacion = rfvacacion;
    }
}
