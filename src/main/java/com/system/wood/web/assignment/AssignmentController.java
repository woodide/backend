package com.system.wood.web.assignment;

import com.system.wood.infra.storage.StorageService;
import com.system.wood.web.assignment.dto.AssignmentReqDto;
import com.system.wood.web.container.dto.ResponseDto;
import com.system.wood.web.container.dto.ReturnStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AssignmentController {

    private final StorageService storageService;

    @ResponseBody
    @PostMapping(value = "/api/uploadfile")
    public ResponseEntity<ResponseDto> registerZipFile(AssignmentReqDto assignmentReqDto) {

        storageService.store(assignmentReqDto.getMultipartFile());

        return new ResponseEntity<>(ResponseDto.getSuccessDto(), HttpStatus.valueOf(200));
    }
}
