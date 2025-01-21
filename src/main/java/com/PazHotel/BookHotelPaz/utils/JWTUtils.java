package com.PazHotel.BookHotelPaz.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTUtils {

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;; // 7 days

    /*
     * Clave secreta que se va a usar para firmar y verificar los tokens JWT, el
     * cual se genera en el constructor
     */
    private final SecretKey key;

    public JWTUtils() {
        /*
         * Es una cadena codificada en Base64 que actua como la clave secreta. Debe ser
         * segura y confidencial.
         */
        String secretKey = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
        /* Decodifica la cadena en Base64 para convertirla en un arreglo de bytes. */
        byte[] keyBytes = Base64.getDecoder().decode(secretKey.getBytes(StandardCharsets.UTF_8));
        /*
         * Crea una clave secreta a partir del arreglo de bytes y usando el algoritmo
         * HmacSHA256. Esta clave será usada para firmar y verificar los tokens JWT
         */
        this.key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public String generateToken(UserDetails userDetails) {
        /* Construye un token JWT */
        return Jwts.builder()
                /*
                 * Define al usuario que esta asociado con el token. Se usa el nombre del
                 * usuario
                 */
                .subject(userDetails.getUsername())
                /* Define la fecha de creación del token */
                .issuedAt(new Date(System.currentTimeMillis()))
                /* Define la fecha de expiración del token */
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                /*
                 * Firma el token usando la clave secreta generada anteriormente con el
                 * algoritmo HmacSHA256.
                 */
                .signWith(key)
                /* Construye y devuelve el token como una cadena. */
                .compact();
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
        /*
         * Aplica la función que se pasó como parámetro para extraer un dato específico
         * del token (por ejemplo, el nombre de usuario o la fecha de expiración).
         */
        return claimsTFunction
                .apply(
                    /* Inicializa el proceso de análisis del token JWT. */
                    Jwts.parser()
                    /* Usa la clave secreta para verificar la firma del token. */
                    .verifyWith(key)
                    .build()
                    /* Analiza el token y extrae los claims (informacion dentro del token) */
                    .parseSignedClaims(token)
                    /* Obtiene el payload del token */
                    .getPayload());
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

    public String extractUsername(String token) {
        /*
         * Obtiene el nombre de usuario del token y Claims::getSubject es una función
         * que devuelve el "subject" del token JWT, es decir el nombre del usuario.
         */
        return extractClaims(token, Claims::getSubject);
    }

    public boolean isValidToken(String token, UserDetails userDetails) {
        /* Extrae el nombre de usuario desde el token. */
        final String username = extractUsername(token);
        /* Compara el nombre de usuario dentro del token con el nombre de usuario del UserDetails proporcionado. */
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
