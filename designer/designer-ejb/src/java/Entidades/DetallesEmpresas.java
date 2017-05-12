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
@Table(name = "DETALLESEMPRESAS")
public class DetallesEmpresas implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @Basic(optional = false)
   @NotNull
   @Column(name = "SECUENCIA")
   private BigInteger secuencia;
   @Basic(optional = false)
   @NotNull
   @Size(min = 1, max = 10)
   @Column(name = "TIPO")
   private String tipo;
   @Basic(optional = false)
   @NotNull
   @Size(min = 1, max = 40)
   @Column(name = "DIRECCION")
   private String direccion;
   @Basic(optional = false)
   @NotNull
   @Size(min = 1, max = 10)
   @Column(name = "TELEFONO")
   private String telefono;
   @Basic(optional = false)
   @NotNull
   @Size(min = 1, max = 10)
   @Column(name = "FAX")
   private String fax;
   @Basic(optional = false)
   @NotNull
   @Size(min = 1, max = 40)
   @Column(name = "NOMBREREPRESENTANTE")
   private String nombrerepresentante;
   @Basic(optional = false)
   @NotNull
   @Size(min = 1, max = 15)
   @Column(name = "DOCUMENTOREPRESENTANTE")
   private String documentorepresentante;
   @Size(max = 5)
   @Column(name = "TIPONIT")
   private String tiponit;
   @Column(name = "DIGITOVERIFICACION")
   private Short digitoverificacion;
   @Size(max = 100)
   @Column(name = "EMAIL")
   private String email;
   @Size(max = 10)
   @Column(name = "ZONA")
   private String zona;
   @Size(max = 200)
   @Column(name = "ACTIVIDADECONOMICA")
   private String actividadeconomica;
   @Column(name = "FECHAPENSIONES")
   @Temporal(TemporalType.TIMESTAMP)
   private Date fechapensiones;
   @Column(name = "SUCURSAL")
   private BigInteger sucursal;
   @Size(max = 2)
   @Column(name = "TIPODOCUMENTO")
   private String tipodocumento;
   @Size(max = 1)
   @Column(name = "CLASEAPORTANTE")
   private String claseaportante;
   @Size(max = 1)
   @Column(name = "NATURALEZAJURIDICA")
   private String naturalezajuridica;
   @Size(max = 1)
   @Column(name = "TIPOPERSONA")
   private String tipopersona;
   @Size(max = 1)
   @Column(name = "FORMAPRESENTACION")
   private String formapresentacion;
   @Column(name = "TIPOAPORTANTE")
   private BigInteger tipoaportante;
   @Column(name = "TIPOACCION")
   private BigInteger tipoaccion;
   @Size(max = 10)
   @Column(name = "CIIU")
   private String ciiu;
   @Size(max = 100)
   @Column(name = "NOMBREARQUITECTO")
   private String nombrearquitecto;
   @Size(max = 50)
   @Column(name = "CARGOARQUITECTO")
   private String cargoarquitecto;
   @Size(max = 20)
   @Column(name = "PILAULTIMAPLANILLA")
   private String pilaultimaplanilla;
   @Size(max = 1)
   @Column(name = "AFILIADOARP")
   private String afiliadoarp;
   @Size(max = 1)
   @Column(name = "AFILIADOCCF")
   private String afiliadoccf;
   @Size(max = 1)
   @Column(name = "REFORMAEXONERAICBFSENASALUD")
   private String reformaexoneraicbfsenasalud;
   @Size(max = 1)
   @Column(name = "PILAUSAMULTILINEASLN")
   private String pilausamultilineasln;
   @Size(max = 1)
   @Column(name = "SOLIDARIDADFOSYGAEXENTOPRF")
   private String solidaridadfosygaexentoprf;
   @Size(max = 1)
   @Column(name = "EXONERALNSTARIFAAFPPATRON")
   private String exoneralnstarifaafppatron;
   @Size(max = 1)
   @Column(name = "REPORTALNSTARIFAAFPESPECIAL")
   private String reportalnstarifaafpespecial;
   @Column(name = "TERCEROARP")
   private BigInteger terceroarp;
   @Column(name = "TERCEROCCF")
   private BigInteger terceroccf;
   @Column(name = "TIPOACTIVIDADECONOMICA")
   private BigInteger tipoactividadeconomica;
   @Column(name = "FECHACAMARACOMERCIO")
   @Temporal(TemporalType.TIMESTAMP)
   private Date fechacamaracomercio;
   @Column(name = "ANOSINICIALESEXENTOPRF")
   private Short anosinicialesexentoprf;
   @Column(name = "PERSONAFIRMACONSTANCIA")
   private BigInteger ref_personafirmaconstancia;
   @Column(name = "EMPRESA")
   private BigInteger ref_empresa;
   @Column(name = "REPRESENTANTECIR")
   private BigInteger ref_representantecir;
   @Column(name = "SUBGERENTE")
   private BigInteger ref_subgerente;
   @Column(name = "ARQUITECTO")
   private BigInteger ref_arquitecto;
   @Column(name = "GERENTEGENERAL")
   private BigInteger ref_gerentegeneral;
   @Column(name = "CIUDAD")
   private BigInteger ref_ciudad;
   @Column(name = "CIUDADDOCUMENTOREPRESENTANTE")
   private BigInteger ref_ciudaddocrepresentante;
   @Column(name = "CARGOFIRMACONSTANCIA")
   private BigInteger ref_cargofirmaconstancia;

   @Transient
   private String nombre_personafirmaconstancia;
   @Transient
   private String nombre_empresa;
   @Transient
   private String nombre_representantecir;
   @Transient
   private String nombre_subgerente;
   @Transient
   private String nombre_arquitecto;
   @Transient
   private String nombre_gerentegeneral;
   @Transient
   private String nombre_ciudad;
   @Transient
   private String nombre_ciudaddocumentorepresentante;
   @Transient
   private String nombre_cargofirmaconstancia;

   @Transient
   private String strTipoDocumento;
   @Transient
   private String strTipoPersona;
   @Transient
   private String strNaturalezaJuridica;
   @Transient
   private String strClaseAportante;
   @Transient
   private String strFormaPresentacion;
   @Transient
   private String strTipoAportante;
   @Transient
   private String strTipoAccion;
   @Transient
   private boolean checkReformaExoneraIcbfSenaSalud;
   @Transient
   private String strReformaExoneraIcbfSenaSalud;
   @Transient
   private boolean checkPilaSsaMultilineasln;
   @Transient
   private String strPilaSsaMultilineasln;
   @Transient
   private boolean checkSolidaridadFosygaeExentoPrf;
   @Transient
   private String strSolidaridadFosygaeExentoPrf;
   @Transient
   private boolean checkExoneraLnsTarifaAfpPatron;
   @Transient
   private String strExoneraLnsTarifaAfpPatron;
   @Transient
   private boolean checkReportaLnsTarifaAfpEspecial;
   @Transient
   private String strReportaLnsTarifaAfpEspecial;
   @Transient
   private BigInteger controlempleados;
   @Transient
   private BigInteger nit_Empresa;

   public DetallesEmpresas() {
   }

   public DetallesEmpresas(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public DetallesEmpresas(BigInteger secuencia, String tipo, String direccion, String telefono, String fax, String nombrerepresentante, String documentorepresentante) {
      this.secuencia = secuencia;
      this.tipo = tipo;
      this.direccion = direccion;
      this.telefono = telefono;
      this.fax = fax;
      this.nombrerepresentante = nombrerepresentante;
      this.documentorepresentante = documentorepresentante;
   }

   public BigInteger getSecuencia() {
      return secuencia;
   }

   public void setSecuencia(BigInteger secuencia) {
      this.secuencia = secuencia;
   }

   public String getTipo() {
      return tipo;
   }

   public void setTipo(String tipo) {
      this.tipo = tipo.toUpperCase();
   }

   public String getDireccion() {
      return direccion;
   }

   public void setDireccion(String direccion) {
      this.direccion = direccion.toUpperCase();
   }

   public String getTelefono() {
      return telefono;
   }

   public void setTelefono(String telefono) {
      this.telefono = telefono.toUpperCase();
   }

   public String getFax() {
      return fax;
   }

   public void setFax(String fax) {
      this.fax = fax.toUpperCase();
   }

   public String getNombrerepresentante() {
      return nombrerepresentante;
   }

   public void setNombrerepresentante(String nombrerepresentante) {
      this.nombrerepresentante = nombrerepresentante.toUpperCase();
   }

   public String getDocumentorepresentante() {
      return documentorepresentante;
   }

   public void setDocumentorepresentante(String documentorepresentante) {
      this.documentorepresentante = documentorepresentante.toUpperCase();
   }

   public String getTiponit() {
      return tiponit;
   }

   public void setTiponit(String tiponit) {
      this.tiponit = tiponit.toUpperCase();
   }

   public Short getDigitoverificacion() {
      return digitoverificacion;
   }

   public void setDigitoverificacion(Short digitoverificacion) {
      this.digitoverificacion = digitoverificacion;
   }

   public String getEmail() {
      if (email == null) {
         email = " ";
      }
      return email;
   }

   public void setEmail(String email) {
      this.email = email.toUpperCase();
   }

   public String getZona() {
      if (zona == null) {
         zona = "RURAL";
      }
      return zona;
   }

   public void setZona(String zona) {
      this.zona = zona;
   }

   public String getActividadeconomica() {
      if (actividadeconomica == null) {
         actividadeconomica = " ";
      }
      return actividadeconomica;
   }

   public void setActividadeconomica(String actividadeconomica) {
      this.actividadeconomica = actividadeconomica.toUpperCase();
   }

   public Date getFechapensiones() {
      return fechapensiones;
   }

   public void setFechapensiones(Date fechapensiones) {
      this.fechapensiones = fechapensiones;
   }

   public BigInteger getSucursal() {
      return sucursal;
   }

   public void setSucursal(BigInteger sucursal) {
      this.sucursal = sucursal;
   }

   public String getTipodocumento() {
      if (tipodocumento == null) {
         tipodocumento = "NIT";
      }
      return tipodocumento;
   }

   public void setTipodocumento(String tipodocumento) {
      this.tipodocumento = tipodocumento;
   }

   public String getStrTipoDocumento() {
      getTipodocumento();
      if (tipodocumento.equalsIgnoreCase("NI")) {
         strTipoDocumento = "NIT";
      }
      if (tipodocumento.equalsIgnoreCase("CC")) {
         strTipoDocumento = "CEDULA";
      }
      if (tipodocumento.equalsIgnoreCase("CE")) {
         strTipoDocumento = "CEDULA EXTRANJERIA";
      }
      if (tipodocumento.equalsIgnoreCase("TI")) {
         strTipoDocumento = "TARJETA IDENTIDAD";
      }
      if (tipodocumento.equalsIgnoreCase("RC")) {
         strTipoDocumento = "REGISTRO CIVIL";
      }
      if (tipodocumento.equalsIgnoreCase("PA")) {
         strTipoDocumento = "PASAPORTE";
      }
      return strTipoDocumento;
   }

   public void setStrTipoDocumento(String strTipoDocumento) {
      this.strTipoDocumento = strTipoDocumento;
   }

   public String getClaseaportante() {
      if (claseaportante == null) {
         claseaportante = "A";
      }
      return claseaportante;
   }

   public void setClaseaportante(String claseaportante) {
      this.claseaportante = claseaportante;
   }

   public String getStrClaseAportante() {
      getClaseaportante();
      if (claseaportante.equalsIgnoreCase("A")) {
         strClaseAportante = "APORTANTE CON MAS DE 200 COTIZANTES";
      }
      if (claseaportante.equalsIgnoreCase("B")) {
         strClaseAportante = "APORTANTE CON MENOS DE 200 COTIZANTES";
      }
      if (claseaportante.equalsIgnoreCase("I")) {
         strClaseAportante = "INDEPENDIENTE";
      }
      return strClaseAportante;
   }

   public void setStrClaseAportante(String strClaseAportante) {
      this.strClaseAportante = strClaseAportante;
   }

   public String getNaturalezajuridica() {
      if (naturalezajuridica == null) {
         naturalezajuridica = "1";
      }
      return naturalezajuridica;
   }

   public void setNaturalezajuridica(String naturalezajuridica) {
      this.naturalezajuridica = naturalezajuridica;
   }

   public String getStrNaturalezaJuridica() {
      getNaturalezajuridica();
      if (naturalezajuridica.equalsIgnoreCase("1")) {
         strNaturalezaJuridica = "PUBLICA";
      }
      if (naturalezajuridica.equalsIgnoreCase("2")) {
         strNaturalezaJuridica = "PRIVADA";
      }
      if (naturalezajuridica.equalsIgnoreCase("3")) {
         strNaturalezaJuridica = "MIXTA";
      }
      if (naturalezajuridica.equalsIgnoreCase("4")) {
         strNaturalezaJuridica = "ORGANISMO MULTILATERAL";
      }
      if (naturalezajuridica.equalsIgnoreCase("5")) {
         strNaturalezaJuridica = "ENTIDADES DE DERECHO PUBLICO NO SOMETIDO LEGISLACION COLOMBIANA";
      }
      return strNaturalezaJuridica;
   }

   public void setStrNaturalezaJuridica(String strNaturalezaJuridica) {
      this.strNaturalezaJuridica = strNaturalezaJuridica;
   }

   public String getTipopersona() {
      if (tipopersona == null) {
         tipopersona = "J";
      }
      return tipopersona;
   }

   public void setTipopersona(String tipopersona) {
      this.tipopersona = tipopersona;
   }

   public String getStrTipoPersona() {
      getTipopersona();
      if (tipopersona.equalsIgnoreCase("N")) {
         strTipoPersona = "NATURAL";
      }
      if (tipopersona.equalsIgnoreCase("J")) {
         strTipoPersona = "JURIDICA";
      }
      return strTipoPersona;
   }

   public void setStrTipoPersona(String strTipoPersona) {
      this.strTipoPersona = strTipoPersona;
   }

   public String getFormapresentacion() {
      if (formapresentacion == null) {
         formapresentacion = "U";
      }
      return formapresentacion;
   }

   public void setFormapresentacion(String formapresentacion) {
      this.formapresentacion = formapresentacion;
   }

   public String getStrFormaPresentacion() {
      getFormapresentacion();
      if (formapresentacion.equalsIgnoreCase("U")) {
         strFormaPresentacion = "UNICO";
      }
      if (formapresentacion.equalsIgnoreCase("C")) {
         strFormaPresentacion = "CONSOLIDADO";
      }
      if (formapresentacion.equalsIgnoreCase("S")) {
         strFormaPresentacion = "SUCURSAL";
      }
      if (formapresentacion.equalsIgnoreCase("D")) {
         strFormaPresentacion = "DEPENDENCIA";
      }
      return strFormaPresentacion;
   }

   public void setStrFormaPresentacion(String strFormaPresentacion) {
      this.strFormaPresentacion = strFormaPresentacion;
   }

   public BigInteger getTipoaportante() {
      if (tipoaportante == null) {
         tipoaportante = new BigInteger("1");
      }
      return tipoaportante;
   }

   public void setTipoaportante(BigInteger tipoaportante) {
      this.tipoaportante = tipoaportante;
   }

   public String getStrTipoAportante() {
      getTipoaportante();
      if (tipoaportante.equals(new BigInteger("1"))) {
         strTipoAportante = "EMPLEADOR";
      }
      if (tipoaportante.equals(new BigInteger("2"))) {
         strTipoAportante = "INDEPENDIENTE";
      }
      if (tipoaportante.equals(new BigInteger("3"))) {
         strTipoAportante = "ENTIDADES O UNIVERSIDADES PUBLICAS CON REGIMEN ESPECIAL EN SALUD";
      }
      if (tipoaportante.equals(new BigInteger("4"))) {
         strTipoAportante = "AGREMIACIONES O ASOCIACIONES";
      }
      if (tipoaportante.equals(new BigInteger("5"))) {
         strTipoAportante = "COOPERATUCAS Y PRECOPERATIVAS DE TRABAJO ASOCIADO";
      }
      if (tipoaportante.equals(new BigInteger("6"))) {
         strTipoAportante = "MISIONES DIPLOMATICAS, CONSULARES U ORGANISMO MULTILATERAL";
      }
      if (tipoaportante.equals(new BigInteger("7"))) {
         strTipoAportante = "ORGANIZACIONES ADMINISTRADORAS DEL PROGRAMA DE HOGARES DE BIENTESTAR";
      }
      if (tipoaportante.equals(new BigInteger("8"))) {
         strTipoAportante = "PAGADOR DE APORTES DE LOS CONCELAJES MUNICIPALES";
      }
      return strTipoAportante;
   }

   public void setStrTipoAportante(String strTipoAportante) {
      this.strTipoAportante = strTipoAportante;
   }

   public BigInteger getTipoaccion() {
      if (tipoaccion == null) {
         tipoaccion = new BigInteger("1");
      }
      return tipoaccion;
   }

   public void setTipoaccion(BigInteger tipoaccion) {
      this.tipoaccion = tipoaccion;
   }

   public String getStrTipoAccion() {
      getTipoaccion();
      if (tipoaccion.equals(new BigInteger("1"))) {
         strTipoAccion = "CONCORDATO";
      }
      if (tipoaccion.equals(new BigInteger("2"))) {
         strTipoAccion = "REESTRUCTURACION";
      }
      if (tipoaccion.equals(new BigInteger("3"))) {
         strTipoAccion = "LIQUIDACION";
      }
      if (tipoaccion.equals(new BigInteger("3"))) {
         strTipoAccion = "CESE DE ACTIVIDADES";
      }
      return strTipoAccion;
   }

   public void setStrTipoAccion(String strTipoAccion) {
      this.strTipoAccion = strTipoAccion;
   }

   public String getCiiu() {
      return ciiu;
   }

   public void setCiiu(String ciiu) {
      this.ciiu = ciiu;
   }

   public String getNombrearquitecto() {
      return nombrearquitecto;
   }

   public void setNombrearquitecto(String nombrearquitecto) {
      this.nombrearquitecto = nombrearquitecto.toUpperCase();
   }

   public String getCargoarquitecto() {
      if (cargoarquitecto == null) {
         cargoarquitecto = " ";
      }
      return cargoarquitecto;
   }

   public void setCargoarquitecto(String cargoarquitecto) {
      this.cargoarquitecto = cargoarquitecto.toUpperCase();
   }

   public String getPilaultimaplanilla() {
      if (pilaultimaplanilla == null) {
         pilaultimaplanilla = " ";
      }
      return pilaultimaplanilla;
   }

   public void setPilaultimaplanilla(String pilaultimaplanilla) {
      this.pilaultimaplanilla = pilaultimaplanilla.toUpperCase();
   }

   public String getAfiliadoarp() {
      return afiliadoarp;
   }

   public void setAfiliadoarp(String afiliadoarp) {
      this.afiliadoarp = afiliadoarp;
   }

   public String getAfiliadoccf() {
      return afiliadoccf;
   }

   public void setAfiliadoccf(String afiliadoccf) {
      this.afiliadoccf = afiliadoccf;
   }

   public BigInteger getTerceroarp() {
      return terceroarp;
   }

   public void setTerceroarp(BigInteger terceroarp) {
      this.terceroarp = terceroarp;
   }

   public BigInteger getTerceroccf() {
      return terceroccf;
   }

   public void setTerceroccf(BigInteger terceroccf) {
      this.terceroccf = terceroccf;
   }

   public BigInteger getTipoactividadeconomica() {
      return tipoactividadeconomica;
   }

   public void setTipoactividadeconomica(BigInteger tipoactividadeconomica) {
      this.tipoactividadeconomica = tipoactividadeconomica;
   }

   public Date getFechacamaracomercio() {
      return fechacamaracomercio;
   }

   public void setFechacamaracomercio(Date fechacamaracomercio) {
      this.fechacamaracomercio = fechacamaracomercio;
   }

   public Short getAnosinicialesexentoprf() {
      return anosinicialesexentoprf;
   }

   public void setAnosinicialesexentoprf(Short anosinicialesexentoprf) {
      this.anosinicialesexentoprf = anosinicialesexentoprf;
   }

   public BigInteger getRef_personafirmaconstancia() {
      return ref_personafirmaconstancia;
   }

   public void setRef_personafirmaconstancia(BigInteger ref_personafirmaconstancia) {
      this.ref_personafirmaconstancia = ref_personafirmaconstancia;
   }

   public BigInteger getRef_empresa() {
      return ref_empresa;
   }

   public void setRef_empresa(BigInteger ref_empresa) {
      this.ref_empresa = ref_empresa;
   }

   public BigInteger getRef_representantecir() {
      return ref_representantecir;
   }

   public void setRef_representantecir(BigInteger ref_representantecir) {
      this.ref_representantecir = ref_representantecir;
   }

   public BigInteger getRef_subgerente() {
      return ref_subgerente;
   }

   public void setRef_subgerente(BigInteger ref_subgerente) {
      this.ref_subgerente = ref_subgerente;
   }

   public BigInteger getRef_arquitecto() {
      return ref_arquitecto;
   }

   public void setRef_arquitecto(BigInteger ref_arquitecto) {
      this.ref_arquitecto = ref_arquitecto;
   }

   public BigInteger getRef_gerentegeneral() {
      return ref_gerentegeneral;
   }

   public void setRef_gerentegeneral(BigInteger ref_gerentegeneral) {
      this.ref_gerentegeneral = ref_gerentegeneral;
   }

   public BigInteger getRef_ciudad() {
      return ref_ciudad;
   }

   public void setRef_ciudad(BigInteger ref_ciudad) {
      this.ref_ciudad = ref_ciudad;
   }

   public BigInteger getRef_ciudaddocrepresentante() {
      return ref_ciudaddocrepresentante;
   }

   public void setRef_ciudaddocrepresentante(BigInteger ref_ciudaddocrepresentante) {
      this.ref_ciudaddocrepresentante = ref_ciudaddocrepresentante;
   }

   public BigInteger getRef_cargofirmaconstancia() {
      return ref_cargofirmaconstancia;
   }

   public void setRef_cargofirmaconstancia(BigInteger ref_cargofirmaconstancia) {
      this.ref_cargofirmaconstancia = ref_cargofirmaconstancia;
   }

   public String getNombre_personafirmaconstancia() {
      return nombre_personafirmaconstancia;
   }

   public void setNombre_personafirmaconstancia(String nombre_personafirmaconstancia) {
      this.nombre_personafirmaconstancia = nombre_personafirmaconstancia;
   }

   public String getNombre_empresa() {
      return nombre_empresa;
   }

   public void setNombre_empresa(String nombre_empresa) {
      this.nombre_empresa = nombre_empresa;
   }

   public String getNombre_representantecir() {
      return nombre_representantecir;
   }

   public void setNombre_representantecir(String nombre_representantecir) {
      this.nombre_representantecir = nombre_representantecir;
   }

   public String getNombre_subgerente() {
      return nombre_subgerente;
   }

   public void setNombre_subgerente(String nombre_subgerente) {
      this.nombre_subgerente = nombre_subgerente;
   }

   public String getNombre_arquitecto() {
      return nombre_arquitecto;
   }

   public void setNombre_arquitecto(String nombre_arquitecto) {
      this.nombre_arquitecto = nombre_arquitecto;
   }

   public String getNombre_gerentegeneral() {
      return nombre_gerentegeneral;
   }

   public void setNombre_gerentegeneral(String nombre_gerentegeneral) {
      this.nombre_gerentegeneral = nombre_gerentegeneral;
   }

   public String getNombre_ciudad() {
      return nombre_ciudad;
   }

   public void setNombre_ciudad(String nombre_ciudad) {
      this.nombre_ciudad = nombre_ciudad;
   }

   public String getNombre_ciudaddocumentorepresentante() {
      return nombre_ciudaddocumentorepresentante;
   }

   public void setNombre_ciudaddocumentorepresentante(String nombre_ciudaddocumentorepresentante) {
      this.nombre_ciudaddocumentorepresentante = nombre_ciudaddocumentorepresentante;
   }

   public String getNombre_cargofirmaconstancia() {
      return nombre_cargofirmaconstancia;
   }

   public void setNombre_cargofirmaconstancia(String nombre_cargofirmaconstancia) {
      this.nombre_cargofirmaconstancia = nombre_cargofirmaconstancia;
   }

   public String getReformaexoneraicbfsenasalud() {
      if (reformaexoneraicbfsenasalud == null) {
         reformaexoneraicbfsenasalud = "N";
      }
      return reformaexoneraicbfsenasalud;
   }

   public void setReformaexoneraicbfsenasalud(String reformaexoneraicbfsenasalud) {
      this.reformaexoneraicbfsenasalud = reformaexoneraicbfsenasalud;
   }

   public String getPilausamultilineasln() {
      if (pilausamultilineasln == null) {
         pilausamultilineasln = "N";
      }
      return pilausamultilineasln;
   }

   public void setPilausamultilineasln(String pilausamultilineasln) {
      this.pilausamultilineasln = pilausamultilineasln;
   }

   public String getSolidaridadfosygaexentoprf() {
      if (solidaridadfosygaexentoprf == null) {
         solidaridadfosygaexentoprf = "N";
      }
      return solidaridadfosygaexentoprf;
   }

   public void setSolidaridadfosygaexentoprf(String solidaridadfosygaexentoprf) {
      this.solidaridadfosygaexentoprf = solidaridadfosygaexentoprf;
   }

   public String getExoneralnstarifaafppatron() {
      if (exoneralnstarifaafppatron == null) {
         exoneralnstarifaafppatron = "N";
      }
      return exoneralnstarifaafppatron;
   }

   public void setExoneralnstarifaafppatron(String exoneralnstarifaafppatron) {
      this.exoneralnstarifaafppatron = exoneralnstarifaafppatron;
   }

   public String getReportalnstarifaafpespecial() {
      if (reportalnstarifaafpespecial == null) {
         reportalnstarifaafpespecial = "N";
      }
      return reportalnstarifaafpespecial;
   }

   public void setReportalnstarifaafpespecial(String reportalnstarifaafpespecial) {
      this.reportalnstarifaafpespecial = reportalnstarifaafpespecial;
   }

   public boolean isCheckReformaExoneraIcbfSenaSalud() {
      getReformaexoneraicbfsenasalud();
      if (reformaexoneraicbfsenasalud.equalsIgnoreCase("N")) {
         checkReformaExoneraIcbfSenaSalud = false;
      } else {
         checkReformaExoneraIcbfSenaSalud = true;
      }
      return checkReformaExoneraIcbfSenaSalud;
   }

   public void setCheckReformaExoneraIcbfSenaSalud(boolean checkReformaExoneraIcbfSenaSalud) {
      if (checkReformaExoneraIcbfSenaSalud == false) {
         reformaexoneraicbfsenasalud = "N";
      } else {
         reformaexoneraicbfsenasalud = "S";
      }
      this.checkReformaExoneraIcbfSenaSalud = checkReformaExoneraIcbfSenaSalud;
   }

   public String getStrReformaExoneraIcbfSenaSalud() {
      getReformaexoneraicbfsenasalud();
      if (reformaexoneraicbfsenasalud.equalsIgnoreCase("N")) {
         strReformaExoneraIcbfSenaSalud = "NO";
      } else {
         strReformaExoneraIcbfSenaSalud = "SI";
      }
      return strReformaExoneraIcbfSenaSalud;
   }

   public void setStrReformaExoneraIcbfSenaSalud(String strReformaExoneraIcbfSenaSalud) {
      this.strReformaExoneraIcbfSenaSalud = strReformaExoneraIcbfSenaSalud;
   }

   public boolean isCheckPilaSsaMultilineasln() {
      getPilausamultilineasln();
      if (pilausamultilineasln.equalsIgnoreCase("N")) {
         checkPilaSsaMultilineasln = false;
      } else {
         checkPilaSsaMultilineasln = true;
      }
      return checkPilaSsaMultilineasln;
   }

   public void setCheckPilaSsaMultilineasln(boolean checkPilaSsaMultilineasln) {
      if (checkPilaSsaMultilineasln == false) {
         pilausamultilineasln = "N";
      } else {
         pilausamultilineasln = "S";
      }
      this.checkPilaSsaMultilineasln = checkPilaSsaMultilineasln;
   }

   public String getStrPilaSsaMultilineasln() {
      getPilausamultilineasln();
      if (pilausamultilineasln.equalsIgnoreCase("N")) {
         strPilaSsaMultilineasln = "NO";
      } else {
         strPilaSsaMultilineasln = "SI";
      }
      return strPilaSsaMultilineasln;
   }

   public void setStrPilaSsaMultilineasln(String strPilaSsaMultilineasln) {
      this.strPilaSsaMultilineasln = strPilaSsaMultilineasln;
   }

   public boolean isCheckSolidaridadFosygaeExentoPrf() {
      getSolidaridadfosygaexentoprf();
      if (solidaridadfosygaexentoprf.equalsIgnoreCase("N")) {
         checkSolidaridadFosygaeExentoPrf = false;
      } else {
         checkSolidaridadFosygaeExentoPrf = true;
      }
      return checkSolidaridadFosygaeExentoPrf;
   }

   public void setCheckSolidaridadFosygaeExentoPrf(boolean checkSolidaridadFosygaeExentoPrf) {
      if (checkSolidaridadFosygaeExentoPrf == false) {
         solidaridadfosygaexentoprf = "N";
      } else {
         solidaridadfosygaexentoprf = "S";
      }
      this.checkSolidaridadFosygaeExentoPrf = checkSolidaridadFosygaeExentoPrf;
   }

   public String getStrSolidaridadFosygaeExentoPrf() {
      getSolidaridadfosygaexentoprf();
      if (solidaridadfosygaexentoprf.equalsIgnoreCase("N")) {
         strSolidaridadFosygaeExentoPrf = "NO";
      } else {
         strSolidaridadFosygaeExentoPrf = "SI";
      }
      return strSolidaridadFosygaeExentoPrf;
   }

   public void setStrSolidaridadFosygaeExentoPrf(String strSolidaridadFosygaeExentoPrf) {
      this.strSolidaridadFosygaeExentoPrf = strSolidaridadFosygaeExentoPrf;
   }

   public boolean isCheckExoneraLnsTarifaAfpPatron() {
      getExoneralnstarifaafppatron();
      if (exoneralnstarifaafppatron.equalsIgnoreCase("N")) {
         checkExoneraLnsTarifaAfpPatron = false;
      } else {
         checkExoneraLnsTarifaAfpPatron = true;
      }
      return checkExoneraLnsTarifaAfpPatron;
   }

   public void setCheckExoneraLnsTarifaAfpPatron(boolean checkExoneraLnsTarifaAfpPatron) {
      if (checkExoneraLnsTarifaAfpPatron == false) {
         exoneralnstarifaafppatron = "N";
      } else {
         exoneralnstarifaafppatron = "S";
      }
      this.checkExoneraLnsTarifaAfpPatron = checkExoneraLnsTarifaAfpPatron;
   }

   public String getStrExoneraLnsTarifaAfpPatron() {
      getExoneralnstarifaafppatron();
      if (exoneralnstarifaafppatron.equalsIgnoreCase("N")) {
         strExoneraLnsTarifaAfpPatron = "NO";
      } else {
         strExoneraLnsTarifaAfpPatron = "SI";
      }
      return strExoneraLnsTarifaAfpPatron;
   }

   public void setStrExoneraLnsTarifaAfpPatron(String strExoneraLnsTarifaAfpPatron) {
      this.strExoneraLnsTarifaAfpPatron = strExoneraLnsTarifaAfpPatron;
   }

   public boolean isCheckReportaLnsTarifaAfpEspecial() {
      getReportalnstarifaafpespecial();
      if (reportalnstarifaafpespecial.equalsIgnoreCase("N")) {
         checkReportaLnsTarifaAfpEspecial = false;
      } else {
         checkReportaLnsTarifaAfpEspecial = true;
      }
      return checkReportaLnsTarifaAfpEspecial;
   }

   public void setCheckReportaLnsTarifaAfpEspecial(boolean checkReportaLnsTarifaAfpEspecial) {
      if (checkReportaLnsTarifaAfpEspecial == false) {
         reportalnstarifaafpespecial = "N";
      } else {
         reportalnstarifaafpespecial = "S";
      }
      this.checkReportaLnsTarifaAfpEspecial = checkReportaLnsTarifaAfpEspecial;
   }

   public String getStrReportaLnsTarifaAfpEspecial() {
      getReportalnstarifaafpespecial();
      if (reportalnstarifaafpespecial.equalsIgnoreCase("N")) {
         strReportaLnsTarifaAfpEspecial = "NO";
      } else {
         strReportaLnsTarifaAfpEspecial = "SI";
      }
      return strReportaLnsTarifaAfpEspecial;
   }

   public void setStrReportaLnsTarifaAfpEspecial(String strReportaLnsTarifaAfpEspecial) {
      this.strReportaLnsTarifaAfpEspecial = strReportaLnsTarifaAfpEspecial;
   }

   public BigInteger getControlempleados() {
      return controlempleados;
   }

   public void setControlempleados(BigInteger controlempleados) {
      this.controlempleados = controlempleados;
   }

   public BigInteger getNit_Empresa() {
      return nit_Empresa;
   }

   public void setNit_Empresa(BigInteger nit_Empresa) {
      this.nit_Empresa = nit_Empresa;
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
      if (!(object instanceof DetallesEmpresas)) {
         return false;
      }
      DetallesEmpresas other = (DetallesEmpresas) object;
      if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "Entidades.Detallesempresas[ secuencia=" + secuencia + " ]";
   }
}
