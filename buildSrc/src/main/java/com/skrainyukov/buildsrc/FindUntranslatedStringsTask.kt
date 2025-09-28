package com.skrainyukov.buildsrc

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

abstract class FindUntranslatedStringsTask : DefaultTask() {

    @TaskAction
    fun findUntranslatedStrings() {
        val resDir = File(project.projectDir, "src/main/res")

        // Получаем default strings.xml
        val defaultStringsFile = File(resDir, "values/strings.xml")
        if (!defaultStringsFile.exists()) {
            logger.warn("Default strings.xml not found")
            return
        }

        // Парсим ресурсы из default strings.xml
        val defaultResources = parseStringResources(defaultStringsFile)
        logger.lifecycle("Found ${defaultResources.size} default string resources")

        // Находим все каталоги values-*
        val valueDirs = resDir.listFiles { file ->
            file.isDirectory && file.name.startsWith("values-")
        } ?: emptyArray()

        logger.lifecycle("Found ${valueDirs.size} localized directories")

        val missingTranslations = mutableMapOf<String, List<String>>()

        // Для каждого каталога values-* проверяем ресурсы
        for (valueDir in valueDirs) {
            val localeStringsFile = File(valueDir, "strings.xml")
            if (!localeStringsFile.exists()) {
                continue
            }

            val localeResources = parseStringResources(localeStringsFile)
            val localeName = valueDir.name

            // Находим отсутствующие переводы
            val missingStrings = defaultResources - localeResources
            if (missingStrings.isNotEmpty()) {
                missingTranslations[localeName] = missingStrings.toList()
                logger.warn("$localeName: missing ${missingStrings.size} translations")
            }
        }

        // Генерируем исключение если есть непереведенные строки
        if (missingTranslations.isNotEmpty()) {
            val stringBuilderErrorText = StringBuilder("Missing translations").append(System.lineSeparator())
            missingTranslations.forEach { (locale, missingStrings) ->
                stringBuilderErrorText
                    .append("=== $locale ===")
                    .append(System.lineSeparator())
                    .append(missingStrings.joinToString(separator = System.lineSeparator()))
                    .append(System.lineSeparator())
            }
            throw GradleException(stringBuilderErrorText.toString())
        } else {
            logger.lifecycle("✓ All translations are complete!")
        }
    }

    private fun parseStringResources(file: File): Set<String> {
        return try {
            val stringsFromXml = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(file)
                .getElementsByTagName("string")

            stringsFromXml.let { nodeList ->
                (0 until nodeList.length).map { i ->
                    val node = nodeList.item(i)
                    val name = node.attributes?.getNamedItem("name")?.nodeValue ?: ""
                    name
                }.toSet()
            }
        } catch (e: Exception) {
            logger.warn("Failed to parse ${file.absolutePath}: ${e.message}")
            emptySet()
        }
    }
}