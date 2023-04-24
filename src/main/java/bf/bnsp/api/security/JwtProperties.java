package bf.bnsp.api.security;

/**
 * @author Berickal
 */
public class JwtProperties {
    public static final String SECRET = "BNSP_H@5hing_5@LT";
    public static final int EXPIRATION_TIME = 86_400_000; // 1 day
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
