package com.samuraism.maven2gradle

import org.jdom2.Element
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory
import org.jdom2.filter.ElementFilter
import org.jdom2.input.DOMBuilder

class Maven2Gradle(val generateKts: Boolean = false) {
    fun maven2gradle(pom: String): String {
        val factory = DocumentBuilderFactory.newInstance()
        val documentBuilder = factory.newDocumentBuilder()
        val w3cDocument = documentBuilder.parse(InputSource(StringReader("<root>$pom</root>")))
        val document = DOMBuilder().build(w3cDocument)
        val result = StringBuilder()
        val dependencies = document.getDescendants(ElementFilter("dependency"))
        for (dependency in dependencies) {
            result.append(convertDependency(dependency))
        }
        return result.toString()
    }

    private fun convertDependency(dependency: Element): String {
        val groupId = findOrThrow(dependency, "groupId")
        val artifactId = findOrThrow(dependency, "artifactId")
        val version = findOrThrow(dependency, "version")
        val gradleScope = when (find(dependency, "scope", "compile")) {
            "compile" -> "implementation"
            "provided" -> "compileOnly"
            "test" -> "testImplementation"
            else -> "implementation"
        }

        val exclusionsIterator = dependency.getDescendants(ElementFilter("exclusions"))
        return if (exclusionsIterator.hasNext()) {
            val exclusion = excludesToString(exclusionsIterator.next())
            if(generateKts){
                "$gradleScope(\"$groupId:$artifactId:$version\") $exclusion"
            }else {
                "$gradleScope('$groupId:$artifactId:$version') $exclusion"
            }

        } else {
            if(generateKts){
                "$gradleScope(\"$groupId:$artifactId:$version\")"
            }else{
                "$gradleScope '$groupId:$artifactId:$version'"
            }
        }
    }

    private fun excludesToString(excludesElement: Element): String {
        val result = StringBuilder("{\n")
        var count = 0
        val excludes = excludesElement.getDescendants(ElementFilter("exclusion"))
        for (exclude in excludes) {
            result.append(convertExclude(exclude as Element))
            count++
        }
        result.append("}")
        return if (count == 0) {
            ""
        } else {
            result.toString()
        }
    }

    private fun convertExclude(exclude: Element): String {
        val groupId = findOrThrow(exclude, "groupId")
        val artifactId = findOrThrow(exclude, "artifactId")
        return if(generateKts){
            """    exclude(group = "$groupId", module = "$artifactId")
        |""".trimMargin()

        }else {

            """    exclude group: $groupId, module: "$artifactId"
        |""".trimMargin()
        }
    }


    private fun findOrThrow(content: Element, tag: String): String {
        val tagName = content.getDescendants(ElementFilter(tag))
        for (element in tagName) {
            return element.text.trim()
        }
        throw IllegalArgumentException("$tag not found")
    }

    private fun find(content: Element, @Suppress("SameParameterValue") tag: String,
                     @Suppress("SameParameterValue") fallback: String): String {
        val tagName = content.getDescendants(ElementFilter(tag))
        for (element in tagName) {
            return element.text.trim()
        }
        return fallback
    }

}