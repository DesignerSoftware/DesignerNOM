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
@Table(name = "TERCEROSSUCURSALES")
public class TercerosSucursales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CODIGOSUCURSAL")
    private BigInteger codigosucursal;
    @Column(name = "CODIGOPATRONAL")
    private Long codigopatronal;
    @Size(max = 200)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @JoinColumn(name = "TERCERO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Terceros tercero;
    @JoinColumn(name = "CIUDAD", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Ciudades ciudad;

    public TercerosSucursales() {
    }

    public TercerosSucursales(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public TercerosSucursales(BigInteger secuencia, BigInteger codigosucursal) {
        this.secuencia = secuencia;
        this.codigosucursal = codigosucursal;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getCodigosucursal() {
        return codigosucursal;
    }

    public void setCodigosucursal(BigInteger codigosucursal) {
        this.codigosucursal = codigosucursal;
    }

    public Long getCodigopatronal() {
        return codigopatronal;
    }

    public void setCodigopatronal(Long codigopatronal) {
        this.codigopatronal = codigopatronal;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Terceros getTercero() {
         return tercero;
    }

    public void setTercero(Terceros tercero) {
        this.tercero = tercero;
    }

    public Ciudades getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudades ciudad) {
        this.ciudad = ciudad;
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
        if (!(object instanceof TercerosSucursales)) {
            return false;
        }
        TercerosSucursales other = (TercerosSucursales) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Tercerossucursales[ secuencia=" + secuencia + " ]";
    }
}
