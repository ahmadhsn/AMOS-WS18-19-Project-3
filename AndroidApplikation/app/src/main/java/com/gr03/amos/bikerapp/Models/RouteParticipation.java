package com.gr03.amos.bikerapp.Models;

import io.realm.RealmObject;

public class RouteParticipation extends RealmObject {
    private long idRoute;
    private long idUser;

    public RouteParticipation() {}

    public RouteParticipation(Long idUser, Long idEvent) {
        this.idUser = idUser;
        this.idRoute = idEvent;
    }

    public long getIdRoute() {
        return idRoute;
    }

    public void setIdRoute(long idRoute) {
        this.idRoute = idRoute;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long id_user) {
        this.idUser = id_user;
    }
}
