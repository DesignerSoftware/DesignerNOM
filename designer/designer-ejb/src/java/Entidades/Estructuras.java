package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "ESTRUCTURAS")
public class Estructuras implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @Basic(optional = false)
   @NotNull
   @Column(name = "SECUENCIA")
   private BigInteger secuencia;
   @Column(name = "CODIGO")
   private Long codigo;
   @Column(name = "NOMBRE")
   private String nombre;
   @Size(max = 20)
   @Column(name = "CODIGOALFA")
   private String codigoalfa;
   @Size(max = 20)
   @Column(name = "CODIGOALTERNATIVO")
   private String codigoalternativo;
   @JoinColumn(name = "ORGANIGRAMA", referencedColumnName = "SECUENCIA")
   @ManyToOne(optional = false)
   private Organigramas organigrama;
   @JoinColumn(name = "ESTRUCTURAPADRE", referencedColumnName = "SECUENCIA")
   @ManyToOne
   private Estructuras estructurapadre;
   @JoinColumn(name = "CENTROCOSTO", referencedColumnName = "SECUENCIA")
   @ManyToOne
   private CentrosCostos centrocosto;
   @Transient
   private String cantidadCargosControlar;
   @Transient
   private String cantidadCargosEmplActivos;

   public Estructuras() {
   }

   public Estructuras(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public Estructuras(BigInteger secuencia, String nombre) {
      this.secuencia = secuencia;
      this.nombre = nombre;
   }

   public BigInteger getSecuencia() {
      return secuencia;
   }

   public void setSecuencia(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public Long getCodigo() {
      return codigo;
   }

   public void setCodigo(Long codigo) {
      this.codigo = codigo;
   }

   public String getNombre() {
      return nombre;
   }

   public void setNombre(String nombre) {
      this.nombre = nombre.toUpperCase();
   }

   public String getCodigoalfa() {
      return codigoalfa;
   }

   public void setCodigoalfa(String codigoalfa) {
      this.codigoalfa = codigoalfa;
   }

   public String getCodigoalternativo() {
      return codigoalternativo;
   }

   public void setCodigoalternativo(String codigoalternativo) {
      this.codigoalternativo = codigoalternativo;
   }

   public Organigramas getOrganigrama() {
      return organigrama;
   }

   public void setOrganigrama(Organigramas organigrama) {
      this.organigrama = organigrama;
   }

   public Estructuras getEstructurapadre() {
      return estructurapadre;
   }

   public void setEstructurapadre(Estructuras estructurapadre) {
      this.estructurapadre = estructurapadre;
   }

   public CentrosCostos getCentrocosto() {
      return centrocosto;
   }

   public void setCentrocosto(CentrosCostos centrocosto) {
      this.centrocosto = centrocosto;
   }

   public String getCantidadCargosControlar() {
      return cantidadCargosControlar;
   }

   public void setCantidadCargosControlar(String cantidadCargosControlar) {
      this.cantidadCargosControlar = cantidadCargosControlar;
   }

   public String getCantidadCargosEmplActivos() {
      return cantidadCargosEmplActivos;
   }

   public void setCantidadCargosEmplActivos(String cantidadCargosEmplActivos) {
      this.cantidadCargosEmplActivos = cantidadCargosEmplActivos;
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
      if (!(object instanceof Estructuras)) {
         return false;
      }
      Estructuras other = (Estructuras) object;
      if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "Entidades.Estructuras[ secuencia=" + secuencia + " ]";
   }
}
