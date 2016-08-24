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
@Cacheable(false)
public class VWActualesSueldos implements Serializable {

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
    @Basic(optional = false)
    @NotNull
    @Column(name = "MOTIVOCAMBIOSUELDO")
    private BigInteger motivoCambioSueldo;
    @Size(max = 1)
    @Column(name = "RETROACTIVO")
    private String retroactivo;
    @JoinColumn(name = "TIPOSUELDO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private TiposSueldos tiposSueldo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "UNIDADPAGO")
    private BigInteger unidadPago;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VALOR")
    private BigDecimal valor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHASISTEMA")
    @Temporal(TemporalType.DATE)
    private Date fechaSistema;

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

    public BigInteger getMotivoCambioSueldo() {
        return motivoCambioSueldo;
    }

    public void setMotivoCambioSueldo(BigInteger motivoCambioSueldo) {
        this.motivoCambioSueldo = motivoCambioSueldo;
    }

    public String getRetroactivo() {
        return retroactivo;
    }

    public void setRetroactivo(String retroactivo) {
        this.retroactivo = retroactivo;
    }

    public TiposSueldos getTiposSueldo() {
        return tiposSueldo;
    }

    public void setTiposSueldo(TiposSueldos tiposSueldo) {
        this.tiposSueldo = tiposSueldo;
    }

    public BigInteger getUnidadPago() {
        return unidadPago;
    }

    public void setUnidadPago(BigInteger unidadPago) {
        this.unidadPago = unidadPago;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Date getFechaSistema() {
        return fechaSistema;
    }

    public void setFechaSistema(Date fechaSistema) {
        this.fechaSistema = fechaSistema;
    }
}
