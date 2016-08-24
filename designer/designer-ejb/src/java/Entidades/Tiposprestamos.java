package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "TIPOSPRESTAMOS")
public class Tiposprestamos implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigDecimal secuencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CODIGO")
    private short codigo;
    @JoinColumn(name = "CONCEPTOABONO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Conceptos conceptoabono;
    @JoinColumn(name = "CONCEPTOTERCERO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Conceptos conceptotercero;
    @JoinColumn(name = "CONCEPTODESEMBOLSO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Conceptos conceptodesembolso;
    

    public Tiposprestamos() {
    }

    public Tiposprestamos(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public Tiposprestamos(BigDecimal secuencia, String descripcion, short codigo) {
        this.secuencia = secuencia;
        this.descripcion = descripcion;
        this.codigo = codigo;
    }

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public short getCodigo() {
        return codigo;
    }

    public void setCodigo(short codigo) {
        this.codigo = codigo;
    }

    public Conceptos getConceptoabono() {
        return conceptoabono;
    }

    public void setConceptoabono(Conceptos conceptoabono) {
        this.conceptoabono = conceptoabono;
    }

    public Conceptos getConceptotercero() {
        return conceptotercero;
    }

    public void setConceptotercero(Conceptos conceptotercero) {
        this.conceptotercero = conceptotercero;
    }

    public Conceptos getConceptodesembolso() {
        return conceptodesembolso;
    }

    public void setConceptodesembolso(Conceptos conceptodesembolso) {
        this.conceptodesembolso = conceptodesembolso;
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
        if (!(object instanceof Tiposprestamos)) {
            return false;
        }
        Tiposprestamos other = (Tiposprestamos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Tiposprestamos[ secuencia=" + secuencia + " ]";
    } 
}
