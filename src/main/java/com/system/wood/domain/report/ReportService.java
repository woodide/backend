package com.system.wood.domain.report;

import com.system.wood.domain.assigment.Assignment;
import com.system.wood.domain.student.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    public Long save(Report report) {
        Report savedReport = reportRepository.save(report);
        return savedReport.getId();
    }

    public String getReport(Student student, Assignment assignment) {
        Optional<Report> report = reportRepository.findByStudentAndAssignment(student, assignment);
        return report.isPresent() ? report.get().getContent() : "";
    }
}
