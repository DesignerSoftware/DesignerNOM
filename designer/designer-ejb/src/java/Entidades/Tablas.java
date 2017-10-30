package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author -Felipphe-
 */
@Entity
public class Tablas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Size(min = 1, max = 30)
    @Column(name = "TIPO")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NOMBRE")
    private String nombre;
    @JoinColumn(name = "MODULO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Modulos modulo;
    @Size(min = 1, max = 80)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "LLEVARLOG")
    private String llevarLog;
    @Column(name = "CAPTURARXPLANO")
    private String capturarXPlano;
    @Column(name = "TRACE")
    private String trace;
    @Column(name = "NUMEROFILA")
    private String numeroFila;
    @Column(name = "ORDEN")
    private String orden;
    @Column(name = "CLASIFICACION")
    private String clasificacion;
    @Size(max = 1)
    @Column(name = "AUTORIZADA")
    private String autorizada;
    @Transient
    private String estadoTipo;

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Modulos getModulo() {
        return modulo;
    }

    public void setModulo(Modulos modulo) {
        this.modulo = modulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getLlevarLog() {
        return llevarLog;
    }

    public void setLlevarLog(String llevarLog) {
        this.llevarLog = llevarLog;
    }

    public String getCapturarXPlano() {
        return capturarXPlano;
    }

    public void setCapturarXPlano(String capturarXPlano) {
        this.capturarXPlano = capturarXPlano;
    }

    public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }

    public String getNumeroFila() {
        return numeroFila;
    }

    public void setNumeroFila(String numeroFila) {
        this.numeroFila = numeroFila;
    }

    public String getOrden() {
        return orden;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public String getAutorizada() {
        return autorizada;
    }

    public void setAutorizada(String autorizada) {
        this.autorizada = autorizada;
    }

    public String getEstadoTipo() {
        if (estadoTipo == null) {
            if (tipo == null) {
                estadoTipo = " ";
            } else if (tipo.equalsIgnoreCase("CONFIGURACION")) {
                estadoTipo = "CONFIGURACION";
            } else if (tipo.equalsIgnoreCase("CONSTRUCCION")) {
                estadoTipo = "CONSTRUCCION";
            } else if (tipo.equalsIgnoreCase("DATOS")) {
                estadoTipo = "DATOS";
            } else if (tipo.equalsIgnoreCase("SISTEMA")) {
                estadoTipo = "SISTEMA";
            } else if (tipo.equalsIgnoreCase("POR ASIGNAR")) {
                estadoTipo = "POR ASIGNAR";
            }
        }
        return estadoTipo;
    }

    public void setEstadoTipo(String estadoTipo) {
        this.estadoTipo = estadoTipo;
    }

}
