package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "INDICES")
public class Indices implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "CODIGO")
    private Short codigo;
    @Size(max = 50)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "PORCENTAJEESTANDAR")
    private BigDecimal porcentajeestandar;
    @Size(max = 60)
    @Column(name = "OBJETIVO")
    private String objetivo;
    @Size(max = 2000)
    @Column(name = "DIVIDENDO")
    private String dividendo;
    @Size(max = 2000)
    @Column(name = "DIVISOR")
    private String divisor;
    @JoinColumn(name = "TIPOINDICE", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private TiposIndices tipoindice;

    public Indices() {
    }

    public Indices(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Short getCodigo() {
        return codigo;
    }

    public void setCodigo(Short codigo) {
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

    public BigDecimal getPorcentajeestandar() {
        return porcentajeestandar;
    }

    public void setPorcentajeestandar(BigDecimal porcentajeestandar) {
        this.porcentajeestandar = porcentajeestandar;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getDividendo() {
        return dividendo;
    }

    public void setDividendo(String dividendo) {
        this.dividendo = dividendo;
    }

    public String getDivisor() {
        return divisor;
    }

    public void setDivisor(String divisor) {
        this.divisor = divisor;
    }

    public TiposIndices getTipoindice() {
        return tipoindice;
    }

    public void setTipoindice(TiposIndices tipoindice) {
        this.tipoindice = tipoindice;
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
        if (!(object instanceof Indices)) {
            return false;
        }
        Indices other = (Indices) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Indices[ secuencia=" + secuencia + " ]";
    }

}
