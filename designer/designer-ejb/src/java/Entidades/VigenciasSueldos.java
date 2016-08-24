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
@Table(name = "VIGENCIASSUELDOS")
public class VigenciasSueldos implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "FECHAVIGENCIA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechavigencia;
    @Size(max = 1)
    @Column(name = "RETROACTIVO")
    private String retroactivo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VALOR")
    private BigDecimal valor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHASISTEMA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechasistema;
    @Column(name = "FECHAVIGENCIARETROACTIVO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechavigenciaretroactivo;
    @Column(name = "TEMPDISTRIBUCION")
    private BigInteger tempdistribucion;
    @Column(name = "FECHAVIGENCIAESCALAFON")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechavigenciaescalafon;
    @Size(max = 100)
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @JoinColumn(name = "UNIDADPAGO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Unidades unidadpago;
    @JoinColumn(name = "TIPOSUELDO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private TiposSueldos tiposueldo;
    @JoinColumn(name = "MOTIVOCAMBIOSUELDO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private MotivosCambiosSueldos motivocambiosueldo;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empleados empleado;
    @Transient
    private boolean checkRetroactivo;

    public VigenciasSueldos() {
    }

    public VigenciasSueldos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public VigenciasSueldos(BigInteger secuencia, Date fechavigencia, BigDecimal valor, Date fechasistema, Date fechavigenciaretroactivo) {
        this.secuencia = secuencia;
        this.fechavigencia = fechavigencia;
        this.valor = valor;
        this.fechasistema = fechasistema;
        this.fechavigenciaretroactivo = fechavigenciaretroactivo;
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

    public String getRetroactivo() {
        return retroactivo;
    }

    public void setRetroactivo(String retroactivo) {
        this.retroactivo = retroactivo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Date getFechasistema() {
        return fechasistema;
    }

    public void setFechasistema(Date fechasistema) {
        this.fechasistema = fechasistema;
    }

    public Date getFechavigenciaretroactivo() {
        return fechavigenciaretroactivo;
    }

    public void setFechavigenciaretroactivo(Date fechavigenciaretroactivo) {
        this.fechavigenciaretroactivo = fechavigenciaretroactivo;
    }

    public BigInteger getTempdistribucion() {
        return tempdistribucion;
    }

    public void setTempdistribucion(BigInteger tempdistribucion) {
        this.tempdistribucion = tempdistribucion;
    }

    public Date getFechavigenciaescalafon() {
        return fechavigenciaescalafon;
    }

    public void setFechavigenciaescalafon(Date fechavigenciaescalafon) {
        this.fechavigenciaescalafon = fechavigenciaescalafon;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Unidades getUnidadpago() {
        return unidadpago;
    }

    public void setUnidadpago(Unidades unidadpago) {
        this.unidadpago = unidadpago;
    }

    public TiposSueldos getTiposueldo() {
        return tiposueldo;
    }

    public void setTiposueldo(TiposSueldos tiposueldo) {
        this.tiposueldo = tiposueldo;
    }

    public MotivosCambiosSueldos getMotivocambiosueldo() {
        return motivocambiosueldo;
    }

    public void setMotivocambiosueldo(MotivosCambiosSueldos motivocambiosueldo) {
        this.motivocambiosueldo = motivocambiosueldo;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public boolean isCheckRetroactivo() {
        if(this.retroactivo == null || this.retroactivo.equals("N")){
            this.checkRetroactivo = false;
        }
        else{
            this.checkRetroactivo = true;
        }
        return checkRetroactivo;
    }

    public void setCheckRetroactivo(boolean checkRetroactivo) {
        if(checkRetroactivo == false){
           this.retroactivo = "N"; 
        }
        if(checkRetroactivo == true){
           this.retroactivo = "S";  
        }
        this.checkRetroactivo = checkRetroactivo;
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
        if (!(object instanceof VigenciasSueldos)) {
            return false;
        }
        VigenciasSueldos other = (VigenciasSueldos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Vigenciassueldos[ secuencia=" + secuencia + " ]";
    }
    
}
