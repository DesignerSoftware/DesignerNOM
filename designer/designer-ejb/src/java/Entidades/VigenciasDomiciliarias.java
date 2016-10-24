package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "VIGENCIASDOMICILIARIAS")
public class VigenciasDomiciliarias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigDecimal secuencia;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 10)
    @Column(name = "CALIFICACIONFAMILIAR")
    private String calificacionfamiliar;
    @Size(max = 200)
    @Column(name = "OBSERVACIONFAMILIAR")
    private String observacionfamiliar;
    @Size(max = 2000)
    @Column(name = "CONDICIONESGENERALES")
    private String condicionesgenerales;
    @Size(max = 15)
    @Column(name = "CONSTRUCCION")
    private String construccion;
    @Size(max = 1)
    @Column(name = "SERVICIOAGUA")
    private String servicioagua;
    @Size(max = 1)
    @Column(name = "SERVICIOLUZ")
    private String servicioluz;
    @Size(max = 1)
    @Column(name = "SERVICIOTELEFONO")
    private String serviciotelefono;
    @Size(max = 1)
    @Column(name = "SERVICIOPARABOLICA")
    private String servicioparabolica;
    @Size(max = 1)
    @Column(name = "SERVICIOTRANSPORTE")
    private String serviciotransporte;
    @Size(max = 1)
    @Column(name = "SERVICIOALCANTARILLADO")
    private String servicioalcantarillado;
    @Size(max = 1)
    @Column(name = "SERVICIOASEO")
    private String servicioaseo;
    @Size(max = 1)
    @Column(name = "SERVICIOOTROS")
    private String serviciootros;
    @Size(max = 200)
    @Column(name = "DETALLEOTROSSERVICIOS")
    private String detalleotrosservicios;
    @Size(max = 2000)
    @Column(name = "DISTRIBUCIONVIVIENDA")
    private String distribucionvivienda;
    @Size(max = 2000)
    @Column(name = "DESCRIPCIONVIVIENDA")
    private String descripcionvivienda;
    @Size(max = 200)
    @Column(name = "INGRESOS")
    private String ingresos;
    @Size(max = 1)
    @Column(name = "INGRESOPAPA")
    private String ingresopapa;
    @Size(max = 1)
    @Column(name = "INGRESOMAMA")
    private String ingresomama;
    @Size(max = 1)
    @Column(name = "INGRESOHERMANO")
    private String ingresohermano;
    @Size(max = 1)
    @Column(name = "INGRESOABUELO")
    private String ingresoabuelo;
    @Size(max = 1)
    @Column(name = "INGRESOTIO")
    private String ingresotio;
    @Size(max = 1)
    @Column(name = "INGRESOOTRO")
    private String ingresootro;
    @Size(max = 200)
    @Column(name = "DETALLEOTROINGRESO")
    private String detalleotroingreso;
    @Size(max = 1)
    @Column(name = "ORIGENINDEPENDIENTE")
    private String origenindependiente;
    @Size(max = 1)
    @Column(name = "ORIGENARRENDAMIENTO")
    private String origenarrendamiento;
    @Size(max = 1)
    @Column(name = "ORIGENPENSION")
    private String origenpension;
    @Size(max = 1)
    @Column(name = "ORIGENSALARIO")
    private String origensalario;
    @Size(max = 1)
    @Column(name = "ORIGENCDT")
    private String origencdt;
    @Size(max = 1)
    @Column(name = "ORIGENAUXILIOS")
    private String origenauxilios;
    @Size(max = 1)
    @Column(name = "INVERSIONEDUCACION")
    private String inversioneducacion;
    @Size(max = 1)
    @Column(name = "INVERSIONRECREACION")
    private String inversionrecreacion;
    @Size(max = 1)
    @Column(name = "INVERSIONALIMENTACION")
    private String inversionalimentacion;
    @Size(max = 1)
    @Column(name = "INVERSIONMEDICA")
    private String inversionmedica;
    @Size(max = 1)
    @Column(name = "INVERSIONARRIENDO")
    private String inversionarriendo;
    @Size(max = 1)
    @Column(name = "INVERSIONSERVICIOS")
    private String inversionservicios;
    @Size(max = 1)
    @Column(name = "INVERSIONOTROS")
    private String inversionotros;
    @Size(max = 200)
    @Column(name = "DETALLEOTRASINVERSIONES")
    private String detalleotrasinversiones;
    @Size(max = 2000)
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @Size(max = 2000)
    @Column(name = "CONCEPTOFINAL")
    private String conceptofinal;
    @Size(max = 2000)
    @Column(name = "CONCEPTOSOCIAL")
    private String conceptosocial;
    @Size(max = 15)
    @Column(name = "CONDICIONFAMILIAR")
    private String condicionfamiliar;
    @Size(max = 15)
    @Column(name = "CONDICIONSOCIAL")
    private String condicionsocial;
    @Size(max = 15)
    @Column(name = "SITUACIONECONOMICA")
    private String situacioneconomica;
    @Size(max = 15)
    @Column(name = "NIVELACADEMICO")
    private String nivelacademico;
    @Size(max = 15)
    @Column(name = "MOTIVACIONCARGO")
    private String motivacioncargo;
    @Size(max = 200)
    @Column(name = "PERSONASPRESENTES")
    private String personaspresentes;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "PROFESIONAL")
    private String profesional;
    @JoinColumn(name = "PERSONA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Personas persona;
    @Transient
    private boolean recagua;
    @Transient
    private boolean recluz;
    @Transient
    private boolean rectelefono;
    @Transient
    private boolean recparabolica;
    @Transient
    private boolean rectransporte;
    @Transient
    private boolean recalcantarillado;
    @Transient
    private boolean recaseo;
    @Transient
    private boolean recotro;
    @Transient
    private boolean origenind;
    @Transient
    private boolean origenarriendo;
    @Transient
    private boolean origenpen;
    @Transient
    private boolean origensal;
    @Transient
    private boolean origenncdt;
    @Transient
    private boolean origenaux;
    @Transient
    private boolean aportepadre;
    @Transient
    private boolean aportemadre;
    @Transient
    private boolean aportehermano;
    @Transient
    private boolean aporteabuelo;
    @Transient
    private boolean aportetio;
    @Transient
    private boolean aporteotro;
    @Transient
    private boolean egresoEducacion;
    @Transient
    private boolean egresoRecreacion;
    @Transient
    private boolean egresoAlimentacion;
    @Transient
    private boolean egresoMedico;
    @Transient
    private boolean egresoArriendo;
    @Transient
    private boolean egresoServicios;
    @Transient
    private boolean egresoOtros;
    
      

    public VigenciasDomiciliarias() {
    }

    public VigenciasDomiciliarias(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public VigenciasDomiciliarias(BigDecimal secuencia, String profesional) {
        this.secuencia = secuencia;
        this.profesional = profesional;
    }

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCalificacionfamiliar() {
        return calificacionfamiliar;
    }

    public void setCalificacionfamiliar(String calificacionfamiliar) {
        this.calificacionfamiliar = calificacionfamiliar;
    }

    public String getObservacionfamiliar() {
        return observacionfamiliar;
    }

    public void setObservacionfamiliar(String observacionfamiliar) {
        this.observacionfamiliar = observacionfamiliar;
    }

    public String getCondicionesgenerales() {
        return condicionesgenerales;
    }

    public void setCondicionesgenerales(String condicionesgenerales) {
        this.condicionesgenerales = condicionesgenerales;
    }

    public String getConstruccion() {
        return construccion;
    }

    public void setConstruccion(String construccion) {
        this.construccion = construccion;
        System.out.println("construcción seleccionada : " + construccion );
    }

    public String getServicioagua() {
        return servicioagua;
    }

    public void setServicioagua(String servicioagua) {
        this.servicioagua = servicioagua;
    }

    public String getServicioluz() {
        return servicioluz;
    }

    public void setServicioluz(String servicioluz) {
        this.servicioluz = servicioluz;
    }

    public String getServiciotelefono() {
        return serviciotelefono;
    }

    public void setServiciotelefono(String serviciotelefono) {
        this.serviciotelefono = serviciotelefono;
    }

    public String getServicioparabolica() {
        return servicioparabolica;
    }

    public void setServicioparabolica(String servicioparabolica) {
        this.servicioparabolica = servicioparabolica;
    }

    public String getServiciotransporte() {
        return serviciotransporte;
    }

    public void setServiciotransporte(String serviciotransporte) {
        this.serviciotransporte = serviciotransporte;
    }

    public String getServicioalcantarillado() {
        return servicioalcantarillado;
    }

    public void setServicioalcantarillado(String servicioalcantarillado) {
        this.servicioalcantarillado = servicioalcantarillado;
    }

    public String getServicioaseo() {
        return servicioaseo;
    }

    public void setServicioaseo(String servicioaseo) {
        this.servicioaseo = servicioaseo;
    }

    public String getServiciootros() {
        return serviciootros;
    }

    public void setServiciootros(String serviciootros) {
        this.serviciootros = serviciootros;
    }

    public String getDetalleotrosservicios() {
        return detalleotrosservicios;
    }

    public void setDetalleotrosservicios(String detalleotrosservicios) {
        this.detalleotrosservicios = detalleotrosservicios;
    }

    public String getDistribucionvivienda() {
        return distribucionvivienda;
    }

    public void setDistribucionvivienda(String distribucionvivienda) {
        this.distribucionvivienda = distribucionvivienda;
    }

    public String getDescripcionvivienda() {
        return descripcionvivienda;
    }

    public void setDescripcionvivienda(String descripcionvivienda) {
        this.descripcionvivienda = descripcionvivienda;
    }

    public String getIngresos() {
        return ingresos;
    }

    public void setIngresos(String ingresos) {
        this.ingresos = ingresos;
    }

    public String getIngresopapa() {
        return ingresopapa;
    }

    public void setIngresopapa(String ingresopapa) {
        this.ingresopapa = ingresopapa;
    }

    public String getIngresomama() {
        return ingresomama;
    }

    public void setIngresomama(String ingresomama) {
        this.ingresomama = ingresomama;
    }

    public String getIngresohermano() {
        return ingresohermano;
    }

    public void setIngresohermano(String ingresohermano) {
        this.ingresohermano = ingresohermano;
    }

    public String getIngresoabuelo() {
        return ingresoabuelo;
    }

    public void setIngresoabuelo(String ingresoabuelo) {
        this.ingresoabuelo = ingresoabuelo;
    }

    public String getIngresotio() {
        return ingresotio;
    }

    public void setIngresotio(String ingresotio) {
        this.ingresotio = ingresotio;
    }

    public String getIngresootro() {
        return ingresootro;
    }

    public void setIngresootro(String ingresootro) {
        this.ingresootro = ingresootro;
    }

    public String getDetalleotroingreso() {
        return detalleotroingreso;
    }

    public void setDetalleotroingreso(String detalleotroingreso) {
        this.detalleotroingreso = detalleotroingreso;
    }

    public String getOrigenindependiente() {
        return origenindependiente;
    }

    public void setOrigenindependiente(String origenindependiente) {
        this.origenindependiente = origenindependiente;
    }

    public String getOrigenarrendamiento() {
        return origenarrendamiento;
    }

    public void setOrigenarrendamiento(String origenarrendamiento) {
        this.origenarrendamiento = origenarrendamiento;
    }

    public String getOrigenpension() {
        return origenpension;
    }

    public void setOrigenpension(String origenpension) {
        this.origenpension = origenpension;
    }

    public String getOrigensalario() {
        return origensalario;
    }

    public void setOrigensalario(String origensalario) {
        this.origensalario = origensalario;
    }

    public String getOrigencdt() {
        return origencdt;
    }

    public void setOrigencdt(String origencdt) {
        this.origencdt = origencdt;
    }

    public String getOrigenauxilios() {
        return origenauxilios;
    }

    public void setOrigenauxilios(String origenauxilios) {
        this.origenauxilios = origenauxilios;
    }

    public String getInversioneducacion() {
        return inversioneducacion;
    }

    public void setInversioneducacion(String inversioneducacion) {
        this.inversioneducacion = inversioneducacion;
    }

    public String getInversionrecreacion() {
        return inversionrecreacion;
    }

    public void setInversionrecreacion(String inversionrecreacion) {
        this.inversionrecreacion = inversionrecreacion;
    }

    public String getInversionalimentacion() {
        return inversionalimentacion;
    }

    public void setInversionalimentacion(String inversionalimentacion) {
        this.inversionalimentacion = inversionalimentacion;
    }

    public String getInversionmedica() {
        return inversionmedica;
    }

    public void setInversionmedica(String inversionmedica) {
        this.inversionmedica = inversionmedica;
    }

    public String getInversionarriendo() {
        return inversionarriendo;
    }

    public void setInversionarriendo(String inversionarriendo) {
        this.inversionarriendo = inversionarriendo;
    }

    public String getInversionservicios() {
        return inversionservicios;
    }

    public void setInversionservicios(String inversionservicios) {
        this.inversionservicios = inversionservicios;
    }

    public String getInversionotros() {
        return inversionotros;
    }

    public void setInversionotros(String inversionotros) {
        this.inversionotros = inversionotros;
    }

    public String getDetalleotrasinversiones() {
        return detalleotrasinversiones;
    }

    public void setDetalleotrasinversiones(String detalleotrasinversiones) {
        this.detalleotrasinversiones = detalleotrasinversiones;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getConceptofinal() {
        return conceptofinal;
    }

    public void setConceptofinal(String conceptofinal) {
        this.conceptofinal = conceptofinal;
    }

    public String getConceptosocial() {
        return conceptosocial;
    }

    public void setConceptosocial(String conceptosocial) {
        this.conceptosocial = conceptosocial;
    }

    public String getCondicionfamiliar() {
        return condicionfamiliar;
    }

    public void setCondicionfamiliar(String condicionfamiliar) {
        this.condicionfamiliar = condicionfamiliar;
    }

    public String getCondicionsocial() {
        return condicionsocial;
    }

    public void setCondicionsocial(String condicionsocial) {
        this.condicionsocial = condicionsocial;
    }

    public String getSituacioneconomica() {
        return situacioneconomica;
    }

    public void setSituacioneconomica(String situacioneconomica) {
        this.situacioneconomica = situacioneconomica;
    }

    public String getNivelacademico() {
        return nivelacademico;
    }

    public void setNivelacademico(String nivelacademico) {
        this.nivelacademico = nivelacademico;
    }

    public String getMotivacioncargo() {
        return motivacioncargo;
    }

    public void setMotivacioncargo(String motivacioncargo) {
        this.motivacioncargo = motivacioncargo;
    }

    public String getPersonaspresentes() {
        return personaspresentes;
    }

    public void setPersonaspresentes(String personaspresentes) {
        this.personaspresentes = personaspresentes;
    }

    public String getProfesional() {
        return profesional;
    }

    public void setProfesional(String profesional) {
        this.profesional = profesional;
    }

    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
    }

    public boolean isRecagua() {
        if ("S".equals(servicioagua)) {
            recagua = true;
        } else {
            recagua = false;
        }
        return recagua;
    }

    public void setRecagua(boolean recagua) {
        this.recagua = recagua;
        if (recagua) {
            this.servicioagua = "S";
        } else {
            this.servicioagua = "N";
        }
        System.out.println("servicio agua: " + recagua + "" + servicioagua);
    }

    public boolean isRecluz() {
        if ("S".equals(servicioluz)) {
            recluz = true;
        } else {
            recluz = false;
        }
        return recluz;
    }

    public void setRecluz(boolean recluz) {
        this.recluz = recluz;
        if (recluz) {
            this.servicioluz = "S";
        } else {
            this.servicioluz = "N";
        }
        System.out.println("servicio luz: " + recluz + "" + servicioluz);
    }

    public boolean isRectelefono() {
         if ("S".equals(serviciotelefono)) {
            rectelefono = true;
        } else {
            rectelefono = false;
        }
        return rectelefono;
    }

    public void setRectelefono(boolean rectelefono) {
        this.rectelefono = rectelefono;
        if (rectelefono) {
            this.serviciotelefono = "S";
        } else {
            this.serviciotelefono = "N";
        }
        System.out.println("servicio Telefono: " + this.rectelefono + "" + serviciotelefono);
    }

    public boolean isRecparabolica() {
         if ("S".equals(servicioparabolica)) {
            recparabolica = true;
        } else {
            recparabolica = false;
        }
        return recparabolica;
    }

    public void setRecparabolica(boolean recparabolica) {
        this.recparabolica = recparabolica;
        if (recparabolica) {
            this.servicioparabolica = "S";
        } else {
            this.servicioparabolica = "N";
        }
        System.out.println("servicio Parabolica: " + this.recparabolica + "" + servicioparabolica);
    }

    public boolean isRectransporte() {
       if ("S".equals(serviciotransporte)) {
            rectransporte = true;
        } else {
            rectransporte = false;
        }
        return rectransporte;
    }

    public void setRectransporte(boolean rectransporte) {
        this.rectransporte = rectransporte;
        if (rectransporte) {
            this.serviciotransporte = "S";
        } else {
            this.serviciotransporte = "N";
        }
        System.out.println("servicio Transporte: " + this.rectransporte + "" + serviciotransporte);
    }

    public boolean isRecalcantarillado() {
         if ("S".equals(servicioalcantarillado)) {
            recalcantarillado = true;
        } else {
            recalcantarillado = false;
        }
        return recalcantarillado;
    }

    public void setRecalcantarillado(boolean recalcantarillado) {
        this.recalcantarillado = recalcantarillado;
        if (recalcantarillado) {
            this.servicioalcantarillado = "S";
        } else {
            this.servicioalcantarillado = "N";
        }
        System.out.println("servicio Alcantarillado: " + this.recalcantarillado + "" + servicioalcantarillado);
    }

    public boolean isRecaseo() {
         if ("S".equals(servicioaseo)) {
            recaseo = true;
        } else {
            recaseo = false;
        }
        return recaseo;
    }

    public void setRecaseo(boolean recaseo) {
        this.recaseo = recaseo;
        if (recaseo) {
            this.servicioaseo = "S";
        } else {
            this.servicioaseo = "N";
        }
        System.out.println("servicio Aseo: " + this.recaseo + "" + servicioaseo);
    }

    public boolean isRecotro() {
        if ("S".equals(serviciootros)) {
            recotro = true;
        } else {
            recotro = false;
        }
        return recotro;
    }

    public void setRecotro(boolean recotro) {
        this.recotro = recotro;
        if (recotro) {
            this.serviciootros = "S";
        } else {
            this.serviciootros = "N";
        }
        System.out.println("servicio Otro: " + this.recotro + "" + serviciootros);
    }

    public boolean isOrigenind() {
        if ("S".equals(origenindependiente)) {
            origenind = true;
        } else {
            origenind = false;
        }
        return origenind;
    }

    public void setOrigenind(boolean origenind) {
        this.origenind = origenind;
        if (origenind) {
            this.origenindependiente = "S";
        } else {
            this.origenindependiente = "N";
        }
        System.out.println("Origen comercio Independiente: " + this.origenind + "" + origenindependiente);
    }

    public boolean isOrigenarriendo() {
        if ("S".equals(origenarrendamiento)) {
            origenarriendo = true;
        } else {
            origenarriendo = false;
        }
        return origenarriendo;
    }

    public void setOrigenarriendo(boolean origenarriendo) {
        this.origenarriendo = origenarriendo;
        if (origenarriendo) {
            this.origenarrendamiento = "S";
        } else {
            this.origenarrendamiento = "N";
        }
        System.out.println("Origen Arrendamiento: " + this.origenarriendo + "" + origenarrendamiento);
    }

    public boolean isOrigenpen() {
        if ("S".equals(origenpension)) {
            origenpen = true;
        } else {
            origenpen = false;
        }
        return origenpen;
    }

    public void setOrigenpen(boolean origenpen) {
        this.origenpen = origenpen;
        if (origenpen) {
            this.origenpension = "S";
        } else {
            this.origenpension = "N";
        }
        System.out.println("Origen Pensión: " + this.origenpen + "" + origenpension);
    }

    public boolean isOrigensal() {
        if ("S".equals(origensalario)) {
            origensal = true;
        } else {
            origensal = false;
        }
        return origensal;
    }

    public void setOrigensal(boolean origensal) {
        this.origensal = origensal;
        if (origensal) {
            this.origensalario = "S";
        } else {
            this.origensalario = "N";
        }
        System.out.println("Origen Salario: " + this.origensal + "" + origensalario);
    }

    public boolean isOrigenncdt() {
        if ("S".equals(origencdt)) {
            origenncdt = true;
        } else {
            origenncdt = false;
        }
        return origenncdt;
    }

    public void setOrigenncdt(boolean origenncdt) {
        this.origenncdt = origenncdt;
        if (origenncdt) {
            this.origencdt = "S";
        } else {
            this.origencdt = "N";
        }
        System.out.println("Origen CDT: " + this.origenncdt + "" + origencdt);
    }

    public boolean isOrigenaux() {
        if ("S".equals(origenauxilios)) {
            origenaux = true;
        } else {
            origenaux = false;
        }
        return origenaux;
    }

    public void setOrigenaux(boolean origenaux) {
        this.origenaux = origenaux;
        if (origenaux) {
            this.origenauxilios = "S";
        } else {
            this.origenauxilios = "N";
        }
        System.out.println("Origen Auxilios: " + this.origenaux + "" + origenauxilios);
    }

    public boolean isAportepadre() {
        if ("S".equals(ingresopapa)) {
            aportepadre = true;
        } else {
            aportepadre = false;
        }
        return aportepadre;
    }

    public void setAportepadre(boolean aportepadre) {
        this.aportepadre = aportepadre;
        if (aportepadre) {
            this.ingresopapa = "S";
        } else {
            this.ingresopapa = "N";
        }
        System.out.println("Ingreso padre: " + this.aportepadre + "" + ingresopapa);
    }

    public boolean isAportemadre() {
        if ("S".equals(ingresomama)) {
            aportemadre = true;
        } else {
            aportemadre = false;
        }
        return aportemadre;
    }

    public void setAportemadre(boolean aportemadre) {
        this.aportemadre = aportemadre;
        if (aportemadre) {
            this.ingresomama = "S";
        } else {
            this.ingresomama = "N";
        }
        System.out.println("Ingreso madre: " + this.aportemadre + "" + ingresomama);
    }

    public boolean isAportehermano() {
        if ("S".equals(ingresohermano)) {
            aportehermano = true;
        } else {
            aportehermano = false;
        }
        return aportehermano;
    }

    public void setAportehermano(boolean aportehermano) {
        this.aportehermano = aportehermano;
        if (aportehermano) {
            this.ingresohermano = "S";
        } else {
            this.ingresohermano = "N";
        }
        System.out.println("Ingreso Hermano: " + this.aportehermano + "" + ingresohermano);
    }

    public boolean isAporteabuelo() {
        if ("S".equals(ingresoabuelo)) {
            aporteabuelo = true;
        } else {
            aporteabuelo = false;
        }
        return aporteabuelo;
    }

    public void setAporteabuelo(boolean aporteabuelo) {
        this.aporteabuelo = aporteabuelo;
        if (aporteabuelo) {
            this.ingresoabuelo = "S";
        } else {
            this.ingresoabuelo = "N";
        }
        System.out.println("Ingreso Abuelo: " + this.aporteabuelo + "" + ingresoabuelo);
    }

    public boolean isAportetio() {
        if ("S".equals(ingresotio)) {
            aportetio = true;
        } else {
            aportetio = false;
        }
        return aportetio;
    }

    public void setAportetio(boolean aportetio) {
        this.aportetio = aportetio;
        if (aportetio) {
            this.ingresotio = "S";
        } else {
            this.ingresotio = "N";
        }
        System.out.println("Ingreso Tío: " + this.aportetio + "" + ingresotio);
    }

    public boolean isAporteotro() {
        if ("S".equals(ingresootro)) {
            aporteotro = true;
        } else {
            aporteotro = false;
        }
        return aporteotro;
    }

    public void setAporteotro(boolean aporteotro) {
        this.aporteotro = aporteotro;
        if (aporteotro) {
            this.ingresootro = "S";
        } else {
            this.ingresootro = "N";
        }
        System.out.println("Ingreso Otro: " + this.aporteotro + "" + ingresootro);
    }

    public boolean isEgresoEducacion() {
        if ("S".equals(inversioneducacion)) {
            egresoEducacion = true;
        } else {
            egresoEducacion = false;
        }
        return egresoEducacion;
    }

    public void setEgresoEducacion(boolean egresoEducacion) {
        this.egresoEducacion = egresoEducacion;
        if (egresoEducacion) {
            this.inversioneducacion = "S";
        } else {
            this.inversioneducacion = "N";
        }
        System.out.println("Egreso Educación: " + this.egresoEducacion + "" + inversioneducacion);
    }

    public boolean isEgresoRecreacion() {
         if ("S".equals(inversionrecreacion)) {
            egresoRecreacion = true;
        } else {
            egresoRecreacion = false;
        }
        return egresoRecreacion;
    }

    public void setEgresoRecreacion(boolean egresoRecreacion) {
        this.egresoRecreacion = egresoRecreacion;
        if (egresoRecreacion) {
            this.inversionrecreacion = "S";
        } else {
            this.inversionrecreacion = "N";
        }
        System.out.println("Egreso Recreación: " + this.egresoRecreacion + "" + inversionrecreacion);
    }

    public boolean isEgresoAlimentacion() {
         if ("S".equals(inversionalimentacion)) {
            egresoAlimentacion = true;
        } else {
            egresoAlimentacion = false;
        }
        return egresoAlimentacion;
    }

    public void setEgresoAlimentacion(boolean egresoAlimentacion) {
        this.egresoAlimentacion = egresoAlimentacion;
        if (egresoAlimentacion) {
            this.inversionalimentacion = "S";
        } else {
            this.inversionalimentacion = "N";
        }
        System.out.println("Egreso Alimentación: " + this.egresoAlimentacion + "" + inversionalimentacion);
    }

    public boolean isEgresoMedico() {
         if ("S".equals(inversionmedica)) {
            egresoMedico = true;
        } else {
            egresoMedico = false;
        }
        return egresoMedico;
    }

    public void setEgresoMedico(boolean egresoMedico) {
        this.egresoMedico = egresoMedico;
        if (egresoMedico) {
            this.inversionmedica = "S";
        } else {
            this.inversionmedica = "N";
        }
        System.out.println("Egreso Médico: " + this.egresoMedico + "" + inversionmedica);
    }

    public boolean isEgresoArriendo() {
         if ("S".equals(inversionarriendo)) {
            egresoArriendo = true;
        } else {
            egresoArriendo = false;
        }
        return egresoArriendo;
    }

    public void setEgresoArriendo(boolean egresoArriendo) {
        this.egresoArriendo = egresoArriendo;
        if (egresoArriendo) {
            this.inversionarriendo = "S";
        } else {
            this.inversionarriendo = "N";
        }
        System.out.println("Egreso Arriendo: " + this.egresoArriendo + "" + inversionarriendo);
    }

    public boolean isEgresoServicios() {
        if ("S".equals(inversionservicios)) {
            egresoServicios = true;
        } else {
            egresoServicios = false;
        }
        return egresoServicios;
    }

    public void setEgresoServicios(boolean egresoServicios) {
        this.egresoServicios = egresoServicios;
        if (egresoServicios) {
            this.inversionservicios = "S";
        } else {
            this.inversionservicios = "N";
        }
        System.out.println("Egreso Servicios: " + this.egresoServicios + "" + inversionservicios);
    }

    public boolean isEgresoOtros() {
        if ("S".equals(inversionotros)) {
            egresoOtros = true;
        } else {
            egresoOtros = false;
        }
        return egresoOtros;
    }

    public void setEgresoOtros(boolean egresoOtros) {
        this.egresoOtros = egresoOtros;
        if (egresoOtros) {
            this.inversionotros = "S";
        } else {
            this.inversionotros = "N";
        }
        System.out.println("Egreso Otros: " + this.egresoOtros + "" + inversionotros);
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
        if (!(object instanceof VigenciasDomiciliarias)) {
            return false;
        }
        VigenciasDomiciliarias other = (VigenciasDomiciliarias) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.VigenciasDomiciliarias[ secuencia=" + secuencia + " ]";
    }

}
