package com.ciena.controller;

import com.ciena.controller.dao.Conexion;
import com.ciena.controller.dao.DBRecord;
import com.ciena.controller.dao.DBTable;
import com.ciena.controller.entity.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import util.Util;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static Conexion.DBConnector dataBase;
    private static DBTable tablaPhysicalcontext;
    private static DBTable tablaDevice;
    private static DBTable tablaEquipment;
    private static DBTable tablaAccessport;

    //  solo esta para probar mis metodos
    public static void main(final String[] args) throws IOException {
        Main m = new Main();
        m.analizarInformacionPhysicalContext("D:\\archivos\\objetociena.json");
    }
    public Boolean analizarInformacionPhysicalContext(String rutaDeArchivo) throws IOException {
        Boolean analizo = false;
        ObjetosPrincipales principal = Util.getObjetosPrincipales(rutaDeArchivo);

        //PARA PROBAR SE ESTA IMPRIMIENDO UN CAMPO DEL JSON CON EL OBJETO principal previamente creado
        System.out.println("INFORMACION: " + principal.getTapi_common_context().getUuid());

        System.out.println("--------------------IMPRIMIR service-interface-point-------- \n");
        Main m = new Main(); // SE CREA ESTE OBJETO PARA PODER ACCEDER A LOS METODOS CREADOS
        // COMO ES UN ARCHIVO GRANDE, NECESITAMOS VALIDAR SI TODA LA INFO SE HA MAPEADO BIEN
        // PARA ESO CREO UN METODO imprimirServiceInterface que me va a IMPRIMIR TODO LO QUE TIENE  ESA LISTA List<ServiceInterface>(VER EL JSON0)
        try {
            crearTablas();
            // LLAMAR A LA CLASE CREADA

            m.insertarAccessPort(principal.getTapi_common_context().getTapi_equipment_physical_context());
            m.insertarEquipment(principal.getTapi_common_context().getTapi_equipment_physical_context());
            m.insertarDevice(principal.getTapi_common_context().getTapi_equipment_physical_context());
            m.insertarPhysicalContext(principal.getTapi_common_context().getTapi_equipment_physical_context());
            analizo = true;
        } catch (SQLException | ClassNotFoundException e) {
            analizo = false;
            e.printStackTrace();
        }
        //m.imprimirServiceInterface(principal.getTapi_common_context().getService_interface_point());
        //m.imprimirName(principal.getTapi_common_context().getGetName());
        //m.imprimirTapiStreaming(principal.getTapi_common_context().getTapi_streaming_stream_context());
        //m.imprimirTapiTopology(principal.getTapi_common_context().getTapi_topology_topology_context());

        return analizo;
    }




    private static void crearTablas() throws SQLException, ClassNotFoundException {
        /*dataBase = new Conexion.DBConnector();
        // PASO 2 LLENAR COMO QUIERO LA TABLA
        String[][] fields = new String[][] {
                {
                        "id",
                        "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY"
                }, {
                "uuid",
                "varchar(250)"
        },
                //{"device_uuid", "varchar(250)"},
                //{"physical_span_uuid", "varchar(250)"},
                {
                        "name",
                        "varchar(250)"
                }
        };
        //PASO 1 ELIMINAR LA TABLA ANTERIOR
        tablaPhysicalcontext = dataBase.deleteTableIfExsist("exp_physical_context");

        // CREAR LA TABLA en base a los fields que son las columnas y tipos de dato de mi tabla exp_physical_context..
        // PASO 3 CREAR LA TABLA
        tablaPhysicalcontext.createTable(fields);

        fields = new String[][] {
                {
                        "id",
                        "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY"
                }, {
                "uuid",
                "varchar(250)"
        },
                //{"equipment_uuid", "varchar(250)"},
                //{"access_port_uuid", "varchar(250)"},
                {
                        "name",
                        "varchar(250)"
                }, {
                "uuid_physical_context",
                "varchar(250)"
        }
        };
        //TAREA CREAR TABLAS FALTANTES
        //tablaDevice = dataBase.deleteTableIfExsist("exp_device");
        tablaDevice.createTable(fields);

        //tablaEquipment = dataBase.deleteTableIfExsist("exp_equipment");
        fields = new String[][] {
                {
                        "id",
                        "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY"
                }, {
                "uuid",
                "varchar(250)"
        }, {
                "name",
                "varchar(250)"
        },{
                "expected_equipment",
                "mediumtext"
        }, {
                "actual_equipment",
                "mediumtext"
        }, {
                "containedHolder",
                "mediumtext"
        }, {
                "geographicalLocation",
                "varchar(250)"
        }, {
                "equipmentLocation",
                "mediumtext"
        }, {
                "category",
                "varchar(250)"
        }, {
                "is_expected_actual_mismatch",
                "boolean"
        }, {
                "uuid_device",
                "varchar(50)"
        }
        };
        tablaEquipment.createTable(fields);

        tablaAccessport = dataBase.deleteTableIfExsist("exp_access_port");
        fields = new String[][] {
                {
                        "id",
                        "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY"
                }, {
                "uuid",
                "varchar(250)"
        }, {
                "connector_pin_uuid",
                "varchar(250)"
        }, {
                "name",
                "varchar(250)"
        }, {
                "uuid_device",
                "varchar(50)"
        }
        };
        tablaAccessport.createTable(fields);*/

        dataBase = new Conexion.DBConnector();
        String[][] physicalcontext = new String[][] {
                {
                        "id",
                        "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY"
                }, {
                "uuid",
                "varchar(250)"
        },
                //{"device_uuid", "varchar(250)"},
                //{"physical_span_uuid", "varchar(250)"},
                {
                        "name",
                        "varchar(250)"
                }
        };

        tablaPhysicalcontext = Util.crearTablasGenerico(dataBase,"exp_physical_context",tablaPhysicalcontext,physicalcontext);
        String[][] device = new String[][] {
                {
                        "id",
                        "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY"
                }, {
                "uuid",
                "varchar(250)"
        },
                //{"equipment_uuid", "varchar(250)"},
                //{"access_port_uuid", "varchar(250)"},
                {
                        "name",
                        "varchar(250)"
                }, {
                "uuid_physical_context",
                "varchar(250)"
        }
        };
        tablaDevice = Util.crearTablasGenerico(dataBase,"exp_device",tablaDevice,device);
        String[][] equipment = new String[][] {
                {
                        "id",
                        "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY"
                }, {
                "uuid",
                "varchar(250)"
        }, {
                "name",
                "varchar(250)"
        },{
                "expected_equipment",
                "mediumtext"
        }, {
                "actual_equipment",
                "mediumtext"
        }, {
                "containedHolder",
                "mediumtext"
        }, {
                "geographicalLocation",
                "varchar(250)"
        }, {
                "equipmentLocation",
                "mediumtext"
        }, {
                "category",
                "varchar(250)"
        }, {
                "is_expected_actual_mismatch",
                "boolean"
        }, {
                "uuid_device",
                "varchar(50)"
        }
        };
        tablaEquipment = Util.crearTablasGenerico(dataBase,"exp_equipment",tablaEquipment,equipment);
        String [][] accessport = new String[][] {
                {
                        "id",
                        "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY"
                }, {
                "uuid",
                "varchar(250)"
        }, {
                "connector_pin_uuid",
                "varchar(250)"
        }, {
                "name",
                "varchar(250)"
        }, {
                "uuid_device",
                "varchar(50)"
        }
        };
        tablaAccessport = Util.crearTablasGenerico(dataBase,"exp_access_port",tablaAccessport,accessport);
    }

    private void imprimirTapiTopology(TapiTopology tapiTopology) {
        System.out.println("  --- nw-topology-service ---  ");
        System.out.println(" -- uuid: " + tapiTopology.getNw_topology_service().getUuid());
        System.out.println("  --- topology ---  ");
        for (TopologyObject topology2: tapiTopology.getNw_topology_service().getTopology()) {
            System.out.println(" -- topology-uuid: " + topology2.getTopology_uuid());
        }
        for (Name name: tapiTopology.getNw_topology_service().getName()) {
            System.out.println("  --- Name ---  ");
            System.out.println(" -- value-name: " + name.getValue_name());
            System.out.println(" -- value: " + name.getValue());
        }

        for (Topology topology: tapiTopology.getTopology()) {
            System.out.println(" -- uuid: " + topology.getUuid());
            System.out.println("  --- Node --- ");
            for (Node node: topology.getNode()) {
                System.out.println(" -- uuid: " + node.getUuid());
                System.out.println(" -- lifecycle-state: " + node.getLifecycle_state());
                for (Name name: node.getName()) {
                    System.out.println("  --- Name ---  ");
                    System.out.println(" -- value-name: " + name.getValue_name());
                    System.out.println(" -- value: " + name.getValue());
                }
                System.out.println(" -- operational-state: " + node.getOperational_state());
                System.out.println(" --- owned-node-edge-point -- ");
                for (OwnedNode ownedNode: node.getOwned_node_edge_point()) {
                    System.out.println(" -- uuid: " + ownedNode.getUuid());
                    System.out.println(" -- termination-state: " + ownedNode.getTermination_state());
                    System.out.println(" -- termination-direction: " + ownedNode.getTermination_direction());
                    System.out.println(" -- layer-protocol-name: " + ownedNode.getLayer_protocol_name());
                    System.out.println(" -- lifecycle-state: " + ownedNode.getLifecycle_state());
                    for (Name name: ownedNode.getName()) {
                        System.out.println("  --- Name ---  ");
                        System.out.println("    -- value-name: " + name.getValue_name());
                        System.out.println("    -- value: " + name.getValue());
                    }
                    System.out.println(" -- operational-state: " + ownedNode.getOperational_state());
                    System.out.println("  --- tapi-equipment:supporting-access-port --- ");
                    System.out.println("   --- access-port --- ");
                    if (null != ownedNode.getTapi_equipment_supporting_access_port() && null != ownedNode.getTapi_equipment_supporting_access_port().getAccess_port()) {
                        System.out.println(" -- access-port-uuid: " + ownedNode.getTapi_equipment_supporting_access_port().getAccess_port().getAccess_port_uuid());
                        System.out.println(" -- device-uuid: " + ownedNode.getTapi_equipment_supporting_access_port().getAccess_port().getDevice_uuid());
                    }
                    System.out.println(" --- supported-cep-layer-protocol-qualifier --- ");
                    for (String supportedlayer: ownedNode.getSupported_cep_layer_protocol_qualifier()) {
                        System.out.println("        --- " + supportedlayer);
                    }
                    System.out.println(" -- administrative-state: " + ownedNode.getAdministrative_state());
                    System.out.println(" --- tapi-photonic-media:media-channel-node-edge-point-spec --- ");
                    System.out.println("   -- mc-pool -- ");
                    if (null != ownedNode.getTapi_photonic_media_media_channel_node_edge_point_spec() &&
                            null != ownedNode.getTapi_photonic_media_media_channel_node_edge_point_spec().getMc_pool()
                    ) {
                        if (null != ownedNode.getTapi_photonic_media_media_channel_node_edge_point_spec().getMc_pool().getSupportable_spectrum())
                            for (Spectrum spectrum: ownedNode.getTapi_photonic_media_media_channel_node_edge_point_spec().getMc_pool().getSupportable_spectrum()) {
                                System.out.println(" -- upper-frequency: " + spectrum.getUpper_frequency());
                                System.out.println(" -- lower-frequency: " + spectrum.getLower_frequency());
                                System.out.println(" --- frequency-constraint --- ");
                                System.out.println("  -- adjustment-granularity: " + spectrum.getFrequency_constraint().getAdjustment_granularity());
                                System.out.println("  -- grid-type: " + spectrum.getFrequency_constraint().getGrid_type());
                            }
                        if (null != ownedNode.getTapi_photonic_media_media_channel_node_edge_point_spec().getMc_pool().getAvailable_spectrum())
                            for (Spectrum available: ownedNode.getTapi_photonic_media_media_channel_node_edge_point_spec().getMc_pool().getAvailable_spectrum()) {
                                System.out.println(" -- upper-frequency: " + available.getUpper_frequency());
                                System.out.println(" -- lower-frequency: " + available.getLower_frequency());
                                System.out.println(" --- frequency-constraint --- ");
                                System.out.println("  -- adjustment-granularity: " + available.getFrequency_constraint().getAdjustment_granularity());
                                System.out.println("  -- grid-type: " + available.getFrequency_constraint().getGrid_type());

                            }
                    }

                }
            }
            for (Name name: topology.getName()) {
                System.out.println("  --- Name ---  ");
                System.out.println("    -- value-name: " + name.getValue_name());
                System.out.println("    -- value: " + name.getValue());
            }
            for (Link link: topology.getLink()) {
                System.out.println("    --- link ---    ");
                System.out.println("      -- uuid: " + link.getUuid());
                if (null != link.getNode_edge_point()) {
                    for (NodeEdge nodeEdge: link.getNode_edge_point()) {
                        System.out.println("       --- node-edge-point ---          ");
                        System.out.println("         -- topology-uuid: " + nodeEdge.getTopology_uuid());
                        System.out.println("         -- node-uuid: " + nodeEdge.getNode_uuid());
                        System.out.println("         -- node-edge-point-uuid: " + nodeEdge.getNode_edge_point_uuid());
                    }
                }
                System.out.println("      -- lifecycle-state: " + link.getLifecycle_state());
                for (Name name: link.getName()) {
                    System.out.println("  --- Name ---  ");
                    System.out.println("    -- value-name: " + name.getValue_name());
                    System.out.println("    -- value: " + name.getValue());
                }
                System.out.println("      -- operational-state: " + link.getOperational_state());
                System.out.println("      -- administrative-state: " + link.getAdministrative_state());
                System.out.println("      -- direction: " + link.getDirection());
                System.out.println("   --- layer-protocol-name ---  ");
                for (String layerprotocol: link.getLayer_protocol_name()) {
                    System.out.println("        --- " + layerprotocol);
                }
            }
            System.out.println("   --- layer-protocol-name ---  ");
            for (String layerprotocol: topology.getLayer_protocol_name()) {
                System.out.println("        --- " + layerprotocol);
            }
        }

    }
    private void imprimirTapiStreaming(TapiStreaming tapiStreaming) {
        if (null != tapiStreaming) {
            for (AvailableStream availableStream: tapiStreaming.getAvailable_stream()) {
                System.out.println(" --- uuid: " + availableStream.getUuid());
                System.out.println(" ---- supported-stream-type --- ");
                System.out.println(" -- supported-stream-type-uuid: " + availableStream.getSupported_stream_type().getSupported_stream_type_uuid());
                System.out.println(" --- stream-state: " + availableStream.getStream_state());
                System.out.println(" --- stream-id: " + availableStream.getStream_id());
                System.out.println(" --- connection-protocol: " + availableStream.getConnection_protocol());
                System.out.println(" --- connection-address: " + availableStream.getConnection_address());
                for (Name name: availableStream.getName()) {
                    System.out.println(" -- Name -- ");
                    System.out.println(" --- value-name: " + name.getValue_name());
                    System.out.println(" --- value: " + name.getValue());
                }
            }
        }
        if (null != tapiStreaming) {
            for (StreamType streamType: tapiStreaming.getSupported_stream_type()) {
                System.out.println(" --- uuid: " + streamType.getUudi());
                System.out.println(" --- record-retention: " + streamType.getRecord_retention());
                System.out.println(" --- stream-type-name: " + streamType.getStream_type_name());
                System.out.println(" --- log-storage-strategy: " + streamType.getLog_storage_strategy());
                System.out.println(" --- segment-size: " + streamType.getSegment_size());
                System.out.println(" --- log-record-strategy: " + streamType.getLog_record_strategy());
                System.out.println("   --- record-content -- ");
                for (String record: streamType.getRecord_content()) {
                    System.out.println("        --- " + record);
                }
                for (Name name: streamType.getName()) {
                    System.out.println(" -- Name -- ");
                    System.out.println(" --- value-name: " + name.getValue_name());
                    System.out.println(" --- value: " + name.getValue());
                }
                System.out.println("   --- connection-protocol-details --- ");
                for (String allowed: streamType.getConnection_protocol_details().getAllowed_connection_protocols()) {
                    System.out.println("        --- " + allowed);
                }
            }
        }
    }
    private void imprimirName(List < Name > parametroName) {
        for (Name name: parametroName) {
            System.out.println(" --- Name --- ");
            System.out.println("  -- value-name: " + name.getValue_name());
            System.out.println("  -- value: " + name.getValue());

        }
    }

    private void insertarAccessPort(TapiEquipment accessport) {
        for (Device device: accessport.getDevice()) {
            DBRecord rec = tablaAccessport.newRecord();
            if (null != device.getAccess_port()) {
                for (AccessPort accessportcontext: device.getAccess_port()) {
                    rec = tablaAccessport.newRecord();
                    rec.addField("uuid_device", device.getUuid());
                    if (null != accessportcontext.getUuid()) {
                        rec.addField("uuid", accessportcontext.getUuid().toString());
                    }
                    if (null != accessportcontext.getConnector_pin()) {
                        String result = accessportcontext.getConnector_pin().stream()
                                .map(n -> String.valueOf(n))
                                .collect(Collectors.joining("-", "{", "}"));
                        rec.addField("connector_pin_uuid", result);
                    }
                    if (null != accessportcontext.getName()) {
                        String nameStringValue = Util.generarNames(accessportcontext.getName());
                        System.out.println("nameStringValue: " + nameStringValue);
                        rec.addField("name", nameStringValue);
                    }
                    try {
                        tablaAccessport.insert(rec);
                    } catch (SQLException e) {
                        System.out.println("Ex: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void insertarEquipment(TapiEquipment equipment) {
        for (Device device: equipment.getDevice()) {
            DBRecord rec = tablaEquipment.newRecord();
            for (Equipment equipmentcontext: device.getEquipment()) {
                rec = tablaEquipment.newRecord();
                rec.addField("uuid_device", device.getUuid());
                if (null != equipmentcontext.getUuid()) {
                    rec.addField("uuid", equipmentcontext.getUuid().toString());
                }
                if (null != equipmentcontext.getName()) {
                    String nameStringValue = Util.generarNames(equipmentcontext.getName());
                    System.out.println("nameStringValue: " + nameStringValue);
                    rec.addField("name", nameStringValue);
                }
                if (null != equipmentcontext.getExpected_equipment()) {
                    // LISTA TOSTRING
                    String result = equipmentcontext.getExpected_equipment().stream()
                            .map(n -> String.valueOf(n))
                            .collect(Collectors.joining("-", "{", "}"));

                    rec.addField("expected_equipment", result);
                }
                if (null != equipmentcontext.getActual_equipment()) {
                    rec.addField("actual_equipment", equipmentcontext.getActual_equipment().toString());
                }
                if (null != equipmentcontext.getContained_holder()) {
                    rec.addField("containedHolder", equipmentcontext.getContained_holder().toString());
                }
                if (null != equipmentcontext.getGeographical_location()) {
                    rec.addField("geographicalLocation", equipmentcontext.getGeographical_location());
                }
                if (null != equipmentcontext.getEquipment_location()) {
                    rec.addField("equipmentLocation", equipmentcontext.getEquipment_location());
                }
                if (null != equipmentcontext.getCategory()) {
                    rec.addField("category", equipmentcontext.getCategory());
                }
                rec.addField("is_expected_actual_mismatch", equipmentcontext.isIs_expected_actual_mismatch());
                try {
                    tablaEquipment.insert(rec);
                } catch (SQLException e) {
                    System.out.println("Ex: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    private void insertarDevice(TapiEquipment devicecontext) {
        for (Device device: devicecontext.getDevice()) {
            DBRecord rec = tablaDevice.newRecord();
            String nameStringValue = Util.generarNames(device.getName());
            System.out.println("nameStringValue: " + nameStringValue);
            rec.addField("name", nameStringValue);
            rec.addField("uuid", device.getUuid());
            rec.addField("uuid_physical_context", devicecontext.getUuid());
      /*int cantidadElementos = device.getEquipment().size();
      if(null != device.getAccess_port() && cantidadElementos < device.getAccess_port().size()){
          cantidadElementos = device.getAccess_port().size();
      }
      for(int i = 0 ;i < cantidadElementos; i ++ ){
          rec.addField("uuid", device.getUuid()); // "uuid": "d0710cf8-ac19-48eb-bc00-3b7499bd13bb",
          if(null != device.getEquipment() && i < device.getEquipment().size()) {
              rec.addField("equipment_uuid", device.getEquipment().get(i).getUuid()); //  "uuid": "7a882cb1-33ff-32a2-ac5a-88803459457c",
          }else{
              rec.addField("equipment_uuid", ""); //  "uuid": "7a882cb1-33ff-32a2-ac5a-88803459457c",
          }
          if(null != device.getAccess_port() && i < device.getAccess_port().size()){
              rec.addField("access_port_uuid", device.getAccess_port().get(i).getUuid()); //  "uuid": "7a882cb1-33ff-32a2-ac5a-88803459457c",
          }else{
              rec.addField("access_port_uuid", ""); //  "uuid": "7a882cb1-33ff-32a2-ac5a-88803459457c",

          }*/
            try {
                tablaDevice.insert(rec);
                rec = tablaDevice.newRecord();
            } catch (SQLException e) {
                System.out.println("Ex: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void insertarPhysicalContext(TapiEquipment physicalcontext) {
        DBRecord rec = tablaPhysicalcontext.newRecord();
        rec.addField("uuid", physicalcontext.getUuid());
        String nameStringValue = Util.generarNames(physicalcontext.getName());
        System.out.println("nameStringValue: " + nameStringValue);
        rec.addField("name", nameStringValue);
        try {
            tablaPhysicalcontext.insert(rec);
        } catch (SQLException e) {
            System.out.println("Ex: " + e.getMessage());
            e.printStackTrace();
        }

    /*for (Device device : physicalcontext.getDevice()){
        DBRecord rec = tablaPhysicalcontext.newRecord();

        rec.addField("uuid", physicalcontext.getUuid().toString());
        rec.addField("device_uuid", device.getUuid());

        String nameStringValue = generarNames(device.getName());
        System.out.println("nameStringValue: " + nameStringValue);
        rec.addField("name", nameStringValue);
        rec.addField("physical_span_uuid", "");
        try {
            tablaPhysicalcontext.insert(rec);
        } catch (SQLException e) {
            System.out.println("Ex: " + e.getMessage());
            e.printStackTrace();
        }
    }*/

    }


  /*
            for (Equipment equipment : device.getEquipment()){
                System.out.println(" -- uuid: " + equipment.getUuid());
                for (Name name : equipment.getName()){
                    System.out.println(" -- value-name: " + name.getValue_name());
                    System.out.println(" -- value: " + name.getValue());
                }
                System.out.println(" -- expected-equipment -- ");
                for (ExpectedEquipment expectedEquipment : equipment.getExpected_equipment()){
                    System.out.println(" --common-equipment --");
                    System.out.println(" -- equipment-type: " + expectedEquipment.getCommon_equipment_properties().getEquipment_type_version());
                    System.out.println(" -- manufacturer-name: " + expectedEquipment.getCommon_equipment_properties().getManufacturer_name());
                    System.out.println(" -- equipment-type-name: " + expectedEquipment.getCommon_equipment_properties().getEquipment_type_name());
                }
                System.out.println(" -- Actual-Equipment -- ");
                System.out.println("    -- common-equipment -- ");
                System.out.println("      -- equipment-type: " + equipment.getActual_equipment().getCommon_equipment_properties().getEquipment_type_version());
                System.out.println("      -- manufacturer: " + equipment.getActual_equipment().getCommon_equipment_properties().getManufacturer_name());
                System.out.println("      -- equipment-name: " + equipment.getActual_equipment().getCommon_equipment_properties().getEquipment_type_name());
                System.out.println(" -- category: " + equipment.getCategory());
                System.out.println(" -- is-expected: " + equipment.isIs_expected_actual_mismatch());

            }
            for (Name name : device.getName()){
                System.out.println(" -- Name --");
                System.out.println("  -- value-name: " + name.getValue_name());
                System.out.println("  -- value: " + name.getValue());
            }
        }
        System.out.println(" -- uuid: " + tapiEquipment.getUuid());
        for (Name name : tapiEquipment.getName()){
            System.out.println(" -- Name --");
            System.out.println("   -- value-name: " + name.getValue_name());
            System.out.println("   -- value: " + name.getValue());
        }*/

    // SE CREA UN METODO QUE ME IMPRIMIRA TODO LO QUE TIENE UNA LISTA DE ServiceInterface, ASI COMO ESTE, DEBES CREAR UN METODO POR CADA CLASE
    // PARA VALIDAR TODO EL ARCHIVO
    private void imprimirServiceInterface(List < ServiceInterface > parametroServiceInterface) {
        // COMO ES UNA LISTA,  NECESITAMOS RECORRERLO, PARA ELLO USAMOS UN FOR
        // EL FOR SE COMPONE DE 2 ELEMENTOS EL PRIMERO (ServiceInterface service :) ES LA CLASE QUE  POR CADA ITERASION ME ALMACENA LOS DATOS
        // EL SEGUNDO ES LA LISTA QUE VOY A RECORRER, QUE EN ESTE CASO ES EL PARAMETRO parametroServiceInterface
        for (ServiceInterface service: parametroServiceInterface) {
            // PINTAMOS TODAS LAS PROPIEDADES QUE TIENE MI CLASE, A MODO DE EJEMPLO PINTE EL PRIMERO, SERIA IDEA PINTES TODOS
            System.out.println("   --- uuid: " + service.getUuid() + "");
            System.out.println("   --- Supported_layer_protocol_qualifier -- ");
            for (String dato: service.getSupported_layer_protocol_qualifier()) {
                System.out.println("        --- " + dato);
            }
            System.out.println("   --- LifecycleState: " + service.getLifecycle_state() + "");
            System.out.println("   --- total-potential-capacity -- ");
            System.out.println("     --- total-size:  ");
            System.out.println("       --- unit: " + service.getTotal_potential_capacity().getTotal_size().getUnit());
            System.out.println("       --- value: " + service.getTotal_potential_capacity().getTotal_size().getValue());
            System.out.println("   ---LayerProtocol: " + service.getLayer_protocol_name());
            System.out.println("   ---AdministrativeState: " + service.getAdministrative_state());
            System.out.println("   --- available-capacity -- ");
            if (null != service.getAvailable_capacity()) {
                System.out.println("     --- total-size:  ");
                System.out.println("       --- unit: " + service.getAvailable_capacity().getTotal_size().getUnit());
                System.out.println("       --- value: " + service.getAvailable_capacity().getTotal_size().getValue());
            }
            System.out.println("   --- direction: " + service.getDirection());
            System.out.println("   --- operational-state: " + service.getOperational_state());
            System.out.println("   --- Name --- ");
            for (Name name: service.getName()) {
                System.out.println("    -- value-name: " + name.getValue_name());
                System.out.println("    -- value: " + name.getValue());
            }
            System.out.println("    --- tapi-photonic-media:otsi-service-interface-point-spec ---     ");
            System.out.println("        --- otsi-capability ---         ");
            System.out.println("           --- supportable-central-frequency-spectrum-band --            ");
            if (null != service.getTapi_photonic_media_otsi_service_interface_point_spec()) {
                for (SupportableCentral supportableCentral: service.getTapi_photonic_media_otsi_service_interface_point_spec().getOtsi_capability().getSupportable_central_frequency_spectrum_band()) {
                    System.out.println("           -- lower-central-frequency: " + supportableCentral.getLower_central_frequency());
                    System.out.println("           -- upper-central-frequency: " + supportableCentral.getUpper_central_frequency());
                    System.out.println("       --- frequency-constraint --- ");
                    System.out.println("          -- adjustment-granularity: " + supportableCentral.getFrequency_constraint().getAdjustment_granularity());
                    System.out.println("          -- grid-type: " + supportableCentral.getFrequency_constraint().getGrid_type());
                }
            }
        }
    }
}