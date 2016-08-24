package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "PARAMETROSESTRUCTURAS")
public class ParametrosEstructuras implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "CODIGO")
    private Short codigo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHADESDECAUSADO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechadesdecausado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHAHASTACAUSADO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechahastacausado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHASISTEMA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechasistema;
    @JoinColumn(name = "USUARIO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Usuarios usuario;
    @JoinColumn(name = "TIPOTRABAJADOR", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private TiposTrabajadores tipotrabajador;
    @JoinColumn(name = "PROCESO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Procesos proceso;
    @JoinColumn(name = "ESTRUCTURA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Estructuras estructura;

    public ParametrosEstructuras() {
    }

    public ParametrosEstructuras(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public ParametrosEstructuras(BigInteger secuencia, Date fechadesdecausado, Date fechahastacausado, Date fechasistema, BigInteger usuario) {
        this.secuencia = secuencia;
        this.fechadesdecausado = fechadesdecausado;
        this.fechahastacausado = fechahastacausado;
        this.fechasistema = fechasistema;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Short getCodigo() {
        return codigo;
    }

    public void setCodigo(Short codigo) {
        this.codigo = codigo;
    }

    public Date getFechadesdecausado() {
        return fechadesdecausado;
    }

    public void setFechadesdecausado(Date fechadesdecausado) {
        this.fechadesdecausado = fechadesdecausado;
    }

    public Date getFechahastacausado() {
        return fechahastacausado;
    }

    public void setFechahastacausado(Date fechahastacausado) {
        this.fechahastacausado = fechahastacausado;
    }

    public Date getFechasistema() {
        return fechasistema;
    }

    public void setFechasistema(Date fechasistema) {
        this.fechasistema = fechasistema;
    }

    public TiposTrabajadores getTipotrabajador() {
        if(tipotrabajador == null){
            tipotrabajador = new TiposTrabajadores();
        }
        return tipotrabajador;
    }

    public void setTipotrabajador(TiposTrabajadores tipotrabajador) {
        this.tipotrabajador = tipotrabajador;
    }

    public Procesos getProceso() {
        return proceso;
    }

    public void setProceso(Procesos proceso) {
        this.proceso = proceso;
    }

    public Estructuras getEstructura() {
        if (estructura == null) {
            estructura = new Estructuras();
        }
        return estructura;
    }

    public void setEstructura(Estructuras estructura) {
        this.estructura = estructura;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
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
        if (!(object instanceof ParametrosEstructuras)) {
            return false;
        }
        ParametrosEstructuras other = (ParametrosEstructuras) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Parametrosestructuras[ secuencia=" + secuencia + " ]";
    }
}
