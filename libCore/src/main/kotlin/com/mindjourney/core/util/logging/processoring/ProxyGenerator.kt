package com.mindjourney.core.util.logging.processoring

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File

object ProxyGenerator {
    fun generateProxies(methodsByClass: Map<KSClassDeclaration, List<KSFunctionDeclaration>>) {
        val packageName = "generated"

        methodsByClass.forEach { (classDecl, methods) ->
            val className = classDecl.simpleName.asString()

            val loggingWrapperFile = FileSpec.builder(packageName, "${className}LoggingProxy")
                .addType(
                    TypeSpec.classBuilder("${className}LoggingProxy")
                        .addSuperinterface(ClassName.bestGuess(LoggingConstants.LOGGING_WRAPPER))
                        .primaryConstructor(
                            FunSpec.constructorBuilder()
                                .addParameter("delegate", ClassName("", className))
                                .build()
                        )
                        .addProperty(
                            PropertySpec.builder("delegate", ClassName("", className))
                                .initializer("delegate")
                                .build()
                        )
                        .apply {
                            methods.forEach { method ->
                                addFunction(
                                    FunSpec.builder(method.simpleName.asString())
                                        .addStatement("println(\"MJ: Logging: ${method.simpleName.asString()}\")")
                                        .addStatement("delegate.${method.simpleName.asString()}()")
                                        .build()
                                )
                            }
                        }
                        .build()
                )
                .build()

            val outputDir = File("generated/")
            outputDir.mkdirs()
            loggingWrapperFile.writeTo(outputDir)
        }
    }
}
