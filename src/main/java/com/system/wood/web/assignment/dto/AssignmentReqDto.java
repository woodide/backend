package com.system.wood.web.assignment.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class AssignmentReqDto {

    private MultipartFile multipartFile;
}
