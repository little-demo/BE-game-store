package antran.project.Service;

import antran.project.DTO.Request.AuthenticationRequest;
import antran.project.DTO.Request.IntrospectRequest;
import antran.project.DTO.Request.LogoutRequest;
import antran.project.DTO.Request.RefreshTokenRequest;
import antran.project.DTO.Response.AuthenticationResponse;
import antran.project.DTO.Response.IntrospectResponse;
import antran.project.Entity.InvalidatedToken;
import antran.project.Repository.InvalidatedTokenRepository;
import antran.project.Entity.User;
import antran.project.Exception.AppException;
import antran.project.Exception.ErrorCode;
import antran.project.Repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal // Lombok annotation to disable final for this field
    @Value("${jwt.signerKey}")
    protected String SIGNING_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        boolean isValid = true;
        try {
            verifyToken(token, false); // Verify the JWT token
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!user.getEnabled()) {
            throw new AppException(ErrorCode.USER_DISABLED); // cần thêm mã lỗi
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String token = generateToken(user); // Generate JWT token

        return AuthenticationResponse.builder()
                .token(token)
                .isAuthenticated(true)
                .build();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken(), true); // Verify the JWT token

        var jti = signToken.getJWTClaimsSet().getJWTID();
        var expirationTime = signToken.getJWTClaimsSet().getExpirationTime();

        // Remove the old token
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jti) // Set the JWT ID
                .expiryTime(expirationTime) // Set the expiration time
                .build();

        invalidatedTokenRepository.save(invalidatedToken);

        var user = userRepository.findByUsername(signToken.getJWTClaimsSet().getSubject())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        String token = generateToken(user); // Generate JWT token

        return AuthenticationResponse.builder()
                .token(token)
                .isAuthenticated(true)
                .build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            var signToken = verifyToken(request.getToken(), true); // Verify the JWT token

            String jti = signToken.getJWTClaimsSet().getJWTID(); // Get the JWT ID from the claims
            Date expirationTime = signToken.getJWTClaimsSet().getExpirationTime(); // Get the expiration time

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jti) // Set the JWT ID
                    .expiryTime(expirationTime) // Set the expiration time
                    .build();

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException e) {
            log.error("Token already expired");
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNING_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token); // Parse the JWT token

        Date  expirationTime = (isRefresh)
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime()
                    .toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expirationTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        return signedJWT;
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512); // Specify the algorithm

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder() // Create a JWT claims set
                .subject(user.getUsername())
                .issuer("antran.project") //usually the issuer is your application name or URL
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                )) // Token valid for 1 hour
                .jwtID(UUID.randomUUID().toString()) // Unique identifier for the token
                .claim("scope", buildScope(user)) // Add custom claims if needed, here we add user roles as scope
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject()); // Create a payload with the claims

        JWSObject jwsObject = new JWSObject(header, payload); // Create a JWS object with the header and payload

        //sign JWS object with a MAC signer
        try {
            jwsObject.sign(new MACSigner(SIGNING_KEY.getBytes()));
            return jwsObject.serialize(); // Serialize the JWS object to a compact string
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    //get the scope from user roles
    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add(role.getName());
            });
        }
        return stringJoiner.toString();
    }
}
