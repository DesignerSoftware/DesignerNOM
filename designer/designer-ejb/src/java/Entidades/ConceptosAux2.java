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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Cacheable(false)
public class ConceptosAux2 implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @Basic(optional = false)
   @NotNull
   @Column(name = "SECUENCIA")
   private BigInteger secuencia;
   @Column(name = "DESCRIPCION")
   private String descripcion;
   @Basic(optional = false)
   @NotNull
   @Size(min = 1, max = 1)
   @Column(name = "NATURALEZA")
   private String naturaleza;
   @Size(max = 8)
   @Column(name = "ACTIVO")
   private String activo;
   @Basic(optional = false)
   @NotNull
   @Column(name = "CODIGO")
   private BigInteger codigo;
   @Column(name = "CONJUNTO")
   private Short conjunto;
   @Column(name = "EMPRESA")
   private BigInteger empresa;
   @Column(name = "VIGENCIASCUENTAS")
   private int vigenciascuentas;

   @Transient
   private String naturalezaConcepto;
   @Transient
   private String estadoConcepto;
   @Transient
   private String parametrizado;

   public ConceptosAux2() {
      parametrizado = null;
      estadoConcepto = null;
      naturalezaConcepto = null;
   }

   public BigInteger getSecuencia() {
      return secuencia;
   }

   public void setSecuencia(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public String getDescripcion() {
      return descripcion;
   }

   public void setDescripcion(String descripcion) {
      this.descripcion = descripcion;
   }

   public String getNaturaleza() {
      return naturaleza;
   }

   public void setNaturaleza(String naturaleza) {
      this.naturaleza = naturaleza;
   }

   public String getActivo() {
      return activo;
   }

   public void setActivo(String activo) {
      this.activo = activo;
   }

   public BigInteger getCodigo() {
      return codigo;
   }

   public void setCodigo(BigInteger codigo) {
      this.codigo = codigo;
   }

   public Short getConjunto() {
      return conjunto;
   }

   public void setConjunto(Short conjunto) {
      this.conjunto = conjunto;
   }

   public BigInteger getEmpresa() {
      return empresa;
   }

   public void setEmpresa(BigInteger empresa) {
      this.empresa = empresa;
   }

   public String getNaturalezaConcepto() {
      if (naturalezaConcepto == null) {
         if (naturaleza == null) {
            naturalezaConcepto = "NETO";
         } else if (naturaleza.equalsIgnoreCase("N")) {
            naturalezaConcepto = "NETO";
         } else if (naturaleza.equalsIgnoreCase("G")) {
            naturalezaConcepto = "GASTO";
         } else if (naturaleza.equalsIgnoreCase("D")) {
            naturalezaConcepto = "DESCUENTO";
         } else if (naturaleza.equalsIgnoreCase("P")) {
            naturalezaConcepto = "PAGO";
         } else if (naturaleza.equalsIgnoreCase("L")) {
            naturalezaConcepto = "PASIVO";
         }
      }
      return naturalezaConcepto;
   }

   public void setNaturalezaConcepto(String naturalezaConcepto) {
      this.naturalezaConcepto = naturalezaConcepto;
   }

   public int getVigenciascuentas() {
      return vigenciascuentas;
   }

   public void setVigenciascuentas(int vigenciascuentas) {
      this.vigenciascuentas = vigenciascuentas;
   }

   public String getEstadoConcepto() {
      if (estadoConcepto == null) {
         if (activo == null) {
            estadoConcepto = "ACTIVO";
         } else if (activo.equalsIgnoreCase("S")) {
            estadoConcepto = "ACTIVO";
         } else if (activo.equalsIgnoreCase("N")) {
            estadoConcepto = "INACT";
         }
      }
      return estadoConcepto;
   }

   public void setEstadoConcepto(String estadoConcepto) {
      this.estadoConcepto = estadoConcepto;
   }

   public String getParametrizado() {
      if (parametrizado == null) {
         if (vigenciascuentas > 0) {
            parametrizado = "SI";
         } else {
            parametrizado = "NO";
         }
      }
      return parametrizado;
   }

   public void setParametrizado(String parametrizado) {
      this.parametrizado = parametrizado;
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
      if (!(object instanceof ConceptosAux2)) {
         return false;
      }
      ConceptosAux2 other = (ConceptosAux2) object;
      if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "Entidades.ConceptosAux2[ id=" + secuencia + " ]";
   }

}
