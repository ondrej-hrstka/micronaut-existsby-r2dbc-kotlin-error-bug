package com.example

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

@MicronautTest
class MicronautBrokenExistsR2dbcExampleTest {

    @Inject
    lateinit var recordReactiveRepository: RecordReactiveRepository

    @Inject
    lateinit var recordCoroutineRepository: RecordCoroutineRepository


    @Test
    fun `coroutines, non-existing, nullable`() = runBlocking {
        assertEquals(false, recordCoroutineRepository.existsByFoo(UUID.randomUUID()))
    }

    @Test
    fun `coroutines, non-existing, non-nullable`()= runBlocking {// returns null anyway
        val actual = recordCoroutineRepository.existsByBar(UUID.randomUUID())
        assertEquals(false, actual)
    }


    @Test
    fun `coroutines, existing`() = runBlocking {
        val record =  Record(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
        recordCoroutineRepository.save(record)
        assertEquals(true, recordCoroutineRepository.existsByFoo(record.foo))
        assertEquals(true, recordCoroutineRepository.existsByBar(record.bar))
    }

    @Test
    fun `reactive, non-existion, nullbable`() {
        assertEquals(false, recordReactiveRepository.existsByFoo(UUID.randomUUID()).block())
    }

    @Test
    fun `reactive, non-existion, non-nullbable`() {// returns null anyway
        val actual = recordReactiveRepository.existsByBar(UUID.randomUUID()).block()
        assertEquals(false, actual)
    }

    @Test
    fun `reactive, existing`() {
        val record =  Record(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
        recordReactiveRepository.save(record).block()
        assertEquals(true, recordReactiveRepository.existsByFoo(record.foo).block())
        assertEquals(true, recordReactiveRepository.existsByBar(record.bar).block())
    }


}
