package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Cacheable(false)
@Table(name = "VWACTUALESTIPOSCONTRATOS")
public class VWActualesTiposContratos implements Serializable {

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
    @Column(name = "MOTIVOCONTRATO")
    private BigInteger motivoContrato;
    @JoinColumn(name = "TIPOCONTRATO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private TiposContratos tipoContrato;
    @Size(max = 1024)
    @Column(name = "OBSERVACIONES")
    private String observaciones;

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

    public Date getFechaVigencia() throws ParseException {
        return fechaVigencia;
    }

    public void setFechaVigencia(Date fechaVigencia) throws ParseException {
        this.fechaVigencia =fechaVigencia; //formato.parse(f);
        
    }

    public BigInteger getMotivoContrato() {
        return motivoContrato;
    }

    public void setMotivoContrato(BigInteger motivoContrato) {
        this.motivoContrato = motivoContrato;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public TiposContratos getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(TiposContratos tipoContrato) {
        this.tipoContrato = tipoContrato;
    }
}
