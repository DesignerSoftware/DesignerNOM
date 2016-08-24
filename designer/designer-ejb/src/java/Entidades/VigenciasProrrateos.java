package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user
 */
@Entity
@Table(name = "VIGENCIASPRORRATEOS")
public class VigenciasProrrateos implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PORCENTAJE")
    private BigDecimal porcentaje;
    @Column(name = "FECHAINICIAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechainicial;
    @Column(name = "FECHAFINAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechafinal;
    @Column(name = "SUBPORCENTAJE")
    private BigInteger subporcentaje;
    @JoinColumn(name = "VIGLOCALIZACION", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private VigenciasLocalizaciones viglocalizacion;
    @JoinColumn(name = "PROYECTO", referencedColumnName = "SECUENCIA")
    @ManyToOne(cascade={CascadeType.DETACH},fetch=FetchType.LAZY)
    private Proyectos proyecto;
    @JoinColumn(name = "CENTROCOSTO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private CentrosCostos centrocosto;

    public VigenciasProrrateos() {
    }

    public VigenciasProrrateos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public VigenciasProrrateos(BigInteger secuencia, BigDecimal porcentaje, Date fechainicial) {
        this.secuencia = secuencia;
        this.porcentaje = porcentaje;
        this.fechainicial = fechainicial;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Date getFechainicial() {
        return fechainicial;
    }

    public void setFechainicial(Date fechainicial) {
        this.fechainicial = fechainicial;
    }

    public Date getFechafinal() {
        return fechafinal;
    }

    public void setFechafinal(Date fechafinal) {
        this.fechafinal = fechafinal;
    }

    public BigInteger getSubporcentaje() {
        return subporcentaje;
    }

    public void setSubporcentaje(BigInteger subporcentaje) {
        this.subporcentaje = subporcentaje;
    }

    public VigenciasLocalizaciones getViglocalizacion() {
        return viglocalizacion;
    }

    public void setViglocalizacion(VigenciasLocalizaciones viglocalizacion) {
        this.viglocalizacion = viglocalizacion;
    }

    public Proyectos getProyecto() {
        if(proyecto == null){
            //System.out.println("Error en: VigenciasProrrateos.getProyecto():  proyecto == null");
            proyecto = new Proyectos();
            return proyecto;
        }else{
            return proyecto;
        }
    }

    public void setProyecto(Proyectos proyecto) {
        this.proyecto = proyecto;
    }

    public CentrosCostos getCentrocosto() {
        return centrocosto;
    }

    public void setCentrocosto(CentrosCostos centrocosto) {
        this.centrocosto = centrocosto;
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
        if (!(object instanceof VigenciasProrrateos)) {
            return false;
        }
        VigenciasProrrateos other = (VigenciasProrrateos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Vigenciasprorrateos[ secuencia=" + secuencia + " ]";
    }
    
}
