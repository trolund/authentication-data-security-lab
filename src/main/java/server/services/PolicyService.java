package server.services;

import server.services.interfaces.IPolicyService;
import shared.dto.Policy;
import shared.dto.Roles;
import shared.exceptions.Unauthorized;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PolicyService implements IPolicyService {

    private String path = "";
    private static List<Policy> policyList;

    public PolicyService(String path) {
        this.path = path;
    }

    @Override
    public void getRoles(){
        policyList = new ArrayList<>();
        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] parts = data.split(" ");
                String un = parts[0];
                String ro = parts[1];

                Roles r = StringToRole(ro);
                if(!r.equals("none")){
                    policyList.add(new Policy(un, r));
                }else{
                    System.out.println("role [" + r + "] was not recognized");
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void haveAllRolesThrow(String username, Roles[] roles) throws Unauthorized {
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

    @Override
    public boolean haveAllRoles(String username, Roles[] roles){
        return policyList.stream().allMatch(x -> haveRole(username, x.role));
    }

    @Override
    public boolean haveSomeRoles(String username, Roles[] roles){
        return policyList.stream().anyMatch(x -> haveRole(username, x.role));
    }

    @Override
    public boolean haveRole(String username, Roles role){
        for (Policy p: policyList) {
            if(p.username.equals(username) && p.role.equals(role)){
                return true;
            }
        }

        return false;
    }

    public Roles StringToRole(String r){
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
                return Roles.NONE;
            }
        }
    }
}
