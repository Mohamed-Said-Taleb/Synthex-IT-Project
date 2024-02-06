package com.devsling.fr.tools;

import com.devsling.fr.dto.EmployerDto;
import com.devsling.fr.model.Employer;
import org.springframework.beans.BeanUtils;

public class AppUtils {
    public static EmployerDto entityToDto(Employer employer) {
        EmployerDto employerDto = new EmployerDto();
        BeanUtils.copyProperties(employer, employerDto);
        return employerDto;
    }

    public static Employer dtoToEntity(EmployerDto employerDto) {
        Employer employer = new Employer();
        BeanUtils.copyProperties(employerDto, employer);
        return employer;
    }
}
