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
@Table(name = "TIPOSEDUCACIONES")
public class TiposEducaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CODIGO")
    private short codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "NIVELEDUCATIVO")
    private BigInteger niveleducativo;

    public TiposEducaciones() {
    }

    public TiposEducaciones(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public TiposEducaciones(BigInteger secuencia, short codigo, String nombre) {
        this.secuencia = secuencia;
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public short getCodigo() {
        return codigo;
    }

    public void setCodigo(short codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        if (nombre == null) {
            nombre = "";
            return nombre;
        } else {
            return nombre.toUpperCase();
        }
     }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigInteger getNiveleducativo() {
        return niveleducativo;
    }

    public void setNiveleducativo(BigInteger niveleducativo) {
        this.niveleducativo = niveleducativo;
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
        if (!(object instanceof TiposEducaciones)) {
            return false;
        }
        TiposEducaciones other = (TiposEducaciones) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.TiposEducaciones[ secuencia=" + secuencia + " ]";
    }    
}
