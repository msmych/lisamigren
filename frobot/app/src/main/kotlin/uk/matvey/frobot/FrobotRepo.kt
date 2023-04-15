package uk.matvey.frobot

import org.jooq.generated.Tables.FROBOT
import org.jooq.generated.tables.records.FrobotRecord
import uk.matvey.frobot.Frobot.BatteryLevel
import uk.matvey.frobot.Frobot.Id
import uk.matvey.persistence.AuditedEntityRepo
import uk.matvey.persistence.JooqRepo
import java.util.UUID

class FrobotRepo(jooqRepo: JooqRepo) : AuditedEntityRepo<Id, Frobot, UUID, FrobotRecord>(
    jooqRepo, FROBOT, FROBOT.ID, FROBOT.UPDATED_AT
) {

    fun findBy(userId: Long): Frobot? {
        return findAllWhere(FROBOT.USER_ID.eq(userId)).singleOrNull()
    }

    fun getBy(userId: Long): Frobot {
        return requireNotNull(findBy(userId))
    }

    override fun Frobot.toRecord(): FrobotRecord {
        return FrobotRecord(
            id.value,
            userId,
            batteryLevel.name,
            rockGardenMessageId?.toLong(),
            rockGardenBoard?.serialize(),
            createdAt,
            updatedAt
        )
    }

    override fun FrobotRecord.toEntity(): Frobot {
        return Frobot(
            Id(getId()),
            userId,
            BatteryLevel.valueOf(batteryLevel),
            rockGardenMessageId?.toInt(),
            rockGardenBoard?.let(RockGardenBoard::fromString),
            getCreatedAt(),
            getUpdatedAt()
        )
    }
}
