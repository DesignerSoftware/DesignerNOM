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
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user
 */
@Entity
public class TempProrrateosAux implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @Basic(optional = false)
   @NotNull
   @Column(name = "SECUENCIA")
   private BigInteger secuencia;
   @Column(name = "NOMBREEMPLEADO")
   private String nombreEmpleado;
   @Column(name = "NOMBREPROYECTO")
   private String nombreProyecto;
   @Column(name = "NOMBRECENTROCOSTO")
   private String nombreCentrocosto;

   public BigInteger getSecuencia() {
      return secuencia;
   }

   public void setSecuencia(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public String getNombreEmpleado() {
      return nombreEmpleado;
   }

   public void setNombreEmpleado(String nombreEmpleado) {
      this.nombreEmpleado = nombreEmpleado;
   }

   public String getNombreProyecto() {
      return nombreProyecto;
   }

   public void setNombreProyecto(String nombreProyecto) {
      this.nombreProyecto = nombreProyecto;
   }

   public String getNombreCentrocosto() {
      return nombreCentrocosto;
   }

   public void setNombreCentrocosto(String nombreCentrocosto) {
      this.nombreCentrocosto = nombreCentrocosto;
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
      if (!(object instanceof TempProrrateosAux)) {
         return false;
      }
      TempProrrateosAux other = (TempProrrateosAux) object;
      if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "Entidades.TempProrrateosAux[ id=" + secuencia + " ]";
   }

}
