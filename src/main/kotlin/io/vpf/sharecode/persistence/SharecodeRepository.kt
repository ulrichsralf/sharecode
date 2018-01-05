package io.vpf.sharecode.persistence

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.stereotype.Repository
import java.util.*
import java.util.concurrent.TimeUnit


@Repository
class SharecodeRepository {

    val cache = Caffeine.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build<UUID, String>()


    fun putCode(key: UUID, value: String) {
        cache.put(key, value)
    }

    fun getCode(key: UUID): String? {
        return cache.getIfPresent(key)
    }
}