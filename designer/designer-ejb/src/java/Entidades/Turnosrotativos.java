package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user
 */
@Entity
@Table(name = "TURNOSROTATIVOS")
public class Turnosrotativos implements Serializable {

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
    @Column(name = "FECHASEMILLA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechasemilla;
    @Column(name = "HORAINICIAL")
    private short horainicial;
    @Column(name = "HORAFINAL")
    private short horafinal;
    @Column(name = "MINUTOINICIAL")
    private short minutoinicial;
    @Column(name = "MINUTOFINAL")
    private short minutofinal;
    @Column(name = "FINSEMILLA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date finsemilla;
    @JoinColumn(name = "CUADRILLA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Cuadrillas cuadrilla;

    public Turnosrotativos() {
    }

    public Turnosrotativos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Turnosrotativos(BigInteger secuencia, short codigo, String descripcion, Date fechasemilla, short horainicial, short horafinal, short minutoinicial, short minutofinal) {
        this.secuencia = secuencia;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.fechasemilla = fechasemilla;
        this.horainicial = horainicial;
        this.horafinal = horafinal;
        this.minutoinicial = minutoinicial;
        this.minutofinal = minutofinal;
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

    public Date getFechasemilla() {
        return fechasemilla;
    }

    public void setFechasemilla(Date fechasemilla) {
        this.fechasemilla = fechasemilla;
    }

    public short getHorainicial() {
        return horainicial;
    }

    public void setHorainicial(short horainicial) {
        this.horainicial = horainicial;
    }

    public short getHorafinal() {
        return horafinal;
    }

    public void setHorafinal(short horafinal) {
        this.horafinal = horafinal;
    }

    public short getMinutoinicial() {
        return minutoinicial;
    }

    public void setMinutoinicial(short minutoinicial) {
        this.minutoinicial = minutoinicial;
    }

    public short getMinutofinal() {
        return minutofinal;
    }

    public void setMinutofinal(short minutofinal) {
        this.minutofinal = minutofinal;
    }

    public Date getFinsemilla() {
        return finsemilla;
    }

    public void setFinsemilla(Date finsemilla) {
        this.finsemilla = finsemilla;
    }

    public Cuadrillas getCuadrilla() {
        return cuadrilla;
    }

    public void setCuadrilla(Cuadrillas cuadrilla) {
        this.cuadrilla = cuadrilla;
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
        if (!(object instanceof Turnosrotativos)) {
            return false;
        }
        Turnosrotativos other = (Turnosrotativos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Turnosrotativos[ secuencia=" + secuencia + " ]";
    }
}
