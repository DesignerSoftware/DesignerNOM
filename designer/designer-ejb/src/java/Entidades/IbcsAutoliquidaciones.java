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
@Table(name = "IBCSAUTOLIQUIDACIONES")
public class IbcsAutoliquidaciones implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "FECHAINICIAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechainicial;
    @Column(name = "FECHAFINAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechafinal;
    @Column(name = "VALOR")
    private BigDecimal valor;
    @Column(name = "UNIDADES")
    private Short unidades;
    @Size(max = 20)
    @Column(name = "ESTADO")
    private String estado;
    @Column(name = "FECHAPAGO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechapago;
    @Column(name = "FECHASISTEMA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechasistema;
    @Column(name = "VALORLIQUIDADO")
    private BigInteger valorliquidado;
    @Column(name = "VALORANTERIOR")
    private BigInteger valoranterior;
    @JoinColumn(name = "TIPOENTIDAD", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private TiposEntidades tipoentidad;
    @JoinColumn(name = "PROCESO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Procesos proceso;
    @JoinColumn(name = "PROCESOLIQUIDADO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Procesos procesoliquidado;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empleados empleado;

    public IbcsAutoliquidaciones() {
    }

    public IbcsAutoliquidaciones(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public IbcsAutoliquidaciones(BigInteger secuencia, Date fechainicial, BigDecimal valor) {
        this.secuencia = secuencia;
        this.fechainicial = fechainicial;
        this.valor = valor;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Date getFechainicial() {
        return fechainicial;
    }

    public void setFechainicial(Date fechainicial) {
        this.fechainicial = fechainicial;
    }

    public Date getFechafinal() {
        return fechafinal;
    }

    public void setFechafinal(Date fechafinal) {
        this.fechafinal = fechafinal;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Short getUnidades() {
        return unidades;
    }

    public void setUnidades(Short unidades) {
        this.unidades = unidades;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechapago() {
        return fechapago;
    }

    public void setFechapago(Date fechapago) {
        this.fechapago = fechapago;
    }

    public Date getFechasistema() {
        return fechasistema;
    }

    public void setFechasistema(Date fechasistema) {
        this.fechasistema = fechasistema;
    }

    public BigInteger getValorliquidado() {
        return valorliquidado;
    }

    public void setValorliquidado(BigInteger valorliquidado) {
        this.valorliquidado = valorliquidado;
    }

    public BigInteger getValoranterior() {
        return valoranterior;
    }

    public void setValoranterior(BigInteger valoranterior) {
        this.valoranterior = valoranterior;
    }

    public TiposEntidades getTipoentidad() {
        return tipoentidad;
    }

    public void setTipoentidad(TiposEntidades tipoentidad) {
        this.tipoentidad = tipoentidad;
    }

    public Procesos getProceso() {
        return proceso;
    }

    public void setProceso(Procesos proceso) {
        this.proceso = proceso;
    }

    public Procesos getProcesoliquidado() {
        return procesoliquidado;
    }

    public void setProcesoliquidado(Procesos procesoliquidado) {
        this.procesoliquidado = procesoliquidado;
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
        if (!(object instanceof IbcsAutoliquidaciones)) {
            return false;
        }
        IbcsAutoliquidaciones other = (IbcsAutoliquidaciones) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.IbcsAutoliquidaciones[ secuencia=" + secuencia + " ]";
    }
    
}
