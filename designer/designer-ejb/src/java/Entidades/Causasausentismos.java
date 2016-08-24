package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "CAUSASAUSENTISMOS")
public class Causasausentismos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "CODIGO")
    private Short codigo;
    @Size(max = 50)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Size(max = 1)
    @Column(name = "DESCUENTATIEMPOCONTINUO")
    private String descuentatiempocontinuo;
    @Size(max = 3)
    @Column(name = "ORIGENINCAPACIDAD")
    private String origenincapacidad;
    @Size(max = 1)
    @Column(name = "REMUNERADA")
    private String remunerada;
    @Column(name = "PORCENTAJELIQUIDACION")
    private BigDecimal porcentajeliquidacion;
    @Column(name = "RESTADIASINCAPACIDAD")
    private Short restadiasincapacidad;
    @Size(max = 50)
    @Column(name = "FORMALIQUIDACION")
    private String formaliquidacion;
    @Size(max = 1)
    @Column(name = "RESTATIEMPOSOLOPRESTACIONES")
    private String restatiemposoloprestaciones;
    @Size(max = 1)
    @Column(name = "GARANTIZABASESML")
    private String garantizabasesml;
    @JoinColumn(name = "CLASE", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Clasesausentismos clase;
    @Transient
    private String estadoOrigenIncapacidad;
    @Transient
    private String estadoFormaLiquidacion;
    @Transient
    private boolean estadoRemunerada;
    @Transient
    private boolean estadoDescuentaTiempoContinuo;
    @Transient
    private boolean estadoRestaTiempoSoloPrestaciones;
    @Transient
    private boolean estadoGarantizaBaseml;

    public Causasausentismos() {
    }

    public Causasausentismos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Causasausentismos(BigInteger secuencia, Short codigo) {
        this.secuencia = secuencia;
        this.codigo = codigo;
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
        if (descripcion == null) {
            descripcion = ("");
        }
        return descripcion.toUpperCase();
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescuentatiempocontinuo() {
        return descuentatiempocontinuo;
    }

    public void setDescuentatiempocontinuo(String descuentatiempocontinuo) {
        this.descuentatiempocontinuo = descuentatiempocontinuo;
    }

    public String getOrigenincapacidad() {
        return origenincapacidad;
    }

    public void setOrigenincapacidad(String origenincapacidad) {
        this.origenincapacidad = origenincapacidad;
    }

    public String getRemunerada() {
        return remunerada;
    }

    public void setRemunerada(String remunerada) {
        this.remunerada = remunerada;
    }

    public BigDecimal getPorcentajeliquidacion() {
        return porcentajeliquidacion;
    }

    public void setPorcentajeliquidacion(BigDecimal porcentajeliquidacion) {
        this.porcentajeliquidacion = porcentajeliquidacion;
    }

    public Short getRestadiasincapacidad() {
        return restadiasincapacidad;
    }

    public void setRestadiasincapacidad(Short restadiasincapacidad) {
        this.restadiasincapacidad = restadiasincapacidad;
    }

    public String getFormaliquidacion() {
        return formaliquidacion;
    }

    public void setFormaliquidacion(String formaliquidacion) {
        this.formaliquidacion = formaliquidacion;
    }

    public String getRestatiemposoloprestaciones() {
        return restatiemposoloprestaciones;
    }

    public void setRestatiemposoloprestaciones(String restatiemposoloprestaciones) {
        this.restatiemposoloprestaciones = restatiemposoloprestaciones;
    }

    public String getGarantizabasesml() {
        return garantizabasesml;
    }

    public void setGarantizabasesml(String garantizabasesml) {
        this.garantizabasesml = garantizabasesml;
    }

    public Clasesausentismos getClase() {
        return clase;
    }

    public void setClase(Clasesausentismos clase) {
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
        if (!(object instanceof Causasausentismos)) {
            return false;
        }
        Causasausentismos other = (Causasausentismos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Causasausentismos[ secuencia=" + secuencia + " ]";
    }

    public String getEstadoOrigenIncapacidad() {
        getOrigenincapacidad();
        if (origenincapacidad == null) {
            estadoOrigenIncapacidad = " ";
        } else {
            if (origenincapacidad.equalsIgnoreCase("AT")) {
                estadoOrigenIncapacidad = "AT";
            } else if (origenincapacidad.equalsIgnoreCase("EG")) {
                estadoOrigenIncapacidad = "EG";
            } else if (origenincapacidad.equalsIgnoreCase("EP")) {
                estadoOrigenIncapacidad = "EP";
            } else if (origenincapacidad.equalsIgnoreCase("MA")) {
                estadoOrigenIncapacidad = "MA";
            } else if (origenincapacidad.equalsIgnoreCase("OT")) {
                estadoOrigenIncapacidad = "OT";
            } else if (origenincapacidad.equalsIgnoreCase(" ")) {
                estadoOrigenIncapacidad = null;
            }
        }

        return estadoOrigenIncapacidad;
    }

    public void setEstadoOrigenIncapacidad(String estadoOrigenIncapacidad) {
        if (estadoOrigenIncapacidad.equals("AT")) {
            setOrigenincapacidad("AT");
        } else if (estadoOrigenIncapacidad.equals("EG")) {
            setOrigenincapacidad("EG");
        } else if (estadoOrigenIncapacidad.equals("EP")) {
            setOrigenincapacidad("EP");
        } else if (estadoOrigenIncapacidad.equals("MA")) {
            setOrigenincapacidad("MA");
        } else if (estadoOrigenIncapacidad.equals("OT")) {
            setOrigenincapacidad("OT");
        } else if (estadoOrigenIncapacidad.equals(" ")) {
            setOrigenincapacidad(null);
        }
        this.estadoOrigenIncapacidad = estadoOrigenIncapacidad;
    }

    public String getEstadoFormaLiquidacion() {
        getFormaliquidacion();
            if (formaliquidacion == null) {
                estadoFormaLiquidacion = " ";

            } else {
                if (formaliquidacion.equalsIgnoreCase("BASICO")) {
                    estadoFormaLiquidacion = "BASICO";
                } else if (formaliquidacion.equalsIgnoreCase("IBC MES ANTERIOR")) {
                    estadoFormaLiquidacion = "IBC MES ANTERIOR";
                } else if (formaliquidacion.equalsIgnoreCase("IBC MES ENERO")) {
                    estadoFormaLiquidacion = "IBC MES ENERO";
                } else if (formaliquidacion.equalsIgnoreCase("IBC MES INCAPACIDAD")) {
                    estadoFormaLiquidacion = "IBC MES INCAPACIDAD";
                } else if (formaliquidacion.equalsIgnoreCase("PROMEDIO ACUMULADOS 12 MESES")) {
                    estadoFormaLiquidacion = "PROMEDIO ACUMULADOS 12 MESES";
                } else if (formaliquidacion.equalsIgnoreCase("PROMEDIO IBC 12 MESES")) {
                    estadoFormaLiquidacion = "PROMEDIO IBC 12 MESES";
                } else if (formaliquidacion.equalsIgnoreCase("PROMEDIO IBC 6 MESES")) {
                    estadoFormaLiquidacion = "PROMEDIO IBC 6 MESES";
                } else if (formaliquidacion.equalsIgnoreCase(" ")) {
                    estadoFormaLiquidacion = null;
                }
            }
        
        return estadoFormaLiquidacion;
    }

    public void setEstadoFormaLiquidacion(String estadoFormaLiquidacion) {
        if (estadoFormaLiquidacion.equals("BASICO")) {
            setFormaliquidacion("BASICO");
        } else if (estadoFormaLiquidacion.equals("IBC MES ANTERIOR")) {
            setFormaliquidacion("IBC MES ANTERIOR");
        } else if (estadoFormaLiquidacion.equals("IBC MES ENERO")) {
            setFormaliquidacion("IBC MES ENERO");
        } else if (estadoFormaLiquidacion.equals("IBC MES INCAPACIDAD")) {
            setFormaliquidacion("IBC MES INCAPACIDAD");
        } else if (estadoFormaLiquidacion.equals("PROMEDIO ACUMULADOS 12 MESES")) {
            setFormaliquidacion("PROMEDIO ACUMULADOS 12 MESES");
        } else if (estadoFormaLiquidacion.equals("PROMEDIO IBC 12 MESES")) {
            setFormaliquidacion("PROMEDIO IBC 12 MESES");
        } else if (estadoFormaLiquidacion.equals("PROMEDIO IBC 6 MESES")) {
            setFormaliquidacion("PROMEDIO IBC 6 MESES");
        } else if (estadoFormaLiquidacion.equals(" ")) {
            setFormaliquidacion(null);
        }
        this.estadoFormaLiquidacion = estadoFormaLiquidacion;
    }

    public boolean isEstadoRemunerada() {
        if (remunerada != null) {
            if (remunerada.equals("S")) {
                estadoRemunerada = true;
            } else {
                estadoRemunerada = false;
            }
        } else {
            estadoRemunerada = false;
        }
        return estadoRemunerada;
    }

    public void setEstadoRemunerada(boolean estadoRemunerada) {
        if (estadoRemunerada == true) {
            remunerada = "S";
        } else {
            remunerada = "N";
        }
        this.estadoRemunerada = estadoRemunerada;
    }

    public boolean isEstadoDescuentaTiempoContinuo() {
        if (descuentatiempocontinuo != null) {
            if (descuentatiempocontinuo.equals("S")) {
                estadoDescuentaTiempoContinuo = true;
            } else {
                estadoDescuentaTiempoContinuo = false;
            }
        } else {
            estadoDescuentaTiempoContinuo = false;
        }
        return estadoDescuentaTiempoContinuo;
    }

    public void setEstadoDescuentaTiempoContinuo(boolean estadoDescuentaTiempoContinuo) {
        if (estadoDescuentaTiempoContinuo == true) {
            descuentatiempocontinuo = "S";
        } else {
            descuentatiempocontinuo = "N";
        }
        this.estadoDescuentaTiempoContinuo = estadoDescuentaTiempoContinuo;
    }

    public boolean isEstadoRestaTiempoSoloPrestaciones() {
        if (restatiemposoloprestaciones != null) {
            if (restatiemposoloprestaciones.equals("S")) {
                estadoRestaTiempoSoloPrestaciones = true;
            } else {
                estadoRestaTiempoSoloPrestaciones = false;
            }
        } else {
            estadoRestaTiempoSoloPrestaciones = false;
        }
        return estadoRestaTiempoSoloPrestaciones;
    }

    public void setEstadoRestaTiempoSoloPrestaciones(boolean estadoRestaTiempoSoloPrestaciones) {
        if (estadoRestaTiempoSoloPrestaciones == true) {
            restatiemposoloprestaciones = "S";
        } else {
            restatiemposoloprestaciones = "N";
        }
        this.estadoRestaTiempoSoloPrestaciones = estadoRestaTiempoSoloPrestaciones;
    }

    public boolean isEstadoGarantizaBaseml() {
        if (garantizabasesml != null) {
            if (garantizabasesml.equals("S")) {
                estadoGarantizaBaseml = true;
            } else {
                estadoGarantizaBaseml = false;
            }
        } else {
            estadoGarantizaBaseml = false;
        }
        return estadoGarantizaBaseml;
    }

    public void setEstadoGarantizaBaseml(boolean estadoGarantizaBaseml) {
        if (estadoGarantizaBaseml == true) {
            garantizabasesml = "S";
        } else {
            garantizabasesml = "N";
        }
        this.estadoGarantizaBaseml = estadoGarantizaBaseml;
    }

}
