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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user
 */
@Entity
@Cacheable(false)
public class SolucionesNodosAux implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @Basic(optional = false)
   @NotNull
   @Column(name = "SECUENCIA")
   private BigInteger secuencia;
   @Column(name = "NOMBRETIPOTRABAJADOR")
   private String nombretipotrabajador;
   @Column(name = "NOMBRETIPOCONTRATO")
   private String nombretipocontrato;
   @Column(name = "NOMBRETERCERO")
   private String nombretercero;
   @Column(name = "NOMBREREFORMALABORAL")
   private String nombrereformalaboral;
   @Column(name = "NOMBREPROCESO")
   private String nombreproceso;
   @Column(name = "NOMBRECARGO")
   private String nombrecargo;
   @Column(name = "NOMBRECENTROCOSTOD")
   private String nombrecentrocostod;
   @Column(name = "NOMBRECENTROCOSTOC")
   private String nombrecentrocostoc;
   @Column(name = "NOMBRECONCEPTO")
   private String nombreconcepto;
   @Column(name = "CODIGOCONCEPTO")
   private BigInteger codigoconcepto;
   @Column(name = "CODIGOCUENTAD")
   private String codigocuentad;
   @Column(name = "CODIGOCUENTAC")
   private String codigocuentac;
   @Column(name = "NOMBREEMPLEADO")
   private String nombreempleado;
   @Column(name = "NOMBREESTRUCTURA")
   private String nombreestructura;
   @Column(name = "NOMBREFORMULA")
   private String nombreformula;
   @Column(name = "NITTERCERO")
   private BigInteger nittercero;

   public SolucionesNodosAux() {
   }

    public SolucionesNodosAux(BigInteger secuencia, String nombretipotrabajador, String nombretipocontrato, String nombretercero, String nombrereformalaboral, String nombreproceso, String nombrecargo, String nombrecentrocostod, String nombrecentrocostoc, String nombreconcepto, BigInteger codigoconcepto, String codigocuentad, String codigocuentac, String nombreempleado, String nombreestructura, String nombreformula, BigInteger nittercero) {
        this.secuencia = secuencia;
        this.nombretipotrabajador = nombretipotrabajador;
        this.nombretipocontrato = nombretipocontrato;
        this.nombretercero = nombretercero;
        this.nombrereformalaboral = nombrereformalaboral;
        this.nombreproceso = nombreproceso;
        this.nombrecargo = nombrecargo;
        this.nombrecentrocostod = nombrecentrocostod;
        this.nombrecentrocostoc = nombrecentrocostoc;
        this.nombreconcepto = nombreconcepto;
        this.codigoconcepto = codigoconcepto;
        this.codigocuentad = codigocuentad;
        this.codigocuentac = codigocuentac;
        this.nombreempleado = nombreempleado;
        this.nombreestructura = nombreestructura;
        this.nombreformula = nombreformula;
        this.nittercero = nittercero;
    }

   public SolucionesNodosAux(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public BigInteger getSecuencia() {
      return secuencia;
   }

   public void setSecuencia(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public String getNombretipotrabajador() {
      return nombretipotrabajador;
   }

   public void setNombretipotrabajador(String nombretipotrabajador) {
      this.nombretipotrabajador = nombretipotrabajador;
   }

   public String getNombretipocontrato() {
      return nombretipocontrato;
   }

   public void setNombretipocontrato(String nombretipocontrato) {
      this.nombretipocontrato = nombretipocontrato;
   }

   public String getNombretercero() {
      return nombretercero;
   }

   public void setNombretercero(String nombretercero) {
      this.nombretercero = nombretercero;
   }

   public String getNombrereformalaboral() {
      return nombrereformalaboral;
   }

   public void setNombrereformalaboral(String nombrereformalaboral) {
      this.nombrereformalaboral = nombrereformalaboral;
   }

   public String getNombreproceso() {
      return nombreproceso;
   }

   public void setNombreproceso(String nombreproceso) {
      this.nombreproceso = nombreproceso;
   }

   public String getNombrecargo() {
      return nombrecargo;
   }

   public void setNombrecargo(String nombrecargo) {
      this.nombrecargo = nombrecargo;
   }

   public String getNombrecentrocostod() {
      return nombrecentrocostod;
   }

   public void setNombrecentrocostod(String nombrecentrocostod) {
      this.nombrecentrocostod = nombrecentrocostod;
   }

   public String getNombrecentrocostoc() {
      return nombrecentrocostoc;
   }

   public void setNombrecentrocostoc(String nombrecentrocostoc) {
      this.nombrecentrocostoc = nombrecentrocostoc;
   }

   public String getNombreconcepto() {
      return nombreconcepto;
   }

   public void setNombreconcepto(String nombreconcepto) {
      this.nombreconcepto = nombreconcepto;
   }

   public BigInteger getCodigoconcepto() {
      return codigoconcepto;
   }

   public void setCodigoconcepto(BigInteger codigoconcepto) {
      this.codigoconcepto = codigoconcepto;
   }

   public String getCodigocuentad() {
      return codigocuentad;
   }

   public void setCodigocuentad(String codigocuentad) {
      this.codigocuentad = codigocuentad;
   }

   public String getCodigocuentac() {
      return codigocuentac;
   }

   public void setCodigocuentac(String codigocuentac) {
      this.codigocuentac = codigocuentac;
   }

   public String getNombreempleado() {
      return nombreempleado;
   }

   public void setNombreempleado(String nombreempleado) {
      this.nombreempleado = nombreempleado;
   }

   public String getNombreestructura() {
      return nombreestructura;
   }

   public void setNombreestructura(String nombreestructura) {
      this.nombreestructura = nombreestructura;
   }

   public String getNombreformula() {
      return nombreformula;
   }

   public void setNombreformula(String nombreformula) {
      this.nombreformula = nombreformula;
   }

    public BigInteger getNittercero() {
        return nittercero;
    }

    public void setNittercero(BigInteger nittercero) {
        this.nittercero = nittercero;
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
      if (!(object instanceof SolucionesNodosAux)) {
         return false;
      }
      SolucionesNodosAux other = (SolucionesNodosAux) object;
      if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "Entidades.SolucionesNodosAux[ secuencia=" + secuencia + " ]";
   }
}
