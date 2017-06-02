package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "VIGENCIASARPS")
public class VigenciasArps implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @Basic(optional = false)
   @NotNull
   @Column(name = "SECUENCIA")
   private BigInteger secuencia;
   @Basic(optional = false)
   @NotNull
   @Column(name = "FECHAINICIAL")
   @Temporal(TemporalType.TIMESTAMP)
   private Date fechainicial;
   @Basic(optional = false)
   @NotNull
   @Column(name = "FECHAFINAL")
   @Temporal(TemporalType.TIMESTAMP)
   private Date fechafinal;
   @Basic(optional = false)
   @NotNull
   @Column(name = "PORCENTAJE")
   private BigDecimal porcentaje;
   @Column(name = "ESTRUCTURA")
   private BigInteger estructura;
   @Column(name = "CLASERIESGO")
   private BigInteger clasesRiesgos;
   @Column(name = "CARGO")
   private BigInteger cargo;
   @Transient
   private String nombreEstructura;
   @Transient
   private String nombreCargo;

   public VigenciasArps() {
   }

   public VigenciasArps(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public VigenciasArps(BigInteger secuencia, Date fechainicial, Date fechafinal, BigDecimal porcentaje) {
      this.secuencia = secuencia;
      this.fechainicial = fechainicial;
      this.fechafinal = fechafinal;
      this.porcentaje = porcentaje;
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

   public BigDecimal getPorcentaje() {
      return porcentaje;
   }

   public void setPorcentaje(BigDecimal porcentaje) {
      this.porcentaje = porcentaje;
   }

   public BigInteger getEstructura() {
      return estructura;
   }

   public void setEstructura(BigInteger estructura) {
      this.estructura = estructura;
   }

   public BigInteger getClasesRiesgos() {
      return clasesRiesgos;
   }

   public void setClasesRiesgos(BigInteger clasesRiesgos) {
      this.clasesRiesgos = clasesRiesgos;
   }

   public BigInteger getCargo() {
      return cargo;
   }

   public void setCargo(BigInteger cargo) {
      this.cargo = cargo;
   }

   public String getNombreEstructura() {
      return nombreEstructura;
   }

   public void setNombreEstructura(String nombreEstructura) {
      this.nombreEstructura = nombreEstructura;
   }

   public String getNombreCargo() {
      return nombreCargo;
   }

   public void setNombreCargo(String nombreCargo) {
      this.nombreCargo = nombreCargo;
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
      if (!(object instanceof VigenciasArps)) {
         return false;
      }
      VigenciasArps other = (VigenciasArps) object;
      if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "Entidades.VigenciasArps[ secuencia=" + secuencia + " ]";
   }

}
