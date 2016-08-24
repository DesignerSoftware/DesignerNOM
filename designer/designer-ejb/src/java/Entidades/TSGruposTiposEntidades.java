package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author PROYECTO01
 */
@Entity
@Table(name = "TSGRUPOSTIPOSENTIDADES")
public class TSGruposTiposEntidades implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @JoinColumn(name = "TIPOSUELDO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private TiposSueldos tiposueldo;
    @JoinColumn(name = "GRUPOTIPOENTIDAD", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Grupostiposentidades grupotipoentidad;

    public TSGruposTiposEntidades() {
    }

    public TSGruposTiposEntidades(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public TiposSueldos getTiposueldo() {
        return tiposueldo;
    }

    public void setTiposueldo(TiposSueldos tiposueldo) {
        this.tiposueldo = tiposueldo;
    }

    public Grupostiposentidades getGrupotipoentidad() {
        return grupotipoentidad;
    }

    public void setGrupotipoentidad(Grupostiposentidades grupotipoentidad) {
        this.grupotipoentidad = grupotipoentidad;
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
        if (!(object instanceof TSGruposTiposEntidades)) {
            return false;
        }
        TSGruposTiposEntidades other = (TSGruposTiposEntidades) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.TSGruposTiposEntidades[ secuencia=" + secuencia + " ]";
    }
    
}
