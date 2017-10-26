package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "CONCEPTOS")
public class Conceptos implements Serializable {

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
   @Size(max = 1)
   @Column(name = "DESCONTABLE")
   private String descontable;
   @Column(name = "CODIGODESPRENDIBLE")
   private Integer codigodesprendible;
   @Size(max = 100)
   @Column(name = "DESCRIPCIONDESPRENDIBLE")
   private String descripciondesprendible;
   @Column(name = "ADELANTADIAS")
   private Short adelantadias;
   @Size(max = 1)
   @Column(name = "VACAADELANTAPAGO")
   private String vacaadelantapago;
   @Size(max = 1)
   @Column(name = "VACAADELANTADTO")
   private String vacaadelantadto;
   @Size(max = 10)
   @Column(name = "CONTENIDOFECHAHASTA")
   private String contenidofechahasta;
   @Size(max = 1)
   @Column(name = "INDEPENDIENTE")
   private String independiente;
   @Size(max = 1)
   @Column(name = "LIQUIDAHACIAADELANTE")
   private String liquidahaciaadelante;
   @Size(max = 1)
   @Column(name = "LIQUIDAHACIAATRAS")
   private String liquidahaciaatras;
   @Size(max = 1)
   @Column(name = "LIQUIDAULTIMOPERIODOVACA")
   private String liquidaultimoperiodovaca;
   @Size(max = 1)
   @Column(name = "LIQUIDAREGRESOPERIODOVACA")
   private String liquidaregresoperiodovaca;
   @Size(max = 1)
   @Column(name = "INDICEDEUDOR")
   private String indicedeudor;
   @Size(max = 1)
   @Column(name = "INDICEACREEDOR")
   private String indiceacreedor;
   @Column(name = "FECHACREACION")
   @Temporal(TemporalType.TIMESTAMP)
   private Date fechacreacion;
   @Column(name = "PRIORIDADSOBREGIRO")
   private Short prioridadsobregiro;
   @Size(max = 1)
   @Column(name = "ENVIOTESORERIA")
   private String enviotesoreria;
   @Size(max = 8)
   @Column(name = "ACTIVO")
   private String activo;
   @Size(max = 15)
   @Column(name = "CODIGOALTERNATIVO")
   private String codigoalternativo;
   @Size(max = 1)
   @Column(name = "GARANTIZAENVACACION")
   private String garantizaenvacacion;
   @Basic(optional = false)
   @NotNull
   @Column(name = "CODIGO")
   private BigInteger codigo;
   @Column(name = "CONJUNTO")
   private Short conjunto;
//
//   @JoinColumn(name = "UNIDAD", referencedColumnName = "SECUENCIA")
//   @ManyToOne(optional = false)
//   private Unidades unidad;
//   @JoinColumn(name = "TERCERO", referencedColumnName = "SECUENCIA")
//   @ManyToOne
//   private Terceros tercero;
//   @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
//   @ManyToOne(optional = false)
//   private Empresas empresa;
//   @JoinColumn(name = "CLAVECONTABLEDEBITO", referencedColumnName = "SECUENCIA")
//   @ManyToOne
//   private ClavesSap clavecontabledebito;
//   @JoinColumn(name = "CLAVECONTABLECREDITO", referencedColumnName = "SECUENCIA")
//   @ManyToOne
//   private ClavesSap clavecontablecredito;

   @Column(name = "UNIDAD")
   private BigInteger unidad;
   @Column(name = "TERCERO")
   private BigInteger tercero;
   @Column(name = "EMPRESA")
   private BigInteger empresa;
   @Column(name = "CLAVECONTABLEDEBITO")
   private BigInteger clavecontabledebito;
   @Column(name = "CLAVECONTABLECREDITO")
   private BigInteger clavecontablecredito;

   @Transient
   private String nombreUnidad;
   @Transient
   private String codigoUnidad;
   @Transient
   private String nombreTercero;
   @Transient
   private String nombreEmpresa;
   @Transient
   private long nitEmpresa;

   @Transient
   private String naturalezaConcepto;
   @Transient
   private String estadoConcepto;
   @Transient
   private String enviarConcepto;
   @Transient
   private boolean independienteConcepto;
   @Transient
   private String informacionConcepto;
   @Transient
   private String codigoSTR;
   @Transient
   private String infoDetalleConcepto;
   @Transient
   private String strNaturalezaDescripcion;

   public Conceptos() {
   }

   public Conceptos(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public Conceptos(BigInteger secuencia, BigInteger codigo, String descripcion, String naturaleza) {
      this.secuencia = secuencia;
      this.codigo = codigo;
      this.descripcion = descripcion;
      this.naturaleza = naturaleza;
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
      if (descripcion == null) {
         this.descripcion = descripcion;
      } else {
         this.descripcion = descripcion.toUpperCase();
      }
   }

   public String getNaturaleza() {
      return naturaleza;
   }

   public void setNaturaleza(String naturaleza) {
      this.naturaleza = naturaleza;
   }

   public String getDescontable() {
      return descontable;
   }

   public void setDescontable(String descontable) {
      this.descontable = descontable;
   }

   public Integer getCodigodesprendible() {
      return codigodesprendible;
   }

   public void setCodigodesprendible(Integer codigodesprendible) {
      this.codigodesprendible = codigodesprendible;
   }

   public String getDescripciondesprendible() {
      return descripciondesprendible;
   }

   public void setDescripciondesprendible(String descripciondesprendible) {
      this.descripciondesprendible = descripciondesprendible;
   }

   public Short getAdelantadias() {
      return adelantadias;
   }

   public void setAdelantadias(Short adelantadias) {
      this.adelantadias = adelantadias;
   }

   public String getVacaadelantapago() {
      return vacaadelantapago;
   }

   public void setVacaadelantapago(String vacaadelantapago) {
      this.vacaadelantapago = vacaadelantapago;
   }

   public String getVacaadelantadto() {
      return vacaadelantadto;
   }

   public void setVacaadelantadto(String vacaadelantadto) {
      this.vacaadelantadto = vacaadelantadto;
   }

   public String getContenidofechahasta() {
      if (contenidofechahasta == null) {
         contenidofechahasta = "FECHAPAGO";
      }
      return contenidofechahasta;
   }

   public void setContenidofechahasta(String contenidofechahasta) {
      this.contenidofechahasta = contenidofechahasta;
   }

   public String getIndependiente() {
      return independiente;
   }

   public void setIndependiente(String independiente) {

      this.independiente = independiente;
   }

   public String getLiquidahaciaadelante() {
      return liquidahaciaadelante;
   }

   public void setLiquidahaciaadelante(String liquidahaciaadelante) {
      this.liquidahaciaadelante = liquidahaciaadelante;
   }

   public String getLiquidahaciaatras() {
      return liquidahaciaatras;
   }

   public void setLiquidahaciaatras(String liquidahaciaatras) {
      this.liquidahaciaatras = liquidahaciaatras;
   }

   public String getLiquidaultimoperiodovaca() {
      return liquidaultimoperiodovaca;
   }

   public void setLiquidaultimoperiodovaca(String liquidaultimoperiodovaca) {
      this.liquidaultimoperiodovaca = liquidaultimoperiodovaca;
   }

   public String getLiquidaregresoperiodovaca() {
      return liquidaregresoperiodovaca;
   }

   public void setLiquidaregresoperiodovaca(String liquidaregresoperiodovaca) {
      this.liquidaregresoperiodovaca = liquidaregresoperiodovaca;
   }

   public String getIndicedeudor() {
      return indicedeudor;
   }

   public void setIndicedeudor(String indicedeudor) {
      this.indicedeudor = indicedeudor;
   }

   public String getIndiceacreedor() {
      return indiceacreedor;
   }

   public void setIndiceacreedor(String indiceacreedor) {
      this.indiceacreedor = indiceacreedor;
   }

   public Date getFechacreacion() {
      return fechacreacion;
   }

   public void setFechacreacion(Date fechacreacion) {
      this.fechacreacion = fechacreacion;
   }

   public Short getPrioridadsobregiro() {
      return prioridadsobregiro;
   }

   public void setPrioridadsobregiro(Short prioridadsobregiro) {
      this.prioridadsobregiro = prioridadsobregiro;
   }

   public String getEnviotesoreria() {
      return enviotesoreria;
   }

   public void setEnviotesoreria(String enviotesoreria) {
      this.enviotesoreria = enviotesoreria;
   }

   public String getActivo() {
      return activo;
   }

   public void setActivo(String activo) {
      this.activo = activo;
   }

   public String getCodigoalternativo() {
      return codigoalternativo;
   }

   public void setCodigoalternativo(String codigoalternativo) {
      this.codigoalternativo = codigoalternativo;
   }

   public String getGarantizaenvacacion() {
      return garantizaenvacacion;
   }

   public void setGarantizaenvacacion(String garantizaenvacacion) {
      this.garantizaenvacacion = garantizaenvacacion;
   }

   public BigInteger getUnidad() {
      return unidad;
   }

   public void setUnidad(BigInteger unidad) {
      this.unidad = unidad;
   }

   public BigInteger getTercero() {
      return tercero;
   }

   public void setTercero(BigInteger tercero) {
      this.tercero = tercero;
   }

   public BigInteger getEmpresa() {
      return empresa;
   }

   public void setEmpresa(BigInteger empresa) {
      this.empresa = empresa;
   }

   public BigInteger getClavecontabledebito() {
      return clavecontabledebito;
   }

   public void setClavecontabledebito(BigInteger clavecontabledebito) {
      this.clavecontabledebito = clavecontabledebito;
   }

   public BigInteger getClavecontablecredito() {
      return clavecontablecredito;
   }

   public void setClavecontablecredito(BigInteger clavecontablecredito) {
      this.clavecontablecredito = clavecontablecredito;
   }

   public String getNombreUnidad() {
      return nombreUnidad;
   }

   public void setNombreUnidad(String nombreUnidad) {
      this.nombreUnidad = nombreUnidad;
   }

   public String getNombreTercero() {
      return nombreTercero;
   }

   public void setNombreTercero(String nombreTercero) {
      this.nombreTercero = nombreTercero;
   }

   public String getNombreEmpresa() {
      return nombreEmpresa;
   }

   public void setNombreEmpresa(String nombreEmpresa) {
      this.nombreEmpresa = nombreEmpresa;
   }

   public long getNitEmpresa() {
      return nitEmpresa;
   }

   public void setNitEmpresa(long nitEmpresa) {
      this.nitEmpresa = nitEmpresa;
   }

   public String getCodigoUnidad() {
      return codigoUnidad;
   }

   public void setCodigoUnidad(String codigoUnidad) {
      this.codigoUnidad = codigoUnidad;
   }

   public void llenarTransients(ConceptosAux conceptoAux) {
      this.setNombreUnidad(conceptoAux.getNombreUnidad());
      this.setCodigoUnidad(conceptoAux.getCodigoUnidad());
      this.setNombreTercero(conceptoAux.getNombreTercero());
      this.setNombreEmpresa(conceptoAux.getNombreEmpresa());
      this.setNitEmpresa(conceptoAux.getNitEmpresa());
   }

   public String getStrNaturalezaDescripcion() {
      getNaturaleza();
      getDescripcion();
      if (descripcion != null) {
         if (naturaleza.equalsIgnoreCase("P")) {
            strNaturalezaDescripcion = "PAGO POR> " + descripcion;
         } else if (naturaleza.equalsIgnoreCase("D")) {
            strNaturalezaDescripcion = "DESCUENTO POR> " + descripcion;
         } else if (naturaleza.equalsIgnoreCase("G")) {
            strNaturalezaDescripcion = "PAGO POR> " + descripcion;
         } else if (naturaleza.equalsIgnoreCase("L")) {
            strNaturalezaDescripcion = "PASIVO POR> " + descripcion;
         } else {
            strNaturalezaDescripcion = "OTROS POR> " + descripcion;
         }
      } else {
         strNaturalezaDescripcion = "OTROS POR> " + "";
      }
      return strNaturalezaDescripcion;
   }

   public void setStrNaturalezaDescripcion(String strNaturalezaDescripcion) {
      this.strNaturalezaDescripcion = strNaturalezaDescripcion;
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

   public String getEstadoConcepto() {
      if (estadoConcepto == null) {
         if (activo == null) {
            estadoConcepto = "ACTIVO";
         } else if (activo.equalsIgnoreCase("S")) {
            estadoConcepto = "ACTIVO";
         } else if (activo.equalsIgnoreCase("N")) {
            estadoConcepto = "INACTIVO";
         }
      }
      return estadoConcepto;
   }

   public void setEstadoConcepto(String estadoConcepto) {
      this.estadoConcepto = estadoConcepto;
   }

   public String getEnviarConcepto() {
      if (enviarConcepto == null) {
         if (enviotesoreria == null) {
            enviarConcepto = "SI";
         } else if (enviotesoreria.equalsIgnoreCase("S")) {
            enviarConcepto = "SI";
         } else if (enviotesoreria.equalsIgnoreCase("N")) {
            enviarConcepto = "NO";
         }
      }
      return enviarConcepto;
   }

   public void setEnviarConcepto(String enviarConcepto) {
      this.enviarConcepto = enviarConcepto;
   }

   public boolean isIndependienteConcepto() {
      if (independienteConcepto == false) {
         if (independiente == null) {
            independienteConcepto = false;
         } else if (independiente.equalsIgnoreCase("S")) {
            independienteConcepto = true;
         } else if (independiente.equalsIgnoreCase("N")) {
            independienteConcepto = false;
         }
      }
      return independienteConcepto;
   }

   public void setIndependienteConcepto(boolean independienteConcepto) {
      this.independienteConcepto = independienteConcepto;
   }

   public String getInformacionConcepto() {
      if (informacionConcepto == null) {
         if (codigo != null && descripcion != null) {
            informacionConcepto = codigo + " - " + descripcion;
         }
      }
      return informacionConcepto;
   }

   public void setInformacionConcepto(String informacionConcepto) {
      this.informacionConcepto = informacionConcepto;
   }

   public String getCodigoSTR() {
      if (codigo != null) {
         codigoSTR = codigo.toString();
      } else {
         codigoSTR = "0";
         codigo = null;
      }
      return codigoSTR;
   }

   public void setCodigoSTR(String codigoSTR) {
      codigo = new BigInteger(codigoSTR);
      this.codigoSTR = codigoSTR;
   }

   public String getInfoDetalleConcepto() {
      getNaturalezaConcepto();
      if (codigo != null && descripcion != null && naturalezaConcepto != null) {
//            infoDetalleConcepto = codigo + " - " + descripcion + " - " + naturalezaConcepto + " / Tiene comportamiento: ";
         infoDetalleConcepto = codigo + " - " + descripcion + " - " + naturalezaConcepto + " / ";
      } else {
         infoDetalleConcepto = "";
      }
      return infoDetalleConcepto;
   }

   public void setInfoDetalleConcepto(String infoDetalleConcepto) {
      this.infoDetalleConcepto = infoDetalleConcepto;
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

   @Override
   public int hashCode() {
      int hash = 0;
      hash += (secuencia != null ? secuencia.hashCode() : 0);
      return hash;
   }

   @Override
   public boolean equals(Object object) {
      // TODO: Warning - this method won't work in the case the id fields are not set
      if (!(object instanceof Conceptos)) {
         return false;
      }
      Conceptos other = (Conceptos) object;
      if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "Entidades.Conceptos[ secuencia=" + secuencia + " ]";
   }

}
