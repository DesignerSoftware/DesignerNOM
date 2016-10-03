package Entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author Administrador
 */
@Entity
@Cacheable(false)
public class VWActualesFechas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private BigInteger secuencia;
    @Column(name = "FECHADESDECAUSADO")
    @Temporal(TemporalType.DATE)
    private Date fechaDesdeCausado;
    @Column(name = "FECHAHASTACAUSADO")
    @Temporal(TemporalType.DATE)
    private Date fechaHastaCausado;

    public Date getFechaDesdeCausado() {
        return fechaDesdeCausado;
    }

    public void setFechaDesdeCausado(Date fechaDesdeCausado) {
        this.fechaDesdeCausado = fechaDesdeCausado;
    }

    public Date getFechaHastaCausado() {
        return fechaHastaCausado;
    }

    public void setFechaHastaCausado(Date fechaHastaCausado) {
        this.fechaHastaCausado = fechaHastaCausado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fechaDesdeCausado != null ? fechaDesdeCausado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VWActualesFechas)) {
            return false;
        }
        VWActualesFechas other = (VWActualesFechas) object;
        if ((this.fechaDesdeCausado == null && other.fechaDesdeCausado != null) || (this.fechaDesdeCausado != null && !this.fechaDesdeCausado.equals(other.fechaDesdeCausado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.VWActualesFechas[ fechaDesdeCausado = " + fechaDesdeCausado + " ]";
    }

}
