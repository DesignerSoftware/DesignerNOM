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
public class DetallesEmpresasAux implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @Basic(optional = false)
   @NotNull
   @Column(name = "SECUENCIA")
   private BigInteger secuencia;

   @Column(name = "NOMBRE_PERSONAFIRMACONSTANCIA")
   private String nombre_personafirmaconstancia;
   @Column(name = "NOMBRE_EMPRESA")
   private String nombre_empresa;
   @Column(name = "NOMBRE_REPRESENTANTECIR")
   private String nombre_representantecir;
   @Column(name = "NOMBRE_SUBGERENTE")
   private String nombre_subgerente;
   @Column(name = "NOMBRE_ARQUITECTO")
   private String nombre_arquitecto;
   @Column(name = "NOMBRE_GERENTEGENERAL")
   private String nombre_gerentegeneral;
   @Column(name = "NOMBRE_CIUDAD")
   private String nombre_ciudad;
   @Column(name = "NOMBRE_CIUDADDOCREPRESENTANTE")
   private String ref_ciudaddocrepresentante;
   @Column(name = "NOMBRE_CARGOFIRMACONSTANCIA")
   private String nombre_cargofirmaconstancia;
   @Column(name = "CONTROLEMPLEADOS")
   private BigInteger controlempleados;
   @Column(name = "NIT")
   private BigInteger nit_Empresa;

   ;

   public BigInteger getSecuencia() {
      return secuencia;
   }

   public void setSecuencia(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public String getNombre_personafirmaconstancia() {
      return nombre_personafirmaconstancia;
   }

   public void setNombre_personafirmaconstancia(String nombre_personafirmaconstancia) {
      this.nombre_personafirmaconstancia = nombre_personafirmaconstancia;
   }

   public String getNombre_empresa() {
      return nombre_empresa;
   }

   public void setNombre_empresa(String nombre_empresa) {
      this.nombre_empresa = nombre_empresa;
   }

   public String getNombre_representantecir() {
      return nombre_representantecir;
   }

   public void setNombre_representantecir(String nombre_representantecir) {
      this.nombre_representantecir = nombre_representantecir;
   }

   public String getNombre_subgerente() {
      return nombre_subgerente;
   }

   public void setNombre_subgerente(String nombre_subgerente) {
      this.nombre_subgerente = nombre_subgerente;
   }

   public String getNombre_arquitecto() {
      return nombre_arquitecto;
   }

   public void setNombre_arquitecto(String nombre_arquitecto) {
      this.nombre_arquitecto = nombre_arquitecto;
   }

   public String getNombre_gerentegeneral() {
      return nombre_gerentegeneral;
   }

   public void setNombre_gerentegeneral(String nombre_gerentegeneral) {
      this.nombre_gerentegeneral = nombre_gerentegeneral;
   }

   public String getNombre_ciudad() {
      return nombre_ciudad;
   }

   public void setNombre_ciudad(String nombre_ciudad) {
      this.nombre_ciudad = nombre_ciudad;
   }

   public String getRef_ciudaddocrepresentante() {
      return ref_ciudaddocrepresentante;
   }

   public void setRef_ciudaddocrepresentante(String ref_ciudaddocrepresentante) {
      this.ref_ciudaddocrepresentante = ref_ciudaddocrepresentante;
   }

   public String getNombre_cargofirmaconstancia() {
      return nombre_cargofirmaconstancia;
   }

   public void setNombre_cargofirmaconstancia(String nombre_cargofirmaconstancia) {
      this.nombre_cargofirmaconstancia = nombre_cargofirmaconstancia;
   }

   public BigInteger getControlempleados() {
      return controlempleados;
   }

   public void setControlempleados(BigInteger controlempleados) {
      this.controlempleados = controlempleados;
   }

   public BigInteger getNit_Empresa() {
      return nit_Empresa;
   }

   public void setNit_Empresa(BigInteger nit_Empresa) {
      this.nit_Empresa = nit_Empresa;
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
      if (!(object instanceof DetallesEmpresasAux)) {
         return false;
      }
      DetallesEmpresasAux other = (DetallesEmpresasAux) object;
      if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "Entidades.DetallesEmpresasAux[ id=" + secuencia + " ]";
   }

}
