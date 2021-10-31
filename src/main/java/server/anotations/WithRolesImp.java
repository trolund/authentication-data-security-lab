package server.anotations;

import java.lang.annotation.Annotation;

public class WithRolesImp implements WithRoles {

    @Override
    public String[] roles() {
        System.out.println("ano");
        return new String[0];
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        System.out.println("ano");
        return null;
    }
}
