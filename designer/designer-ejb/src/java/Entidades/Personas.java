package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.Locale;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "PERSONAS")
public class Personas implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @Basic(optional = false)
   @NotNull
   @Column(name = "SECUENCIA")
   private BigInteger secuencia;
   @Size(max = 1)
   @Column(name = "FACTORRH")
   private String factorrh;
   @Column(name = "FECHANACIMIENTO")
   @Temporal(TemporalType.DATE)
   private Date fechanacimiento;
   @Column(name = "FECHAVENCIMIENTOCERTIFICADO")
   @Temporal(TemporalType.DATE)
   private Date fechavencimientocertificado;
   @Column(name = "FECHAFALLECIMIENTO")
   @Temporal(TemporalType.DATE)
   private Date fechafallecimiento;
   @Size(max = 2)
   @Column(name = "GRUPOSANGUINEO")
   private String gruposanguineo;
   @Column(name = "NOMBRE")
   private String nombre;
   @Basic(optional = false)
   @NotNull
   @Size(max = 20)
   @Column(name = "PRIMERAPELLIDO")
   private String primerapellido;
   @Size(max = 20)
   @Column(name = "SEGUNDOAPELLIDO")
   private String segundoapellido;
   @Size(max = 1)
   @Column(name = "SEXO")
   private String sexo;
   @Size(max = 1)
   @Column(name = "VIVIENDAPROPIA")
   private String viviendapropia;
   @Column(name = "CLASELIBRETAMILITAR")
   private Short claselibretamilitar;
   @Column(name = "NUMEROLIBRETAMILITAR")
   private Long numerolibretamilitar;
   @Column(name = "DISTRITOMILITAR")
   private Short distritomilitar;
   @Size(max = 15)
   @Column(name = "CERTIFICADOJUDICIAL")
   private String certificadojudicial;
   @Size(max = 100)
   @Column(name = "PATHFOTO")
   private String pathfoto;
   @Size(max = 100)
   @Column(name = "EMAIL")
   private String email;
   @Size(max = 10)
   @Column(name = "PLACAVEHICULO")
   private String placavehiculo;
   @Size(max = 20)
   @Column(name = "NUMEROMATRICULAPROF")
   private String numeromatriculaprof;
   @Column(name = "FECHAEXPMATRICULA")
   @Temporal(TemporalType.DATE)
   private Date fechaexpmatricula;
   @Column(name = "DIGITOVERIFICACIONDOCUMENTO")
   private Short digitoverificaciondocumento;
   @Size(max = 50)
   @Column(name = "OBSERVACION")
   private String observacion;
   @Size(max = 1)
   @Column(name = "VEHICULOEMPRESA")
   private String vehiculoempresa;
   @Size(max = 30)
   @Column(name = "SEGUNDONOMBRE")
   private String segundonombre;
   @Column(name = "NUMERODOCUMENTO")
   private BigInteger numerodocumento;
   @Column(name = "TIPODOCUMENTO")
   private BigInteger tipoDocumento;
   @Column(name = "CIUDADDOCUMENTO")
   private BigInteger ciudadDocumento;
   @Column(name = "CIUDADNACIMIENTO")
   private BigInteger ciudadNacimiento;
   @Transient
   private String nombreTipoDocumento;
   @Transient
   private String nombreCiudadDocumento;
   @Transient
   private String nombreCiudadNacimiento;
//   @JoinColumn(name = "TIPODOCUMENTO", referencedColumnName = "SECUENCIA")
//   @ManyToOne(optional = false)
//   private TiposDocumentos tipoDocumento;
//   @JoinColumn(name = "CIUDADDOCUMENTO", referencedColumnName = "SECUENCIA")
//   @ManyToOne
//   private Ciudades ciudadDocumento;
//   @JoinColumn(name = "CIUDADNACIMIENTO", referencedColumnName = "SECUENCIA")
//   @ManyToOne(optional = false)
//   private Ciudades ciudadNacimiento;
   @Transient
   private String nombreCompleto;
   @Transient
   private int edad;
   @Transient
   private String strNumeroDocumento;

   public Personas() {
   }

   public Personas(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public Personas(BigInteger secuencia, String nombre, BigInteger numerodocumento, String primerapellido) {
      this.secuencia = secuencia;
      this.nombre = nombre;
      this.numerodocumento = numerodocumento;
      this.primerapellido = primerapellido;
   }

   public BigInteger getSecuencia() {
      return secuencia;
   }

   public void setSecuencia(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public String getFactorrh() {
      return factorrh;
   }

   public void setFactorrh(String factorrh) {
      this.factorrh = factorrh;
   }

   public Date getFechanacimiento() {
      return fechanacimiento;
   }

   public void setFechanacimiento(Date fechanacimiento) {
      this.fechanacimiento = fechanacimiento;
   }

   public Date getFechavencimientocertificado() {
      return fechavencimientocertificado;
   }

   public void setFechavencimientocertificado(Date fechavencimientocertificado) {
      this.fechavencimientocertificado = fechavencimientocertificado;
   }

   public Date getFechafallecimiento() {
      return fechafallecimiento;
   }

   public void setFechafallecimiento(Date fechafallecimiento) {
      this.fechafallecimiento = fechafallecimiento;
   }

   public String getGruposanguineo() {
      return gruposanguineo;
   }

   public void setGruposanguineo(String gruposanguineo) {
      this.gruposanguineo = gruposanguineo;
   }

   public String getNombre() {
      if (nombre == null) {
         return " ";
      } else {
         return nombre;
      }
   }

   public void setNombre(String nombre) {
      this.nombre = nombre.toUpperCase();
   }

   public String getPrimerapellido() {
      if (primerapellido == null) {
         return " ";
      } else {
         return primerapellido;
      }
   }

   public void setPrimerapellido(String primerapellido) {
      this.primerapellido = primerapellido.toUpperCase();
   }

   public String getSegundoapellido() {
      if (segundoapellido == null) {
         return " ";
      } else {
         return segundoapellido;
      }
   }

   public void setSegundoapellido(String segundoapellido) {
      this.segundoapellido = segundoapellido.toUpperCase();
   }

   public String getSexo() {
      return sexo;
   }

   public void setSexo(String sexo) {
      this.sexo = sexo;
   }

   public String getViviendapropia() {
      if (viviendapropia == null) {
         viviendapropia = "N";
      }
      return viviendapropia;
   }

   public void setViviendapropia(String viviendapropia) {
      this.viviendapropia = viviendapropia;
   }

   public Short getClaselibretamilitar() {
      return claselibretamilitar;
   }

   public void setClaselibretamilitar(Short claselibretamilitar) {
      this.claselibretamilitar = claselibretamilitar;
   }

   public Long getNumerolibretamilitar() {
      return numerolibretamilitar;
   }

   public void setNumerolibretamilitar(Long numerolibretamilitar) {
      this.numerolibretamilitar = numerolibretamilitar;
   }

   public Short getDistritomilitar() {
      return distritomilitar;
   }

   public void setDistritomilitar(Short distritomilitar) {
      this.distritomilitar = distritomilitar;
   }

   public String getCertificadojudicial() {
      return certificadojudicial;
   }

   public void setCertificadojudicial(String certificadojudicial) {
      this.certificadojudicial = certificadojudicial;
   }

   public String getPathfoto() {
      return pathfoto;
   }

   public void setPathfoto(String pathfoto) {
      this.pathfoto = pathfoto;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      if (email != null) {
         this.email = email.toLowerCase(Locale.ENGLISH);
      } else {
         this.email = email;
      }
   }

   public String getPlacavehiculo() {
      return placavehiculo;
   }

   public void setPlacavehiculo(String placavehiculo) {
      this.placavehiculo = placavehiculo;
   }

   public String getNumeromatriculaprof() {
      return numeromatriculaprof;
   }

   public void setNumeromatriculaprof(String numeromatriculaprof) {
      this.numeromatriculaprof = numeromatriculaprof.toUpperCase();
   }

   public Date getFechaexpmatricula() {
      return fechaexpmatricula;
   }

   public void setFechaexpmatricula(Date fechaexpmatricula) {
      this.fechaexpmatricula = fechaexpmatricula;
   }

   public Short getDigitoverificaciondocumento() {
      return digitoverificaciondocumento;
   }

   public void setDigitoverificaciondocumento(Short digitoverificaciondocumento) {
      this.digitoverificaciondocumento = digitoverificaciondocumento;
   }

   public String getObservacion() {
      return observacion;
   }

   public void setObservacion(String observacion) {
      this.observacion = observacion.toUpperCase();
   }

   public String getVehiculoempresa() {
      if (vehiculoempresa == null) {
         vehiculoempresa = "";
      }
      return vehiculoempresa;
   }

   public void setVehiculoempresa(String vehiculoempresa) {
      this.vehiculoempresa = vehiculoempresa;
   }

   public String getSegundonombre() {
      if (segundonombre == null) {
         segundonombre = "";
      }
      return segundonombre;
   }

   public void setSegundonombre(String segundonombre) {
      this.segundonombre = segundonombre.toUpperCase();
   }

   public String getStrNumeroDocumento() {
      if (strNumeroDocumento == null) {
         strNumeroDocumento = "" + numerodocumento;
      }
      return strNumeroDocumento;
   }

   public void setStrNumeroDocumento(String strNumeroDocumento) {
      this.strNumeroDocumento = strNumeroDocumento;
   }

   public String getNombreCompleto() {
      if (nombreCompleto == null) {
         nombreCompleto = getPrimerapellido() + " " + getSegundoapellido() + " " + getNombre();
         if (nombreCompleto.equals("  ")) {
            nombreCompleto = null;
         }
      }
      return nombreCompleto;
   }

   public String getNombreCompletoOrden2() {
      if (nombreCompleto == null) {
         nombreCompleto = getNombre() + " " + getPrimerapellido() + " " + getSegundoapellido();
         if (nombreCompleto.equals(" ") || nombreCompleto.equals("  ")) {
            nombreCompleto = null;
         }
         return nombreCompleto;
      } else {
         return nombreCompleto;
      }
   }

   public void setNombreCompleto(String nombreCompleto) {
      if (nombreCompleto != null) {
         this.nombreCompleto = nombreCompleto.toUpperCase();
      } else {
         this.nombreCompleto = nombreCompleto;
      }
   }

   public int getEdad() {
      if (fechanacimiento != null) {
         Date fechaHoy = new Date();
         edad = fechaHoy.getYear() - fechanacimiento.getYear();
         if ((fechanacimiento.getMonth() - fechaHoy.getMonth()) > 0) {
            edad--;
         } else if ((fechanacimiento.getMonth() - fechaHoy.getMonth()) == 0) {
            if ((fechanacimiento.getDate() - fechaHoy.getDate()) > 0) {
               edad--;
            }
         }
      }
      return edad;
   }

   public void setEdad(int edad) {
      this.edad = edad;
   }

   public BigInteger getNumerodocumento() {
      return numerodocumento;
   }

   public void setNumerodocumento(BigInteger numerodocumento) {
      this.numerodocumento = numerodocumento;
   }

   public BigInteger getTipoDocumento() {
      return tipoDocumento;
   }

   public void setTipoDocumento(BigInteger tipoDocumento) {
      this.tipoDocumento = tipoDocumento;
   }

   public BigInteger getCiudadDocumento() {
      return ciudadDocumento;
   }

   public void setCiudadDocumento(BigInteger ciudadDocumento) {
      this.ciudadDocumento = ciudadDocumento;
   }

   public BigInteger getCiudadNacimiento() {
      return ciudadNacimiento;
   }

   public void setCiudadNacimiento(BigInteger ciudadNacimiento) {
      this.ciudadNacimiento = ciudadNacimiento;
   }

   public String getNombreTipoDocumento() {
      return nombreTipoDocumento;
   }

   public void setNombreTipoDocumento(String nombreTipoDocumento) {
      this.nombreTipoDocumento = nombreTipoDocumento;
   }

   public String getNombreCiudadNacimiento() {
      return nombreCiudadNacimiento;
   }

   public void setNombreCiudadNacimiento(String nombreCiudadNacimiento) {
      this.nombreCiudadNacimiento = nombreCiudadNacimiento;
   }

   public String getNombreCiudadDocumento() {
      return nombreCiudadDocumento;
   }

   public void setNombreCiudadDocumento(String nombreCiudadDocumento) {
      this.nombreCiudadDocumento = nombreCiudadDocumento;
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
      if (!(object instanceof Personas)) {
         return false;
      }
      Personas other = (Personas) object;
      if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "Entidades.Personas[ secuencia=" + secuencia + " ]";
   }

   public void llenarTransients(PersonasAux personaAux) {
      this.setNombreCiudadDocumento(personaAux.getNombreCiudadDocumento());
      this.setNombreCiudadNacimiento(personaAux.getNombreCiudadNacimiento());
      this.setNombreTipoDocumento(personaAux.getNombreTipoDocumento());
   }

}
