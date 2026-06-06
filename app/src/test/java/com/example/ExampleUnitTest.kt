package com.example

import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testMarkdownImageRegex() {
    val content = """
      Markdown Content:
      ![Image 1: پنل BPB](https://bia-pain-bache.github.io/BPB-Worker-Panel/fa/images/panel-overview.jpg)
      
      Some text.
      
      Images:
      - ![Image 1,2: logo](https://bia-pain-bache.github.io/BPB-Worker-Panel/fa/favicon.ico)
    """.trimIndent()

    val isRtlText = true
    val imageLabel = if (isRtlText) "تصویر" else "Image"

    // 1. Validate main content sanitization (removing visual image loading)
    val mainContentPart = content.substringBefore("Images:")
    val markdownImageRegex = """!\[(.*?)\]\((.*?)\)""".toRegex()
    val sanitizedMain = mainContentPart.replace(markdownImageRegex) { matchResult ->
      val altText = matchResult.groupValues[1].trim()
      if (altText.isNotEmpty()) {
        "[$imageLabel: $altText]"
      } else {
        "[$imageLabel]"
      }
    }

    println("SANITIZED MAIN:\n$sanitizedMain")
    assertTrue(sanitizedMain.contains("[تصویر: Image 1: پنل BPB]"))
    assertFalse(sanitizedMain.contains("!["))

    // 2. Validate summary link conversion (Keep links but convert ![]() to []() so they do not load visually)
    val summaryPart = "Images:\n" + content.substringAfter("Images:")
    val convertedSummary = summaryPart.replace(markdownImageRegex) { matchResult ->
      val altText = matchResult.groupValues[1].trim()
      val urlAndTitle = matchResult.groupValues[2].trim()
      val cleanUrl = urlAndTitle.substringBefore(" ").trim().trim('"', '\'')
      val cleanAltText = altText.replace("""(?i)^Image\s+\d+(,\d+)*:\s*""".toRegex(), "").trim()
      
      if (cleanAltText.isNotEmpty()) {
        "[$imageLabel: $cleanAltText]($cleanUrl)"
      } else {
        "[$imageLabel]($cleanUrl)"
      }
    }

    println("CONVERTED SUMMARY:\n$convertedSummary")
    assertTrue(convertedSummary.contains("[تصویر: logo](https://bia-pain-bache.github.io/BPB-Worker-Panel/fa/favicon.ico)"))
    assertFalse(convertedSummary.contains("!["))
  }
}

