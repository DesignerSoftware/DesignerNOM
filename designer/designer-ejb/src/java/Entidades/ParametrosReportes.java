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
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "PARAMETROSREPORTES")
public class ParametrosReportes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "USUARIO")
    private String usuario;
    @Column(name = "FECHADESDE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechadesde;
    @Column(name = "FECHAHASTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechahasta;
    @Column(name = "CODIGOEMPLEADODESDE")
    private BigDecimal codigoempleadodesde;
    @Column(name = "CODIGOEMPLEADOHASTA")
    private BigDecimal codigoempleadohasta;
    @Column(name = "CONCEPTO")
    private BigInteger concepto;
    @Size(max = 15)
    @Column(name = "TIPOPERSONAL")
    private String tipopersonal;
    @Size(max = 1)
    @Column(name = "GENERAPLANO")
    private String generaplano;
    @Column(name = "NUMERODOCUMENTO")
    private Long numerodocumento;
    @Column(name = "CONSECUTIVOELEMENTO")
    private Integer consecutivoelemento;
    @Column(name = "FECHAINICONTABILIZACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechainicontabilizacion;
    @Column(name = "FECHAFINCONTABILIZACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechafincontabilizacion;
    @Size(max = 100)
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @Column(name = "CIUDADNOM")
    private BigInteger ciudadnom;
    @Column(name = "EVALEMPLEADO")
    private BigInteger evalempleado;
    @Column(name = "EVALINDAGACION")
    private BigInteger evalindagacion;
    @Size(max = 20)
    @Column(name = "NUMEROPATRONAL")
    private String numeropatronal;
    @Column(name = "SEXO")
    private Character sexo;
    @Column(name = "EDAD")
    private BigInteger edad;
    @Column(name = "EDADMAX")
    private BigInteger edadmax;
    @Size(max = 100)
    @Column(name = "RODAMINETO")
    private String rodamineto;
    @Column(name = "FECHACORTE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechacorte;
    @Size(max = 15)
    @Column(name = "NUMEROCUENTACREDI")
    private String numerocuentacredi;
    @Size(max = 1)
    @Column(name = "TIPOCUENTACREDI")
    private String tipocuentacredi;
    @Size(max = 100)
    @Column(name = "FONDOCUMPLEANOS")
    private String fondocumpleanos;
    @Size(max = 100)
    @Column(name = "EVALUADOR")
    private String evaluador;
    @Column(name = "FECHACONVOCATORIAS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaconvocatorias;
    @Size(max = 100)
    @Column(name = "MENSAJEDESPRENDIBLE")
    private String mensajedesprendible;
    @Column(name = "PDGPOLITICA")
    private BigInteger pdgpolitica;
    @Column(name = "PDGESTRATEGIA")
    private BigInteger pdgestrategia;
    @JoinColumn(name = "NOMBREGERENTE", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empleados nombregerente;
    @JoinColumn(name = "UBICACIONGEOGRAFICA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private UbicacionesGeograficas ubicaciongeografica;
    @JoinColumn(name = "TIPOTRABAJADOR", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private TiposTrabajadores tipotrabajador;
    @JoinColumn(name = "TIPOTELEFONO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private TiposTelefonos tipotelefono;
    @JoinColumn(name = "TIPOPRESTAMO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Tiposprestamos tipoprestamo;
    @JoinColumn(name = "NIVELEDUCATIVO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private TiposEducaciones niveleducativo;
    @JoinColumn(name = "TIPOASOCIACION", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private TiposAsociaciones tipoasociacion;
    @JoinColumn(name = "TERCERO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Terceros tercero;
    @JoinColumn(name = "SUCURSAL_PILA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private SucursalesPila sucursalPila;
    @JoinColumn(name = "PROCESO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Procesos proceso;
    @JoinColumn(name = "IDIOMA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Idiomas idioma;
    @JoinColumn(name = "GRUPO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private GruposConceptos grupo;
    @JoinColumn(name = "ACTIVIDADBIENESTAR", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Actividades actividadbienestar;
    @JoinColumn(name = "AFICION", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Aficiones aficion;
    @JoinColumn(name = "ASOCIACION", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Asociaciones asociacion;
    @JoinColumn(name = "BANCO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Bancos banco;
    @JoinColumn(name = "CARGO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Cargos cargo;
    @JoinColumn(name = "CIUDAD", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Ciudades ciudad;
    @JoinColumn(name = "DEPORTE", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Deportes deporte;
    @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empresas empresa;
    @JoinColumn(name = "ESTADOCIVIL", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private EstadosCiviles estadocivil;
    @JoinColumn(name = "LOCALIZACION", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Estructuras localizacion;
    @JoinColumn(name = "CONVOCATORIA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Evalconvocatorias convocatoria;
    @JoinColumn(name = "EVALPLANILLA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Evalplanillas evalplanilla;
    @Size(max = 20)
    @Column(name = "TIPOPARATRABAJADOR")
    private String tipoparatrabajador;
    @Size(max = 10)
    @Column(name = "ESTADOSOLUCIONNODO")
    private String estadosolucionnodo;
    @Transient
    private String estadoPersonal;
    @Transient
    private String estadoSN;

    public ParametrosReportes() {
    }

    public ParametrosReportes(String usuario) {
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getFechadesde() {
        return fechadesde;
    }

    public void setFechadesde(Date fechadesde) {
        this.fechadesde = fechadesde;
    }

    public Date getFechahasta() {
        return fechahasta;
    }

    public void setFechahasta(Date fechahasta) {
        this.fechahasta = fechahasta;
    }

    public BigDecimal getCodigoempleadodesde() {
        return codigoempleadodesde;
    }

    public void setCodigoempleadodesde(BigDecimal codigoempleadodesde) {
        this.codigoempleadodesde = codigoempleadodesde;
    }

    public BigDecimal getCodigoempleadohasta() {
        return codigoempleadohasta;
    }

    public void setCodigoempleadohasta(BigDecimal codigoempleadohasta) {
        this.codigoempleadohasta = codigoempleadohasta;
    }

    public BigInteger getConcepto() {
        return concepto;
    }

    public void setConcepto(BigInteger concepto) {
        this.concepto = concepto;
    }

    public String getTipopersonal() {
        return tipopersonal;
    }

    public void setTipopersonal(String tipopersonal) {
        this.tipopersonal = tipopersonal;
    }

    public String getGeneraplano() {
        return generaplano;
    }

    public void setGeneraplano(String generaplano) {
        this.generaplano = generaplano;
    }

    public String getTipoparatrabajador() {
        return tipoparatrabajador;
    }

    public void setTipoparatrabajador(String tipoparatrabajador) {
        this.tipoparatrabajador = tipoparatrabajador;
    }

    public Long getNumerodocumento() {
        return numerodocumento;
    }

    public void setNumerodocumento(Long numerodocumento) {
        this.numerodocumento = numerodocumento;
    }

    public Integer getConsecutivoelemento() {
        return consecutivoelemento;
    }

    public void setConsecutivoelemento(Integer consecutivoelemento) {
        this.consecutivoelemento = consecutivoelemento;
    }

    public Date getFechainicontabilizacion() {
        return fechainicontabilizacion;
    }

    public void setFechainicontabilizacion(Date fechainicontabilizacion) {
        this.fechainicontabilizacion = fechainicontabilizacion;
    }

    public Date getFechafincontabilizacion() {
        return fechafincontabilizacion;
    }

    public void setFechafincontabilizacion(Date fechafincontabilizacion) {
        this.fechafincontabilizacion = fechafincontabilizacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public BigInteger getCiudadnom() {
        return ciudadnom;
    }

    public void setCiudadnom(BigInteger ciudadnom) {
        this.ciudadnom = ciudadnom;
    }

    public String getEstadosolucionnodo() {
        return estadosolucionnodo;
    }

    public void setEstadosolucionnodo(String estadosolucionnodo) {
        this.estadosolucionnodo = estadosolucionnodo;
    }

    public BigInteger getEvalempleado() {
        return evalempleado;
    }

    public void setEvalempleado(BigInteger evalempleado) {
        this.evalempleado = evalempleado;
    }

    public BigInteger getEvalindagacion() {
        return evalindagacion;
    }

    public void setEvalindagacion(BigInteger evalindagacion) {
        this.evalindagacion = evalindagacion;
    }

    public String getNumeropatronal() {
        return numeropatronal;
    }

    public void setNumeropatronal(String numeropatronal) {
        this.numeropatronal = numeropatronal;
    }

    public Character getSexo() {
        return sexo;
    }

    public void setSexo(Character sexo) {
        this.sexo = sexo;
    }

    public BigInteger getEdad() {
        return edad;
    }

    public void setEdad(BigInteger edad) {
        this.edad = edad;
    }

    public BigInteger getEdadmax() {
        return edadmax;
    }

    public void setEdadmax(BigInteger edadmax) {
        this.edadmax = edadmax;
    }

    public String getRodamineto() {
        return rodamineto;
    }

    public void setRodamineto(String rodamineto) {
        this.rodamineto = rodamineto;
    }

    public Date getFechacorte() {
        return fechacorte;
    }

    public void setFechacorte(Date fechacorte) {
        this.fechacorte = fechacorte;
    }

    public String getNumerocuentacredi() {
        return numerocuentacredi;
    }

    public void setNumerocuentacredi(String numerocuentacredi) {
        this.numerocuentacredi = numerocuentacredi;
    }

    public String getTipocuentacredi() {
        return tipocuentacredi;
    }

    public void setTipocuentacredi(String tipocuentacredi) {
        this.tipocuentacredi = tipocuentacredi;
    }

    public String getFondocumpleanos() {
        return fondocumpleanos;
    }

    public void setFondocumpleanos(String fondocumpleanos) {
        this.fondocumpleanos = fondocumpleanos;
    }

    public String getEvaluador() {
        return evaluador;
    }

    public void setEvaluador(String evaluador) {
        this.evaluador = evaluador;
    }

    public Date getFechaconvocatorias() {
        return fechaconvocatorias;
    }

    public void setFechaconvocatorias(Date fechaconvocatorias) {
        this.fechaconvocatorias = fechaconvocatorias;
    }

    public String getMensajedesprendible() {
        return mensajedesprendible;
    }

    public void setMensajedesprendible(String mensajedesprendible) {
        this.mensajedesprendible = mensajedesprendible;
    }

    public BigInteger getPdgpolitica() {
        return pdgpolitica;
    }

    public void setPdgpolitica(BigInteger pdgpolitica) {
        this.pdgpolitica = pdgpolitica;
    }

    public BigInteger getPdgestrategia() {
        return pdgestrategia;
    }

    public void setPdgestrategia(BigInteger pdgestrategia) {
        this.pdgestrategia = pdgestrategia;
    }

    public UbicacionesGeograficas getUbicaciongeografica() {
        return ubicaciongeografica;
    }

    public void setUbicaciongeografica(UbicacionesGeograficas ubicaciongeografica) {
        this.ubicaciongeografica = ubicaciongeografica;
    }

    public TiposTrabajadores getTipotrabajador() {
        return tipotrabajador;
    }

    public void setTipotrabajador(TiposTrabajadores tipotrabajador) {
        this.tipotrabajador = tipotrabajador;
    }

    public TiposTelefonos getTipotelefono() {
        return tipotelefono;
    }

    public void setTipotelefono(TiposTelefonos tipotelefono) {
        this.tipotelefono = tipotelefono;
    }

    public Tiposprestamos getTipoprestamo() {
        return tipoprestamo;
    }

    public void setTipoprestamo(Tiposprestamos tipoprestamo) {
        this.tipoprestamo = tipoprestamo;
    }

    public TiposEducaciones getNiveleducativo() {
        return niveleducativo;
    }

    public void setNiveleducativo(TiposEducaciones niveleducativo) {
        this.niveleducativo = niveleducativo;
    }

    public TiposAsociaciones getTipoasociacion() {
        return tipoasociacion;
    }

    public void setTipoasociacion(TiposAsociaciones tipoasociacion) {
        this.tipoasociacion = tipoasociacion;
    }

    public Terceros getTercero() {
        return tercero;
    }

    public void setTercero(Terceros tercero) {
        this.tercero = tercero;
    }

    public SucursalesPila getSucursalPila() {
        return sucursalPila;
    }

    public void setSucursalPila(SucursalesPila sucursalPila) {
        this.sucursalPila = sucursalPila;
    }

    public Procesos getProceso() {
        return proceso;
    }

    public void setProceso(Procesos proceso) {
        this.proceso = proceso;
    }

    public Idiomas getIdioma() {
        return idioma;
    }

    public void setIdioma(Idiomas idioma) {
        this.idioma = idioma;
    }

    public GruposConceptos getGrupo() {
        return grupo;
    }

    public void setGrupo(GruposConceptos grupo) {
        this.grupo = grupo;
    }

    public Actividades getActividadbienestar() {
        return actividadbienestar;
    }

    public void setActividadbienestar(Actividades actividadbienestar) {
        this.actividadbienestar = actividadbienestar;
    }

    public Aficiones getAficion() {
        return aficion;
    }

    public void setAficion(Aficiones aficion) {
        this.aficion = aficion;
    }

    public Asociaciones getAsociacion() {
        return asociacion;
    }

    public void setAsociacion(Asociaciones asociacion) {
        this.asociacion = asociacion;
    }

    public Bancos getBanco() {
        return banco;
    }

    public void setBanco(Bancos banco) {
        this.banco = banco;
    }

    public Cargos getCargo() {
        return cargo;
    }

    public void setCargo(Cargos cargo) {
        this.cargo = cargo;
    }

    public Ciudades getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudades ciudad) {
        this.ciudad = ciudad;
    }

    public Deportes getDeporte() {
        return deporte;
    }

    public void setDeporte(Deportes deporte) {
        this.deporte = deporte;
    }

    public Empresas getEmpresa() {
        if (empresa == null) {
            empresa = new Empresas();
        }
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public EstadosCiviles getEstadocivil() {
        return estadocivil;
    }

    public void setEstadocivil(EstadosCiviles estadocivil) {
        this.estadocivil = estadocivil;
    }

    public Estructuras getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(Estructuras localizacion) {
        this.localizacion = localizacion;
    }

    public Evalconvocatorias getConvocatoria() {
        return convocatoria;
    }

    public void setConvocatoria(Evalconvocatorias convocatoria) {
        this.convocatoria = convocatoria;
    }

    public Evalplanillas getEvalplanilla() {
        return evalplanilla;
    }

    public void setEvalplanilla(Evalplanillas evalplanilla) {
        this.evalplanilla = evalplanilla;
    }

    public String getEstadoPersonal() {
        if(tipoparatrabajador == null){
            this.estadoPersonal = " ";
        } else {
            estadoPersonal = tipoparatrabajador;
        }
        return estadoPersonal;
    }

    public void setEstadoPersonal(String estadoPersonal) {
        this.estadoPersonal = estadoPersonal;
        if(estadoPersonal.trim().isEmpty()){
            this.tipoparatrabajador = null;
        } else {
            this.tipoparatrabajador = estadoPersonal;
        }
    }

    public String getEstadoSN() {
        if(estadosolucionnodo == null){
            this.estadoSN = " ";
        } else {
            estadoSN = estadosolucionnodo;
        }
        return estadoSN;
    }

    public void setEstadoSN(String estadoSN) {
        this.estadoSN = estadoSN;
        if(estadoSN.trim().isEmpty()){
            this.estadosolucionnodo = null;
        } else {
            this.estadosolucionnodo = estadoSN;
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuario != null ? usuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ParametrosReportes)) {
            return false;
        }
        ParametrosReportes other = (ParametrosReportes) object;
        if ((this.usuario == null && other.usuario != null) || (this.usuario != null && !this.usuario.equals(other.usuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.ParametrosReportes[ usuario=" + usuario + " ]";
    }

    public Empleados getNombregerente() {
        if (nombregerente == null) {
            nombregerente = new Empleados();
        }
        return nombregerente;
    }

    public void setNombregerente(Empleados nombregerente) {
        this.nombregerente = nombregerente;
    }

}
