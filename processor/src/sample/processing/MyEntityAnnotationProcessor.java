package sample.processing;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

import javax.annotation.Generated;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import com.google.auto.common.MoreElements;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

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
        TypeElement typeElement = MoreElements.asType(element);
        PackageElement packageElement = MoreElements.getPackage(element);
        String name = element.getSimpleName().toString() + "Finder";
        ClassName generatedClassName = ClassName.get(packageElement.getQualifiedName().toString(), name);

        TypeSpec finder = TypeSpec.classBuilder(name)
                // class
                .addAnnotation(AnnotationSpec.builder(Generated.class)
                        .addMember("value", "$S", getClass().getCanonicalName())
                        .build())
                .addModifiers(Modifier.PUBLIC)
                // static method
                .addMethod(MethodSpec.methodBuilder("newInstance")
                        .addModifiers(Modifier.PUBLIC)
                        .addModifiers(Modifier.STATIC)
                        .returns(generatedClassName)
                        .addCode(CodeBlock.builder()
                                .add("return new $T();\n", generatedClassName)
                                .build())
                        .build())
                // instance method
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PRIVATE)
                        .build())
                .addMethod(MethodSpec.methodBuilder("find")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(TypeName.get(typeElement.asType()))
                        .addCode(CodeBlock.builder()
                                .add("return new $T();\n", TypeName.get(typeElement.asType()))
                                .build())
                        .build())
                .build();
                
        JavaFile javaFile =
                JavaFile.builder(packageElement.getQualifiedName().toString(), finder).build();
        try {
            JavaFileObject source = processingEnv.getFiler()
                    .createSourceFile(packageElement.getQualifiedName().toString() + "." + name);
            try (Writer w = source.openWriter()) {
                javaFile.writeTo(w);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    protected void processElement(Element element) {
//        Messager messager = processingEnv.getMessager();
//        messager.printMessage(Kind.NOTE, element.toString());
//        if (element.getKind() == ElementKind.CLASS) {
//            messager.printMessage(Kind.NOTE, "classです");
//            TypeElement classElement = (TypeElement) element;
//            PackageElement packageElement = (PackageElement) classElement.getEnclosingElement();
//            
//            // 新しいソースファイルを生成
//            try {
//                JavaFileObject fo = processingEnv.getFiler().createSourceFile(classElement.getQualifiedName() + "Finder");
//                try (PrintWriter w = new PrintWriter(fo.openWriter())) {
//                    String entity = classElement.getSimpleName().toString();
//                    w.println("package " + packageElement.getQualifiedName() + ";");
//                    w.println("");
//                    w.println("import javax.annotation.Generated;");
//                    w.println("import " + classElement.getQualifiedName() + ";");
//                    w.println("");
//                    w.println("@Generated(\"" + getClass().getCanonicalName() + "\")");
//                    w.println("public class " + classElement.getSimpleName() + "Finder {");
//                    w.println("    public " + entity + " find" + classElement.getSimpleName() + "() {");
//                    w.println("        //TODO 実際はまともな処理");
//                    w.println("        return new " + entity + "();");
//                    w.println("    }");
//                    w.println("}");
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            messager.printMessage(Kind.NOTE, "classじゃありません " + element);
//        }
//    }
}
