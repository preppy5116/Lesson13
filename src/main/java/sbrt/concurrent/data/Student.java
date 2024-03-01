package sbrt.concurrent.data;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class Student {
    private String firstName;
    private String secondName;
    private LocalDate dateOfBirth;
    private String address;
    private Double averageScore;
    private String faculty;
}
