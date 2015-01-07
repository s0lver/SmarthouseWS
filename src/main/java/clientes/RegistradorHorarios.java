package clientes;

import tamps.cinvestav.se.listenerEventos.ws.Configuraciones;
import tamps.cinvestav.se.listenerEventos.ws.Ordenes;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

public class RegistradorHorarios {

    public static String registrarHorario(int idLego, int idRecurso, int opcionesTimer, String hora){
    String respuesta = "";

    try {
        URL url = new URL("http://localhost:9998/ws/configuraciones?wsdl");

        QName qname = new QName("http://ws.listenerEventos.se.cinvestav.tamps/", "ConfiguracionesImplService");

        Service service = Service.create(url, qname);

        Configuraciones configuracionesListener = service.getPort(Configuraciones.class);

        respuesta = configuracionesListener.agregarConfiguracion(idLego, idRecurso, opcionesTimer, hora);

    } catch (MalformedURLException e) {
        e.printStackTrace();
    }

    return respuesta;
}
}
