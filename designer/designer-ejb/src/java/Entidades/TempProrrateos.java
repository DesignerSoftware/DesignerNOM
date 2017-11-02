/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
public class TempProrrateos implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @Basic(optional = false)
   @NotNull
   @Column(name = "SECUENCIA")
   private BigInteger secuencia;
   @Column(name = "CODIGOEMPLEADO")
   @Size(max = 15)
   private String empleado;
   @Column(name = "CENTROCOSTO")
   @Size(max = 20)
   private String centroCosto;
   @Column(name = "FECHAINICIAL")
   private Date fechaInicial;
   @Column(name = "FECHAFINAL")
   private Date FechaFinal;
   @Column(name = "PORCENTAJE")
   private BigDecimal porcentaje;
   @Column(name = "FECHASISTEMA")
   private Date fechaSistema;
   @Column(name = "TERMINAL")
   @Size(max = 50)
   private String terminal;
   @Column(name = "USUARIOBD")
   @Size(max = 30)
   private String usuarioBD;
   @Column(name = "CODIGOEMPLEADOCARGUE")
   @Size(max = 30)
   private String codigoEmpleadoCargue;
   @Column(name = "ARCHIVO")
   @Size(max = 30)
   private String archivo;
   @Column(name = "ESTADO")
   @Size(max = 1)
   private String estado;
   @Column(name = "CAUSARECHAZO")
   @Size(max = 100)
   private String causaRechazo;
   @Column(name = "VIGLOCALIZACION")
   private BigInteger vigLocalizacion;
   @Column(name = "SUBPORCENTAJE")
   private BigDecimal subPorcentaje;
   @Column(name = "PROYECTO")
   @Size(max = 20)
   private String proyecto;
   @Column(name = "OBSERVACION")
   @Size(max = 100)
   private String observacion;

   @Transient
   private String nombreEmpleado;
   @Transient
   private String nombreProyecto;
   @Transient
   private String nombreCentrocosto;
   @Transient
   private String codigoProyecto;
   @Transient
   private BigInteger codigoEmpleado;
   @Transient
   private BigInteger codigoCentrocosto;

   public BigInteger getSecuencia() {
      return secuencia;
   }

   public void setSecuencia(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public String getEmpleado() {
      return empleado;
   }

   public void setEmpleado(String empleado) {
      this.empleado = empleado;
   }

   public String getCentroCosto() {
      return centroCosto;
   }

   public void setCentroCosto(String centroCosto) {
      this.centroCosto = centroCosto;
   }

   public Date getFechaInicial() {
      return fechaInicial;
   }

   public void setFechaInicial(Date fechaInicial) {
      this.fechaInicial = fechaInicial;
   }

   public Date getFechaFinal() {
      return FechaFinal;
   }

   public void setFechaFinal(Date FechaFinal) {
      this.FechaFinal = FechaFinal;
   }

   public BigDecimal getPorcentaje() {
      return porcentaje;
   }

   public void setPorcentaje(BigDecimal porcentaje) {
      this.porcentaje = porcentaje;
   }

   public Date getFechaSistema() {
      return fechaSistema;
   }

   public void setFechaSistema(Date fechaSistema) {
      this.fechaSistema = fechaSistema;
   }

   public String getTerminal() {
      return terminal;
   }

   public void setTerminal(String terminal) {
      this.terminal = terminal;
   }

   public String getUsuarioBD() {
      return usuarioBD;
   }

   public void setUsuarioBD(String usuarioBD) {
      this.usuarioBD = usuarioBD;
   }

   public String getCodigoEmpleadoCargue() {
      return codigoEmpleadoCargue;
   }

   public void setCodigoEmpleadoCargue(String codigoEmpleadoCargue) {
      this.codigoEmpleadoCargue = codigoEmpleadoCargue;
   }

   public String getArchivo() {
      return archivo;
   }

   public void setArchivo(String archivo) {
      this.archivo = archivo;
   }

   public String getEstado() {
      return estado;
   }

   public void setEstado(String estado) {
      this.estado = estado;
   }

   public String getCausaRechazo() {
      return causaRechazo;
   }

   public void setCausaRechazo(String causaRechazo) {
      this.causaRechazo = causaRechazo;
   }

   public BigInteger getVigLocalizacion() {
      return vigLocalizacion;
   }

   public void setVigLocalizacion(BigInteger vigLocalizacion) {
      this.vigLocalizacion = vigLocalizacion;
   }

   public BigDecimal getSubPorcentaje() {
      return subPorcentaje;
   }

   public void setSubPorcentaje(BigDecimal subPorcentaje) {
      this.subPorcentaje = subPorcentaje;
   }

   public String getProyecto() {
      return proyecto;
   }

   public void setProyecto(String proyecto) {
      this.proyecto = proyecto;
   }

   public String getObservacion() {
      return observacion;
   }

   public void setObservacion(String observacion) {
      this.observacion = observacion;
   }

   public String getNombreEmpleado() {
      return nombreEmpleado;
   }

   public void setNombreEmpleado(String nombreEmpleado) {
      this.nombreEmpleado = nombreEmpleado;
   }

   public String getNombreProyecto() {
      return nombreProyecto;
   }

   public void setNombreProyecto(String nombreProyecto) {
      this.nombreProyecto = nombreProyecto;
   }

   public String getNombreCentrocosto() {
      return nombreCentrocosto;
   }

   public void setNombreCentrocosto(String nombreCentrocosto) {
      this.nombreCentrocosto = nombreCentrocosto;
   }

   public String getCodigoProyecto() {
      if (this.proyecto != null && codigoProyecto == null) {
         codigoProyecto = new String(proyecto);
      }
      return codigoProyecto;
   }

   public void setCodigoProyecto(String codigoProy) {
      if (codigoProy != null) {
         this.proyecto = codigoProy;
      } else {
         this.proyecto = null;
      }
      this.codigoProyecto = codigoProy;
   }

   public BigInteger getCodigoEmpleado() {
      if (this.empleado != null && codigoEmpleado == null) {
         codigoEmpleado = new BigInteger(empleado);
      }
      return codigoEmpleado;
   }

   public void setCodigoEmpleado(BigInteger codigoEmpl) {
      if (codigoEmpl != null) {
         this.empleado = codigoEmpl.toString();
      } else {
         this.empleado = null;
      }
      this.codigoEmpleado = codigoEmpleado;
   }

   public BigInteger getCodigoCentrocosto() {
      if (this.centroCosto != null && codigoCentrocosto == null) {
         codigoCentrocosto = new BigInteger(centroCosto);
      }
      return codigoCentrocosto;
   }

   public void setCodigoCentrocosto(BigInteger codigoCC) {
      if (codigoCC != null) {
         this.centroCosto = codigoCC.toString();
      } else {
         this.centroCosto = null;
      }
      this.codigoCentrocosto = codigoCentrocosto;
   }

   @Override
   public int hashCode() {
      int hash = 0;
      hash += (secuencia != null ? secuencia.hashCode() : 0);
      return hash;
   }

   @Override
   public boolean equals(Object object) {
      // TODO: Warning - this method won't work in the case the secuencia fields are not set
      if (!(object instanceof TempProrrateos)) {
         return false;
      }
      TempProrrateos other = (TempProrrateos) object;
      if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "Entidades.Tempprorrateos[ id=" + secuencia + " ]";
   }

}
