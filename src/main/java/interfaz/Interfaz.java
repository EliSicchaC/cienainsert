package interfaz;

import com.ciena.controller.*;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
        final JButton btn1 = new JButton("Upload");
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

        final JLabel analyzing = new JLabel("Processing");
        analyzing.setBounds(60, 100, 150, 25);
        analyzing.setFont(font);
        analyzing.setVisible(false);
        frame.add(analyzing);

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
                final JFileChooser fileSelector = new JFileChooser();
                fileSelector.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                final int returnVal = fileSelector.showOpenDialog(btn1);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    final File file = fileSelector.getSelectedFile();
                    path = file.getAbsolutePath();
                    textField.setText(path);

                    if (path != "") {
                        analyzing.setVisible(true);

                        TopologyMain topologyMain = null;
                        LinkMain linkmain = null;
                        NodeMain nodeMain =null;
                        OwnedNodePoint ownedNodePoint = null;

                        PhysicalContextMain physicalContextMain = null;
                        DeviceMain deviceMain = null;
                        EquipmentMain equipmentMain = null;
                        AccessPortMain accessPortMain = null;
                        try {
                            linkmain = new LinkMain();
                            ownedNodePoint = new OwnedNodePoint();
                            nodeMain = new NodeMain();
                            topologyMain = new TopologyMain();

                            accessPortMain = new AccessPortMain();
                            equipmentMain = new EquipmentMain();
                            deviceMain = new DeviceMain();
                            physicalContextMain = new PhysicalContextMain();

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                        }

                        Boolean topology = topologyMain.analizarInformacionTopoloy(path,"tapi-common:context",
                                "tapi-topology:topology-context","topology");
                        Boolean node = nodeMain.analizarInformacionNode(path,"tapi-common:context",
                                "tapi-topology:topology-context","topology","node");
                        Boolean owned = ownedNodePoint.analizarInformacionOwned(path, "tapi-common:context",
                                "tapi-topology:topology-context", "topology", "node", "owned-node-edge-point");
                        Boolean link = linkmain.analyzeInformationLink(path, "tapi-common:context",
                                "tapi-topology:topology-context", "topology", "link","node-edge-point");

                        Boolean physical = physicalContextMain.analizarInformacionPhysicalContext(path,"tapi-common:context",
                                "tapi-equipment:physical-context");
                        Boolean device = deviceMain.analyzeInformationDevice(path,"tapi-common:context",
                                "tapi-equipment:physical-context","device");
                        Boolean equipment = equipmentMain.analyzeInformationEquipment(path,"tapi-common:context",
                                "tapi-equipment:physical-context","device","equipment");
                        Boolean accessport = accessPortMain.analyzeInformationAccessPort(path,"tapi-common:context",
                                "tapi-equipment:physical-context","device","access-port");

                        if (topology && link && node && owned && physical && device && equipment && accessport) {
                            analyzing.setText("Successful Process!");
                        }else{
                            analyzing.setText("Failed Process!");
                        }
                    }
                }
            }
        });
    }

    public void addInfo(final String string) {
        final JFrame f = new JFrame("Fast");
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