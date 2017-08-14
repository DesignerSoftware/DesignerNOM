package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author PROYECTO01
 */
@Entity
@Table(name = "TEFORMULASCONCEPTOS")
public class TEFormulasConceptos implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @Basic(optional = false)
   @NotNull
   @Column(name = "SECUENCIA")
   private BigInteger secuencia;
   @JoinColumn(name = "TSGRUPOTIPOENTIDAD", referencedColumnName = "SECUENCIA")
   @ManyToOne
   private TSGruposTiposEntidades tsgrupotipoentidad;
   @JoinColumn(name = "TIPOENTIDAD", referencedColumnName = "SECUENCIA")
   @ManyToOne(optional = false)
   private TiposEntidades tipoentidad;
   @JoinColumn(name = "FORMULA", referencedColumnName = "SECUENCIA")
   @ManyToOne(optional = false)
   private Formulas formula;
//   @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
   @Column(name = "EMPRESA")
   private BigInteger empresa;
   @JoinColumn(name = "CONCEPTO", referencedColumnName = "SECUENCIA")
   @ManyToOne(optional = false)
   private Conceptos concepto;

   @Transient
   private String nombreEmpresa;

   public TEFormulasConceptos() {
   }

   public TEFormulasConceptos(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public BigInteger getSecuencia() {
      return secuencia;
   }

   public void setSecuencia(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public TSGruposTiposEntidades getTsgrupotipoentidad() {
      return tsgrupotipoentidad;
   }

   public void setTsgrupotipoentidad(TSGruposTiposEntidades tsgrupotipoentidad) {
      this.tsgrupotipoentidad = tsgrupotipoentidad;
   }

   public TiposEntidades getTipoentidad() {
      return tipoentidad;
   }

   public void setTipoentidad(TiposEntidades tipoentidad) {
      this.tipoentidad = tipoentidad;
   }

   public Formulas getFormula() {
      return formula;
   }

   public void setFormula(Formulas formula) {
      this.formula = formula;
   }

//   public Empresas getEmpresa() {
//        getConcepto();
//        if (concepto != null) {
//            empresa = concepto.getEmpresa();
//        }
//      return empresa;
//   }
   public BigInteger getEmpresa() {
      return empresa;
   }

   public void setEmpresa(BigInteger empresa) {
      this.empresa = empresa;
   }

   public String getNombreEmpresa() {
      return nombreEmpresa;
   }

   public void setNombreEmpresa(String nombreEmpresa) {
      this.nombreEmpresa = nombreEmpresa;
   }

   public Conceptos getConcepto() {
      return concepto;
   }

   public void setConcepto(Conceptos concepto) {
      this.concepto = concepto;
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
      if (!(object instanceof TEFormulasConceptos)) {
         return false;
      }
      TEFormulasConceptos other = (TEFormulasConceptos) object;
      if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "Entidades.TEFormulasConceptos[ secuencia=" + secuencia + " ]";
   }

}
