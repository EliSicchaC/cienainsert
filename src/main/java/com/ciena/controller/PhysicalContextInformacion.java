package com.ciena.controller;

import com.ciena.controller.dao.Conexion;
import com.ciena.controller.dao.DBRecord;
import com.ciena.controller.dao.DBTable;
import com.ciena.controller.entity.*;
import org.json.JSONObject;
import util.Util;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PhysicalContextInformacion {
    private static Conexion.DBConnector dataBase;
    private static DBTable tablaDicPhysical;
    private static DBTable tablaExpPhysical;


    //  solo esta para probar mis metodos
    public static void main(final String[] args) throws IOException {
        PhysicalContextInformacion physical = new PhysicalContextInformacion();
        physical.analizarInformacionPhysicalContext("D:\\archivos\\objetociena.json","tapi-common:context",
                "tapi-equipment:physical-context");
    }
    public Boolean analizarInformacionPhysicalContext(String rutaDelArchivo,String tapiContext,String physicalContext){
        Boolean analizo = false;
        PhysicalContextInformacion physical = new PhysicalContextInformacion();
        try{
            physical.diccionarioPhysical(rutaDelArchivo,tapiContext,physicalContext);
            analizo = true;
        } catch (Exception exception) {
            analizo = false;
            exception.printStackTrace();
        }
        return analizo;
    }

    public void diccionarioPhysical(String lugarDelArchivo,String tapiContext,String physicalContext) throws SQLException, ClassNotFoundException {
        Map<String, String> exp_physical = new HashMap<>();
        List<String> listaDeColumnas = new ArrayList<>();
        JSONObject objectEvaluado = null;
        try {
            JSONObject json = Util.parseJSONFile(lugarDelArchivo);
            JSONObject identifica = json.getJSONObject(tapiContext).
                    getJSONObject(physicalContext);
            objectEvaluado = identifica;
            Map<String, Object> objectMap = objectEvaluado.toMap();
            for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
                listaDeColumnas.add(entry.getKey());
            }
        } catch (Exception exception) {
            System.out.println("error:: " + exception.getMessage());
        }
        String[][] dicPhysical = new String[][]{
                {
                        "id", "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY"
                },
                {
                        "atribute_name", "varchar(250)"
                }
        };
        listaDeColumnas = listaDeColumnas.stream().distinct().collect(Collectors.toList());
        for (String objectos : listaDeColumnas) {
            String nombreColumna = objectos.replaceAll("-", "_").replaceAll(":", "_");
            if (nombreColumna.equals("uuid")) {
                exp_physical.put(nombreColumna, "varchar(50) primary key ");
            } else {
                exp_physical.put(nombreColumna, "MEDIUMTEXT");
            }
        }
        dataBase = new Conexion.DBConnector();
        tablaDicPhysical = Util.crearTablasGenerico(dataBase, "dic_Physical", tablaDicPhysical, dicPhysical);
        tablaExpPhysical = Util.crearTablasGenericoMap(dataBase, "exp_physical_context", tablaExpPhysical, exp_physical);
        DBRecord recorre = tablaDicPhysical.newRecord();
        for (String objetos : listaDeColumnas) {
            recorre = tablaDicPhysical.newRecord();
            try {
                recorre.addField("atribute_name", objetos);
                tablaDicPhysical.insert(recorre);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        DBRecord record = tablaExpPhysical.newRecord();
        Map<String, Object> objetosMap = objectEvaluado.toMap();
        record = tablaExpPhysical.newRecord();
        for (Map.Entry<String, Object> entry : objetosMap.entrySet()) {
            if (listaDeColumnas.stream().filter(x -> entry.getKey().equals(x)).findFirst().isPresent()) {
                record.addField(entry.getKey().replaceAll("-", "_").replaceAll(":", "_"), entry.getValue().toString());
            } else {
                record.addField(entry.getKey().replaceAll("-", "_").replaceAll(":", "_"), null);
            }
        }
        try {
            tablaExpPhysical.insert(record);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}