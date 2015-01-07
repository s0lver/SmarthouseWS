package controllers;

import administradores.AdministradorHorarios;
import administradores.AdministradorLegos;
import administradores.AdministradorRecursos;
import clientes.RegistradorHorarios;
import clientes.RegistradorOrdenes;
import iadministradores.IAdministradorHorarios;
import iadministradores.IAdministradorLegos;
import iadministradores.IAdministradorRecursos;
import modelos.HorariosEntity;
import modelos.LegosEntity;
import modelos.RecursosEntity;
import org.hibernate.Session;
import transformaciones.TransformarRecurso;
import util.SessionUtil;

import javax.json.*;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.Collection;

@Path("/recursos")
public class RecursosController {
    private IAdministradorRecursos administradorRecursos;
    private IAdministradorLegos administradorLegos;
    private IAdministradorHorarios administradorHorarios;

    private Session sesion;

    public RecursosController(){
        sesion  = SessionUtil.getSession();
        administradorRecursos = new AdministradorRecursos(sesion);
        administradorLegos = new AdministradorLegos(sesion);
        administradorHorarios = new AdministradorHorarios(sesion);
    }

    @Path("/porLego/{idLego}")
    @GET
    @Produces("application/json")
    public JsonArray todos(@PathParam("idLego") int idLego) {
        LegosEntity lego = administradorLegos.buscarPorId(idLego);

        Collection<RecursosEntity> recursosPorLego = administradorRecursos.buscarPorTipoLego(lego.getIdTipoLego());

        JsonArrayBuilder creador = TransformarRecurso.transformarEnListaJSON(recursosPorLego);

        sesion.close();

        return creador.build();
    }

    @Path("/crearOrden/{idLego}/{idRecurso}/{accion}")
    @GET
    @Produces("application/json")
    public JsonObject ejecutarOrden(@PathParam("idLego") int idLego, @PathParam("idRecurso") int idRecurso, @PathParam("accion") int accion) {
        String respuesta = RegistradorOrdenes.registrarOrden(idLego, idRecurso, accion);
        JsonObjectBuilder creador = Json.createObjectBuilder();
        creador.add("respuesta", respuesta);

        return creador.build();
    }
}
