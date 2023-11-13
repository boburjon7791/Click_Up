package com.example.demo.payloads.auth_user;

import com.example.demo.payloads.card.CardGetDto;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthUserGetDto {
   public Long id;

   public String firstName;

   public String lastName;

   public String fatherName;

   public LocalDate birthdate;

   public Boolean confirmed;

   public String phoneNumber;

   public String email;

   public Set<String> roles;

   public String profileImage;

   public CardGetDto mainCard;

   public Set<CardGetDto> cards;
}
