package com.system.wood.infra;

import com.system.wood.domain.assigment.Assignment;
import com.system.wood.domain.container.Container;
import com.system.wood.domain.testcase.Testcase;
import com.system.wood.global.error.StorageException;
import com.system.wood.infra.dto.CompileDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GradingService {

    private final String cLang = "gcc";
    private final String python = "python";

    public String execute(Assignment assignment, Container container, String target) {
        String language = assignment.getLanguage();
        String result;

        if (language.equals(cLang)) {

            // 컴파일
            CompileDto compileDto = compileCLang(assignment, container, target);
            if (!compileDto.getCompileSuccess())
                return compileDto.getCompileResult();

            // 실행
            result = executeCLang(assignment, container, compileDto.getExeFileName());

        } else if (language.equals(python)) {
            result = executePython(assignment, target);
        } else {
            throw new RuntimeException("일치하는 언어가 없습니다.");
        }

        // 채점
        Float grade = grade(result, assignment.getTestcase());
        log.info("점수는 "+grade);
        return result;
    }

    private Float grade(String result, Testcase testcase) {
        String[] resultList = result.split(System.getProperty("line.separator"));
        List<String> cmpList;
        try {
            cmpList = Files.readAllLines(Path.of(testcase.getOutputUrl()));
        } catch (IOException e) {
            return 100F;
        }
        int length = resultList.length;
        int numberOfSameLine = 0;
        int max = Math.max(length, cmpList.size());
        for (int idx = 0; idx < length && idx < cmpList.size(); idx++) {
            if (resultList[idx].trim().equals(cmpList.get(idx).trim())) {
                numberOfSameLine++;
            }
        }

        return (float) numberOfSameLine * 100 / max;
    }

    private String executeCLang(Assignment assignment, Container container, String exeFileName) {
        // todo: 시간 초과 시 종료하는 로직 추가(executePython()에도 적용)
        try {
            StringBuilder stringBuilder = new StringBuilder();

            ProcessBuilder builder = new ProcessBuilder("./" + exeFileName)
                    .directory(new File(assignment.getUploadUrl()))
                    .redirectErrorStream(true);

            Process process = builder.start();

            InputStream processInputStream = process.getInputStream();
            OutputStream processOutputStream = process.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(processOutputStream));
            BufferedReader reader = new BufferedReader(new InputStreamReader(processInputStream));

            String content = Files.readString(Paths.get(assignment.getTestcase().getInputUrl()));
            writer.write(content);
            writer.flush();

            String s = null;
            while ((s = reader.readLine()) != null) {
                stringBuilder.append(s)
                        .append(System.getProperty("line.separator"));
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "실행 불가";
        }
    }

    private String executePython(Assignment assignment, String target) {
        try {
            StringBuilder stringBuilder = new StringBuilder();

            ProcessBuilder builder = new ProcessBuilder("python", target)
                    .directory(new File(assignment.getUploadUrl()))
                    .redirectErrorStream(true);
            Process process = builder.start();

            InputStream processInputStream = process.getInputStream();
            OutputStream processOutputStream = process.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(processOutputStream));
            BufferedReader reader = new BufferedReader(new InputStreamReader(processInputStream));

            String content = Files.readString(Paths.get(assignment.getTestcase().getInputUrl()));
            writer.write(content);
            writer.flush();

            String s = null;
            while ((s = reader.readLine()) != null) {
                stringBuilder.append(s)
                        .append(System.getProperty("line.separator"));
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "실행 불가";
        }
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
