package com.hotsix.titans.electronicApproval.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EARetireDTO extends EADocumentDTO {

    private Date retireDate;
}