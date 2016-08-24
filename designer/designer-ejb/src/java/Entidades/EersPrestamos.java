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
@Table(name = "EERSPRESTAMOS")
public class EersPrestamos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "TIPOEER")
    private String tipoeer;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "DOCUMENTO")
    private String documento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHADOCUMENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechadocumento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHAINICIODESCUENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechainiciodescuento;
    @Column(name = "VALORTOTAL")
    private BigInteger valortotal;
    @Size(max = 1)
    @Column(name = "DESEMBOLSO")
    private String desembolso;
    @Column(name = "NITPAGAR_A")
    private BigInteger nitpagarA;
    @Size(max = 40)
    @Column(name = "NOMBREPAGAR_A")
    private String nombrepagarA;
    @Column(name = "CUOTAS")
    private BigInteger cuotas;
    @Size(max = 30)
    @Column(name = "AUTORIZADOPOR")
    private String autorizadopor;
    @Size(max = 30)
    @Column(name = "BENEFICIARIO")
    private String beneficiario;
    @Size(max = 200)
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @Size(max = 20)
    @Column(name = "CANCELACIONDOCUMENTO")
    private String cancelaciondocumento;
    @Column(name = "CANCELACIONFECHAHASTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cancelacionfechahasta;
    @Column(name = "PAGARECUOTACAPITALMENSUAL")
    private BigInteger pagarecuotacapitalmensual;
    @Column(name = "PAGARECUOTAINTERESMENSUAL")
    private BigInteger pagarecuotainteresmensual;
    @Column(name = "PAGAREPORCENTAJEINTERES")
    private BigDecimal pagareporcentajeinteres;
    @Column(name = "PAGAREPORCENTAJEINTERESMORA")
    private BigDecimal pagareporcentajeinteresmora;
    @Column(name = "PAGAREFECHAVENCIMIENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pagarefechavencimiento;
    @Size(max = 50)
    @Column(name = "NUMEROPROCESO")
    private String numeroproceso;
    @Size(max = 50)
    @Column(name = "RADICACION")
    private String radicacion;
    @Column(name = "FECHASISTEMA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechasistema;
    @JoinColumn(name = "TIPOPRESTAMO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Tiposprestamos tipoprestamo;
    @JoinColumn(name = "TIPOEMBARGO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private TiposEmbargos tipoembargo;
    @JoinColumn(name = "DEMANDANTE", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Terceros demandante;
    @JoinColumn(name = "TERCERO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Terceros tercero;
    @JoinColumn(name = "MOTIVOPRESTAMO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private MotivosPrestamos motivoprestamo;
    @JoinColumn(name = "MOTIVOEMBARGO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private MotivosEmbargos motivoembargo;
    @JoinColumn(name = "JUZGADO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Juzgados juzgado;
    @JoinColumn(name = "FORMADTO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private FormasDtos formadto;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empleados empleado;

    public EersPrestamos() {
    }

    public EersPrestamos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public EersPrestamos(BigInteger secuencia, String tipoeer, String documento, Date fechadocumento, Date fechainiciodescuento) {
        this.secuencia = secuencia;
        this.tipoeer = tipoeer;
        this.documento = documento;
        this.fechadocumento = fechadocumento;
        this.fechainiciodescuento = fechainiciodescuento;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getTipoeer() {
        if (tipoeer == null) {
            tipoeer = " ";
        }
        return tipoeer;
    }

    public void setTipoeer(String tipoeer) {
        this.tipoeer = tipoeer;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Date getFechadocumento() {
        return fechadocumento;
    }

    public void setFechadocumento(Date fechadocumento) {
        this.fechadocumento = fechadocumento;
    }

    public Date getFechainiciodescuento() {
        return fechainiciodescuento;
    }

    public void setFechainiciodescuento(Date fechainiciodescuento) {
        this.fechainiciodescuento = fechainiciodescuento;
    }

    public BigInteger getValortotal() {
        return valortotal;
    }

    public void setValortotal(BigInteger valortotal) {
        this.valortotal = valortotal;
    }

    public String getDesembolso() {
        return desembolso;
    }

    public void setDesembolso(String desembolso) {
        this.desembolso = desembolso;
    }

    public BigInteger getNitpagarA() {
        return nitpagarA;
    }

    public void setNitpagarA(BigInteger nitpagarA) {
        this.nitpagarA = nitpagarA;
    }

    public String getNombrepagarA() {
        return nombrepagarA;
    }

    public void setNombrepagarA(String nombrepagarA) {
        this.nombrepagarA = nombrepagarA;
    }

    public BigInteger getCuotas() {
        return cuotas;
    }

    public void setCuotas(BigInteger cuotas) {
        this.cuotas = cuotas;
    }

    public String getAutorizadopor() {
        return autorizadopor;
    }

    public void setAutorizadopor(String autorizadopor) {
        this.autorizadopor = autorizadopor;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getCancelaciondocumento() {
        return cancelaciondocumento;
    }

    public void setCancelaciondocumento(String cancelaciondocumento) {
        this.cancelaciondocumento = cancelaciondocumento;
    }

    public Date getCancelacionfechahasta() {
        return cancelacionfechahasta;
    }

    public void setCancelacionfechahasta(Date cancelacionfechahasta) {
        this.cancelacionfechahasta = cancelacionfechahasta;
    }

    public BigInteger getPagarecuotacapitalmensual() {
        return pagarecuotacapitalmensual;
    }

    public void setPagarecuotacapitalmensual(BigInteger pagarecuotacapitalmensual) {
        this.pagarecuotacapitalmensual = pagarecuotacapitalmensual;
    }

    public BigInteger getPagarecuotainteresmensual() {
        return pagarecuotainteresmensual;
    }

    public void setPagarecuotainteresmensual(BigInteger pagarecuotainteresmensual) {
        this.pagarecuotainteresmensual = pagarecuotainteresmensual;
    }

    public BigDecimal getPagareporcentajeinteres() {
        return pagareporcentajeinteres;
    }

    public void setPagareporcentajeinteres(BigDecimal pagareporcentajeinteres) {
        this.pagareporcentajeinteres = pagareporcentajeinteres;
    }

    public BigDecimal getPagareporcentajeinteresmora() {
        return pagareporcentajeinteresmora;
    }

    public void setPagareporcentajeinteresmora(BigDecimal pagareporcentajeinteresmora) {
        this.pagareporcentajeinteresmora = pagareporcentajeinteresmora;
    }

    public Date getPagarefechavencimiento() {
        return pagarefechavencimiento;
    }

    public void setPagarefechavencimiento(Date pagarefechavencimiento) {
        this.pagarefechavencimiento = pagarefechavencimiento;
    }

    public String getNumeroproceso() {
        return numeroproceso;
    }

    public void setNumeroproceso(String numeroproceso) {
        this.numeroproceso = numeroproceso;
    }

    public String getRadicacion() {
        return radicacion;
    }

    public void setRadicacion(String radicacion) {
        this.radicacion = radicacion;
    }

    public Date getFechasistema() {
        return fechasistema;
    }

    public void setFechasistema(Date fechasistema) {
        this.fechasistema = fechasistema;
    }

    public Tiposprestamos getTipoprestamo() {
        return tipoprestamo;
    }

    public void setTipoprestamo(Tiposprestamos tipoprestamo) {
        this.tipoprestamo = tipoprestamo;
    }

    public TiposEmbargos getTipoembargo() {
        if (tipoembargo == null) {
            tipoembargo = new TiposEmbargos();
        }
        return tipoembargo;
    }

    public void setTipoembargo(TiposEmbargos tipoembargo) {
        this.tipoembargo = tipoembargo;
    }

    public Terceros getDemandante() {
        return demandante;
    }

    public void setDemandante(Terceros demandante) {
        this.demandante = demandante;
    }

    public Terceros getTercero() {
        return tercero;
    }

    public void setTercero(Terceros tercero) {
        this.tercero = tercero;
    }

    public MotivosPrestamos getMotivoprestamo() {
        return motivoprestamo;
    }

    public void setMotivoprestamo(MotivosPrestamos motivoprestamo) {
        this.motivoprestamo = motivoprestamo;
    }

    public MotivosEmbargos getMotivoembargo() {
        return motivoembargo;
    }

    public void setMotivoembargo(MotivosEmbargos motivoembargo) {
        this.motivoembargo = motivoembargo;
    }

    public Juzgados getJuzgado() {
        return juzgado;
    }

    public void setJuzgado(Juzgados juzgado) {
        this.juzgado = juzgado;
    }

    public FormasDtos getFormadto() {
        if (formadto == null) {
            formadto = new FormasDtos();

        }
        return formadto;
    }

    public void setFormadto(FormasDtos formadto) {
        this.formadto = formadto;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
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
        if (!(object instanceof EersPrestamos)) {
            return false;
        }
        EersPrestamos other = (EersPrestamos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.EersPrestamos[ secuencia=" + secuencia + " ]";
    }

}
