package com.solidarix.backend.repository;

import com.solidarix.backend.model.HelpFeedback;
import com.solidarix.backend.model.HelpIncidentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpIncidentReportRepository extends JpaRepository<HelpIncidentReport, Long> {

    List<HelpIncidentReport> findByHelpOfferId(Long helpOfferId);

}
