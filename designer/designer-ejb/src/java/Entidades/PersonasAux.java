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
public class PersonasAux implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @Basic(optional = false)
   @NotNull
   @Column(name = "SECUENCIA")
   private BigInteger secuencia;
   @Column(name = "NOMBRETIPODOCUMENTO")
   private String nombreTipoDocumento;
   @Column(name = "NOMBRECIUDADDOCUMENTO")
   private String nombreCiudadDocumento;
   @Column(name = "NOMBRECIUDADNACIMIENTO")
   private String nombreCiudadNacimiento;

   public BigInteger getSecuencia() {
      return secuencia;
   }

   public void setSecuencia(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public String getNombreTipoDocumento() {
      return nombreTipoDocumento;
   }

   public void setNombreTipoDocumento(String nombreTipoDocumento) {
      this.nombreTipoDocumento = nombreTipoDocumento;
   }

   public String getNombreCiudadDocumento() {
      return nombreCiudadDocumento;
   }

   public void setNombreCiudadDocumento(String nombreCiudadDocumento) {
      this.nombreCiudadDocumento = nombreCiudadDocumento;
   }

   public String getNombreCiudadNacimiento() {
      return nombreCiudadNacimiento;
   }

   public void setNombreCiudadNacimiento(String nombreCiudadNacimiento) {
      this.nombreCiudadNacimiento = nombreCiudadNacimiento;
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
      if (!(object instanceof PersonasAux)) {
         return false;
      }
      PersonasAux other = (PersonasAux) object;
      if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "Entidades.PersonasAux[ id=" + secuencia + " ]";
   }

}
