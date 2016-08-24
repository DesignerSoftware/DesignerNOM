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
@Table(name = "PDGMETAS")
public class Pdgmetas implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigDecimal secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CODIGO")
    private BigInteger codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "PORCENTAJECUMPLIDO")
    private BigInteger porcentajecumplido;
    @Column(name = "PESO")
    private BigInteger peso;
    @Column(name = "SECUENCIAACTIVIDAD")
    private BigInteger secuenciaactividad;
    @Size(max = 1)
    @Column(name = "ACTIVIDAD")
    private String actividad;
    @Size(max = 20)
    @Column(name = "CODIGOALTERNATIVO")
    private String codigoalternativo;
    @JoinColumn(name = "PDGESTRATEGIA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Pdgestrategias pdgestrategia;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empleados empleado;

    public Pdgmetas() {
    }

    public Pdgmetas(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public Pdgmetas(BigDecimal secuencia, BigInteger codigo, String descripcion) {
        this.secuencia = secuencia;
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
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
        this.descripcion = descripcion;
    }

    public BigInteger getPorcentajecumplido() {
        return porcentajecumplido;
    }

    public void setPorcentajecumplido(BigInteger porcentajecumplido) {
        this.porcentajecumplido = porcentajecumplido;
    }

    public BigInteger getPeso() {
        return peso;
    }

    public void setPeso(BigInteger peso) {
        this.peso = peso;
    }

    public BigInteger getSecuenciaactividad() {
        return secuenciaactividad;
    }

    public void setSecuenciaactividad(BigInteger secuenciaactividad) {
        this.secuenciaactividad = secuenciaactividad;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getCodigoalternativo() {
        return codigoalternativo;
    }

    public void setCodigoalternativo(String codigoalternativo) {
        this.codigoalternativo = codigoalternativo;
    }

    public Pdgestrategias getPdgestrategia() {
        return pdgestrategia;
    }

    public void setPdgestrategia(Pdgestrategias pdgestrategia) {
        this.pdgestrategia = pdgestrategia;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
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
        if (!(object instanceof Pdgmetas)) {
            return false;
        }
        Pdgmetas other = (Pdgmetas) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Pdgmetas[ secuencia=" + secuencia + " ]";
    }
    
}
