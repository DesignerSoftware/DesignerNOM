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
@Table(name = "TIPOSENTIDADES")
public class TiposEntidades implements Serializable {
    
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
    private Short codigo;
    @JoinColumn(name = "RUBROPRESUPUESTAL", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Rubrospresupuestales rubropresupuestal;
    @JoinColumn(name = "GRUPO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Grupostiposentidades grupo;

    public TiposEntidades() {
    }

    public TiposEntidades(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public TiposEntidades(BigInteger secuencia, Short codigo, String nombre) {
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
        this.nombre = nombre;
    }

    public Rubrospresupuestales getRubropresupuestal() {
        return rubropresupuestal;
    }

    public void setRubropresupuestal(Rubrospresupuestales rubropresupuestal) {
        this.rubropresupuestal = rubropresupuestal;
    }

    public Grupostiposentidades getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupostiposentidades grupo) {
        this.grupo = grupo;
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
        if (!(object instanceof TiposEntidades)) {
            return false;
        }
        TiposEntidades other = (TiposEntidades) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Tiposentidades[ secuencia=" + secuencia + " ]";
    }

    public Short getCodigo() {
        return codigo;
    }

    public void setCodigo(Short codigo) {
        this.codigo = codigo;
    }
}
