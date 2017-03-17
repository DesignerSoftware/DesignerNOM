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
@Table(name = "APORTESENTIDADESXDIA")
public class AportesEntidadesXDia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @NotNull
    @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empresas empresa;
    @NotNull
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empleados empleado;
    @NotNull
    @Column(name = "ANO")
    private short ano;
    @NotNull
    @Column(name = "MES")
    private short mes;
    @Column(name = "SEGMENTO")
    @NotNull
    private BigInteger segmento;
    @Column(name = "NOVEDAD")
    @NotNull
    @Size(max = 20)
    private String novedad;
    @Column(name = "DIA")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date dia;
    @Column(name = "IBCPENSION")
    private BigDecimal ibcpension;
    @Column(name = "IBCSALUD")
    private BigDecimal ibcsalud;
    @Column(name = "IBCRIESGO")
    private BigDecimal ibcriesgo;
    @Column(name = "IBCCAJA")
    private BigDecimal ibccaja;
    @Column(name = "IBCICBF")
    private BigDecimal ibcicbf;
    @Column(name = "IBCSENA")
    private BigDecimal ibcsena;
    @Column(name = "APORTEPENSION")
    private BigDecimal aportepension;
    @Column(name = "APORTESALUD")
    private BigDecimal aportesalud;
    @Column(name = "APORTERIESGO")
    private BigDecimal aporteriesgo;
    @Column(name = "APORTECAJA")
    private BigDecimal aportecaja;
    @Column(name = "APORTEICBF")
    private BigDecimal aporteicbf;
    @Column(name = "APORTESENA")
    private BigDecimal aportesena;
    @Column(name = "SOLIDARIDAD")
    private BigDecimal solidaridad;
    @Column(name = "SUBSISTENCIA")
    private BigDecimal subsistencia;
    @Column(name = "VOLUNTARIOEMPL")
    private BigDecimal voluntarioempl;
    @Column(name = "VOLUNTARIOPATR")
    private BigDecimal voluntariopatr;
    @Column(name = "RETENCIONCONTI")
    private BigDecimal retencionconti;
    @Column(name = "TARIFAPENSION")
    private BigDecimal tarifapension;
    @Column(name = "TARIFASALUD")
    private BigDecimal tarifasalud;
    @Column(name = "TARIFARIESGO")
    private BigDecimal tarifariesgo;
    @Column(name = "TARIFACAJA")
    private BigDecimal tarifacaja;
    @Column(name = "TARIFAICBF")
    private BigDecimal tarifaicbf;
    @Column(name = "TARIFASENA")
    private BigDecimal tarifasena;
    @Column(name = "IBCPENSIONREAL")
    private BigDecimal ibcpensionreal;
    @Column(name = "IBCSALUDREAL")
    private BigDecimal ibcsaludreal;
    @Column(name = "IBCRIESGOREAL")
    private BigDecimal ibcriesgoreal;
    @Column(name = "IBCCAJAREAL")
    private BigDecimal ibccajareal;
    @Column(name = "IBCICBFREAL")
    private BigDecimal ibcicbfreal;
    @Column(name = "IBCSENAREAL")
    private BigDecimal ibcsenareal;
    @Column(name = "APORTEPENSIONREAL")
    private BigDecimal aportepensionreal;
    @Column(name = "APORTESALUDREAL")
    private BigDecimal aportesaludreal;
    @Column(name = "APORTERIESGOREAL")
    private BigDecimal aporteriesgoreal;
    @Column(name = "APORTECAJAREAL")
    private BigDecimal aportecajareal;
    @Column(name = "APORTEICBFREAL")
    private BigDecimal aporteicbfreal;
    @Column(name = "APORTESENAREAL")
    private BigDecimal aportesenareal;
    @Column(name = "IBCPENSIONSOBRANTE")
    private BigDecimal ibcpensionsobrante;
    @Column(name = "IBCSALUDSOBRANTE")
    private BigDecimal ibcsaludsobrante;
    @Column(name = "IBCRIESGOSOBRANTE")
    private BigDecimal ibcriesgosobrante;
    @Column(name = "IBCCAJASOBRANTE")
    private BigDecimal ibccajasobrante;
    @Column(name = "IBCICBFSOBRANTE")
    private BigDecimal ibcicbfsobrante;
    @Column(name = "IBCSENASOBRANTE")
    private BigDecimal ibcsenasobrante;
    @Column(name = "APORTEPENSIONSOBRANTE")
    private BigDecimal aportepensionsobrante;
    @Column(name = "APORTESALUDSOBRANTE")
    private BigDecimal aportesaludsobrante;
    @Column(name = "APORTERIESGOSOBRANTE")
    private BigDecimal aporteriesgosobrante;
    @Column(name = "APORTECAJASOBRANTE")
    private BigDecimal aportecajasobrante;
    @Column(name = "APORTEICBFSOBRANTE")
    private BigDecimal aporteicbfsobrante;
    @Column(name = "APORTESENASOBRANTE")
    private BigDecimal aportesenasobrante;
    @Column(name = "DIASPENSION")
    private BigInteger diaspension;
    @Column(name = "DIASSALUD")
    private BigInteger diassalud;
    @Column(name = "DIASRIESGO")
    private BigInteger diasriesgo;
    @Column(name = "DIASCAJA")
    private BigInteger diascaja;
    @Column(name = "DIASICBF")
    private BigInteger diasicbf;
    @Column(name = "DIASSENA")
    private BigInteger diassena;
    @Column(name = "TARIFAPENSIONSOBRANTE")
    private BigDecimal tarifapensionsobrante;
    @Column(name = "TARIFASALUDSOBRANTE")
    private BigDecimal tarifasaludsobrante;
    @Column(name = "TARIFARIESGOSOBRANTE")
    private BigDecimal tarifariesgosobrante;
    @Column(name = "TARIFACAJASOBRANTE")
    private BigDecimal tarifacajasobrante;
    @Column(name = "TARIFAICBFSOBRANTE")
    private BigDecimal tarifaicbfsobrante;
    @Column(name = "TARIFASENASOBRANTE")
    private BigDecimal tarifasenasobrante;
    @Column(name = "SALARIO")
    private BigInteger salario;
    @Size(max = 1)
    @Column(name = "VCT")
    private String vct;
    @Column(name = "CORTEPROCESO")
    private BigInteger corteproceso;
    @Column(name = "AJUSTEAPORTEPENSION")
    private BigDecimal ajusteaportepension;
    @Column(name = "AJUSTEAPORTESALUD")
    private BigDecimal ajusteaportesalud;
    @Column(name = "AJUSTEAPORTERIESGO")
    private BigDecimal ajusteaporteriesgo;
    @Column(name = "AJUSTEAPORTECAJA")
    private BigDecimal ajusteaportecaja;
    @Column(name = "AJUSTEAPORTEICBF")
    private BigDecimal ajusteaporteicbf;
    @Column(name = "AJUSTEAPORTESENA")
    private BigDecimal ajusteaportesena;
    @Transient
    private BigDecimal tarifaaux;
    @Transient
    private BigDecimal aporteaux;
    @Transient
    private BigDecimal tarifa2aux;
    @Transient
    private BigDecimal ibcaux;
    @Transient
    private BigDecimal ibcespaux;
    @Transient
    private BigDecimal aporteentaux;

    public AportesEntidadesXDia() {
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
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

    public BigInteger getSegmento() {
        return segmento;
    }

    public void setSegmento(BigInteger segmento) {
        this.segmento = segmento;
    }

    public String getNovedad() {
        return novedad;
    }

    public void setNovedad(String novedad) {
        this.novedad = novedad;
    }

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    public BigDecimal getIbcpension() {
        if (ibcpension == null) {
            ibcpension = BigDecimal.ZERO;
        }
        return ibcpension;
    }

    public void setIbcpension(BigDecimal ibcpension) {
        this.ibcpension = ibcpension;
    }

    public BigDecimal getIbcsalud() {
        if (ibcsalud == null) {
            ibcsalud = BigDecimal.ZERO;
        }
        return ibcsalud;
    }

    public void setIbcsalud(BigDecimal ibcsalud) {
        this.ibcsalud = ibcsalud;
    }

    public BigDecimal getIbcriesgo() {
        if (ibcriesgo == null) {
            ibcriesgo = BigDecimal.ZERO;
        }
        return ibcriesgo;
    }

    public void setIbcriesgo(BigDecimal ibcriesgo) {
        this.ibcriesgo = ibcriesgo;
    }

    public BigDecimal getIbccaja() {
        if (ibccaja == null) {
            ibccaja = BigDecimal.ZERO;
        }
        return ibccaja;
    }

    public void setIbccaja(BigDecimal ibccaja) {
        this.ibccaja = ibccaja;
    }

    public BigDecimal getIbcicbf() {
        if (ibcicbf == null) {
            ibcicbf = BigDecimal.ZERO;
        }
        return ibcicbf;
    }

    public void setIbcicbf(BigDecimal ibcicbf) {
        this.ibcicbf = ibcicbf;
    }

    public BigDecimal getIbcsena() {
        if (ibcsena == null) {
            ibcsena = BigDecimal.ZERO;
        }
        return ibcsena;
    }

    public void setIbcsena(BigDecimal ibcsena) {
        this.ibcsena = ibcsena;
    }

    public BigDecimal getAportepension() {
        if (aportepension == null) {
            aportepension = BigDecimal.ZERO;
        }
        return aportepension;
    }

    public void setAportepension(BigDecimal aportepension) {
        this.aportepension = aportepension;
    }

    public BigDecimal getAportesalud() {
        if (aportesalud == null) {
            aportesalud = BigDecimal.ZERO;
        }
        return aportesalud;
    }

    public void setAportesalud(BigDecimal aportesalud) {
        this.aportesalud = aportesalud;
    }

    public BigDecimal getAporteriesgo() {
        if (aporteriesgo == null) {
            aporteriesgo = BigDecimal.ZERO;
        }
        return aporteriesgo;
    }

    public void setAporteriesgo(BigDecimal aporteriesgo) {
        this.aporteriesgo = aporteriesgo;
    }

    public BigDecimal getAportecaja() {
        if (aportecaja == null) {
            aportecaja = BigDecimal.ZERO;
        }
        return aportecaja;
    }

    public void setAportecaja(BigDecimal aportecaja) {
        this.aportecaja = aportecaja;
    }

    public BigDecimal getAporteicbf() {
        if (aporteicbf == null) {
            aporteicbf = BigDecimal.ZERO;
        }
        return aporteicbf;
    }

    public void setAporteicbf(BigDecimal aporteicbf) {
        this.aporteicbf = aporteicbf;
    }

    public BigDecimal getAportesena() {
        if (aportesena == null) {
            aportesena = BigDecimal.ZERO;
        }
        return aportesena;
    }

    public void setAportesena(BigDecimal aportesena) {
        this.aportesena = aportesena;
    }

    public BigDecimal getSolidaridad() {
        if (solidaridad == null) {
            solidaridad = BigDecimal.ZERO;
        }
        return solidaridad;
    }

    public void setSolidaridad(BigDecimal solidaridad) {
        this.solidaridad = solidaridad;
    }

    public BigDecimal getSubsistencia() {
        if (subsistencia == null) {
            subsistencia = BigDecimal.ZERO;
        }
        return subsistencia;
    }

    public void setSubsistencia(BigDecimal subsistencia) {
        this.subsistencia = subsistencia;
    }

    public BigDecimal getVoluntarioempl() {
        if (voluntarioempl == null) {
            voluntarioempl = BigDecimal.ZERO;
        }
        return voluntarioempl;
    }

    public void setVoluntarioempl(BigDecimal voluntarioempl) {
        this.voluntarioempl = voluntarioempl;
    }

    public BigDecimal getVoluntariopatr() {
        return voluntariopatr;
    }

    public void setVoluntariopatr(BigDecimal voluntariopatr) {
        this.voluntariopatr = voluntariopatr;
    }

    public BigDecimal getRetencionconti() {
        return retencionconti;
    }

    public void setRetencionconti(BigDecimal retencionconti) {
        this.retencionconti = retencionconti;
    }

    public BigDecimal getTarifapension() {
        if (tarifapension == null) {
            tarifapension = BigDecimal.ZERO;
        }
        return tarifapension;
    }

    public void setTarifapension(BigDecimal tarifapension) {
        this.tarifapension = tarifapension;
    }

    public BigDecimal getTarifasalud() {
        if (tarifasalud == null) {
            tarifasalud = BigDecimal.ZERO;
        }
        return tarifasalud;
    }

    public void setTarifasalud(BigDecimal tarifasalud) {
        this.tarifasalud = tarifasalud;
    }

    public BigDecimal getTarifariesgo() {
        if (tarifariesgo == null) {
            tarifariesgo = BigDecimal.ZERO;
        }
        return tarifariesgo;
    }

    public void setTarifariesgo(BigDecimal tarifariesgo) {
        this.tarifariesgo = tarifariesgo;
    }

    public BigDecimal getTarifacaja() {
        if (tarifacaja == null) {
            tarifacaja = BigDecimal.ZERO;
        }
        return tarifacaja;
    }

    public void setTarifacaja(BigDecimal tarifacaja) {
        this.tarifacaja = tarifacaja;
    }

    public BigDecimal getTarifaicbf() {
        if (tarifaicbf == null) {
            tarifaicbf = BigDecimal.ZERO;
        }
        return tarifaicbf;
    }

    public void setTarifaicbf(BigDecimal tarifaicbf) {
        this.tarifaicbf = tarifaicbf;
    }

    public BigDecimal getTarifasena() {
        if (tarifasena == null) {
            tarifasena = BigDecimal.ZERO;
        }
        return tarifasena;
    }

    public void setTarifasena(BigDecimal tarifasena) {
        this.tarifasena = tarifasena;
    }

    public BigDecimal getIbcpensionreal() {
        if (ibcpensionreal == null) {
            ibcpensionreal = BigDecimal.ZERO;
        }
        return ibcpensionreal;
    }

    public void setIbcpensionreal(BigDecimal ibcpensionreal) {
        this.ibcpensionreal = ibcpensionreal;
    }

    public BigDecimal getIbcsaludreal() {
        if (ibcsaludreal == null) {
            ibcsaludreal = BigDecimal.ZERO;
        }
        return ibcsaludreal;
    }

    public void setIbcsaludreal(BigDecimal ibcsaludreal) {
        this.ibcsaludreal = ibcsaludreal;
    }

    public BigDecimal getIbcriesgoreal() {
        if (ibcriesgoreal == null) {
            ibcriesgoreal = BigDecimal.ZERO;
        }
        return ibcriesgoreal;
    }

    public void setIbcriesgoreal(BigDecimal ibcriesgoreal) {
        this.ibcriesgoreal = ibcriesgoreal;
    }

    public BigDecimal getIbccajareal() {
        if (ibccajareal == null) {
            ibccajareal = BigDecimal.ZERO;
        }
        return ibccajareal;
    }

    public void setIbccajareal(BigDecimal ibccajareal) {
        this.ibccajareal = ibccajareal;
    }

    public BigDecimal getIbcicbfreal() {
        if (ibcicbfreal == null) {
            ibcicbfreal = BigDecimal.ZERO;
        }
        return ibcicbfreal;
    }

    public void setIbcicbfreal(BigDecimal ibcicbfreal) {
        this.ibcicbfreal = ibcicbfreal;
    }

    public BigDecimal getIbcsenareal() {
        if (ibcsenareal == null) {
            ibcsenareal = BigDecimal.ZERO;
        }
        return ibcsenareal;
    }

    public void setIbcsenareal(BigDecimal ibcsenareal) {

        this.ibcsenareal = ibcsenareal;
    }

    public BigDecimal getAportepensionreal() {
        if (aportepensionreal == null) {
            aportepensionreal = BigDecimal.ZERO;
        }
        return aportepensionreal;
    }

    public void setAportepensionreal(BigDecimal aportepensionreal) {
        this.aportepensionreal = aportepensionreal;
    }

    public BigDecimal getAportesaludreal() {
        if (aportesaludreal == null) {
            aportesaludreal = BigDecimal.ZERO;
        }
        return aportesaludreal;
    }

    public void setAportesaludreal(BigDecimal aportesaludreal) {
        this.aportesaludreal = aportesaludreal;
    }

    public BigDecimal getAporteriesgoreal() {
        if (aporteriesgoreal == null) {
            aporteriesgoreal = BigDecimal.ZERO;
        }
        return aporteriesgoreal;
    }

    public void setAporteriesgoreal(BigDecimal aporteriesgoreal) {
        this.aporteriesgoreal = aporteriesgoreal;
    }

    public BigDecimal getAportecajareal() {
        if (aportecajareal == null) {
            aportecajareal = BigDecimal.ZERO;
        }
        return aportecajareal;
    }

    public void setAportecajareal(BigDecimal aportecajareal) {
        this.aportecajareal = aportecajareal;
    }

    public BigDecimal getAporteicbfreal() {
        if (aporteicbfreal == null) {
            aporteicbfreal = BigDecimal.ZERO;
        }
        return aporteicbfreal;
    }

    public void setAporteicbfreal(BigDecimal aporteicbfreal) {
        this.aporteicbfreal = aporteicbfreal;
    }

    public BigDecimal getAportesenareal() {
        if (aportesenareal == null) {
            aportesenareal = BigDecimal.ZERO;
        }
        return aportesenareal;
    }

    public void setAportesenareal(BigDecimal aportesenareal) {
        this.aportesenareal = aportesenareal;
    }

    public BigDecimal getIbcpensionsobrante() {
        if (ibcpensionsobrante == null) {
            ibcpensionsobrante = BigDecimal.ZERO;
        }
        return ibcpensionsobrante;
    }

    public void setIbcpensionsobrante(BigDecimal ibcpensionsobrante) {
        this.ibcpensionsobrante = ibcpensionsobrante;
    }

    public BigDecimal getIbcsaludsobrante() {
        if (ibcsaludsobrante == null) {
            ibcsaludsobrante = BigDecimal.ZERO;
        }
        return ibcsaludsobrante;
    }

    public void setIbcsaludsobrante(BigDecimal ibcsaludsobrante) {
        this.ibcsaludsobrante = ibcsaludsobrante;
    }

    public BigDecimal getIbcriesgosobrante() {
        if (ibcriesgosobrante == null) {
            ibcriesgosobrante = BigDecimal.ZERO;
        }
        return ibcriesgosobrante;
    }

    public void setIbcriesgosobrante(BigDecimal ibcriesgosobrante) {
        this.ibcriesgosobrante = ibcriesgosobrante;
    }

    public BigDecimal getIbccajasobrante() {
        if (ibccajasobrante == null) {
            ibccajasobrante = BigDecimal.ZERO;
        }
        return ibccajasobrante;
    }

    public void setIbccajasobrante(BigDecimal ibccajasobrante) {
        this.ibccajasobrante = ibccajasobrante;
    }

    public BigDecimal getIbcicbfsobrante() {
        if (ibcicbfsobrante == null) {
            ibcicbfsobrante = BigDecimal.ZERO;
        }
        return ibcicbfsobrante;
    }

    public void setIbcicbfsobrante(BigDecimal ibcicbfsobrante) {
        this.ibcicbfsobrante = ibcicbfsobrante;
    }

    public BigDecimal getIbcsenasobrante() {
        if (ibcsenasobrante == null) {
            ibcsenasobrante = BigDecimal.ZERO;
        }
        return ibcsenasobrante;
    }

    public void setIbcsenasobrante(BigDecimal ibcsenasobrante) {
        this.ibcsenasobrante = ibcsenasobrante;
    }

    public BigDecimal getAportepensionsobrante() {
        if (aportepensionsobrante == null) {
            aportepensionsobrante = BigDecimal.ZERO;
        }
        return aportepensionsobrante;
    }

    public void setAportepensionsobrante(BigDecimal aportepensionsobrante) {
        this.aportepensionsobrante = aportepensionsobrante;
    }

    public BigDecimal getAportesaludsobrante() {
        if (aportesaludsobrante == null) {
            aportesaludsobrante = BigDecimal.ZERO;
        }
        return aportesaludsobrante;
    }

    public void setAportesaludsobrante(BigDecimal aportesaludsobrante) {
        this.aportesaludsobrante = aportesaludsobrante;
    }

    public BigDecimal getAporteriesgosobrante() {
        if (aporteriesgosobrante == null) {
            aporteriesgosobrante = BigDecimal.ZERO;
        }
        return aporteriesgosobrante;
    }

    public void setAporteriesgosobrante(BigDecimal aporteriesgosobrante) {
        this.aporteriesgosobrante = aporteriesgosobrante;
    }

    public BigDecimal getAportecajasobrante() {
        if (aportecajasobrante == null) {
            aportecajasobrante = BigDecimal.ZERO;
        }
        return aportecajasobrante;
    }

    public void setAportecajasobrante(BigDecimal aportecajasobrante) {
        this.aportecajasobrante = aportecajasobrante;
    }

    public BigDecimal getAporteicbfsobrante() {
        if (aporteicbfsobrante == null) {
            aporteicbfsobrante = BigDecimal.ZERO;
        }
        return aporteicbfsobrante;
    }

    public void setAporteicbfsobrante(BigDecimal aporteicbfsobrante) {
        this.aporteicbfsobrante = aporteicbfsobrante;
    }

    public BigDecimal getAportesenasobrante() {
        if (aportesenasobrante == null) {
            aportesenasobrante = BigDecimal.ZERO;
        }
        return aportesenasobrante;
    }

    public void setAportesenasobrante(BigDecimal aportesenasobrante) {
        this.aportesenasobrante = aportesenasobrante;
    }

    public BigInteger getDiaspension() {
        return diaspension;
    }

    public void setDiaspension(BigInteger diaspension) {
        this.diaspension = diaspension;
    }

    public BigInteger getDiassalud() {
        return diassalud;
    }

    public void setDiassalud(BigInteger diassalud) {
        this.diassalud = diassalud;
    }

    public BigInteger getDiasriesgo() {
        return diasriesgo;
    }

    public void setDiasriesgo(BigInteger diasriesgo) {
        this.diasriesgo = diasriesgo;
    }

    public BigInteger getDiascaja() {
        return diascaja;
    }

    public void setDiascaja(BigInteger diascaja) {
        this.diascaja = diascaja;
    }

    public BigInteger getDiasicbf() {
        return diasicbf;
    }

    public void setDiasicbf(BigInteger diasicbf) {
        this.diasicbf = diasicbf;
    }

    public BigInteger getDiassena() {
        return diassena;
    }

    public void setDiassena(BigInteger diassena) {
        this.diassena = diassena;
    }

    public BigDecimal getTarifapensionsobrante() {
        if (tarifapensionsobrante == null) {
            tarifapensionsobrante = BigDecimal.ZERO;
        }
        return tarifapensionsobrante;
    }

    public void setTarifapensionsobrante(BigDecimal tarifapensionsobrante) {
        this.tarifapensionsobrante = tarifapensionsobrante;
    }

    public BigDecimal getTarifasaludsobrante() {
        if (tarifasaludsobrante == null) {
            tarifasaludsobrante = BigDecimal.ZERO;
        }
        return tarifasaludsobrante;
    }

    public void setTarifasaludsobrante(BigDecimal tarifasaludsobrante) {
        this.tarifasaludsobrante = tarifasaludsobrante;
    }

    public BigDecimal getTarifariesgosobrante() {
        if (tarifariesgosobrante == null) {
            tarifariesgosobrante = BigDecimal.ZERO;
        }
        return tarifariesgosobrante;
    }

    public void setTarifariesgosobrante(BigDecimal tarifariesgosobrante) {
        this.tarifariesgosobrante = tarifariesgosobrante;
    }

    public BigDecimal getTarifacajasobrante() {
        if (tarifacajasobrante == null) {
            tarifacajasobrante = BigDecimal.ZERO;
        }
        return tarifacajasobrante;
    }

    public void setTarifacajasobrante(BigDecimal tarifacajasobrante) {
        this.tarifacajasobrante = tarifacajasobrante;
    }

    public BigDecimal getTarifaicbfsobrante() {
        if (tarifaicbfsobrante == null) {
            tarifaicbfsobrante = BigDecimal.ZERO;
        }
        return tarifaicbfsobrante;
    }

    public void setTarifaicbfsobrante(BigDecimal tarifaicbfsobrante) {
        this.tarifaicbfsobrante = tarifaicbfsobrante;
    }

    public BigDecimal getTarifasenasobrante() {
        if (tarifasenasobrante == null) {
            tarifasenasobrante = BigDecimal.ZERO;
        }
        return tarifasenasobrante;
    }

    public void setTarifasenasobrante(BigDecimal tarifasenasobrante) {
        this.tarifasenasobrante = tarifasenasobrante;
    }

    public BigInteger getSalario() {
        if (salario == null) {
            salario = BigInteger.ZERO;
        }
        return salario;
    }

    public void setSalario(BigInteger salario) {
        this.salario = salario;
    }

    public String getVct() {
        return vct;
    }

    public void setVct(String vct) {
        this.vct = vct;
    }

    public BigInteger getCorteproceso() {
        return corteproceso;
    }

    public void setCorteproceso(BigInteger corteproceso) {
        this.corteproceso = corteproceso;
    }

    public BigDecimal getAjusteaportepension() {
        if (ajusteaportepension == null) {
            ajusteaportepension = BigDecimal.ZERO;
        }
        return ajusteaportepension;
    }

    public void setAjusteaportepension(BigDecimal ajusteaportepension) {
        this.ajusteaportepension = ajusteaportepension;
    }

    public BigDecimal getAjusteaportesalud() {
        if (ajusteaportesalud == null) {
            ajusteaportesalud = BigDecimal.ZERO;
        }
        return ajusteaportesalud;
    }

    public void setAjusteaportesalud(BigDecimal ajusteaportesalud) {
        this.ajusteaportesalud = ajusteaportesalud;
    }

    public BigDecimal getAjusteaporteriesgo() {
        if (ajusteaporteriesgo == null) {
            ajusteaporteriesgo = BigDecimal.ZERO;
        }
        return ajusteaporteriesgo;
    }

    public void setAjusteaporteriesgo(BigDecimal ajusteaporteriesgo) {
        this.ajusteaporteriesgo = ajusteaporteriesgo;
    }

    public BigDecimal getAjusteaportecaja() {
        if (ajusteaportecaja == null) {
            ajusteaportecaja = BigDecimal.ZERO;
        }
        return ajusteaportecaja;
    }

    public void setAjusteaportecaja(BigDecimal ajusteaportecaja) {
        this.ajusteaportecaja = ajusteaportecaja;
    }

    public BigDecimal getAjusteaporteicbf() {
        if (ajusteaporteicbf == null) {
            ajusteaporteicbf = BigDecimal.ZERO;
        }
        return ajusteaporteicbf;
    }

    public void setAjusteaporteicbf(BigDecimal ajusteaporteicbf) {
        this.ajusteaporteicbf = ajusteaporteicbf;
    }

    public BigDecimal getAjusteaportesena() {
        if (ajusteaportesena == null) {
            ajusteaportesena = BigDecimal.ZERO;
        }
        return ajusteaportesena;
    }

    public void setAjusteaportesena(BigDecimal ajusteaportesena) {
        this.ajusteaportesena = ajusteaportesena;
    }

    public BigDecimal getTarifaaux() {
        if (tarifaaux == null) {
            tarifaaux = BigDecimal.ZERO;
        }
        return tarifaaux;
    }

    public void setTarifaaux(BigDecimal tarifaaux) {
        this.tarifaaux = tarifaaux;
    }

    public BigDecimal getAporteaux() {
        if (aporteaux == null) {
            aporteaux = BigDecimal.ZERO;
        }
        return aporteaux;
    }

    public void setAporteaux(BigDecimal aporteaux) {
        this.aporteaux = aporteaux;
    }

    public BigDecimal getTarifa2aux() {
        if (tarifa2aux == null) {
            tarifa2aux = BigDecimal.ZERO;
        }
        return tarifa2aux;
    }

    public void setTarifa2aux(BigDecimal tarifa2aux) {
        this.tarifa2aux = tarifa2aux;
    }

    public BigDecimal getIbcaux() {
        if (ibcaux == null) {
            ibcaux = BigDecimal.ZERO;
        }
        return ibcaux;
    }

    public void setIbcaux(BigDecimal ibcaux) {
        this.ibcaux = ibcaux;
    }

    public BigDecimal getAporteentaux() {
        if (aporteentaux == null) {
            aporteentaux = BigDecimal.ZERO;
        }
        return aporteentaux;
    }

    public void setAporteentaux(BigDecimal aporteentaux) {
        this.aporteentaux = aporteentaux;
    }

    public BigDecimal getIbcespaux() {
        if (ibcespaux == null) {
            ibcespaux = BigDecimal.ZERO;
        }
        return ibcespaux;
    }

    public void setIbcespaux(BigDecimal ibcespaux) {
        this.ibcespaux = ibcespaux;
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
        if (!(object instanceof AportesEntidadesXDia)) {
            return false;
        }
        AportesEntidadesXDia other = (AportesEntidadesXDia) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.AportesEntidadesXDia[ secuencia=" + secuencia + " ]";
    }

}
