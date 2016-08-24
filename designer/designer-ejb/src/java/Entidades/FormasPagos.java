package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "FORMASPAGOS")
public class FormasPagos implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "IC")
    private String ic;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "TIPO")
    private String tipo;
    @Column(name = "CODIGO")
    private Short codigo;
    @JoinColumn(name = "FORMULACAPITAL", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Formulas formulacapital;
    @JoinColumn(name = "FORMULAINTERES", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Formulas formulainteres;

    public FormasPagos() {
    }

    public FormasPagos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public FormasPagos(BigInteger secuencia, String descripcion, String ic, String tipo) {
        this.secuencia = secuencia;
        this.descripcion = descripcion;
        this.ic = ic;
        this.tipo = tipo;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Short getCodigo() {
        return codigo;
    }

    public void setCodigo(Short codigo) {
        this.codigo = codigo;
    }

    public Formulas getFormulacapital() {
        return formulacapital;
    }

    public void setFormulacapital(Formulas formulacapital) {
        this.formulacapital = formulacapital;
    }

    public Formulas getFormulainteres() {
        return formulainteres;
    }

    public void setFormulainteres(Formulas formulainteres) {
        this.formulainteres = formulainteres;
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
        if (!(object instanceof FormasPagos)) {
            return false;
        }
        FormasPagos other = (FormasPagos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Formaspagos[ secuencia=" + secuencia + " ]";
    }
    
}
