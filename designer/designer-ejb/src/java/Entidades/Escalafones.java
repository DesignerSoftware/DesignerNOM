package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "ESCALAFONES")
public class Escalafones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "CODIGO")
    private String codigo;
    @JoinColumn(name = "SUBCATEGORIA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private SubCategorias subcategoria;
    @JoinColumn(name = "CATEGORIA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Categorias categoria;

    public Escalafones() {
    }

    public Escalafones(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Escalafones(BigInteger secuencia, String codigo) {
        this.secuencia = secuencia;
        this.codigo = codigo;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public SubCategorias getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(SubCategorias subcategoria) {
        this.subcategoria = subcategoria;
    }

    public Categorias getCategoria() {
        return categoria;
    }

    public void setCategoria(Categorias categoria) {
        this.categoria = categoria;
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
        if (!(object instanceof Escalafones)) {
            return false;
        }
        Escalafones other = (Escalafones) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Escalafones[ secuencia=" + secuencia + " ]";
    }
    
}
