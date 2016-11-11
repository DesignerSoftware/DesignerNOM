package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
//import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "APORTESENTIDADES")
public class AportesEntidades implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ANO")
    private short ano;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MES")
    private short mes;
    @Column(name = "TERCERO")
    private BigInteger tercero;
    @Column(name = "SUCURSAL")
    private BigInteger sucursal;
    @Column(name = "COTIZACION1")
    private BigDecimal cotizacion1;
    @Size(max = 40)
    @Column(name = "COMENTARIOCOTI1")
    private String comentariocoti1;
    @Column(name = "COTIZACION2")
    private BigDecimal cotizacion2;
    @Size(max = 40)
    @Column(name = "COMENTARIOCOTI2")
    private String comentariocoti2;
    @Column(name = "COTIZACION3")
    private BigDecimal cotizacion3;
    @Size(max = 40)
    @Column(name = "COMENTARIOCOTI3")
    private String comentariocoti3;
    @Column(name = "COTIZACION4")
    private BigDecimal cotizacion4;
    @Size(max = 40)
    @Column(name = "COMENTARIOCOTI4")
    private String comentariocoti4;
    @Column(name = "COTIZACION5")
    private BigDecimal cotizacion5;
    @Size(max = 40)
    @Column(name = "COMENTARIOCOTI5")
    private String comentariocoti5;
    @Column(name = "NOVEDAD1")
    private BigDecimal novedad1;
    @Size(max = 40)
    @Column(name = "COMENTARIONOVE1")
    private String comentarionove1;
    @Column(name = "NOVEDAD2")
    private BigDecimal novedad2;
    @Size(max = 40)
    @Column(name = "COMENTARIONOVE2")
    private String comentarionove2;
    @Column(name = "NOVEDAD3")
    private BigDecimal novedad3;
    @Size(max = 40)
    @Column(name = "COMENTARIONOVE3")
    private String comentarionove3;
    @Column(name = "NOVEDAD4")
    private BigDecimal novedad4;
    @Size(max = 40)
    @Column(name = "COMENTARIONOVE4")
    private String comentarionove4;
    @Column(name = "NOVEDAD5")
    private BigDecimal novedad5;
    @Size(max = 40)
    @Column(name = "COMENTARIONOVE5")
    private String comentarionove5;
    @Column(name = "SALARIOBASICO")
    private BigInteger salariobasico;
    @Column(name = "IBC")
    private BigInteger ibc;
    @Column(name = "IBCREFERENCIA")
    private BigInteger ibcreferencia;
    @Column(name = "DIASCOTIZADOS")
    private Short diascotizados;
    @Column(name = "TIPOAPORTANTE")
    private Short tipoaportante;
    @Size(max = 1)
    @Column(name = "ING")
    private String ing;
    @Size(max = 1)
    @Column(name = "RET")
    private String ret;
    @Size(max = 1)
    @Column(name = "TDA")
    private String tda;
    @Size(max = 1)
    @Column(name = "TAA")
    private String taa;
    @Size(max = 1)
    @Column(name = "VSP")
    private String vsp;
    @Size(max = 1)
    @Column(name = "VTE")
    private String vte;
    @Size(max = 1)
    @Column(name = "VST")
    private String vst;
    @Size(max = 1)
    @Column(name = "SLN")
    private String sln;
    @Size(max = 1)
    @Column(name = "IGE")
    private String ige;
    @Size(max = 1)
    @Column(name = "LMA")
    private String lma;
    @Size(max = 1)
    @Column(name = "VAC")
    private String vac;
    @Size(max = 1)
    @Column(name = "AVP")
    private String avp;
    @Size(max = 1)
    @Column(name = "VCT")
    private String vct;
    @Size(max = 2)
    @Column(name = "IRP")
    private String irp;
    @Column(name = "TARIFAAFP")
    private BigInteger tarifaafp;
    @Column(name = "TARIFACTT")
    private BigInteger tarifactt;
    @Size(max = 15)
    @Column(name = "CODIGOCTT")
    private String codigoctt;
    @Column(name = "AVPEVALOR")
    private BigInteger avpevalor;
    @Column(name = "AVPPVALOR")
    private BigInteger avppvalor;
    @Column(name = "RETCONTAVPVALOR")
    private BigInteger retcontavpvalor;
    @Size(max = 6)
    @Column(name = "CODIGONEPS")
    private String codigoneps;
    @Size(max = 6)
    @Column(name = "CODIGONAFP")
    private String codigonafp;
    @Column(name = "EGVALOR")
    private BigInteger egvalor;
    @Size(max = 15)
    @Column(name = "EGAUTORIZACION")
    private String egautorizacion;
    @Column(name = "MATERNIDADVALOR")
    private BigInteger maternidadvalor;
    @Size(max = 15)
    @Column(name = "MATERNIDADAUTORIZACION")
    private String maternidadautorizacion;
    @Column(name = "UPCVALOR")
    private BigInteger upcvalor;
    @Size(max = 15)
    @Column(name = "TIPO")
    private String tipo;
    @Size(max = 1)
    @Column(name = "MODIFICADOMANUALMENTE")
    private String modificadomanualmente;
    @Column(name = "TARIFAEPS")
    private BigInteger tarifaeps;
    @Column(name = "TIPOPENSIONADO")
    private Short tipopensionado;
    @Size(max = 1)
    @Column(name = "SUS")
    private String sus;
    @Size(max = 1)
    @Column(name = "PENSIONCOMPARTIDA")
    private String pensioncompartida;
    @Size(max = 1)
    @Column(name = "INTEGRAL")
    private String integral;
    @Column(name = "COTIZACION1ORIGINAL")
    private BigInteger cotizacion1original;
    @Column(name = "COTIZACION2ORIGINAL")
    private BigInteger cotizacion2original;
    @Column(name = "COTIZACION3ORIGINAL")
    private BigInteger cotizacion3original;
    @Column(name = "COTIZACION4ORIGINAL")
    private BigInteger cotizacion4original;
    @Column(name = "COTIZACION5ORIGINAL")
    private BigInteger cotizacion5original;
    @Column(name = "NOVEDAD1ORIGINAL")
    private BigInteger novedad1original;
    @Column(name = "NOVEDAD2ORIGINAL")
    private BigInteger novedad2original;
    @Column(name = "NOVEDAD3ORIGINAL")
    private BigInteger novedad3original;
    @Column(name = "NOVEDAD4ORIGINAL")
    private BigInteger novedad4original;
    @Column(name = "NOVEDAD5ORIGINAL")
    private BigInteger novedad5original;
    @Column(name = "ORIGEN")
    private Short origen;
    @Column(name = "FECHAORIGEN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaorigen;
    @Column(name = "FECHAINGRESO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaingreso;
    @Column(name = "FECHAHASTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechahasta;
    @Column(name = "SUBTIPOCOTIZANTE")
    private BigInteger subtipocotizante;
    @Size(max = 1)
    @Column(name = "EXTRANJERO")
    private String extranjero;
    @Column(name = "TARIFACAJA")
    private BigInteger tarifacaja;
    @Column(name = "TARIFASENA")
    private BigInteger tarifasena;
    @Column(name = "TARIFAICBF")
    private BigInteger tarifaicbf;
    @Column(name = "TARIFAESAP")
    private BigInteger tarifaesap;
    @Column(name = "TARIFAMEN")
    private BigInteger tarifamen;
    @Column(name = "IBCORIGINAL")
    private BigInteger ibcoriginal;
    @Size(max = 1)
    @Column(name = "TIPOPLANILLA")
    private String tipoplanilla;
    @Size(max = 1)
    @Column(name = "IDENTICOAPAGADO")
    private String identicoapagado;
    @Column(name = "TOTALCORRECCION")
    private BigInteger totalcorreccion;
    @JoinColumn(name = "TIPOTRABAJADOR", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private TiposTrabajadores tipotrabajador;
    @JoinColumn(name = "TIPOENTIDAD", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private TiposEntidades tipoentidad;
    @JoinColumn(name = "PARAMETROPRESUPUESTO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Parametrospresupuestos parametropresupuesto;
    @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empresas empresa;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empleados empleado;
    @Transient
    private String nombretercero;
    @Transient
    private Terceros terceroRegistro;
    @Transient
    private String subtipocotizanteSTR;
    @Transient
    private boolean bing;
    @Transient
    private boolean bret;
    @Transient
    private boolean btda;
    @Transient
    private boolean btaa;
    @Transient
    private boolean bvsp;
    @Transient
    private boolean bvte;
    @Transient
    private boolean bvst;
    @Transient
    private boolean bsln;
    @Transient
    private boolean bige;
    @Transient
    private boolean blma;
    @Transient
    private boolean bvac;
    @Transient
    private boolean bavp;
    @Transient
    private boolean bvct;
    @Transient
    private boolean birp;
    @Transient
    private boolean bsus;
    @Transient
    private boolean binte;

    public AportesEntidades() {
    }

    public AportesEntidades(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public AportesEntidades(BigInteger secuencia, short ano, short mes) {
        this.secuencia = secuencia;
        this.ano = ano;
        this.mes = mes;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public short getAno() {
        return ano;
    }

    public void setAno(short ano) {
        this.ano = ano;
    }

    public short getMes() {
        return mes;
    }

    public void setMes(short mes) {
        this.mes = mes;
    }

    public BigInteger getTercero() {
        return tercero;
    }

    public void setTercero(BigInteger tercero) {
        this.tercero = tercero;
    }

    public Terceros getTerceroRegistro() {
        return terceroRegistro;
    }

    public void setTerceroRegistro(Terceros terceroRegistro) {
        this.terceroRegistro = terceroRegistro;
    }

    public BigInteger getSucursal() {
        return sucursal;
    }

    public void setSucursal(BigInteger sucursal) {
        this.sucursal = sucursal;
    }

    public BigDecimal getCotizacion1() {
        return cotizacion1;
    }

    public void setCotizacion1(BigDecimal cotizacion1) {
        this.cotizacion1 = cotizacion1;
    }

    public String getComentariocoti1() {
        return comentariocoti1;
    }

    public void setComentariocoti1(String comentariocoti1) {
        this.comentariocoti1 = comentariocoti1;
    }

    public BigDecimal getCotizacion2() {
        return cotizacion2;
    }

    public void setCotizacion2(BigDecimal cotizacion2) {
        this.cotizacion2 = cotizacion2;
    }

    public String getComentariocoti2() {
        return comentariocoti2;
    }

    public void setComentariocoti2(String comentariocoti2) {
        this.comentariocoti2 = comentariocoti2;
    }

    public BigDecimal getCotizacion3() {
        return cotizacion3;
    }

    public void setCotizacion3(BigDecimal cotizacion3) {
        this.cotizacion3 = cotizacion3;
    }

    public String getComentariocoti3() {
        return comentariocoti3;
    }

    public void setComentariocoti3(String comentariocoti3) {
        this.comentariocoti3 = comentariocoti3;
    }

    public BigDecimal getCotizacion4() {
        return cotizacion4;
    }

    public void setCotizacion4(BigDecimal cotizacion4) {
        this.cotizacion4 = cotizacion4;
    }

    public String getComentariocoti4() {
        return comentariocoti4;
    }

    public void setComentariocoti4(String comentariocoti4) {
        this.comentariocoti4 = comentariocoti4;
    }

    public BigDecimal getCotizacion5() {
        return cotizacion5;
    }

    public void setCotizacion5(BigDecimal cotizacion5) {
        this.cotizacion5 = cotizacion5;
    }

    public String getComentariocoti5() {
        return comentariocoti5;
    }

    public void setComentariocoti5(String comentariocoti5) {
        this.comentariocoti5 = comentariocoti5;
    }

    public BigDecimal getNovedad1() {
        return novedad1;
    }

    public void setNovedad1(BigDecimal novedad1) {
        this.novedad1 = novedad1;
    }

    public String getComentarionove1() {
        return comentarionove1;
    }

    public void setComentarionove1(String comentarionove1) {
        this.comentarionove1 = comentarionove1;
    }

    public BigDecimal getNovedad2() {
        return novedad2;
    }

    public void setNovedad2(BigDecimal novedad2) {
        this.novedad2 = novedad2;
    }

    public String getComentarionove2() {
        return comentarionove2;
    }

    public void setComentarionove2(String comentarionove2) {
        this.comentarionove2 = comentarionove2;
    }

    public BigDecimal getNovedad3() {
        return novedad3;
    }

    public void setNovedad3(BigDecimal novedad3) {
        this.novedad3 = novedad3;
    }

    public String getComentarionove3() {
        return comentarionove3;
    }

    public void setComentarionove3(String comentarionove3) {
        this.comentarionove3 = comentarionove3;
    }

    public BigDecimal getNovedad4() {
        return novedad4;
    }

    public void setNovedad4(BigDecimal novedad4) {
        this.novedad4 = novedad4;
    }

    public String getComentarionove4() {
        return comentarionove4;
    }

    public void setComentarionove4(String comentarionove4) {
        this.comentarionove4 = comentarionove4;
    }

    public BigDecimal getNovedad5() {
        return novedad5;
    }

    public void setNovedad5(BigDecimal novedad5) {
        this.novedad5 = novedad5;
    }

    public String getComentarionove5() {
        return comentarionove5;
    }

    public void setComentarionove5(String comentarionove5) {
        this.comentarionove5 = comentarionove5;
    }

    public BigInteger getSalariobasico() {
        return salariobasico;
    }

    public void setSalariobasico(BigInteger salariobasico) {
        this.salariobasico = salariobasico;
    }

    public BigInteger getIbc() {
        return ibc;
    }

    public void setIbc(BigInteger ibc) {
        this.ibc = ibc;
    }

    public BigInteger getIbcreferencia() {
        return ibcreferencia;
    }

    public void setIbcreferencia(BigInteger ibcreferencia) {
        this.ibcreferencia = ibcreferencia;
    }

    public Short getDiascotizados() {
        return diascotizados;
    }

    public void setDiascotizados(Short diascotizados) {
        this.diascotizados = diascotizados;
    }

    public Short getTipoaportante() {
        return tipoaportante;
    }

    public void setTipoaportante(Short tipoaportante) {
        this.tipoaportante = tipoaportante;
    }

    public String getIng() {
        return ing;
    }

    public void setIng(String ing) {
        this.ing = ing;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getTda() {
        return tda;
    }

    public void setTda(String tda) {
        this.tda = tda;
    }

    public String getTaa() {
        return taa;
    }

    public void setTaa(String taa) {
        this.taa = taa;
    }

    public String getVsp() {
        return vsp;
    }

    public void setVsp(String vsp) {
        this.vsp = vsp;
    }

    public String getVte() {
        return vte;
    }

    public void setVte(String vte) {
        this.vte = vte;
    }

    public String getVst() {
        return vst;
    }

    public void setVst(String vst) {
        this.vst = vst;
    }

    public String getSln() {
        return sln;
    }

    public void setSln(String sln) {
        this.sln = sln;
    }

    public String getIge() {
        return ige;
    }

    public void setIge(String ige) {
        this.ige = ige;
    }

    public String getLma() {
        return lma;
    }

    public void setLma(String lma) {
        this.lma = lma;
    }

    public String getVac() {
        return vac;
    }

    public void setVac(String vac) {
        this.vac = vac;
    }

    public String getAvp() {
        return avp;
    }

    public void setAvp(String avp) {
        this.avp = avp;
    }

    public String getVct() {
        return vct;
    }

    public void setVct(String vct) {
        this.vct = vct;
    }

    public String getIrp() {
        return irp;
    }

    public void setIrp(String irp) {
        this.irp = irp;
    }

    public BigInteger getTarifaafp() {
        return tarifaafp;
    }

    public void setTarifaafp(BigInteger tarifaafp) {
        this.tarifaafp = tarifaafp;
    }

    public BigInteger getTarifactt() {
        return tarifactt;
    }

    public void setTarifactt(BigInteger tarifactt) {
        this.tarifactt = tarifactt;
    }

    public String getCodigoctt() {
        return codigoctt;
    }

    public void setCodigoctt(String codigoctt) {
        this.codigoctt = codigoctt;
    }

    public BigInteger getAvpevalor() {
        return avpevalor;
    }

    public void setAvpevalor(BigInteger avpevalor) {
        this.avpevalor = avpevalor;
    }

    public BigInteger getAvppvalor() {
        return avppvalor;
    }

    public void setAvppvalor(BigInteger avppvalor) {
        this.avppvalor = avppvalor;
    }

    public BigInteger getRetcontavpvalor() {
        return retcontavpvalor;
    }

    public void setRetcontavpvalor(BigInteger retcontavpvalor) {
        this.retcontavpvalor = retcontavpvalor;
    }

    public String getCodigoneps() {
        return codigoneps;
    }

    public void setCodigoneps(String codigoneps) {
        this.codigoneps = codigoneps;
    }

    public String getCodigonafp() {
        return codigonafp;
    }

    public void setCodigonafp(String codigonafp) {
        this.codigonafp = codigonafp;
    }

    public BigInteger getEgvalor() {
        return egvalor;
    }

    public void setEgvalor(BigInteger egvalor) {
        this.egvalor = egvalor;
    }

    public String getEgautorizacion() {
        return egautorizacion;
    }

    public void setEgautorizacion(String egautorizacion) {
        this.egautorizacion = egautorizacion;
    }

    public BigInteger getMaternidadvalor() {
        return maternidadvalor;
    }

    public void setMaternidadvalor(BigInteger maternidadvalor) {
        this.maternidadvalor = maternidadvalor;
    }

    public String getMaternidadautorizacion() {
        return maternidadautorizacion;
    }

    public void setMaternidadautorizacion(String maternidadautorizacion) {
        this.maternidadautorizacion = maternidadautorizacion;
    }

    public BigInteger getUpcvalor() {
        return upcvalor;
    }

    public void setUpcvalor(BigInteger upcvalor) {
        this.upcvalor = upcvalor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getModificadomanualmente() {
        return modificadomanualmente;
    }

    public void setModificadomanualmente(String modificadomanualmente) {
        this.modificadomanualmente = modificadomanualmente;
    }

    public BigInteger getTarifaeps() {
        return tarifaeps;
    }

    public void setTarifaeps(BigInteger tarifaeps) {
        this.tarifaeps = tarifaeps;
    }

    public Short getTipopensionado() {
        return tipopensionado;
    }

    public void setTipopensionado(Short tipopensionado) {
        this.tipopensionado = tipopensionado;
    }

    public String getSus() {
        return sus;
    }

    public void setSus(String sus) {
        this.sus = sus;
    }

    public String getPensioncompartida() {
        return pensioncompartida;
    }

    public void setPensioncompartida(String pensioncompartida) {
        this.pensioncompartida = pensioncompartida;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public BigInteger getCotizacion1original() {
        return cotizacion1original;
    }

    public void setCotizacion1original(BigInteger cotizacion1original) {
        this.cotizacion1original = cotizacion1original;
    }

    public BigInteger getCotizacion2original() {
        return cotizacion2original;
    }

    public void setCotizacion2original(BigInteger cotizacion2original) {
        this.cotizacion2original = cotizacion2original;
    }

    public BigInteger getCotizacion3original() {
        return cotizacion3original;
    }

    public void setCotizacion3original(BigInteger cotizacion3original) {
        this.cotizacion3original = cotizacion3original;
    }

    public BigInteger getCotizacion4original() {
        return cotizacion4original;
    }

    public void setCotizacion4original(BigInteger cotizacion4original) {
        this.cotizacion4original = cotizacion4original;
    }

    public BigInteger getCotizacion5original() {
        return cotizacion5original;
    }

    public void setCotizacion5original(BigInteger cotizacion5original) {
        this.cotizacion5original = cotizacion5original;
    }

    public BigInteger getNovedad1original() {
        return novedad1original;
    }

    public void setNovedad1original(BigInteger novedad1original) {
        this.novedad1original = novedad1original;
    }

    public BigInteger getNovedad2original() {
        return novedad2original;
    }

    public void setNovedad2original(BigInteger novedad2original) {
        this.novedad2original = novedad2original;
    }

    public BigInteger getNovedad3original() {
        return novedad3original;
    }

    public void setNovedad3original(BigInteger novedad3original) {
        this.novedad3original = novedad3original;
    }

    public BigInteger getNovedad4original() {
        return novedad4original;
    }

    public void setNovedad4original(BigInteger novedad4original) {
        this.novedad4original = novedad4original;
    }

    public BigInteger getNovedad5original() {
        return novedad5original;
    }

    public void setNovedad5original(BigInteger novedad5original) {
        this.novedad5original = novedad5original;
    }

    public Short getOrigen() {
        return origen;
    }

    public void setOrigen(Short origen) {
        this.origen = origen;
    }

    public Date getFechaorigen() {
        return fechaorigen;
    }

    public void setFechaorigen(Date fechaorigen) {
        this.fechaorigen = fechaorigen;
    }

    public Date getFechaingreso() {
        return fechaingreso;
    }

    public void setFechaingreso(Date fechaingreso) {
        this.fechaingreso = fechaingreso;
    }

    public Date getFechahasta() {
        return fechahasta;
    }

    public void setFechahasta(Date fechahasta) {
        this.fechahasta = fechahasta;
    }

    public BigInteger getSubtipocotizante() {
        return subtipocotizante;
    }

    public void setSubtipocotizante(BigInteger subtipocotizante) {
        this.subtipocotizante = subtipocotizante;
    }

    public String getSubtipocotizanteSTR() {
        getSubtipocotizante();
        if (subtipocotizante == null) {
            subtipocotizanteSTR = " ";
        } else {
            if (subtipocotizante.intValue() == 0) {
                subtipocotizanteSTR = "0. Valor desconocido";
            }
            if (subtipocotizante.intValue() == 1) {
                subtipocotizanteSTR = "1. Dependiente pensionado por vejez activo";
            }
            if (subtipocotizante.intValue() == 2) {
                subtipocotizanteSTR = "2. Independiente pensionado por vejez activo";
            }
            if (subtipocotizante.intValue() == 3) {
                subtipocotizanteSTR = "3. Cotizante no obligado a cotización a pensiones por edad";
            }
            if (subtipocotizante.intValue() == 4) {
                subtipocotizanteSTR = "4. Cotizante con requisitos cumplidos para pensión";
            }
            if (subtipocotizante.intValue() == 5) {
                subtipocotizanteSTR = "5. Cotizante a quien se le ha reconocido indemnización sustitutiva o devolución de saldos";
            }
            if (subtipocotizante.intValue() == 6) {
                subtipocotizanteSTR = "6. Cotizante perteneciente a un régimen exceptuado de pensiones o a entidades autorizadas para recibir aportes exclusivamente de un grupo de sus propios trabajadores";
            }
        }
        return subtipocotizanteSTR;
    }

    public void setSubtipocotizanteSTR(String subtipocotizanteSTR) {
        this.subtipocotizanteSTR = subtipocotizanteSTR;
    }

    public String getExtranjero() {
        return extranjero;
    }

    public void setExtranjero(String extranjero) {
        this.extranjero = extranjero;
    }

    public BigInteger getTarifacaja() {
        return tarifacaja;
    }

    public void setTarifacaja(BigInteger tarifacaja) {
        this.tarifacaja = tarifacaja;
    }

    public BigInteger getTarifasena() {
        return tarifasena;
    }

    public void setTarifasena(BigInteger tarifasena) {
        this.tarifasena = tarifasena;
    }

    public BigInteger getTarifaicbf() {
        return tarifaicbf;
    }

    public void setTarifaicbf(BigInteger tarifaicbf) {
        this.tarifaicbf = tarifaicbf;
    }

    public BigInteger getTarifaesap() {
        return tarifaesap;
    }

    public void setTarifaesap(BigInteger tarifaesap) {
        this.tarifaesap = tarifaesap;
    }

    public BigInteger getTarifamen() {
        return tarifamen;
    }

    public void setTarifamen(BigInteger tarifamen) {
        this.tarifamen = tarifamen;
    }

    public BigInteger getIbcoriginal() {
        return ibcoriginal;
    }

    public void setIbcoriginal(BigInteger ibcoriginal) {
        this.ibcoriginal = ibcoriginal;
    }

    public String getTipoplanilla() {
        return tipoplanilla;
    }

    public void setTipoplanilla(String tipoplanilla) {
        this.tipoplanilla = tipoplanilla;
    }

    public String getIdenticoapagado() {
        return identicoapagado;
    }

    public void setIdenticoapagado(String identicoapagado) {
        this.identicoapagado = identicoapagado;
    }

    public BigInteger getTotalcorreccion() {
        return totalcorreccion;
    }

    public void setTotalcorreccion(BigInteger totalcorreccion) {
        this.totalcorreccion = totalcorreccion;
    }

    public TiposTrabajadores getTipotrabajador() {
        return tipotrabajador;
    }

    public void setTipotrabajador(TiposTrabajadores tipotrabajador) {
        this.tipotrabajador = tipotrabajador;
    }

    public TiposEntidades getTipoentidad() {
        return tipoentidad;
    }

    public void setTipoentidad(TiposEntidades tipoentidad) {
        this.tipoentidad = tipoentidad;
    }

    public Parametrospresupuestos getParametropresupuesto() {
        return parametropresupuesto;
    }

    public void setParametropresupuesto(Parametrospresupuestos parametropresupuesto) {
        this.parametropresupuesto = parametropresupuesto;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public boolean isBing() {
        if ("X".equals(ing)) {
            bing = true;
        } else {
            bing = false;
        }
        return bing;
    }

    public void setBing(boolean bing) {
//        System.out.println("entró a set bing : " + bing);
        this.bing = bing;
        if (bing == true) {
            ing = "X";
        } else {
            ing = null;
        }
    }

    public boolean isBret() {
        if ("X".equals(ret)) {
            bret = true;
        } else {
            bret = false;
        }
        return bret;
    }

    public void setBret(boolean bret) {
        this.bret = bret;
        if (bret == true) {
            ret = "X";
        } else {
            ret = null;
        }
    }

    public boolean isBtda() {
        if ("X".equals(tda)) {
            btda = true;
        } else {
            btda = false;
        }
        return btda;
    }

    public void setBtda(boolean btda) {
        this.btda = btda;
        if (btda == true) {
            tda = "X";
        } else {
            tda = null;
        }
    }

    public boolean isBtaa() {
        if ("X".equals(taa)) {
            btaa = true;
        } else {
            btaa = false;
        }
        return btaa;
    }

    public void setBtaa(boolean btaa) {
        this.btaa = btaa;
        if (btaa == true) {
            taa = "X";
        } else {
            taa = null;
        }
    }

    public boolean isBvsp() {
        if ("X".equals(vsp)) {
            bvsp = true;
        } else {
            bvsp = false;
        }
        return bvsp;
    }

    public void setBvsp(boolean bvsp) {
        this.bvsp = bvsp;
        if (bvsp == true) {
            vsp = "X";
        } else {
            vsp = null;
        }
    }

    public boolean isBvte() {
        if ("X".equals(vte)) {
            bvte = true;
        } else {
            bvte = false;
        }
        return bvte;
    }

    public void setBvte(boolean bvte) {
        this.bvte = bvte;
        if (bvte == true) {
            vte = "X";
        } else {
            vte = null;
        }
    }

    public boolean isBvst() {
        if ("X".equals(vst)) {
            bvst = true;
        } else {
            bvst = false;
        }
        return bvst;
    }

    public void setBvst(boolean bvst) {
        this.bvst = bvst;
        if (bvst == true) {
            vst = "X";
        } else {
            vst = null;
        }
    }

    public boolean isBsln() {
        if ("X".equals(sln)) {
            bsln = true;
        } else {
            bsln = false;
        }
        return bsln;
    }

    public void setBsln(boolean bsln) {
        this.bsln = bsln;
        if (bsln == true) {
            sln = "X";
        } else {
            sln = null;
        }
    }

    public boolean isBige() {
        if ("X".equals(ige)) {
            bige = true;
        } else {
            bige = false;
        }
        return bige;
    }

    public void setBige(boolean bige) {
        this.bige = bige;
        if (bige == true) {
            ige = "X";
        } else {
            ige = null;
        }
    }

    public boolean isBlma() {
        if ("X".equals(lma)) {
            blma = true;
        } else {
            blma = false;
        }
        return blma;
    }

    public void setBlma(boolean blma) {
        this.blma = blma;
        if (blma == true) {
            lma = "X";
        } else {
            lma = null;
        }
    }

    public boolean isBvac() {
        if ("X".equals(vac)) {
            bvac = true;
        } else {
            bvac = false;
        }
        return bvac;
    }

    public void setBvac(boolean bvac) {
        this.bvac = bvac;
        if (bvac == true) {
            vac = "X";
        } else {
            vac = null;
        }
    }

    public boolean isBavp() {
        if ("X".equals(avp)) {
            bavp = true;
        } else {
            bavp = false;
        }
        return bavp;
    }

    public void setBavp(boolean bavp) {
        this.bavp = bavp;
        if (bavp) {
            this.avp = "X";
        } else {
            this.avp = null;
        }
    }

    public boolean isBvct() {
        if ("X".equals(vct)) {
            bvct = true;
        } else {
            bvct = false;
        }
        return bvct;
    }

    public void setBvct(boolean bvct) {
        this.bvct = bvct;
        if (bvct == true) {
            vct = "X";
        } else {
            vct = null;
        }
    }

    public boolean isBirp() {
        if ("X".equals(irp)) {
            birp = true;
        } else {
            birp = false;
        }
        return birp;
    }

    public void setBirp(boolean birp) {
        this.birp = birp;
        if (birp == true) {
            irp = "X";
        } else {
            irp = null;
        }
    }

    public boolean isBsus() {
        if ("X".equals(sus)) {
            bsus = true;
        } else {
            bsus = false;
        }
        return bsus;
    }

    public void setBsus(boolean bsus) {
        this.bsus = bsus;
        if (bsus == true) {
            sus = "X";
        } else {
            sus = null;
        }
    }

    public boolean isBinte() {
        if ("X".equals(integral)) {
            binte = true;
        } else {
            binte = false;
        }
        return binte;
    }

    public void setBinte(boolean binte) {
        this.binte = binte;
        if (binte == true) {
            integral = "X";
        } else {
            integral = null;
        }
    }

    public String getNombretercero() {
        return nombretercero;
    }

    public void setNombretercero(String nombretercero) {
        this.nombretercero = nombretercero;
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
        if (!(object instanceof AportesEntidades)) {
            return false;
        }
        AportesEntidades other = (AportesEntidades) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.AportesEntidades[ secuencia=" + secuencia + " ]";
    }

}
