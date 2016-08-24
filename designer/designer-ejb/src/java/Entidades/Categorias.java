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
@Table(name = "CATEGORIAS")
public class Categorias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "CODIGO")
    private BigInteger codigo;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @JoinColumn(name = "TIPOSUELDO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private TiposSueldos tiposueldo;
    @JoinColumn(name = "CONCEPTO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Conceptos concepto;
    @JoinColumn(name = "CLASECATEGORIA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private ClasesCategorias clasecategoria;

    public Categorias() {
    }

    public Categorias(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Categorias(BigInteger secuencia, BigInteger codigo, String descripcion) {
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

    public BigInteger getCodigo() {
        return codigo;
    }

    public void setCodigo(BigInteger codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion.toUpperCase();
    }

    public TiposSueldos getTiposueldo() {
        return tiposueldo;
    }

    public void setTiposueldo(TiposSueldos tiposueldo) {
        this.tiposueldo = tiposueldo;
    }

    public Conceptos getConcepto() {
        return concepto;
    }

    public void setConcepto(Conceptos concepto) {
        this.concepto = concepto;
    }

    public ClasesCategorias getClasecategoria() {
        return clasecategoria;
    }

    public void setClasecategoria(ClasesCategorias clasecategoria) {
        this.clasecategoria = clasecategoria;
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
        if (!(object instanceof Categorias)) {
            return false;
        }
        Categorias other = (Categorias) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Categorias[ secuencia=" + secuencia + " ]";
    }
    
}
