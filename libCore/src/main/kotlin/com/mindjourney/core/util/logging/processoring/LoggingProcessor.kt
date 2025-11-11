package com.mindjourney.core.util.logging.processoring

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated

/**
 * This processor generates proxy classes for classes annotated with @ClassLog or methods annotated with @MethodLog.
 * BUT it works only for classes which are not orchestrated by Hilt/Dagger.
 */
class LoggingProcessor : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        println("🚀 MJ: LoggingProcessor is launching!")
        val loggedClassesAnnotation =
            resolver.getSymbolsWithAnnotation(AnnotationClassLog::class.qualifiedName!!)
        val loggedMethodsAnnotation =
            resolver.getSymbolsWithAnnotation(AnnotationMethodLog::class.qualifiedName!!)
        println("MJ: ✅ I generated logging proxies for:")

        val annotatedMethods =
            MethodExtractor.extractMethods(loggedClassesAnnotation, loggedMethodsAnnotation)
        println(
            "MJ: annotated methods: ${
                annotatedMethods.values.flatten().map { it.simpleName.asString() }
            }"
        )
        ProxyGenerator.generateProxies(annotatedMethods)

        return emptyList()
    }

}
