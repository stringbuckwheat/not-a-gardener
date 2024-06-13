package xyz.notagardener.status.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import xyz.notagardener.status.model.StatusLog;
import xyz.notagardener.status.model.StatusType;

import java.util.List;
import java.util.Optional;

public interface StatusLogRepository extends Repository<StatusLog, Long> {
    StatusLog save(StatusLog statusLog);

    @Query(value = """
                SELECT ps.*
                FROM plant_status ps
                JOIN (
                    SELECT status AS sub_status, MAX(recorded_date) AS max_recorded_date
                    FROM plant_status
                    WHERE plant_id = :plantId
                    GROUP BY status
                ) AS sub
                ON ps.status = sub.sub_status 
                    AND ps.recorded_date = sub.max_recorded_date
                WHERE ps.plant_id = :plantId
                AND (
                    ps.create_date = (
                        SELECT MAX(create_date)
                        FROM plant_status ps2
                        WHERE ps2.status = ps.status
                        AND ps2.recorded_date = ps.recorded_date
                    )
                )
            """, nativeQuery = true)
    List<StatusLog> findCurrentStatusByPlantId(@Param("plantId") Long plantId);

    @Query(value = """
            SELECT ps.*
            FROM plant_status ps
            JOIN (
                SELECT plant_id, MAX(recorded_date) AS max_recorded_date
                FROM plant_status
                WHERE status = 'ATTENTION_PLEASE'
                GROUP BY plant_id
            ) AS sub
            ON ps.plant_id = sub.plant_id AND ps.recorded_date = sub.max_recorded_date
            JOIN plant p ON ps.plant_id = p.plant_id
            JOIN gardener g ON p.gardener_id = g.gardener_id
            WHERE ps.status = 'ATTENTION_PLEASE' AND g.gardener_id = :gardenerId
            GROUP BY ps.plant_id
             """, nativeQuery = true)
    List<StatusLog> findAttentionRequiredPlants(@Param("gardenerId") Long gardenerId);

    @EntityGraph(attributePaths = {"status", "status.plant", "status.plant.gardener"})
    Optional<StatusLog> findByStatusLogId(Long statusLogId);

    @EntityGraph(attributePaths = {"status", "status.plant"})
    List<StatusLog> findAllStatusLogByStatus_Plant_PlantIdAndStatus_Plant_Gardener_GardenerIdOrderByRecordedDateDescCreateDateDesc(Long plantId, Long gardenerId);

    Optional<StatusLog> findTopByStatus_Plant_PlantIdAndStatusTypeOrderByRecordedDateDescCreateDateDesc(Long plantId, StatusType statusType);

    void delete(StatusLog statusLog);
    void deleteByStatus_StatusId(Long statusLogId);
}
