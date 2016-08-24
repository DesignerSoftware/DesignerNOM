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
@Table(name = "TIPOSCENTROSCOSTOS")
public class TiposCentrosCostos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CODIGO")
    private Integer codigo;
    @JoinColumn(name = "GRUPOTIPOCC", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private GruposTiposCC grupotipocc;

    public TiposCentrosCostos() {
    }

    public TiposCentrosCostos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public TiposCentrosCostos(BigInteger secuencia, Integer codigo, String nombre) {
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null) {
            this.nombre = nombre;
        } else {
            this.nombre = nombre.toUpperCase();
        }

    }

    public GruposTiposCC getGrupotipocc() {
        return grupotipocc;
    }

    public void setGrupotipocc(GruposTiposCC grupotipocc) {
        this.grupotipocc = grupotipocc;
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
        if (!(object instanceof TiposCentrosCostos)) {
            return false;
        }
        TiposCentrosCostos other = (TiposCentrosCostos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Tiposcentroscostos[ secuencia=" + secuencia + " ]";
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

}
