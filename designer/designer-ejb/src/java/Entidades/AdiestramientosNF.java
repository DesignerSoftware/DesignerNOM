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
@Table(name = "ADIESTRAMIENTOSNF")
public class AdiestramientosNF implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Size(max = 250)
    @Column(name = "DESCCRIPCION")
    private String desccripcion;
    @Size(max = 150)
    @Column(name = "OBJETIVO")
    private String objetivo;
    @Size(max = 250)
    @Column(name = "METAS")
    private String metas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COSTOTOTAL")
    private BigDecimal costototal;
    @Size(max = 80)
    @Column(name = "INSTRUCTOR")
    private String instructor;
    @Size(max = 50)
    @Column(name = "LUGAR")
    private String lugar;
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
    @Size(max = 50)
    @Column(name = "ESTADO")
    private String estado;
    @Column(name = "ESTRUCTURA")
    private Long estructura;
    @JoinColumn(name = "CURSO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Cursos curso;

    public AdiestramientosNF() {
    }

    public AdiestramientosNF(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public AdiestramientosNF(BigInteger secuencia, BigDecimal costototal, Date fechadesde, Date fechahasta) {
        this.secuencia = secuencia;
        this.costototal = costototal;
        this.fechadesde = fechadesde;
        this.fechahasta = fechahasta;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getDesccripcion() {
         if (desccripcion == null) {
            desccripcion = " ";
        } 
        return desccripcion;
    }

    public void setDesccripcion(String desccripcion) {
        this.desccripcion = desccripcion;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getMetas() {
        return metas;
    }

    public void setMetas(String metas) {
        this.metas = metas;
    }

    public BigDecimal getCostototal() {
        return costototal;
    }

    public void setCostototal(BigDecimal costototal) {
        this.costototal = costototal;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getEstructura() {
        return estructura;
    }

    public void setEstructura(Long estructura) {
        this.estructura = estructura;
    }

    public Cursos getCurso() {
        return curso;
    }

    public void setCurso(Cursos curso) {
        this.curso = curso;
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
        if (!(object instanceof AdiestramientosNF)) {
            return false;
        }
        AdiestramientosNF other = (AdiestramientosNF) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.AdiestramientosNF[ secuencia=" + secuencia + " ]";
    }
    
}
