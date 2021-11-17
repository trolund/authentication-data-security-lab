package server.services;

import server.services.interfaces.IAuthService;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ACLService implements IAuthService {

    private String path = "";
    private static String[] actions;
    private static HashMap<String, List<String>> userActionRights; // username -> action[]

    public ACLService(String path) {
        this.path = path;
    }

    @Override
    public void load(){
        loadPolicies();
    }

    private void loadPolicies(){
        userActionRights = new HashMap<>();
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
                    List<String> allowedActions = new ArrayList<>();
                    for (int i = 0; i < actions.length; i++) {

                        if(i == 0){
                            user = parts[i];
                        }else {
                            boolean haveAccess = parts[i].equals("1");
                            if(haveAccess){
                                allowedActions.add(actions[i]);
                            }
                        }
                    }
                    userActionRights.put(user, allowedActions);
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
        var list = userActionRights.get(username);
        if ((list != null) && (list.contains(action))){
            return true;
        } else {
            return false;
        }
    }
}
