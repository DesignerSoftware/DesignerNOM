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
public class ConceptosAux implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @Basic(optional = false)
   @NotNull
   @Column(name = "SECUENCIA")
   private BigInteger secuencia;
   @Column(name = "NOMBREUNIDAD")
   private String nombreUnidad;
   @Column(name = "CODIGOUNIDAD")
   private String codigoUnidad;
   @Column(name = "NOMBRETERCERO")
   private String nombreTercero;
   @Column(name = "NOMBREEMPRESA")
   private String nombreEmpresa;
   @Column(name = "NITEMPRESA")
   private long nitEmpresa;

   public BigInteger getSecuencia() {
      return secuencia;
   }

   public void setSecuencia(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public String getNombreUnidad() {
      return nombreUnidad;
   }

   public void setNombreUnidad(String nombreUnidad) {
      this.nombreUnidad = nombreUnidad;
   }

   public String getCodigoUnidad() {
      return codigoUnidad;
   }

   public void setCodigoUnidad(String codigoUnidad) {
      this.codigoUnidad = codigoUnidad;
   }

   public String getNombreTercero() {
      return nombreTercero;
   }

   public void setNombreTercero(String nombreTercero) {
      this.nombreTercero = nombreTercero;
   }

   public String getNombreEmpresa() {
      return nombreEmpresa;
   }

   public void setNombreEmpresa(String nombreEmpresa) {
      this.nombreEmpresa = nombreEmpresa;
   }

   public long getNitEmpresa() {
      return nitEmpresa;
   }

   public void setNitEmpresa(long nitEmpresa) {
      this.nitEmpresa = nitEmpresa;
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
      if (!(object instanceof ConceptosAux)) {
         return false;
      }
      ConceptosAux other = (ConceptosAux) object;
      if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "Entidades.ConceptosAux[ id=" + secuencia + " ]";
   }

}
