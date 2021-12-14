package util;

import com.ciena.controller.dao.Conexion;
import com.ciena.controller.dao.DBTable;
import com.ciena.controller.entity.Name;
import com.ciena.controller.entity.ObjetosPrincipales;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public  class Util {
    public static ObjetosPrincipales getObjetosPrincipales(String rutaDeArchivo) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        // CONFIGURAMOS LA CLASE MAPPER CON ALGUNOS PARAMETROS PARA EVITAR ERRORES DE SITAXIS
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // FAIL_ON_UNKNOWN_PROPERTIES QUE FALLE SI NO ENCUENTRA PROPIEDADES ? FALSE -- NO, QUE SIGA
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // ACCEPT_SINGLE_VALUE_AS_ARRAY QUE ACEPTE ARREGLOS -- TRUE, QUE SI ACEPTE
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        // LEER EL ARCHIVO JSON PARA ELLO USAMOS LA CLASE FILE QUE ME PIDE UNA RUTA DONDE ESTA EL ARCHIVO
        File archivoJson = new File(rutaDeArchivo);

        // PARA PODER TRANSFORMAR EL ARCHIVO A OBJETOS USAMOS EL READVALUE DE MAPPER, QUE ME PIDE EL ARCHIVO Y LA CLASE QUE ME SERVIRA PARA ALMACENAR LOS DATOS DEL ARCHIVO
        ObjetosPrincipales principal = mapper.readValue(archivoJson, ObjetosPrincipales.class);
        return principal;
    }
    // REUTILIZANDO NAME
    public static String generarNames(List<Name> listaDeNames) {
        String nameStringValue = "";
        for (Name name: listaDeNames) {
            nameStringValue = nameStringValue + name.getValue_name() + " : " + name.getValue() + " \n";
        }
        return nameStringValue;
    }

    public static DBTable crearTablasGenerico(Conexion.DBConnector dataBase, String nombreTabla, DBTable tabla, String[][] fields ) throws SQLException, ClassNotFoundException {
        //PASO 1 ELIMINAR LA TABLA ANTERIOR
        tabla = dataBase.deleteTableIfExsist(nombreTabla);
        tabla.createTable(fields);
        return tabla;
    }
    public static DBTable crearTablasGenericoMap(Conexion.DBConnector dataBase, String nombreTabla, DBTable tabla, Map<String, String> fields) throws SQLException, ClassNotFoundException {
        //PASO 1 ELIMINAR LA TABLA ANTERIOR
        tabla = dataBase.deleteTableIfExsist(nombreTabla);
        tabla.createTableMap(fields);
        return tabla;
    }
    //TRANSFORMA A JSONOBJECT
    public static JSONObject parseJSONFile(String filename) throws IOException {
        String contenido = new String(Files.readAllBytes(Paths.get(filename)));
        return new JSONObject(contenido);
    }

    public static JSONObject retonarListaPropiedadesAsociadasNodoHijo(JSONObject archivoJson, String nodoPadre, String nodoHijoPrimerSegmento ){
        JSONObject propiedades = null;
        try {
            propiedades = archivoJson.getJSONObject(nodoPadre)
                    .getJSONObject(nodoHijoPrimerSegmento);

        } catch (Exception exception) {
            System.out.println("error:: " + exception.getMessage());
        }
         return propiedades;
    }

    public static List < String > listaDeColumnasPadreArray(JSONArray nodoConLasColumnas, String nodo) {
        List < String > listaDeColumnas = new ArrayList < > ();
        for (Object objetosNode: nodoConLasColumnas) {
            JSONObject ownedNode = (JSONObject) objetosNode;
            JSONArray listaDeEdgePoint = ownedNode.getJSONArray(nodo);
            for (Object objetoEvaluado: listaDeEdgePoint) {
                JSONObject objetoEvaluadoJson = (JSONObject) objetoEvaluado;
                Map < String, Object > objectMap = objetoEvaluadoJson.toMap();
                for (Map.Entry < String, Object > entry: objectMap.entrySet()) {
                    listaDeColumnas.add(entry.getKey());
                }
            }
        }
        return listaDeColumnas;

    }
    public static List < String > listaDeColumnasPadreObject(JSONObject nodoConLasColumnas, String nodo) {
        List < String > listaDeColumnas = new ArrayList < > ();
        JSONArray listaDeEdgePoint = nodoConLasColumnas.getJSONArray(nodo);
        for (Object objetoEvaluado: listaDeEdgePoint) {
            JSONObject objetoEvaluadoJson = (JSONObject) objetoEvaluado;
            Map < String, Object > objectMap = objetoEvaluadoJson.toMap();
            for (Map.Entry < String, Object > entry: objectMap.entrySet()) {
                listaDeColumnas.add(entry.getKey());
            }
        }

        return listaDeColumnas;

    }

}
