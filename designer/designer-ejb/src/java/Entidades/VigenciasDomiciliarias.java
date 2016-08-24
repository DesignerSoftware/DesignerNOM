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
