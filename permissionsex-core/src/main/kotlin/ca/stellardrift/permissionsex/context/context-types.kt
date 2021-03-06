/*
 * PermissionsEx
 * Copyright (C) zml and PermissionsEx contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ca.stellardrift.permissionsex.context

import ca.stellardrift.permissionsex.config.PermissionsExConfiguration
import ca.stellardrift.permissionsex.subject.CalculatedSubject
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

/**
 * A placeholder context definition for implementations to use when a context query comes in for an unknown context
 */
class FallbackContextDefinition(name: String) : SimpleContextDefinition(name)

object ServerTagContextDefinition : PEXContextDefinition<String>("server-tag") {
    private var activeTags: List<String> = listOf()

    override fun serialize(userValue: String): String = userValue
    override fun deserialize(canonicalValue: String): String = canonicalValue
    override fun matches(ownVal: String, testVal: String): Boolean =
        ownVal == testVal

    override fun update(config: PermissionsExConfiguration<*>) {
        activeTags = config.serverTags
    }

    override fun accumulateCurrentValues(subject: CalculatedSubject, consumer: (value: String) -> Unit) {
        activeTags.forEach(consumer)
    }
}


open class TimeContextDefinition internal constructor(name: String) : PEXContextDefinition<LocalDateTime>(name) {
    private var currentTimeZone: ZoneId = ZoneId.systemDefault()
    override fun update(config: PermissionsExConfiguration<*>) {
        // TODO: implement timezone configuration option
    }

    override fun accumulateCurrentValues(subject: CalculatedSubject, consumer: (value: LocalDateTime) -> Unit) {
        consumer(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
    }

    override fun serialize(userValue: LocalDateTime): String {
        return userValue.toString()
    }

    override fun deserialize(canonicalValue: String): LocalDateTime {
        try {
            return LocalDateTime.parse(canonicalValue)
        } catch (ex: DateTimeParseException) {
            LocalDateTime.from(Instant.ofEpochSecond(canonicalValue.toLong()))
        }
        return LocalDateTime.now()
    }

    override fun matches(ownVal: LocalDateTime, testVal: LocalDateTime): Boolean {
        return testVal.truncatedTo(ChronoUnit.SECONDS)
            .isEqual(ownVal.truncatedTo(ChronoUnit.SECONDS))
    }
}

object BeforeTimeContextDefinition : TimeContextDefinition("before-time") {
    override fun matches(ownVal: LocalDateTime, testVal: LocalDateTime): Boolean {
        return testVal.truncatedTo(ChronoUnit.SECONDS)
            .isBefore(ownVal.truncatedTo(ChronoUnit.SECONDS))
    }
}

object AfterTimeContextDefinition : TimeContextDefinition("after-time") {
    override fun matches(ownVal: LocalDateTime, testVal: LocalDateTime): Boolean {
        return testVal.truncatedTo(ChronoUnit.SECONDS)
            .isAfter(ownVal.truncatedTo(ChronoUnit.SECONDS))
    }
}
