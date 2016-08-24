package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "UNIDADES")
public class Unidades implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 4)
    @Column(name = "CODIGO")
    private String codigo;
    @JoinColumn(name = "TIPOUNIDAD", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private TiposUnidades tipounidad;

    public Unidades() {
    }

    public Unidades(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Unidades(BigInteger secuencia, String nombre) {
        this.secuencia = secuencia;
        this.nombre = nombre;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre != null) {
            this.nombre = nombre.toUpperCase();
        } else {
            this.nombre = nombre;
        }
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        if (codigo != null) {
            this.codigo = codigo.toUpperCase();
        } else {
            this.codigo = codigo;
        }
    }

    public TiposUnidades getTipounidad() {
        return tipounidad;
    }

    public void setTipounidad(TiposUnidades tipounidad) {
        this.tipounidad = tipounidad;
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
        if (!(object instanceof Unidades)) {
            return false;
        }
        Unidades other = (Unidades) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Unidades[ secuencia=" + secuencia + " ]";
    }
}
