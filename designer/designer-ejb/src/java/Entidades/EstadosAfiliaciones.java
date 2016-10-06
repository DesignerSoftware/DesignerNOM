package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "ESTADOSAFILIACIONES")
public class EstadosAfiliaciones implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @Basic(optional = false)
   @NotNull
   @Column(name = "SECUENCIA")
   private BigInteger secuencia;
   @Basic(optional = false)
   @NotNull
   @Column(name = "CODIGO")
   private BigInteger codigo;
   @Size(max = 30)
   @Column(name = "NOMBRE")
   private String nombre;

   public EstadosAfiliaciones() {
   }

   public EstadosAfiliaciones(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public EstadosAfiliaciones(BigInteger secuencia, BigInteger codigo) {
      this.secuencia = secuencia;
      this.codigo = codigo;
   }

   public BigInteger getSecuencia() {
      return secuencia;
   }

   public void setSecuencia(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public BigInteger getCodigo() {
      return codigo;
   }

   public void setCodigo(BigInteger codigo) {
      this.codigo = codigo;
   }

   public String getNombre() {
      return nombre;
   }

   public void setNombre(String nombre) {
      this.nombre = nombre;
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
      if (!(object instanceof EstadosAfiliaciones)) {
         return false;
      }
      EstadosAfiliaciones other = (EstadosAfiliaciones) object;
      if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "Entidades.EstadosAfiliaciones[ secuencia=" + secuencia + " ]";
   }

}
