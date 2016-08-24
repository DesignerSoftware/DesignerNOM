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
@Table(name = "NOVEDADESSISTEMA")
public class NovedadesSistema implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHAINICIALDISFRUTE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechainicialdisfrute;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DIAS")
    private BigInteger dias;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "TIPO")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "SUBTIPO")
    private String subtipo;
    @Column(name = "VALORCESANTIA")
    private BigInteger valorcesantia;
    @Size(max = 100)
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @Column(name = "FECHASISTEMA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechasistema;
    @Size(max = 30)
    @Column(name = "USUARIO")
    private String usuario;
    @Column(name = "FECHACORTECESANTIA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechacortecesantia;
    @Column(name = "VALORINTERESCESANTIA")
    private BigInteger valorinterescesantia;
    @Size(max = 100)
    @Column(name = "BENEFICIARIO")
    private String beneficiario;
    @Column(name = "FECHASIGUIENTEFINVACA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechasiguientefinvaca;
    @Size(max = 10)
    @Column(name = "ESTADO")
    private String estado;
    @Size(max = 1)
    @Column(name = "ADELANTAPAGO")
    private String adelantapago;
    @Column(name = "PROPORCIONADELANTAPAGO")
    private BigInteger proporcionadelantapago;
    @Column(name = "ADELANTAPAGOHASTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date adelantapagohasta;
    @Size(max = 1)
    @Column(name = "PAGARPORFUERA")
    private String pagarporfuera;
    @Column(name = "FECHAINICIONOMINA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechainicionomina;
    @Column(name = "FECHAPAGO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechapago;
    @Column(name = "HORAINICIO")
    private Short horainicio;
    @Column(name = "MINUTOINICIO")
    private Short minutoinicio;
    @Column(name = "HORAFIN")
    private Short horafin;
    @Column(name = "MINUTOFIN")
    private Short minutofin;
    @Column(name = "MINUTOS")
    private Short minutos;
    @Column(name = "HORAS")
    private Short horas;
    @Size(max = 1)
    @Column(name = "PAGADO")
    private String pagado;
    @Column(name = "VACADIASAPLAZADOS")
    private Short vacadiasaplazados;
    @Size(max = 1)
    @Column(name = "CONTABILIZARALCIERRE")
    private String contabilizaralcierre;
    @Column(name = "VALORSOLICITADO")
    private BigInteger valorsolicitado;
    @Size(max = 1)
    @Column(name = "INDEMNIZA")
    private String indemniza;
    @JoinColumn(name = "VACACION", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Vacaciones vacacion;
    @JoinColumn(name = "TERCERO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Terceros tercero;
    @JoinColumn(name = "SOAUSENTISMO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Soausentismos soausentismo;
    @JoinColumn(name = "NORMALABORAL", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private NormasLaborales normalaboral;
    @JoinColumn(name = "MOTIVORETIRO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private MotivosRetiros motivoretiros;
    @JoinColumn(name = "MOTIVODEFINITIVA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private MotivosDefinitivas motivodefinitiva;
    @JoinColumn(name = "MOTIVOCESANTIA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private MotivosCesantias motivocesantia;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empleados empleado;
    @JoinColumn(name = "CAUSAAUSENTISMO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Causasausentismos causaausentismo;
    @Transient
    private Boolean indemnizaBool;

    public NovedadesSistema() {
    }

    public NovedadesSistema(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public NovedadesSistema(BigInteger secuencia, Date fechainicialdisfrute, BigInteger dias, String tipo, String subtipo) {
        this.secuencia = secuencia;
        this.fechainicialdisfrute = fechainicialdisfrute;
        this.dias = dias;
        this.tipo = tipo;
        this.subtipo = subtipo;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Date getFechainicialdisfrute() {
        return fechainicialdisfrute;
    }

    public void setFechainicialdisfrute(Date fechainicialdisfrute) {
        this.fechainicialdisfrute = fechainicialdisfrute;
    }

    public BigInteger getDias() {
        return dias;
    }

    public void setDias(BigInteger dias) {
        this.dias = dias;
    }

    public String getTipo() {
        if (tipo == null) {
            tipo = (" ");
        }
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSubtipo() {
        if (subtipo == null) {
            subtipo = ("TIEMPO");
        }
        return subtipo;
    }

    public void setSubtipo(String subtipo) {
        this.subtipo = subtipo;
    }

    public BigInteger getValorcesantia() {
        return valorcesantia;
    }

    public void setValorcesantia(BigInteger valorcesantia) {
        this.valorcesantia = valorcesantia;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Date getFechasistema() {
        return fechasistema;
    }

    public void setFechasistema(Date fechasistema) {
        this.fechasistema = fechasistema;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getFechacortecesantia() {
        return fechacortecesantia;
    }

    public void setFechacortecesantia(Date fechacortecesantia) {
        this.fechacortecesantia = fechacortecesantia;
    }

    public BigInteger getValorinterescesantia() {
        return valorinterescesantia;
    }

    public void setValorinterescesantia(BigInteger valorinterescesantia) {
        this.valorinterescesantia = valorinterescesantia;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }

    public Date getFechasiguientefinvaca() {
        return fechasiguientefinvaca;
    }

    public void setFechasiguientefinvaca(Date fechasiguientefinvaca) {
        this.fechasiguientefinvaca = fechasiguientefinvaca;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getAdelantapago() {
        return adelantapago;
    }

    public void setAdelantapago(String adelantapago) {
        this.adelantapago = adelantapago;
    }

    public BigInteger getProporcionadelantapago() {
        return proporcionadelantapago;
    }

    public void setProporcionadelantapago(BigInteger proporcionadelantapago) {
        this.proporcionadelantapago = proporcionadelantapago;
    }

    public Date getAdelantapagohasta() {
        return adelantapagohasta;
    }

    public void setAdelantapagohasta(Date adelantapagohasta) {
        this.adelantapagohasta = adelantapagohasta;
    }

    public String getPagarporfuera() {
        return pagarporfuera;
    }

    public void setPagarporfuera(String pagarporfuera) {
        this.pagarporfuera = pagarporfuera;
    }

    public Date getFechainicionomina() {
        return fechainicionomina;
    }

    public void setFechainicionomina(Date fechainicionomina) {
        this.fechainicionomina = fechainicionomina;
    }

    public Date getFechapago() {
        return fechapago;
    }

    public void setFechapago(Date fechapago) {
        this.fechapago = fechapago;
    }

    public Short getHorainicio() {
        return horainicio;
    }

    public void setHorainicio(Short horainicio) {
        this.horainicio = horainicio;
    }

    public Short getMinutoinicio() {
        return minutoinicio;
    }

    public void setMinutoinicio(Short minutoinicio) {
        this.minutoinicio = minutoinicio;
    }

    public Short getHorafin() {
        return horafin;
    }

    public void setHorafin(Short horafin) {
        this.horafin = horafin;
    }

    public Short getMinutofin() {
        return minutofin;
    }

    public void setMinutofin(Short minutofin) {
        this.minutofin = minutofin;
    }

    public Short getMinutos() {
        return minutos;
    }

    public void setMinutos(Short minutos) {
        this.minutos = minutos;
    }

    public Short getHoras() {
        return horas;
    }

    public void setHoras(Short horas) {
        this.horas = horas;
    }

    public String getPagado() {
        return pagado;
    }

    public void setPagado(String pagado) {
        this.pagado = pagado;
    }

    public Short getVacadiasaplazados() {
        if(vacadiasaplazados == null){
            vacadiasaplazados = 0;
        }
        return vacadiasaplazados;
    }

    public void setVacadiasaplazados(Short vacadiasaplazados) {
        this.vacadiasaplazados = vacadiasaplazados;
    }

    public String getContabilizaralcierre() {
        return contabilizaralcierre;
    }

    public void setContabilizaralcierre(String contabilizaralcierre) {
        this.contabilizaralcierre = contabilizaralcierre;
    }

    public BigInteger getValorsolicitado() {
        return valorsolicitado;
    }

    public void setValorsolicitado(BigInteger valorsolicitado) {
        this.valorsolicitado = valorsolicitado;
    }

    public String getIndemniza() {
        if (indemniza == null) {
            indemniza = "N";
        }
        return indemniza;
    }

    public void setIndemniza(String indemniza) {
        this.indemniza = indemniza;
    }

    public Boolean getIndemnizaBool() {
        getIndemniza();
        if (indemniza != null) {
            if (indemniza.equals("S")) {
                indemnizaBool = true;
            } else {
                indemnizaBool = false;
            }
        } else {
            indemnizaBool = false;
        }

        return indemnizaBool;
    }

    public void setIndemnizaBool(Boolean indemnizaBool) {
        if (indemnizaBool == true) {
            indemniza = "S";
        } else {
            indemniza = "N";
        }
        this.indemnizaBool = indemnizaBool;
    }

    public Vacaciones getVacacion() {
        if (vacacion == null) {
            vacacion = new Vacaciones();
        }
        return vacacion;
    }

    public void setVacacion(Vacaciones vacacion) {
        this.vacacion = vacacion;
    }

    public Terceros getTercero() {
        return tercero;
    }

    public void setTercero(Terceros tercero) {
        this.tercero = tercero;
    }

    public Soausentismos getSoausentismo() {
        return soausentismo;
    }

    public void setSoausentismo(Soausentismos soausentismo) {
        this.soausentismo = soausentismo;
    }

    public NormasLaborales getNormalaboral() {
        return normalaboral;
    }

    public void setNormalaboral(NormasLaborales normalaboral) {
        this.normalaboral = normalaboral;
    }

    public MotivosRetiros getMotivoretiro() {
        if (motivoretiros == null) {
            motivoretiros = new MotivosRetiros();
        }
        return motivoretiros;
    }

    public void setMotivoretiro(MotivosRetiros motivoretiro) {
        this.motivoretiros = motivoretiro;
    }

    public MotivosDefinitivas getMotivodefinitiva() {
        if (motivodefinitiva == null) {
            motivodefinitiva = new MotivosDefinitivas();
        }
        return motivodefinitiva;
    }

    public void setMotivodefinitiva(MotivosDefinitivas motivodefinitiva) {
        this.motivodefinitiva = motivodefinitiva;
    }

    public MotivosCesantias getMotivocesantia() {
        return motivocesantia;
    }

    public void setMotivocesantia(MotivosCesantias motivocesantia) {
        this.motivocesantia = motivocesantia;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public Causasausentismos getCausaausentismo() {
        return causaausentismo;
    }

    public void setCausaausentismo(Causasausentismos causaausentismo) {
        this.causaausentismo = causaausentismo;
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
        if (!(object instanceof NovedadesSistema)) {
            return false;
        }
        NovedadesSistema other = (NovedadesSistema) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Novedadessistema[ secuencia=" + secuencia + " ]";
    }
}
