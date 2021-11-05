package server.services;

import server.services.interfaces.IAuthService;
import shared.dto.Roles;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class PolicyService implements IAuthService {

    private String path = "";
    private static String[] actions;
    private static List<Vector> userPolicies;

    public PolicyService(String path) {
        this.path = path;
        userPolicies = new ArrayList<>();
    }

    @Override
    public void load(){
        loadPolicies();

        for (Vector v: userPolicies) {
            System.out.println(v.get(0) + " : " + v.get(1));
        }
    }

    private void loadPolicies(){
        userPolicies = new ArrayList<>();
        try {
            boolean first = true;
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] parts = data.split(";");
                if(first){
                    actions = parts;
                    first = false;
                }else {
                    String user = null;
                    for (int i = 0; i < actions.length; i++) {
                        if(i == 0){
                            user = parts[i];
                        }else {
                            boolean haveAccess = parts[i].equals("1");
                            if(haveAccess){
                                String action = actions[i];
                                Vector v = new Vector();
                                v.add(user);
                                v.add(action);
                                userPolicies.add(v);
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
        return userPolicies.stream()
                .anyMatch(p -> ((String )p.get(0)).equalsIgnoreCase(username)
                               && ((String )p.get(1)).equalsIgnoreCase(action));
    }

/*    public void haveAllRolesThrow(String username, Roles[] roles) throws Unauthorized {
        if(!haveAllRoles(username, roles)){
            throw new Unauthorized("User does not have all thise roles: " + roles.toString());
        }
    }

    public void haveSomeRolesThrow(String username, Roles[] roles) throws Unauthorized {
        if(!haveSomeRoles(username, roles)){
            throw new Unauthorized("User does not have any of thise roles: " + roles.toString());
        }
    }

    public void haveRoleThrow(String username, Roles role) throws Unauthorized {
        if(!haveRole(username, role)){
            throw new Unauthorized("User does not have the role: " + role.toString());
        }
    }

    public boolean haveAllRoles(String username, Roles[] roles){
        return policyList.stream().allMatch(x -> haveRole(username, x.role));
    }

    public boolean haveSomeRoles(String username, Roles[] roles){
        return policyList.stream().anyMatch(x -> haveRole(username, x.role));
    }

    public boolean haveRole(String username, Roles role){
        for (Policy p: policyList) {
            if(p.username.equals(username) && p.role.equals(role)){
                return true;
            }
        }

        return false;
    }*/

    public Roles StringToRole(String r) throws Exception {
        switch (r){
            case "admin" -> {
                return Roles.ADMIN;
            }
            case "technician" -> {
                return Roles.TECHNICIAN;
            }
            case "power_user" -> {
                return Roles.POWER_USER;
            }
            case "basic" -> {
                return Roles.BASIC;
            }
            default -> {
                throw new Exception("String is not a role.");
            }
        }
    }
}
