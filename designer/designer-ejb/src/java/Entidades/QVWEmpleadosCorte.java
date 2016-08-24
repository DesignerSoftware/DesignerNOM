package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Table(name = "QVWEMPLEADOSCORTE")
@Entity
public class QVWEmpleadosCorte implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    @Id
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CODIGOEMPLEADO")
    private BigInteger codigoempleado;
    @Size(max = 20)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 20)
    @Column(name = "PRIMERAPELLIDO")
    private String primerapellido;
    @Size(max = 20)
    @Column(name = "SEGUNDOAPELLIDO")
    private String segundoapellido;
    @Size(max = 1)
    @Column(name = "SEXO")
    private String sexo;
    @Size(max = 100)
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "NUMERODOCUMENTO")
    private Long numerodocumento;
    @Size(max = 3)
    @Column(name = "TIPODOCUMENTO")
    private String tipodocumento;
    @Size(max = 30)
    @Column(name = "CIUDADDOCUMENTO")
    private String ciudaddocumento;
    @Size(max = 30)
    @Column(name = "DEPARTAMENTODOCUMENTO")
    private String departamentodocumento;
    @Size(max = 30)
    @Column(name = "CIUDADNACIMIENTO")
    private String ciudadnacimiento;
    @Size(max = 30)
    @Column(name = "DEPARTAMENTONACIMIENTO")
    private String departamentonacimiento;
    @Size(max = 30)
    @Column(name = "REFORMALABORALNOMBRE")
    private String reformalaboralnombre;
    @Column(name = "REFORMALABORALFECHAVIGENCIA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reformalaboralfechavigencia;
    @Size(max = 50)
    @Column(name = "CARGONOMBRE")
    private String cargonombre;
    @Column(name = "CARGOFECHAVIGENCIA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cargofechavigencia;
    @Size(max = 30)
    @Column(name = "MOTIVOCAMBIOCARGO")
    private String motivocambiocargo;
    @Size(max = 50)
    @Column(name = "NOMBREESTRUCTURACARGO")
    private String nombreestructuracargo;
    @Size(max = 50)
    @Column(name = "LOCALIZACIONNOMBRE")
    private String localizacionnombre;
    @Column(name = "LOCALIZACIONCODIGO")
    private Long localizacioncodigo;
    @Column(name = "LOCALIZACIONFECHAVIGENCIA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date localizacionfechavigencia;
    @Size(max = 30)
    @Column(name = "MOTIVOLOCALIZACION")
    private String motivolocalizacion;
    @Size(max = 15)
    @Column(name = "CODIGOCENTROCOSTOLOCALIZACION")
    private String codigocentrocostolocalizacion;
    @Size(max = 50)
    @Column(name = "ORIGENNOMBRE")
    private String origennombre;
    @Column(name = "ORIGENFECHAVIGENCIA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date origenfechavigencia;
    @Size(max = 40)
    @Column(name = "NORMANOMBRE")
    private String normanombre;
    @Column(name = "NORMAFECHAVIGENCIA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date normafechavigencia;
    @Column(name = "TIPOTRABAJADORFECHAVIGENCIA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tipotrabajadorfechavigencia;
    @Size(max = 40)
    @Column(name = "TIPOTRABAJADORNOMBRE")
    private String tipotrabajadornombre;
    @Size(max = 15)
    @Column(name = "TIPOTRABAJADORTIPO")
    private String tipotrabajadortipo;
    @Size(max = 30)
    @Column(name = "FORMAPAGONOMBRE")
    private String formapagonombre;
    @Column(name = "FORMAPAGOFECHAVIGENCIA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date formapagofechavigencia;
    @Size(max = 1)
    @Column(name = "FORMAPAGOTIPOCUENTA")
    private String formapagotipocuenta;
    @Size(max = 25)
    @Column(name = "FORMAPAGOCUENTA")
    private String formapagocuenta;
    @Size(max = 50)
    @Column(name = "CONTRATONOMBRE")
    private String contratonombre;
    @Column(name = "CONTRATOFECHAINICIAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date contratofechainicial;
    @Column(name = "CONTRATOFECHAFINAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date contratofechafinal;
    @Size(max = 40)
    @Column(name = "MVRNOMBRE")
    private String mvrnombre;
    @Column(name = "MVRECHAINICIAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date mvrechainicial;
    @Column(name = "MVRFECHAFINAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date mvrfechafinal;
    @Column(name = "MVRVALOR")
    private BigDecimal mvrvalor;
    @Size(max = 1)
    @Column(name = "TIPOSET")
    private String tiposet;
    @Column(name = "SETFECHAINICIAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date setfechainicial;
    @Column(name = "SETFECHAFINAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date setfechafinal;
    @Column(name = "SETVALOR")
    private BigDecimal setvalor;
    @Column(name = "SETPORCENTAJE")
    private BigDecimal setporcentaje;
    @Column(name = "IBCFECHAINICIAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ibcfechainicial;
    @Column(name = "IBCFECHAFINAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ibcfechafinal;
    @Column(name = "IBCVALOR")
    private BigDecimal ibcvalor;
    @Size(max = 4000)
    @Column(name = "DIRECCION")
    private String direccion;
    @Size(max = 4000)
    @Column(name = "INFOADICIONAL1")
    private String infoadicional1;
    @Size(max = 4000)
    @Column(name = "INFOADICIONAL2")
    private String infoadicional2;
    @Size(max = 4000)
    @Column(name = "INFOADICIONAL3")
    private String infoadicional3;
    @Size(max = 4000)
    @Column(name = "INFOADICIONAL4")
    private String infoadicional4;
    @Size(max = 4000)
    @Column(name = "INFOADICIONAL5")
    private String infoadicional5;
    @Size(max = 4000)
    @Column(name = "NIVELEDUCATIVO")
    private String niveleducativo;
    @Size(max = 4000)
    @Column(name = "UBICACIONGEOGRAFICA")
    private String ubicaciongeografica;
    @Column(name = "VALORTIPOSUELDO1")
    private BigInteger valortiposueldo1;
    @Column(name = "VALORTIPOSUELDO2")
    private BigInteger valortiposueldo2;
    @Column(name = "VALORTIPOSUELDO3")
    private BigInteger valortiposueldo3;
    @Column(name = "VALORTIPOSUELDO4")
    private BigInteger valortiposueldo4;
    @Column(name = "VALORTIPOSUELDO5")
    private BigInteger valortiposueldo5;
    @Column(name = "VALORTIPOSUELDO6")
    private BigInteger valortiposueldo6;
    @Column(name = "VALORTIPOSUELDO7")
    private BigInteger valortiposueldo7;
    @Column(name = "VALORTIPOSUELDO8")
    private BigInteger valortiposueldo8;
    @Column(name = "VALORTIPOSUELDO9")
    private BigInteger valortiposueldo9;
    @Column(name = "VALORTIPOSUELDO10")
    private BigInteger valortiposueldo10;
    @Column(name = "VALORTIPOSUELDO11")
    private BigInteger valortiposueldo11;
    @Column(name = "VALORTIPOSUELDO12")
    private BigInteger valortiposueldo12;
    @Column(name = "VALORTIPOSUELDO13")
    private BigInteger valortiposueldo13;
    @Column(name = "VALORTIPOSUELDO14")
    private BigInteger valortiposueldo14;
    @Column(name = "VALORTIPOSUELDO15")
    private BigInteger valortiposueldo15;
    @Column(name = "VALORTIPOSUELDO16")
    private BigInteger valortiposueldo16;
    @Column(name = "VALORTIPOSUELDO17")
    private BigInteger valortiposueldo17;
    @Column(name = "VALORTIPOSUELDO18")
    private BigInteger valortiposueldo18;
    @Column(name = "VALORTIPOSUELDO19")
    private BigInteger valortiposueldo19;
    @Column(name = "VALORTIPOSUELDO20")
    private BigInteger valortiposueldo20;
    @Column(name = "VALORTIPOSUELDO21")
    private BigInteger valortiposueldo21;
    @Column(name = "VALORTIPOSUELDO22")
    private BigInteger valortiposueldo22;
    @Column(name = "VALORTIPOSUELDO23")
    private BigInteger valortiposueldo23;
    @Column(name = "VALORTIPOSUELDO24")
    private BigInteger valortiposueldo24;
    @Column(name = "VALORTIPOSUELDO25")
    private BigInteger valortiposueldo25;
    @Column(name = "VALORTIPOSUELDO26")
    private BigInteger valortiposueldo26;
    @Column(name = "VALORTIPOSUELDO27")
    private BigInteger valortiposueldo27;
    @Column(name = "VALORTIPOSUELDO28")
    private BigInteger valortiposueldo28;
    @Column(name = "VALORTIPOSUELDO29")
    private BigInteger valortiposueldo29;
    @Column(name = "VALORTIPOSUELDO30")
    private BigInteger valortiposueldo30;
    @Column(name = "VALORTIPOSUELDO31")
    private BigInteger valortiposueldo31;
    @Column(name = "VALORTIPOSUELDO32")
    private BigInteger valortiposueldo32;
    @Column(name = "VALORTIPOSUELDO33")
    private BigInteger valortiposueldo33;
    @Column(name = "VALORTIPOSUELDO34")
    private BigInteger valortiposueldo34;
    @Column(name = "VALORTIPOSUELDO35")
    private BigInteger valortiposueldo35;
    @Column(name = "VALORTIPOSUELDO36")
    private BigInteger valortiposueldo36;
    @Column(name = "VALORTIPOSUELDO37")
    private BigInteger valortiposueldo37;
    @Column(name = "VALORTIPOSUELDO38")
    private BigInteger valortiposueldo38;
    @Column(name = "VALORTIPOSUELDO39")
    private BigInteger valortiposueldo39;
    @Column(name = "VALORTIPOSUELDO40")
    private BigInteger valortiposueldo40;
    @Column(name = "VALORTIPOSUELDO41")
    private BigInteger valortiposueldo41;
    @Column(name = "VALORTIPOSUELDO42")
    private BigInteger valortiposueldo42;
    @Column(name = "VALORTIPOSUELDO43")
    private BigInteger valortiposueldo43;
    @Column(name = "VALORTIPOSUELDO44")
    private BigInteger valortiposueldo44;
    @Column(name = "VALORTIPOSUELDO45")
    private BigInteger valortiposueldo45;
    @Column(name = "VALORTIPOSUELDO46")
    private BigInteger valortiposueldo46;
    @Column(name = "VALORTIPOSUELDO47")
    private BigInteger valortiposueldo47;
    @Column(name = "VALORTIPOSUELDO48")
    private BigInteger valortiposueldo48;
    @Column(name = "VALORTIPOSUELDO49")
    private BigInteger valortiposueldo49;
    @Column(name = "VALORTIPOSUELDO50")
    private BigInteger valortiposueldo50;
    @Column(name = "VALORTIPOSUELDO51")
    private BigInteger valortiposueldo51;
    @Column(name = "VALORTIPOSUELDO52")
    private BigInteger valortiposueldo52;
    @Column(name = "VALORTIPOSUELDO53")
    private BigInteger valortiposueldo53;
    @Column(name = "VALORTIPOSUELDO54")
    private BigInteger valortiposueldo54;
    @Column(name = "VALORTIPOSUELDO55")
    private BigInteger valortiposueldo55;
    @Column(name = "VALORTIPOSUELDO56")
    private BigInteger valortiposueldo56;
    @Column(name = "VALORTIPOSUELDO57")
    private BigInteger valortiposueldo57;
    @Column(name = "VALORTIPOSUELDO58")
    private BigInteger valortiposueldo58;
    @Column(name = "VALORTIPOSUELDO59")
    private BigInteger valortiposueldo59;
    @Column(name = "VALORTIPOSUELDO60")
    private BigInteger valortiposueldo60;
    @Column(name = "VALORTIPOSUELDO61")
    private BigInteger valortiposueldo61;
    @Column(name = "VALORTIPOSUELDO62")
    private BigInteger valortiposueldo62;
    @Column(name = "VALORTIPOSUELDO63")
    private BigInteger valortiposueldo63;
    @Column(name = "VALORTIPOSUELDO64")
    private BigInteger valortiposueldo64;
    @Column(name = "VALORTIPOSUELDO65")
    private BigInteger valortiposueldo65;
    @Column(name = "VALORTIPOSUELDO66")
    private BigInteger valortiposueldo66;
    @Column(name = "VALORTIPOSUELDO67")
    private BigInteger valortiposueldo67;
    @Column(name = "VALORTIPOSUELDO68")
    private BigInteger valortiposueldo68;
    @Column(name = "VALORTIPOSUELDO69")
    private BigInteger valortiposueldo69;
    @Column(name = "VALORTIPOSUELDO70")
    private BigInteger valortiposueldo70;
    @Column(name = "VALORTIPOSUELDO71")
    private BigInteger valortiposueldo71;
    @Column(name = "VALORTIPOSUELDO72")
    private BigInteger valortiposueldo72;
    @Column(name = "VALORTIPOSUELDO73")
    private BigInteger valortiposueldo73;
    @Column(name = "VALORTIPOSUELDO74")
    private BigInteger valortiposueldo74;
    @Column(name = "VALORTIPOSUELDO75")
    private BigInteger valortiposueldo75;
    @Column(name = "VALORTIPOSUELDO76")
    private BigInteger valortiposueldo76;
    @Column(name = "VALORTIPOSUELDO77")
    private BigInteger valortiposueldo77;
    @Column(name = "VALORTIPOSUELDO78")
    private BigInteger valortiposueldo78;
    @Column(name = "VALORTIPOSUELDO79")
    private BigInteger valortiposueldo79;
    @Column(name = "FECHARETIRO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecharetiro;
    @Size(max = 62)
    @Column(name = "NOMBRECOMPLETOEMPLEADO")
    private String nombrecompletoempleado;
    @Size(max = 4000)
    @Column(name = "TIPOPLANTA")
    private String tipoplanta;
    @Column(name = "EMPRESAESTRUCTURA")
    private Short empresaestructura;
    @Column(name = "FECHACONTRATACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechacontratacion;
    @Size(max = 30)
    @Column(name = "NOMBRETIPOCONTRATOCONTRATACION")
    private String nombretipocontratocontratacion;
    @Size(max = 60)
    @Column(name = "OBSERVACIONESCONTRATACION")
    private String observacionescontratacion;
    @Column(name = "FECHANACIMIENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechanacimiento;
    @Size(max = 4000)
    @Column(name = "AFILIACIONSALUD")
    private String afiliacionsalud;
    @Size(max = 4000)
    @Column(name = "AFILIACIONARP")
    private String afiliacionarp;
    @Size(max = 4000)
    @Column(name = "AFILIACIONPENSION")
    private String afiliacionpension;
    @Size(max = 4000)
    @Column(name = "AFILIACIONCESANTIAS")
    private String afiliacioncesantias;
    @Size(max = 4000)
    @Column(name = "AFILIACIONCAJA")
    private String afiliacioncaja;
    @Column(name = "PORCENTAJERIESGO")
    private BigInteger porcentajeriesgo;
    @Size(max = 50)
    @Column(name = "EMPRESANOMBRE")
    private String empresanombre;
    @Size(max = 30)
    @Column(name = "NOMBREMOTIVOCONTRATACION")
    private String nombremotivocontratacion;
    @Size(max = 30)
    @Column(name = "FORMAPAGOSUCURSALNOMBRE")
    private String formapagosucursalnombre;
    @Size(max = 4000)
    @Column(name = "FORMAPAGOMETODOPAGO")
    private String formapagometodopago;
    @Size(max = 4000)
    @Column(name = "JORNADALABORAL")
    private String jornadalaboral;
    @Size(max = 30)
    @Column(name = "NOMBRECENTROCOSTOLOCALIZACION")
    private String nombrecentrocostolocalizacion;
    @Column(name = "TOTALTIPOSSUELDOS")
    private BigInteger totaltipossueldos;
    @Size(max = 4000)
    @Column(name = "CODIGOALTERNOUBICACION")
    private String codigoalternoubicacion;
    @Size(max = 4000)
    @Column(name = "NOMBRECIUDADUBICACIONCORTE")
    private String nombreciudadubicacioncorte;
    @Size(max = 30)
    @Column(name = "TIPOCENTROCOSTOLOCALIZACION")
    private String tipocentrocostolocalizacion;
    @Column(name = "CERTIFICADOSALUD")
    private BigInteger certificadosalud;
    @Column(name = "FACTORDESALARIZACION")
    private BigInteger factordesalarizacion;
    @Column(name = "MINIMAFECHAINGRESO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date minimafechaingreso;
    @Size(max = 4000)
    @Column(name = "MOTIVORETIRO")
    private String motivoretiro;
    @Column(name = "ANTIGUEDADEMPLEADO")
    private BigInteger antiguedadempleado;
    @Column(name = "EDADPERSONA")
    private BigInteger edadpersona;
    @Size(max = 4000)
    @Column(name = "ESTADOCIVIL")
    private String estadocivil;
    @Column(name = "FECHAVENCIMIENTOCERTIFICADO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechavencimientocertificado;
    @Size(max = 15)
    @Column(name = "CERTIFICADOJUDICIAL")
    private String certificadojudicial;
    @Size(max = 4000)
    @Column(name = "LOCALIZACIONESTRUCTURAPADRE")
    private String localizacionestructurapadre;
    @Size(max = 4000)
    @Column(name = "CARGOCODIGOALTERNO")
    private String cargocodigoalterno;
    @Size(max = 4000)
    @Column(name = "CODIGOALTERNOCENTROCOSTO")
    private String codigoalternocentrocosto;
    @Size(max = 4000)
    @Column(name = "JEFE")
    private String jefe;
    @Size(max = 4000)
    @Column(name = "ESCALAFONSUBCATEGORIA")
    private String escalafonsubcategoria;
    @Size(max = 4000)
    @Column(name = "ESCALAFONBCATEGORIA")
    private String escalafonbcategoria;
    @Size(max = 4000)
    @Column(name = "ESCALAFON")
    private String escalafon;
    @Size(max = 30)
    @Column(name = "PAISNACIMIENTO")
    private String paisnacimiento;
    @Size(max = 4000)
    @Column(name = "TELEFONO")
    private String telefono;

    public QVWEmpleadosCorte() {
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getCodigoempleado() {
        return codigoempleado;
    }

    public void setCodigoempleado(BigInteger codigoempleado) {
        this.codigoempleado = codigoempleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrimerapellido() {
        return primerapellido;
    }

    public void setPrimerapellido(String primerapellido) {
        this.primerapellido = primerapellido;
    }

    public String getSegundoapellido() {
        return segundoapellido;
    }

    public void setSegundoapellido(String segundoapellido) {
        this.segundoapellido = segundoapellido;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getNumerodocumento() {
        return numerodocumento;
    }

    public void setNumerodocumento(Long numerodocumento) {
        this.numerodocumento = numerodocumento;
    }

    public String getTipodocumento() {
        return tipodocumento;
    }

    public void setTipodocumento(String tipodocumento) {
        this.tipodocumento = tipodocumento;
    }

    public String getCiudaddocumento() {
        return ciudaddocumento;
    }

    public void setCiudaddocumento(String ciudaddocumento) {
        this.ciudaddocumento = ciudaddocumento;
    }

    public String getDepartamentodocumento() {
        return departamentodocumento;
    }

    public void setDepartamentodocumento(String departamentodocumento) {
        this.departamentodocumento = departamentodocumento;
    }

    public String getCiudadnacimiento() {
        return ciudadnacimiento;
    }

    public void setCiudadnacimiento(String ciudadnacimiento) {
        this.ciudadnacimiento = ciudadnacimiento;
    }

    public String getDepartamentonacimiento() {
        return departamentonacimiento;
    }

    public void setDepartamentonacimiento(String departamentonacimiento) {
        this.departamentonacimiento = departamentonacimiento;
    }

    public String getReformalaboralnombre() {
        return reformalaboralnombre;
    }

    public void setReformalaboralnombre(String reformalaboralnombre) {
        this.reformalaboralnombre = reformalaboralnombre;
    }

    public Date getReformalaboralfechavigencia() {
        return reformalaboralfechavigencia;
    }

    public void setReformalaboralfechavigencia(Date reformalaboralfechavigencia) {
        this.reformalaboralfechavigencia = reformalaboralfechavigencia;
    }

    public String getCargonombre() {
        return cargonombre;
    }

    public void setCargonombre(String cargonombre) {
        this.cargonombre = cargonombre;
    }

    public Date getCargofechavigencia() {
        return cargofechavigencia;
    }

    public void setCargofechavigencia(Date cargofechavigencia) {
        this.cargofechavigencia = cargofechavigencia;
    }

    public String getMotivocambiocargo() {
        return motivocambiocargo;
    }

    public void setMotivocambiocargo(String motivocambiocargo) {
        this.motivocambiocargo = motivocambiocargo;
    }

    public String getNombreestructuracargo() {
        return nombreestructuracargo;
    }

    public void setNombreestructuracargo(String nombreestructuracargo) {
        this.nombreestructuracargo = nombreestructuracargo;
    }

    public String getLocalizacionnombre() {
        return localizacionnombre;
    }

    public void setLocalizacionnombre(String localizacionnombre) {
        this.localizacionnombre = localizacionnombre;
    }

    public Long getLocalizacioncodigo() {
        return localizacioncodigo;
    }

    public void setLocalizacioncodigo(Long localizacioncodigo) {
        this.localizacioncodigo = localizacioncodigo;
    }

    public Date getLocalizacionfechavigencia() {
        return localizacionfechavigencia;
    }

    public void setLocalizacionfechavigencia(Date localizacionfechavigencia) {
        this.localizacionfechavigencia = localizacionfechavigencia;
    }

    public String getMotivolocalizacion() {
        return motivolocalizacion;
    }

    public void setMotivolocalizacion(String motivolocalizacion) {
        this.motivolocalizacion = motivolocalizacion;
    }

    public String getCodigocentrocostolocalizacion() {
        return codigocentrocostolocalizacion;
    }

    public void setCodigocentrocostolocalizacion(String codigocentrocostolocalizacion) {
        this.codigocentrocostolocalizacion = codigocentrocostolocalizacion;
    }

    public String getOrigennombre() {
        return origennombre;
    }

    public void setOrigennombre(String origennombre) {
        this.origennombre = origennombre;
    }

    public Date getOrigenfechavigencia() {
        return origenfechavigencia;
    }

    public void setOrigenfechavigencia(Date origenfechavigencia) {
        this.origenfechavigencia = origenfechavigencia;
    }

    public String getNormanombre() {
        return normanombre;
    }

    public void setNormanombre(String normanombre) {
        this.normanombre = normanombre;
    }

    public Date getNormafechavigencia() {
        return normafechavigencia;
    }

    public void setNormafechavigencia(Date normafechavigencia) {
        this.normafechavigencia = normafechavigencia;
    }

    public Date getTipotrabajadorfechavigencia() {
        return tipotrabajadorfechavigencia;
    }

    public void setTipotrabajadorfechavigencia(Date tipotrabajadorfechavigencia) {
        this.tipotrabajadorfechavigencia = tipotrabajadorfechavigencia;
    }

    public String getTipotrabajadornombre() {
        return tipotrabajadornombre;
    }

    public void setTipotrabajadornombre(String tipotrabajadornombre) {
        this.tipotrabajadornombre = tipotrabajadornombre;
    }

    public String getTipotrabajadortipo() {
        return tipotrabajadortipo;
    }

    public void setTipotrabajadortipo(String tipotrabajadortipo) {
        this.tipotrabajadortipo = tipotrabajadortipo;
    }

    public String getFormapagonombre() {
        return formapagonombre;
    }

    public void setFormapagonombre(String formapagonombre) {
        this.formapagonombre = formapagonombre;
    }

    public Date getFormapagofechavigencia() {
        return formapagofechavigencia;
    }

    public void setFormapagofechavigencia(Date formapagofechavigencia) {
        this.formapagofechavigencia = formapagofechavigencia;
    }

    public String getFormapagotipocuenta() {
        return formapagotipocuenta;
    }

    public void setFormapagotipocuenta(String formapagotipocuenta) {
        this.formapagotipocuenta = formapagotipocuenta;
    }

    public String getFormapagocuenta() {
        return formapagocuenta;
    }

    public void setFormapagocuenta(String formapagocuenta) {
        this.formapagocuenta = formapagocuenta;
    }

    public String getContratonombre() {
        return contratonombre;
    }

    public void setContratonombre(String contratonombre) {
        this.contratonombre = contratonombre;
    }

    public Date getContratofechainicial() {
        return contratofechainicial;
    }

    public void setContratofechainicial(Date contratofechainicial) {
        this.contratofechainicial = contratofechainicial;
    }

    public Date getContratofechafinal() {
        return contratofechafinal;
    }

    public void setContratofechafinal(Date contratofechafinal) {
        this.contratofechafinal = contratofechafinal;
    }

    public String getMvrnombre() {
        return mvrnombre;
    }

    public void setMvrnombre(String mvrnombre) {
        this.mvrnombre = mvrnombre;
    }

    public Date getMvrechainicial() {
        return mvrechainicial;
    }

    public void setMvrechainicial(Date mvrechainicial) {
        this.mvrechainicial = mvrechainicial;
    }

    public Date getMvrfechafinal() {
        return mvrfechafinal;
    }

    public void setMvrfechafinal(Date mvrfechafinal) {
        this.mvrfechafinal = mvrfechafinal;
    }

    public BigDecimal getMvrvalor() {
        return mvrvalor;
    }

    public void setMvrvalor(BigDecimal mvrvalor) {
        this.mvrvalor = mvrvalor;
    }

    public String getTiposet() {
        return tiposet;
    }

    public void setTiposet(String tiposet) {
        this.tiposet = tiposet;
    }

    public Date getSetfechainicial() {
        return setfechainicial;
    }

    public void setSetfechainicial(Date setfechainicial) {
        this.setfechainicial = setfechainicial;
    }

    public Date getSetfechafinal() {
        return setfechafinal;
    }

    public void setSetfechafinal(Date setfechafinal) {
        this.setfechafinal = setfechafinal;
    }

    public BigDecimal getSetvalor() {
        return setvalor;
    }

    public void setSetvalor(BigDecimal setvalor) {
        this.setvalor = setvalor;
    }

    public BigDecimal getSetporcentaje() {
        return setporcentaje;
    }

    public void setSetporcentaje(BigDecimal setporcentaje) {
        this.setporcentaje = setporcentaje;
    }

    public Date getIbcfechainicial() {
        return ibcfechainicial;
    }

    public void setIbcfechainicial(Date ibcfechainicial) {
        this.ibcfechainicial = ibcfechainicial;
    }

    public Date getIbcfechafinal() {
        return ibcfechafinal;
    }

    public void setIbcfechafinal(Date ibcfechafinal) {
        this.ibcfechafinal = ibcfechafinal;
    }

    public BigDecimal getIbcvalor() {
        return ibcvalor;
    }

    public void setIbcvalor(BigDecimal ibcvalor) {
        this.ibcvalor = ibcvalor;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getInfoadicional1() {
        return infoadicional1;
    }

    public void setInfoadicional1(String infoadicional1) {
        this.infoadicional1 = infoadicional1;
    }

    public String getInfoadicional2() {
        return infoadicional2;
    }

    public void setInfoadicional2(String infoadicional2) {
        this.infoadicional2 = infoadicional2;
    }

    public String getInfoadicional3() {
        return infoadicional3;
    }

    public void setInfoadicional3(String infoadicional3) {
        this.infoadicional3 = infoadicional3;
    }

    public String getInfoadicional4() {
        return infoadicional4;
    }

    public void setInfoadicional4(String infoadicional4) {
        this.infoadicional4 = infoadicional4;
    }

    public String getInfoadicional5() {
        return infoadicional5;
    }

    public void setInfoadicional5(String infoadicional5) {
        this.infoadicional5 = infoadicional5;
    }

    public String getNiveleducativo() {
        return niveleducativo;
    }

    public void setNiveleducativo(String niveleducativo) {
        this.niveleducativo = niveleducativo;
    }

    public String getUbicaciongeografica() {
        return ubicaciongeografica;
    }

    public void setUbicaciongeografica(String ubicaciongeografica) {
        this.ubicaciongeografica = ubicaciongeografica;
    }

    public BigInteger getValortiposueldo1() {
        return valortiposueldo1;
    }

    public void setValortiposueldo1(BigInteger valortiposueldo1) {
        this.valortiposueldo1 = valortiposueldo1;
    }

    public BigInteger getValortiposueldo2() {
        return valortiposueldo2;
    }

    public void setValortiposueldo2(BigInteger valortiposueldo2) {
        this.valortiposueldo2 = valortiposueldo2;
    }

    public BigInteger getValortiposueldo3() {
        return valortiposueldo3;
    }

    public void setValortiposueldo3(BigInteger valortiposueldo3) {
        this.valortiposueldo3 = valortiposueldo3;
    }

    public BigInteger getValortiposueldo4() {
        return valortiposueldo4;
    }

    public void setValortiposueldo4(BigInteger valortiposueldo4) {
        this.valortiposueldo4 = valortiposueldo4;
    }

    public BigInteger getValortiposueldo5() {
        return valortiposueldo5;
    }

    public void setValortiposueldo5(BigInteger valortiposueldo5) {
        this.valortiposueldo5 = valortiposueldo5;
    }

    public BigInteger getValortiposueldo6() {
        return valortiposueldo6;
    }

    public void setValortiposueldo6(BigInteger valortiposueldo6) {
        this.valortiposueldo6 = valortiposueldo6;
    }

    public BigInteger getValortiposueldo7() {
        return valortiposueldo7;
    }

    public void setValortiposueldo7(BigInteger valortiposueldo7) {
        this.valortiposueldo7 = valortiposueldo7;
    }

    public BigInteger getValortiposueldo8() {
        return valortiposueldo8;
    }

    public void setValortiposueldo8(BigInteger valortiposueldo8) {
        this.valortiposueldo8 = valortiposueldo8;
    }

    public BigInteger getValortiposueldo9() {
        return valortiposueldo9;
    }

    public void setValortiposueldo9(BigInteger valortiposueldo9) {
        this.valortiposueldo9 = valortiposueldo9;
    }

    public BigInteger getValortiposueldo10() {
        return valortiposueldo10;
    }

    public void setValortiposueldo10(BigInteger valortiposueldo10) {
        this.valortiposueldo10 = valortiposueldo10;
    }

    public BigInteger getValortiposueldo11() {
        return valortiposueldo11;
    }

    public void setValortiposueldo11(BigInteger valortiposueldo11) {
        this.valortiposueldo11 = valortiposueldo11;
    }

    public BigInteger getValortiposueldo12() {
        return valortiposueldo12;
    }

    public void setValortiposueldo12(BigInteger valortiposueldo12) {
        this.valortiposueldo12 = valortiposueldo12;
    }

    public BigInteger getValortiposueldo13() {
        return valortiposueldo13;
    }

    public void setValortiposueldo13(BigInteger valortiposueldo13) {
        this.valortiposueldo13 = valortiposueldo13;
    }

    public BigInteger getValortiposueldo14() {
        return valortiposueldo14;
    }

    public void setValortiposueldo14(BigInteger valortiposueldo14) {
        this.valortiposueldo14 = valortiposueldo14;
    }

    public BigInteger getValortiposueldo15() {
        return valortiposueldo15;
    }

    public void setValortiposueldo15(BigInteger valortiposueldo15) {
        this.valortiposueldo15 = valortiposueldo15;
    }

    public BigInteger getValortiposueldo16() {
        return valortiposueldo16;
    }

    public void setValortiposueldo16(BigInteger valortiposueldo16) {
        this.valortiposueldo16 = valortiposueldo16;
    }

    public BigInteger getValortiposueldo17() {
        return valortiposueldo17;
    }

    public void setValortiposueldo17(BigInteger valortiposueldo17) {
        this.valortiposueldo17 = valortiposueldo17;
    }

    public BigInteger getValortiposueldo18() {
        return valortiposueldo18;
    }

    public void setValortiposueldo18(BigInteger valortiposueldo18) {
        this.valortiposueldo18 = valortiposueldo18;
    }

    public BigInteger getValortiposueldo19() {
        return valortiposueldo19;
    }

    public void setValortiposueldo19(BigInteger valortiposueldo19) {
        this.valortiposueldo19 = valortiposueldo19;
    }

    public BigInteger getValortiposueldo20() {
        return valortiposueldo20;
    }

    public void setValortiposueldo20(BigInteger valortiposueldo20) {
        this.valortiposueldo20 = valortiposueldo20;
    }

    public BigInteger getValortiposueldo21() {
        return valortiposueldo21;
    }

    public void setValortiposueldo21(BigInteger valortiposueldo21) {
        this.valortiposueldo21 = valortiposueldo21;
    }

    public BigInteger getValortiposueldo22() {
        return valortiposueldo22;
    }

    public void setValortiposueldo22(BigInteger valortiposueldo22) {
        this.valortiposueldo22 = valortiposueldo22;
    }

    public BigInteger getValortiposueldo23() {
        return valortiposueldo23;
    }

    public void setValortiposueldo23(BigInteger valortiposueldo23) {
        this.valortiposueldo23 = valortiposueldo23;
    }

    public BigInteger getValortiposueldo24() {
        return valortiposueldo24;
    }

    public void setValortiposueldo24(BigInteger valortiposueldo24) {
        this.valortiposueldo24 = valortiposueldo24;
    }

    public BigInteger getValortiposueldo25() {
        return valortiposueldo25;
    }

    public void setValortiposueldo25(BigInteger valortiposueldo25) {
        this.valortiposueldo25 = valortiposueldo25;
    }

    public BigInteger getValortiposueldo26() {
        return valortiposueldo26;
    }

    public void setValortiposueldo26(BigInteger valortiposueldo26) {
        this.valortiposueldo26 = valortiposueldo26;
    }

    public BigInteger getValortiposueldo27() {
        return valortiposueldo27;
    }

    public void setValortiposueldo27(BigInteger valortiposueldo27) {
        this.valortiposueldo27 = valortiposueldo27;
    }

    public BigInteger getValortiposueldo28() {
        return valortiposueldo28;
    }

    public void setValortiposueldo28(BigInteger valortiposueldo28) {
        this.valortiposueldo28 = valortiposueldo28;
    }

    public BigInteger getValortiposueldo29() {
        return valortiposueldo29;
    }

    public void setValortiposueldo29(BigInteger valortiposueldo29) {
        this.valortiposueldo29 = valortiposueldo29;
    }

    public BigInteger getValortiposueldo30() {
        return valortiposueldo30;
    }

    public void setValortiposueldo30(BigInteger valortiposueldo30) {
        this.valortiposueldo30 = valortiposueldo30;
    }

    public BigInteger getValortiposueldo31() {
        return valortiposueldo31;
    }

    public void setValortiposueldo31(BigInteger valortiposueldo31) {
        this.valortiposueldo31 = valortiposueldo31;
    }

    public BigInteger getValortiposueldo32() {
        return valortiposueldo32;
    }

    public void setValortiposueldo32(BigInteger valortiposueldo32) {
        this.valortiposueldo32 = valortiposueldo32;
    }

    public BigInteger getValortiposueldo33() {
        return valortiposueldo33;
    }

    public void setValortiposueldo33(BigInteger valortiposueldo33) {
        this.valortiposueldo33 = valortiposueldo33;
    }

    public BigInteger getValortiposueldo34() {
        return valortiposueldo34;
    }

    public void setValortiposueldo34(BigInteger valortiposueldo34) {
        this.valortiposueldo34 = valortiposueldo34;
    }

    public BigInteger getValortiposueldo35() {
        return valortiposueldo35;
    }

    public void setValortiposueldo35(BigInteger valortiposueldo35) {
        this.valortiposueldo35 = valortiposueldo35;
    }

    public BigInteger getValortiposueldo36() {
        return valortiposueldo36;
    }

    public void setValortiposueldo36(BigInteger valortiposueldo36) {
        this.valortiposueldo36 = valortiposueldo36;
    }

    public BigInteger getValortiposueldo37() {
        return valortiposueldo37;
    }

    public void setValortiposueldo37(BigInteger valortiposueldo37) {
        this.valortiposueldo37 = valortiposueldo37;
    }

    public BigInteger getValortiposueldo38() {
        return valortiposueldo38;
    }

    public void setValortiposueldo38(BigInteger valortiposueldo38) {
        this.valortiposueldo38 = valortiposueldo38;
    }

    public BigInteger getValortiposueldo39() {
        return valortiposueldo39;
    }

    public void setValortiposueldo39(BigInteger valortiposueldo39) {
        this.valortiposueldo39 = valortiposueldo39;
    }

    public BigInteger getValortiposueldo40() {
        return valortiposueldo40;
    }

    public void setValortiposueldo40(BigInteger valortiposueldo40) {
        this.valortiposueldo40 = valortiposueldo40;
    }

    public BigInteger getValortiposueldo41() {
        return valortiposueldo41;
    }

    public void setValortiposueldo41(BigInteger valortiposueldo41) {
        this.valortiposueldo41 = valortiposueldo41;
    }

    public BigInteger getValortiposueldo42() {
        return valortiposueldo42;
    }

    public void setValortiposueldo42(BigInteger valortiposueldo42) {
        this.valortiposueldo42 = valortiposueldo42;
    }

    public BigInteger getValortiposueldo43() {
        return valortiposueldo43;
    }

    public void setValortiposueldo43(BigInteger valortiposueldo43) {
        this.valortiposueldo43 = valortiposueldo43;
    }

    public BigInteger getValortiposueldo44() {
        return valortiposueldo44;
    }

    public void setValortiposueldo44(BigInteger valortiposueldo44) {
        this.valortiposueldo44 = valortiposueldo44;
    }

    public BigInteger getValortiposueldo45() {
        return valortiposueldo45;
    }

    public void setValortiposueldo45(BigInteger valortiposueldo45) {
        this.valortiposueldo45 = valortiposueldo45;
    }

    public BigInteger getValortiposueldo46() {
        return valortiposueldo46;
    }

    public void setValortiposueldo46(BigInteger valortiposueldo46) {
        this.valortiposueldo46 = valortiposueldo46;
    }

    public BigInteger getValortiposueldo47() {
        return valortiposueldo47;
    }

    public void setValortiposueldo47(BigInteger valortiposueldo47) {
        this.valortiposueldo47 = valortiposueldo47;
    }

    public BigInteger getValortiposueldo48() {
        return valortiposueldo48;
    }

    public void setValortiposueldo48(BigInteger valortiposueldo48) {
        this.valortiposueldo48 = valortiposueldo48;
    }

    public BigInteger getValortiposueldo49() {
        return valortiposueldo49;
    }

    public void setValortiposueldo49(BigInteger valortiposueldo49) {
        this.valortiposueldo49 = valortiposueldo49;
    }

    public BigInteger getValortiposueldo50() {
        return valortiposueldo50;
    }

    public void setValortiposueldo50(BigInteger valortiposueldo50) {
        this.valortiposueldo50 = valortiposueldo50;
    }

    public BigInteger getValortiposueldo51() {
        return valortiposueldo51;
    }

    public void setValortiposueldo51(BigInteger valortiposueldo51) {
        this.valortiposueldo51 = valortiposueldo51;
    }

    public BigInteger getValortiposueldo52() {
        return valortiposueldo52;
    }

    public void setValortiposueldo52(BigInteger valortiposueldo52) {
        this.valortiposueldo52 = valortiposueldo52;
    }

    public BigInteger getValortiposueldo53() {
        return valortiposueldo53;
    }

    public void setValortiposueldo53(BigInteger valortiposueldo53) {
        this.valortiposueldo53 = valortiposueldo53;
    }

    public BigInteger getValortiposueldo54() {
        return valortiposueldo54;
    }

    public void setValortiposueldo54(BigInteger valortiposueldo54) {
        this.valortiposueldo54 = valortiposueldo54;
    }

    public BigInteger getValortiposueldo55() {
        return valortiposueldo55;
    }

    public void setValortiposueldo55(BigInteger valortiposueldo55) {
        this.valortiposueldo55 = valortiposueldo55;
    }

    public BigInteger getValortiposueldo56() {
        return valortiposueldo56;
    }

    public void setValortiposueldo56(BigInteger valortiposueldo56) {
        this.valortiposueldo56 = valortiposueldo56;
    }

    public BigInteger getValortiposueldo57() {
        return valortiposueldo57;
    }

    public void setValortiposueldo57(BigInteger valortiposueldo57) {
        this.valortiposueldo57 = valortiposueldo57;
    }

    public BigInteger getValortiposueldo58() {
        return valortiposueldo58;
    }

    public void setValortiposueldo58(BigInteger valortiposueldo58) {
        this.valortiposueldo58 = valortiposueldo58;
    }

    public BigInteger getValortiposueldo59() {
        return valortiposueldo59;
    }

    public void setValortiposueldo59(BigInteger valortiposueldo59) {
        this.valortiposueldo59 = valortiposueldo59;
    }

    public BigInteger getValortiposueldo60() {
        return valortiposueldo60;
    }

    public void setValortiposueldo60(BigInteger valortiposueldo60) {
        this.valortiposueldo60 = valortiposueldo60;
    }

    public BigInteger getValortiposueldo61() {
        return valortiposueldo61;
    }

    public void setValortiposueldo61(BigInteger valortiposueldo61) {
        this.valortiposueldo61 = valortiposueldo61;
    }

    public BigInteger getValortiposueldo62() {
        return valortiposueldo62;
    }

    public void setValortiposueldo62(BigInteger valortiposueldo62) {
        this.valortiposueldo62 = valortiposueldo62;
    }

    public BigInteger getValortiposueldo63() {
        return valortiposueldo63;
    }

    public void setValortiposueldo63(BigInteger valortiposueldo63) {
        this.valortiposueldo63 = valortiposueldo63;
    }

    public BigInteger getValortiposueldo64() {
        return valortiposueldo64;
    }

    public void setValortiposueldo64(BigInteger valortiposueldo64) {
        this.valortiposueldo64 = valortiposueldo64;
    }

    public BigInteger getValortiposueldo65() {
        return valortiposueldo65;
    }

    public void setValortiposueldo65(BigInteger valortiposueldo65) {
        this.valortiposueldo65 = valortiposueldo65;
    }

    public BigInteger getValortiposueldo66() {
        return valortiposueldo66;
    }

    public void setValortiposueldo66(BigInteger valortiposueldo66) {
        this.valortiposueldo66 = valortiposueldo66;
    }

    public BigInteger getValortiposueldo67() {
        return valortiposueldo67;
    }

    public void setValortiposueldo67(BigInteger valortiposueldo67) {
        this.valortiposueldo67 = valortiposueldo67;
    }

    public BigInteger getValortiposueldo68() {
        return valortiposueldo68;
    }

    public void setValortiposueldo68(BigInteger valortiposueldo68) {
        this.valortiposueldo68 = valortiposueldo68;
    }

    public BigInteger getValortiposueldo69() {
        return valortiposueldo69;
    }

    public void setValortiposueldo69(BigInteger valortiposueldo69) {
        this.valortiposueldo69 = valortiposueldo69;
    }

    public BigInteger getValortiposueldo70() {
        return valortiposueldo70;
    }

    public void setValortiposueldo70(BigInteger valortiposueldo70) {
        this.valortiposueldo70 = valortiposueldo70;
    }

    public BigInteger getValortiposueldo71() {
        return valortiposueldo71;
    }

    public void setValortiposueldo71(BigInteger valortiposueldo71) {
        this.valortiposueldo71 = valortiposueldo71;
    }

    public BigInteger getValortiposueldo72() {
        return valortiposueldo72;
    }

    public void setValortiposueldo72(BigInteger valortiposueldo72) {
        this.valortiposueldo72 = valortiposueldo72;
    }

    public BigInteger getValortiposueldo73() {
        return valortiposueldo73;
    }

    public void setValortiposueldo73(BigInteger valortiposueldo73) {
        this.valortiposueldo73 = valortiposueldo73;
    }

    public BigInteger getValortiposueldo74() {
        return valortiposueldo74;
    }

    public void setValortiposueldo74(BigInteger valortiposueldo74) {
        this.valortiposueldo74 = valortiposueldo74;
    }

    public BigInteger getValortiposueldo75() {
        return valortiposueldo75;
    }

    public void setValortiposueldo75(BigInteger valortiposueldo75) {
        this.valortiposueldo75 = valortiposueldo75;
    }

    public BigInteger getValortiposueldo76() {
        return valortiposueldo76;
    }

    public void setValortiposueldo76(BigInteger valortiposueldo76) {
        this.valortiposueldo76 = valortiposueldo76;
    }

    public BigInteger getValortiposueldo77() {
        return valortiposueldo77;
    }

    public void setValortiposueldo77(BigInteger valortiposueldo77) {
        this.valortiposueldo77 = valortiposueldo77;
    }

    public BigInteger getValortiposueldo78() {
        return valortiposueldo78;
    }

    public void setValortiposueldo78(BigInteger valortiposueldo78) {
        this.valortiposueldo78 = valortiposueldo78;
    }

    public BigInteger getValortiposueldo79() {
        return valortiposueldo79;
    }

    public void setValortiposueldo79(BigInteger valortiposueldo79) {
        this.valortiposueldo79 = valortiposueldo79;
    }

    public Date getFecharetiro() {
        return fecharetiro;
    }

    public void setFecharetiro(Date fecharetiro) {
        this.fecharetiro = fecharetiro;
    }

    public String getNombrecompletoempleado() {
        return nombrecompletoempleado;
    }

    public void setNombrecompletoempleado(String nombrecompletoempleado) {
        this.nombrecompletoempleado = nombrecompletoempleado;
    }

    public String getTipoplanta() {
        return tipoplanta;
    }

    public void setTipoplanta(String tipoplanta) {
        this.tipoplanta = tipoplanta;
    }

    public Short getEmpresaestructura() {
        return empresaestructura;
    }

    public void setEmpresaestructura(Short empresaestructura) {
        this.empresaestructura = empresaestructura;
    }

    public Date getFechacontratacion() {
        return fechacontratacion;
    }

    public void setFechacontratacion(Date fechacontratacion) {
        this.fechacontratacion = fechacontratacion;
    }

    public String getNombretipocontratocontratacion() {
        return nombretipocontratocontratacion;
    }

    public void setNombretipocontratocontratacion(String nombretipocontratocontratacion) {
        this.nombretipocontratocontratacion = nombretipocontratocontratacion;
    }

    public String getObservacionescontratacion() {
        return observacionescontratacion;
    }

    public void setObservacionescontratacion(String observacionescontratacion) {
        this.observacionescontratacion = observacionescontratacion;
    }

    public Date getFechanacimiento() {
        return fechanacimiento;
    }

    public void setFechanacimiento(Date fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
    }

    public String getAfiliacionsalud() {
        return afiliacionsalud;
    }

    public void setAfiliacionsalud(String afiliacionsalud) {
        this.afiliacionsalud = afiliacionsalud;
    }

    public String getAfiliacionarp() {
        return afiliacionarp;
    }

    public void setAfiliacionarp(String afiliacionarp) {
        this.afiliacionarp = afiliacionarp;
    }

    public String getAfiliacionpension() {
        return afiliacionpension;
    }

    public void setAfiliacionpension(String afiliacionpension) {
        this.afiliacionpension = afiliacionpension;
    }

    public String getAfiliacioncesantias() {
        return afiliacioncesantias;
    }

    public void setAfiliacioncesantias(String afiliacioncesantias) {
        this.afiliacioncesantias = afiliacioncesantias;
    }

    public String getAfiliacioncaja() {
        return afiliacioncaja;
    }

    public void setAfiliacioncaja(String afiliacioncaja) {
        this.afiliacioncaja = afiliacioncaja;
    }

    public BigInteger getPorcentajeriesgo() {
        return porcentajeriesgo;
    }

    public void setPorcentajeriesgo(BigInteger porcentajeriesgo) {
        this.porcentajeriesgo = porcentajeriesgo;
    }

    public String getEmpresanombre() {
        return empresanombre;
    }

    public void setEmpresanombre(String empresanombre) {
        this.empresanombre = empresanombre;
    }

    public String getNombremotivocontratacion() {
        return nombremotivocontratacion;
    }

    public void setNombremotivocontratacion(String nombremotivocontratacion) {
        this.nombremotivocontratacion = nombremotivocontratacion;
    }

    public String getFormapagosucursalnombre() {
        return formapagosucursalnombre;
    }

    public void setFormapagosucursalnombre(String formapagosucursalnombre) {
        this.formapagosucursalnombre = formapagosucursalnombre;
    }

    public String getFormapagometodopago() {
        return formapagometodopago;
    }

    public void setFormapagometodopago(String formapagometodopago) {
        this.formapagometodopago = formapagometodopago;
    }

    public String getJornadalaboral() {
        return jornadalaboral;
    }

    public void setJornadalaboral(String jornadalaboral) {
        this.jornadalaboral = jornadalaboral;
    }

    public String getNombrecentrocostolocalizacion() {
        return nombrecentrocostolocalizacion;
    }

    public void setNombrecentrocostolocalizacion(String nombrecentrocostolocalizacion) {
        this.nombrecentrocostolocalizacion = nombrecentrocostolocalizacion;
    }

    public BigInteger getTotaltipossueldos() {
        return totaltipossueldos;
    }

    public void setTotaltipossueldos(BigInteger totaltipossueldos) {
        this.totaltipossueldos = totaltipossueldos;
    }

    public String getCodigoalternoubicacion() {
        return codigoalternoubicacion;
    }

    public void setCodigoalternoubicacion(String codigoalternoubicacion) {
        this.codigoalternoubicacion = codigoalternoubicacion;
    }

    public String getNombreciudadubicacioncorte() {
        return nombreciudadubicacioncorte;
    }

    public void setNombreciudadubicacioncorte(String nombreciudadubicacioncorte) {
        this.nombreciudadubicacioncorte = nombreciudadubicacioncorte;
    }

    public String getTipocentrocostolocalizacion() {
        return tipocentrocostolocalizacion;
    }

    public void setTipocentrocostolocalizacion(String tipocentrocostolocalizacion) {
        this.tipocentrocostolocalizacion = tipocentrocostolocalizacion;
    }

    public BigInteger getCertificadosalud() {
        return certificadosalud;
    }

    public void setCertificadosalud(BigInteger certificadosalud) {
        this.certificadosalud = certificadosalud;
    }

    public BigInteger getFactordesalarizacion() {
        return factordesalarizacion;
    }

    public void setFactordesalarizacion(BigInteger factordesalarizacion) {
        this.factordesalarizacion = factordesalarizacion;
    }

    public Date getMinimafechaingreso() {
        return minimafechaingreso;
    }

    public void setMinimafechaingreso(Date minimafechaingreso) {
        this.minimafechaingreso = minimafechaingreso;
    }

    public String getMotivoretiro() {
        return motivoretiro;
    }

    public void setMotivoretiro(String motivoretiro) {
        this.motivoretiro = motivoretiro;
    }

    public BigInteger getAntiguedadempleado() {
        return antiguedadempleado;
    }

    public void setAntiguedadempleado(BigInteger antiguedadempleado) {
        this.antiguedadempleado = antiguedadempleado;
    }

    public BigInteger getEdadpersona() {
        return edadpersona;
    }

    public void setEdadpersona(BigInteger edadpersona) {
        this.edadpersona = edadpersona;
    }

    public String getEstadocivil() {
        return estadocivil;
    }

    public void setEstadocivil(String estadocivil) {
        this.estadocivil = estadocivil;
    }

    public Date getFechavencimientocertificado() {
        return fechavencimientocertificado;
    }

    public void setFechavencimientocertificado(Date fechavencimientocertificado) {
        this.fechavencimientocertificado = fechavencimientocertificado;
    }

    public String getCertificadojudicial() {
        return certificadojudicial;
    }

    public void setCertificadojudicial(String certificadojudicial) {
        this.certificadojudicial = certificadojudicial;
    }

    public String getLocalizacionestructurapadre() {
        return localizacionestructurapadre;
    }

    public void setLocalizacionestructurapadre(String localizacionestructurapadre) {
        this.localizacionestructurapadre = localizacionestructurapadre;
    }

    public String getCargocodigoalterno() {
        return cargocodigoalterno;
    }

    public void setCargocodigoalterno(String cargocodigoalterno) {
        this.cargocodigoalterno = cargocodigoalterno;
    }

    public String getCodigoalternocentrocosto() {
        return codigoalternocentrocosto;
    }

    public void setCodigoalternocentrocosto(String codigoalternocentrocosto) {
        this.codigoalternocentrocosto = codigoalternocentrocosto;
    }

    public String getJefe() {
        return jefe;
    }

    public void setJefe(String jefe) {
        this.jefe = jefe;
    }

    public String getEscalafonsubcategoria() {
        return escalafonsubcategoria;
    }

    public void setEscalafonsubcategoria(String escalafonsubcategoria) {
        this.escalafonsubcategoria = escalafonsubcategoria;
    }

    public String getEscalafonbcategoria() {
        return escalafonbcategoria;
    }

    public void setEscalafonbcategoria(String escalafonbcategoria) {
        this.escalafonbcategoria = escalafonbcategoria;
    }

    public String getEscalafon() {
        return escalafon;
    }

    public void setEscalafon(String escalafon) {
        this.escalafon = escalafon;
    }

    public String getPaisnacimiento() {
        return paisnacimiento;
    }

    public void setPaisnacimiento(String paisnacimiento) {
        this.paisnacimiento = paisnacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}