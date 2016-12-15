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
@Table(name = "CAMBIOSMASIVOS")
public class CambiosMasivos implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @Basic(optional = false)
   @NotNull
   @Column(name = "SECUENCIA")
   private BigInteger secuencia;
   @Size(max = 30)
   @Column(name = "USUARIOBD")
   private String usuarioBD;
   @Size(max = 30)
   @Column(name = "MARCA")
   private String marca;
   @Column(name = "EMPLEADO")
   private BigInteger empleado;
   @Column(name = "SECUENCIATABLA")
   private BigInteger secuenciaTabla;
   @Column(name = "ULTIMAMODIFICACION")
   @Temporal(TemporalType.DATE)
   private Date ultimaModificacion;
   
   @Transient
   private String nombreEmpleado;

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

   public String getMarca() {
      return marca;
   }

   public void setMarca(String marca) {
      this.marca = marca;
   }

   public BigInteger getEmpleado() {
      return empleado;
   }

   public void setEmpleado(BigInteger empleado) {
      this.empleado = empleado;
   }

   public BigInteger getSecuenciaTabla() {
      return secuenciaTabla;
   }

   public void setSecuenciaTabla(BigInteger secuenciaTabla) {
      this.secuenciaTabla = secuenciaTabla;
   }

   public Date getUltimaModificacion() {
      return ultimaModificacion;
   }

   public void setUltimaModificacion(Date ultimaModificacion) {
      this.ultimaModificacion = ultimaModificacion;
   }

   public String getNombreEmpleado() {
      return nombreEmpleado;
   }

   public void setNombreEmpleado(String nombreEmpleado) {
      this.nombreEmpleado = nombreEmpleado;
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
      if (!(object instanceof CambiosMasivos)) {
         return false;
      }
      CambiosMasivos other = (CambiosMasivos) object;
      if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "Entidades.CambiosMasivos[" + secuencia + "]";
   }

}
