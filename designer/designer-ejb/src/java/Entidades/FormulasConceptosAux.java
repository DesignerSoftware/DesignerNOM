package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrator
 */
@Entity
@Cacheable(false)
public class FormulasConceptosAux implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @Basic(optional = false)
   @NotNull
   @Column(name = "SECUENCIA")
   private BigInteger secuencia;

   @Column(name = "NITEMPRESA")
   private long nitEmpresa;
   @Column(name = "NOMBREEMPRESA")
   private String nombreEmpresa;
   @Column(name = "NOMBREFORMULA")
   private String nombreFormula;
   @Column(name = "NOMBRECONCEPTO")
   private String nombreConcepto;
   @Column(name = "CODIGOCONCEPTO")
   private BigInteger codigoConcepto;

   public BigInteger getSecuencia() {
      return secuencia;
   }

   public void setSecuencia(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public FormulasConceptosAux() {
   }

   public FormulasConceptosAux(BigInteger secuencia, long nitEmpresa, String nombreEmpresa, String nombreFormula, String nombreConcepto, BigInteger codigoConcepto) {
      this.secuencia = secuencia;
      this.nitEmpresa = nitEmpresa;
      this.nombreEmpresa = nombreEmpresa;
      this.nombreFormula = nombreFormula;
      this.nombreConcepto = nombreConcepto;
      this.codigoConcepto = codigoConcepto;
   }

   public String getNombreEmpresa() {
      return nombreEmpresa;
   }

   public void setNombreEmpresa(String nombreEmpresa) {
      this.nombreEmpresa = nombreEmpresa;
   }

   public String getNombreFormula() {
      return nombreFormula;
   }

   public void setNombreFormula(String nombreFormula) {
      this.nombreFormula = nombreFormula;
   }

   public String getNombreConcepto() {
      return nombreConcepto;
   }

   public void setNombreConcepto(String nombreConcepto) {
      this.nombreConcepto = nombreConcepto;
   }

   public BigInteger getCodigoConcepto() {
      return codigoConcepto;
   }

   public void setCodigoConcepto(BigInteger codigoConcepto) {
      this.codigoConcepto = codigoConcepto;
   }

   public long getNitEmpresa() {
      return nitEmpresa;
   }

   public void setNitEmpresa(long nitEmpresa) {
      this.nitEmpresa = nitEmpresa;
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
      if (!(object instanceof FormulasConceptosAux)) {
         return false;
      }
      FormulasConceptosAux other = (FormulasConceptosAux) object;
      if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "Entidades.FormulasConceptosAux[ secuencia = " + secuencia + " ], [ nombreFormula = " + nombreFormula + " ]  y [ nombreConcepto = " + nombreConcepto + " ]";
   }
}
