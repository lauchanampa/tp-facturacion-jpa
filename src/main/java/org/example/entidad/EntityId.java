package org.example.entidad;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class EntityId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    public EntityId() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
