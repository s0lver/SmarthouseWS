package controllers;

import administradores.AdministradorEventos;
import iadministradores.IAdministradorEventos;
import modelos.EventosEntity;
import org.hibernate.Session;
import transformaciones.TransformarEvento;
import util.SessionUtil;

import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.xml.ws.WebServiceException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

@Path("/eventos")
public class EventosController {
    private IAdministradorEventos administradorEventos;

    private Session sesion;

    public EventosController(){
        sesion  = SessionUtil.getSession();
        administradorEventos = new AdministradorEventos(sesion);
    }

    @Path("/{idEvento}")
    @GET
    @Produces("application/json")
    public JsonObject evento(@PathParam("idEvento") int idEvento) {
        EventosEntity evento = administradorEventos.buscarPorId(idEvento);
        JsonObjectBuilder creadorEvento = null;

        if (null != evento) {
            creadorEvento = TransformarEvento.transformarEnEventoJSON(evento);
        }

        sesion.close();

        if (null != evento) return creadorEvento.build();

        throw new WebServiceException(String.format("Evento con id %d no encontrado", idEvento));
    }

    @Path("/todos")
    @GET
    @Produces("application/json")
    public JsonArray todos(@Context UriInfo info) {
        String strFechaInicio = info.getQueryParameters().getFirst("fechaInicio");
        String strFechaFin = info.getQueryParameters().getFirst("fechaFin");

        Date fechaInicio = prepararFechaInicio(strFechaInicio);
        Date fechaFin = prepararFechaFin(strFechaFin);

        Collection<EventosEntity> eventos = administradorEventos.listaConFechas(fechaInicio, fechaFin);

        JsonArrayBuilder creador = TransformarEvento.transformarEnListaJSON(eventos);

        sesion.close();

        return creador.build();

    }

    @Path("/porLego/{idLego}")
    @GET
    @Produces("application/json")
    public JsonArray porLego(@PathParam("idLego") int idLego, @Context UriInfo info) {
        String strFechaInicio = info.getQueryParameters().getFirst("fechaInicio");
        String strFechaFin = info.getQueryParameters().getFirst("fechaFin");

        Date fechaInicio = prepararFechaInicio(strFechaInicio);
        Date fechaFin = prepararFechaFin(strFechaFin);

        Collection<EventosEntity> eventos = administradorEventos.listaPorLegoConFechas(idLego, fechaInicio, fechaFin);

        JsonArrayBuilder creador = TransformarEvento.transformarEnListaJSON(eventos);

        sesion.close();

        return creador.build();
    }

    @Path("/porTipoLego/{idTipoLego}")
    @GET
    @Produces("application/json")
    public JsonArray porTipoLego(@PathParam("idTipoLego") int idTipoLego, @Context UriInfo info) {
        String strFechaInicio = info.getQueryParameters().getFirst("fechaInicio");
        String strFechaFin = info.getQueryParameters().getFirst("fechaFin");

        Date fechaInicio = intentarParsing(strFechaInicio);
        Date fechaFin = intentarParsing(strFechaFin);

        Collection<EventosEntity> eventos = administradorEventos.listaPorTipoLegoConFechas(idTipoLego, fechaInicio, fechaFin);

        JsonArrayBuilder creador = TransformarEvento.transformarEnListaJSON(eventos);

        sesion.close();

        return creador.build();
    }

    private static Date intentarParsing(String strFecha) {
        try {
            if (strFecha == null) {
                return null;
            }else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return sdf.parse(strFecha);
            }
        }catch (ParseException e) {
            return null;
        }
    }

    private static Date prepararFechaInicio(String strFecha) {
        return intentarParsing(strFecha);
    }

    private static Date prepararFechaFin(String strFecha) {
        Date fecha = intentarParsing(strFecha);
        if (fecha == null) {
            return null;
        } else {
            int segundo = 1000;
            int minuto = 60 * segundo;
            long hora = minuto * 60;

            Date fechaFin = new Date(fecha.getTime() + (23 * hora) + (59 * minuto) + (59 * segundo));
            return fechaFin;
        }
    }
}
