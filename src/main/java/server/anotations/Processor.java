package server.anotations;

import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("server.anotations.WithRoles")
public class Processor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        Map<Boolean, List<Element>> annotatedMethods = annotations.stream().collect(
                Collectors.partitioningBy(element ->
                        ((ExecutableType) element.asType()).getParameterTypes().size() == 1
                                && element.getSimpleName().toString().startsWith("set")));

        List<Element> setters = annotatedMethods.get(true);
        List<Element> otherMethods = annotatedMethods.get(false);

        System.out.println("Processor!!!!!!!!!!");
        System.out.println(setters);
        System.out.println(otherMethods);

        return false;
    }
}
