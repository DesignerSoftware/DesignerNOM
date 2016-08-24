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
@Table(name = "SOLUCIONESNODOS")
public class SolucionesNodos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Size(max = 10)
    @Column(name = "ESTADO")
    private String estado;
    @Column(name = "AJUSTE")
    private BigInteger ajuste;
    @Size(max = 10)
    @Column(name = "TIPO")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VALOR")
    private BigDecimal valor;
    @Column(name = "UNIDADES")
    private BigDecimal unidades;
    @Column(name = "ULTIMAMODIFICACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimamodificacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHADESDE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechadesde;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHAHASTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechahasta;
    @Size(max = 500)
    @Column(name = "PARCIALES")
    private String parciales;
    @Column(name = "SALDO")
    private BigDecimal saldo;
    @Column(name = "FECHAPAGO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechapago;
    @Column(name = "VALORINCREMENTO")
    private BigInteger valorincremento;
    @Column(name = "PARAMETROTESORERIA")
    private BigInteger parametrotesoreria;
    @JoinColumn(name = "TIPOTRABAJADOR", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private TiposTrabajadores tipotrabajador;
    @JoinColumn(name = "TIPOCONTRATO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private TiposContratos tipocontrato;
    @JoinColumn(name = "NIT", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Terceros nit;
    @JoinColumn(name = "REFORMALABORAL", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private ReformasLaborales reformalaboral;
    @JoinColumn(name = "PROCESO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Procesos proceso;
    @JoinColumn(name = "PARAMETROPRESUPUESTO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Parametrospresupuestos parametropresupuesto;
    @JoinColumn(name = "CARGO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Cargos cargo;
    @JoinColumn(name = "CENTROCOSTOD", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private CentrosCostos centrocostod;
    @JoinColumn(name = "CENTROCOSTOC", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private CentrosCostos centrocostoc;
    @JoinColumn(name = "CONCEPTO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Conceptos concepto;
    @JoinColumn(name = "CORTEPROCESO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private CortesProcesos corteproceso;
    @JoinColumn(name = "CUENTAD", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Cuentas cuentad;
    @JoinColumn(name = "CUENTAC", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Cuentas cuentac;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empleados empleado;
    @JoinColumn(name = "ESTRUCTURA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Estructuras estructura;
    @JoinColumn(name = "LOCALIZACION", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Estructuras localizacion;
    @JoinColumn(name = "FORMULA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Formulas formula;
    @JoinColumn(name = "NODO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Nodos nodo;
    @Transient
    private BigDecimal pago;
    @Transient
    private BigDecimal descuento;
    @Transient
    private BigDecimal pasivo;
    @Transient
    private BigDecimal gasto;

    public SolucionesNodos() {
    }

    public SolucionesNodos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public SolucionesNodos(BigInteger secuencia, BigDecimal valor, Date fechadesde, Date fechahasta) {
        this.secuencia = secuencia;
        this.valor = valor;
        this.fechadesde = fechadesde;
        this.fechahasta = fechahasta;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
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

    public Date getUltimamodificacion() {
        return ultimamodificacion;
    }

    public void setUltimamodificacion(Date ultimamodificacion) {
        this.ultimamodificacion = ultimamodificacion;
    }

    public Date getFechadesde() {
        return fechadesde;
    }

    public void setFechadesde(Date fechadesde) {
        this.fechadesde = fechadesde;
    }

    public Date getFechahasta() {
        return fechahasta;
    }

    public void setFechahasta(Date fechahasta) {
        this.fechahasta = fechahasta;
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

    public Date getFechapago() {
        return fechapago;
    }

    public void setFechapago(Date fechapago) {
        this.fechapago = fechapago;
    }

    public BigInteger getValorincremento() {
        return valorincremento;
    }

    public void setValorincremento(BigInteger valorincremento) {
        this.valorincremento = valorincremento;
    }

    public BigInteger getParametrotesoreria() {
        return parametrotesoreria;
    }

    public void setParametrotesoreria(BigInteger parametrotesoreria) {
        this.parametrotesoreria = parametrotesoreria;
    }

    public TiposTrabajadores getTipotrabajador() {
        return tipotrabajador;
    }

    public void setTipotrabajador(TiposTrabajadores tipotrabajador) {
        this.tipotrabajador = tipotrabajador;
    }

    public TiposContratos getTipocontrato() {
        return tipocontrato;
    }

    public void setTipocontrato(TiposContratos tipocontrato) {
        this.tipocontrato = tipocontrato;
    }

    public Terceros getNit() {
        if(nit == null){
            nit = new Terceros();
        }
        return nit;
    }

    public void setNit(Terceros nit) {
        this.nit = nit;
    }

    public ReformasLaborales getReformalaboral() {
        return reformalaboral;
    }

    public void setReformalaboral(ReformasLaborales reformalaboral) {
        this.reformalaboral = reformalaboral;
    }

    public Procesos getProceso() {
        return proceso;
    }

    public void setProceso(Procesos proceso) {
        this.proceso = proceso;
    }

    public Parametrospresupuestos getParametropresupuesto() {
        return parametropresupuesto;
    }

    public void setParametropresupuesto(Parametrospresupuestos parametropresupuesto) {
        this.parametropresupuesto = parametropresupuesto;
    }

    public Cargos getCargo() {
        return cargo;
    }

    public void setCargo(Cargos cargo) {
        this.cargo = cargo;
    }

    public CentrosCostos getCentrocostod() {
        return centrocostod;
    }

    public void setCentrocostod(CentrosCostos centrocostod) {
        this.centrocostod = centrocostod;
    }

    public CentrosCostos getCentrocostoc() {
        return centrocostoc;
    }

    public void setCentrocostoc(CentrosCostos centrocostoc) {
        this.centrocostoc = centrocostoc;
    }

    public Conceptos getConcepto() {
        return concepto;
    }

    public void setConcepto(Conceptos concepto) {
        this.concepto = concepto;
    }

    public CortesProcesos getCorteproceso() {
        return corteproceso;
    }

    public void setCorteproceso(CortesProcesos corteproceso) {
        this.corteproceso = corteproceso;
    }

    public Cuentas getCuentad() {
        return cuentad;
    }

    public void setCuentad(Cuentas cuentad) {
        this.cuentad = cuentad;
    }

    public Cuentas getCuentac() {
        return cuentac;
    }

    public void setCuentac(Cuentas cuentac) {
        this.cuentac = cuentac;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public Estructuras getEstructura() {
        return estructura;
    }

    public void setEstructura(Estructuras estructura) {
        this.estructura = estructura;
    }

    public Estructuras getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(Estructuras localizacion) {
        this.localizacion = localizacion;
    }

    public Formulas getFormula() {
        return formula;
    }

    public void setFormula(Formulas formula) {
        this.formula = formula;
    }

    public Nodos getNodo() {
        return nodo;
    }

    public void setNodo(Nodos nodo) {
        this.nodo = nodo;
    }

    public BigDecimal getPago() {
        if (tipo.equals("PAGO")) {
            pago = valor;
        }
        return pago;
    }

    public void setPago(BigDecimal pago) {
        this.pago = pago;
    }

    public BigDecimal getDescuento() {
        if (tipo.equals("DESCUENTO")) {
            descuento = valor;
        }
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getPasivo() {
        if (tipo.equals("PASIVO")) {
            pasivo = valor;
        }
        return pasivo;
    }

    public void setPasivo(BigDecimal pasivo) {
        this.pasivo = pasivo;
    }

    public BigDecimal getGasto() {
        if (tipo.equals("GASTO")) {
            gasto = valor;
        }
        return gasto;
    }

    public void setGasto(BigDecimal gasto) {
        this.gasto = gasto;
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
        if (!(object instanceof SolucionesNodos)) {
            return false;
        }
        SolucionesNodos other = (SolucionesNodos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.SolucionesNodos[ secuencia=" + secuencia + " ]";
    }
}
