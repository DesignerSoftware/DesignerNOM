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
 * @author Administrador
 */
@Entity
@Table(name = "INTERCON_SAPBO")
public class InterconSapBO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Size(max = 20)
    @Column(name = "CODIGOTERCERO")
    private String codigotercero;
    @Size(max = 28)
    @Column(name = "FLAG")
    private String flag;
    @Column(name = "FECHAULTIMAMODIFICACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaultimamodificacion;
    @Column(name = "FECHACONTABILIZACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechacontabilizacion;
    @Size(max = 50)
    @Column(name = "SALIDA")
    private String salida;
    @Size(max = 20)
    @Column(name = "NATURALEZA")
    private String naturaleza;
    @Column(name = "VALORC")
    private BigDecimal valorc;
    @Column(name = "VALORD")
    private BigDecimal valord;
    @Column(name = "EMPRESA_CODIGO")
    private Short empresaCodigo;
    @Column(name = "CONSECUTIVO")
    private BigInteger consecutivo;
    @Column(name = "FECHAVENCIMIENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechavencimiento;
    @Column(name = "LOTE")
    private BigInteger lote;
    @Size(max = 40)
    @Column(name = "CODIGOCUENTACONTABLE")
    private String codigocuentacontable;
    @JoinColumn(name = "PROYECTO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Proyectos proyecto;
    @Column(name = "PROCESO")
    private BigInteger proceso;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empleados empleado;
    @JoinColumn(name = "CUENTA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Cuentas cuenta;
    @JoinColumn(name = "CONTABILIZACION", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Contabilizaciones contabilizacion;
    @JoinColumn(name = "CONCEPTO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Conceptos concepto;
    @JoinColumn(name = "CENTROCOSTO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private CentrosCostos centrocosto;
    @Transient
    private Terceros terceroRegistro;

    public InterconSapBO() {
    }

    public InterconSapBO(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getCodigotercero() {
        return codigotercero;
    }

    public void setCodigotercero(String codigotercero) {
        this.codigotercero = codigotercero;
    }

    public Terceros getTerceroRegistro() {
        return terceroRegistro;
    }

    public void setTerceroRegistro(Terceros terceroRegistro) {
        this.terceroRegistro = terceroRegistro;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Date getFechaultimamodificacion() {
        return fechaultimamodificacion;
    }

    public void setFechaultimamodificacion(Date fechaultimamodificacion) {
        this.fechaultimamodificacion = fechaultimamodificacion;
    }

    public Date getFechacontabilizacion() {
        return fechacontabilizacion;
    }

    public void setFechacontabilizacion(Date fechacontabilizacion) {
        this.fechacontabilizacion = fechacontabilizacion;
    }

    public String getSalida() {
        return salida;
    }

    public void setSalida(String salida) {
        this.salida = salida;
    }

    public String getNaturaleza() {
        return naturaleza;
    }

    public void setNaturaleza(String naturaleza) {
        this.naturaleza = naturaleza;
    }

    public BigDecimal getValorc() {
        return valorc;
    }

    public void setValorc(BigDecimal valorc) {
        this.valorc = valorc;
    }

    public BigDecimal getValord() {
        return valord;
    }

    public void setValord(BigDecimal valord) {
        this.valord = valord;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Short getEmpresaCodigo() {
        return empresaCodigo;
    }

    public void setEmpresaCodigo(Short empresaCodigo) {
        this.empresaCodigo = empresaCodigo;
    }

    public BigInteger getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(BigInteger consecutivo) {
        this.consecutivo = consecutivo;
    }

    public Date getFechavencimiento() {
        return fechavencimiento;
    }

    public void setFechavencimiento(Date fechavencimiento) {
        this.fechavencimiento = fechavencimiento;
    }

    public BigInteger getLote() {
        return lote;
    }

    public void setLote(BigInteger lote) {
        this.lote = lote;
    }

    public String getCodigocuentacontable() {
        return codigocuentacontable;
    }

    public void setCodigocuentacontable(String codigocuentacontable) {
        this.codigocuentacontable = codigocuentacontable;
    }

    public Proyectos getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyectos proyecto) {
        this.proyecto = proyecto;
    }

    public BigInteger getProceso() {
        return proceso;
    }

    public void setProceso(BigInteger proceso) {
        this.proceso = proceso;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public Cuentas getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuentas cuenta) {
        this.cuenta = cuenta;
    }

    public Contabilizaciones getContabilizacion() {
        return contabilizacion;
    }

    public void setContabilizacion(Contabilizaciones contabilizacion) {
        this.contabilizacion = contabilizacion;
    }

    public Conceptos getConcepto() {
        return concepto;
    }

    public void setConcepto(Conceptos concepto) {
        this.concepto = concepto;
    }

    public CentrosCostos getCentrocosto() {
        return centrocosto;
    }

    public void setCentrocosto(CentrosCostos centrocosto) {
        this.centrocosto = centrocosto;
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
        if (!(object instanceof InterconSapBO)) {
            return false;
        }
        InterconSapBO other = (InterconSapBO) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.InterconSapBO[ secuencia=" + secuencia + " ]";
    }

}
