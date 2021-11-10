package server.services;

import server.services.interfaces.IAuthService;
import shared.dto.Roles;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class RolesService implements IAuthService {

    private String path = "";
    private static List<Vector> rolesList;
    private static List<Vector> mapping;
    private static String[] actions;


    public RolesService(String path) {
        this.path = path;
    }

    @Override
    public void load(){
        loadUserRoles();
        loadMappings();
    }

    private void loadUserRoles(){
        rolesList = new ArrayList<>();
        try {
            File myObj = new File(path + "roles.csv");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] parts = data.split(";");
                String un = parts[1];
                String ro = parts[0];

                Roles r = null;
                try {
                    r = StringToRole(ro);
                    Vector v = new Vector();
                    v.add(un);
                    v.add(ro);
                    rolesList.add(v);
                } catch (Exception e) {
                    System.out.println("role [" + r + "] was not recognized");
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void loadMappings(){
        mapping = new ArrayList<>();
        try {
            boolean first = true;
            File myObj = new File(path + "rolesActions.csv");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] parts = data.split(";");
                if(first){
                    actions = parts;
                    first = false;
                }else {
                    String role = null;
                    for (int i = 0; i < actions.length; i++) {
                        if(i == 0){
                            role = parts[i];
                        }else {
                            boolean haveAccess = parts[i].equals("1");
                            if(haveAccess){
                                String action = actions[i];
                                Vector v = new Vector();
                                v.add(role);
                                v.add(action);
                                mapping.add(v);
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
       List<String> userRoles = rolesList
               .stream()
               .filter(rp -> rp.get(0).toString().equalsIgnoreCase(username.toLowerCase()))
               .map(x -> x.get(1).toString())
               .collect(Collectors.toList());

       List<String> allowedRoles = mapping.stream()
               .filter(m -> m.get(1).toString().equalsIgnoreCase(action.toLowerCase()))
               .map(x -> x.get(0).toString())
               .collect(Collectors.toList());

       Set<String> result = allowedRoles.stream()
               .filter(v -> contains(userRoles, v))
               .collect(Collectors.toSet());

       return true;
    }

    private boolean contains(List<String> list, String match){
        return list.stream().anyMatch(x -> {

            byte[] bytes = x.getBytes(StandardCharsets.UTF_8);
            byte[] bytes2 = match.getBytes(StandardCharsets.UTF_8);
            String utf8EncodedString = new String(bytes, StandardCharsets.UTF_8);
            String utf8EncodedString2 = new String(bytes2, StandardCharsets.UTF_8);

            return utf8EncodedString2.equalsIgnoreCase(utf8EncodedString);
        });
    }

    public Roles StringToRole(String r) {
        String s = r.toLowerCase();
        if(s.equals("admin")) {
            return Roles.ADMIN;
        }else if(s.equals("technician")){
            return Roles.TECHNICIAN;
        } else if(r.equals("poweruser")) {
            return Roles.POWER_USER;
        }else{
            return Roles.BASIC;
        }
    }
}
