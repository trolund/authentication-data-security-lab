package server.services;

import server.services.interfaces.IAuthService;
import shared.Colors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class RBACService implements IAuthService {

    private final String path;

    private static HashMap<String, List<String>> userRoles; // username -> roles
    private static HashMap<String, List<String>> capabilityRoles; // capability -> roles
    private static String[] actions;

    /**
     * @param path Path to where the roles.csv and rolesActions.csv is located
     */
    public RBACService(String path) {
        this.path = path;
    }

    @Override
    public void load() {
        userRoles = new HashMap<>();
        capabilityRoles = new HashMap<>();

        loadUserRoles();
        loadRoleCapabilities();

        System.out.println(Colors.ANSI_GREEN + "Loaded roles from file" + Colors.ANSI_RESET);
    }

    private void loadUserRoles() {
        try {
            File myObj = new File(path + "roles.csv");
            Scanner myReader = new Scanner(myObj, StandardCharsets.UTF_8);

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();

                String[] parts = data.split(";");
                String role = parts[0];
                String userName = parts[1];

                if (userRoles.containsKey(userName)) {
                    var roles = userRoles.get(userName);
                    roles.add(role);
                    userRoles.replace(userName, roles);
                } else {
                    var roles = new ArrayList<String>();
                    roles.add(role);
                    userRoles.put(userName, roles);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadRoleCapabilities() {
        try {
            boolean first = true;
            File myObj = new File(path + "rolesActions.csv");
            Scanner myReader = new Scanner(myObj, StandardCharsets.UTF_8);

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] parts = data.split(";");
                if (first) {
                    actions = parts;
                    first = false;
                } else {
                    String role = null;
                    for (int i = 0; i < actions.length; i++) {
                        if (i == 0) {
                            role = parts[i];
                        } else {
                            boolean haveAccess = parts[i].equals("1");
                            if (haveAccess) {
                                if (capabilityRoles.containsKey(actions[i])) {
                                    var roles = capabilityRoles.get(actions[i]);
                                    roles.add(role);
                                    capabilityRoles.replace(actions[i], roles);
                                } else {
                                    var roles = new ArrayList<String>();
                                    roles.add(role);
                                    capabilityRoles.put(actions[i], roles);
                                }
                            }
                        }
                    }
                }
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean haveAccess(String username, String action) {
        var userRole = userRoles.get(username);
        var role = capabilityRoles.get(action);

        if((userRole == null) || (role == null)){
            return false;
        }

        for (String r : userRole) {
            if (role.contains(r)) {
                return true;
            }
        }

        return false;
    }
}
