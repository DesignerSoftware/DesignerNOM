package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user
 */
@Entity
@Table(name = "VIGENCIASCUENTAS")
public class VigenciasCuentas implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @Basic(optional = false)
   @NotNull
   @Column(name = "SECUENCIA")
   private BigInteger secuencia;
   @NotNull
   @Column(name = "FECHAFINAL")
   @Temporal(TemporalType.TIMESTAMP)
   private Date fechafinal;
   @Column(name = "PROCESO")
   private BigInteger proceso;
   @NotNull
   @Column(name = "FECHAINICIAL")
   @Temporal(TemporalType.TIMESTAMP)
   private Date fechainicial;
//   @NotNull
//   @JoinColumn(name = "TIPOCC", referencedColumnName = "SECUENCIA")
//   @ManyToOne(optional = true)
//   private TiposCentrosCostos tipocc;
//   @NotNull
//   @JoinColumn(name = "CUENTAD", referencedColumnName = "SECUENCIA")
//   @ManyToOne(optional = true)
//   private Cuentas cuentad;
//   @NotNull
//   @JoinColumn(name = "CUENTAC", referencedColumnName = "SECUENCIA")
//   @ManyToOne(optional = false)
//   private Cuentas cuentac;
//   @NotNull
//   @JoinColumn(name = "CONCEPTO", referencedColumnName = "SECUENCIA")
//   @ManyToOne(optional = true)
//   private Conceptos concepto;
//   @NotNull
//   @JoinColumn(name = "CONSOLIDADORD", referencedColumnName = "SECUENCIA")
//   @ManyToOne(optional = true)
//   private CentrosCostos consolidadord;
//   @NotNull
//   @JoinColumn(name = "CONSOLIDADORC", referencedColumnName = "SECUENCIA")
//   @ManyToOne(optional = true)
//   private CentrosCostos consolidadorc;

   @Column(name = "TIPOCC")
   private BigInteger tipocc;
   @Column(name = "CUENTAD")
   private BigInteger cuentad;
   @Column(name = "CUENTAC")
   private BigInteger cuentac;
   @Column(name = "CONCEPTO")
   private BigInteger concepto;
   @Column(name = "CONSOLIDADORD")
   private BigInteger consolidadord;
   @Column(name = "CONSOLIDADORC")
   private BigInteger consolidadorc;

   @Transient
   private String strFechaInicial;
   @Transient
   private String strFechaFinal;
   @Transient
   private String nombreProceso;

   @Transient
   private String nombreTipocc;
   @Transient
   private String descripcionCuentad;
   @Transient
   private String descripcionCuentac;
   @Transient
   private String descripcionConcepto;
   @Transient
   private String nombreConsolidadord;
   @Transient
   private String nombreConsolidadorc;
   @Transient
   private BigInteger codigoConcepto;
   @Transient
   private String codConsolidadorc;
   @Transient
   private String codConsolidadord;
   @Transient
   private String codCuentac;
   @Transient
   private String codCuentad;
   @Transient
   private BigInteger empresa;
   @Transient
   private String naturaleza;
   @Transient
   private boolean acrear;

   public VigenciasCuentas() {
   }

   public VigenciasCuentas(BigInteger secuencia, Conceptos concepto) {
      clear();
      this.secuencia = secuencia;
      this.concepto = concepto.getSecuencia();
      this.descripcionConcepto = concepto.getDescripcion();
      this.codigoConcepto = concepto.getCodigo();
      this.empresa = concepto.getEmpresa();
      this.naturaleza = concepto.getNaturaleza();
   }

   public VigenciasCuentas(BigInteger secuencia, ConceptosAux2 concepto) {
      clear();
      this.secuencia = secuencia;
      this.concepto = concepto.getSecuencia();
      this.descripcionConcepto = concepto.getDescripcion();
      this.codigoConcepto = concepto.getCodigo();
      this.empresa = concepto.getEmpresa();
      this.naturaleza = concepto.getNaturaleza();
   }

   public BigInteger getSecuencia() {
      return secuencia;
   }

   public void setSecuencia(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public Date getFechafinal() {
      return fechafinal;
   }

   public void setFechafinal(Date fechafinal) {
      this.fechafinal = fechafinal;
   }

   public Date getFechainicial() {
      return fechainicial;
   }

   public void setFechainicial(Date fechainicial) {
      this.fechainicial = fechainicial;
   }

   public BigInteger getTipocc() {
      return tipocc;
   }

   public void setTipocc(BigInteger tipocc) {
      this.tipocc = tipocc;
   }

   public void setTipocc(TiposCentrosCostos tipocc) {
      this.tipocc = tipocc.getSecuencia();
      this.nombreTipocc = tipocc.getNombre();
   }

   public BigInteger getCuentad() {
      return cuentad;
   }

   public void setCuentad(BigInteger cuentad) {
      this.cuentad = cuentad;
   }
//
//   public void setCuentad(BigInteger cuentad, String nombre, String cod) {
//      this.cuentad = cuentad;
//      this.descripcionCuentad = nombre;
//      this.codCuentad = cod;
//   }

   public void setCuentad(Cuentas cuenta) {
      this.cuentad = cuenta.getSecuencia();
      this.descripcionCuentad = cuenta.getDescripcion();
      this.codCuentad = cuenta.getCodigo();
   }

   public BigInteger getCuentac() {
      return cuentac;
   }

   public void setCuentac(BigInteger cuentac) {
      this.cuentac = cuentac;
   }

//   public void setCuentac(BigInteger cuentac, String nombre, String cod) {
//      this.cuentac = cuentac;
//      this.descripcionCuentac = nombre;
//      this.codCuentac = cod;
//   }
   public void setCuentac(Cuentas cuenta) {
      this.cuentac = cuenta.getSecuencia();
      this.descripcionCuentac = cuenta.getDescripcion();
      this.codCuentac = cuenta.getCodigo();
   }

   public BigInteger getConcepto() {
      return concepto;
   }

   public void setConcepto(BigInteger concepto) {
      this.concepto = concepto;
   }
//
//   public void setConcepto(BigInteger concepto, String nombre, BigInteger cod) {
//      this.concepto = concepto;
//      this.descripcionConcepto = nombre;
//      this.codigoConcepto = cod;
//   }

   public void setConcepto(Conceptos concepto) {
      this.concepto = concepto.getSecuencia();
      this.descripcionConcepto = concepto.getDescripcion();
      this.codigoConcepto = concepto.getCodigo();
      this.empresa = concepto.getEmpresa();
      this.naturaleza = concepto.getNaturaleza();
   }

   public void setConcepto(ConceptosAux2 concepto) {
      this.concepto = concepto.getSecuencia();
      this.descripcionConcepto = concepto.getDescripcion();
      this.codigoConcepto = concepto.getCodigo();
      this.empresa = concepto.getEmpresa();
      this.naturaleza = concepto.getNaturaleza();
   }

   public BigInteger getConsolidadord() {
      return consolidadord;
   }

   public void setConsolidadord(BigInteger consolidadord) {
      this.consolidadord = consolidadord;
   }
//
//   public void setConsolidadord(BigInteger consolidadord, String nombre, String cod) {
//      this.consolidadord = consolidadord;
//      this.nombreConsolidadord = nombre;
//      this.codConsolidadord = cod;
//   }

   public void setConsolidadord(CentrosCostos centroc) {
      this.consolidadord = centroc.getSecuencia();
      this.nombreConsolidadord = centroc.getNombre();
      this.codConsolidadord = centroc.getCodigo();
   }

   public BigInteger getConsolidadorc() {
      return consolidadorc;
   }

   public void setConsolidadorc(BigInteger consolidadorc) {
      this.consolidadorc = consolidadorc;
   }
//
//   public void setConsolidadorc(BigInteger consolidadorc, String nombre, String cod) {
//      this.consolidadorc = consolidadorc;
//      this.nombreConsolidadorc = nombre;
//      this.codConsolidadorc = cod;
//   }

   public void setConsolidadorc(CentrosCostos centroc) {
      this.consolidadorc = centroc.getSecuencia();
      this.nombreConsolidadorc = centroc.getNombre();
      this.codConsolidadorc = centroc.getCodigo();
   }

   public String getStrFechaInicial() {
      if (fechainicial != null) {
         SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
         strFechaInicial = formatoFecha.format(fechainicial);
      } else {
         strFechaInicial = "";
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
         strFechaFinal = "";
      }
      return strFechaFinal;
   }

   public void setStrFechaFinal(String strFechaFinal) throws ParseException {
      SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
      fechafinal = formatoFecha.parse(strFechaFinal);
      this.strFechaFinal = strFechaFinal;
   }

   public BigInteger getProceso() {
      return proceso;
   }

   public void setProceso(BigInteger proceso) {
      this.proceso = proceso;
   }

   public String getNombreProceso() {
      return nombreProceso;
   }

   public void setNombreProceso(String nombreProceso) {
      this.nombreProceso = nombreProceso;
   }

   public boolean getAcrear() {
      return acrear;
   }

   public String getNombreTipocc() {
      return nombreTipocc;
   }

   public void setNombreTipocc(String nombreTipocc) {
      this.nombreTipocc = nombreTipocc;
   }

   public String getDescripcionCuentad() {
      return descripcionCuentad;
   }

   public void setDescripcionCuentad(String descripcionCuentad) {
      this.descripcionCuentad = descripcionCuentad;
   }

   public String getDescripcionCuentac() {
      return descripcionCuentac;
   }

   public void setDescripcionCuentac(String descripcionCuentac) {
      this.descripcionCuentac = descripcionCuentac;
   }

   public String getDescripcionConcepto() {
      return descripcionConcepto;
   }

   public void setDescripcionConcepto(String descripcionConcepto) {
      this.descripcionConcepto = descripcionConcepto;
   }

   public String getNombreConsolidadord() {
      return nombreConsolidadord;
   }

   public void setNombreConsolidadord(String nombreConsolidadord) {
      this.nombreConsolidadord = nombreConsolidadord;
   }

   public String getNombreConsolidadorc() {
      return nombreConsolidadorc;
   }

   public void setNombreConsolidadorc(String nombreConsolidadorc) {
      this.nombreConsolidadorc = nombreConsolidadorc;
   }

   public void setAcrear(boolean acrear) {
      this.acrear = acrear;
   }

   public BigInteger getCodigoConcepto() {
      return codigoConcepto;
   }

   public void setCodigoConcepto(BigInteger codigoConcepto) {
      this.codigoConcepto = codigoConcepto;
   }

   public String getCodConsolidadorc() {
      return codConsolidadorc;
   }

   public void setCodConsolidadorc(String codConsolidadorc) {
      this.codConsolidadorc = codConsolidadorc;
   }

   public String getCodConsolidadord() {
      return codConsolidadord;
   }

   public void setCodConsolidadord(String codConsolidadord) {
      this.codConsolidadord = codConsolidadord;
   }

   public String getCodCuentac() {
      return codCuentac;
   }

   public void setCodCuentac(String codCuentac) {
      this.codCuentac = codCuentac;
   }

   public String getCodCuentad() {
      return codCuentad;
   }

   public void setCodCuentad(String codCuentad) {
      this.codCuentad = codCuentad;
   }

   public BigInteger getEmpresa() {
      return empresa;
   }

   public void setEmpresa(BigInteger empresa) {
      this.empresa = empresa;
   }

   public String getNaturaleza() {
      return naturaleza;
   }

   public void setNaturaleza(String naturaleza) {
      this.naturaleza = naturaleza;
   }

   public void clear() {
      this.fechainicial = new Date(100, 0, 1);
      this.fechafinal = new Date(9999 - 1900, 11, 31);
      this.consolidadorc = null;
      this.consolidadord = null;
      this.cuentac = null;
      this.cuentad = null;
      this.tipocc = null;
      this.proceso = null;
      this.nombreProceso = null;
      this.nombreTipocc = null;
      this.descripcionCuentad = null;
      this.descripcionCuentac = null;
      this.descripcionConcepto = null;
      this.nombreConsolidadord = null;
      this.nombreConsolidadorc = null;
      this.codConsolidadorc = null;
      this.codConsolidadord = null;
      this.codCuentac = null;
      this.codCuentad = null;
      this.empresa = null;
      this.naturaleza = null;
      this.acrear = false;
   }

   public void clonar(VigenciasCuentas vigenciac) {
      this.concepto = vigenciac.getSecuencia();
      this.descripcionConcepto = vigenciac.getDescripcionConcepto();
      this.codigoConcepto = vigenciac.getCodigoConcepto();
      this.empresa = vigenciac.getEmpresa();
      this.naturaleza = vigenciac.getNaturaleza();
      this.consolidadorc = vigenciac.getConsolidadorc();
      this.consolidadord = vigenciac.getConsolidadord();
      this.cuentac = vigenciac.getCuentac();
      this.cuentad = vigenciac.getCuentad();
      this.tipocc = vigenciac.getTipocc();
      this.proceso = vigenciac.getProceso();
      this.nombreProceso = vigenciac.getNombreProceso();
      this.nombreTipocc = vigenciac.getNombreTipocc();
      this.descripcionCuentad = vigenciac.getDescripcionCuentad();
      this.descripcionCuentac = vigenciac.getDescripcionCuentac();
      this.descripcionConcepto = vigenciac.getDescripcionConcepto();
      this.nombreConsolidadord = vigenciac.getNombreConsolidadord();
      this.nombreConsolidadorc = vigenciac.getNombreConsolidadorc();
      this.codConsolidadorc = vigenciac.getCodConsolidadorc();
      this.codConsolidadord = vigenciac.getCodConsolidadord();
      this.codCuentac = vigenciac.getCodCuentac();
      this.codCuentad = vigenciac.getCodCuentad();
      this.empresa = vigenciac.getEmpresa();
      this.naturaleza = vigenciac.getNaturaleza();
      this.fechafinal = vigenciac.getFechafinal();
      this.fechainicial = vigenciac.getFechainicial();
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
      if (!(object instanceof VigenciasCuentas)) {
         return false;
      }
      VigenciasCuentas other = (VigenciasCuentas) object;
      if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "Entidades.VigenciasCuentas[ secuencia=" + secuencia + " ]";
   }

   public void llenarTransients(VigenciasCuentasAux vigAux) {
      this.setNombreConsolidadorc(vigAux.getNombreConsolidadorc());
      this.setNombreConsolidadord(vigAux.getNombreConsolidadord());
      this.setNombreTipocc(vigAux.getNombreTipocc());
      this.setDescripcionConcepto(vigAux.getDescripcionConcepto());
      this.setDescripcionCuentac(vigAux.getDescripcionCuentac());
      this.setDescripcionCuentad(vigAux.getDescripcionCuentad());
      this.setCodConsolidadorc(vigAux.getCodConsolidadorc());
      this.setCodConsolidadord(vigAux.getCodConsolidadord());
      this.setCodigoConcepto(vigAux.getCodigoConcepto());
      this.setCodCuentac(vigAux.getCodCuentac());
      this.setCodCuentad(vigAux.getCodCuentad());
      this.setEmpresa(vigAux.getEmpresa());
      this.setNaturaleza(vigAux.getNaturaleza());
   }

}
