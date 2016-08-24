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
 * @author Administrator
 */
@Entity
@Table(name = "TEMPNOVEDADES")
public class TempNovedades implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "CONCEPTO")
    private BigInteger concepto;
    @Column(name = "EMPLEADO")
    private BigInteger empleado;
    @Column(name = "FECHAFINAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechafinal;
    @Column(name = "FECHAINICIAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechainicial;
    @Column(name = "FECHAREPORTE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechareporte;
    @Size(max = 20)
    @Column(name = "TERMINAL")
    private String terminal;
    @Size(max = 20)
    @Column(name = "DOCUMENTOSOPORTE")
    private String documentosoporte;
    @Column(name = "USUARIOREPORTA")
    private BigInteger usuarioreporta;
    @Column(name = "VALORTOTAL")
    private BigDecimal valortotal;
    @Column(name = "PERIODICIDAD")
    private BigInteger periodicidad;
    @Size(max = 100)
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @Column(name = "TERCERO")
    private BigInteger tercero;
    @Column(name = "SALDO")
    private BigDecimal saldo;
    @Size(max = 1)
    @Column(name = "ESTADO")
    private String estado;
    @Size(max = 30)
    @Column(name = "ARCHIVO")
    private String archivo;
    @Column(name = "UNIDADESPARTEENTERA")
    private Integer unidadesparteentera;
    @Column(name = "UNIDADESPARTEFRACCION")
    private Integer unidadespartefraccion;
    @Size(max = 30)
    @Column(name = "USUARIOBD")
    private String usuariobd;
    @Size(max = 20)
    @Column(name = "TIPO")
    private String tipo;
    @Column(name = "MOTIVONOVEDAD")
    private Integer motivonovedad;
    @Size(max = 1)
    @Column(name = "ESTADOVALIDACION")
    private String estadovalidacion;

    public TempNovedades() {
    }

    public TempNovedades(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getConcepto() {
        return concepto;
    }

    public void setConcepto(BigInteger concepto) {
        this.concepto = concepto;
    }

    public BigInteger getEmpleado() {
        return empleado;
    }

    public void setEmpleado(BigInteger empleado) {
        this.empleado = empleado;
    }

    public Date getFechafinal() {
        return fechafinal;
    }

    public void setFechafinal(Date fechafinal) {
        this.fechafinal = fechafinal;
    }

    public Date getFechainicial() {
        return fechainicial;
    }

    public void setFechainicial(Date fechainicial) {
        this.fechainicial = fechainicial;
    }

    public Date getFechareporte() {
        return fechareporte;
    }

    public void setFechareporte(Date fechareporte) {
        this.fechareporte = fechareporte;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getDocumentosoporte() {
        return documentosoporte;
    }

    public void setDocumentosoporte(String documentosoporte) {
        this.documentosoporte = documentosoporte;
    }

    public BigInteger getUsuarioreporta() {
        return usuarioreporta;
    }

    public void setUsuarioreporta(BigInteger usuarioreporta) {
        this.usuarioreporta = usuarioreporta;
    }

    public BigDecimal getValortotal() {
        return valortotal;
    }

    public void setValortotal(BigDecimal valortotal) {
        this.valortotal = valortotal;
    }

    public BigInteger getPeriodicidad() {
        return periodicidad;
    }

    public void setPeriodicidad(BigInteger periodicidad) {
        this.periodicidad = periodicidad;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public BigInteger getTercero() {
        return tercero;
    }

    public void setTercero(BigInteger tercero) {
        this.tercero = tercero;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public Integer getUnidadesparteentera() {
        return unidadesparteentera;
    }

    public void setUnidadesparteentera(Integer unidadesparteentera) {
        this.unidadesparteentera = unidadesparteentera;
    }

    public Integer getUnidadespartefraccion() {
        return unidadespartefraccion;
    }

    public void setUnidadespartefraccion(Integer unidadespartefraccion) {
        this.unidadespartefraccion = unidadespartefraccion;
    }

    public String getUsuariobd() {
        return usuariobd;
    }

    public void setUsuariobd(String usuariobd) {
        this.usuariobd = usuariobd;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getMotivonovedad() {
        return motivonovedad;
    }

    public void setMotivonovedad(Integer motivonovedad) {
        this.motivonovedad = motivonovedad;
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
        if (!(object instanceof TempNovedades)) {
            return false;
        }
        TempNovedades other = (TempNovedades) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Tempnovedades[ secuencia=" + secuencia + " ]";
    }

    public String getEstadovalidacion() {
        return estadovalidacion;
    }

    public void setEstadovalidacion(String estadovalidacion) {
        this.estadovalidacion = estadovalidacion;
    }
}
