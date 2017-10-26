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
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Cacheable(false)
public class VigenciasCuentasAux implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @Basic(optional = false)
   @NotNull
   @Column(name = "SECUENCIA")
   private BigInteger secuencia;
   @Column(name = "NOMBRETIPOCC")
   private String nombreTipocc;
   @Column(name = "NOMBRECUENTAD")
   private String descripcionCuentad;
   @Column(name = "NOMBRECUENTAC")
   private String descripcionCuentac;
   @Column(name = "NOMBRECONSD")
   private String nombreConsolidadord;
   @Column(name = "NOMBRECONSC")
   private String nombreConsolidadorc;
   @Column(name = "DESCRIPCIONCONCEPTO")
   private String descripcionConcepto;

   @Basic(optional = false)
   @NotNull
   @Column(name = "CODIGOCONCEPTO")
   private BigInteger codigoConcepto;
   @Column(name = "CODCONSOLIDAC")
   private String codConsolidadorc;
   @Column(name = "CODCONSOLIDAD")
   private String codConsolidadord;
   @Basic(optional = false)
   @NotNull
   @Size(max = 20)
   @Column(name = "CODCUENTAC")
   private String codCuentac;
   @Basic(optional = false)
   @NotNull
   @Size(max = 20)
   @Column(name = "CODCUENTAD")
   private String codCuentad;
   @Column(name = "EMPRESA")
   private BigInteger empresa;
   @Basic(optional = false)
   @NotNull
   @Size(min = 1, max = 1)
   @Column(name = "NATURALEZA")
   private String naturaleza;

   public BigInteger getSecuencia() {
      return secuencia;
   }

   public void setSecuencia(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public String getNombreTipocc() {
      return nombreTipocc;
   }

   public void setNombreTipocc(String nombreTipocc) {
      this.nombreTipocc = nombreTipocc;
   }

   public String getDescripcionCuentad() {
      return descripcionCuentad;
   }

   public void setDescripcionCuentad(String descripcionCuentad) {
      this.descripcionCuentad = descripcionCuentad;
   }

   public String getDescripcionCuentac() {
      return descripcionCuentac;
   }

   public void setDescripcionCuentac(String descripcionCuentac) {
      this.descripcionCuentac = descripcionCuentac;
   }

   public String getNombreConsolidadord() {
      return nombreConsolidadord;
   }

   public void setNombreConsolidadord(String nombreConsolidadord) {
      this.nombreConsolidadord = nombreConsolidadord;
   }

   public String getNombreConsolidadorc() {
      return nombreConsolidadorc;
   }

   public void setNombreConsolidadorc(String nombreConsolidadorc) {
      this.nombreConsolidadorc = nombreConsolidadorc;
   }

   public BigInteger getCodigoConcepto() {
      return codigoConcepto;
   }

   public void setCodigoConcepto(BigInteger codigoConcepto) {
      this.codigoConcepto = codigoConcepto;
   }

   public String getDescripcionConcepto() {
      return descripcionConcepto;
   }

   public void setDescripcionConcepto(String descripcionConcepto) {
      this.descripcionConcepto = descripcionConcepto;
   }

   public String getCodConsolidadorc() {
      return codConsolidadorc;
   }

   public void setCodConsolidadorc(String codConsolidadorc) {
      this.codConsolidadorc = codConsolidadorc;
   }

   public String getCodConsolidadord() {
      return codConsolidadord;
   }

   public void setCodConsolidadord(String codConsolidadord) {
      this.codConsolidadord = codConsolidadord;
   }

   public String getCodCuentac() {
      return codCuentac;
   }

   public void setCodCuentac(String codCuentac) {
      this.codCuentac = codCuentac;
   }

   public String getCodCuentad() {
      return codCuentad;
   }

   public void setCodCuentad(String codCuentad) {
      this.codCuentad = codCuentad;
   }

   public BigInteger getEmpresa() {
      return empresa;
   }

   public void setEmpresa(BigInteger empresa) {
      this.empresa = empresa;
   }

   public String getNaturaleza() {
      return naturaleza;
   }

   public void setNaturaleza(String naturaleza) {
      this.naturaleza = naturaleza;
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
      if (!(object instanceof VigenciasCuentasAux)) {
         return false;
      }
      VigenciasCuentasAux other = (VigenciasCuentasAux) object;
      if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "Entidades.VigenciasCuentasAux[ id=" + secuencia + " ]";
   }

}
