package team.three.usedstroller.api.gpt.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.three.usedstroller.api.gpt.entity.ReviewSummaryEntity;

public interface ReviewSummaryRepository extends JpaRepository<ReviewSummaryEntity, Long> {

  @Query(value = "SELECT * FROM review_summary WHERE model_id = :modelId ORDER BY RANDOM() LIMIT 3", nativeQuery = true)
  List<ReviewSummaryEntity> findRandom3ByModelId(@Param("modelId") Long model_id);
}
