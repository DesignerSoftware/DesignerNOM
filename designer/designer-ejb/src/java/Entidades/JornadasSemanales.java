package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "JORNADASSEMANALES")
public class JornadasSemanales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @Column(name = "HORAFINAL")
    private Short horafinal;
    @Basic(optional = false)
    @Column(name = "HORAFINALALIMENTACION")
    private Short horafinalalimentacion;
    @Basic(optional = false)
    @Column(name = "HORAINICIAL")
    private Short horainicial;
    @Basic(optional = false)
    @Column(name = "HORAINICIALALIMENTACION")
    private Short horainicialalimentacion;
    @Basic(optional = false)
    @Column(name = "MINUTOFINAL")
    private Short minutofinal;
    @Basic(optional = false)
    @Column(name = "MINUTOFINALALIMENTACION")
    private Short minutofinalalimentacion;
    @Basic(optional = false)
    @Column(name = "MINUTOINICIAL")
    private Short minutoinicial;
    @Basic(optional = false)
    @Column(name = "MINUTOINICIALALIMENTACION")
    private Short minutoinicialalimentacion;
    @Basic(optional = false)
    @Size(min = 1, max = 3)
    @Column(name = "DIA")
    private String dia;
    @JoinColumn(name = "JORNADALABORAL", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private JornadasLaborales jornadalaboral;
    @Transient
    private String estadoDia;

    public JornadasSemanales() {
    }

    public JornadasSemanales(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public JornadasSemanales(BigInteger secuencia, Short horafinal, Short horafinalalimentacion, Short horainicial, Short horainicialalimentacion, Short minutofinal, Short minutofinalalimentacion, Short minutoinicial, Short minutoinicialalimentacion, String dia) {
        this.secuencia = secuencia;
        this.horafinal = horafinal;
        this.horafinalalimentacion = horafinalalimentacion;
        this.horainicial = horainicial;
        this.horainicialalimentacion = horainicialalimentacion;
        this.minutofinal = minutofinal;
        this.minutofinalalimentacion = minutofinalalimentacion;
        this.minutoinicial = minutoinicial;
        this.minutoinicialalimentacion = minutoinicialalimentacion;
        this.dia = dia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Short getHorafinal() {
        return horafinal;
    }

    public void setHorafinal(Short horafinal) {
        this.horafinal = horafinal;
    }

    public Short getHorafinalalimentacion() {
        return horafinalalimentacion;
    }

    public void setHorafinalalimentacion(Short horafinalalimentacion) {
        this.horafinalalimentacion = horafinalalimentacion;
    }

    public Short getHorainicial() {
        return horainicial;
    }

    public void setHorainicial(Short horainicial) {
        this.horainicial = horainicial;
    }

    public Short getHorainicialalimentacion() {
        return horainicialalimentacion;
    }

    public void setHorainicialalimentacion(Short horainicialalimentacion) {
        this.horainicialalimentacion = horainicialalimentacion;
    }

    public Short getMinutofinal() {
        return minutofinal;
    }

    public void setMinutofinal(Short minutofinal) {
        this.minutofinal = minutofinal;
    }

    public Short getMinutofinalalimentacion() {
        return minutofinalalimentacion;
    }

    public void setMinutofinalalimentacion(Short minutofinalalimentacion) {
        this.minutofinalalimentacion = minutofinalalimentacion;
    }

    public Short getMinutoinicial() {
        return minutoinicial;
    }

    public void setMinutoinicial(Short minutoinicial) {
        this.minutoinicial = minutoinicial;
    }

    public Short getMinutoinicialalimentacion() {
        return minutoinicialalimentacion;
    }

    public void setMinutoinicialalimentacion(Short minutoinicialalimentacion) {
        this.minutoinicialalimentacion = minutoinicialalimentacion;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public JornadasLaborales getJornadalaboral() {
        return jornadalaboral;
    }

    public void setJornadalaboral(JornadasLaborales jornadalaboral) {
        this.jornadalaboral = jornadalaboral;
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
        if (!(object instanceof JornadasSemanales)) {
            return false;
        }
        JornadasSemanales other = (JornadasSemanales) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.JornadasSemanales[ secuencia=" + secuencia + " ]";
    }

    public String getEstadoDia() {
        if (estadoDia == null) {
            if (dia == null) {
                estadoDia = "LUNES";

            } else {
                if (dia.equalsIgnoreCase("LUN")) {
                    estadoDia = "LUNES";
                } else if (dia.equalsIgnoreCase("MAR")) {
                    estadoDia = "MARTES";
                } else if (dia.equalsIgnoreCase("MIE")) {
                    estadoDia = "MIERCOLES";
                } else if (dia.equalsIgnoreCase("JUE")) {
                    estadoDia = "JUEVES";
                } else if (dia.equalsIgnoreCase("VIE")) {
                    estadoDia = "VIERNES";
                } else if (dia.equalsIgnoreCase("SAB")) {
                    estadoDia = "SABADO";
                } else if (dia.equalsIgnoreCase("DOM")) {
                    estadoDia = "DOMINGO";                
                }
            }
        }
        return estadoDia;
    }

    public void setEstadoDia(String estadoDia) {
        this.estadoDia = estadoDia;
    }
    

}
