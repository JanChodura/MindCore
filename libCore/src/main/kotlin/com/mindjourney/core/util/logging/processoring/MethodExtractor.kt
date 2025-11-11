package com.mindjourney.core.util.logging.processoring

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration

object MethodExtractor {

    fun extractMethods(
        loggedClassesAnnotation: Sequence<KSAnnotated>,
        loggedMethodsAnnotation: Sequence<KSAnnotated>
    ): MutableMap<KSClassDeclaration, MutableList<KSFunctionDeclaration>> {
        val allLoggedMethods =
            mutableMapOf<KSClassDeclaration, MutableList<KSFunctionDeclaration>>()

        addLoggedClassesMethods(loggedClassesAnnotation, allLoggedMethods)
        addLoggedMethods(loggedMethodsAnnotation, allLoggedMethods)

        return allLoggedMethods
    }

    /**
     * It adds all methods from classes annotated with @ClassLog to the provided map.
     */
    private fun addLoggedClassesMethods(
        allClasses: Sequence<KSAnnotated>,
        methodsByClass: MutableMap<KSClassDeclaration, MutableList<KSFunctionDeclaration>>
    ) {
        allClasses.forEach { classDeclaration ->
            if (classDeclaration is KSClassDeclaration) {
                val methods = classDeclaration.declarations
                    .filterIsInstance<KSFunctionDeclaration>()
                    .toMutableList()

                methodsByClass[classDeclaration] = methods
            }
        }
    }

    /**
     * It adds all methods annotated with @MethodLog to the provided map.
     */
    private fun addLoggedMethods(
        allMethods: Sequence<KSAnnotated>,
        methodsByClass: MutableMap<KSClassDeclaration, MutableList<KSFunctionDeclaration>>
    ) {
        allMethods.forEach { method ->
            val parentClass = method.parent as? KSClassDeclaration ?: return@forEach
            methodsByClass.getOrPut(parentClass) { mutableListOf() }
                .add(method as KSFunctionDeclaration)
        }
    }
}
