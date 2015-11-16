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
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import com.google.auto.common.MoreElements;

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
        Messager messager = processingEnv.getMessager();
        if (element.getKind() == ElementKind.CLASS) {
            if (element.getSimpleName().toString().endsWith("ServiceImpl") == false) {
                messager.printMessage(Kind.ERROR,
                        "クラス名は 'ServiceImpl' で終える必要があります", element);
            }

            PackageElement packageElement = MoreElements.getPackage(element);
            if (packageElement.getQualifiedName().toString().contains("service") == false) {
                messager.printMessage(Kind.ERROR,
                        "@MyServiceを付与したクラスはserviceパッケージに配置してください", element);
            }
        } else if (element.getKind() == ElementKind.INTERFACE) {
            messager.printMessage(Kind.ERROR,
                    "@MyServiceをinterfaceに付与することはできません", element);
        }
    }
}
