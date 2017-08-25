package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "EMPLEADOS")
public class Empleados implements Serializable {

   private static final long serialVersionUID = 1L;
//    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "STABLAS")
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
   @Id
   @Basic(optional = false)
   @NotNull
   @Column(name = "SECUENCIA")
   private BigInteger secuencia;
   @Column(name = "CODIGOEMPLEADO")
   private BigDecimal codigoempleado;
   @Column(name = "RUTATRANSPORTE")
   private Integer rutatransporte;
   @Size(max = 6)
   @Column(name = "PARQUEADERO")
   private String parqueadero;
   @Column(name = "CODIGOALTERNATIVODEUDOR")
   private Integer codigoalternativodeudor;
   @Column(name = "CODIGOALTERNATIVOACREEDOR")
   private Long codigoalternativoacreedor;
   @Column(name = "FECHACREACION")
   @Temporal(TemporalType.TIMESTAMP)
   private Date fechacreacion;
   @Size(max = 20)
   @Column(name = "CODIGOALTERNATIVO")
   private String codigoalternativo;
   @Size(max = 1)
   @Column(name = "PAGASUBSIDIOTRANSPORTELEGAL")
   private String pagasubsidiotransportelegal;
   @Size(max = 30)
   @Column(name = "USUARIOBD")
   private String usuariobd;
   @Column(name = "EMPRESA")
   private BigInteger empresa;
   @Column(name = "PERSONA")
   private BigInteger persona;

   @Transient
   private String pathfotoPersona;
   @Transient
   private String emailPersona;
   @Transient
   private BigInteger numeroDocumentoPersona;
   @Transient
   private String nombrePersona;
   @Transient
   private String primerApellidoPersona;
   @Transient
   private String segundoApellidoPersona;
   @Transient
   private String nombreEmpresa;
   @Transient
   private String retencionysegsocxpersonaEmpresa;
   //
   @Transient
   private String nombreCompleto;
   @Transient
   private String estado;
   @Transient
   private String codigoempleadoSTR;
   @Transient
   private String strNumeroDocumento;

   public Empleados() {
   }

   public BigInteger getSecuencia() {
      return secuencia;
   }

   public void setSecuencia(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public BigInteger getPersona() {
      return persona;
   }

   public void setPersona(BigInteger persona) {
      this.persona = persona;
   }

   public void setPersona(Personas per) {
      this.persona = per.getSecuencia();
      this.nombrePersona = per.getNombre();
      this.pathfotoPersona = per.getPathfoto();
      this.emailPersona = per.getEmail();
      this.numeroDocumentoPersona = per.getNumerodocumento();
      this.primerApellidoPersona = per.getPrimerapellido();
      this.segundoApellidoPersona = per.getSegundoapellido();
   }

   public BigDecimal getCodigoempleado() {
      return codigoempleado;
   }

   public void setCodigoempleado(BigDecimal codigoempleado) {
      this.codigoempleado = codigoempleado;
   }

   public Integer getRutatransporte() {
      return rutatransporte;
   }

   public void setRutatransporte(Integer rutatransporte) {
      this.rutatransporte = rutatransporte;
   }

   public String getParqueadero() {
      return parqueadero;
   }

   public void setParqueadero(String parqueadero) {
      this.parqueadero = parqueadero;
   }

   public Integer getCodigoalternativodeudor() {
      return codigoalternativodeudor;
   }

   public void setCodigoalternativodeudor(Integer codigoalternativodeudor) {
      this.codigoalternativodeudor = codigoalternativodeudor;
   }

   public Long getCodigoalternativoacreedor() {
      return codigoalternativoacreedor;
   }

   public void setCodigoalternativoacreedor(Long codigoalternativoacreedor) {
      this.codigoalternativoacreedor = codigoalternativoacreedor;
   }

   public Date getFechacreacion() {
      return fechacreacion;
   }

   public void setFechacreacion(Date fechacreacion) {
      this.fechacreacion = fechacreacion;
   }

   public String getCodigoalternativo() {
      return codigoalternativo;
   }

   public void setCodigoalternativo(String codigoalternativo) {
      this.codigoalternativo = codigoalternativo;
   }

   /*
     public BigInteger getTemptotalingresos() {
     return temptotalingresos;
     }

     public void setTemptotalingresos(BigInteger temptotalingresos) {
     this.temptotalingresos = temptotalingresos;
     }

     public String getExtranjero() {
     return extranjero;
     }

     public void setExtranjero(String extranjero) {
     this.extranjero = extranjero;
     }
    */
   public String getPagasubsidiotransportelegal() {
      return pagasubsidiotransportelegal;
   }

   public void setPagasubsidiotransportelegal(String pagasubsidiotransportelegal) {
      this.pagasubsidiotransportelegal = pagasubsidiotransportelegal;
   }

   /*
     public BigInteger getTempbaserecalculo() {
     return tempbaserecalculo;
     }

     public void setTempbaserecalculo(BigInteger tempbaserecalculo) {
     this.tempbaserecalculo = tempbaserecalculo;
     }
    */
 /*     
     public BigInteger getEmpresa() {
     return empresa;
     }

     public void setEmpresa(BigInteger empresafk) {
     this.empresa = empresa;
     }
    */
   public String getEstado() {
      return estado;
   }

   public void setEstado(String estado) {
      this.estado = estado;
   }

   public String getCodigoempleadoSTR() {
      getCodigoempleado();
      if (codigoempleado != null) {
         codigoempleadoSTR = codigoempleado.toString();
      } else {
         codigoempleadoSTR = " ";
         codigoempleado = BigDecimal.valueOf(0);

      }
      return codigoempleadoSTR;
   }

   public void setCodigoempleadoSTR(String codigoempleadoSTR) {
      codigoempleado = new BigDecimal(codigoempleadoSTR);
      this.codigoempleadoSTR = codigoempleadoSTR;
   }

   public String getUsuariobd() {
      return usuariobd;
   }

   public void setUsuariobd(String usuariobd) {
      this.usuariobd = usuariobd;
   }

   public BigInteger getEmpresa() {
      return empresa;
   }

   public void setEmpresa(BigInteger empresa) {
      this.empresa = empresa;
   }

   public BigInteger getNumeroDocumentoPersona() {
      return numeroDocumentoPersona;
   }

   public void setNumeroDocumentoPersona(BigInteger numeroDocumentoPersona) {
      this.numeroDocumentoPersona = numeroDocumentoPersona;
   }

   public String getNombrePersona() {
      if (nombrePersona == null) {
         return "";
      } else {
         return nombrePersona;
      }
   }

   public void setNombrePersona(String nombrePersona) {
      this.nombrePersona = nombrePersona;
   }

   public String getPrimerApellidoPersona() {
      if (primerApellidoPersona == null) {
         return "";
      } else {
         return primerApellidoPersona;
      }
   }

   public void setPrimerApellidoPersona(String primerApellidoPersona) {
      this.primerApellidoPersona = primerApellidoPersona;
   }

   public String getSegundoApellidoPersona() {
      if (segundoApellidoPersona == null) {
         return "";
      } else {
      }
      return segundoApellidoPersona;
   }

   public void setSegundoApellidoPersona(String segundoApellidoPersona) {
      this.segundoApellidoPersona = segundoApellidoPersona;
   }

   public String getNombreCompleto() {
      if (nombreCompleto == null) {
         nombreCompleto = getPrimerApellidoPersona() + " " + getSegundoApellidoPersona() + " " + getNombrePersona();
         if (nombreCompleto.equals("  ")) {
            nombreCompleto = null;
         }
      }
      return nombreCompleto;
   }

   public void setNombreCompleto(String nombreCompleto) {
      this.nombreCompleto = nombreCompleto;
   }

   public String getNombreEmpresa() {
      return nombreEmpresa;
   }

   public void setNombreEmpresa(String nombreEmpresa) {
      this.nombreEmpresa = nombreEmpresa;
   }

   public String getPathfotoPersona() {
      return pathfotoPersona;
   }

   public void setPathfotoPersona(String pathfotoPersona) {
      this.pathfotoPersona = pathfotoPersona;
   }

   public String getEmailPersona() {
      return emailPersona;
   }

   public void setEmailPersona(String emailPersona) {
      this.emailPersona = emailPersona;
   }

   public String getRetencionysegsocxpersonaEmpresa() {
      return retencionysegsocxpersonaEmpresa;
   }

   public void setRetencionysegsocxpersonaEmpresa(String retencionysegsocxpersonaEmpresa) {
      this.retencionysegsocxpersonaEmpresa = retencionysegsocxpersonaEmpresa;
   }

   public String getStrNumeroDocumento() {
      if (strNumeroDocumento == null) {
         strNumeroDocumento = "" + numeroDocumentoPersona;
      }
      return strNumeroDocumento;
   }

   public void setStrNumeroDocumento(String strNumeroDocumento) {
      this.strNumeroDocumento = strNumeroDocumento;
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
      if (!(object instanceof Empleados)) {
         return false;
      }
      Empleados other = (Empleados) object;
      if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "Entidades.Empleados[ secuencia = " + secuencia + " ]";
   }

   public void llenarTransients(EmpleadosAux empleadoAux) {
      this.nombrePersona = empleadoAux.getNombrePersona();
      this.pathfotoPersona = empleadoAux.getPathfotoPersona();
      this.emailPersona = empleadoAux.getEmailPersona();
      this.numeroDocumentoPersona = empleadoAux.getNumeroDocumentoPersona();
      this.primerApellidoPersona = empleadoAux.getPrimerApellidoPersona();
      this.segundoApellidoPersona = empleadoAux.getSegundoApellidoPersona();
      this.nombreEmpresa = empleadoAux.getNombreEmpresa();
      this.retencionysegsocxpersonaEmpresa = empleadoAux.getRetencionysegsocxpersonaEmpresa();
   }

}
