package controllers;

import administradores.AdministradorHorarios;
import clientes.RegistradorHorarios;
import iadministradores.IAdministradorHorarios;
import modelos.HorariosEntity;
import org.hibernate.Session;
import transformaciones.TransformarHorario;
import util.SessionUtil;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.sql.Time;

@Path("/horarios")
public class HorariosController {
    private Session sesion;
    private IAdministradorHorarios administradorHorarios;

    public HorariosController(){
        sesion  = SessionUtil.getSession();
        administradorHorarios = new AdministradorHorarios(sesion);
    }

    @Path("/porLegoYRecurso/{idLego}/{idRecurso}")
    @GET
    @Produces("application/json")
    public JsonObject porIDLegoYIDRecurso(@PathParam("idLego") int idLego,
                                          @PathParam("idRecurso") int idRecurso) {
        HorariosEntity horario = administradorHorarios.buscarPorIdLegoIdRecurso(idLego, idRecurso);

        sesion.close();
        return TransformarHorario.transformarEnHorarioJSON(horario).build();
    }

    @Path("/modificarHorario/{idLego}/{idRecurso}/{idLegoRecurso}/{horaInicio}/{horaFin}")
    @GET
    @Produces("application/json")
    public JsonObject modificarHorario(@PathParam("idLego") int idLego,
                                       @PathParam("idRecurso") int idRecurso,
                                       @PathParam("idLegoRecurso") int idLegoRecurso,
                                       @PathParam("horaInicio") String horaInicio,
                                       @PathParam("horaFin") String horaFin
    ) {
        HorariosEntity horario = new HorariosEntity();
        horario.setIdLegoRecurso(idLegoRecurso);

        int horas = Integer.parseInt(horaInicio.substring(0, 2));
        int minutos = Integer.parseInt(horaInicio.substring(3, 5));
        Time tiempo = new Time(horas, minutos, 0);
        horario.setHoraInicio(tiempo);

        horas = Integer.parseInt(horaFin.substring(0, 2));
        minutos = Integer.parseInt(horaFin.substring(3, 5));
        tiempo = new Time(horas, minutos, 0);
        horario.setHoraFin(tiempo);

        int id = administradorHorarios.agregar(horario);
        horario.setId(id);

        sesion.close();

        RegistradorHorarios.registrarHorario(idLego, idRecurso, 1, horaInicio);
        RegistradorHorarios.registrarHorario(idLego, idRecurso, 0, horaFin);

        return Json.createObjectBuilder().add("respuesta", "ok").build();
    }
}
