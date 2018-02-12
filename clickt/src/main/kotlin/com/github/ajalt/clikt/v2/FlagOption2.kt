package com.github.ajalt.clikt.v2

import com.github.ajalt.clikt.options.OptionParser
import com.github.ajalt.clikt.options.ParseResult
import com.github.ajalt.clikt.parser.BadOptionUsage
import kotlin.reflect.KParameter
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.starProjectedType


class FlagOptionParser2(names: List<String> = emptyList()) : OptionParser2<Boolean> {
    private val _rawValues = mutableListOf<Boolean>()
    override val rawValues: List<Boolean> get() = _rawValues
    private val offNames: List<String> = names.mapNotNull {
        if ("/" in it) {
            val split = it.split("/", limit = 2)
            split[1].let { if (it.isBlank()) null else it }
        } else null
    }

    override val repeatableForHelp: Boolean get() = false

    override fun parseLongOpt(name: String, argv: Array<String>, index: Int, explicitValue: String?): Int {
        if (explicitValue != null) throw BadOptionUsage("$name option does not take a value")
        _rawValues += name !in offNames
        return 1
    }

    override fun parseShortOpt(name: String, argv: Array<String>, index: Int, optionIndex: Int): Int {
        _rawValues += name !in offNames
        return if (optionIndex == argv[index].lastIndex) 1 else 0
    }
}