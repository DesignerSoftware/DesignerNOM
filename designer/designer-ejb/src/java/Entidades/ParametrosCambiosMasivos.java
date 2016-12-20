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

   @Transient
   private boolean bul_indemniza;
   @Transient
   private boolean bul_retroactivo;

   @Transient
   private BigInteger trans_baseLiquidacion;
   @Transient
   private BigInteger trans_porcentaje;
   @Transient
   private BigInteger trans_dias;
   @Transient
   private BigInteger trans_horas;

   @Transient
   private Date trans_fechaReingreso;
   @Transient
   private Date trans_fechaFinContrato;
   @Transient
   private Date trans_fechaEmplJefe;
   @Transient
   private Date trans_fechaInicialAusent;
   @Transient
   private Date trans_fechaFinalAusent;
   @Transient
   private Date trans_fechaExpedicionAusent;
   @Transient
   private Date trans_fechaInicialPagoAusent;
   @Transient
   private Date trans_fechaFinalPagoAusent;
   @Transient
   private Date trans_fechaPapel;

   @Transient
   private String trans_nombreJefe;
   @Transient
   private String trans_tipoAusentismo;
   @Transient
   private String trans_clase;
   @Transient
   private String trans_causa;
   @Transient
   private String trans_formaLiquidacion;
   @Transient
   private String trans_papel;

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
      if (afiliaTipoEntidad == null) {
         afiliaTipoEntidad = new BigInteger("0");
      }
      return afiliaTipoEntidad;
   }

   public void setAfiliaTipoEntidad(BigInteger afiliaTipoEntidad) {
      this.afiliaTipoEntidad = afiliaTipoEntidad;
   }

   public BigInteger getAfiliaTerceroSucursal() {
      if (afiliaTerceroSucursal == null) {
         afiliaTerceroSucursal = new BigInteger("0");
      }
      return afiliaTerceroSucursal;
   }

   public void setAfiliaTerceroSucursal(BigInteger afiliaTerceroSucursal) {
      this.afiliaTerceroSucursal = afiliaTerceroSucursal;
   }

   public BigInteger getCargoEstructura() {
      if (cargoEstructura == null) {
         cargoEstructura = new BigInteger("0");
      }
      return cargoEstructura;
   }

   public void setCargoEstructura(BigInteger cargoEstructura) {
      this.cargoEstructura = cargoEstructura;
   }

   public BigInteger getLocaliEstructura() {
      if (localiEstructura == null) {
         localiEstructura = new BigInteger("0");
      }
      return localiEstructura;
   }

   public void setLocaliEstructura(BigInteger localiEstructura) {
      this.localiEstructura = localiEstructura;
   }

   public BigInteger getRetiMotivoDefinitiva() {
      if (retiMotivoDefinitiva == null) {
         retiMotivoDefinitiva = new BigInteger("0");
      }
      return retiMotivoDefinitiva;
   }

   public void setRetiMotivoDefinitiva(BigInteger retiMotivoDefinitiva) {
      this.retiMotivoDefinitiva = retiMotivoDefinitiva;
   }

   public BigInteger getRetiMotivoRetiro() {
      if (retiMotivoRetiro == null) {
         retiMotivoRetiro = new BigInteger("0");
      }
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
      if (noveConcepto == null) {
         noveConcepto = new BigInteger("0");
      }
      return noveConcepto;
   }

   public void setNoveConcepto(BigInteger noveConcepto) {
      this.noveConcepto = noveConcepto;
   }

   public BigInteger getNovePeriodicidad() {
      if (novePeriodicidad == null) {
         novePeriodicidad = new BigInteger("0");
      }
      return novePeriodicidad;
   }

   public void setNovePeriodicidad(BigInteger novePeriodicidad) {
      this.novePeriodicidad = novePeriodicidad;
   }

   public BigInteger getNoveTercero() {
      if (noveTercero == null) {
         noveTercero = new BigInteger("0");
      }
      return noveTercero;
   }

   public void setNoveTercero(BigInteger noveTercero) {
      this.noveTercero = noveTercero;
   }

   public BigInteger getNoveFormula() {
      if (noveFormula == null) {
         noveFormula = new BigInteger("0");
      }
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
      if (sueldoMotivoCambioSueldo == null) {
         sueldoMotivoCambioSueldo = new BigInteger("0");
      }
      return sueldoMotivoCambioSueldo;
   }

   public void setSueldoMotivoCambioSueldo(BigInteger sueldoMotivoCambioSueldo) {
      this.sueldoMotivoCambioSueldo = sueldoMotivoCambioSueldo;
   }

   public BigInteger getSueldoTipoSueldo() {
      if (sueldoTipoSueldo == null) {
         sueldoTipoSueldo = new BigInteger("0");
      }
      return sueldoTipoSueldo;
   }

   public void setSueldoTipoSueldo(BigInteger sueldoTipoSueldo) {
      this.sueldoTipoSueldo = sueldoTipoSueldo;
   }

   public BigInteger getSueldoUnidadPago() {
      if (sueldoUnidadPago == null) {
         sueldoUnidadPago = new BigInteger("0");
      }
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

   public boolean isBul_indemniza() {
      if (retiIndemniza != null) {
         if (retiIndemniza.equalsIgnoreCase("S")) {
            bul_indemniza = true;
         } else {
            bul_indemniza = false;
         }
      } else {
         bul_indemniza = false;
      }
      return bul_indemniza;
   }

   public void setBul_indemniza(boolean bul_indemniza) {
      this.bul_indemniza = bul_indemniza;
   }

   public boolean isBul_retroactivo() {
      if (sueldoRetroactivo != null) {
         if (sueldoRetroactivo.equalsIgnoreCase("S")) {
            bul_retroactivo = true;
         } else {
            bul_retroactivo = false;
         }
      } else {
         bul_retroactivo = false;
      }
      return bul_retroactivo;
   }

   public void setBul_retroactivo(boolean bul_retroactivo) {
      this.bul_retroactivo = bul_retroactivo;
   }

   public BigInteger getTrans_baseLiquidacion() {
      return trans_baseLiquidacion;
   }

   public void setTrans_baseLiquidacion(BigInteger trans_baseLiquidacion) {
      this.trans_baseLiquidacion = trans_baseLiquidacion;
   }

   public BigInteger getTrans_porcentaje() {
      return trans_porcentaje;
   }

   public void setTrans_porcentaje(BigInteger trans_porcentaje) {
      this.trans_porcentaje = trans_porcentaje;
   }

   public BigInteger getTrans_dias() {
      return trans_dias;
   }

   public void setTrans_dias(BigInteger trans_dias) {
      this.trans_dias = trans_dias;
   }

   public BigInteger getTrans_horas() {
      return trans_horas;
   }

   public void setTrans_horas(BigInteger trans_horas) {
      this.trans_horas = trans_horas;
   }

   public Date getTrans_fechaReingreso() {
      return trans_fechaReingreso;
   }

   public void setTrans_fechaReingreso(Date trans_fechaReingreso) {
      this.trans_fechaReingreso = trans_fechaReingreso;
   }

   public Date getTrans_fechaFinContrato() {
      return trans_fechaFinContrato;
   }

   public void setTrans_fechaFinContrato(Date trans_fechaFinContrato) {
      this.trans_fechaFinContrato = trans_fechaFinContrato;
   }

   public Date getTrans_fechaEmplJefe() {
      return trans_fechaEmplJefe;
   }

   public void setTrans_fechaEmplJefe(Date trans_fechaEmplJefe) {
      this.trans_fechaEmplJefe = trans_fechaEmplJefe;
   }

   public Date getTrans_fechaInicialAusent() {
      return trans_fechaInicialAusent;
   }

   public void setTrans_fechaInicialAusent(Date trans_fechaInicialAusent) {
      this.trans_fechaInicialAusent = trans_fechaInicialAusent;
   }

   public Date getTrans_fechaFinalAusent() {
      return trans_fechaFinalAusent;
   }

   public void setTrans_fechaFinalAusent(Date trans_fechaFinalAusent) {
      this.trans_fechaFinalAusent = trans_fechaFinalAusent;
   }

   public Date getTrans_fechaExpedicionAusent() {
      return trans_fechaExpedicionAusent;
   }

   public void setTrans_fechaExpedicionAusent(Date trans_fechaExpedicionAusent) {
      this.trans_fechaExpedicionAusent = trans_fechaExpedicionAusent;
   }

   public Date getTrans_fechaInicialPagoAusent() {
      return trans_fechaInicialPagoAusent;
   }

   public void setTrans_fechaInicialPagoAusent(Date trans_fechaInicialPagoAusent) {
      this.trans_fechaInicialPagoAusent = trans_fechaInicialPagoAusent;
   }

   public Date getTrans_fechaFinalPagoAusent() {
      return trans_fechaFinalPagoAusent;
   }

   public void setTrans_fechaFinalPagoAusent(Date trans_fechaFinalPagoAusent) {
      this.trans_fechaFinalPagoAusent = trans_fechaFinalPagoAusent;
   }

   public Date getTrans_fechaPapel() {
      return trans_fechaPapel;
   }

   public void setTrans_fechaPapel(Date trans_fechaPapel) {
      this.trans_fechaPapel = trans_fechaPapel;
   }

   public String getTrans_nombreJefe() {
      return trans_nombreJefe;
   }

   public void setTrans_nombreJefe(String trans_nombreJefe) {
      this.trans_nombreJefe = trans_nombreJefe;
   }

   public String getTrans_tipoAusentismo() {
      return trans_tipoAusentismo;
   }

   public void setTrans_tipoAusentismo(String trans_tipoAusentismo) {
      this.trans_tipoAusentismo = trans_tipoAusentismo;
   }

   public String getTrans_clase() {
      return trans_clase;
   }

   public void setTrans_clase(String trans_clase) {
      this.trans_clase = trans_clase;
   }

   public String getTrans_causa() {
      return trans_causa;
   }

   public void setTrans_causa(String trans_causa) {
      this.trans_causa = trans_causa;
   }

   public String getTrans_formaLiquidacion() {
      return trans_formaLiquidacion;
   }

   public void setTrans_formaLiquidacion(String trans_formaLiquidacion) {
      this.trans_formaLiquidacion = trans_formaLiquidacion;
   }

   public String getTrans_papel() {
      return trans_papel;
   }

   public void setTrans_papel(String trans_papel) {
      this.trans_papel = trans_papel;
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
