package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "VWACUMULADOS")
public class VWAcumulados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @JoinColumn(name = "CONCEPTO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    @NotNull
    private Conceptos concepto;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    @NotNull
    private Empleados empleado;
    @Size(max = 10)
    @Column(name = "ESTADO")
    private String estado;
    @Column(name = "AJUSTE")
    private BigInteger ajuste;
    @Column(name = "CARGO")
    private BigInteger cargo;
    @JoinColumn(name = "CENTROCOSTOC", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    @NotNull
    private CentrosCostos centroCostoC;
    @JoinColumn(name = "CENTROCOSTOD", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    @NotNull
    private CentrosCostos centroCostoD;
    @JoinColumn(name = "CORTEPROCESO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private CortesProcesos corteProceso;
    @JoinColumn(name = "CUENTAC", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    @NotNull
    private Cuentas cuentaC;
    @JoinColumn(name = "CUENTAD", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    @NotNull
    private Cuentas cuentaD;
    @JoinColumn(name = "FORMULA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    @NotNull
    private Formulas formula;
    @Column(name = "NIT")
    private BigInteger nit;
    @Column(name = "NODO")
    private BigInteger nodo;
    @Column(name = "REFORMALABORAL")
    private BigInteger reformaLaboral;
    @Column(name = "TIPOCONTRATO")
    private BigInteger tipoContrato;
    @Column(name = "TIPOTRABAJADOR")
    @NotNull
    private BigInteger tipoTrabajador;
    @Column(name = "TIPO")
    @Size(max = 10)
    private String tipo;
    @Column(name = "VALOR")
    @NotNull
    private BigDecimal valor;
    @Column(name = "UNIDADES")
    private BigDecimal unidades;
    @Column(name = "ULTIMAMODIFICACION")
    @Temporal(TemporalType.DATE)
    private Date ultimaModificacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHADESDE")
    @Temporal(TemporalType.DATE)
    private Date fechaDesde;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHAHASTA")
    @Temporal(TemporalType.DATE)
    private Date fechaHasta;
    @Column(name = "PARCIALES")
    @Size(max = 500)
    private String parciales;
    @Column(name = "SALDO")
    private BigDecimal saldo;
    @Column(name = "CONCEPTO_CODIGO")
    private BigInteger concepto_Codigo;
    @Column(name = "CONCEPTO_DESCRIPCION")
    @Size(max = 200)
    @NotNull
    private String concepto_Descripcion;
    @Column(name = "NIT_NOMBRE")
    @Size(max = 30)
    private String nit_nombre;
    @NotNull
    @Size(max = 20)
    private String CuentaC_Codigo;
    @NotNull
    @Size(max = 20)
    private String CuentaD_Codigo;
    @Column(name = "FECHAPAGO")
    @Temporal(TemporalType.DATE)
    private Date fechaPago;
    @Column(name = "OBSERVACIONESNOVEDAD")
    @Size(max = 4000)
    private String observacionesMovedad;
    @Column(name = "MOTIVONOVEDAD")
    @Size(max = 4000)
    private String motivoNovedad;

    public VWAcumulados() {
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Conceptos getConcepto() {
        return concepto;
    }

    public void setConcepto(Conceptos concepto) {
        this.concepto = concepto;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigInteger getAjuste() {
        return ajuste;
    }

    public void setAjuste(BigInteger ajuste) {
        this.ajuste = ajuste;
    }

    public BigInteger getCargo() {
        return cargo;
    }

    public void setCargo(BigInteger cargo) {
        this.cargo = cargo;
    }

    public CentrosCostos getCentroCostoC() {
        return centroCostoC;
    }

    public void setCentroCostoC(CentrosCostos centroCostoC) {
        this.centroCostoC = centroCostoC;
    }

    public CentrosCostos getCentroCostoD() {
        return centroCostoD;
    }

    public void setCentroCostoD(CentrosCostos centroCostoD) {
        this.centroCostoD = centroCostoD;
    }

    public CortesProcesos getCorteProceso() {
        if (corteProceso == null) {
            corteProceso = new CortesProcesos();
        }
        return corteProceso;
    }

    public void setCorteProceso(CortesProcesos corteProceso) {
        this.corteProceso = corteProceso;
    }

    public Cuentas getCuentaC() {
        return cuentaC;
    }

    public void setCuentaC(Cuentas cuentaC) {
        this.cuentaC = cuentaC;
    }

    public Cuentas getCuentaD() {
        return cuentaD;
    }

    public void setCuentaD(Cuentas cuentaD) {
        this.cuentaD = cuentaD;
    }

    public Formulas getFormula() {
        return formula;
    }

    public void setFormula(Formulas formula) {
        this.formula = formula;
    }

    public BigInteger getNit() {
        return nit;
    }

    public void setNit(BigInteger nit) {
        this.nit = nit;
    }

    public BigInteger getNodo() {
        return nodo;
    }

    public void setNodo(BigInteger nodo) {
        this.nodo = nodo;
    }

    public BigInteger getReformaLaboral() {
        return reformaLaboral;
    }

    public void setReformaLaboral(BigInteger reformaLaboral) {
        this.reformaLaboral = reformaLaboral;
    }

    public BigInteger getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(BigInteger tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public BigInteger getTipoTrabajador() {
        return tipoTrabajador;
    }

    public void setTipoTrabajador(BigInteger tipoTrabajador) {
        this.tipoTrabajador = tipoTrabajador;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getUnidades() {
        return unidades;
    }

    public void setUnidades(BigDecimal unidades) {
        this.unidades = unidades;
    }

    public Date getUltimaModificacion() {
        return ultimaModificacion;
    }

    public void setUltimaModificacion(Date ultimaModificacion) {
        this.ultimaModificacion = ultimaModificacion;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public String getParciales() {
        return parciales;
    }

    public void setParciales(String parciales) {
        this.parciales = parciales;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigInteger getConcepto_Codigo() {
        return concepto_Codigo;
    }

    public void setConcepto_Codigo(BigInteger concepto_Codigo) {
        this.concepto_Codigo = concepto_Codigo;
    }

    public String getConcepto_Descripcion() {
        return concepto_Descripcion;
    }

    public void setConcepto_Descripcion(String concepto_Descripcion) {
        this.concepto_Descripcion = concepto_Descripcion;
    }

    public String getNit_nombre() {
        if (nit_nombre == null) {
            nit_nombre = "";
        }
        return nit_nombre;
    }

    public void setNit_nombre(String nit_nombre) {
        this.nit_nombre = nit_nombre;
    }

    public String getCuentaC_Codigo() {
        return CuentaC_Codigo;
    }

    public void setCuentaC_Codigo(String CuentaC_Codigo) {
        this.CuentaC_Codigo = CuentaC_Codigo;
    }

    public String getCuentaD_Codigo() {
        return CuentaD_Codigo;
    }

    public void setCuentaD_Codigo(String CuentaD_Codigo) {
        this.CuentaD_Codigo = CuentaD_Codigo;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getObservacionesMovedad() {
        if (observacionesMovedad == null) {
            observacionesMovedad = "";
        }
        return observacionesMovedad;
    }

    public void setObservacionesMovedad(String observacionesMovedad) {
        this.observacionesMovedad = observacionesMovedad;
    }

    public String getMotivoNovedad() {
        return motivoNovedad;
    }

    public void setMotivoNovedad(String motivoNovedad) {
        this.motivoNovedad = motivoNovedad;
    }
}
