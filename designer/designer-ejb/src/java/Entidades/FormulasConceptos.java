package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "FORMULASCONCEPTOS")
public class FormulasConceptos implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @Basic(optional = false)
   @NotNull
   @Column(name = "SECUENCIA")
   private BigInteger secuencia;
   @Column(name = "FECHAINICIAL")
   @Temporal(TemporalType.TIMESTAMP)
   private Date fechainicial;
   @Column(name = "FECHAFINAL")
   @Temporal(TemporalType.TIMESTAMP)
   private Date fechafinal;
   @Basic(optional = false)
   @Column(name = "ORDEN")
   private BigInteger orden;
   @Basic(optional = false)
   @NotNull
   @Size(min = 1, max = 1)
   @Column(name = "TIPO")
   private String tipo;
   @Column(name = "FORMULA")
   private BigInteger formula;
   @Column(name = "CONCEPTO")
   private BigInteger concepto;

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

   @Transient
   private String strFechaInicial;
   @Transient
   private String strFechaFinal;
   @Transient
   private String strOrden;

   public FormulasConceptos() {
   }

   public FormulasConceptos(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public FormulasConceptos(BigInteger secuencia, Date fechainicial, Date fechafinal, BigInteger orden, String tipo) {
      this.secuencia = secuencia;
      this.fechainicial = fechainicial;
      this.fechafinal = fechafinal;
      this.orden = orden;
      this.tipo = tipo;
   }

   public BigInteger getSecuencia() {
      return secuencia;
   }

   public void setSecuencia(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public Date getFechainicial() {
      return fechainicial;
   }

   public void setFechainicial(Date fechainicial) {
      this.fechainicial = fechainicial;
   }

   public Date getFechafinal() {
      return fechafinal;
   }

   public void setFechafinal(Date fechafinal) {
      this.fechafinal = fechafinal;
   }

   public BigInteger getOrden() {
      return orden;
   }

   public void setOrden(BigInteger orden) {
      this.orden = orden;
   }

   public String getStrOrden() {
      getOrden();
      if (orden != null) {
         strOrden = orden.toString();
      } else {
         strOrden = "";
      }
      return strOrden;
   }

   public void setStrOrden(String strOrden) {
      if (!strOrden.isEmpty() && !strOrden.equals("") && !strOrden.equals(" ") && strOrden != null) {
         orden = new BigInteger(strOrden);
      } else {
         orden = null;
      }
      this.strOrden = strOrden;
   }

   public String getTipo() {
      return tipo;
   }

   public void setTipo(String tipo) {
      this.tipo = tipo;
   }

   public String getStrFechaInicial() {
      if (fechainicial != null) {
         SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
         strFechaInicial = formatoFecha.format(fechainicial);
      } else {
         strFechaInicial = " ";
      }
      return strFechaInicial;
   }

   public void setStrFechaInicial(String strFechaInicial) throws ParseException {
      SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
      fechainicial = formatoFecha.parse(strFechaInicial);
      this.strFechaInicial = strFechaInicial;
   }

   public String getStrFechaFinal() {
      if (fechafinal != null) {
         SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
         strFechaFinal = formatoFecha.format(fechafinal);
      } else {
         strFechaFinal = " ";
      }
      return strFechaFinal;
   }

   public void setStrFechaFinal(String strFechaFinal) throws ParseException {
      SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
      fechafinal = formatoFecha.parse(strFechaFinal);
      this.strFechaFinal = strFechaFinal;
   }

   public BigInteger getFormula() {
      return formula;
   }

   public void setFormula(BigInteger formula) {
      this.formula = formula;
   }

   public BigInteger getConcepto() {
      return concepto;
   }

   public void setConcepto(BigInteger concepto) {
      this.concepto = concepto;
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
      if (!(object instanceof FormulasConceptos)) {
         return false;
      }
      FormulasConceptos other = (FormulasConceptos) object;
      if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "Entidades.Formulasconceptos[ secuencia=" + secuencia + " ]";
   }
}
