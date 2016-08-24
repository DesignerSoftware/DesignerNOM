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
@Table(name = "SOACCIDENTES")
public class Soaccidentes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "HORA")
    private Short hora;
    @Size(max = 7)
    @Column(name = "EXPEDIENTE")
    private String expediente;
    @Column(name = "NOTIFICACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date notificacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "LUGARTRABAJO")
    private String lugartrabajo;
    @Size(max = 2000)
    @Column(name = "DETALLELUGAR")
    private String detallelugar;
    @Size(max = 1)
    @Column(name = "PRIMEROSAUXILIOS")
    private String primerosauxilios;
    @Size(max = 2000)
    @Column(name = "PORQUENO")
    private String porqueno;
    @Column(name = "HORAINICIO")
    private Short horainicio;
    @Column(name = "HORAFINAL")
    private Short horafinal;
    @Size(max = 2000)
    @Column(name = "DETALLE")
    private String detalle;
    @Size(max = 2000)
    @Column(name = "PROCEDIMIENTO")
    private String procedimiento;
    @Column(name = "PERSONAPRIMEROSAUXILIOS")
    private BigInteger personaprimerosauxilios;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "TAREAREALIZADA")
    private String tarearealizada;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "DESCRIPCIONCASO")
    private String descripcioncaso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "DETALLEPROTECCION")
    private String detalleproteccion;
    @Column(name = "EPS")
    private BigInteger eps;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "DIA")
    private String dia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "ESTADOPROTECCION")
    private String estadoproteccion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "ESTADOPROTECCIONLABOR")
    private String estadoproteccionlabor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "ANALISISPOSIBLECAUSA")
    private String analisisposiblecausa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "RECOMENDACION")
    private String recomendacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "HORAANTES")
    private String horaantes;
    @JoinColumn(name = "JEFE", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Personas jefe;
    @JoinColumn(name = "TESTIGO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Personas testigo;
    @JoinColumn(name = "PERSONAREPORTA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Personas personareporta;
    @JoinColumn(name = "JEFEAREA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Personas jefearea;
    @JoinColumn(name = "SITIOOCURRENCIA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private LugaresOcurrencias sitioocurrencia;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empleados empleado;
    @JoinColumn(name = "CAUSA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private ElementosCausasAccidentes causa;

    public Soaccidentes() {
    }

    public Soaccidentes(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Soaccidentes(BigInteger secuencia, Date fecha, String lugartrabajo, String tarearealizada, String descripcioncaso, String detalleproteccion, String dia, String estadoproteccion, String estadoproteccionlabor, String analisisposiblecausa, String recomendacion, String horaantes) {
        this.secuencia = secuencia;
        this.fecha = fecha;
        this.lugartrabajo = lugartrabajo;
        this.tarearealizada = tarearealizada;
        this.descripcioncaso = descripcioncaso;
        this.detalleproteccion = detalleproteccion;
        this.dia = dia;
        this.estadoproteccion = estadoproteccion;
        this.estadoproteccionlabor = estadoproteccionlabor;
        this.analisisposiblecausa = analisisposiblecausa;
        this.recomendacion = recomendacion;
        this.horaantes = horaantes;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Short getHora() {
        return hora;
    }

    public void setHora(Short hora) {
        this.hora = hora;
    }

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    public Date getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(Date notificacion) {
        this.notificacion = notificacion;
    }

    public String getLugartrabajo() {
        return lugartrabajo;
    }

    public void setLugartrabajo(String lugartrabajo) {
        this.lugartrabajo = lugartrabajo;
    }

    public String getDetallelugar() {
        return detallelugar;
    }

    public void setDetallelugar(String detallelugar) {
        this.detallelugar = detallelugar;
    }

    public String getPrimerosauxilios() {
        return primerosauxilios;
    }

    public void setPrimerosauxilios(String primerosauxilios) {
        this.primerosauxilios = primerosauxilios;
    }

    public String getPorqueno() {
        return porqueno;
    }

    public void setPorqueno(String porqueno) {
        this.porqueno = porqueno;
    }

    public Short getHorainicio() {
        return horainicio;
    }

    public void setHorainicio(Short horainicio) {
        this.horainicio = horainicio;
    }

    public Short getHorafinal() {
        return horafinal;
    }

    public void setHorafinal(Short horafinal) {
        this.horafinal = horafinal;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
    }

    public BigInteger getPersonaprimerosauxilios() {
        return personaprimerosauxilios;
    }

    public void setPersonaprimerosauxilios(BigInteger personaprimerosauxilios) {
        this.personaprimerosauxilios = personaprimerosauxilios;
    }

    public String getTarearealizada() {
        return tarearealizada;
    }

    public void setTarearealizada(String tarearealizada) {
        this.tarearealizada = tarearealizada;
    }

    public String getDescripcioncaso() {
        if(descripcioncaso == null){
            descripcioncaso = (" ");
        }
        return descripcioncaso;
    }

    public void setDescripcioncaso(String descripcioncaso) {
        this.descripcioncaso = descripcioncaso;
    }

    public String getDetalleproteccion() {
        return detalleproteccion;
    }

    public void setDetalleproteccion(String detalleproteccion) {
        this.detalleproteccion = detalleproteccion;
    }

    public BigInteger getEps() {
        return eps;
    }

    public void setEps(BigInteger eps) {
        this.eps = eps;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getEstadoproteccion() {
        return estadoproteccion;
    }

    public void setEstadoproteccion(String estadoproteccion) {
        this.estadoproteccion = estadoproteccion;
    }

    public String getEstadoproteccionlabor() {
        return estadoproteccionlabor;
    }

    public void setEstadoproteccionlabor(String estadoproteccionlabor) {
        this.estadoproteccionlabor = estadoproteccionlabor;
    }

    public String getAnalisisposiblecausa() {
        return analisisposiblecausa;
    }

    public void setAnalisisposiblecausa(String analisisposiblecausa) {
        this.analisisposiblecausa = analisisposiblecausa;
    }

    public String getRecomendacion() {
        return recomendacion;
    }

    public void setRecomendacion(String recomendacion) {
        this.recomendacion = recomendacion;
    }

    public String getHoraantes() {
        return horaantes;
    }

    public void setHoraantes(String horaantes) {
        this.horaantes = horaantes;
    }

    public Personas getJefe() {
        return jefe;
    }

    public void setJefe(Personas jefe) {
        this.jefe = jefe;
    }

    public Personas getTestigo() {
        return testigo;
    }

    public void setTestigo(Personas testigo) {
        this.testigo = testigo;
    }

    public Personas getPersonareporta() {
        return personareporta;
    }

    public void setPersonareporta(Personas personareporta) {
        this.personareporta = personareporta;
    }

    public Personas getJefearea() {
        return jefearea;
    }

    public void setJefearea(Personas jefearea) {
        this.jefearea = jefearea;
    }

    public LugaresOcurrencias getSitioocurrencia() {
        return sitioocurrencia;
    }

    public void setSitioocurrencia(LugaresOcurrencias sitioocurrencia) {
        this.sitioocurrencia = sitioocurrencia;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public ElementosCausasAccidentes getCausa() {
        return causa;
    }

    public void setCausa(ElementosCausasAccidentes causa) {
        this.causa = causa;
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
        if (!(object instanceof Soaccidentes)) {
            return false;
        }
        Soaccidentes other = (Soaccidentes) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Soaccidentes[ secuencia=" + secuencia + " ]";
    }
    
}
