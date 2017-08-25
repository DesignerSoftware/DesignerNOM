package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "PARAMETROS")
public class Parametros implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @Basic(optional = false)
   @NotNull
   @Column(name = "SECUENCIA")
   private BigInteger secuencia;
   @Basic(optional = false)
   @NotNull
   @Column(name = "FECHAHASTACAUSADO")
   @Temporal(TemporalType.TIMESTAMP)
   private Date fechahastacausado;
   @Basic(optional = false)
   @NotNull
   @Column(name = "FECHADESDECAUSADO")
   @Temporal(TemporalType.TIMESTAMP)
   private Date fechadesdecausado;
   @Column(name = "FECHASISTEMA")
   @Temporal(TemporalType.TIMESTAMP)
   private Date fechasistema;
   @JoinColumn(name = "USUARIO", referencedColumnName = "SECUENCIA")
   @ManyToOne
   private Usuarios usuario;
   @JoinColumn(name = "PROCESO", referencedColumnName = "SECUENCIA")
   @ManyToOne(optional = false)
   private Procesos proceso;
   @JoinColumn(name = "PARAMETROESTRUCTURA", referencedColumnName = "SECUENCIA")
   @ManyToOne
   private ParametrosEstructuras parametroestructura;
//    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
//    @ManyToOne(optional = false)
//    private Empleados empleado;
   @Column(name = "EMPLEADO")
   private BigInteger empleado;

   @Transient
   private BigDecimal codigoEmpleado;
   @Transient
   private BigInteger persona;
   @Transient
   private String nombrePersona;
   @Transient
   private String emailPersona;
   @Transient
   private String primerApellidoPersona;
   @Transient
   private String segundoApellidoPersona;

   @Transient
   private String nombreCompleto;
   @Transient
   private String estadoParametro;
   @Transient
   private String fechasParametros;

   public Parametros() {
   }

   public Parametros(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public Parametros(BigInteger secuencia, Date fechahastacausado, Date fechadesdecausado) {
      this.secuencia = secuencia;
      this.fechahastacausado = fechahastacausado;
      this.fechadesdecausado = fechadesdecausado;
   }

   public BigInteger getSecuencia() {
      return secuencia;
   }

   public void setSecuencia(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public Date getFechahastacausado() {
      return fechahastacausado;
   }

   public void setFechahastacausado(Date fechahastacausado) {
      this.fechahastacausado = fechahastacausado;
   }

   public Date getFechadesdecausado() {
      return fechadesdecausado;
   }

   public void setFechadesdecausado(Date fechadesdecausado) {
      this.fechadesdecausado = fechadesdecausado;
   }

   public Date getFechasistema() {
      return fechasistema;
   }

   public void setFechasistema(Date fechasistema) {
      this.fechasistema = fechasistema;
   }

   public Procesos getProceso() {
      return proceso;
   }

   public void setProceso(Procesos proceso) {
      this.proceso = proceso;
   }

   public ParametrosEstructuras getParametroestructura() {
      return parametroestructura;
   }

   public void setParametroestructura(ParametrosEstructuras parametroestructura) {
      this.parametroestructura = parametroestructura;
   }

//    public Empleados getEmpleado() {
//        return empleado;
//    }
//
//    public void setEmpleado(Empleados empleado) {
//        this.empleado = empleado;
//    }
   public String getEstadoParametro() {
      return estadoParametro;
   }

   public void setEstadoParametro(String estadoParametro) {
      this.estadoParametro = estadoParametro;
   }

   public String getFechasParametros() {
      if (fechasParametros == null && fechadesdecausado != null && fechahastacausado != null) {
         SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
         fechasParametros = formatoFecha.format(fechadesdecausado) + " - " + formatoFecha.format(fechahastacausado);
      }
      return fechasParametros;
   }

   public void setFechasParametros(String fechasParametros) {
      this.fechasParametros = fechasParametros;
   }

   public Usuarios getUsuario() {
      return usuario;
   }

   public void setUsuario(Usuarios usuario) {
      this.usuario = usuario;
   }

   public BigInteger getEmpleado() {
      return empleado;
   }

   public void setEmpleado(BigInteger empleado) {
      this.empleado = empleado;
   }

   public BigDecimal getCodigoEmpleado() {
      return codigoEmpleado;
   }

   public void setCodigoEmpleado(BigDecimal codigoEmpleado) {
      this.codigoEmpleado = codigoEmpleado;
   }

   public BigInteger getPersona() {
      return persona;
   }

   public void setPersona(BigInteger persona) {
      this.persona = persona;
   }

   public String getNombrePersona() {
      if (nombrePersona == null) {
         nombrePersona = "";
      }
      return nombrePersona;
   }

   public void setNombrePersona(String nombrePersona) {
      this.nombrePersona = nombrePersona;
   }

   public String getPrimerApellidoPersona() {
      if (primerApellidoPersona == null) {
         primerApellidoPersona = "";
      }
      return primerApellidoPersona;
   }

   public void setPrimerApellidoPersona(String primerApellidoPersona) {
      this.primerApellidoPersona = primerApellidoPersona;
   }

   public String getSegundoApellidoPersona() {
      if (segundoApellidoPersona == null) {
         segundoApellidoPersona = "";
      }
      return segundoApellidoPersona;
   }

   public void setSegundoApellidoPersona(String segundoApellidoPersona) {
      this.segundoApellidoPersona = segundoApellidoPersona;
   }

   public String getEmailPersona() {
      return emailPersona;
   }

   public void setEmailPersona(String emailPersona) {
      this.emailPersona = emailPersona;
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

   @Override
   public int hashCode() {
      int hash = 0;
      hash += (secuencia != null ? secuencia.hashCode() : 0);
      return hash;
   }

   @Override
   public boolean equals(Object object) {
      // TODO: Warning - this method won't work in the case the id fields are not set
      if (!(object instanceof Parametros)) {
         return false;
      }
      Parametros other = (Parametros) object;
      if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "Entidades.Parametros[ secuencia=" + secuencia + " ]";
   }

   public void llenarTransients(ParametrosAux parametroAux) {
      this.codigoEmpleado = parametroAux.getCodigoEmpleado();
      this.persona = parametroAux.getPersona();
      this.nombrePersona = parametroAux.getNombrePersona();
      this.primerApellidoPersona = parametroAux.getPrimerApellidoPersona();
      this.segundoApellidoPersona = parametroAux.getSegundoApellidoPersona();
   }
}
