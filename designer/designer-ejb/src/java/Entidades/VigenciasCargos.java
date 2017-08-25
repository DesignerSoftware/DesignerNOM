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
@Table(name = "VIGENCIASCARGOS")
public class VigenciasCargos implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @Basic(optional = false)
   @NotNull
   @Column(name = "SECUENCIA")
   private BigInteger secuencia;
   @Basic(optional = false)
   @NotNull
   @Column(name = "FECHAVIGENCIA")
   @Temporal(TemporalType.DATE)
   private Date fechavigencia;
   @Size(max = 1)
   @Column(name = "TURNOROTATIVO")
   private String turnorotativo;
   @Size(max = 1)
   @Column(name = "LIQUIDAHE")
   private String liquidahe;
   @Column(name = "CALIFICACION")
   private Short calificacion;
   @JoinColumn(name = "PAPEL", referencedColumnName = "SECUENCIA")
   @ManyToOne
   private Papeles papel;
   @JoinColumn(name = "MOTIVOCAMBIOCARGO", referencedColumnName = "SECUENCIA")
   @ManyToOne(optional = false)
   private MotivosCambiosCargos motivocambiocargo;
   @JoinColumn(name = "ESTRUCTURA", referencedColumnName = "SECUENCIA")
   @ManyToOne(optional = false)
   private Estructuras estructura;
   @JoinColumn(name = "ESCALAFON", referencedColumnName = "SECUENCIA")
   @ManyToOne
   private Escalafones escalafon;
   @JoinColumn(name = "EMPLEADOJEFE", referencedColumnName = "SECUENCIA")
   @ManyToOne(optional = true)
   private Empleados empleadojefe;
   @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
   @ManyToOne(optional = false)
   private Empleados empleado;
   @JoinColumn(name = "CARGO", referencedColumnName = "SECUENCIA")
   @ManyToOne(optional = false)
   private Cargos cargo;
   @JoinColumn(name = "CLASERIESGO", referencedColumnName = "SECUENCIA")
   @ManyToOne(optional = false)
   private ClasesRiesgos claseRiesgo;
   @Transient
   private String descClase;
   @Transient
   private String nombreEmplJefe;
   @Transient
   private String nombrePapel;

   public VigenciasCargos() {
   }

   public VigenciasCargos(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public VigenciasCargos(BigInteger secuencia, Date fechavigencia) {
      this.secuencia = secuencia;
      this.fechavigencia = fechavigencia;
   }

   public BigInteger getSecuencia() {
      return secuencia;
   }

   public void setSecuencia(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public Date getFechavigencia() {
      return fechavigencia;
   }

   public void setFechavigencia(Date fechavigencia) {
      this.fechavigencia = fechavigencia;
   }

   public String getTurnorotativo() {
      return turnorotativo;
   }

   public void setTurnorotativo(String turnorotativo) {
      this.turnorotativo = turnorotativo;
   }

   public String getLiquidahe() {
      return liquidahe;
   }

   public void setLiquidahe(String liquidahe) {
      this.liquidahe = liquidahe;
   }

   public Short getCalificacion() {
      return calificacion;
   }

   public void setCalificacion(Short calificacion) {
      this.calificacion = calificacion;
   }

   public MotivosCambiosCargos getMotivocambiocargo() {
      return motivocambiocargo;
   }

   public void setMotivocambiocargo(MotivosCambiosCargos motivocambiocargo) {
      this.motivocambiocargo = motivocambiocargo;
   }

   public Estructuras getEstructura() {
      return estructura;
   }

   public void setEstructura(Estructuras estructura) {
      this.estructura = estructura;
   }

   public Escalafones getEscalafon() {
      return escalafon;
   }

   public void setEscalafon(Escalafones escalafon) {
      this.escalafon = escalafon;
   }

   public Empleados getEmpleadojefe() {
      return empleadojefe;
   }

   public void setEmpleadojefe(Empleados empleadojefe) {
      this.empleadojefe = empleadojefe;
   }

   public Empleados getEmpleado() {
      return empleado;
   }

   public void setEmpleado(Empleados empleado) {
      this.empleado = empleado;
   }

   public Cargos getCargo() {
      return cargo;
   }

   public void setCargo(Cargos cargo) {
      this.cargo = cargo;
   }

   public ClasesRiesgos getClaseRiesgo() {
      return claseRiesgo;
   }

   public void setClaseRiesgo(ClasesRiesgos claseRiesgo) {
      this.claseRiesgo = claseRiesgo;
   }

   public String getDescClase() {
      getClaseRiesgo();
      if (claseRiesgo == null) {
         descClase = "";
      } else if (claseRiesgo.getPorcentaje() != null && claseRiesgo.getDescripcion() != null) {
         descClase = claseRiesgo.getPorcentaje().toString() + " - " + claseRiesgo.getDescripcion();
      }
      return descClase;
   }

   public void setDescClase(String descClase) {
      if (descClase == null) {
         setClaseRiesgo(null);
      } else if (descClase.equals("") || descClase.equals(" ") || descClase.isEmpty()) {
         setClaseRiesgo(null);
      }
      this.descClase = descClase;
   }

   public String getNombreEmplJefe() {
      getEmpleadojefe();
      if (empleadojefe == null) {
         nombreEmplJefe = "";
      } else {
         nombreEmplJefe = empleadojefe.getNombreCompleto();
      }
      return nombreEmplJefe;
   }

   public void setNombreEmplJefe(String nombreEmplJefe) {
      this.nombreEmplJefe = nombreEmplJefe;
   }

   public String getNombrePapel() {
      getPapel();
      if (papel == null) {
         nombrePapel = "";
      } else {
         nombrePapel = papel.getDescripcion();
      }
      return nombrePapel;
   }

   public void setNombrePapel(String nombrePapel) {
      this.nombrePapel = nombrePapel;
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
      if (!(object instanceof VigenciasCargos)) {
         return false;
      }
      VigenciasCargos other = (VigenciasCargos) object;
      if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "Entidades.Vigenciascargos[ secuencia=" + secuencia + " ]";
   }

   public Papeles getPapel() {
      return papel;
   }

   public void setPapel(Papeles papel) {
      this.papel = papel;
   }
}
