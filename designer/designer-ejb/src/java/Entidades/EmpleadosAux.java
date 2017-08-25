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
public class EmpleadosAux implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @Basic(optional = false)
   @NotNull
   @Column(name = "SECUENCIA")
   private BigInteger secuencia;
   @Column(name = "PATHFOTO")
   
   private String pathfotoPersona;
   @Column(name = "NOMBREPERSONA")
   private String nombrePersona;
   @Column(name = "EMAIL")
   private String emailPersona;
   @Column(name = "NUMERODOCUMENTO")
   private BigInteger numeroDocumentoPersona;
   @Column(name = "PRIMERAPELLIDO")
   private String primerApellidoPersona;
   @Column(name = "SEGUNDOAPELLIDO")
   private String segundoApellidoPersona;
   @Column(name = "NOMBREEMPRESA")
   private String nombreEmpresa;
   @Column(name = "RETENCIONYSEGSOCXPERSONA")
   private String retencionysegsocxpersonaEmpresa;

   public BigInteger getSecuencia() {
      return secuencia;
   }

   public void setSecuencia(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public String getPathfotoPersona() {
      return pathfotoPersona;
   }

   public void setPathfotoPersona(String pathfotoPersona) {
      this.pathfotoPersona = pathfotoPersona;
   }

   public String getNombrePersona() {
      return nombrePersona;
   }

   public void setNombrePersona(String nombrePersona) {
      this.nombrePersona = nombrePersona;
   }

   public String getEmailPersona() {
      return emailPersona;
   }

   public void setEmailPersona(String emailPersona) {
      this.emailPersona = emailPersona;
   }

   public BigInteger getNumeroDocumentoPersona() {
      return numeroDocumentoPersona;
   }

   public void setNumeroDocumentoPersona(BigInteger numeroDocumentoPersona) {
      this.numeroDocumentoPersona = numeroDocumentoPersona;
   }

   public String getPrimerApellidoPersona() {
      return primerApellidoPersona;
   }

   public void setPrimerApellidoPersona(String primerApellidoPersona) {
      this.primerApellidoPersona = primerApellidoPersona;
   }

   public String getSegundoApellidoPersona() {
      return segundoApellidoPersona;
   }

   public void setSegundoApellidoPersona(String segundoApellidoPersona) {
      this.segundoApellidoPersona = segundoApellidoPersona;
   }

   public String getNombreEmpresa() {
      return nombreEmpresa;
   }

   public void setNombreEmpresa(String nombreEmpresa) {
      this.nombreEmpresa = nombreEmpresa;
   }

   public String getRetencionysegsocxpersonaEmpresa() {
      return retencionysegsocxpersonaEmpresa;
   }

   public void setRetencionysegsocxpersonaEmpresa(String retencionysegsocxpersonaEmpresa) {
      this.retencionysegsocxpersonaEmpresa = retencionysegsocxpersonaEmpresa;
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
      if (!(object instanceof EmpleadosAux)) {
         return false;
      }
      EmpleadosAux other = (EmpleadosAux) object;
      if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "Entidades.EmpleadosAux[ id=" + secuencia + " ]";
   }

}
