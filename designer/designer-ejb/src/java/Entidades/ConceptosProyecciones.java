package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user
 */
@Entity
@Table(name = "CONCEPTOSPROYECCIONES")
public class ConceptosProyecciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "PORCENTAJEPROYECCION")
    private Short porcentajeproyeccion;
    @JoinColumn(name = "CONCEPTO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Conceptos concepto;
    

    public ConceptosProyecciones() {
    }

    public ConceptosProyecciones(BigInteger secuencia, Conceptos concepto) {
        this.secuencia = secuencia;
        this.concepto = concepto;
    }

    public Conceptos getConcepto() {
        return concepto;
    }

    public void setConcepto(Conceptos concepto) {
        this.concepto = concepto;
    }

    public Short getPorcentajeproyeccion() {
        return porcentajeproyeccion;
    }

    public void setPorcentajeproyeccion(Short porcentajeproyeccion) {
        this.porcentajeproyeccion = porcentajeproyeccion;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (concepto != null ? concepto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConceptosProyecciones)) {
            return false;
        }
        ConceptosProyecciones other = (ConceptosProyecciones) object;
        if ((this.concepto == null && other.concepto != null) || (this.concepto != null && !this.concepto.equals(other.concepto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.ConceptosProyecciones[ concepto=" + concepto + " ]";
    }
}
