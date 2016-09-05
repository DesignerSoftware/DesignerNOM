/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "NOVEDADESCORRECCIONESAUTOL")
public class NovedadesCorreccionesAutoLiquidaciones implements Serializable {

   private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ANO")
    private BigInteger anio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MES")
    private BigInteger mes;
    @NotNull
    @JoinColumn(name = "TIPOENTIDAD", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private TiposEntidades tipoentidad;
    @Size(max = 1)
    @Column(name = "CORRECCION")
    private String correccion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "DESTINO")
    private String destino;
    @Size(max = 6)
    @Column(name = "ANOMESCORRECCION")
    private String aniomes;
    @Size(min = 1, max = 50)
    @Column(name = "PLANILLACORREGIR")
    private String planillacorregir;
    @Size(min = 1, max = 50)
    @Column(name = "NUMERORADICACION")
    private String numeroradicacion;
    @Column(name = "VALORINTERESMORA")
    private BigInteger valorinteresesmora;
    @Size(min = 1, max = 50)
    @Column(name = "RADICACIONDESCUENTO")
    private String radicaciondescuento;
    @JoinColumn(name = "TERCERO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Terceros tercero;
    @Column(name = "SALDOAFAVOR")
    private BigInteger saldoafavor;
    @Column(name = "DIASMORA")
    private BigInteger diasmora;
    @Column(name = "VALORMORAUPC")
    private BigInteger valormoraupc;
    @Column(name = "VALORMORASOLIDARIDAD")
    private BigInteger valormorasolidaridad;
    @Column(name = "VALORMORASUBSISTENCIA")
    private BigInteger valormorasubsistencia;
    @Column(name = "SALDOAFAVORUPC")
    private BigInteger saldoafavorupc;
    @Column(name = "FORMULARIOUNICO")
    private BigInteger formulariounico;
    @Column(name = "VALOROTROS")
    private BigInteger valorotros;
    @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empresas empresa;
    @JoinColumn(name = "SUCURSAL_PILA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private SucursalesPila sucursalpila;

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getAnio() {
        return anio;
    }

    public void setAnio(BigInteger anio) {
        this.anio = anio;
    }

    public BigInteger getMes() {
        return mes;
    }

    public void setMes(BigInteger mes) {
        this.mes = mes;
    }

    public TiposEntidades getTipoentidad() {
        return tipoentidad;
    }

    public void setTipoentidad(TiposEntidades tipoentidad) {
        this.tipoentidad = tipoentidad;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getCorreccion() {
        return correccion;
    }

    public void setCorreccion(String correccion) {
        this.correccion = correccion;
    }

    public String getAniomes() {
        return aniomes;
    }

    public void setAniomes(String aniomes) {
        this.aniomes = aniomes;
    }

    public String getPlanillacorregir() {
        return planillacorregir;
    }

    public void setPlanillacorregir(String planillacorregir) {
        this.planillacorregir = planillacorregir;
    }

    public String getNumeroradicacion() {
        return numeroradicacion;
    }

    public void setNumeroradicacion(String numeroradicacion) {
        this.numeroradicacion = numeroradicacion;
    }

    public BigInteger getValorinteresesmora() {
        return valorinteresesmora;
    }

    public void setValorinteresesmora(BigInteger valorinteresesmora) {
        this.valorinteresesmora = valorinteresesmora;
    }

    public String getRadicaciondescuento() {
        return radicaciondescuento;
    }

    public void setRadicaciondescuento(String radicaciondescuento) {
        this.radicaciondescuento = radicaciondescuento;
    }

    public Terceros getTercero() {
        return tercero;
    }

    public void setTercero(Terceros tercero) {
        this.tercero = tercero;
    }

    public BigInteger getSaldoafavor() {
        return saldoafavor;
    }

    public void setSaldoafavor(BigInteger saldoafavor) {
        this.saldoafavor = saldoafavor;
    }

    public BigInteger getDiasmora() {
        return diasmora;
    }

    public void setDiasmora(BigInteger diasmora) {
        this.diasmora = diasmora;
    }

    public BigInteger getValormoraupc() {
        return valormoraupc;
    }

    public void setValormoraupc(BigInteger valormoraupc) {
        this.valormoraupc = valormoraupc;
    }

    public BigInteger getValormorasubsistencia() {
        return valormorasubsistencia;
    }

    public void setValormorasubsistencia(BigInteger valormorasubsistencia) {
        this.valormorasubsistencia = valormorasubsistencia;
    }

    public BigInteger getValormorasolidaridad() {
        return valormorasolidaridad;
    }

    public void setValormorasolidaridad(BigInteger valormorasolidaridad) {
        this.valormorasolidaridad = valormorasolidaridad;
    }

    public BigInteger getSaldoafavorupc() {
        return saldoafavorupc;
    }

    public void setSaldoafavorupc(BigInteger saldoafavorupc) {
        this.saldoafavorupc = saldoafavorupc;
    }

    public BigInteger getFormulariounico() {
        return formulariounico;
    }

    public void setFormulariounico(BigInteger formulariounico) {
        this.formulariounico = formulariounico;
    }

    public BigInteger getValorotros() {
        return valorotros;
    }

    public void setValorotros(BigInteger valorotros) {
        this.valorotros = valorotros;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public SucursalesPila getSucursalpila() {
        return sucursalpila;
    }

    public void setSucursalpila(SucursalesPila sucursalpila) {
        this.sucursalpila = sucursalpila;
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
        if (!(object instanceof NovedadesCorreccionesAutoLiquidaciones)) {
            return false;
        }
        NovedadesCorreccionesAutoLiquidaciones other = (NovedadesCorreccionesAutoLiquidaciones) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.NovedadesAutoLiquidacion[ secuencia=" + secuencia + " ]";
    }

    
}
