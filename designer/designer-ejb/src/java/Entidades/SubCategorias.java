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
@Table(name = "SUBCATEGORIAS")
public class SubCategorias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "CODIGO")
    private Integer codigo;

    public SubCategorias() {
    }

    public SubCategorias(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public SubCategorias(BigInteger secuencia, Integer codigo, String descripcion) {
        this.secuencia = secuencia;
        this.codigo = codigo;
        this.descripcion = descripcion;
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
        if (descripcion == null) {
            this.descripcion = descripcion.toUpperCase();
        } else {
            this.descripcion = descripcion;
        }
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
        if (!(object instanceof SubCategorias)) {
            return false;
        }
        SubCategorias other = (SubCategorias) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Subcategorias[ secuencia=" + secuencia + " ]";
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

}
