package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author PROYECTO01
 */
@Entity
@Table(name = "EXTRASRECARGOS")
public class ExtrasRecargos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "CODIGO")
    private Short codigo;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Size(max = 1)
    @Column(name = "TURNO")
    private String turno;
    @Size(max = 1)
    @Column(name = "APROBACIONAUTOMATICA")
    private String aprobacionautomatica;
    @JoinColumn(name = "TIPOJORNADA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private TiposJornadas tipojornada;
    @JoinColumn(name = "TIPODIA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private TiposDias tipodia;
    @JoinColumn(name = "TIPOLEGISLACION", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Contratos tipolegislacion;
    @Transient
    private boolean checkTurno;
    @Transient
    private boolean checkAprovacion;

    public ExtrasRecargos() {
    }

    public ExtrasRecargos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public ExtrasRecargos(BigInteger secuencia, String descripcion) {
        this.secuencia = secuencia;
        this.descripcion = descripcion;
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
        this.descripcion = descripcion;
    }

    public String getTurno() {
        if (turno == null) {
            turno = "N";
        }
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getAprobacionautomatica() {
        if (aprobacionautomatica == null) {
            aprobacionautomatica = "N";
        }
        return aprobacionautomatica;
    }

    public void setAprobacionautomatica(String aprobacionautomatica) {
        this.aprobacionautomatica = aprobacionautomatica;
    }

    public TiposJornadas getTipojornada() {
        return tipojornada;
    }

    public void setTipojornada(TiposJornadas tipojornada) {
        this.tipojornada = tipojornada;
    }

    public TiposDias getTipodia() {
        return tipodia;
    }

    public void setTipodia(TiposDias tipodia) {
        this.tipodia = tipodia;
    }

    public Contratos getTipolegislacion() {
        return tipolegislacion;
    }

    public void setTipolegislacion(Contratos tipolegislacion) {
        this.tipolegislacion = tipolegislacion;
    }

    public boolean isCheckTurno() {
        if (turno == null || turno.equalsIgnoreCase("N")) {
            checkTurno = false;
        } else {
            checkTurno = true;
        }
        return checkTurno;
    }

    public void setCheckTurno(boolean checkTurno) {
        if (checkTurno == false) {
            turno = "N";
        } else {
            turno = "S";
        }
        this.checkTurno = checkTurno;
    }

    public boolean isCheckAprovacion() {
        if (aprobacionautomatica == null || aprobacionautomatica.equalsIgnoreCase("N")) {
            checkAprovacion = false;
        } else {
            checkAprovacion = true;
        }
        return checkAprovacion;
    }

    public void setCheckAprovacion(boolean checkAprovacion) {
        if (checkTurno == false) {
            aprobacionautomatica = "N";
        } else {
            aprobacionautomatica = "S";
        }
        this.checkAprovacion = checkAprovacion;
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
        if (!(object instanceof ExtrasRecargos)) {
            return false;
        }
        ExtrasRecargos other = (ExtrasRecargos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.ExtrasRecargos[ secuencia=" + secuencia + " ]";
    }

}
