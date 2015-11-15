package sample.processing;

import java.io.IOException;
import java.io.PrintWriter;
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
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes("sample.annotation.MyEntity")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MyEntityAnnotationProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        annotations.forEach(annotation -> {
            roundEnv.getElementsAnnotatedWith(annotation).forEach(this::processElement);
        });
        return true;
    }

    protected void processElement(Element element) {
        Messager messager = processingEnv.getMessager();
        messager.printMessage(Kind.NOTE, element.toString());
        if (element.getKind() == ElementKind.CLASS) {
            messager.printMessage(Kind.NOTE, "classです");
            TypeElement classElement = (TypeElement) element;
            PackageElement packageElement = (PackageElement) classElement.getEnclosingElement();
            
            // 新しいソースファイルを生成
            try {
                JavaFileObject fo = processingEnv.getFiler().createSourceFile(classElement.getQualifiedName() + "Finder");
                try (PrintWriter w = new PrintWriter(fo.openWriter())) {
                    String entity = classElement.getSimpleName().toString();
                    w.println("package " + packageElement.getQualifiedName() + ";");
                    w.println("");
                    w.println("import javax.annotation.Generated;");
                    w.println("import " + classElement.getQualifiedName() + ";");
                    w.println("");
                    w.println("@Generated(\"" + getClass().getCanonicalName() + "\")");
                    w.println("public class " + classElement.getSimpleName() + "Finder {");
                    w.println("    public " + entity + " find" + classElement.getSimpleName() + "() {");
                    w.println("        //TODO 実際はまともな処理");
                    w.println("        return new " + entity + "();");
                    w.println("    }");
                    w.println("}");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            messager.printMessage(Kind.NOTE, "classじゃありません " + element);
        }
    }
}
