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
@Table(name = "UBICACIONESGEOGRAFICAS")
public class UbicacionesGeograficas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "CODIGO")
    private Integer codigo;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Size(max = 60)
    @Column(name = "DIRECCION")
    private String direccion;
    @Column(name = "TELEFONO")
    private Long telefono;
    @Column(name = "FAX")
    private Long fax;
    @Size(max = 12)
    @Column(name = "ZONA")
    private String zona;
    @Size(max = 2000)
    @Column(name = "ACTIVIDADECONOMICA")
    private String actividadeconomica;
    @Size(max = 50)
    @Column(name = "OBSERVACION")
    private String observacion;
    @Size(max = 4)
    @Column(name = "CODIGOALTERNATIVO")
    private String codigoalternativo;
    @Column(name = "SITIOACTIVIDAD")
    private BigInteger sitioactividad;
    @Column(name = "TIPOACTIVIDAD")
    private BigInteger tipoactividad;
    @JoinColumn(name = "SUCURSAL_PILA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private SucursalesPila sucursalPila;
    @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empresas empresa;
    @JoinColumn(name = "CIUDAD", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Ciudades ciudad;

    public UbicacionesGeograficas() {
    }

    public UbicacionesGeograficas(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public UbicacionesGeograficas(BigInteger secuencia, Integer codigo, String descripcion) {
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

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        if (descripcion != null) {
            this.descripcion = descripcion.toUpperCase();
        } else {
            this.descripcion = descripcion;
        }
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        if (direccion != null) {
            this.direccion = direccion.toUpperCase();
        } else {
            this.direccion = direccion;
        }
    }

    public Long getTelefono() {
        return telefono;
    }

    public void setTelefono(Long telefono) {
        this.telefono = telefono;
    }

    public Long getFax() {
        return fax;
    }

    public void setFax(Long fax) {
        this.fax = fax;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getActividadeconomica() {
        return actividadeconomica;
    }

    public void setActividadeconomica(String actividadeconomica) {
        this.actividadeconomica = actividadeconomica;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        if (observacion != null) {
            this.observacion = observacion.toUpperCase();
        } else {
            this.observacion = observacion;
        }
    }

    public String getCodigoalternativo() {
        return codigoalternativo;
    }

    public void setCodigoalternativo(String codigoalternativo) {
        if (codigoalternativo != null) {
            this.codigoalternativo = codigoalternativo.toUpperCase();
        } else {
            this.codigoalternativo = codigoalternativo;
        }
    }

    public BigInteger getSitioactividad() {
        return sitioactividad;
    }

    public void setSitioactividad(BigInteger sitioactividad) {
        this.sitioactividad = sitioactividad;
    }

    public BigInteger getTipoactividad() {
        return tipoactividad;
    }

    public void setTipoactividad(BigInteger tipoactividad) {
        this.tipoactividad = tipoactividad;
    }

    public SucursalesPila getSucursalPila() {
        if (sucursalPila == null) {
            sucursalPila = new SucursalesPila();
        }
        return sucursalPila;
    }

    public void setSucursalPila(SucursalesPila sucursalPila) {
        this.sucursalPila = sucursalPila;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public Ciudades getCiudad() {
        if (ciudad == null) {
            ciudad = new Ciudades();
        }
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
        if (!(object instanceof UbicacionesGeograficas)) {
            return false;
        }
        UbicacionesGeograficas other = (UbicacionesGeograficas) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Ubicacionesgeograficas[ secuencia=" + secuencia + " ]";
    }
}
