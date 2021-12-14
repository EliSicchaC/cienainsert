package com.ciena.controller;

import com.ciena.controller.dao.Conexion;
import com.ciena.controller.dao.DBRecord;
import com.ciena.controller.dao.DBTable;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Util;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TopologyMain {
    private static Conexion.DBConnector dataBase;
    private static DBTable tablaTopology;
    private static DBTable tablaDictTopology;

    public static void main(String[] args) {
        TopologyMain main = new TopologyMain();
        main.llamarAlMetodo("D:\\archivos\\objetociena.json","tapi-common:context",
                "tapi-topology:topology-context","topology");
    }
    public void llamarAlMetodo(String rutaDelArchivo,String tapiContext,String tapiTopology,String topology){
        TopologyMain main = new TopologyMain();
        try{
            main.insertarTopology(rutaDelArchivo,tapiContext,tapiTopology,topology);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public void insertarTopology(String lugarDelArchivo, String tapiContext, String tapiTopology, String topology) throws SQLException, ClassNotFoundException {
        Map<String, String> exp_Topology = new HashMap<>();
        List<String> listaDeColumnas = new ArrayList<>();
        JSONArray evaluarATopology = null;
        try{
            JSONObject json = Util.parseJSONFile(lugarDelArchivo);
            JSONObject identifica = json.getJSONObject(tapiContext).
                    getJSONObject(tapiTopology);
            evaluarATopology = identifica.getJSONArray(topology);
            JSONArray identificaElementos = identifica.getJSONArray(topology);
            for(Object objetos : identificaElementos){
                JSONObject lineaDeElementos = (JSONObject) objetos;
                Map<String, Object> objectMap = lineaDeElementos.toMap();
                for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
                    listaDeColumnas.add(entry.getKey());
                }
            }
        } catch (Exception exception) {
            System.out.println("error:: " + exception.getMessage());
        }
        String[][] dicTopology = new String[][]{
                {
                        "id","int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY"
                },
                {
                        "atribute_name","varchar(250)"
                }
        };
        listaDeColumnas = listaDeColumnas.stream().distinct().collect(Collectors.toList());
        for (String objetos : listaDeColumnas){
            String nombreColumna = objetos.replaceAll("-","_").replaceAll(":","_");
            if(nombreColumna.equals("uuid")){
                //TIPO DE DATO, FOREING KEY, NOMBREDECOLUMNA Y ESTO HACE RELACION AL NOMBRE DE MI TABLA
                //PRIMERO SE CREA EL PADRE PARA QUE PUEDAN EXISTIR LOS HIJOS. SI NO ENTONCES NO SE VA A PODER RELACIONAR
                exp_Topology.put(nombreColumna, "varchar(50) PRIMARY KEY ");
            }else{
                exp_Topology.put(nombreColumna, "MEDIUMTEXT");

            }
        }
        exp_Topology.put("uuid_topology_context","varchar(250)");
        dataBase = new Conexion.DBConnector();
        tablaDictTopology = Util.crearTablasGenerico(dataBase,"dic_topology",tablaDictTopology,dicTopology);
        tablaTopology = Util.crearTablasGenericoMap(dataBase,"exp_topology",tablaTopology,exp_Topology);
        DBRecord recorre = tablaDictTopology.newRecord();
        for(String objetos : listaDeColumnas){
            recorre = tablaDictTopology.newRecord();
            try {
                recorre.addField("atribute_name", objetos);
                tablaDictTopology.insert(recorre);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        DBRecord record = tablaTopology.newRecord();
        for (Object objectEvaluado : evaluarATopology) {
            JSONObject objetosEvaluadoDeJson = (JSONObject) objectEvaluado;
            Map<String, Object> objetosMap = objetosEvaluadoDeJson.toMap();
            record = tablaTopology.newRecord();
            for (Map.Entry<String, Object> entry : objetosMap.entrySet()){
                if (listaDeColumnas.stream().filter(x -> entry.getKey().equals(x)).findFirst().isPresent()){
                    record.addField(entry.getKey().replaceAll("-","_").replaceAll(":","_"), entry.getValue().toString());
                } else {
                    record.addField(entry.getKey().replaceAll("-","_").replaceAll(":","_"), null);
                }
            }
            try {
                tablaTopology.insert(record);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }
}
