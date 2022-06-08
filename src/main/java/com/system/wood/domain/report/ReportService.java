package com.system.wood.domain.report;

import com.system.wood.domain.assigment.Assignment;
import com.system.wood.domain.student.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    @Transactional
    public Long saveOrUpdate(Report report) {
        Optional<Report> optionalReport = reportRepository.findByStudentAndAssignment(report.getStudent(), report.getAssignment());
        if (optionalReport.isPresent()) {
            Report savedReport = optionalReport.get();
            savedReport.updateContent(report.getContent());
            return savedReport.getId();
        } else {
            Report savedReport = reportRepository.save(report);
            return savedReport.getId();
        }
    }

    public Optional<Report> getReport(Student student, Assignment assignment) {
        return reportRepository.findByStudentAndAssignment(student, assignment);
    }
}
