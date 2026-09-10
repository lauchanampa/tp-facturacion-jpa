package org.example.entidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "condicion_iva", schema = "modelo")
@SequenceGenerator(schema = "modelo", sequenceName = "condicion_iva_seq", name = "condicion_iva_generator")
public class CondicionIva extends AuditoriaApp {

    @Column(nullable = false)
    private int codigoAfip;

    @Column(nullable = false)
    private String denominacion;

    public CondicionIva() {
    }

    public CondicionIva(int codigoAfip, String denominacion) {
        this.codigoAfip = codigoAfip;
        this.denominacion = denominacion;
    }

    public int getCodigoAfip() {
        return codigoAfip;
    }

    public void setCodigoAfip(int codigoAfip) {
        this.codigoAfip = codigoAfip;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }
}
