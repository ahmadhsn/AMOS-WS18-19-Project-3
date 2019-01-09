package com.gr03.amos.bikerapp.Models;

import io.realm.RealmObject;

public class EventParticipation extends RealmObject {
    private long id_event;
    private long id_user;

    public EventParticipation() {}

    public EventParticipation(Long id_user, Long id_event) {
        this.id_user = id_user;
        this.id_event = id_event;
    }

    public long getId_event() {
        return id_event;
    }

    public void setId_event(long id_event) {
        this.id_event = id_event;
    }

    public long getId_user() {
        return id_user;
    }

    public void setId_user(long id_user) {
        this.id_user = id_user;
    }
}
