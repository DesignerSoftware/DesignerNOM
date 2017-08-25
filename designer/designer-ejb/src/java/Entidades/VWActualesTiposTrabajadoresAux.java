/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class VWActualesTiposTrabajadoresAux implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @Basic(optional = false)
   @NotNull
   @Column(name = "SECUENCIA")
   private BigInteger secuencia;
   @Column(name = "RETENCIONYSEGSOCXPERSONA")
   private String retencionysegsocxpersonaEmpresa;
   @Column(name = "NUMERODOCUMENTO")
   private BigInteger numeroDocumentoEmpleado;
   @Column(name = "CODIGOEMPLEADO")
   private BigDecimal codigoEmpleado;
   @Column(name = "PERSONA")
   private BigInteger persona;
   @Column(name = "NOMBREPERSONA")
   private String nombrePersona;
   @Column(name = "PRIMERAPELLIDO")
   private String primerApellidoPersona;
   @Column(name = "SEGUNDOAPELLIDO")
   private String segundoApellidoPersona;

   public BigInteger getSecuencia() {
      return secuencia;
   }

   public void setSecuencia(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public String getRetencionysegsocxpersonaEmpresa() {
      return retencionysegsocxpersonaEmpresa;
   }

   public void setRetencionysegsocxpersonaEmpresa(String retencionysegsocxpersonaEmpresa) {
      this.retencionysegsocxpersonaEmpresa = retencionysegsocxpersonaEmpresa;
   }

   public BigInteger getNumeroDocumentoEmpleado() {
      return numeroDocumentoEmpleado;
   }

   public void setNumeroDocumentoEmpleado(BigInteger numeroDocumentoEmpleado) {
      this.numeroDocumentoEmpleado = numeroDocumentoEmpleado;
   }

   public BigDecimal getCodigoEmpleado() {
      return codigoEmpleado;
   }

   public void setCodigoEmpleado(BigDecimal codigoEmpleado) {
      this.codigoEmpleado = codigoEmpleado;
   }

   public BigInteger getPersona() {
      return persona;
   }

   public void setPersona(BigInteger persona) {
      this.persona = persona;
   }

   public String getNombrePersona() {
      return nombrePersona;
   }

   public void setNombrePersona(String nombrePersona) {
      this.nombrePersona = nombrePersona;
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

   @Override
   public int hashCode() {
      int hash = 0;
      hash += (secuencia != null ? secuencia.hashCode() : 0);
      return hash;
   }

   @Override
   public boolean equals(Object object) {
      // TODO: Warning - this method won't work in the case the secuencia fields are not set
      if (!(object instanceof VWActualesTiposTrabajadoresAux)) {
         return false;
      }
      VWActualesTiposTrabajadoresAux other = (VWActualesTiposTrabajadoresAux) object;
      if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "Entidades.VWActualesTiposTrabajadoresAux[ id=" + secuencia + " ]";
   }

}
