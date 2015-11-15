package sample.processing;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

@SupportedAnnotationTypes("sample.annotation.MyService")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MyServiceAnnotationProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        annotations.forEach(annotation -> {
            roundEnv.getElementsAnnotatedWith(annotation).forEach(this::processElement);
        });
        return true;
    }

    protected void processElement(Element element) {
        if (element.getKind() == ElementKind.CLASS) {
            Messager messager = processingEnv.getMessager();
            if (element.getSimpleName().toString().endsWith("ServiceImpl") == false) {
                messager.printMessage(Kind.ERROR,
                        "クラス名は 'ServiceImpl' で終える必要があります", element);
            }
//
//            TypeElement classElement = (TypeElement) element;
//            PackageElement packageElement = (PackageElement) classElement.getEnclosingElement();
//            if (packageElement.getSimpleName().toString().equals("service") == false) {
//                messager.printMessage(Kind.ERROR,
//                        "serviceパッケージに配置してください", packageElement);
//            }
        }
    }
}
