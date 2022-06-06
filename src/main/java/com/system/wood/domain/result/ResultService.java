package com.system.wood.domain.result;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResultService {

    private final ResultRepository gradeRepository;

    public Long save(Result result) {
        Result savedGrade = gradeRepository.save(result);
        return savedGrade.getId();
    }
}
