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
@Table(name = "CLASESAUSENTISMOS")
public class Clasesausentismos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @Column(name = "CODIGO")
    private Integer codigo;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @JoinColumn(name = "TIPO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Tiposausentismos tipo;

    public Clasesausentismos() {
    }

    public Clasesausentismos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Clasesausentismos(BigInteger secuencia, Integer codigo) {
        this.secuencia = secuencia;
        this.codigo = codigo;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        if(descripcion == null){
            descripcion = "";
        }
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        if (descripcion == null) {
            this.descripcion = descripcion;
        } else {
            this.descripcion = descripcion.toUpperCase();
        }
    }

    public Tiposausentismos getTipo() {
        return tipo;
    }

    public void setTipo(Tiposausentismos tipo) {
        this.tipo = tipo;
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
        if (!(object instanceof Clasesausentismos)) {
            return false;
        }
        Clasesausentismos other = (Clasesausentismos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Clasesausentismos[ secuencia=" + secuencia + " ]";
    }

}
