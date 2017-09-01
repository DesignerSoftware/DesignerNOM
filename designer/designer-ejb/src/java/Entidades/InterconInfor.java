/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "INTERCON_INFOR")
public class InterconInfor implements Serializable {

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
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empleados empleado;
    @JoinColumn(name = "CONCEPTO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Conceptos concepto;
    @Column(name = "VALOR")
    private BigDecimal valor;
    @Column(name = "FECHACONTABILIZACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechacontabilizacion;
    @JoinColumn(name = "CONTABILIZACION", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Contabilizaciones contabilizacion;
    @Size(max = 50)
    @Column(name = "SALIDA")
    private String salida;
    @Size(max = 20)
    @Column(name = "NATURALEZA")
    private String naturaleza;
    @Column(name = "EMPRESA_CODIGO")
    private Short empresaCodigo;
    @JoinColumn(name = "CENTROCOSTO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private CentrosCostos centrocosto;
    @NotNull
    @JoinColumn(name = "CUENTA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Cuentas cuenta;
    @Column(name = "PROCESO")
    private BigInteger proceso;
    @Column(name = "BASE")
    private BigInteger base;
    @Column(name = "CONSECUTIVO")
    private BigInteger consecutivo;
    @NotNull
    @Size(max = 60)
    @Column(name = "CODIGOCUENTACONTABLE")
    private String codigocuentacontable;
    @NotNull
    @Column(name = "EQUIVALENCIA")
    private String equivalencia;
    @NotNull
    @Column(name = "DESCRIPCIONCUENTACONTABLE")
    private String descripcioncuentacontable;
    @JoinColumn(name = "PROYECTO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Proyectos proyecto;
    @Transient
    private Terceros terceroRegistro;
    @Transient
    private String nombreempleado;
    @Transient
    private String nombreproceso;

    public InterconInfor() {
    }

    public InterconInfor(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getCodigotercero() {
        return codigotercero;
    }

    public void setCodigotercero(String codigotercero) {
        this.codigotercero = codigotercero;
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

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public Conceptos getConcepto() {
        return concepto;
    }

    public void setConcepto(Conceptos concepto) {
        this.concepto = concepto;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Date getFechacontabilizacion() {
        return fechacontabilizacion;
    }

    public void setFechacontabilizacion(Date fechacontabilizacion) {
        this.fechacontabilizacion = fechacontabilizacion;
    }

    public Contabilizaciones getContabilizacion() {
        return contabilizacion;
    }

    public void setContabilizacion(Contabilizaciones contabilizacion) {
        this.contabilizacion = contabilizacion;
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

    public Short getEmpresaCodigo() {
        return empresaCodigo;
    }

    public void setEmpresaCodigo(Short empresaCodigo) {
        this.empresaCodigo = empresaCodigo;
    }

    public CentrosCostos getCentrocosto() {
        return centrocosto;
    }

    public void setCentrocosto(CentrosCostos centrocosto) {
        this.centrocosto = centrocosto;
    }

    public Cuentas getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuentas cuenta) {
        this.cuenta = cuenta;
    }

    public BigInteger getProceso() {
        return proceso;
    }

    public void setProceso(BigInteger proceso) {
        this.proceso = proceso;
    }

    public BigInteger getBase() {
        return base;
    }

    public void setBase(BigInteger base) {
        this.base = base;
    }

    public BigInteger getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(BigInteger consecutivo) {
        this.consecutivo = consecutivo;
    }

    public String getCodigocuentacontable() {
        return codigocuentacontable;
    }

    public void setCodigocuentacontable(String codigocuentacontable) {
        this.codigocuentacontable = codigocuentacontable;
    }

    public String getEquivalencia() {
        return equivalencia;
    }

    public void setEquivalencia(String equivalencia) {
        this.equivalencia = equivalencia;
    }

    public String getDescripcioncuentacontable() {
        return descripcioncuentacontable;
    }

    public void setDescripcioncuentacontable(String descripcioncuentacontable) {
        this.descripcioncuentacontable = descripcioncuentacontable;
    }

    public Proyectos getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyectos proyecto) {
        this.proyecto = proyecto;
    }

    public Terceros getTerceroRegistro() {
        return terceroRegistro;
    }

    public void setTerceroRegistro(Terceros terceroRegistro) {
        this.terceroRegistro = terceroRegistro;
    }

    public String getNombreempleado() {
        return nombreempleado;
    }

    public void setNombreempleado(String nombreempleado) {
        this.nombreempleado = nombreempleado;
    }

    public String getNombreproceso() {
        return nombreproceso;
    }

    public void setNombreproceso(String nombreproceso) {
        this.nombreproceso = nombreproceso;
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
        if (!(object instanceof InterconInfor)) {
            return false;
        }
        InterconInfor other = (InterconInfor) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.InterconSapBO[ secuencia=" + secuencia + " ]";
    }
    
    public void llenarTransients(InterconAux aporteAux) {
        this.nombreproceso = aporteAux.getNombreproceso();
        this.nombreempleado = aporteAux.getNombreempleado();
    }

}
