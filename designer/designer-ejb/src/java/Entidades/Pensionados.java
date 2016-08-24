package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "PENSIONADOS")
public class Pensionados implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "FECHAFINALPENSION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechafinalpension;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHAINICIOPENSION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechainiciopension;
    @Column(name = "PORCENTAJE")
    private BigDecimal porcentaje;
    @Size(max = 50)
    @Column(name = "RESOLUCIONPENSION")
    private String resolucionpension;
    @JoinColumn(name = "VIGENCIATIPOTRABAJADOR", referencedColumnName = "SECUENCIA")
    @OneToOne
    private VigenciasTiposTrabajadores vigenciatipotrabajador;
    @JoinColumn(name = "TIPOPENSIONADO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private TiposPensionados tipopensionado;
    @JoinColumn(name = "TUTOR", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Personas tutor;
    @JoinColumn(name = "CAUSABIENTE", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empleados causabiente;
    @JoinColumn(name = "CLASE", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private ClasesPensiones clase;

    public Pensionados() {
    }

    public Pensionados(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Pensionados(BigInteger secuencia, Date fechainiciopension) {
        this.secuencia = secuencia;
        this.fechainiciopension = fechainiciopension;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Date getFechafinalpension() {
        return fechafinalpension;
    }

    public void setFechafinalpension(Date fechafinalpension) {
        this.fechafinalpension = fechafinalpension;
    }

    public Date getFechainiciopension() {
        return fechainiciopension;
    }

    public void setFechainiciopension(Date fechainiciopension) {
        this.fechainiciopension = fechainiciopension;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String getResolucionpension() {
        return resolucionpension;
    }

    public void setResolucionpension(String resolucionpension) {
        this.resolucionpension = resolucionpension;
    }

    public VigenciasTiposTrabajadores getVigenciatipotrabajador() {
        return vigenciatipotrabajador;
    }

    public void setVigenciatipotrabajador(VigenciasTiposTrabajadores vigenciatipotrabajador) {
        this.vigenciatipotrabajador = vigenciatipotrabajador;
    }

    public TiposPensionados getTipopensionado() {
        return tipopensionado;
    }

    public void setTipopensionado(TiposPensionados tipopensionado) {
        this.tipopensionado = tipopensionado;
    }

    public Personas getTutor() {
        return tutor;
    }

    public void setTutor(Personas tutor) {
        this.tutor = tutor;
    }

    public Empleados getCausabiente() {
        return causabiente;
    }

    public void setCausabiente(Empleados causabiente) {
        this.causabiente = causabiente;
    }

    public ClasesPensiones getClase() {
        return clase;
    }

    public void setClase(ClasesPensiones clase) {
        this.clase = clase;
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
        if (!(object instanceof Pensionados)) {
            return false;
        }
        Pensionados other = (Pensionados) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Pensionados[ secuencia=" + secuencia + " ]";
    }
    
}
