package org.example.TD3;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Student(
        @JsonProperty("Reference") String reference,
        @JsonProperty("FirstName") String firstName,
        @JsonProperty("LastName") String lastName,
        @JsonProperty("Age") int age
) {}

