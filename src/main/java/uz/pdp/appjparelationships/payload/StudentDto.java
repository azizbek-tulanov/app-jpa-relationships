package uz.pdp.appjparelationships.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.entity.Subject;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private AddressDto addressDto;
    private Integer groupId;
    private List<Integer> subjectIds;
}
