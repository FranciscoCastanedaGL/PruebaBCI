package com.francisco.castaneda.bcitest.model.dto;

import com.francisco.castaneda.bcitest.model.entity.Phone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

import static com.francisco.castaneda.bcitest.model.constants.CustomConstants.REG_EXP_FOR_EMAIL;
import static com.francisco.castaneda.bcitest.model.constants.CustomConstants.REG_EXP_FOR_PASSWORD;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private String  name;
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Size(max = 12, message = "Password don't be more than 12 characters long")
    @Pattern(regexp =REG_EXP_FOR_PASSWORD,message = "The password does not meet the acceptance standards. RegularExpression" )
    private String password;
    @Email(message = "Invalid email address for notation")
    @Pattern(regexp = REG_EXP_FOR_EMAIL , message = "Invalid email address for RegularExpression")
    private String email;
    private Set<Phone> phones;

}
