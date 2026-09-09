package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.entidad.*;

import java.util.ArrayList;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("FacturacionPU");

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Usuario usuario = new Usuario(
                    "jgatica",
                    "1234",
                    "Julian",
                    "Gatica"
            );
            em.persist(usuario);


            PuntoVenta puntoVenta = new PuntoVenta(
                    1,
                    "Punto de Venta Principal",
                    "E",
                    "Av. Las Heras 123"
            );
            puntoVenta.setFechaAlta(new java.util.Date());
            puntoVenta.setFechaModificacion(new java.util.Date());
            puntoVenta.setUsuarioCarga(usuario);
            puntoVenta.setUsuarioModificacion(usuario);
            em.persist(puntoVenta);


            Rubro rubro = new Rubro(
                    "Bebidas",
                    1
            );
            rubro.setFechaAlta(new java.util.Date());
            rubro.setFechaModificacion(new java.util.Date());
            rubro.setUsuarioCarga(usuario);
            rubro.setUsuarioModificacion(usuario);
            em.persist(rubro);


            Marca marca = new Marca(
                    "Coca Cola",
                    1
            );
            marca.setFechaAlta(new java.util.Date());
            marca.setFechaModificacion(new java.util.Date());
            marca.setUsuarioCarga(usuario);
            marca.setUsuarioModificacion(usuario);
            em.persist(marca);


            Articulo articulo = new Articulo(
                    rubro,
                    "52",
                    "Coca Cola 500ml",
                    marca
            );
            articulo.setFechaAlta(new java.util.Date());
            articulo.setFechaModificacion(new java.util.Date());
            articulo.setUsuarioCarga(usuario);
            articulo.setUsuarioModificacion(usuario);
            em.persist(articulo);


            ListaPrecio listaPrecio = new ListaPrecio(
                    "LP001",
                    "Lista de precios general"
            );
            listaPrecio.setFechaAlta(new java.util.Date());
            listaPrecio.setFechaModificacion(new java.util.Date());
            listaPrecio.setUsuarioCarga(usuario);
            listaPrecio.setUsuarioModificacion(usuario);
            em.persist(listaPrecio);


            ListaPrecioArticulo listaPrecioArticulo = new ListaPrecioArticulo(
                    listaPrecio,
                    1500.00,
                    articulo
            );
            listaPrecioArticulo.setFechaAlta(new java.util.Date());
            listaPrecioArticulo.setFechaModificacion(new java.util.Date());
            listaPrecioArticulo.setUsuarioCarga(usuario);
            listaPrecioArticulo.setUsuarioModificacion(usuario);
            em.persist(listaPrecioArticulo);

            FacturaVenta facturaVenta = new FacturaVenta(
                    4L,
                    new Date(),
                    puntoVenta,
                    0,
                    0,
                    1500,
                    null,
                    null,
                    "APROBADO",
                    null,
                    "EMITIDA",
                    null,
                    "Factura de prueba",
                    new ArrayList<>()
            );
            facturaVenta.setFechaAlta(new Date());
            facturaVenta.setFechaModificacion(new Date());
            facturaVenta.setUsuarioCarga(usuario);
            facturaVenta.setUsuarioModificacion(usuario);


            FacturaVentaDetalle detalle = new FacturaVentaDetalle(
                    facturaVenta,
                    listaPrecioArticulo,
                    "Coca Cola 500ml",
                    1,
                    1500,
                    0,
                    1500,
                    315,
                    1815
            );

            facturaVenta.addDetalle(detalle);

            em.persist(facturaVenta);

            em.getTransaction().commit();

        }catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }finally {
            em.close();
            emf.close();
        }
    }
}