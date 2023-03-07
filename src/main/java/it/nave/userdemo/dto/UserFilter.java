package it.nave.userdemo.dto;

public record UserFilter(String name,
                         String surname,
                         String mail,
                         String address) {
}
