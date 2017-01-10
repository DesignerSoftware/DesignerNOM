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
@Table(name = "PERIODICIDADES")
public class Periodicidades implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "INDEPENDIENTEADELANTO")
    private String independienteadelanto;
    @NotNull
    @JoinColumn(name = "UNIDAD", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Unidades unidad;
    @JoinColumn(name = "UNIDADBASE", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Unidades unidadbase;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CODIGO")
    private Integer codigo;
    @Transient
    private String codigoStr;

    public Periodicidades() {
    }

    public Periodicidades(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Periodicidades(BigInteger secuencia, Integer codigo) {
        this.secuencia = secuencia;
        this.codigo = codigo;
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

    public String getIndependienteadelanto() {
        return independienteadelanto;
    }

    public void setIndependienteadelanto(String independienteadelanto) {
        this.independienteadelanto = independienteadelanto;
    }

    public Unidades getUnidad() {
        return unidad;
    }

    public void setUnidad(Unidades unidad) {
        this.unidad = unidad;
    }

    public Unidades getUnidadbase() {
        return unidadbase;
    }

    public void setUnidadbase(Unidades unidadbase) {
        this.unidadbase = unidadbase;
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
        if (!(object instanceof Periodicidades)) {
            return false;
        }
        Periodicidades other = (Periodicidades) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Periodicidades[ secuencia=" + secuencia + " ]";
    }

    public String getCodigoStr() {
        if (codigo != null) {
            codigoStr = String.valueOf(codigo);
        } else {
            codigoStr = " ";
            codigo = 0;
        }
        return codigoStr;
    }

    public void setCodigoStr(String codigoStr) {
        if (!codigoStr.equals(" ") && !codigoStr.equals("")) {
            codigo = Integer.parseInt(codigoStr);
            this.codigoStr = codigoStr;
        } else {
            codigo = 0;
            this.codigoStr = codigoStr;
        }
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
