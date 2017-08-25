/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user
 */
@Entity
@Cacheable(false)
public class ParametrosCambiosMasivosAux implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @Basic(optional = false)
   @NotNull
   @Column(name = "SECUENCIA")
   private BigInteger secuencia;

   @Column(name = "AFILIATIPOENTIDAD")
   private String str_afiliaTipoEntidad;
   @Column(name = "AFILIATERCEROSUCURSAL")
   private String str_afiliaTerceroSucursal;
   @Column(name = "CARGOESTRUCTURA")
   private String str_cargoEstructura;
   @Column(name = "LOCALIESTRUCTURA")
   private String str_localiEstructura;
   @Column(name = "MOTIVODEFINITIVA")
   private String str_retiMotivoDefinitiva;
   @Column(name = "MOTIVORETIRO")
   private String str_retiMotivoRetiro;
   @Column(name = "NOVEDADCONCEPTO")
   private String str_noveConcepto;
   @Column(name = "NOVEDADPERIODICIDAD")
   private String str_novePeriodicidad;
   @Column(name = "NOVEDADTERCERO")
   private String str_noveTercero;
   @Column(name = "NOVEDADFORMULA")
   private String str_noveFormula;
   @Column(name = "MOTIVOCAMBIOSUELDO")
   private String str_sueldoMotivoCambioSueldo;
   @Column(name = "TIPOSUELDO")
   private String str_sueldoTipoSueldo;
   @Column(name = "SUELDOUNIDADPAGO")
   private String str_sueldoUnidadPago;

   public BigInteger getSecuencia() {
      return secuencia;
   }

   public void setSecuencia(BigInteger secuencia) {
      this.secuencia = secuencia;
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
      // TODO: Warning - this method won't work in the case the id fields are not set
      if (!(object instanceof ParametrosCambiosMasivosAux)) {
         return false;
      }
      ParametrosCambiosMasivosAux other = (ParametrosCambiosMasivosAux) object;
      if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "Entidades.ParametrosCambiosMasivosAux[ id=" + secuencia + " ]";
   }

}
