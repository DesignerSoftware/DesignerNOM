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
 * @author Administrador
 */
@Entity
@Table(name = "PARAMETROSTIEMPOS")
public class ParametrosTiempos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHADESDE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechadesde;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHAHASTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechahasta;
    @Column(name = "CODIGOEMPLEADODESDE")
    private BigDecimal codigoempleadodesde;
    @Column(name = "CODIGOEMPLEADOHASTA")
    private BigDecimal codigoempleadohasta;
    @Column(name = "FECHACONTROLNOVEDAD")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechacontrolnovedad;
    @Size(max = 2)
    @Column(name = "FORMALIQUIDACION")
    private String formaliquidacion;
    @Size(max = 30)
    @Column(name = "USUARIOBD")
    private String usuariobd;
    @JoinColumn(name = "ESTRUCTURA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Estructuras estructura;
    @JoinColumn(name = "CUADRILLA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Cuadrillas cuadrilla;
    @Transient
    private String horaFechaDesde;
    @Transient
    private String horaFechaHasta;

    public ParametrosTiempos() {
    }

    public ParametrosTiempos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public ParametrosTiempos(BigInteger secuencia, Date fechadesde, Date fechahasta) {
        this.secuencia = secuencia;
        this.fechadesde = fechadesde;
        this.fechahasta = fechahasta;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
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

    public Date getFechacontrolnovedad() {
        return fechacontrolnovedad;
    }

    public void setFechacontrolnovedad(Date fechacontrolnovedad) {
        this.fechacontrolnovedad = fechacontrolnovedad;
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

    public Estructuras getEstructura() {
        return estructura;
    }

    public void setEstructura(Estructuras estructura) {
        this.estructura = estructura;
    }

    public Cuadrillas getCuadrilla() {
        return cuadrilla;
    }

    public void setCuadrilla(Cuadrillas cuadrilla) {
        this.cuadrilla = cuadrilla;
    }

    public String getHoraFechaDesde() {
        return horaFechaDesde;
    }

    public void setHoraFechaDesde(String horaFechaDesde) {
        this.horaFechaDesde = horaFechaDesde;
    }

    public String getHoraFechaHasta() {
        return horaFechaHasta;
    }

    public void setHoraFechaHasta(String horaFechaHasta) {
        this.horaFechaHasta = horaFechaHasta;
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
        if (!(object instanceof ParametrosTiempos)) {
            return false;
        }
        ParametrosTiempos other = (ParametrosTiempos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.ParametrosTiempos[ secuencia=" + secuencia + " ]";
    }

}
