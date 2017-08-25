package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrator
 */
@Entity
@Cacheable(false)
public class VWActualesTiposTrabajadores implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @Basic(optional = false)
   @NotNull
   @Column(name = "SECUENCIA")
   private BigInteger secuencia;
   @Column(name = "EMPLEADO")
   private BigInteger empleado;
   @Basic(optional = false)
   @NotNull
   @Column(name = "FECHAVIGENCIA")
   @Temporal(TemporalType.DATE)
   private Date fechaVigencia;
   @JoinColumn(name = "TIPOTRABAJADOR", referencedColumnName = "SECUENCIA")
   @ManyToOne(optional = false)
   private TiposTrabajadores tipoTrabajador;
   @Column(name = "FECHARETIRO")
   @Temporal(TemporalType.DATE)
   private Date fechaRetiro;
   @Column(name = "MOTIVORETIRO")
   private BigInteger motivoRetiro;

   @Transient
   private String retencionysegsocxpersonaEmpresa;
   @Transient
   private BigInteger numeroDocumentoEmpleado;
   @Transient
   private BigDecimal codigoEmpleado;
   @Transient
   private BigInteger persona;
   @Transient
   private String nombrePersona;
   @Transient
   private String primerApellidoPersona;
   @Transient
   private String segundoApellidoPersona;

   @Transient
   private String nombreCompleto;

   public BigInteger getSecuencia() {
      return secuencia;
   }

   public void setSecuencia(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public Date getFechaVigencia() {
      return fechaVigencia;
   }

   public void setFechaVigencia(Date fechaVigencia) {
      this.fechaVigencia = fechaVigencia;
   }

   public TiposTrabajadores getTipoTrabajador() {
      return tipoTrabajador;
   }

   public void setTipoTrabajador(TiposTrabajadores tipoTrabajador) {
      this.tipoTrabajador = tipoTrabajador;
   }

   public Date getFechaRetiro() {
      return fechaRetiro;
   }

   public void setFechaRetiro(Date fechaRetiro) {
      this.fechaRetiro = fechaRetiro;
   }

   public BigInteger getMotivoRetiro() {
      return motivoRetiro;
   }

   public void setMotivoRetiro(BigInteger motivoRetiro) {
      this.motivoRetiro = motivoRetiro;
   }

   public BigInteger getEmpleado() {
      return empleado;
   }

   public void setEmpleado(BigInteger empleado) {
      this.empleado = empleado;
   }

   public String getRetencionysegsocxpersonaEmpresa() {
      return retencionysegsocxpersonaEmpresa;
   }

   public void setRetencionysegsocxpersonaEmpresa(String retencionysegsocxpersonaEmpresa) {
      this.retencionysegsocxpersonaEmpresa = retencionysegsocxpersonaEmpresa;
   }

   public BigInteger getNumeroDocumentoEmpleado() {
      return numeroDocumentoEmpleado;
   }

   public void setNumeroDocumentoEmpleado(BigInteger numeroDocumentoEmpleado) {
      this.numeroDocumentoEmpleado = numeroDocumentoEmpleado;
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
         return segundoApellidoPersona;
      }
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

   public void llenarTransients(VWActualesTiposTrabajadoresAux vwActualTTAux) {
      this.retencionysegsocxpersonaEmpresa = vwActualTTAux.getRetencionysegsocxpersonaEmpresa();
      this.numeroDocumentoEmpleado = vwActualTTAux.getNumeroDocumentoEmpleado();
      this.codigoEmpleado = vwActualTTAux.getCodigoEmpleado();
      this.persona = vwActualTTAux.getPersona();
      this.nombrePersona = vwActualTTAux.getNombrePersona();
      this.primerApellidoPersona = vwActualTTAux.getPrimerApellidoPersona();
      this.segundoApellidoPersona = vwActualTTAux.getSegundoApellidoPersona();
   }
}
