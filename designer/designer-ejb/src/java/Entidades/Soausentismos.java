package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "SOAUSENTISMOS")
public class Soausentismos implements Serializable {

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
    @Column(name = "DIAS")
    private BigInteger dias;
    @Column(name = "HORAS")
    private BigInteger horas;
    @Size(max = 15)
    @Column(name = "CLASEAUSENTISMO")
    private String claseausentismo;
    @Size(max = 1)
    @Column(name = "RELACIONADA")
    private String relacionada;
    @Column(name = "FECHAINIPAGO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechainipago;
    @Column(name = "FECHAFINPAGO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechafinpago;
    @Column(name = "FECHAFINAUS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechafinaus;
    @Column(name = "FECHAEXPEDICION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaexpedicion;
    @Size(max = 50)
    @Column(name = "NUMEROCERTIFICADO")
    private String numerocertificado;
    @Column(name = "FECHASISTEMA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechasistema;
    @Size(max = 60)
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @Size(max = 50)
    @Column(name = "FORMALIQUIDACION")
    private String formaliquidacion;
    @Size(max = 30)
    @Column(name = "USUARIOBD")
    private String usuariobd;
    @Column(name = "BASELIQUIDACION")
    private BigInteger baseliquidacion;
    @Column(name = "PORCENTAJEINDIVIDUAL")
    private BigDecimal porcentajeindividual;
    @JoinColumn(name = "TIPO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Tiposausentismos tipo;
    @JoinColumn(name = "TERCERO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Terceros tercero;
    @JoinColumn(name = "PRORROGA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Soausentismos prorroga;
    @JoinColumn(name = "ACCIDENTE", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Soaccidentes accidente;
    @JoinColumn(name = "ENFERMEDAD", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private EnfermeadadesProfesionales enfermedad;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empleados empleado;
    @JoinColumn(name = "DIAGNOSTICOCATEGORIA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Diagnosticoscategorias diagnosticocategoria;
    @JoinColumn(name = "CLASE", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Clasesausentismos clase;
    @JoinColumn(name = "CAUSA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Causasausentismos causa;
    @Transient
    private String relacion;
    @Transient
    private String prorrogaAusentismo;
    @Transient
    private boolean relacionadaBool;

    public Soausentismos() {
    }

    public Soausentismos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Soausentismos(BigInteger secuencia, Date fecha) {
        this.secuencia = secuencia;
        this.fecha = fecha;
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

    public BigInteger getDias() {
        if(dias == null){
            dias = BigInteger.valueOf(0);
        }
        return dias;
    }

    public void setDias(BigInteger dias) {
        this.dias = dias;
    }

    public BigInteger getHoras() {
        if(horas == null){
            horas = BigInteger.valueOf(0);
        }
        return horas;
    }

    public void setHoras(BigInteger horas) {
        this.horas = horas;
    }

    public String getClaseausentismo() {
        return claseausentismo;
    }

    public void setClaseausentismo(String claseausentismo) {
        this.claseausentismo = claseausentismo;
    }

    public String getRelacionada() {
        if(relacionada == null){
            relacionada = "N";
        }
        return relacionada;
    }

    public void setRelacionada(String relacionada) {
        this.relacionada = relacionada;
    }

    public Date getFechainipago() {
        return fechainipago;
    }

    public void setFechainipago(Date fechainipago) {
        this.fechainipago = fechainipago;
    }

    public Date getFechafinpago() {
        return fechafinpago;
    }

    public void setFechafinpago(Date fechafinpago) {
        this.fechafinpago = fechafinpago;
    }

    public Date getFechafinaus() {
        return fechafinaus;
    }

    public void setFechafinaus(Date fechafinaus) {
        this.fechafinaus = fechafinaus;
    }

    public Date getFechaexpedicion() {
        return fechaexpedicion;
    }

    public void setFechaexpedicion(Date fechaexpedicion) {
        this.fechaexpedicion = fechaexpedicion;
    }

    public String getNumerocertificado() {
        return numerocertificado;
    }

    public void setNumerocertificado(String numerocertificado) {
        this.numerocertificado = numerocertificado;
    }

    public Date getFechasistema() {
        return fechasistema;
    }

    public void setFechasistema(Date fechasistema) {
        this.fechasistema = fechasistema;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getFormaliquidacion() {
        return formaliquidacion;
    }

    public void setFormaliquidacion(String formaliquidacion) {
        this.formaliquidacion = formaliquidacion;
    }

    public String getUsuariobd() {
        return usuariobd;
    }

    public void setUsuariobd(String usuariobd) {
        this.usuariobd = usuariobd;
    }

    public BigInteger getBaseliquidacion() {
//        if (baseliquidacion == null) {
//            baseliquidacion = BigInteger.valueOf(0);
//        }
        return baseliquidacion;
    }

    public void setBaseliquidacion(BigInteger baseliquidacion) {
        this.baseliquidacion = baseliquidacion;
    }

    public Tiposausentismos getTipo() {
        if (tipo == null) {
            tipo = new Tiposausentismos();
        }
        return tipo;
    }

    public void setTipo(Tiposausentismos tipo) {
        this.tipo = tipo;
    }

    public Terceros getTercero() {
        if (tercero == null) {
            tercero = new Terceros();
        }
        return tercero;
    }

    public void setTercero(Terceros tercero) {
        this.tercero = tercero;
    }

    public String getRelacion() {
        return relacion;
    }

    public void setRelacion(String relacion) {
        this.relacion = relacion;
    }

    public String getProrrogaAusentismo() {
        SimpleDateFormat dt = new SimpleDateFormat("dd-mm-yyyy");
        if (fecha != null) {
            if (numerocertificado == null) {
                prorrogaAusentismo = ("Falta # Certificado" + ": " + dt.format(fecha) + "->" + dt.format(fechafinaus)).toUpperCase();
            } else {
                prorrogaAusentismo = (numerocertificado + ": " + dt.format(fecha) + "->" + dt.format(fechafinaus)).toUpperCase();
            }
        } else {
            prorrogaAusentismo = null;
        }
        return prorrogaAusentismo;
    }

    public void setProrrogaAusentismo(String prorrogaAusentismo) {
        this.prorrogaAusentismo = prorrogaAusentismo;
    }

    public Soausentismos getProrroga() {
        if (prorroga == null) {
            prorroga = new Soausentismos();
        }
        return prorroga;
    }

    public void setProrroga(Soausentismos prorroga) {
        this.prorroga = prorroga;
    }

    public Soaccidentes getAccidente() {
        if (accidente == null) {
            accidente = new Soaccidentes();
        }
        return accidente;
    }

    public void setAccidente(Soaccidentes accidente) {
        this.accidente = accidente;
    }

    public EnfermeadadesProfesionales getEnfermedad() {
        if (enfermedad == null) {
            enfermedad = new EnfermeadadesProfesionales();
        }
        return enfermedad;
    }

    public void setEnfermedad(EnfermeadadesProfesionales enfermedad) {
        this.enfermedad = enfermedad;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public Diagnosticoscategorias getDiagnosticocategoria() {
        if (diagnosticocategoria == null) {
            diagnosticocategoria = new Diagnosticoscategorias();
        }
        return diagnosticocategoria;
    }

    public void setDiagnosticocategoria(Diagnosticoscategorias diagnosticocategoria) {
        this.diagnosticocategoria = diagnosticocategoria;
    }

    public Clasesausentismos getClase() {
        if (clase == null) {
            clase = new Clasesausentismos();
        }
        return clase;
    }

    public void setClase(Clasesausentismos clase) {
        this.clase = clase;
    }

    public Causasausentismos getCausa() {
        if (causa == null) {
            causa = new Causasausentismos();
        }
        return causa;
    }

    public void setCausa(Causasausentismos causa) {
        this.causa = causa;
    }

    public boolean getRelacionadaBool() {
        getRelacionada();
        if (relacionada != null) {
            if (relacionada.equals("S")) {
                relacionadaBool = true;
            } else {
                relacionadaBool = false;
            }
        } else {
            relacionadaBool = false;
        }
        return relacionadaBool;
    }

    public void setRelacionadaBool(boolean relacionadaBool) {
        if (relacionadaBool == true) {
            relacionada = "S";
        } else {
            relacionada = "N";
        }
        this.relacionadaBool = relacionadaBool;
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
        if (!(object instanceof Soausentismos)) {
            return false;
        }
        Soausentismos other = (Soausentismos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Soausentismos[ secuencia=" + secuencia + " ]";
    }

    public BigDecimal getPorcentajeindividual() {
//        if (porcentajeindividual == null) {
//            porcentajeindividual = BigDecimal.valueOf(0);
//        }
        return porcentajeindividual;
    }

    public void setPorcentajeindividual(BigDecimal porcentajeindividual) {
        this.porcentajeindividual = porcentajeindividual;
    }
}