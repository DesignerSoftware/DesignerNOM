package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "VIGENCIASINDICADORES")
public class VigenciasIndicadores implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "FECHAFINAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechafinal;
    @Column(name = "FECHAINICIAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechainicial;
    @JoinColumn(name = "TIPOINDICADOR", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private TiposIndicadores tipoindicador;
    @JoinColumn(name = "INDICADOR", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Indicadores indicador;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empleados empleado;
    @Transient
    private String strFechaIni;
    @Transient
    private String strFechaFin;

    public VigenciasIndicadores() {
    }

    public VigenciasIndicadores(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public VigenciasIndicadores(BigInteger secuencia, Date fechainicial) {
        this.secuencia = secuencia;
        this.fechainicial = fechainicial;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Date getFechafinal() {
        return fechafinal;
    }

    public void setFechafinal(Date fechafinal) {
        this.fechafinal = fechafinal;
    }

    public Date getFechainicial() {
        return fechainicial;
    }

    public void setFechainicial(Date fechainicial) {
        this.fechainicial = fechainicial;
    }

    public String getStrFechaIni() {
         if (fechafinal != null) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            strFechaIni = formatoFecha.format(fechafinal);
        } else {
            strFechaIni = " ";
        }
        return strFechaIni;
    }

    public void setStrFechaIni(String strFechaIni) throws ParseException {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        fechafinal = formatoFecha.parse(strFechaIni);
        this.strFechaIni = strFechaIni;
    }

    public String getStrFechaFin() {
         if (fechainicial != null) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            strFechaFin = formatoFecha.format(fechainicial);
        } else {
            strFechaFin = " ";
        }
        return strFechaFin;
    }

    public void setStrFechaFin(String strFechaFin) throws ParseException {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        fechainicial = formatoFecha.parse(strFechaFin);
        this.strFechaFin = strFechaFin;
    }
    
    

    public TiposIndicadores getTipoindicador() {
        return tipoindicador;
    }

    public void setTipoindicador(TiposIndicadores tipoindicador) {
        this.tipoindicador = tipoindicador;
    }

    public Indicadores getIndicador() {
        return indicador;
    }

    public void setIndicador(Indicadores indicador) {
        this.indicador = indicador;
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
        if (!(object instanceof VigenciasIndicadores)) {
            return false;
        }
        VigenciasIndicadores other = (VigenciasIndicadores) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.VigenciasIndicadores[ secuencia=" + secuencia + " ]";
    }
    
}
