package br.com.OrderTrack.Common.security;

import java.util.List;
import java.util.UUID;

public record UserPrincipal(
        UUID id,
        String email,
        String name,
        List<String> roles
) {}