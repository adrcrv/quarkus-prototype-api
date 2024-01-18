package dev.adrcrv.service;

import dev.adrcrv.dto.TextManagementGetReqDTO;
import dev.adrcrv.dto.TextManagementGetResDTO;
import dev.adrcrv.dto.TextManagementPostReqDTO;
import dev.adrcrv.dto.TextManagementPostResDTO;

public interface ITextManagementService {
    TextManagementGetResDTO getByParams(TextManagementGetReqDTO params) throws Exception;

    TextManagementPostResDTO create(TextManagementPostReqDTO body) throws Exception;
}
