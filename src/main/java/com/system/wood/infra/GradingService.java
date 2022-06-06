package com.system.wood.infra;

import com.system.wood.domain.assigment.Assignment;
import com.system.wood.domain.container.Container;
import com.system.wood.global.error.StorageException;
import com.system.wood.infra.dto.CompileDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GradingService {

    private final String cLang = "gcc";
    private final String python = "python";

    public String execute(Assignment assignment, Container container, String target) {
        String language = assignment.getLanguage();

        if (language.equals(cLang)) {

            // 컴파일
            CompileDto compileDto = compileCLang(assignment, container, target);
            if (!compileDto.getCompileSuccess())
                return compileDto.getCompileResult();
            return "컴파일 성공";

        } else if (language.equals(python)) {
            return executePython(assignment);
        }
        return "일치하는 언어가 없음";
    }

    private String executePython(Assignment assignment) {
        return "python";
    }

    private CompileDto compileCLang(Assignment assignment, Container container, String target) {
        String exeFileName = container.getStudent().getStudentNumber()+"exe";
        String command = new StringBuilder("gcc ")
                            .append(target)
                            .append(" -o ")
                            .append(exeFileName)
                            .toString();

        try {
            Process exec = Runtime.getRuntime().exec(command, new String[0], new File(assignment.getUploadUrl()));
            String compileErrorLog = new BufferedReader(
                    new InputStreamReader(exec.getErrorStream(), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining());
            if (!compileErrorLog.isEmpty())
                return new CompileDto(compileErrorLog, "", false);
            else {
                log.info("컴파일 성공");
                return new CompileDto("",exeFileName,  true);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new StorageException("컴파일 중 시스템 오류 발생");
        }
    }
}
