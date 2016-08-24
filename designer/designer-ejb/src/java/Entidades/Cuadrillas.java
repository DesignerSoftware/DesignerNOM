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
@Table(name = "CUADRILLAS")
public class Cuadrillas implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "CODIGO")
    private short codigo;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "MODULO")
    private Short modulo;
    @Size(max = 10)
    @Column(name = "METODOROTACION")
    private String metodorotacion;
    @Column(name = "DIASCICLO")
    private Short diasciclo;
    @Size(max = 10)
    @Column(name = "ESTADO")
    private String estado;

    public Cuadrillas() {
    }

    public Cuadrillas(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Cuadrillas(BigInteger secuencia, short codigo, String descripcion) {
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

    public short getCodigo() {
        return codigo;
    }

    public void setCodigo(short codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Short getModulo() {
        return modulo;
    }

    public void setModulo(Short modulo) {
        this.modulo = modulo;
    }

    public String getMetodorotacion() {
        return metodorotacion;
    }

    public void setMetodorotacion(String metodorotacion) {
        this.metodorotacion = metodorotacion;
    }

    public Short getDiasciclo() {
        return diasciclo;
    }

    public void setDiasciclo(Short diasciclo) {
        this.diasciclo = diasciclo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
        if (!(object instanceof Cuadrillas)) {
            return false;
        }
        Cuadrillas other = (Cuadrillas) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Cuadrillas[ secuencia=" + secuencia + " ]";
    }
}
