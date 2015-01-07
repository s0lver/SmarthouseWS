package controllers;

import administradores.AdministradorLegos;
import modelos.LegosEntity;
import org.glassfish.jersey.server.mvc.Viewable;
import org.hibernate.Session;
import transformaciones.TransformarLego;
import util.SessionUtil;

import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.ws.WebServiceException;
import java.net.URI;
import java.util.Collection;

@Path("/legos")
public class LegosController {
    private AdministradorLegos administradorLegos;

    private Session sesion;

    public LegosController(){
        sesion  = SessionUtil.getSession();
        administradorLegos = new AdministradorLegos(sesion);
    }

    @Path("/{idLego}")
    @GET
    @Produces("application/json")
    public JsonObject lego(@PathParam("idLego") int idLego) {
        LegosEntity lego = administradorLegos.buscarPorId(idLego);
        JsonObjectBuilder creadorLego = null;

        if (null != lego) {
             creadorLego = TransformarLego.transformarEnLegoJSON(lego);
        }

        sesion.close();

        if (null != lego) return creadorLego.build();

        throw new WebServiceException(String.format("Lego con id %d no encontrado", idLego));
    }

    @Path("/todos")
    @GET
    @Produces("application/json")
    public JsonArray todos() {
        Collection<LegosEntity> legos = administradorLegos.lista();

        JsonArrayBuilder creador = TransformarLego.transformarEnListaJSON(legos);

        sesion.close();

        return creador.build();
    }

    @Path("/porTipo/{idTipo}")
    @GET
    @Produces("application/json")
    public JsonArray porTipo(@PathParam("idTipo") int idTipo) {
        administradorLegos = new AdministradorLegos(sesion);

        Collection<LegosEntity> legos = administradorLegos.listaPorTipo(idTipo);

        JsonArrayBuilder creador = TransformarLego.transformarEnListaJSON(legos);

        sesion.close();

        return creador.build();
    }

    @Path("/detallesLego/{idLego}")
    @GET
    @Produces("text/html")
    public Response detallesLego(@PathParam("idLego") int idLego) {
        return Response.ok(new Viewable("/legos/lego", idLego)).build();
    }

    @Path("nuevo")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createMessage(
            @FormParam("ddlTipoLego") String ddlTipoLego, @FormParam("mac") String mac) {
        if (null == ddlTipoLego || null== mac) {
            return Response.serverError().status(Response.Status.BAD_REQUEST).build();
        }
        if (mac.length() == 0) {
            return Response.serverError().status(Response.Status.BAD_REQUEST).build();
        }

        LegosEntity lego = new LegosEntity();
        lego.setIdTipoLego(Integer.parseInt(ddlTipoLego));
        lego.setMac(mac);

        int id = administradorLegos.agregar(lego);

        sesion.close();

        return Response.created(URI.create("legos/lego/" + id)).build();
    }
}
