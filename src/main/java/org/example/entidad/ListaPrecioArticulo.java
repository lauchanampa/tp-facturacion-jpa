package org.example.entidad;

import jakarta.persistence.*;

@Entity
@Table(name = "lista_precio_articulo", schema = "modelo")
@SequenceGenerator(schema = "modelo", sequenceName = "lista_precio_articulo_seq", name = "lista_precio_articulo_generator")
public class ListaPrecioArticulo extends AuditoriaApp {

    @ManyToOne
    @JoinColumn(nullable = false)
    private ListaPrecio listaPrecio;

    @Column(nullable = false)
    private double precioVenta;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Articulo articulo;

    public ListaPrecioArticulo() {
    }

    public ListaPrecioArticulo(ListaPrecio listaPrecio, double precioVenta, Articulo articulo) {
        this.listaPrecio = listaPrecio;
        this.precioVenta = precioVenta;
        this.articulo = articulo;
    }

    public ListaPrecio getListaPrecio() {
        return listaPrecio;
    }

    public void setListaPrecio(ListaPrecio listaPrecio) {
        this.listaPrecio = listaPrecio;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }
}
