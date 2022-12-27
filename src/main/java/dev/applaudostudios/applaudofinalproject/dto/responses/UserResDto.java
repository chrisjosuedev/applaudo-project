package dev.applaudostudios.applaudofinalproject.dto.responses;

import dev.applaudostudios.applaudofinalproject.entity.Address;
import dev.applaudostudios.applaudofinalproject.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResDto {
    private String sid;
    private String fullName;
    private String username;

    private String telephone;

    private Set<Address> address;

    private Set<Payment> payments;

}
