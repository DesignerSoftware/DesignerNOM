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
@Table(name = "VIGENCIASNOFORMALES")
public class VigenciasNoFormales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHAVIGENCIA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechavigencia;
    @Size(max = 50)
    @Column(name = "TITULO")
    private String titulo;
    @Size(max = 1)
    @Column(name = "ACARGO")
    private String acargo;
    @Size(max = 150)
    @Column(name = "OBSERVACION")
    private String observacion;
    @Size(max = 20)
    @Column(name = "CALIFICACIONOBTENIDA")
    private String calificacionobtenida;
    @JoinColumn(name = "PERSONA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Personas persona;
    @JoinColumn(name = "INSTITUCION", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Instituciones institucion;
    @JoinColumn(name = "CURSO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Cursos curso;
    @JoinColumn(name = "ADIESTRAMIENTONF", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private AdiestramientosNF adiestramientonf;

    public VigenciasNoFormales() {
    }

    public VigenciasNoFormales(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public VigenciasNoFormales(BigInteger secuencia, Date fechavigencia) {
        this.secuencia = secuencia;
        this.fechavigencia = fechavigencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Date getFechavigencia() {
        return fechavigencia;
    }

    public void setFechavigencia(Date fechavigencia) {
        this.fechavigencia = fechavigencia;
    }

    public String getTitulo() {
        if (titulo == null) {
            titulo = " ";
            return titulo;
        } else {
            return titulo.toUpperCase();

        }
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAcargo() {
        return acargo;
    }

    public void setAcargo(String acargo) {
        this.acargo = acargo;
    }

    public String getObservacion() {
        if (observacion == null) {
            observacion = " ";
            return observacion;
        } else {
            return observacion.toUpperCase();

        }
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getCalificacionobtenida() {
        return calificacionobtenida;
    }

    public void setCalificacionobtenida(String calificacionobtenida) {
        this.calificacionobtenida = calificacionobtenida;
    }

    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
    }

    public Instituciones getInstitucion() {
        return institucion;
    }

    public void setInstitucion(Instituciones institucion) {
        this.institucion = institucion;
    }

    public Cursos getCurso() {
        return curso;
    }

    public void setCurso(Cursos curso) {
        this.curso = curso;
    }

    public AdiestramientosNF getAdiestramientonf() {
        return adiestramientonf;
    }

    public void setAdiestramientonf(AdiestramientosNF adiestramientonf) {
        this.adiestramientonf = adiestramientonf;
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
        if (!(object instanceof VigenciasNoFormales)) {
            return false;
        }
        VigenciasNoFormales other = (VigenciasNoFormales) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.VigenciasNoFormales[ secuencia=" + secuencia + " ]";
    }
}
