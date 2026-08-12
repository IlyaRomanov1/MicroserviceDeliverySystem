# 🔐 Auth Service: Регистрация, Логин и JWT

Вначале пользователь отправляет запрос на регистрацию

```java
@RequestMapping("/auth") 
@PostMapping("/register")
```
с dto

```java
public class UserDTO {

    private String email;
    private String name;
    private Byte age;
    private String password;
}
```
в сервисе, dto преобразуется в user с помощью modelMapper, назначается роль ROLE_USER, шифруется пароль и результат сохраняется в бд

```java
@Transactional
public void register(UserDTO dto) {
    User user = modelMapper.map(dto, User.class);
    user.setRole(Role.ROLE_USER);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userRepository.save(user);
}
```

В секьюрити конфиг отключаем csrf и разрешаем досутп на регистрацию и логин

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/auth/register", "/auth/login").permitAll())
                .build();
    }
}
```

Далее пользователь должен залогиниться он отправляет запрос на

```java
@RequestMapping("/auth")
@PostMapping("/login")
```

с dto

```java
public class AuthRequestDTO {

    private String email;
    private String password;
}
```

В сервисе login вытаскиваем User по email сравниваем пароли если совпдают возвращаем response dto

```java
public class AuthResponseDTO {

    private Long id;
    private Role role;
}

public AuthResponseDTO login(AuthRequestDTO dto) {
    User user = userRepository.getUserByEmail(dto.getEmail());
    if (passwordEncoder.matches(dto.getPassword(), user.getPassword()))
        return modelMapper.map(user, AuthResponseDTO.class);
    return null;
}
```

Далее полученое response dto передается в класс JWTUtil который создает токен

В этот класс внедряется секретынй ключ (сгенерированный uuid) далее мы создаем токен – добавляем в него описание объекта, добавляем данные айди пользователя и роль, дату выпуска и кто выпускает, срок истечния и алгоритм шифрования

```java
@Value("jwt_secret")
private String secret;

public String generateToken(AuthResponseDTO dto) {
    Date expirationDate = Date.from(ZonedDateTime.now().plusHours(2L).toInstant());

    return JWT.create()
            .withSubject("Person details")
            .withClaim("userId", dto.getId())
            .withClaim("role", dto.getRole().toString())
            .withIssuedAt(new Date())
            .withIssuer("auth-service")
            .withExpiresAt(expirationDate)
            .sign(Algorithm.HMAC256(secret));
}
```

Далее в контроллере мы возрващаем мапу с токеном

```java
@PostMapping("/login")
public Map<String, String> login(@RequestBody AuthRequestDTO dto){
    AuthResponseDTO claims = authService.login(dto);
    String token = jwtUtil.generateToken(claims);
    return Map.of("jwt-token", token);
}
```