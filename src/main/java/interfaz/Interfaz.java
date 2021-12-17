package interfaz;

import com.ciena.controller.*;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Interfaz extends JFrame {

    private static final long serialVersionUID = 1L;
    static String path = null;

    @SuppressWarnings("deprecation")
    public static void main(final String[] args) {

        // dibuja la ventana
        final JFrame frame = new Interfaz();
        frame.setLayout(null);
        frame.setTitle("myFrame");
        frame.setSize(400, 170);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // botón agregar
        final JButton btn1 = new JButton("Subir");
        btn1.setBounds(150, 70, 120, 25);
        btn1.setVisible(true);
        frame.add(btn1);

        // establecer fuente
        final Font font = new Font("Song Ti", Font.BOLD, 11);
        btn1.setFont(font);

        // Agregar cuadro de presentación
        final JLabel label = new JLabel("File:");
        label.setBounds(60, 20, 50, 25);
        label.setVisible(true);
        label.setFont(font);
        frame.add(label);

        final JLabel analizando = new JLabel("Procesando");
        analizando.setBounds(60, 100, 150, 25);
        analizando.setFont(font);
        analizando.setVisible(false);
        frame.add(analizando);

        // agregar cuadro de texto
        final JTextField textField = new JTextField();
        textField.setBounds(120, 20, 190, 22);
        textField.setVisible(true);
        textField.disable();
        frame.add(textField);
        frame.setVisible(true);

        // agregar evento al botón
        btn1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                final JFileChooser selectorArchivos = new JFileChooser();
                selectorArchivos.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                final int returnVal = selectorArchivos.showOpenDialog(btn1);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    final File file = selectorArchivos.getSelectedFile();
                    path = file.getAbsolutePath();
                    textField.setText(path);

                    if (path != "") {
                        analizando.setVisible(true);

                        PhysicalContextMain physicalContextMain = null;
                        DeviceMain deviceMain = null;
                        EquipmentMain equipmentMain = null;
                        AccessPortMain accessPortMain = null;

                        TopologyMain topologyMain = null;
                        LinkMain linkmain = null;
                        NodeMain nodeMain =null;
                        OwnedNodePoint ownedNodePoint = null;
                        try {
                            linkmain = new LinkMain();
                            ownedNodePoint = new OwnedNodePoint();
                            nodeMain = new NodeMain();
                            topologyMain = new TopologyMain();

                            equipmentMain = new EquipmentMain();
                            accessPortMain = new AccessPortMain();
                            deviceMain = new DeviceMain();
                            physicalContextMain = new PhysicalContextMain();

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                        }

                        Boolean physical = physicalContextMain.analizarInformacionPhysicalContext(path,"tapi-common:context",
                                "tapi-equipment:physical-context");
                        Boolean device = deviceMain.analizarInformacionDevice(path,"tapi-common:context",
                                "tapi-equipment:physical-context","device");
                        Boolean equipment = equipmentMain.analizarInformacionEquipment(path,"tapi-common:context",
                                "tapi-equipment:physical-context","device","equipment");
                        Boolean accessport = accessPortMain.analizarInformacionAccessPort(path,"tapi-common:context",
                                "tapi-equipment:physical-context","device","access-port");

                        Boolean topology = topologyMain.analizarInformacionTopoloy(path,"tapi-common:context",
                                "tapi-topology:topology-context","topology");
                        Boolean node = nodeMain.analizarInformacionNode(path,"tapi-common:context",
                                "tapi-topology:topology-context","topology","node");
                        Boolean owned = ownedNodePoint.analizarInformacionOwned(path, "tapi-common:context",
                                "tapi-topology:topology-context", "topology", "node", "owned-node-edge-point");
                        Boolean link = linkmain.analizarInformacionLink(path, "tapi-common:context",
                                "tapi-topology:topology-context", "topology", "link","node-edge-point");

                        if (topology && link && node && owned) {
                            analizando.setText("Proceso exitoso!");
                        }else{
                            analizando.setText("Proceso Fallido!");
                        }
                    }
                }
            }
        });
    }

    public void addInfo(final String string) {
        final JFrame f = new JFrame("rápido");
        f.setLayout(null);
        f.setBounds(40, 40, 300, 100);
        f.setVisible(true);
        final JLabel label = new JLabel(string);
        label.setBounds(30, 20, 250, 20);
        final Font f1 = new Font("Song Ti", Font.BOLD, 12);
        label.setFont(f1);
        f.add(label);

    }
}