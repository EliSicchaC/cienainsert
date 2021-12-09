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

    public static void main(String[] args) {
        TopologyMain main = new TopologyMain();
        main.llamarAlMetodo("D:\\archivos\\objetociena.json","tapi-common:context",
                "tapi-topology:topology-context","topology");
    }
    public void llamarAlMetodo(String rutaDelArchivo,String tapiContext,String tapiTopology,String topology){
        TopologyMain main = new TopologyMain();
        try{
            main.diccionarioTopology(rutaDelArchivo,tapiContext,tapiTopology,topology);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public void diccionarioTopology(String lugarDelArchivo,String tapiContext,String tapiTopology,String topology) throws SQLException, ClassNotFoundException {
        Map<String, String> dic_Topology = new HashMap<>();
        List<String> listaDeColumnas = new ArrayList<>();
        JSONArray EvaluarATopology = null;
        try{
            JSONObject json = Util.parseJSONFile(lugarDelArchivo);
            JSONObject identifica = json.getJSONObject(tapiContext).
                    getJSONObject(tapiTopology);
            JSONArray identificaElementos = identifica.getJSONArray(topology);
        } catch (Exception exception) {
            System.out.println("error:: " + exception.getMessage());
        }
        listaDeColumnas = listaDeColumnas.stream().distinct().collect(Collectors.toList());
        for (String objectos : listaDeColumnas){
            dic_Topology.put(objectos.replaceAll("-","_").replaceAll(":","_"), "MEDIUMTEXT");
        }
        dataBase = new Conexion.DBConnector();
        tablaTopology = Util.crearTablasGenericoMap(dataBase,"dic_topology",tablaTopology,dic_Topology);
    }
}
