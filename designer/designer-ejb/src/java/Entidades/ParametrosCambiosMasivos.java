/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "PARAMETROSCAMBIOSMASIVOS")
public class ParametrosCambiosMasivos implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @Basic(optional = false)
   @NotNull
   @Column(name = "SECUENCIA")
   private BigInteger secuencia;
   @Size(max = 30)
   @Column(name = "USUARIOBD")
   private String usuarioBD;
   @Size(max = 1)
   @Column(name = "RETIINDEMNIZA")
   private String retiIndemniza;
   @Size(max = 20)
   @Column(name = "NOVETIPO")
   private String noveTipo;
   @Size(max = 50)
   @Column(name = "SUELDORETROACTIVO")
   private String sueldoRetroactivo;

   @Column(name = "AFILIACAMBIO")
   @Temporal(TemporalType.DATE)
   private Date fechaAfiliaCambio;
   @Column(name = "RETICAMBIO")
   @Temporal(TemporalType.DATE)
   private Date fechaRetiCambio;
   @Column(name = "CARGOCAMBIO")
   @Temporal(TemporalType.DATE)
   private Date fechaCargoCambio;
   @Column(name = "LOCALICAMBIO")
   @Temporal(TemporalType.DATE)
   private Date fechaLocaliCambio;
   @Column(name = "VACACAMBIO")
   @Temporal(TemporalType.DATE)
   private Date fechaVacaCambio;
   @Column(name = "NOVECAMBIOINICIAL")
   @Temporal(TemporalType.DATE)
   private Date fechaNoveCambioInicial;
   @Column(name = "NOVECAMBIOFINAL")
   @Temporal(TemporalType.DATE)
   private Date fechaNoveCambioFinal;
   @Column(name = "SUELDOCAMBIO")
   @Temporal(TemporalType.DATE)
   private Date fechaSueldoCambio;
   @Column(name = "VACAPAGO")
   @Temporal(TemporalType.DATE)
   private Date fechaVacaPago;
   @Column(name = "SUELDOVIGENCIARETROACTIVO")
   @Temporal(TemporalType.DATE)
   private Date fechaSueldoVigenciaRetroactivo;

   @Column(name = "VACADIAS")
   private BigInteger vacaDias;
   @Column(name = "NOVEVALOR")
   private BigInteger noveValor;
   @Column(name = "NOVESALDO")
   private BigInteger noveSaldo;
   @Column(name = "NOVEUNIDADESPARTEENTERA")
   private BigInteger noveUnidadParteEntera;
   @Column(name = "NOVEUNIDADESPARTEFRACCION")
   private BigInteger noveUnidadParteFraccion;
   @Column(name = "SUELDOVALOR")
   private BigInteger sueldoValor;

   @Column(name = "AFILIATIPOENTIDAD")
   private BigInteger afiliaTipoEntidad;
   @Column(name = "AFILIATERCEROSUCURSAL")
   private BigInteger afiliaTerceroSucursal;
   @Column(name = "CARGOESTRUCTURA")
   private BigInteger cargoEstructura;
   @Column(name = "LOCALIESTRUCTURA")
   private BigInteger localiEstructura;
   @Column(name = "RETIMOTIVODEFINITIVA")
   private BigInteger retiMotivoDefinitiva;
   @Column(name = "RETIMOTIVORETIRO")
   private BigInteger retiMotivoRetiro;
   @Column(name = "NOVECONCEPTO")
   private BigInteger noveConcepto;
   @Column(name = "NOVEPERIODICIDAD")
   private BigInteger novePeriodicidad;
   @Column(name = "NOVETERCERO")
   private BigInteger noveTercero;
   @Column(name = "NOVEFORMULA")
   private BigInteger noveFormula;
   @Column(name = "SUELDOMOTIVOCAMBIOSUELDO")
   private BigInteger sueldoMotivoCambioSueldo;
   @Column(name = "SUELDOTIPOSUELDO")
   private BigInteger sueldoTipoSueldo;
   @Column(name = "SUELDOUNIDADPAGO")
   private BigInteger sueldoUnidadPago;

   @Transient
   private String str_afiliaTipoEntidad;
   @Transient
   private String str_afiliaTerceroSucursal;
   @Transient
   private String str_cargoEstructura;
   @Transient
   private String str_localiEstructura;
   @Transient
   private String str_retiMotivoDefinitiva;
   @Transient
   private String str_retiMotivoRetiro;
   @Transient
   private String str_noveConcepto;
   @Transient
   private String str_novePeriodicidad;
   @Transient
   private String str_noveTercero;
   @Transient
   private String str_noveFormula;
   @Transient
   private String str_sueldoMotivoCambioSueldo;
   @Transient
   private String str_sueldoTipoSueldo;
   @Transient
   private String str_sueldoUnidadPago;

   public BigInteger getSecuencia() {
      return secuencia;
   }

   public void setSecuencia(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public String getUsuarioBD() {
      return usuarioBD;
   }

   public void setUsuarioBD(String usuarioBD) {
      this.usuarioBD = usuarioBD;
   }

   public String getRetiIndemniza() {
      return retiIndemniza;
   }

   public void setRetiIndemniza(String retiIndemniza) {
      this.retiIndemniza = retiIndemniza;
   }

   public String getNoveTipo() {
      return noveTipo;
   }

   public void setNoveTipo(String noveTipo) {
      this.noveTipo = noveTipo;
   }

   public String getSueldoRetroactivo() {
      return sueldoRetroactivo;
   }

   public void setSueldoRetroactivo(String sueldoRetroactivo) {
      this.sueldoRetroactivo = sueldoRetroactivo;
   }

   public Date getFechaAfiliaCambio() {
      return fechaAfiliaCambio;
   }

   public void setFechaAfiliaCambio(Date fechaAfiliaCambio) {
      this.fechaAfiliaCambio = fechaAfiliaCambio;
   }

   public Date getFechaRetiCambio() {
      return fechaRetiCambio;
   }

   public void setFechaRetiCambio(Date fechaRetiCambio) {
      this.fechaRetiCambio = fechaRetiCambio;
   }

   public Date getFechaCargoCambio() {
      return fechaCargoCambio;
   }

   public void setFechaCargoCambio(Date fechaCargoCambio) {
      this.fechaCargoCambio = fechaCargoCambio;
   }

   public Date getFechaLocaliCambio() {
      return fechaLocaliCambio;
   }

   public void setFechaLocaliCambio(Date fechaLocaliCambio) {
      this.fechaLocaliCambio = fechaLocaliCambio;
   }

   public Date getFechaVacaCambio() {
      return fechaVacaCambio;
   }

   public void setFechaVacaCambio(Date fechaVacaCambio) {
      this.fechaVacaCambio = fechaVacaCambio;
   }

   public Date getFechaNoveCambioInicial() {
      return fechaNoveCambioInicial;
   }

   public void setFechaNoveCambioInicial(Date fechaNoveCambioInicial) {
      this.fechaNoveCambioInicial = fechaNoveCambioInicial;
   }

   public Date getFechaNoveCambioFinal() {
      return fechaNoveCambioFinal;
   }

   public void setFechaNoveCambioFinal(Date fechaNoveCambioFinal) {
      this.fechaNoveCambioFinal = fechaNoveCambioFinal;
   }

   public Date getFechaSueldoCambio() {
      return fechaSueldoCambio;
   }

   public void setFechaSueldoCambio(Date fechaSueldoCambio) {
      this.fechaSueldoCambio = fechaSueldoCambio;
   }

   public Date getFechaVacaPago() {
      return fechaVacaPago;
   }

   public void setFechaVacaPago(Date fechaVacaPago) {
      this.fechaVacaPago = fechaVacaPago;
   }

   public Date getFechaSueldoVigenciaRetroactivo() {
      return fechaSueldoVigenciaRetroactivo;
   }

   public void setFechaSueldoVigenciaRetroactivo(Date fechaSueldoVigenciaRetroactivo) {
      this.fechaSueldoVigenciaRetroactivo = fechaSueldoVigenciaRetroactivo;
   }

   public BigInteger getAfiliaTipoEntidad() {
      return afiliaTipoEntidad;
   }

   public void setAfiliaTipoEntidad(BigInteger afiliaTipoEntidad) {
      this.afiliaTipoEntidad = afiliaTipoEntidad;
   }

   public BigInteger getAfiliaTerceroSucursal() {
      return afiliaTerceroSucursal;
   }

   public void setAfiliaTerceroSucursal(BigInteger afiliaTerceroSucursal) {
      this.afiliaTerceroSucursal = afiliaTerceroSucursal;
   }

   public BigInteger getCargoEstructura() {
      return cargoEstructura;
   }

   public void setCargoEstructura(BigInteger cargoEstructura) {
      this.cargoEstructura = cargoEstructura;
   }

   public BigInteger getLocaliEstructura() {
      return localiEstructura;
   }

   public void setLocaliEstructura(BigInteger localiEstructura) {
      this.localiEstructura = localiEstructura;
   }

   public BigInteger getRetiMotivoDefinitiva() {
      return retiMotivoDefinitiva;
   }

   public void setRetiMotivoDefinitiva(BigInteger retiMotivoDefinitiva) {
      this.retiMotivoDefinitiva = retiMotivoDefinitiva;
   }

   public BigInteger getRetiMotivoRetiro() {
      return retiMotivoRetiro;
   }

   public void setRetiMotivoRetiro(BigInteger retiMotivoRetiro) {
      this.retiMotivoRetiro = retiMotivoRetiro;
   }

   public BigInteger getVacaDias() {
      return vacaDias;
   }

   public void setVacaDias(BigInteger vacaDias) {
      this.vacaDias = vacaDias;
   }

   public BigInteger getNoveValor() {
      return noveValor;
   }

   public void setNoveValor(BigInteger noveValor) {
      this.noveValor = noveValor;
   }

   public BigInteger getNoveSaldo() {
      return noveSaldo;
   }

   public void setNoveSaldo(BigInteger noveSaldo) {
      this.noveSaldo = noveSaldo;
   }

   public BigInteger getNoveConcepto() {
      return noveConcepto;
   }

   public void setNoveConcepto(BigInteger noveConcepto) {
      this.noveConcepto = noveConcepto;
   }

   public BigInteger getNovePeriodicidad() {
      return novePeriodicidad;
   }

   public void setNovePeriodicidad(BigInteger novePeriodicidad) {
      this.novePeriodicidad = novePeriodicidad;
   }

   public BigInteger getNoveTercero() {
      return noveTercero;
   }

   public void setNoveTercero(BigInteger noveTercero) {
      this.noveTercero = noveTercero;
   }

   public BigInteger getNoveFormula() {
      return noveFormula;
   }

   public void setNoveFormula(BigInteger noveFormula) {
      this.noveFormula = noveFormula;
   }

   public BigInteger getNoveUnidadParteEntera() {
      return noveUnidadParteEntera;
   }

   public void setNoveUnidadParteEntera(BigInteger noveUnidadParteEntera) {
      this.noveUnidadParteEntera = noveUnidadParteEntera;
   }

   public BigInteger getNoveUnidadParteFraccion() {
      return noveUnidadParteFraccion;
   }

   public void setNoveUnidadParteFraccion(BigInteger noveUnidadParteFraccion) {
      this.noveUnidadParteFraccion = noveUnidadParteFraccion;
   }

   public BigInteger getSueldoMotivoCambioSueldo() {
      return sueldoMotivoCambioSueldo;
   }

   public void setSueldoMotivoCambioSueldo(BigInteger sueldoMotivoCambioSueldo) {
      this.sueldoMotivoCambioSueldo = sueldoMotivoCambioSueldo;
   }

   public BigInteger getSueldoTipoSueldo() {
      return sueldoTipoSueldo;
   }

   public void setSueldoTipoSueldo(BigInteger sueldoTipoSueldo) {
      this.sueldoTipoSueldo = sueldoTipoSueldo;
   }

   public BigInteger getSueldoUnidadPago() {
      return sueldoUnidadPago;
   }

   public void setSueldoUnidadPago(BigInteger sueldoUnidadPago) {
      this.sueldoUnidadPago = sueldoUnidadPago;
   }

   public BigInteger getSueldoValor() {
      return sueldoValor;
   }

   public void setSueldoValor(BigInteger sueldoValor) {
      this.sueldoValor = sueldoValor;
   }

   public String getStr_afiliaTipoEntidad() {
      return str_afiliaTipoEntidad;
   }

   public void setStr_afiliaTipoEntidad(String str_afiliaTipoEntidad) {
      this.str_afiliaTipoEntidad = str_afiliaTipoEntidad;
   }

   public String getStr_afiliaTerceroSucursal() {
      return str_afiliaTerceroSucursal;
   }

   public void setStr_afiliaTerceroSucursal(String str_afiliaTerceroSucursal) {
      this.str_afiliaTerceroSucursal = str_afiliaTerceroSucursal;
   }

   public String getStr_cargoEstructura() {
      return str_cargoEstructura;
   }

   public void setStr_cargoEstructura(String str_cargoEstructura) {
      this.str_cargoEstructura = str_cargoEstructura;
   }

   public String getStr_localiEstructura() {
      return str_localiEstructura;
   }

   public void setStr_localiEstructura(String str_localiEstructura) {
      this.str_localiEstructura = str_localiEstructura;
   }

   public String getStr_retiMotivoDefinitiva() {
      return str_retiMotivoDefinitiva;
   }

   public void setStr_retiMotivoDefinitiva(String str_retiMotivoDefinitiva) {
      this.str_retiMotivoDefinitiva = str_retiMotivoDefinitiva;
   }

   public String getStr_retiMotivoRetiro() {
      return str_retiMotivoRetiro;
   }

   public void setStr_retiMotivoRetiro(String str_retiMotivoRetiro) {
      this.str_retiMotivoRetiro = str_retiMotivoRetiro;
   }

   public String getStr_noveConcepto() {
      return str_noveConcepto;
   }

   public void setStr_noveConcepto(String str_noveConcepto) {
      this.str_noveConcepto = str_noveConcepto;
   }

   public String getStr_novePeriodicidad() {
      return str_novePeriodicidad;
   }

   public void setStr_novePeriodicidad(String str_novePeriodicidad) {
      this.str_novePeriodicidad = str_novePeriodicidad;
   }

   public String getStr_noveTercero() {
      return str_noveTercero;
   }

   public void setStr_noveTercero(String str_noveTercero) {
      this.str_noveTercero = str_noveTercero;
   }

   public String getStr_noveFormula() {
      return str_noveFormula;
   }

   public void setStr_noveFormula(String str_noveFormula) {
      this.str_noveFormula = str_noveFormula;
   }

   public String getStr_sueldoMotivoCambioSueldo() {
      return str_sueldoMotivoCambioSueldo;
   }

   public void setStr_sueldoMotivoCambioSueldo(String str_sueldoMotivoCambioSueldo) {
      this.str_sueldoMotivoCambioSueldo = str_sueldoMotivoCambioSueldo;
   }

   public String getStr_sueldoTipoSueldo() {
      return str_sueldoTipoSueldo;
   }

   public void setStr_sueldoTipoSueldo(String str_sueldoTipoSueldo) {
      this.str_sueldoTipoSueldo = str_sueldoTipoSueldo;
   }

   public String getStr_sueldoUnidadPago() {
      return str_sueldoUnidadPago;
   }

   public void setStr_sueldoUnidadPago(String str_sueldoUnidadPago) {
      this.str_sueldoUnidadPago = str_sueldoUnidadPago;
   }

   @Override
   public int hashCode() {
      int hash = 0;
      hash += (secuencia != null ? secuencia.hashCode() : 0);
      return hash;
   }

   @Override
   public boolean equals(Object object) {
      // TODO: Warning - this method won't work in the case the secuencia fields are not set
      if (!(object instanceof ParametrosCambiosMasivos)) {
         return false;
      }
      ParametrosCambiosMasivos other = (ParametrosCambiosMasivos) object;
      if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "Entidades.ParametrosCambiosMasivos[" + secuencia + "]";
   }

}
