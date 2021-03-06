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
    @Temporal(TemporalType.DATE)
    private Date fechadesde;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHAHASTA")
    @Temporal(TemporalType.DATE)
    private Date fechahasta;
    @Size(max = 500)
    @Column(name = "PARCIALES")
    private String parciales;
    @Column(name = "SALDO")
    private BigDecimal saldo;
    @Column(name = "FECHAPAGO")
    @Temporal(TemporalType.DATE)
    private Date fechapago;
    @Column(name = "VALORINCREMENTO")
    private BigInteger valorincremento;
    @Column(name = "PARAMETROTESORERIA")
    private BigInteger parametrotesoreria;
    //
    @Column(name = "TIPOTRABAJADOR")
    private BigInteger tipotrabajador;
    @Column(name = "TIPOCONTRATO")
    private BigInteger tipocontrato;
    @Column(name = "NIT")
    private BigInteger nittercero;
    @Column(name = "REFORMALABORAL")
    private BigInteger reformalaboral;
    @Column(name = "PROCESO")
    private BigInteger proceso;
    @Column(name = "PARAMETROPRESUPUESTO")
    private BigInteger parametropresupuesto;
    @Column(name = "CARGO")
    private BigInteger cargo;
    @Column(name = "CENTROCOSTOD")
    private BigInteger centrocostod;
    @Column(name = "CENTROCOSTOC")
    private BigInteger centrocostoc;
    @Column(name = "CONCEPTO")
    private BigInteger concepto;
    @Column(name = "CORTEPROCESO")
    private BigInteger corteproceso;
    @Column(name = "CUENTAD")
    private BigInteger cuentad;
    @Column(name = "CUENTAC")
    private BigInteger cuentac;
    @Column(name = "EMPLEADO")
    private BigInteger empleado;
    @Column(name = "ESTRUCTURA")
    private BigInteger estructura;
    @Column(name = "LOCALIZACION")
    private BigInteger localizacion;
    @Column(name = "FORMULA")
    private BigInteger formula;
    @Column(name = "NODO")
    //
    private BigInteger nodo;
    @Transient
    private BigDecimal pago;
    @Transient
    private BigDecimal descuento;
    @Transient
    private BigDecimal pasivo;
    @Transient
    private BigDecimal gasto;
    //
    @Transient
    private String nombretipotrabajador;
    @Transient
    private String nombretipocontrato;
    @Transient
    private String nombretercero;
    @Transient
    private String nombrereformalaboral;
    @Transient
    private String nombreproceso;
    @Transient
    private String nombrecargo;
    @Transient
    private String nombrecentrocostod;
    @Transient
    private String nombrecentrocostoc;
    @Transient
    private String nombreconcepto;
    @Transient
    private BigInteger codigoconcepto;
    @Transient
    private String codigocuentad;
    @Transient
    private String codigocuentac;
    @Transient
    private String nombreempleado;
    @Transient
    private String nombreestructura;
    @Transient
    private String nombreformula;
    @Transient
    private String nombretipo;

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

    public SolucionesNodos clonarObjeto() {
        SolucionesNodos clon = new SolucionesNodos();
        clon.setSecuencia(secuencia);
        clon.setEstado(estado);
        clon.setAjuste(ajuste);
        clon.setTipo(tipo);
        clon.setValor(valor);
        clon.setUnidades(unidades);
        clon.setUltimamodificacion(ultimamodificacion);
        clon.setFechadesde(fechadesde);
        clon.setFechahasta(fechahasta);
        clon.setParciales(parciales);
        clon.setSaldo(saldo);
        clon.setFechapago(fechapago);
        clon.setValorincremento(valorincremento);
        clon.setParametrotesoreria(parametrotesoreria);
        clon.setTipotrabajador(tipotrabajador);
        clon.setTipocontrato(tipocontrato);
        clon.setNittercero(nittercero);
        clon.setReformalaboral(reformalaboral);
        clon.setProceso(proceso);
        clon.setParametropresupuesto(parametropresupuesto);
        clon.setCargo(cargo);
        clon.setCentrocostod(centrocostod);
        clon.setCentrocostoc(centrocostoc);
        clon.setConcepto(concepto);
        clon.setCorteproceso(corteproceso);
        clon.setCuentad(cuentad);
        clon.setCuentac(cuentac);
        clon.setEmpleado(empleado);
        clon.setEstructura(estructura);
        clon.setLocalizacion(localizacion);
        clon.setFormula(formula);
        clon.setNodo(nodo);
        clon.setPago(pago);
        clon.setDescuento(descuento);
        clon.setPasivo(pasivo);
        clon.setGasto(gasto);
        clon.setNombretipotrabajador(nombretipotrabajador);
        clon.setNombretipocontrato(nombretipocontrato);
        clon.setNombretercero(nombretercero);
        clon.setNombrereformalaboral(nombrereformalaboral);
        clon.setNombreproceso(nombreproceso);
        clon.setNombrecargo(nombrecargo);
        clon.setNombrecentrocostod(nombrecentrocostod);
        clon.setNombrecentrocostoc(nombrecentrocostoc);
        clon.setNombreconcepto(nombreconcepto);
        clon.setCodigoconcepto(codigoconcepto);
        clon.setCodigocuentad(codigocuentad);
        clon.setCodigocuentac(codigocuentac);
        clon.setNombreempleado(nombreempleado);
        clon.setNombreestructura(nombreestructura);
        clon.setNombreformula(nombreformula);
        return clon;
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

    public BigInteger getTipotrabajador() {
        return tipotrabajador;
    }

    public void setTipotrabajador(BigInteger tipotrabajador) {
        this.tipotrabajador = tipotrabajador;
    }

    public BigInteger getTipocontrato() {
        return tipocontrato;
    }

    public void setTipocontrato(BigInteger tipocontrato) {
        this.tipocontrato = tipocontrato;
    }

    public BigInteger getNittercero() {
        return nittercero;
    }

    public void setNittercero(BigInteger nittercero) {
        this.nittercero = nittercero;
    }

    public BigInteger getReformalaboral() {
        return reformalaboral;
    }

    public void setReformalaboral(BigInteger reformalaboral) {
        this.reformalaboral = reformalaboral;
    }

    public BigInteger getProceso() {
        return proceso;
    }

    public void setProceso(BigInteger proceso) {
        this.proceso = proceso;
    }

    public BigInteger getParametropresupuesto() {
        return parametropresupuesto;
    }

    public void setParametropresupuesto(BigInteger parametropresupuesto) {
        this.parametropresupuesto = parametropresupuesto;
    }

    public BigInteger getCargo() {
        return cargo;
    }

    public void setCargo(BigInteger cargo) {
        this.cargo = cargo;
    }

    public BigInteger getCentrocostod() {
        return centrocostod;
    }

    public void setCentrocostod(BigInteger centrocostod) {
        this.centrocostod = centrocostod;
    }

    public BigInteger getCentrocostoc() {
        return centrocostoc;
    }

    public void setCentrocostoc(BigInteger centrocostoc) {
        this.centrocostoc = centrocostoc;
    }

    public BigInteger getConcepto() {
        return concepto;
    }

    public void setConcepto(BigInteger concepto) {
        this.concepto = concepto;
    }

    public BigInteger getCorteproceso() {
        return corteproceso;
    }

    public void setCorteproceso(BigInteger corteproceso) {
        this.corteproceso = corteproceso;
    }

    public BigInteger getCuentad() {
        return cuentad;
    }

    public void setCuentad(BigInteger cuentad) {
        this.cuentad = cuentad;
    }

    public BigInteger getCuentac() {
        return cuentac;
    }

    public void setCuentac(BigInteger cuentac) {
        this.cuentac = cuentac;
    }

    public BigInteger getEmpleado() {
        return empleado;
    }

    public void setEmpleado(BigInteger empleado) {
        this.empleado = empleado;
    }

    public BigInteger getEstructura() {
        return estructura;
    }

    public void setEstructura(BigInteger estructura) {
        this.estructura = estructura;
    }

    public BigInteger getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(BigInteger localizacion) {
        this.localizacion = localizacion;
    }

    public BigInteger getFormula() {
        return formula;
    }

    public void setFormula(BigInteger formula) {
        this.formula = formula;
    }

    public BigInteger getNodo() {
        return nodo;
    }

    public void setNodo(BigInteger nodo) {
        this.nodo = nodo;
    }

    public String getNombretipotrabajador() {
        return nombretipotrabajador;
    }

    public void setNombretipotrabajador(String nombretipotrabajador) {
        this.nombretipotrabajador = nombretipotrabajador;
    }

    public String getNombretipocontrato() {
        return nombretipocontrato;
    }

    public void setNombretipocontrato(String nombretipocontrato) {
        this.nombretipocontrato = nombretipocontrato;
    }

    public String getNombretercero() {
        return nombretercero;
    }

    public void setNombretercero(String nombretercero) {
        this.nombretercero = nombretercero;
    }

    public String getNombrereformalaboral() {
        return nombrereformalaboral;
    }

    public void setNombrereformalaboral(String nombrereformalaboral) {
        this.nombrereformalaboral = nombrereformalaboral;
    }

    public String getNombreproceso() {
        return nombreproceso;
    }

    public void setNombreproceso(String nombreproceso) {
        this.nombreproceso = nombreproceso;
    }

    public String getNombrecargo() {
        return nombrecargo;
    }

    public void setNombrecargo(String nombrecargo) {
        this.nombrecargo = nombrecargo;
    }

    public String getNombrecentrocostod() {
        return nombrecentrocostod;
    }

    public void setNombrecentrocostod(String nombrecentrocostod) {
        this.nombrecentrocostod = nombrecentrocostod;
    }

    public String getNombrecentrocostoc() {
        return nombrecentrocostoc;
    }

    public void setNombrecentrocostoc(String nombrecentrocostoc) {
        this.nombrecentrocostoc = nombrecentrocostoc;
    }

    public String getNombreconcepto() {
        return nombreconcepto;
    }

    public void setNombreconcepto(String nombreconcepto) {
        this.nombreconcepto = nombreconcepto;
    }

    public String getCodigocuentad() {
        return codigocuentad;
    }

    public void setCodigocuentad(String codigocuentad) {
        this.codigocuentad = codigocuentad;
    }

    public String getCodigocuentac() {
        return codigocuentac;
    }

    public void setCodigocuentac(String codigocuentac) {
        this.codigocuentac = codigocuentac;
    }

    public String getNombreempleado() {
        return nombreempleado;
    }

    public void setNombreempleado(String nombreempleado) {
        this.nombreempleado = nombreempleado;
    }

    public String getNombreestructura() {
        return nombreestructura;
    }

    public void setNombreestructura(String nombreestructura) {
        this.nombreestructura = nombreestructura;
    }

    public String getNombreformula() {
        return nombreformula;
    }

    public void setNombreformula(String nombreformula) {
        this.nombreformula = nombreformula;
    }

    public BigInteger getCodigoconcepto() {
        return codigoconcepto;
    }

    public void setCodigoconcepto(BigInteger codigoconcepto) {
        this.codigoconcepto = codigoconcepto;
    }

    public String getNombretipo() {
        if (nombretipo == null) {
            if (tipo == null) {
                nombretipo = "PAGO";
            } else if (tipo.equalsIgnoreCase("PAGO")) {
                nombretipo = "PAGO";
            } else if (tipo.equalsIgnoreCase("DESCUENTO")) {
                nombretipo = "DESCUENTO";
            } else if (tipo.equalsIgnoreCase("PASIVO")) {
                nombretipo = "PASIVO";
            } else if (tipo.equalsIgnoreCase("GASTO")) {
                nombretipo = "GASTO";
            } else if (tipo.equalsIgnoreCase("NETO")) {
                nombretipo = "NETO";
            }
        }
        return nombretipo;
    }

    public void setNombretipo(String nombretipo) {
        this.nombretipo = nombretipo;
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

    public void llenarTransients(SolucionesNodosAux solucionNodoAux) {
        this.setCodigoconcepto(solucionNodoAux.getCodigoconcepto());
        this.setNombreconcepto(solucionNodoAux.getNombreconcepto());
        this.setNombretercero(solucionNodoAux.getNombretercero());
        this.setCodigocuentad(solucionNodoAux.getCodigocuentad());
        this.setCodigocuentac(solucionNodoAux.getCodigocuentac());
        this.setNombreempleado(solucionNodoAux.getNombreempleado());
        this.setNombrecentrocostod(solucionNodoAux.getNombrecentrocostod());
        this.setNombrecentrocostoc(solucionNodoAux.getNombrecentrocostoc());
        this.setNombreproceso(solucionNodoAux.getNombreproceso());
    }
}
