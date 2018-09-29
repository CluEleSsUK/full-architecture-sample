package cluelessuk.eventretention

import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn
import org.springframework.data.cassandra.core.mapping.Table
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.sql.Timestamp
import java.util.*

@Table("analytics")
data class AnalyticsEventEntity(
    @PrimaryKeyColumn(
        name = "eventId",
        ordinal = 0,
        type = PrimaryKeyType.PARTITIONED)
    val eventId: UUID,

    @Column
    val clientId: String,

    @Column
    val action: String,

    @Column
    val time: Timestamp
)

interface CassandraRepository: ReactiveCassandraRepository<AnalyticsEventEntity, String> {

}

@Component
class CassandraPermanentStore(private val repository: CassandraRepository): PermanentStore {

  override fun persist(event: AnalyticsEvent): Mono<Void> {
    return Mono.just(event)
        .map { AnalyticsEventEntity(UUID.randomUUID(), it.clientId, it.action, Timestamp.from(it.time)) }
        .flatMap { repository.save(it).then() }
  }

}