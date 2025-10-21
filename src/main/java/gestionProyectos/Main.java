package gestionProyectos;

import gestionProyectos.dataAccesLayer.ProyectFileStore;
import gestionProyectos.dataAccesLayer.UserFileStore;
import gestionProyectos.domainLayer.User;
import gestionProyectos.presentationLayer.Controllers.ProyectController;
import gestionProyectos.presentationLayer.Controllers.WorkController;
import gestionProyectos.presentationLayer.Models.ProyectTableModel;
import gestionProyectos.presentationLayer.Models.WorkTableModel;
import gestionProyectos.presentationLayer.Views.TableroView;
import gestionProyectos.serviceLayer.ProyectService;
import gestionProyectos.serviceLayer.UserService;
import gestionProyectos.serviceLayer.WorkService;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                File usersFile = new File("data.xml");
                File proyectsFile = new File("proyects.xml");

                UserService userService = new UserService(new UserFileStore(usersFile));
                ProyectService proyectService = new ProyectService(new ProyectFileStore(proyectsFile));
                WorkService workService = new WorkService(proyectService);

                ProyectController proyectController = new ProyectController(proyectService);
                WorkController workController = new WorkController(workService);

                ProyectTableModel proyectTableModel = new ProyectTableModel();
                WorkTableModel workTableModel = new WorkTableModel();

                proyectService.addObserver(proyectTableModel);
                workService.addObserver(workTableModel);

                List<User> users = userService.leerTodos();

                TableroView view = new TableroView(
                        proyectController,
                        workController,
                        proyectTableModel,
                        workTableModel,
                        users
                );

                view.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error iniciando la aplicación: " + e.getMessage());
            }
        });
    }
}